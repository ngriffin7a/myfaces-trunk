package net.sourceforge.myfaces.component.html.ext;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.*;
import javax.servlet.jsp.jstl.sql.Result;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Reimplement all UIData functionality to be able to have (protected) access
 * the internal DataModel.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class HtmlDataTableHack
        extends javax.faces.component.html.HtmlDataTable
{
    protected DataModel _dataModel;

    //init to false, so that no descendant states are saved for a newly created UIData
    transient protected boolean _saveDescendantStates = false;


    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Every field and method from here is identical to UIData !!!!!!!!!

    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

    private static final Integer INTEGER_MINUS1 = new Integer(-1);

    private int _rowIndex = -1;
    private Object[] _descendantStates;
    private int _descendantEditableValueHolderCount = -1;


    public boolean isRowAvailable()
    {
        return getDataModel().isRowAvailable();
    }

    public int getRowCount()
    {
        return getDataModel().getRowCount();
    }

    public Object getRowData()
    {
        return getDataModel().getRowData();
    }

    public int getRowIndex()
    {
        return _rowIndex;
    }

    public void setRowIndex(int rowIndex)
    {
        saveDescendantComponentStates();

        _rowIndex = rowIndex;

        DataModel dataModel = getDataModel();
        dataModel.setRowIndex(rowIndex);

        String var = getVar();
        if (rowIndex == -1)
        {
            if (var != null)
            {
                getFacesContext().getExternalContext().getRequestMap().remove(var);
            }
        }
        else
        {
            if (var != null)
            {
                if (isRowAvailable())
                {
                    Object rowData = dataModel.getRowData();
                    getFacesContext().getExternalContext().getRequestMap().put(var, rowData);
                }
                else
                {
                    getFacesContext().getExternalContext().getRequestMap().remove(var);
                }
            }
        }

        restoreDescendantComponentStates();
    }


    private int getDescendantStatesRowIndex()
    {
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
        {
            return 0;
        }
        else
        {
            return rowIndex - getFirst() + 1;
        }
    }

    /**
     * The descendant Component states algorithm we implement here is pretty fast
     * but does not support modification of the components tree during the lifecycle.
     * TODO: should we offer an alternative implementation with a clientId based Map ?
     */
    private void saveDescendantComponentStates()
    {
        if (_descendantEditableValueHolderCount == -1)
        {
            //This is the first time we save the descendant components state
            refreshDescendantDataStates();
        }
        else if (_descendantEditableValueHolderCount == 0)
        {
            //There are no EditableValueHolder children
            return;
        }
        else
        {
            int rowIndex = getDescendantStatesRowIndex();
            EditableValueHolderState[] rowState = null;
            // make sure that the underlying data did not change size
            // (i.e. someone added a row to the DataModel)
            // BUG: #925693
            if(rowIndex < _descendantStates.length) {
                rowState = (EditableValueHolderState[])_descendantStates[rowIndex];
            } else {
                // changed size during the lifecycle - should refresh
                refreshDescendantDataStates();
                rowState = (EditableValueHolderState[])_descendantStates[rowIndex];
            }
            if (rowState == null)
            {
                rowState = new EditableValueHolderState[_descendantEditableValueHolderCount];
                _descendantStates[rowIndex] = rowState;
            }
            saveDescendantComponentStates(this, rowState, 0);
        }
    }

    private void refreshDescendantDataStates() {
        List list = new ArrayList();
        saveDescendantComponentStates(this, list);
        _descendantEditableValueHolderCount = list.size();
        if (_descendantEditableValueHolderCount > 0)
        {
            EditableValueHolderState[] rowState
                    = (EditableValueHolderState[])list.toArray(new EditableValueHolderState[list.size()]);
            int rows = getRows();
            if (rows <= 0)
            {
                rows = getRowCount() - getFirst();
            }
            _descendantStates = new Object[rows + 1];
            int rowIndex = getDescendantStatesRowIndex();
            _descendantStates[rowIndex] = rowState;
        }
    }

    private static void saveDescendantComponentStates(UIComponent component, List list)
    {
        for (Iterator it = component.getChildren().iterator(); it.hasNext();)
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof EditableValueHolder)
            {
                list.add(new EditableValueHolderState((EditableValueHolder)child));
            }
            saveDescendantComponentStates(child, list);
        }
    }

    private static int saveDescendantComponentStates(UIComponent component,
                                                      EditableValueHolderState[] states,
                                                      int counter)
    {
        for (Iterator it = component.getChildren().iterator(); it.hasNext();)
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof EditableValueHolder)
            {
                states[counter++] = new EditableValueHolderState((EditableValueHolder)child);
            }
            counter = saveDescendantComponentStates(child, states, counter);
        }
        return counter;
    }

    private void restoreDescendantComponentStates()
    {
        if (_descendantEditableValueHolderCount == -1)
        {
            throw new IllegalStateException("saveDescendantComponentStates not called yet?");
        }
        else if (_descendantEditableValueHolderCount > 0)
        {
            // There is at least one descendant component to be restored

            // Get zero-based index (instead of -1 based UIData zeroBasedRowIdx):
            int zeroBasedRowIdx = getDescendantStatesRowIndex();

            // Is there a reason to restore the state of a new descendant?
            // BUG: 925693
            // manolito: Yes, descendants for a row not yet saved, must be
            //           reset to initial row state!
            int stateRowsCount = _descendantStates.length;

            EditableValueHolderState[] initialStates = null;
            if (stateRowsCount > 0)
            {
                // No state saved yet for this row, let's restore initial values:
                initialStates = (EditableValueHolderState[]) _descendantStates[0];
            }

            if (zeroBasedRowIdx < stateRowsCount)
            {
                // There is a saved state for this row, so restore these values:
                EditableValueHolderState[] rowState =
                    (EditableValueHolderState[]) _descendantStates[zeroBasedRowIdx];
                restoreDescendantComponentStates(this, rowState, initialStates, 0);
            }
            else
            {
                // No state saved yet for this row, let's restore initial values:
                restoreDescendantComponentStates(this, initialStates, initialStates, 0);
            }
        }
        else
        {
            // There are no states to restore, so only recurse to set the
            // right clientIds for all descendants
            restoreDescendantComponentStates(this, null, null, 0);
        }
    }

    private static int restoreDescendantComponentStates(UIComponent component,
                                                        EditableValueHolderState[] states,
                                                        EditableValueHolderState[] initialStates,
                                                        int counter)
    {
        for (Iterator it = component.getChildren().iterator(); it.hasNext();)
        {
            UIComponent child = (UIComponent)it.next();
            //clear this descendant's clientId:
            child.setId(child.getId()); //HACK: This assumes that setId always clears the cached clientId. Can we be sure?
            if (child instanceof EditableValueHolder)
            {
                if (states != null)
                {
                    states[counter].restore((EditableValueHolder)child);
                }
                else if (initialStates != null)
                {
                    initialStates[counter].restore((EditableValueHolder)child);
                }
                else
                {
                    // No state saved yet and no initial state !?
                    // Should never be possible, but let's reset the component
                    // state to null values
                    ((EditableValueHolder)child).setValue(null);
                    ((EditableValueHolder)child).setLocalValueSet(false);
                    ((EditableValueHolder)child).setValid(true);
                    ((EditableValueHolder)child).setSubmittedValue(null);
                }
                counter++;
            }
            counter = restoreDescendantComponentStates(child, states, initialStates, counter);
        }
        return counter;
    }


    public void setValueBinding(String name,
                                ValueBinding binding)
    {
        if (name == null)
        {
            throw new NullPointerException("name");
        }
        else if (name.equals("value"))
        {
            _dataModel = null;
        }
        else if (name.equals("var") || name.equals("rowIndex"))
        {
            throw new IllegalArgumentException("name " + name);
        }
        super.setValueBinding(name, binding);
    }





    private DataModel getDataModel()
    {
        if (_dataModel == null)
        {
            Object value = getValue();
            if (value == null)
            {
                _dataModel = EMPTY_DATA_MODEL;
            }
            else if (value instanceof DataModel)
            {
                _dataModel = (DataModel)value;
            }
            else if (value instanceof List)
            {
                _dataModel = new ListDataModel((List)value);
            }
            else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass()))
            {
                _dataModel = new ArrayDataModel((Object[])value);
            }
            else if (value instanceof ResultSet)
            {
                _dataModel = new ResultSetDataModel((ResultSet)value);
            }
            else if (value instanceof Result)
            {
                _dataModel = new ResultDataModel((Result)value);
            }
            else
            {
                _dataModel = new ScalarDataModel(value);
            }
        }
        return _dataModel;
    }


    private static final DataModel EMPTY_DATA_MODEL = new DataModel()
    {
        public boolean isRowAvailable()
        {
            return false;
        }

        public int getRowCount()
        {
            return 0;
        }

        public Object getRowData()
        {
            throw new IllegalArgumentException();
        }

        public int getRowIndex()
        {
            return -1;
        }

        public void setRowIndex(int i)
        {
            if (i < -1) throw new IllegalArgumentException();
        }

        public Object getWrappedData()
        {
            return null;
        }

        public void setWrappedData(Object obj)
        {
            if (obj == null) return; //Clearing is allowed
            throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
        }
    };


    private static class EditableValueHolderState
            implements Serializable
    {
        private Object _localValue;
        private boolean _localValueSet;
        private boolean _valid;
        private Object _submittedValue;

        public EditableValueHolderState(EditableValueHolder vh)
        {
            _localValue = vh.getLocalValue();
            _localValueSet = vh.isLocalValueSet();
            _valid = vh.isValid();
            _submittedValue = vh.getSubmittedValue();
        }

        public void restore(EditableValueHolder vh)
        {
            vh.setValue(_localValue);
            vh.setLocalValueSet(_localValueSet);
            vh.setValid(_valid);
            vh.setSubmittedValue(_submittedValue);
        }
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = _saveDescendantStates ? _descendantStates : null;
        values[2] = _saveDescendantStates ? new Integer(_descendantEditableValueHolderCount) : INTEGER_MINUS1;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _descendantStates = (Object[])values[1];
        _descendantEditableValueHolderCount = ((Integer)values[2]).intValue();
    }

}

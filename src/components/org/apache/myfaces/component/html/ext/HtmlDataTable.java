/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.component.html.ext;

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.component.UserRoleUtils;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.10  2004/07/01 21:53:05  mwessendorf
 * ASF switch
 *
 * Revision 1.9  2004/06/22 14:33:29  royalts
 * no message
 *
 * Revision 1.8  2004/06/21 16:01:57  royalts
 * setSortAscending(...) and setSortColumn have to update model their own, because processUdates won't be executed.
 *
 * Revision 1.7  2004/06/21 14:43:20  manolito
 * no more calls to getRowCount to determine if list is empty before encodeBegin was called
 *
 * Revision 1.6  2004/06/21 12:15:29  manolito
 * encodeBegin in UIData examines descendants valid flag recursivly now before refreshing DataModel
 *
 * Revision 1.5  2004/05/21 10:39:26  manolito
 * new renderedIfEmpty attribute in ext. HtmlDataTable component
 *
 * Revision 1.4  2004/05/18 14:31:36  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.3  2004/05/18 11:22:44  manolito
 * optimized local value handling, so that getValue of UIData is only called when needed
 *
 */
public class HtmlDataTable
        extends javax.faces.component.html.HtmlDataTable
        implements UserRoleAware
{
    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

    transient private _SerializableDataModel _restoredValue = null;
    transient private Object _cachedValue = null;

    //Flag to detect if component is rendered for the first time (restoreState sets it to false)
    transient private boolean _firstTimeRendered = true;

    private String _sortColumn = null;
    private Boolean _sortAscending = null;

    public Object getValue()
    {
        if (_cachedValue == null)
        {
            if (_restoredValue != null)
            {
                // if we have a restored value we use this value
                _cachedValue = _restoredValue;
            }
            else
            {
                // else get value from model
                _cachedValue = super.getValue();
            }
        }
        return _cachedValue;
    }


    public void setValue(Object value)
    {
        super.setValue(value);

        // clear the restored value
        _restoredValue = null;

        // and update the cached value
        _cachedValue = value;
    }


    public void processUpdates(FacesContext context)
    {
        super.processUpdates(context);

        if (_restoredValue != null)
        {
            updateModelFromPreservedDataModel(context);
        }

        if (isPreserveSort())
        {
            if (_sortColumn != null)
            {
                ValueBinding vb = getValueBinding("sortColumn");
                if (vb != null)
                {
                    vb.setValue(context, _sortColumn);
                    _sortColumn = null;
                }
            }

            if (_sortAscending != null)
            {
                ValueBinding vb = getValueBinding("sortAscending");
                if (vb != null)
                {
                    vb.setValue(context, _sortAscending);
                    _sortAscending = null;
                }
            }
        }
    }


    private void updateModelFromPreservedDataModel(FacesContext context)
    {
        ValueBinding vb = getValueBinding("value");
        if (vb != null && !vb.isReadOnly(context))
        {
            _SerializableDataModel dm = (_SerializableDataModel)_restoredValue;
            Class type = vb.getType(context);
            if (DataModel.class.isAssignableFrom(type))
            {
                vb.setValue(context, dm);
            }
            else if (List.class.isAssignableFrom(type))
            {
                vb.setValue(context, (List)dm.getWrappedData());
            }
            else if (OBJECT_ARRAY_CLASS.isAssignableFrom(type))
            {
                List lst = (List)dm.getWrappedData();
                vb.setValue(context, lst.toArray(new Object[lst.size()]));
            }
            else if (ResultSet.class.isAssignableFrom(type))
            {
                throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
            }
            else
            {
                //Assume scalar data model
                List lst = (List)dm.getWrappedData();
                if (lst.size() > 0)
                {
                    vb.setValue(context, lst.get(0));
                }
                else
                {
                    vb.setValue(context, null);
                }
            }
        }
    }


    /**
     * TODO: We could perhaps optimize this if we know we are derived from MyFaces UIData implementation
     */
    private boolean isAllChildrenAndFacetsValid()
    {
        int first = getFirst();
        int rows = getRows();
        int last;
        if (rows == 0)
        {
            last = getRowCount();
        }
        else
        {
            last = first + rows;
        }
        try
        {
            for (int rowIndex = first; rowIndex < last; rowIndex++)
            {
                setRowIndex(rowIndex);
                if (isRowAvailable())
                {
                    if (!isAllEditableValueHoldersValidRecursive(getFacetsAndChildren()))
                    {
                        return false;
                    }
                }
            }
        }
        finally
        {
            setRowIndex(-1);
        }
        return true;
    }


    private boolean isAllEditableValueHoldersValidRecursive(Iterator facetsAndChildrenIterator)
    {
        while (facetsAndChildrenIterator.hasNext())
        {
            UIComponent c = (UIComponent)facetsAndChildrenIterator.next();
            if (c instanceof EditableValueHolder &&
                !((EditableValueHolder)c).isValid())
            {
                return false;
            }
            if (!isAllEditableValueHoldersValidRecursive(c.getFacetsAndChildren()))
            {
                return false;
            }
        }
        return true;
    }


    public void encodeBegin(FacesContext context) throws IOException
    {
        if (_firstTimeRendered || isAllChildrenAndFacetsValid())
        {
            // No invalid children
            // --> clear restored and cached value
            _restoredValue = null;
            _cachedValue = null;
        }
        if (isRenderedIfEmpty() || getRowCount() > 0)
        {
            super.encodeBegin(context);
        }
    }

    public void encodeChildren(FacesContext context) throws IOException
    {
        if (isRenderedIfEmpty() || getRowCount() > 0)
        {
            super.encodeChildren(context);
        }
    }

    public void encodeEnd(FacesContext context) throws IOException
    {
        if (isRenderedIfEmpty() || getRowCount() > 0)
        {
            super.encodeEnd(context);
        }
    }

    // TODO: manolito still need this ?? (royalts)
    public int getFirst()
    {
        if (_restoredValue != null)
        {
            return _restoredValue.getFirst();
        }
        return super.getFirst();
    }

    // TODO: manolito still need this ?? (royalts)
    public void setFirst(int first)
    {
        if (_restoredValue != null)
        {
            _restoredValue.setFirst(first);
        }
        super.setFirst(first);
    }

    // TODO: manolito still need this ?? (royalts)
    // getRows(...) without overwriting setRows might couse problems
    // see setFirst(...)
    public int getRows()
    {
        if (_restoredValue != null)
        {
            return _restoredValue.getRows();
        }
        return super.getRows();
    }

    public int getRowCount()
    {
        if (_restoredValue != null)
        {
            return _restoredValue.getRowCount();
        }
        return super.getRowCount();
    }

    public boolean isRowAvailable()
    {
        if (_restoredValue != null)
        {
            return _restoredValue.isRowAvailable();
        }
        return super.isRowAvailable();
    }

    public Object getRowData()
    {
        if (_restoredValue != null)
        {
            return _restoredValue.getRowData();
        }
        return super.getRowData();
    }


    public Object saveState(FacesContext context)
    {
        boolean preserveSort = isPreserveSort();
        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = _preserveDataModel;
        if (isPreserveDataModel())
        {
            values[2] = saveAttachedState(context, getSerializableDataModel());
        }
        else
        {
            values[2] = null;
        }
        values[3] = _preserveSort;
        values[4] = preserveSort ? getSortColumn() : _sortColumn;
        values[5] = preserveSort ? Boolean.valueOf(isSortAscending()) : _sortAscending;
        values[6] = _renderedIfEmpty;
        return ((Object) (values));
    }


    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _preserveDataModel = (Boolean)values[1];
        if (isPreserveDataModel())
        {
            _restoredValue = (_SerializableDataModel)restoreAttachedState(context, values[2]);
        }
        else
        {
            _restoredValue = null;
        }
        _preserveSort = (Boolean)values[3];
        _sortColumn = (String)values[4];
        _sortAscending = (Boolean)values[5];
        _renderedIfEmpty = (Boolean)values[6];

        // restore state means component was already rendered at least once:
        _firstTimeRendered = false;
    }


    public _SerializableDataModel getSerializableDataModel()
    {
        Object value = _restoredValue;
        if (value == null)
        {
            value = getValue();
        }

        if (value == null)
        {
            return null;
        }
        else if (value instanceof DataModel)
        {
            return new _SerializableDataModel(getFirst(), getRows(), (DataModel)value);
        }
        else if (value instanceof List)
        {
            return new _SerializableListDataModel(getFirst(), getRows(), (List)value);
        }
        else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass()))
        {
            return new _SerializableArrayDataModel(getFirst(), getRows(), (Object[])value);
        }
        else if (value instanceof ResultSet)
        {
            return new _SerializableResultSetDataModel(getFirst(), getRows(), (ResultSet)value);
        }
        else if (value instanceof javax.servlet.jsp.jstl.sql.Result)
        {
            return new _SerializableResultDataModel(getFirst(), getRows(), (javax.servlet.jsp.jstl.sql.Result)value);
        }
        else
        {
            return new _SerializableScalarDataModel(getFirst(), getRows(), (Object)value);
        }
    }

    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public void setSortColumn(String sortColumn)
    {
        _sortColumn = sortColumn;
        // update model is necessary here, because processUpdates is never called
        // reason: HtmlCommandSortHeader.isImmediate() == true
        ValueBinding vb = getValueBinding("sortColumn");
        if (vb != null)
        {
            vb.setValue(getFacesContext(), _sortColumn);
            _sortColumn = null;
        }
    }

    public String getSortColumn()
    {
        if (_sortColumn != null) return _sortColumn;
        ValueBinding vb = getValueBinding("sortColumn");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSortAscending(boolean sortAscending)
    {
        _sortAscending = Boolean.valueOf(sortAscending);
        // update model is necessary here, because processUpdates is never called
        // reason: HtmlCommandSortHeader.isImmediate() == true
        ValueBinding vb = getValueBinding("sortAscending");
        if (vb != null)
        {
            vb.setValue(getFacesContext(), _sortAscending);
            _sortAscending = null;
        }
    }

    public boolean isSortAscending()
    {
        if (_sortAscending != null) return _sortAscending.booleanValue();
        ValueBinding vb = getValueBinding("sortAscending");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_SORTASCENDING;
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlDataTable";
    private static final boolean DEFAULT_PRESERVEDATAMODEL = false;
    private static final boolean DEFAULT_PRESERVESORT = false;
    private static final boolean DEFAULT_SORTASCENDING = true;
    private static final boolean DEFAULT_RENDEREDIFEMPTY = true;

    private Boolean _preserveDataModel = null;
    private Boolean _preserveSort = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;
    private Boolean _renderedIfEmpty = null;

    public HtmlDataTable()
    {
    }


    public void setPreserveDataModel(boolean preserveDataModel)
    {
        _preserveDataModel = Boolean.valueOf(preserveDataModel);
    }

    public boolean isPreserveDataModel()
    {
        if (_preserveDataModel != null) return _preserveDataModel.booleanValue();
        ValueBinding vb = getValueBinding("preserveDataModel");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_PRESERVEDATAMODEL;
    }

    public void setPreserveSort(boolean preserveSort)
    {
        _preserveSort = Boolean.valueOf(preserveSort);
    }

    public boolean isPreserveSort()
    {
        if (_preserveSort != null) return _preserveSort.booleanValue();
        ValueBinding vb = getValueBinding("preserveSort");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_PRESERVESORT;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null) return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null) return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRenderedIfEmpty(boolean renderedIfEmpty)
    {
        _renderedIfEmpty = Boolean.valueOf(renderedIfEmpty);
    }

    public boolean isRenderedIfEmpty()
    {
        if (_renderedIfEmpty != null) return _renderedIfEmpty.booleanValue();
        ValueBinding vb = getValueBinding("renderedIfEmpty");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_RENDEREDIFEMPTY;
    }



    //------------------ GENERATED CODE END ---------------------------------------
}

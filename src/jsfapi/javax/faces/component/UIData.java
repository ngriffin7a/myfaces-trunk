/*
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package javax.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.model.*;
import javax.servlet.jsp.jstl.sql.Result;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIData
        extends UIComponentBase
        implements NamingContainer
{
    private static final String FOOTER_FACET_NAME = "footer";
    private static final String HEADER_FACET_NAME = "header";
    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();
    private static final int PROCESS_DECODES = 1;
    private static final int PROCESS_VALIDATORS = 2;
    private static final int PROCESS_UPDATES = 3;

    private int _rowIndex = -1;
    private DataModel _dataModel;
    private String _var = null;
    private Object[] _descendantStates;
    private int _descendantEditableValueHolderCount = -1;

    public void setFooter(UIComponent footer)
    {
        getFacets().put(FOOTER_FACET_NAME, footer);
    }

    public UIComponent getFooter()
    {
        return (UIComponent)getFacets().get(FOOTER_FACET_NAME);
    }

    public void setHeader(UIComponent header)
    {
        getFacets().put(HEADER_FACET_NAME, header);
    }

    public UIComponent getHeader()
    {
        return (UIComponent)getFacets().get(HEADER_FACET_NAME);
    }

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
     * They descendant Component states algorithm we implement here is pretty fast
     * but does not support modification of the components tree during the lifecycle.
     * TODO: should we offer an alternative implementation with a clientId based Map ?
     */
    private void saveDescendantComponentStates()
    {
        if (_descendantEditableValueHolderCount == -1)
        {
            //This is the first time we save the descendant components state
            List list = new ArrayList();
            saveDescendantComponentStates(this, list);
            _descendantEditableValueHolderCount = list.size();
            if (_descendantEditableValueHolderCount > 0)
            {
                EditableValueHolderState[] rowState
                        = (EditableValueHolderState[])list.toArray(new EditableValueHolder[list.size()]);
                _descendantStates = new Object[getRows() + 1];
                int rowIndex = getDescendantStatesRowIndex();
                _descendantStates[rowIndex] = rowState;
            }
        }
        else if (_descendantEditableValueHolderCount == 0)
        {
            //There are no EditableValueHolder children
            return;
        }
        else
        {
            int rowIndex = getDescendantStatesRowIndex();
            EditableValueHolderState[] rowState
                    = (EditableValueHolderState[])_descendantStates[rowIndex];
            if (rowState == null)
            {
                rowState = new EditableValueHolderState[_descendantEditableValueHolderCount];
                _descendantStates[rowIndex] = rowState;
            }
            saveDescendantComponentStates(this, rowState, 0);
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

    private static void saveDescendantComponentStates(UIComponent component,
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
            saveDescendantComponentStates(child, states, counter);
        }
    }

    private void restoreDescendantComponentStates()
    {
        if (_descendantEditableValueHolderCount == -1)
        {
            throw new IllegalStateException("saveDescendantComponentStates not called yet?");
        }
        else if (_descendantEditableValueHolderCount > 0)
        {
            int rowIndex = getDescendantStatesRowIndex();

            EditableValueHolderState[] rowState
                    = (EditableValueHolderState[])_descendantStates[rowIndex];
            if (rowState != null)
            {
                restoreDescendantComponentStates(this, rowState, 0);
            }
        }
    }

    private static void restoreDescendantComponentStates(UIComponent component,
                                                         EditableValueHolderState[] states,
                                                         int counter)
    {
        for (Iterator it = component.getChildren().iterator(); it.hasNext();)
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof EditableValueHolder)
            {
                states[counter++].restore((EditableValueHolder)child);
            }
            restoreDescendantComponentStates(child, states, counter);
        }
    }


    public void setRows(int rows)
    {
        _rows = new Integer(rows);
        if (rows < 0) throw new IllegalArgumentException("rows: " + rows);
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public String getVar()
    {
        return _var;
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

    public String getClientId(FacesContext context)
    {
        String clientId = super.getClientId(context);
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
        {
            return clientId;
        }
        else
        {
            return clientId + "_" + rowIndex;
        }
    }

    public void queueEvent(FacesEvent event)
    {
        super.queueEvent(new FacesEventWrapper(event, getRowIndex(), this));
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        if (event instanceof FacesEventWrapper)
        {
            FacesEvent originalEvent = ((FacesEventWrapper)event).getWrappedFacesEvent();
            int eventRowIndex = ((FacesEventWrapper)event).getRowIndex();
            int currentRowIndex = getRowIndex();
            setRowIndex(eventRowIndex);
            originalEvent.getComponent().broadcast(originalEvent);
            setRowIndex(currentRowIndex);
        }
        else
        {
            super.broadcast(event);
        }
    }

    public void encodeBegin(javax.faces.context.FacesContext context)
            throws IOException
    {
        //TODO: ??? javadoc says: ensure that any saved per-row state for our child input components is discarded unless it is needed to rerender the current page with errors
        _descendantStates = null;
        _descendantEditableValueHolderCount = -1;
        super.encodeBegin(context);
    }

    public void processDecodes(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;
        setRowIndex(-1);
        processFacets(context, PROCESS_DECODES);
        processColumnFacets(context, PROCESS_DECODES);
        processColumnChildren(context, PROCESS_DECODES);
        setRowIndex(-1);
        try
        {
            decode(context);
        }
        catch (RuntimeException e)
        {
            context.renderResponse();
            throw e;
        }
    }

    public void processValidators(javax.faces.context.FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;
        setRowIndex(-1);
        processFacets(context, PROCESS_VALIDATORS);
        processColumnFacets(context, PROCESS_VALIDATORS);
        processColumnChildren(context, PROCESS_VALIDATORS);
        setRowIndex(-1);
    }

    public void processUpdates(javax.faces.context.FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;
        setRowIndex(-1);
        processFacets(context, PROCESS_UPDATES);
        processColumnFacets(context, PROCESS_UPDATES);
        processColumnChildren(context, PROCESS_UPDATES);
        setRowIndex(-1);
    }


    private void processFacets(FacesContext context, int processAction)
    {
        for (Iterator it = getFacets().keySet().iterator(); it.hasNext();)
        {
            UIComponent facet = (UIComponent)it.next();
            process(context, facet, processAction);
        }
    }

    private void processColumnFacets(FacesContext context, int processAction)
    {
        for (Iterator it = getChildren().iterator(); it.hasNext();)
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UIColumn)
            {
                if (!child.isRendered())
                {
                    //Column is not visible
                    continue;
                }
                for (Iterator it2 = child.getFacets().keySet().iterator(); it.hasNext();)
                {
                    UIComponent facet = (UIComponent)it2.next();
                    process(context, facet, processAction);
                }
            }
        }
    }

    private void processColumnChildren(FacesContext context, int processAction)
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
        for (int rowIndex = first; rowIndex < last; rowIndex++)
        {
            setRowIndex(rowIndex);
            if (isRowAvailable())
            {
                for (Iterator it = getChildren().iterator(); it.hasNext();)
                {
                    UIComponent child = (UIComponent)it.next();
                    if (child instanceof UIColumn)
                    {
                        if (!child.isRendered())
                        {
                            //Column is not visible
                            continue;
                        }
                        for (Iterator it2 = child.getChildren().iterator(); it.hasNext();)
                        {
                            UIComponent columnChild = (UIComponent)it2.next();
                            process(context, columnChild, processAction);
                        }
                    }
                }
            }
        }
    }

    private void process(FacesContext context, UIComponent component, int processAction)
    {
        switch (processAction)
        {
            case PROCESS_DECODES:
                component.processDecodes(context);
                break;
            case PROCESS_VALIDATORS:
                component.processValidators(context);
                break;
            case PROCESS_UPDATES:
                component.processUpdates(context);
                break;
        }
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



    private static class FacesEventWrapper
            extends FacesEvent
    {
        private FacesEvent _wrappedFacesEvent;
        private int _rowIndex;

        public FacesEventWrapper(FacesEvent facesEvent, int rowIndex,
                                 UIData redirectComponent)
        {
            super(redirectComponent);
            _wrappedFacesEvent = facesEvent;
            _rowIndex = rowIndex;
        }

        public PhaseId getPhaseId()
        {
            return _wrappedFacesEvent.getPhaseId();
        }

        public void setPhaseId(PhaseId phaseId)
        {
            _wrappedFacesEvent.setPhaseId(phaseId);
        }

        public void queue()
        {
            _wrappedFacesEvent.queue();
        }

        public String toString()
        {
            return _wrappedFacesEvent.toString();
        }

        public boolean isAppropriateListener(FacesListener faceslistener)
        {
            return _wrappedFacesEvent.isAppropriateListener(faceslistener);
        }

        public void processListener(FacesListener faceslistener)
        {
            _wrappedFacesEvent.processListener(faceslistener);
        }

        public FacesEvent getWrappedFacesEvent()
        {
            return _wrappedFacesEvent;
        }

        public int getRowIndex()
        {
            return _rowIndex;
        }
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
        values[1] = _first;
        values[2] = _rows;
        values[3] = _value;
        values[4] = _var;
        values[5] = _descendantStates;
        values[6] = new Integer(_descendantEditableValueHolderCount);
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _first = (Integer)values[1];
        _rows = (Integer)values[2];
        _value = (Object)values[3];
        _var = (String)values[4];
        _descendantStates = (Object[])values[5];
        _descendantEditableValueHolderCount = ((Integer)values[6]).intValue();
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.Data";
    public static final String COMPONENT_FAMILY = "javax.faces.Data";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Table";
    private static final int DEFAULT_FIRST = 0;
    private static final int DEFAULT_ROWS = 0;

    private Integer _first = null;
    private Integer _rows = null;
    private Object _value = null;

    public UIData()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setFirst(int first)
    {
        _first = new Integer(first);
    }

    public int getFirst()
    {
        if (_first != null) return _first.intValue();
        ValueBinding vb = getValueBinding("first");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : DEFAULT_FIRST;
    }


    public int getRows()
    {
        if (_rows != null) return _rows.intValue();
        ValueBinding vb = getValueBinding("rows");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : DEFAULT_ROWS;
    }

    public void setValue(Object value)
    {
        _value = value;
    }

    public Object getValue()
    {
        if (_value != null) return _value;
        ValueBinding vb = getValueBinding("value");
        return vb != null ? (Object)vb.getValue(getFacesContext()) : null;
    }


    //------------------ GENERATED CODE END ---------------------------------------



}

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
package net.sourceforge.myfaces.component.html;

import net.sourceforge.myfaces.model.SerializableDataModel;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;
import javax.servlet.jsp.jstl.sql.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class MyFacesHtmlDataTable
    extends HtmlDataTable
{
    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();
    private Object _value;

    public void encodeBegin(FacesContext context) throws IOException
    {
        if (isPreserveDataModel())
        {
            Object value = getLocalValue();
            if (value != null &&
                value instanceof SerializableDataModel &&
                getValueBinding("value") != null)
            {
                //Clear local value, so that current data from model bean is used from now on
                setValue(null);
            }
        }
        super.encodeBegin(context);
    }


    public void processUpdates(FacesContext context)
    {
        if (isPreserveDataModel())
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

        super.processUpdates(context);
    }


    private void updateModelFromPreservedDataModel(FacesContext context)
    {
        Object value = getLocalValue();
        if (value != null &&
            value instanceof SerializableDataModel)
        {
            ValueBinding vb = getValueBinding("value");
            if (vb != null && !vb.isReadOnly(context))
            {
                SerializableDataModel dm = (SerializableDataModel)value;
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
                setValue(null); //clear local value
            }
        }
    }



    public int getFirst()
    {
        if (isPreserveDataModel())
        {
            Object value = getLocalValue();
            if (value != null &&
                value instanceof SerializableDataModel)
            {
                return ((SerializableDataModel)value).getFirst();
            }
        }
        return super.getFirst();
    }

    public int getRows()
    {
        if (isPreserveDataModel())
        {
            Object value = getLocalValue();
            if (value != null &&
                value instanceof SerializableDataModel)
            {
                return ((SerializableDataModel)value).getRows();
            }
        }
        return super.getRows();
    }

    public Object saveState(FacesContext context)
    {
        boolean preserveSort = isPreserveSort();
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = _preserveDataModel;
        values[2] = saveAttachedState(context, isPreserveDataModel() ?
                                               getSerializableDataModel() :
                                               null);
        values[3] = _preserveSort;
        values[4] = preserveSort ? getSortColumn() : _sortColumn;
        values[5] = preserveSort ? Boolean.valueOf(isSortAscending()) : _sortAscending;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _preserveDataModel = (Boolean)values[1];
        if (isPreserveDataModel())
        {
            setValue(restoreAttachedState(context, values[2]));
        }
        _preserveSort = (Boolean)values[3];
        _sortColumn = (String)values[4];
        _sortAscending = (Boolean)values[5];
    }


    public Object getSerializableDataModel()
    {
        Object value = getValue();
        if (value == null)
        {
            return null;
        }
        else if (value instanceof DataModel)
        {
            return new SerializableDataModel(getFirst(), getRows(), (DataModel)value);
        }
        else if (value instanceof List)
        {
            return new SerializableDataModel(getFirst(), getRows(), (List)value);
        }
        else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass()))
        {
            return new SerializableDataModel(getFirst(), getRows(), (Object[])value);
        }
        else if (value instanceof ResultSet)
        {
            return new SerializableDataModel(getFirst(), getRows(), (ResultSet)value);
        }
        else if (value instanceof Result)
        {
            return new SerializableDataModel(getFirst(), getRows(), (Result)value);
        }
        else
        {
            return new SerializableDataModel(getFirst(), getRows(), (Object)value);
        }
    }


    public Object getLocalValue()
    {
        return _value;
    }

    public Object getValue()
    {
        if (_value != null) return _value;
        return super.getValue();
    }

    public void setValue(Object value)
    {
        _value = value;
        super.setValue(value);
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlDataTable";
    public static final String COMPONENT_FAMILY = "javax.faces.Data";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.Table";
    private static final boolean DEFAULT_PRESERVEDATAMODEL = false;
    private static final boolean DEFAULT_PRESERVESORT = false;
    private static final boolean DEFAULT_SORTASCENDING = true;

    private Boolean _preserveDataModel = null;
    private Boolean _preserveSort = null;
    private String _sortColumn = null;
    private Boolean _sortAscending = null;

    public MyFacesHtmlDataTable()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
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

    public void setSortColumn(String sortColumn)
    {
        _sortColumn = sortColumn;
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
    }

    public boolean isSortAscending()
    {
        if (_sortAscending != null) return _sortAscending.booleanValue();
        ValueBinding vb = getValueBinding("sortAscending");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_SORTASCENDING;
    }

    //------------------ GENERATED CODE END ---------------------------------------
}

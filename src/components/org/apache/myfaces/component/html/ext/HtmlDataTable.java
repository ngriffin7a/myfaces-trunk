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
package net.sourceforge.myfaces.component.html.ext;

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.component.UserRoleUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 * $Log$
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
    transient private Object _localValue = null;
    transient private boolean _allChildrenAndFacetsValid = true;

    public void encodeBegin(FacesContext context) throws IOException
    {
        if (_allChildrenAndFacetsValid)
        {
            //Clear local value, so that current data from model bean is used again
            _localValue = null;
        }
        super.encodeBegin(context);
    }

    public void processDecodes(FacesContext context)
    {
        try
        {
            super.processDecodes(context);
        }
        finally
        {
            if (context.getRenderResponse() || context.getResponseComplete())
            {
                // one of the children and facets failed during decode
                _allChildrenAndFacetsValid = false;
            }
        }
    }

    public void processValidators(FacesContext context)
    {
        try
        {
            super.processValidators(context);
        }
        finally
        {
            if (context.getRenderResponse() || context.getResponseComplete())
            {
                // one of the children and facets failed during decode
                _allChildrenAndFacetsValid = false;
            }
        }
    }

    public void processUpdates(FacesContext context)
    {
        try
        {
            super.processUpdates(context);

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

        }
        finally
        {
            if (context.getRenderResponse() || context.getResponseComplete())
            {
                // one of the children and facets failed during decode
                _allChildrenAndFacetsValid = false;
            }
        }
    }


    private void updateModelFromPreservedDataModel(FacesContext context)
    {
        if (_localValue != null &&
            _localValue instanceof _SerializableDataModel)
        {
            ValueBinding vb = getValueBinding("value");
            if (vb != null && !vb.isReadOnly(context))
            {
                _SerializableDataModel dm = (_SerializableDataModel)_localValue;
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
                //setValue(null); //clear local value
            }
        }
    }



    public int getFirst()
    {
        if (isPreserveDataModel())
        {
            if (_localValue != null &&
                _localValue instanceof _SerializableDataModel)
            {
                return ((_SerializableDataModel)_localValue).getFirst();
            }
        }
        return super.getFirst();
    }

    public int getRows()
    {
        if (isPreserveDataModel())
        {
            if (_localValue != null &&
                _localValue instanceof _SerializableDataModel)
            {
                return ((_SerializableDataModel)_localValue).getRows();
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
            _localValue = restoreAttachedState(context, values[2]);
        }
        _preserveSort = (Boolean)values[3];
        _sortColumn = (String)values[4];
        _sortAscending = (Boolean)values[5];
    }


    public Object getSerializableDataModel()
    {
        Object value = _localValue;
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


    public Object getValue()
    {
        // if we have a local value (either from restoreState or from former getValue call)
        // we return this value
        if (_localValue != null) return _localValue;

        // else get the "real" current value, and again remember it as local value
        _localValue = super.getValue();

        return _localValue;
    }


    public void setValue(Object value)
    {
        super.setValue(value);
        _localValue = null;
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlDataTable";
    private static final boolean DEFAULT_PRESERVEDATAMODEL = false;
    private static final boolean DEFAULT_PRESERVESORT = false;
    private static final boolean DEFAULT_SORTASCENDING = true;

    private Boolean _preserveDataModel = null;
    private Boolean _preserveSort = null;
    private String _sortColumn = null;
    private Boolean _sortAscending = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;

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


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    //------------------ GENERATED CODE END ---------------------------------------
}

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

import javax.faces.component.UIData;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesHtmlDataTable
    extends HtmlDataTable
{

    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

    private boolean _preserveDataModel = true;
    private boolean _preserveSort = true;
    private String _sortColumn;
    private Boolean _sortAscending;

    public void encodeBegin(FacesContext context) throws IOException
    {
        if (_preserveDataModel)
        {
            Object value = getLocalValue();
            if (value instanceof SerializableDataModel &&
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
        if (_preserveDataModel)
        {
            updateModelFromPreservedDataModel(context);
        }

        if (_preserveSort)
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
        if (value instanceof SerializableDataModel)
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
                    throw new UnsupportedOperationException();
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
        if (_preserveDataModel)
        {
            Object value = getLocalValue();
            if (value instanceof SerializableDataModel)
            {
                return ((SerializableDataModel)value).getFirst();
            }
        }
        return super.getFirst();
    }

    public int getRows()
    {
        if (_preserveDataModel)
        {
            Object value = getLocalValue();
            if (value instanceof SerializableDataModel)
            {
                return ((SerializableDataModel)value).getRows();
            }
        }
        return super.getRows();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = Boolean.valueOf(_preserveDataModel);
        values[2] = _preserveDataModel ? getSerializableDataModel() : null;
        values[3] = Boolean.valueOf(_preserveSort);
        values[4] = _preserveSort ? getSortColumn() : _sortColumn;
        values[5] = _preserveSort ? Boolean.valueOf(getSortAscending()) : _sortAscending;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _preserveDataModel = ((Boolean)values[1]).booleanValue();
        SerializableDataModel serDataModel = (SerializableDataModel)values[2];
        if (_preserveDataModel && serDataModel != null)
        {
            setValue(serDataModel);
        }
        _preserveSort = ((Boolean)values[3]).booleanValue();
        _sortColumn = (String)values[4];
        _sortAscending = (Boolean)values[5];
    }


    public Object getSerializableDataModel()
    {
        DataModel dm = getDataModelHack();
        if (dm == null)
        {
            return null;
        }
        return new SerializableDataModel(getFirst(), getRows(), dm);
    }


    /**
     * Hack to access the private getDataModel method in UIData superclass
     * @return
     */
    private DataModel getDataModelHack()
    {
        try
        {
            Method m = null;
            Class c = UIData.class;
            m = c.getDeclaredMethod("getDataModel", null);
            if (m == null)
            {
                throw new NoSuchMethodException();
            }

            DataModel dm;
            if (m.isAccessible())
            {
                dm = (DataModel)m.invoke(this, null);
            }
            else
            {
                final Method finalM = m;
                AccessController.doPrivileged(
                    new PrivilegedAction()
                    {
                        public Object run()
                        {
                            finalM.setAccessible(true);
                            return null;
                        }
                    });
                dm = (DataModel)m.invoke(this, null);
                m.setAccessible(false);
            }

            return dm;
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
        catch (SecurityException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static class SerializableDataModel
        extends DataModel
        implements Serializable
    {
        private int _first;
        private int _rows;
        private List _list;
        private int _rowCount;

        private transient int _index = -1;

        public SerializableDataModel(int first, int rows, DataModel dataModel)
        {
            _first = first;
            _rows = rows != 0 ? rows : dataModel.getRowCount() - first;
            _list = new ArrayList(rows);
            for (int i = 0; i < _rows; i++)
            {
                dataModel.setRowIndex(_first + i);
                if (!dataModel.isRowAvailable()) break;
                _list.add(dataModel.getRowData());
            }
            _rowCount = dataModel.getRowCount();
            _index = -1;

            //TODO: take over DataModelListeners
        }

        public int getFirst()
        {
            return _first;
        }

        public int getRows()
        {
            return _rows;
        }

        public boolean isRowAvailable()
        {
            return _index >= 0 && _index < _rowCount;
        }

        public int getRowCount()
        {
            return _rowCount;
        }

        public Object getRowData()
        {
            if (_index < _first || _index - _first >= _list.size())
            {
                throw new IllegalStateException();
            }
            return _list.get(_index - _first);
        }

        public int getRowIndex()
        {
            return _index;
        }

        public void setRowIndex(int rowIndex)
        {
            if (rowIndex < -1)
            {
                throw new IllegalArgumentException();
            }
            _index = rowIndex;
            //TODO: handle DataModelListeners
        }

        public Object getWrappedData()
        {
            //throw new UnsupportedOperationException();
            return _list;
        }

        public void setWrappedData(Object obj)
        {
            throw new UnsupportedOperationException();
        }
    }


    public String getSortColumn()
    {
        if (_sortColumn != null) return _sortColumn;
        ValueBinding vb = getValueBinding("sortColumn");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSortColumn(String sortColumn)
    {
        _sortColumn = sortColumn;
    }


    public boolean getSortAscending()
    {
        if (_sortAscending != null) return _sortAscending.booleanValue();
        ValueBinding vb = getValueBinding("sortAscending");
        Boolean b = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return b != null ? b.booleanValue() : true;
    }

    public void setSortAscending(boolean sortAscending)
    {
        _sortAscending = Boolean.valueOf(sortAscending);
    }
}

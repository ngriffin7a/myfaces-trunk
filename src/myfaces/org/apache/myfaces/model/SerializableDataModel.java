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
package net.sourceforge.myfaces.model;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import javax.servlet.jsp.jstl.sql.Result;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SerializableDataModel
        extends DataModel
        implements Serializable, StateHolder
{
    //private static final Log log = LogFactory.getLog(SerializableDataModel.class);

    private int _first;
    private int _rows;
    private int _rowCount;
    private List _list;
    private transient int _rowIndex = -1;

    public SerializableDataModel(int first, int rows, DataModel dataModel)
    {
        _first = first;
        _rows = rows;
        _rowCount = dataModel.getRowCount();
        if (_rows <= 0)
        {
            _rows = _rowCount - first;
        }
        _list = new ArrayList(rows);
        for (int i = 0; i < _rows; i++)
        {
            dataModel.setRowIndex(_first + i);
            if (!dataModel.isRowAvailable()) break;
            _list.add(dataModel.getRowData());
        }
        _rowIndex = -1;

        DataModelListener[] dataModelListeners = dataModel.getDataModelListeners();
        for (int i = 0; i < dataModelListeners.length; i++)
        {
            DataModelListener dataModelListener = dataModelListeners[i];
            addDataModelListener(dataModelListener);
        }
    }

    public SerializableDataModel(int first, int rows, List list)
    {
        _first = first;
        _rows = rows;
        _rowCount = list.size();
        if (_rows <= 0)
        {
            _rows = _rowCount - first;
        }

        if (_rows == _rowCount)
        {
            //whole list must be saved
            if (list instanceof Serializable || list instanceof StateHolder)
            {
                _list = list;
            }
            else
            {
                //copy list
                _list = new ArrayList(list);
            }
        }
        else
        {
            _list = new ArrayList(_rows);
            for (int i = 0; i < _rowCount; i++)
            {
                _list.add(list.get(_first + i));
            }
        }
    }

    public SerializableDataModel(int first, int rows, Object[] array)
    {
        _first = first;
        _rows = rows;
        _rowCount = array.length;
        if (_rows <= 0)
        {
            _rows = _rowCount - first;
        }
        _list = new ArrayList(_rows);
        for (int i = 0; i < _rowCount; i++)
        {
            _list.add(array[_first + i]);
        }
    }

    public SerializableDataModel(int first, int rows, ResultSet resultSet)
    {
        throw new UnsupportedOperationException("not yet supported"); //TODO
    }

    public SerializableDataModel(int first, int rows, Result result)
    {
        _first = first;
        _rows = rows;
        _rowCount = result.getRowCount();
        if (_rows <= 0)
        {
            _rows = _rowCount - first;
        }
        _list = new ArrayList(_rows);
        SortedMap[] resultRows = result.getRows();
        for (int i = 0; i < _rowCount; i++)
        {
            _list.add(resultRows[_first + i]);
        }
    }

    public SerializableDataModel(int first, int rows, Object scalar)
    {
        _first = first;
        _rows = rows;
        _rowCount = 1;
        if (_rows <= 0)
        {
            _rows = _rowCount - first;
        }
        _list = Collections.singletonList(scalar);
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
        return _rowIndex >= _first && _rowIndex < _first + _rows;
    }

    public int getRowCount()
    {
        return _rowCount;
    }

    public Object getRowData()
    {
        if (!isRowAvailable())
        {
            throw new IllegalStateException();
        }
        return _list.get(_rowIndex - _first);
    }

    public int getRowIndex()
    {
        return _rowIndex;
    }

    public void setRowIndex(int rowIndex)
    {
        if (rowIndex < -1)
        {
            throw new IllegalArgumentException();
        }

        int oldRowIndex = _rowIndex;
        _rowIndex = rowIndex;
        if (oldRowIndex != _rowIndex)
        {
            Object data = isRowAvailable() ? getRowData() : null;
            DataModelEvent event = new DataModelEvent(this, _rowIndex, data);
            DataModelListener[] listeners = getDataModelListeners();
            for (int i = 0; i < listeners.length; i++)
            {
                listeners[i].rowSelected(event);
            }
        }
    }

    public Object getWrappedData()
    {
        return _list;
    }

    public void setWrappedData(Object obj)
    {
        if (obj != null)
        {
            throw new IllegalArgumentException("Cannot set wrapped data of SerializableDataModel");
        }
    }



    // StateHolder interface

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = new Integer(_first);
        values[1] = new Integer(_rows);
        values[2] = new Integer(_rowCount);
        values[3] = _list;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        _first    = ((Integer)values[0]).intValue();
        _rows     = ((Integer)values[1]).intValue();
        _rowCount = ((Integer)values[2]).intValue();
        _list     = (List)values[3];
    }

    public boolean isTransient()
    {
        return false;
    }

    public void setTransient(boolean newTransientValue)
    {
        throw new UnsupportedOperationException();
    }
}

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

import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _SerializableDataModel
        extends DataModel
        implements Serializable
{
    //private static final Log log = LogFactory.getLog(_SerializableDataModel.class);

    protected int _first;
    protected int _rows;
    protected int _rowCount;
    protected List _list;
    private transient int _rowIndex = -1;

    public _SerializableDataModel(int first, int rows, DataModel dataModel)
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

    protected _SerializableDataModel()
    {
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
        return _rowIndex >= _first && 
            _rowIndex < _first + _rows &&
            _rowIndex < _rowCount;
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
            throw new IllegalArgumentException("Cannot set wrapped data of _SerializableDataModel");
        }
    }



    /*
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
    */
}

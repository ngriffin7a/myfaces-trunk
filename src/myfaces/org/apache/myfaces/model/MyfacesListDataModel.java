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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyfacesListDataModel
        extends DataModel
        implements Serializable
{
    //private static final Log log = LogFactory.getLog(MyfacesListDataModel.class);

    private int _index = -1;
    private ValueBinding _valueBinding = null;
    private List _list = null;

    protected MyfacesListDataModel()
    {
        _index = -1;
        _valueBinding = null;
        setWrappedData(null);
    }

    public MyfacesListDataModel(ValueBinding valueBinding)
    {
        _index = -1;
        _valueBinding = valueBinding;
        setWrappedData(null);
    }

    public MyfacesListDataModel(List list)
    {
        _index = -1;
        _valueBinding = null;
        setWrappedData(list);
    }

    public boolean isRowAvailable()
    {
        List lst = (List)getWrappedData();
        if (lst == null)
        {
            return false;
        }
        return _index >= 0 && _index < lst.size();
    }

    public int getRowCount()
    {
        List lst = (List)getWrappedData();
        if (lst == null)
        {
            return -1;
        }
        else
        {
            return lst.size();
        }
    }

    public Object getRowData()
    {
        List lst = (List)getWrappedData();
        if (lst == null)
        {
            return null;
        }

        if (!isRowAvailable())
        {
            throw new IllegalArgumentException();
        }

        return lst.get(_index);
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
        int previousIndex = _index;
        _index = rowIndex;

        if (getWrappedData() == null)
        {
            return;
        }

        DataModelListener[] listeners = getDataModelListeners();
        if (previousIndex != _index && listeners != null && listeners.length > 0)
        {
            Object rowData = null;
            if (isRowAvailable())
            {
                rowData = getRowData();
            }
            DataModelEvent event = new DataModelEvent(this, _index, rowData);
            for (int i = 0, len = listeners.length; i < len; i++)
            {
                listeners[i].rowSelected(event);
            }
        }
    }

    public Object getWrappedData()
    {
        if (_list == null)
        {
            _list = (List)_valueBinding.getValue(FacesContext.getCurrentInstance());
        }
        return _list;
    }

    public void setWrappedData(Object wrappedData)
    {
        if (wrappedData == null)
        {
            _list = null;
        }
        else
        {
            _list = (List)wrappedData;
            setRowIndex(0);
        }
    }


    private void writeObject(java.io.ObjectOutputStream out)
         throws IOException
    {
        out.writeObject(getDataModelListeners());

        out.writeObject(_valueBinding != null ?
                        _valueBinding.getExpressionString() :
                        null);

        List lst = (List)getWrappedData();
        if (lst instanceof Serializable)
        {
            out.writeObject(lst);
        }
        else
        {
            List serializableLst = new ArrayList();
            serializableLst.addAll(lst);
            out.writeObject(serializableLst);
        }
    }

    private void readObject(java.io.ObjectInputStream in)
         throws IOException, ClassNotFoundException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        DataModelListener[] listeners = (DataModelListener[])in.readObject();
        for (int i = 0, len = listeners.length; i < len; i++)
        {
            addDataModelListener(listeners[i]);
        }

        String vb = (String)in.readObject();
        if (vb != null)
        {
            _valueBinding = facesContext.getApplication().createValueBinding(vb);
        }
        else
        {
            _valueBinding = null;
        }

        setWrappedData(in.readObject());
    }

}

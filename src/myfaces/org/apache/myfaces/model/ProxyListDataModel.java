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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ProxyListDataModel
    extends DataModel
{
    //private static final Log log = LogFactory.getLog(ProxyListDataModel.class);

    private ValueBinding _valueBinding;
    private int _rowCount;
    private Class _rowObjectInterface;

    private int _index;
    private List _list;

    public ProxyListDataModel(ValueBinding valueBinding,
                              int rowCount,
                              Class rowObjectInterface)
    {
        _valueBinding = valueBinding;
        _rowCount = rowCount;
        _rowObjectInterface = rowObjectInterface;

        _index = -1;
        setWrappedData(null);
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
        if (!isRowAvailable())
        {
            throw new IllegalArgumentException();
        }

        if (_list == null)
        {
            return Proxy.newProxyInstance(_rowObjectInterface.getClassLoader(),
                                          new Class[] {_rowObjectInterface}, //TODO: optimize
                                          _invocationHandler);
        }
        else
        {
            return _list.get(_index);
        }
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
        int prevIndex = _index;
        _index = rowIndex;
        if (prevIndex != _index && listeners != null)
        {
            Object rowData = null;
            if (isRowAvailable())
            {
                rowData = getRowData();
            }
            DataModelEvent event = new DataModelEvent(this, _index, rowData);
            int n = listeners.size();
            for(int i = 0; i < n; i++)
            {
                ((DataModelListener)listeners.get(i)).rowSelected(event);
            }
        }
    }

    public Object getWrappedData()
    {
        if (_list == null)
        {
            _list = (List)_valueBinding.getValue(FacesContext.getCurrentInstance());
            _rowCount = _list.size();
        }
        return _list;
    }

    public void setWrappedData(Object data)
    {
        if (data == null)
        {
            _list = null;
            _rowCount = 0;
        }
        else
        {
            _list = (List)data;
            _rowCount = _list.size();
            setRowIndex(0);
        }
    }



    private InvocationHandler _invocationHandler = new InvocationHandler()
    {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            getWrappedData(); //get real List from ValueBinding
            Object rowObject = getRowData();
            return method.invoke(rowObject, args);
        }
    };

}

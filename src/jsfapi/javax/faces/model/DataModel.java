/**
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

package javax.faces.model;

import java.util.ArrayList;
import java.util.List;

/**
  * @author Thomas Spiegl (latest modification by $Author$)
  * @version $Revision$ $Date$
*/
public abstract class DataModel
{

	// FIELDS
    private List _listeners;

	// CONSTRUCTORS
	public DataModel()
	{
		setWrappedData(null);
 	}

	// METHODS
	public void addDataModelListener(DataModelListener listener)
	{
        if (listener == null) throw new NullPointerException("listener");
		if (_listeners == null)
        {
            _listeners = new ArrayList();
        }
        _listeners.add(listener);
	}

	public DataModelListener[] getDataModelListeners()
	{
        if (_listeners == null)
        {
            return new DataModelListener[0];
        }
        return (DataModelListener[])_listeners.toArray(new DataModelListener[_listeners.size()]);
	}

	abstract public int getRowCount();

	abstract public Object getRowData();

	abstract public int getRowIndex();

	abstract public Object getWrappedData();

	abstract public boolean isRowAvailable();

	public void removeDataModelListener(DataModelListener listener)
	{
		if (listener == null) throw new NullPointerException("listener");
		if (_listeners != null)
        {
            _listeners.remove(listener);
        }
	}

	abstract public void setRowIndex(int rowIndex);

	abstract public void setWrappedData(Object data);

}

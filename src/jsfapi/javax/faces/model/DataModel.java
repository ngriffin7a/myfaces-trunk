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

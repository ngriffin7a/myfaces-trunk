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

/**
  * @author Thomas Spiegl (latest modification by $Author$)
  * @version $Revision$ $Date$
*/
public class DataModelEvent
{

	// FIELDS
    private DataModel _model;
    private int _index;
    private Object _data;

	// CONSTRUCTORS
	public DataModelEvent(DataModel model, int index, Object data)
	{
		_model = model;
        _index = index;
        _data = data;
	}

	// METHODS
	public DataModel getDataModel()
	{
		return _model;
	}

	public Object getRowData()
	{
		return _data;
	}

	public int getRowIndex()
	{
		return _index;
	}

}

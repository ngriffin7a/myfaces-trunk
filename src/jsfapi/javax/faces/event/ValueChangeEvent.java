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
package javax.faces.event;

import javax.faces.component.UIComponent;

/**
  * @author Thomas Spiegl (latest modification by $Author$)
  * @version $Revision$ $Date$
*/
public class ValueChangeEvent extends FacesEvent {

	// FIELDS
    private Object _oldValue;
    private Object _newValue;

	// CONSTRUCTORS
	public ValueChangeEvent(UIComponent uiComponent, Object oldValue, Object newValue)
	{
        super(uiComponent);
        if (uiComponent == null) throw new IllegalArgumentException("uiComponent");
        _oldValue = oldValue;
        _newValue = newValue;
	}

	// METHODS
	public Object getNewValue()
	{
		return _newValue;
	}

	public Object getOldValue()
	{
		return _oldValue;
	}

	public boolean isAppropriateListener(FacesListener facesListeners)
	{
        return facesListeners instanceof ValueChangeListener;
    }

    public void processListener(FacesListener facesListeners)
    {
        ((ValueChangeListener)facesListeners).processValueChange(this);
    }
}

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
package net.sourceforge.myfaces.config;

import net.sourceforge.myfaces.util.ClassUtils;

import javax.faces.webapp.UIComponentTag;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ListEntriesConfig
{
    //~ Instance fields ----------------------------------------------------------------------------

    private Class _valueClass = null;
    private List  _values = null;
    private boolean _containsValueBindings = false;

    //~ Methods ------------------------------------------------------------------------------------

    public void setValueClass(String valueClass)
    {
        _valueClass = ClassUtils.classForName(valueClass);  //no need to support primitive types
    }

    public Class getValueClass()
    {
        return _valueClass;
    }

    public void addNullValue(String dummy)
    {
        addValue(null);
    }

    public void addValue(String value)
    {
        if (_values == null)
        {
            _values = new ArrayList();
        }

        if (value != null && UIComponentTag.isValueReference(value))
        {
            _values.add(new ValueBindingExpression(value));
            _containsValueBindings = true;
        }
        else
        {
            if (_valueClass != null)
            {
                _values.add(ClassUtils.convertToType(value, _valueClass));
            }
            else
            {
                _values.add(value);
            }
        }
    }

    public List getValues()
    {
        return _values;
    }

    public boolean isContainsValueBindings()
    {
        return _containsValueBindings;
    }
}

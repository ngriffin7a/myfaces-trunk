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
import java.util.HashMap;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class MapEntriesConfig
{
    //~ Instance fields ----------------------------------------------------------------------------

    private Class _keyClass = null;
    private Class _valueClass = null;
    private Map _map = null;
    private boolean _containsValueBindings = false;

    //~ Methods ------------------------------------------------------------------------------------

    public void setKeyClass(String keyClass)
    {
        _keyClass = ClassUtils.classForName(keyClass);  //no need to support primitive types
    }

    public Class getKeyClass()
    {
        return _keyClass;
    }

    public void setValueClass(String valueClass)
    {
        _valueClass = ClassUtils.classForName(valueClass);  //no need to support primitive types
    }

    public Class getValueClass()
    {
        return _valueClass;
    }

    public void addMapEntryConfig(MapEntryConfig mapEntryConfig)
    {
        if (_map == null)
        {
            _map = new HashMap();
        }

        Object key;
        if (_keyClass != null)
        {
            key = ClassUtils.convertToType(mapEntryConfig.getKey(), _keyClass);
        }
        else
        {
            key = mapEntryConfig.getKey();
        }

        String strValue = mapEntryConfig.getValue();
        if (strValue == null)
        {
            _map.put(key, null);
        }
        else if (UIComponentTag.isValueReference(strValue))
        {
            _map.put(key, new ValueBindingExpression(strValue));
            _containsValueBindings = true;
        }
        else
        {
            if (_valueClass != null)
            {
                _map.put(key, ClassUtils.convertToType(strValue, _valueClass));
            }
            else
            {
                _map.put(key, strValue);
            }
        }
    }

    public Map getMap()
    {
        return _map;
    }

    public boolean isContainsValueBindings()
    {
        return _containsValueBindings;
    }

}

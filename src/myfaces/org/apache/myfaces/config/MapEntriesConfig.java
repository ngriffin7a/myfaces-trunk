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

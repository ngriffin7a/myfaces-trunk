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

import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.8  2004/07/01 22:05:08  mwessendorf
 * ASF switch
 *
 * Revision 1.7  2004/05/12 07:57:43  manolito
 * Log in javadoc header
 *
 */
public class ManagedPropertyConfig extends PropertyConfig
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final int TYPE_UNKNOWN       = 0;
    public static final int TYPE_OBJECT        = 1;
    public static final int TYPE_VALUE_BINDING = 2;
    public static final int TYPE_MAP           = 3;
    public static final int TYPE_LIST          = 4;

    //~ Instance fields ----------------------------------------------------------------------------

// inherited
//    private String _description;
//    private String _displayName;
//    private IconConfig _iconConfig;
//    private String _propertyName;
//    private Class _propertyClass;
    
    private Object            _value = null;
    private ValueBinding      _valueBinding = null;
    private MapEntriesConfig  _mapEntriesConfig = null;
    private ListEntriesConfig _listEntriesConfig = null;
    private int               _type = TYPE_UNKNOWN;

    //~ Methods ------------------------------------------------------------------------------------

    public void setDescription(String description)
    {
// ignore
//        _description = description;
    }

    public void setDisplayName(String displayName)
    {
// ignore
//        _displayName = displayName;
    }

    public void setIconConfig(IconConfig iconConfig)
    {
// ignore
//        _iconConfig = iconConfig;
    }

    public void setPropertyClass(String propertyClassName)
    {
        _propertyClass = ClassUtils.javaTypeToClass(propertyClassName);
    }

    /** 
     * Used to update the type with later during execution 
     */
    public void setPropertyClass(Class clazz)
    {
        _propertyClass = clazz;
    }

    public Class getPropertyClass()
    {
        return _propertyClass;
    }
    
    public void setPropertyName(String propertyName)
    {
        _propertyName = propertyName.intern();
    }

    public String getPropertyName()
    {
        return _propertyName;
    }

    public void setNullValue(String dummy)
    {
        _type = TYPE_OBJECT;
        _value = null;
    }

    public void setValue(String value)
    {
        if (UIComponentTag.isValueReference(value))
        {
            _type = TYPE_VALUE_BINDING;
            _value = value.intern();
        }
        else
        {
            _type = TYPE_OBJECT;
            if (_propertyClass != null)
            {
                _value = ClassUtils.convertToType(value, _propertyClass);
            }
            else
            {
                _value = value.intern();
            }
        }
    }

    public Object getValue()
    {
        return _value;
    }

    /** 
     * Used to update the value with a coerced object matching the type of the
     * property later during execution 
     */
    public void setValue(Object value) {
        _value = value;
    }

    public ValueBinding getValueBinding()
    {
        return _valueBinding;
    }
    
    /** 
     * Used to update the ValueBinding later during execution 
     */
    public void setValueBinding(ValueBinding valueBinding) {
        _valueBinding = valueBinding;
    }

    public void setListEntriesConfig(ListEntriesConfig listEntriesConfig)
    {
        _listEntriesConfig = listEntriesConfig;
        _type = TYPE_LIST;
    }

    public ListEntriesConfig getListEntriesConfig()
    {
        return _listEntriesConfig;
    }

    public void setMapEntriesConfig(MapEntriesConfig mapEntriesConfig)
    {
        _mapEntriesConfig = mapEntriesConfig;
        _type = TYPE_MAP;
    }

    public MapEntriesConfig getMapEntriesConfig()
    {
        return _mapEntriesConfig;
    }

    public int getType()
    {
        return _type;
    }

}

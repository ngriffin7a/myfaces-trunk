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

import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
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

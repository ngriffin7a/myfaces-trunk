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
package javax.faces.component;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import java.beans.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _ComponentAttributesMap
        implements Map, Serializable
{
    private static final Object[] EMPTY_ARGS = new Object[0];

    private UIComponent _component;
    private Map _attributes = null;    //We delegate instead of derive from HashMap, so that we can later optimize Serialization
    private transient Map _propertyDescriptorMap = null;

    _ComponentAttributesMap(UIComponent component)
    {
        _component = component;
        _attributes = new HashMap();
    }

    _ComponentAttributesMap(UIComponent component, Map attributes)
    {
        _component = component;
        _attributes = attributes;
    }

    public int size()
    {
        return _attributes.size();
    }

    public void clear()
    {
        _attributes.clear();
    }

    public boolean isEmpty()
    {
        return _attributes.isEmpty();
    }

    public boolean containsKey(Object key)
    {
        checkKey(key);
        if (getPropertyDescriptor((String)key) == null)
        {
            return _attributes.containsKey(key);
        }
        else
        {
            return false;
        }
    }

    /**
     * @param value  null is allowed
     */
    public boolean containsValue(Object value)
    {
        return _attributes.containsValue(value);
    }

    public Collection values()
    {
        return _attributes.values();
    }

    public void putAll(Map t)
    {
        for (Iterator it = t.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Entry)it.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    public Set entrySet()
    {
        return _attributes.entrySet();
    }

    public Set keySet()
    {
        return _attributes.keySet();
    }

    public Object get(Object key)
    {
        checkKey(key);
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor((String)key);
        if (propertyDescriptor != null)
        {
            return getComponentProperty(propertyDescriptor);
        }
        else
        {
            return _attributes.get(key);
        }
    }

    public Object remove(Object key)
    {
        checkKey(key);
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor((String)key);
        if (propertyDescriptor != null)
        {
            throw new IllegalArgumentException("Cannot remove component property attribute");
        }
        return _attributes.remove(key);
    }

    /**
     * @param key   String, null is not allowed
     * @param value null is allowed
     */
    public Object put(Object key, Object value)
    {
        checkKey(key);
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor((String)key);
        if (propertyDescriptor != null)
        {
            if (propertyDescriptor.getReadMethod() != null)
            {
                Object oldValue = getComponentProperty(propertyDescriptor);
                setComponentProperty(propertyDescriptor, value);
                return oldValue;
            }
            else
            {
                setComponentProperty(propertyDescriptor, value);
                return null;
            }
        }
        else
        {
            return _attributes.put(key, value);
        }
    }


    private PropertyDescriptor getPropertyDescriptor(String key)
    {
        if (_propertyDescriptorMap == null)
        {
            BeanInfo beanInfo;
            try
            {
                beanInfo = Introspector.getBeanInfo(_component.getClass());
            }
            catch (IntrospectionException e)
            {
                throw new FacesException(e);
            }
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            _propertyDescriptorMap = new HashMap();
            for (int i = 0; i < propertyDescriptors.length; i++)
            {
                PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
                if (!(propertyDescriptor instanceof IndexedPropertyDescriptor))
                {
                    _propertyDescriptorMap.put(propertyDescriptor.getName(),
                                               propertyDescriptor);
                }
            }
        }
        return (PropertyDescriptor)_propertyDescriptorMap.get(key);
    }


    private Object getComponentProperty(PropertyDescriptor propertyDescriptor)
    {
        Method readMethod = propertyDescriptor.getReadMethod();
        if (readMethod == null)
        {
            throw new IllegalArgumentException("Component property " + propertyDescriptor.getName() + " is not readable");
        }
        try
        {
            return readMethod.invoke(_component, EMPTY_ARGS);
        }
        catch (Exception e)
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            throw new FacesException("Could not get property " + propertyDescriptor.getName() + " of component " + _component.getClientId(facesContext), e);
        }
    }


    private void setComponentProperty(PropertyDescriptor propertyDescriptor, Object value)
    {
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if (writeMethod == null)
        {
            throw new IllegalArgumentException("Component property " + propertyDescriptor.getName() + " is not writable");
        }
        try
        {
            writeMethod.invoke(_component, new Object[] {value});
        }
        catch (Exception e)
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            throw new FacesException("Could not set property " + propertyDescriptor.getName() + " of component " + _component.getClientId(facesContext), e);
        }
    }


    private void checkKey(Object key)
    {
        if (key == null) throw new NullPointerException("key");
        if (!(key instanceof String)) throw new ClassCastException("key is not a String");
    }

    Map getUnderlyingMap()
    {
        return _attributes;
    }
}

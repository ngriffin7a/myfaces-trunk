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
package net.sourceforge.myfaces.el;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class PropertyResolverImpl
    extends PropertyResolver
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Object[] NO_ARGS = {};

    //~ Methods ------------------------------------------------------------------------------------

    public static void setProperty(Object base, String name, Object newValue)
    {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(base, name);

        Method             m = propertyDescriptor.getWriteMethod();

        if (m == null)
        {
            throw new PropertyNotFoundException("Bean: " + base.getClass() + ", property: " + name);
        }

        try
        {
            m.invoke(
                base,
                new Object[] {newValue});
        }
        catch (Throwable t)
        {
            throw new EvaluationException("Bean: " + base.getClass() + ", property: " + name, t);
        }
    }

    public static PropertyDescriptor getPropertyDescriptor(Object base, String name)
    {
        PropertyDescriptor propertyDescriptor;

        try
        {
            propertyDescriptor =
                findPropertyDescriptor(
                    Introspector.getBeanInfo(base.getClass()),
                    name);
        }
        catch (IntrospectionException e)
        {
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", property: " + name, e);
        }

        return propertyDescriptor;
    }

    public boolean isReadOnly(Object base, String name)
    throws PropertyNotFoundException
    {
        if (base == null)
        {
            throw new PropertyNotFoundException("Null bean, property: " + name);
        }

        if ((name == null) || (name.length() == 0))
        {
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", null or empty property name");
        }

        // Is there any way to determine whether Map.put() will fail?
        if (base instanceof Map)
        {
            return false;
        }

        if (base instanceof UIComponent)
        {
            return true;
        }

        // If none of the special bean types, then process as normal Bean
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(base, name);

        return propertyDescriptor.getWriteMethod() == null;
    }

    public boolean isReadOnly(Object base, int index)
    throws PropertyNotFoundException
    {
        if (base == null)
        {
            throw new PropertyNotFoundException("Null bean, index: " + index);
        }

        // Is there any way to determine whether List.set() will be declined?
        if (base instanceof List || base.getClass().isArray())
        {
            return false;
        }

        if (base instanceof UIComponent)
        {
            return true;
        }

        throw new ReferenceSyntaxException(
            "Must be array or List. Bean: " + base.getClass() + ", index " + index);
    }

    public Class getType(Object base, String name)
    throws PropertyNotFoundException
    {
        if (base == null)
        {
            throw new PropertyNotFoundException("Null bean, property: " + name);
        }

        if ((name == null) || (name.length() == 0))
        {
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", null or empty property name");
        }

        if (base instanceof Map)
        {
            Object value = ((Map) base).get(name);

            return (value == null) ? Object.class : value.getClass(); // REVISIT: when generics are imlemented in JVM 1.5
        }

        if (base instanceof UIComponent)
        {
            return UIComponent.class;
        }

        // If none of the special bean types, then process as normal Bean
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(base, name);

        return propertyDescriptor.getPropertyType();
    }

    public Class getType(Object base, int index)
    throws PropertyNotFoundException
    {
        if (base == null)
        {
            throw new PropertyNotFoundException("Null bean, index: " + index);
        }

        if (base.getClass().isArray())
        {
            return base.getClass().getComponentType();
        }

        try
        {
            if (base instanceof List)
            {
                // REVISIT: does it make sense to do this or simply return Object.class? What if the new value is not of the old value's class?
                Object value = ((List) base).get(index);

                return (value != null) ? value.getClass() : Object.class; // until generics are implemented in JVM 1.5
            }
        }
        catch (Throwable t)
        {
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", index: " + index, t);
        }

        if (base instanceof UIComponent)
        {
            return UIComponent.class;
        }

        throw new ReferenceSyntaxException(
            "Must be array or List. Bean: " + base.getClass() + ", index " + index);
    }

    public void setValue(Object base, int index, Object newValue)
    throws PropertyNotFoundException
    {
        if (base == null)
        {
            throw new PropertyNotFoundException("Null bean, index: " + index);
        }

        // TODO: convert newValue to property type
        try
        {
            if (base.getClass().isArray())
            {
                Array.set(base, index, newValue);

                return;
            }

            if (base instanceof List)
            {
                // REVISIT: should we try to grow the list, if growable type (e.g., ArrayList, etc.), and if not large enough?
                ((List) base).set(index, newValue);

                return;
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            throw new PropertyNotFoundException("Bean: " + base.getClass() + ", index " + index, e);
        }

        throw new ReferenceSyntaxException(
            "Must be array or List. Bean: " + base.getClass() + ", index " + index);
    }

    public void setValue(Object base, String name, Object newValue)
    throws PropertyNotFoundException
    {
        // TODO: convert newValue to property type
        if (base == null)
        {
            throw new PropertyNotFoundException("Null bean, property: " + name);
        }

        if ((name == null) || (name.length() == 0))
        {
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", null or empty property name");
        }

        if (base instanceof Map)
        {
            ((Map) base).put(name, newValue);

            return;
        }

        if (base instanceof UIComponent)
        {
            throw new PropertyNotFoundException("Bean must not be UIComponent, property: " + name);
        }

        // If none of the special bean types, then process as normal Bean
        setProperty(base, name, newValue);
    }

    public Object getValue(Object base, String name)
    {
        if ((base == null) || (name == null) || (name.length() == 0))
        {
            return null;
        }

        if (base instanceof Map)
        {
            return ((Map) base).get(name);
        }

        if (base instanceof UIComponent)
        {
            for (
                Iterator children = ((UIComponent) base).getChildren().iterator();
                        children.hasNext();)
            {
                UIComponent child = (UIComponent) children.next();

                if (name.equals(child.getId()))
                {
                    return child;
                }
            }

            return null;
        }

        // If none of the special bean types, then process as normal Bean
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(base, name);

        Method             m = propertyDescriptor.getReadMethod();

        if (m == null)
        {
            throw new PropertyNotFoundException("Bean: " + base.getClass() + ", property: " + name);
        }

        try
        {
            return m.invoke(base, NO_ARGS);
        }
        catch (Throwable t)
        {
            throw new EvaluationException("Bean: " + base.getClass() + ", property: " + name, t);
        }
    }

    public Object getValue(Object base, int index)
    throws PropertyNotFoundException
    {
        if (base == null)
        {
            return null;
        }

        try
        {
            if (base.getClass().isArray())
            {
                return Array.get(base, index);
            }

            if (base instanceof List)
            {
                return ((List) base).get(index);
            }

            if (base instanceof UIComponent)
            {
                return ((UIComponent) base).getChildren().get(index);
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            // Note: ArrayIndexOutOfBoundsException also here
            return null;
        }

        throw new ReferenceSyntaxException(
            "Must be array or List. Bean: " + base.getClass() + ", index " + index);
    }

    public static PropertyDescriptor findPropertyDescriptor(BeanInfo beanInfo, String propertyName)
    {
        PropertyDescriptor[] propDescriptors = beanInfo.getPropertyDescriptors();

        // TODO: cache this in classLoader safe way
        // binary search
        for (int l = 0, h = propDescriptors.length - 1, i = h >> 1; l <= h; i = (l + h) >> 1)
        {
            int compare = propDescriptors[i].getName().compareTo(propertyName);

            if (compare > 0)
            {
                h = i - 1;
            }
            else if (compare < 0)
            {
                l = i + 1;
            }
            else
            {
                return propDescriptors[i];
            }
        }

        throw new PropertyNotFoundException(
            "Bean: " + beanInfo.getBeanDescriptor().getBeanClass() + ", property: " + propertyName);
    }
}

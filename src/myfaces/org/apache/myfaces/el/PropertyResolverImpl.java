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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class PropertyResolverImpl extends PropertyResolver
{
    private static final Log log = LogFactory.getLog(PropertyResolverImpl.class);

    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Object[] NO_ARGS = {};

    //~ Public PropertyResolver Methods ------------------------------------------------------------

    public Object getValue(Object base, Object property)
            throws EvaluationException, PropertyNotFoundException
    {
        try
        {
            if (base == null || property == null ||
                property instanceof String && ((String)property).length() == 0)
            {
                return null;
            }
            if (base instanceof Map)
            {
                return ((Map) base).get(property);
            }
            if (base instanceof UIComponent)
            {
                for (Iterator children = ((UIComponent) base).getChildren().iterator(); children.hasNext();)
                {
                    UIComponent child = (UIComponent) children.next();

                    if (property.equals(child.getId()))
                    {
                        return child;
                    }
                }

                return null;
            }

            // If none of the special bean types, then process as normal Bean
            return getProperty(base, property.toString());
        }
        catch (RuntimeException e)
        {
            log.error("Exception getting value of property " + property + " of bean " +
                      base != null ? base.getClass().getName() : "NULL", e);
            throw e;
        }
    }

    public Object getValue(Object base, int index)
            throws EvaluationException, PropertyNotFoundException
    {
        try
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

            throw new ReferenceSyntaxException("Must be array or List. Bean: " + base.getClass().getName() + ", index " + index);
        }
        catch (RuntimeException e)
        {
            log.error("Exception getting value for index " + index + " of bean " +
                      base != null ? base.getClass().getName() : "NULL", e);
            throw e;
        }
    }

    public void setValue(Object base, Object property, Object newValue)
            throws EvaluationException, PropertyNotFoundException
    {
        try
        {
            // TODO: convert newValue to property type
            if (base == null)
            {
                throw new PropertyNotFoundException("Null bean, property: " + property);
            }
            if (property == null ||
                property instanceof String && ((String)property).length() == 0)
            {
                throw new PropertyNotFoundException("Bean: " + base.getClass().getName() + ", null or empty property property");
            }

            if (base instanceof Map)
            {
                ((Map) base).put(property, newValue);

                return;
            }
            if (base instanceof UIComponent)
            {
                throw new PropertyNotFoundException("Bean must not be UIComponent, property: " + property);
            }

            // If none of the special bean types, then process as normal Bean
            setProperty(base, property.toString(), newValue);
        }
        catch (RuntimeException e)
        {
            log.error("Exception setting value of property " + property + " of bean " +
                      base != null ? base.getClass().getName() : "NULL", e);
            throw e;
        }
    }

    public void setValue(Object base, int index, Object newValue)
            throws EvaluationException, PropertyNotFoundException
    {
        try
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
                throw new PropertyNotFoundException("Bean: " + base.getClass().getName() + ", index " + index, e);
            }

            throw new ReferenceSyntaxException("Must be array or List. Bean: " + base.getClass().getName() + ", index " + index);
        }
        catch (RuntimeException e)
        {
            log.error("Exception setting value of index " + index + " of bean " +
                      base != null ? base.getClass().getName() : "NULL", e);
            throw e;
        }
    }

    public boolean isReadOnly(Object base, Object property)
            throws EvaluationException, PropertyNotFoundException
    {
        try
        {
            if (base == null)
            {
                throw new PropertyNotFoundException("Null bean, property: " + property);
            }
            if (property == null ||
                property instanceof String && ((String)property).length() == 0)
            {
                throw new PropertyNotFoundException("Bean: " + base.getClass().getName() + ", null or empty property property");
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
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(base,
                                                                          property.toString());

            return propertyDescriptor.getWriteMethod() == null;
        }
        catch (RuntimeException e)
        {
            log.error("Exception determining readonly for property " + property + " of bean " +
                      base != null ? base.getClass().getName() : "NULL", e);
            throw e;
        }
    }

    public boolean isReadOnly(Object base, int index)
            throws EvaluationException, PropertyNotFoundException
    {
        try
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

            throw new ReferenceSyntaxException("Must be array or List. Bean: " + base.getClass().getName() + ", index " + index);
        }
        catch (RuntimeException e)
        {
            log.error("Exception determining readonly for index " + index + " of bean " +
                      base != null ? base.getClass().getName() : "NULL", e);
            throw e;
        }
    }

    public Class getType(Object base, Object property)
            throws EvaluationException, PropertyNotFoundException
    {
        try
        {
            if (base == null)
            {
                throw new PropertyNotFoundException("Null bean, property: " + property);
            }
            if (property == null ||
                property instanceof String && ((String)property).length() == 0)
            {
                throw new PropertyNotFoundException("Bean: " + base.getClass().getName() + ", null or empty property property");
            }

            if (base instanceof Map)
            {
                Object value = ((Map) base).get(property);

                return (value == null) ? Object.class : value.getClass(); // REVISIT: when generics are imlemented in JVM 1.5
            }
            if (base instanceof UIComponent)
            {
                return UIComponent.class;
            }

            // If none of the special bean types, then process as normal Bean
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(base,
                                                                          property.toString());

            return propertyDescriptor.getPropertyType();
        }
        catch (RuntimeException e)
        {
            log.error("Exception determining type of property " + property + " of bean " +
                      base != null ? base.getClass().getName() : "NULL", e);
            throw e;
        }
    }

    public Class getType(Object base, int index)
            throws EvaluationException, PropertyNotFoundException
    {
        try
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
                throw new PropertyNotFoundException("Bean: " + base.getClass().getName() + ", index: " + index, t);
            }

            if (base instanceof UIComponent)
            {
                return UIComponent.class;
            }

            throw new ReferenceSyntaxException("Must be array or List. Bean: " + base.getClass().getName() + ", index " + index);
        }
        catch (RuntimeException e)
        {
            log.error("Exception determining type for index " + index + " of bean " +
                      base != null ? base.getClass().getName() : "NULL", e);
            throw e;
        }
    }


    //~ Internal Helper Methods ------------------------------------------------------------

    public static void setProperty(Object base, String name, Object newValue)
    {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(base, name);

        Method m = propertyDescriptor.getWriteMethod();
        if (m == null)
        {
            throw new PropertyNotFoundException("Bean: " + base.getClass().getName() + ", property: " + name);
        }

        try
        {
            m.invoke(base, new Object[] {newValue});
        }
        catch (Throwable t)
        {
            throw new EvaluationException("Bean: " + base.getClass().getName() + ", property: " + name, t);
        }
    }

    public static Object getProperty(Object base, String name)
    {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(base, name);

        Method m = propertyDescriptor.getReadMethod();
        if (m == null)
        {
            throw new PropertyNotFoundException("Bean: " + base.getClass().getName() + ", property: " + name);
        }

        try
        {
            return m.invoke(base, NO_ARGS);
        }
        catch (Throwable t)
        {
            throw new EvaluationException("Bean: " + base.getClass().getName() + ", property: " + name, t);
        }
    }

    public static PropertyDescriptor getPropertyDescriptor(Object base, String name)
    {
        PropertyDescriptor propertyDescriptor;

        try
        {
            propertyDescriptor =
                getPropertyDescriptor(Introspector.getBeanInfo(base.getClass()), name);
        }
        catch (IntrospectionException e)
        {
            throw new PropertyNotFoundException("Bean: " + base.getClass().getName() + ", property: " + name, e);
        }

        return propertyDescriptor;
    }

    public static PropertyDescriptor getPropertyDescriptor(BeanInfo beanInfo, String propertyName)
    {
        PropertyDescriptor[] propDescriptors = beanInfo.getPropertyDescriptors();

        // TODO: cache this in classLoader safe way
        // binary search
        for (int l = 0, h = propDescriptors.length - 1, i = h >> 1; l <= h; i = (l + h) >> 1)
        {
            //TODO: Exclude IndexedPropertyDescriptor ?
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

        throw new PropertyNotFoundException("Bean: " + beanInfo.getBeanDescriptor().getBeanClass().getName() + ", property: " + propertyName);
    }
}

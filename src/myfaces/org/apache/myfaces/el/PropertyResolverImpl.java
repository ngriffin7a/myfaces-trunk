/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;


/**
 * JSF 1.0 PRD2, 5.2.2
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class PropertyResolverImpl extends PropertyResolver {
    //~ Static fields/initializers ---------------------------------------------

    private static final Object[] EMPTY_ARGS = {};

    //~ Methods ----------------------------------------------------------------

    public static void setProperty(Object base, String name, Object newValue) {
        PropertyDescriptor propertyDescriptor =
            getPropertyDescriptor(base, name);

        Method             m = propertyDescriptor.getWriteMethod();

        if (m == null)
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", property: " + name);

        try {
            m.invoke(base, new Object[] {newValue});
        } catch (IllegalAccessException e) {
            throw new EvaluationException(
                "Bean: " + base.getClass() + ", property: " + name, e);
        } catch (InvocationTargetException e) {
            throw new EvaluationException(
                "Bean: " + base.getClass() + ", property: " + name, e);
        }
    }

    public static PropertyDescriptor getPropertyDescriptor(
        Object base, String name) {
        PropertyDescriptor propertyDescriptor;

        try {
            propertyDescriptor =
                findPropertyDescriptor(
                    Introspector.getBeanInfo(base.getClass()), name);
        } catch (IntrospectionException e) {
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", property: " + name, e);
        }

        if (propertyDescriptor == null)
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", property: " + name);

        return propertyDescriptor;
    }

    public boolean isReadOnly(Object base, String name)
    throws PropertyNotFoundException
    {
        if (base == null)
            throw new NullPointerException("Null bean, property: " + name);

        if ((name == null) || (name.length() == 0))
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", null or empty property name");

        // Is there any way to determine whether Map.put() will fail?
        if (base instanceof Map)
            return false;

        if (base instanceof UIComponent)
            return true;

        // If none of the special bean types, then process as normal Bean
        PropertyDescriptor propertyDescriptor =
            getPropertyDescriptor(base, name);

        return propertyDescriptor.getWriteMethod() == null;
    }

    public boolean isReadOnly(Object base, int index)
    throws PropertyNotFoundException
    {
        if (base == null)
            throw new NullPointerException("Null bean, index: " + index);

        // Is there any way to determine whether List.set() will be declined?
        if (base instanceof List || base.getClass().isArray())
            return false;

        if (base instanceof UIComponent)
            return true;

        throw new IllegalArgumentException(
            "Must be array or List. Bean: " + base.getClass() + ", index "
            + index);
    }

    public Class getType(Object base, String name)
    throws PropertyNotFoundException
    {
        if (base == null)
            throw new NullPointerException("Null bean, property: " + name);

        if ((name == null) || (name.length() == 0))
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", null or empty property name");

        if (base instanceof Map)
            return Object.class; // until variable datatypes are imlemented in JVM 1.5

        if (base instanceof UIComponent)
            return ((UIComponent)base).findComponent(name).getClass();

        // If none of the special bean types, then process as normal Bean
        PropertyDescriptor propertyDescriptor =
            getPropertyDescriptor(base, name);

        return propertyDescriptor.getPropertyType();
    }

    public Class getType(Object base, int index)
    throws PropertyNotFoundException
    {
        if (base == null)
            throw new PropertyNotFoundException(
                "Null bean (getting type of index " + index + ")");

        try {
            if (base instanceof List) {
                // REVISIT: does it make sense to do this or simply return Object.class?
                List   l = (List)base;

                Object o = l.get(index);

                if (o != null)
                    return o.getClass();

                return Object.class; // until variable datatype in JVM 1.5 is implemented
            }

            if (base.getClass().isArray())
                return base.getClass().getComponentType();

            if (base instanceof UIComponent)
                return ((UIComponent)base).getChild(index).getClass();
        } catch (IndexOutOfBoundsException e) {
            throw e;
        } catch (Throwable t) {
            return null;
        }

        throw new IllegalArgumentException(
            "Must be array or List. Bean: " + base.getClass() + ", index "
            + index);
    }

    public void setValue(Object base, int index, Object newValue)
    throws PropertyNotFoundException
    {
        // TODO: convert newValue to property type
        if (base == null)
            throw new NullPointerException("Null bean, index: " + index);

        // Note: IndexOutOfBoundsException will be handled by the access methods
        try {
            if (base instanceof List) {
                ((List)base).add(index, newValue);

                return;
            }

            if (base.getClass().isArray()) {
                Array.set(base, index, newValue);

                return;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", index " + index, e);
        }

        throw new IllegalArgumentException(
            "Must be array or List. Bean: " + base.getClass() + ", index "
            + index);
    }

    public void setValue(Object base, String name, Object newValue)
    throws PropertyNotFoundException
    {
        // TODO: convert newValue to property type
        if (base == null)
            throw new NullPointerException("Null bean, property: " + name);

        if ((name == null) || (name.length() == 0))
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", null or empty property name");

        if (base instanceof Map) {
            ((Map)base).put(name, newValue);

            return;
        }

        if (base instanceof UIComponent)
            throw new IllegalArgumentException(
                "Bean must not be UIComponent, property: " + name);

        // If none of the special bean types, then process as normal Bean
        setProperty(base, name, newValue);
    }

    public Object getValue(Object base, String name) {
        if ((base == null) || (name == null) || (name.length() == 0))
            return null; // (see JSF 1.0, PRD2, 5.1.2.1)

        if (base instanceof Map)
            return ((Map)base).get(name);

        if (base instanceof UIComponent)
            return ((UIComponent)base).findComponent(name);

        // If none of the special bean types, then process as normal Bean
        PropertyDescriptor propertyDescriptor =
            getPropertyDescriptor(base, name);

        Method             m = propertyDescriptor.getReadMethod();

        if (m == null)
            throw new PropertyNotFoundException(
                "Bean: " + base.getClass() + ", property: " + name);

        try {
            return m.invoke(base, EMPTY_ARGS);
        } catch (IllegalAccessException e) {
            throw new EvaluationException(
                "Bean: " + base.getClass() + ", property: " + name, e);
        } catch (InvocationTargetException e) {
            throw new EvaluationException(
                "Bean: " + base.getClass() + ", property: " + name, e);
        }
    }

    public Object getValue(Object base, int index)
    throws PropertyNotFoundException
    {
        if (base == null)
            return null; // (see JSF 1.0, PRD2, 5.1.2.1)

        try {
            if (base instanceof List)
                return ((List)base).get(index);

            if (base.getClass().isArray())
                return Array.get(base, index);

            if (base instanceof UIComponent)
                return ((UIComponent)base).getChild(index);
        } catch (IndexOutOfBoundsException e) {
            // Note: ArrayIndexOutOfBoundsException also here
            return null; // (see JSF 1.0, PRD2, 5.1.2.1)
        }

        throw new IllegalArgumentException(
            "Must be array or List. Bean: " + base.getClass() + ", index "
            + index);
    }

    public static PropertyDescriptor findPropertyDescriptor(
        BeanInfo beanInfo, String propertyName) {
        PropertyDescriptor[] propDescriptors =
            beanInfo.getPropertyDescriptors();

        // TODO: cache this in classLoader safe way
        for (int i = 0, len = propDescriptors.length; i < len; i++) {
            if (propDescriptors[i].getName().equals(propertyName))
                return propDescriptors[i];
        }

        return null;
    }
}

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
package net.sourceforge.myfaces.util.bean;

import net.sourceforge.myfaces.util.logging.LogUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 * Convenient bean property get and set methods that support
 * nested bean properties.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class BeanUtils
{
    private static final Object[] EMPTY_ARGS = new Object[]{};

    private BeanUtils()
    {
    }

    /**
     * Get BeanInfo for the given Object.
     * Since Introspector already caches recent BeanInfos we do not
     * need to cache them ourselves.
     * @param bean
     * @return
     */
    public static BeanInfo getBeanInfo(Object bean)
    {
        try
        {
            return Introspector.getBeanInfo(bean.getClass());
        }
        catch (IntrospectionException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static PropertyDescriptor findPropertyDescriptor(Object bean,
                                                            String propertyName)
    {
        return findPropertyDescriptor(getBeanInfo(bean), propertyName);
    }

    public static PropertyDescriptor findNestedPropertyDescriptor(Object bean,
                                                                  String propertyName)
    {
        if (isNestedPropertyName(propertyName))
        {
            Object[] nested = getNestedBeanAndPropertyName(bean, propertyName);
            bean = nested[0];
            propertyName = (String)nested[1];
        }
        return findPropertyDescriptor(getBeanInfo(bean), propertyName);
    }

    public static PropertyDescriptor findPropertyDescriptor(BeanInfo beanInfo,
                                                            String propertyName)
    {
        PropertyDescriptor propDescriptors[] = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propDescriptors.length; i++)
        {
            if (propDescriptors[i].getName().equals(propertyName))
            {
                return propDescriptors[i];
            }
        }
        return null;
    }


    /**
     * @param bean
     * @param propertyName
     * @return value of this bean's property with the specified name
     */
    public static Object getBeanPropertyValue(Object bean,
                                              String propertyName)
    {
        if (isNestedPropertyName(propertyName))
        {
            Object[] nested = getNestedBeanAndPropertyName(bean, propertyName);
            bean = nested[0];
            propertyName = (String)nested[1];
        }
        return getBeanPropertyValue(bean, propertyName, getBeanInfo(bean));
    }

    /**
     * Optimized version where BeanInfo already is available.
     * @param bean
     * @param propertyName
     * @param beanInfo
     * @return
     */
    public static Object getBeanPropertyValue(Object bean,
                                              String propertyName,
                                              BeanInfo beanInfo)
    {
        if (isNestedPropertyName(propertyName))
        {
            throw new IllegalArgumentException("Optimized version cannot handle nested beans.");
        }
        PropertyDescriptor propertyDescriptor = findPropertyDescriptor(beanInfo, propertyName);
        if (propertyDescriptor == null)
        {
            throw new IllegalArgumentException("Bean " + bean + " of class " + bean.getClass() + " does not have a property '" + propertyName + "'.");
        }
        return getBeanPropertyValue(bean, propertyDescriptor);
    }

    /**
     * Optimized version where PropertyDescriptor already is available.
     * @param bean
     * @param propertyDescriptor
     * @return
     */
    public static Object getBeanPropertyValue(Object bean,
                                              PropertyDescriptor propertyDescriptor)
    {
        if (bean == null)
        {
            throw new NullPointerException("Could not get value of property '" + propertyDescriptor.getName() + "' because bean was null.");
        }
        Method m = propertyDescriptor.getReadMethod();
        if (m == null)
        {
            throw new RuntimeException("Bean " + bean + " does not have a getter method for property '" + propertyDescriptor.getName() + "'.");
        }
        try
        {
            return m.invoke(bean, EMPTY_ARGS);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Cannot invoke " + bean.getClass() + "." + m.getName() + "() with return type " + m.getReturnType() + "\n", e);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException("Cannot invoke " + bean.getClass() + "." + m.getName() + "() with return type " + m.getReturnType() + "\n", e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException("Exception when invoking " + bean.getClass() + "." + m.getName() + "() with return type " + m.getReturnType() + "\n", e);
        }
    }


    public static void setBeanPropertyValue(Object bean,
                                            String propertyName,
                                            Object propertyValue)
    {
        if (isNestedPropertyName(propertyName))
        {
            Object[] nested = getNestedBeanAndPropertyName(bean, propertyName);
            bean = nested[0];
            propertyName = (String)nested[1];
        }
        setBeanPropertyValue(bean, propertyName, propertyValue, getBeanInfo(bean));
    }

    /**
     * Optimized version where BeanInfo already is available.
     */
    public static void setBeanPropertyValue(Object bean,
                                            String propertyName,
                                            Object propertyValue,
                                            BeanInfo beanInfo)
    {
        if (isNestedPropertyName(propertyName))
        {
            throw new IllegalArgumentException("Optimized version cannot handle nested beans.");
        }
        PropertyDescriptor propertyDescriptor = findPropertyDescriptor(beanInfo, propertyName);
        if (propertyDescriptor == null)
        {
            throw new IllegalArgumentException("Bean " + bean + " of class " + bean.getClass() + " does not have a property '" + propertyName + ".");
        }
        setBeanPropertyValue(bean, propertyDescriptor, propertyValue);
    }

    /**
     * Optimized version where PropertyDescriptor already is available.
     */
    public static void setBeanPropertyValue(Object bean,
                                            PropertyDescriptor propertyDescriptor,
                                            Object propertyValue)
    {
        Method m = propertyDescriptor.getWriteMethod();
        if (m == null)
        {
            throw new RuntimeException("Bean " + bean + " does not have a setter method for property " + propertyDescriptor.getName());
        }
        try
        {
            m.invoke(bean, new Object[]{propertyValue});
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param bean
     * @param propertyName
     * @return
     * @throws IllegalArgumentException if the bean has no property with this name
     */
    public static Class getBeanPropertyType(Object bean,
                                            String propertyName)
    {
        if (isNestedPropertyName(propertyName))
        {
            Object[] nested = getNestedBeanAndPropertyName(bean, propertyName);
            bean = nested[0];
            propertyName = (String)nested[1];
        }
        return getBeanPropertyType(getBeanInfo(bean), propertyName);
    }

    /**
     * Optimized version where BeanInfo already is available.
     *
     * @param beanInfo
     * @param propertyName
     * @return
     * @throws IllegalArgumentException if the bean has no property with this name
     */
    public static Class getBeanPropertyType(BeanInfo beanInfo,
                                            String propertyName)
    {
        if (isNestedPropertyName(propertyName))
        {
            throw new IllegalArgumentException("Optimized version cannot handle nested beans.");
        }
        PropertyDescriptor propertyDescriptor = findPropertyDescriptor(beanInfo, propertyName);
        if (propertyDescriptor == null)
        {
            throw new IllegalArgumentException("Bean " + beanInfo.getBeanDescriptor().getName() + " of class " + beanInfo.getBeanDescriptor().getBeanClass() + " does not have a property '" + propertyName + ".");
        }
        return propertyDescriptor.getPropertyType();
    }


    private static boolean isNestedPropertyName(String propertyName)
    {
        return propertyName.indexOf('.') >= 0;
    }

    /**
     *
     * @param bean
     * @param propertyPath
     * @return {NestedBean, PropertyName}
     */
    private static Object[] getNestedBeanAndPropertyName(Object bean,
                                                         String propertyPath)
    {
        Object obj = bean;
        if (obj == null)
        {
            throw new IllegalArgumentException("Bean is null!");
        }
        StringTokenizer st = new StringTokenizer(propertyPath, ".");
        String nextProp;
        while (true)
        {
            nextProp = st.nextToken();
            if (!st.hasMoreTokens())
            {
                return new Object[] {obj, nextProp};
            }

            PropertyDescriptor propDescr = findPropertyDescriptor(obj, nextProp);
            if (propDescr == null)
            {
                throw new IllegalArgumentException("Bean " + obj + " (Class " + obj.getClass() + ") does not have a property of name '" + nextProp + "'.");
            }

            Method m = propDescr.getReadMethod();
            Object newObj;
            try
            {
                newObj = m.invoke(obj, EMPTY_ARGS);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }

            obj = newObj;
            if (obj == null)
            {
                LogUtil.getLogger().warning("Nested property '" + nextProp + "' of bean " + bean + " is null.");
                return new Object[2];
            }
        }
    }


    /**
     * Finds a nested method that is given by the specified propertyPath.
     * @param bean
     * @param propertyPath
     * @param parameterTypes  null means find any method with this name
     * @return a Method
     */
    public static BeanMethod findNestedBeanMethod(Object bean,
                                                  String propertyPath,
                                                  Class[] parameterTypes)
    {
        Object obj;
        String methodName;

        if (isNestedPropertyName(propertyPath))
        {
            Object[] nested = getNestedBeanAndPropertyName(bean, propertyPath);
            obj = nested[0];
            methodName = (String)nested[1];
        }
        else
        {
            obj = bean;
            methodName = propertyPath;
        }

        if (obj == null)
        {
            LogUtil.getLogger().finest("Bean object is null.");
            return null;
        }

        boolean hasNullParameterType = false;
        for (int i = 0; i < parameterTypes.length; i++)
        {
            if (parameterTypes[i] == null)
            {
                hasNullParameterType = true;
                break;
            }
        }

        Method method = null;
        try
        {
            if (hasNullParameterType)
            {
                //Find any method with this number of parameters
                Method[] methods = obj.getClass().getMethods();
                for (int i = 0; i < methods.length; i++)
                {
                    if (methods[i].getName().equals(methodName) &&
                        methods[i].getParameterTypes().length == parameterTypes.length)
                    {
                        if (method != null)
                        {
                            LogUtil.getLogger().severe("Bean class " + bean.getClass().getName() + " has more than one matching '" + methodName + "' methods!");
                        }
                        else
                        {
                            method = methods[i];
                        }
                    }
                }
                if (method == null)
                {
                    throw new NoSuchMethodException("Bean class " + bean.getClass().getName() + " does not have a matching method '" + methodName + "'!");
                }
            }
            else
            {
                method = obj.getClass().getMethod(methodName, parameterTypes);
            }
        }
        catch (NoSuchMethodException e)
        {
            method = null;
        }
        catch (SecurityException e)
        {
            throw new RuntimeException(e);
        }

        return new BeanMethod(obj, method);
    }


    public static void copyProperties(Object fromBean,
                                      Object toBean,
                                      String[] propertyNames)
    {
        for (int i = 0; i < propertyNames.length; i++)
        {
            String p = propertyNames[i];
            Object obj = getBeanPropertyValue(fromBean, p);
            setBeanPropertyValue(toBean, p, obj);
        }
    }


    /**
     * Strip "${" and "}" from a modelReference.
     */
    public static String stripBracketsFromModelReference(String modelReference)
    {
        modelReference = modelReference.trim();
        if (modelReference.startsWith("${") && modelReference.endsWith("}"))
        {
            return modelReference.substring(2, modelReference.length() - 1);
        }
        else
        {
            return modelReference;
        }
    }

}

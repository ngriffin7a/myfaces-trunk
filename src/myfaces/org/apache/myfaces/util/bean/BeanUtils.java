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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 * TODO: description
 * TODO: Array/List properties
 * Can this methods be replaced by jakarta-commons-bean library?
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class BeanUtils
{
    private static final Class[] NO_METHOD_PARAMS = new Class[0];

    public static Object getBeanProperty(Object bean, String property)
    {
        BeanMethod beanMethod = getBeanPropertyGetMethod(bean, property);
        Object obj = beanMethod.getBean();
        if (obj == null)
        {
            LogUtil.getLogger().info("Getting property of a null bean.");
            return null;
        }
        Method method = beanMethod.getMethod();
        try
        {
            return method.invoke(obj, new Object[0]);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e.getTargetException());
        }
    }

    public static Object setBeanProperty(Object bean, String property, Object newValue)
        throws IllegalArgumentException
    {
        BeanMethod beanMethod;
        if (newValue == null)
        {
            beanMethod = getBeanPropertySetMethod(bean, property, null);
        }
        else
        {
            beanMethod = getBeanPropertySetMethod(bean, property, newValue.getClass());
        }
        Object obj = beanMethod.getBean();
        if (obj == null)
        {
            LogUtil.getLogger().warning("Cannot set property of a null bean.");
            return null;
        }
        Method method = beanMethod.getMethod();
        if (method == null)
        {
            Object nestedBean = beanMethod.getBean();
            int i = property.lastIndexOf('.');
            String propName = (i == -1 ? property : property.substring(i + 1));
            throw new IllegalArgumentException("Bean " + nestedBean + " (Class " + nestedBean.getClass() + ") does not have a set method for a property '" + propName + "' of type " + newValue == null ? "'unknown'" : newValue.getClass().getName() + ".");
        }

        try
        {
            if (newValue == null)
            {
                LogUtil.getLogger().finest("Setting null property.");
                Class propertyType = method.getParameterTypes()[0];
                if (propertyType.isPrimitive())
                {
                    LogUtil.getLogger().warning("Setting primitive bean property to null - choosing appropriate '0' value.");
                    if (propertyType == Boolean.TYPE)
                    {
                        return method.invoke(obj, new Object[]{new Boolean(false)});
                    }
                    else if (propertyType == Byte.TYPE)
                    {
                        return method.invoke(obj, new Object[]{new Byte((byte)0)});
                    }
                    else if (propertyType == Integer.TYPE)
                    {
                        return method.invoke(obj, new Object[]{new Integer(0)});
                    }
                    else if (propertyType == Short.TYPE)
                    {
                        return method.invoke(obj, new Object[]{new Short((short)0)});
                    }
                    else if (propertyType == Long.TYPE)
                    {
                        return method.invoke(obj, new Object[]{new Long(0)});
                    }
                    else if (propertyType == Character.TYPE)
                    {
                        return method.invoke(obj, new Object[]{new Character((char)0)});
                    }
                    else if (propertyType == Double.TYPE)
                    {
                        return method.invoke(obj, new Object[]{new Double(0)});
                    }
                    else if (propertyType == Float.TYPE)
                    {
                        return method.invoke(obj, new Object[]{new Float(0)});
                    }
                    throw new UnsupportedOperationException("Unsupported primitive type " + propertyType.getName());
                }
                else
                {
                    return method.invoke(obj, new Object[]{newValue});
                }
            }
            else
            {
                return method.invoke(obj, new Object[]{newValue});
            }
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e.getTargetException());
        }
    }

    public static Class getBeanPropertyType(Object bean, String property)
        throws IllegalArgumentException
    {
        BeanMethod beanMethod = getBeanPropertyGetMethod(bean, property);
        return beanMethod.getMethod().getReturnType();
    }

    private static BeanMethod getBeanPropertyGetMethod(Object bean,
                                                       String propertyPath)
        throws IllegalArgumentException
    {
        BeanMethod beanMethod = findNestedBeanMethod(bean, propertyPath, "get", NO_METHOD_PARAMS);
        if (beanMethod.getBean() != null)
        {
            if (beanMethod.getMethod() == null)
            {
                beanMethod = findNestedBeanMethod(bean, propertyPath, "is", NO_METHOD_PARAMS);
            }
            if (beanMethod.getMethod() == null)
            {
                Object nestedBean = beanMethod.getBean();
                int i = propertyPath.lastIndexOf('.');
                String propName = (i == -1 ? propertyPath : propertyPath.substring(i + 1));
                throw new IllegalArgumentException("Bean " + nestedBean + " (Class " + nestedBean.getClass() + ") does not have a property '" + propName + "'.");
            }
        }
        return beanMethod;
    }

    public static BeanMethod getBeanPropertySetMethod(Object bean,
                                                       String propertyPath,
                                                       Class propertyType)
    {
        if (propertyType == null)
        {
            return findNestedBeanMethod(bean, propertyPath, "set", new Class[] {null});
        }

        BeanMethod beanMethod
            = findNestedBeanMethod(bean, propertyPath, "set",
                                   new Class[] {propertyType});
        if (beanMethod.getMethod() == null)
        {
            //try primitive types:
            if (propertyType == Boolean.class)
            {
                beanMethod
                    = findNestedBeanMethod(bean, propertyPath, "set",
                                           new Class[] {Boolean.TYPE});
            }
            else if (propertyType == Byte.class)
            {
                beanMethod
                    = findNestedBeanMethod(bean, propertyPath, "set",
                                           new Class[]{Byte.TYPE});
            }
            else if (propertyType == Integer.class)
            {
                beanMethod
                    = findNestedBeanMethod(bean, propertyPath, "set",
                                           new Class[]{Integer.TYPE});
            }
            else if (propertyType == Short.class)
            {
                beanMethod
                    = findNestedBeanMethod(bean, propertyPath, "set",
                                           new Class[]{Short.TYPE});
            }
            else if (propertyType == Long.class)
            {
                beanMethod
                    = findNestedBeanMethod(bean, propertyPath, "set",
                                           new Class[]{Long.TYPE});
            }
            else if (propertyType == Character.class)
            {
                beanMethod
                    = findNestedBeanMethod(bean, propertyPath, "set",
                                           new Class[]{Character.TYPE});
            }
            else if (propertyType == Double.class)
            {
                beanMethod
                    = findNestedBeanMethod(bean, propertyPath, "set",
                                           new Class[]{Double.TYPE});
            }
            else if (propertyType == Float.class)
            {
                beanMethod
                    = findNestedBeanMethod(bean, propertyPath, "set",
                                           new Class[]{Float.TYPE});
            }

        }
        return beanMethod;
    }


    /**
     * @param bean
     * @param propertyPath
     * @param methodPrefix
     * @param parameterTypes  null means find any method with this name
     * @return a BeanMethod
     */
    public static BeanMethod findNestedBeanMethod(Object bean,
                                                  String propertyPath,
                                                  String methodPrefix,
                                                  Class[] parameterTypes)
    {
        Object obj;
        String propertyName;
        int dot = propertyPath.lastIndexOf('.');
        if (dot == -1)
        {
            obj = bean;
            propertyName = propertyPath;
        }
        else
        {
            //Nested beans
            obj = getNestedBean(bean, propertyPath);
            propertyName = propertyPath.substring(dot + 1);
        }

        if (obj == null)
        {
            LogUtil.getLogger().finest("Bean object is null.");
            return new BeanMethod(null, null);
        }

        String methodName;
        if (methodPrefix != null && methodPrefix.length() > 0)
        {
            methodName = methodPrefix + capitalize(propertyName);
        }
        else
        {
            methodName = propertyName;
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

    private static Object getNestedBean(Object bean, String property)
    {
        Object obj = bean;
        if (obj == null)
        {
            throw new IllegalArgumentException("Bean is null!");
        }
        StringTokenizer st = new StringTokenizer(property, ".");
        String nextProp;
        String methodName;
        while (true)
        {
            nextProp = st.nextToken();
            if (!st.hasMoreTokens())
            {
                return obj;
            }

            methodName = "get" + capitalize(nextProp);

            Object newObj;
            try
            {
                Method method = obj.getClass().getMethod(methodName, new Class[0]);
                newObj = method.invoke(obj, new Object[0]);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException(e.getTargetException());
            }
            catch (NoSuchMethodException e)
            {
                throw new IllegalArgumentException("Bean " + obj + " (Class " + obj.getClass() + ") does not have a property of name '" + nextProp + "'.");
            }

            obj = newObj;
            if (obj == null)
            {
                LogUtil.getLogger().info("Nested property '" + nextProp + "' of bean " + obj + " is null.");
                return null;
            }
        }
    }

    private static String capitalize(String s)
    {
        if (s.length() < 1) return s;
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) return s;
        return Character.toUpperCase(first) + s.substring(1);
    }


    public static void copyProperties(Object fromBean,
                                      Object toBean,
                                      String[] properties)
    {
        for (int i = 0; i < properties.length; i++)
        {
            String p = properties[i];
            Object obj = getBeanProperty(fromBean, p);
            setBeanProperty(toBean, p, obj);
        }
    }

}

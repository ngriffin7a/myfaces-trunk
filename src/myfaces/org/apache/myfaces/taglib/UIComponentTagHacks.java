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
package net.sourceforge.myfaces.taglib;

import javax.faces.component.UIComponent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Misc. hacks for accessing protected fields in UIComponentTag.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIComponentTagHacks
{
    private UIComponentTagHacks() {}


    private static final Class[] OVERRIDE_PROPERTIES_PARAMS = new Class[] {UIComponent.class};

    /**
     * Hack to access the protected FacesTag method "overrideProperties".
     * @param tag
     * @param comp
     */
    public static void overrideProperties(Object tag, UIComponent comp)
    {
        if (tag instanceof MyFacesTagBaseIF)
        {
            ((MyFacesTagBaseIF)tag).overrideProperties(comp);
        }
        else
        {
            try
            {
                Method m = null;
                Class c = tag.getClass();
                while (m == null && c != null && !c.equals(Object.class))
                {
                    try
                    {
                        m = c.getDeclaredMethod("overrideProperties",
                                                OVERRIDE_PROPERTIES_PARAMS);
                    }
                    catch (NoSuchMethodException e) {}
                    c = c.getSuperclass();
                }

                if (m == null)
                {
                    throw new NoSuchMethodException();
                }

                if (m.isAccessible())
                {
                    m.invoke(tag, new Object[] {comp});
                }
                else
                {
                    final Method finalM = m;
                    AccessController.doPrivileged(
                        new PrivilegedAction()
                        {
                            public Object run()
                            {
                                finalM.setAccessible(true);
                                return null;
                            }
                        });
                    m.invoke(tag, new Object[]{comp});
                    m.setAccessible(false);
                }
            }
            catch (NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            }
            catch (SecurityException e)
            {
                throw new RuntimeException(e);
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
    }


    private static final Class[] GET_COMPONENT_TYPE_PARAMS = new Class[] {};
    private static final Object[] GET_COMPONENT_TYPE_CALL_PARAMS = new Object[] {};

    /**
     * Hack to access the protected UIComponentTag method "getComponentType".
     */
    public static String getComponentType(Object tag)
    {
        if (tag instanceof MyFacesTagBaseIF)
        {
            return ((MyFacesTagBaseIF)tag).getComponentType();
        }
        else
        {
            try
            {
                Method m = null;
                Class c = tag.getClass();
                while (m == null && c != null && !c.equals(Object.class))
                {
                    try
                    {
                        m = c.getDeclaredMethod("getComponentType",
                                                GET_COMPONENT_TYPE_PARAMS);
                    }
                    catch (NoSuchMethodException e) {}
                    c = c.getSuperclass();
                }

                if (m == null)
                {
                    throw new NoSuchMethodException();
                }

                if (m.isAccessible())
                {
                    return (String)m.invoke(tag, GET_COMPONENT_TYPE_CALL_PARAMS);
                }
                else
                {
                    try
                    {
                        final Method finalM = m;
                        AccessController.doPrivileged(
                            new PrivilegedAction()
                            {
                                public Object run()
                                {
                                    finalM.setAccessible(true);
                                    return null;
                                }
                            });
                        return (String)m.invoke(tag, GET_COMPONENT_TYPE_CALL_PARAMS);
                    }
                    finally
                    {
                        m.setAccessible(false);
                    }
                }
            }
            catch (NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            }
            catch (SecurityException e)
            {
                throw new RuntimeException(e);
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
    }



    /**
     * Hack to access the protected "id" field.
     */
    public static String getId(Object tag)
    {
        if (tag instanceof MyFacesTagBaseIF)
        {
            return ((MyFacesTagBaseIF)tag).getId();
        }
        else
        {
            try
            {
                Field f = null;
                Class c = tag.getClass();
                while (f == null && c != null && !c.equals(Object.class))
                {
                    try
                    {
                        f = c.getDeclaredField("id");
                    }
                    catch (NoSuchFieldException e)
                    {
                    }
                    c = c.getSuperclass();
                }

                if (f == null)
                {
                    throw new RuntimeException(new NoSuchFieldException());
                }

                if (f.isAccessible())
                {
                    return (String)f.get(tag);
                }
                else
                {
                    try
                    {
                        final Field finalF = f;
                        AccessController.doPrivileged(
                            new PrivilegedAction()
                            {
                                public Object run()
                                {
                                    finalF.setAccessible(true);
                                    return null;
                                }
                            });
                        return (String)f.get(tag);
                    }
                    finally
                    {
                        f.setAccessible(false);
                    }
                }
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Hack to set the private "created" field.
     */
    public static void setCreated(Object tag, boolean newValue)
    {
        try
        {
            Field f = null;
            Class c = tag.getClass();
            while (f == null && c != null && !c.equals(Object.class))
            {
                try
                {
                    f = c.getDeclaredField("created");
                }
                catch (NoSuchFieldException e)
                {
                }
                c = c.getSuperclass();
            }

            if (f == null)
            {
                throw new RuntimeException(new NoSuchFieldException());
            }

            if (f.isAccessible())
            {
                f.set(tag, Boolean.valueOf(newValue));
            }
            else
            {
                try
                {
                    final Field finalF = f;
                    AccessController.doPrivileged(
                        new PrivilegedAction()
                        {
                            public Object run()
                            {
                                finalF.setAccessible(true);
                                return null;
                            }
                        });
                    f.set(tag, Boolean.valueOf(newValue));
                }
                finally
                {
                    f.setAccessible(false);
                }
            }
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }


}

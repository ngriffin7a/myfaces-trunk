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
package net.sourceforge.myfaces.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ClassUtils
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Log  log                 = LogFactory.getLog(ClassUtils.class);
    public static final Class BYTE_ARRAY_CLASS    = byte[].class;
    public static final Class CHAR_ARRAY_CLASS    = char[].class;
    public static final Class DOUBLE_ARRAY_CLASS  = double[].class;
    public static final Class FLOAT_ARRAY_CLASS   = float[].class;
    public static final Class INT_ARRAY_CLASS     = int[].class;
    public static final Class LONG_ARRAY_CLASS    = long[].class;
    public static final Class SHORT_ARRAY_CLASS   = short[].class;
    public static final Class BOOLEAN_ARRAY_CLASS = boolean[].class;
    public static final Class OBJECT_ARRAY_CLASS  = Object[].class;
    public static final Class STRING_ARRAY_CLASS  = String[].class;
    
// Array of void is an invalid type
//    public static final Class VOID_ARRAY_CLASS    = classForName("[V");
    public static final Map JAVATYPE_LOOKUP_MAP   =
        new BiLevelCacheMap(1024, 128, 80)
        {
            {
                // Pre-initialize cache with built-in types 
                _cacheL1.put("byte", Byte.TYPE);
                _cacheL1.put("char", Character.TYPE);
                _cacheL1.put("double", Double.TYPE);
                _cacheL1.put("float", Float.TYPE);
                _cacheL1.put("int", Integer.TYPE);
                _cacheL1.put("long", Long.TYPE);
                _cacheL1.put("short", Short.TYPE);
                _cacheL1.put("boolean", Boolean.TYPE);
                _cacheL1.put("void", Void.TYPE);
                _cacheL1.put("java.lang.Object", Object.class);
                _cacheL1.put("java.lang.String", String.class);
                _cacheL1.put("byte[]", BYTE_ARRAY_CLASS);
                _cacheL1.put("char[]", CHAR_ARRAY_CLASS);
                _cacheL1.put("double[]", DOUBLE_ARRAY_CLASS);
                _cacheL1.put("float[]", FLOAT_ARRAY_CLASS);
                _cacheL1.put("int[]", INT_ARRAY_CLASS);
                _cacheL1.put("long[]", LONG_ARRAY_CLASS);
                _cacheL1.put("short[]", SHORT_ARRAY_CLASS);
                _cacheL1.put("boolean[]", BOOLEAN_ARRAY_CLASS);
                _cacheL1.put("java.lang.Object[]", OBJECT_ARRAY_CLASS);
                _cacheL1.put("java.lang.String[]", STRING_ARRAY_CLASS);
            }

            protected Object newInstance(Object key)
            {
                String className = (String) key;

                // REVISIT: we could cache the Class itself (instead of the class name),
                //          but not sure whether that would interfere with multiple ContextClassloaders  
                return (className.endsWith("[]"))
                ? ("[L" + className.substring(0, className.length() - 2) + ";") : className;
            }
        };


    //~ Methods ------------------------------------------------------------------------------------

    public static Class classForName(String type)
            throws FacesException
    {
        try
        {
            return Class.forName(type, true, Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException ignore)
        {
            try
            {
                // fallback
                return Class.forName(type, true, ClassUtils.class.getClassLoader());
            }
            catch (ClassNotFoundException e)
            {
                log.error(e.getMessage(), e);
                throw new FacesException(e);
            }
        }
        catch (ExceptionInInitializerError e)
        {
            log.error("Error in static initializer of class " + type + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public static Class javaTypeToClass(String javaType)
    {
        Object clazz = JAVATYPE_LOOKUP_MAP.get(javaType);
        if (clazz instanceof String)
        {
            return classForName(clazz.toString());
        }

        return (Class) clazz;
    }

    public static Object newInstance(String type)
    throws FacesException
    {
        if (type == null)
        {
            return null;
        }

        return newInstance(classForName(type));
    }

    public static Object newInstance(Class clazz)
    throws FacesException
    {
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException e)
        {
            log.error(e.getMessage(), e);
            throw new FacesException(e);
        }
        catch (IllegalAccessException e)
        {
            log.error(e.getMessage(), e);
            throw new FacesException(e);
        }
    }


    public static Object convertToType(String value, Class desiredClass)
    {
        if (value == null)
        {
            return null;
        }
        if (desiredClass.equals(String.class))
        {
            return value;
        }

        try
        {
            if (desiredClass.equals(Byte.TYPE) || desiredClass.equals(Byte.class))
            {
                return new Byte(value.trim());
            }
            else if (desiredClass.equals(Short.TYPE) || desiredClass.equals(Short.class))
            {
                return new Short(value.trim());
            }
            else if (desiredClass.equals(Integer.TYPE) || desiredClass.equals(Integer.class))
            {
                return new Integer(value.trim());
            }
            else if (desiredClass.equals(Long.TYPE) || desiredClass.equals(Long.class))
            {
                return new Long(value.trim());
            }
            else if (desiredClass.equals(Double.TYPE) || desiredClass.equals(Double.class))
            {
                return new Double(value.trim());
            }
            else if (desiredClass.equals(Float.TYPE) || desiredClass.equals(Float.class))
            {
                return new Float(value.trim());
            }
            else if (desiredClass.equals(Boolean.TYPE) || desiredClass.equals(Boolean.class))
            {
                return Boolean.valueOf(value.trim());
            }
        }
        catch (NumberFormatException e)
        {
            log.error("NumberFormatException value '" + value + "' type " + desiredClass.getName());
            throw e;
        }
        throw new UnsupportedOperationException("Conversion to " + desiredClass.getName() + " not yet supported");
    }

//    public static void main(String[] args)
//    {
//        // test code
//        System.out.println(javaTypeToClass("int").getName());
//        System.out.println(javaTypeToClass("int[]").getName());
//        System.out.println(javaTypeToClass("java.lang.String").getName());
//        System.out.println(javaTypeToClass("java.lang.String[]").getName());
//        System.out.println(javaTypeToClass("int").getName());
//        System.out.println(javaTypeToClass("int[]").getName());
//        System.out.println(javaTypeToClass("java.lang.String").getName());
//        System.out.println(javaTypeToClass("java.lang.String[]").getName());
//    }
}

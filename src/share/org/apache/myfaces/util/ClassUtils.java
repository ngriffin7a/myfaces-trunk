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
package org.apache.myfaces.util;

import org.apache.commons.el.Coercions;
import org.apache.commons.el.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.servlet.jsp.el.ELException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.11  2005/01/19 13:18:04  mmarinschek
 * better logging of component information
 *
 * Revision 1.10  2004/10/13 11:51:01  matze
 * renamed packages to org.apache
 *
 * Revision 1.9  2004/10/05 22:34:21  dave0000
 * bug 1021656 with related improvements
 *
 * Revision 1.8  2004/08/10 10:57:38  manolito
 * fixed StackOverflow in ClassUtils and cleaned up ClassUtils methods
 *
 * Revision 1.7  2004/08/05 22:55:51  o_rossmueller
 * fix: resolve primitive classes
 *
 * Revision 1.6  2004/07/13 04:59:25  tinytoony
 * primitive types where not retrieved (call to javaTypeToClass not used)
 *
 * Revision 1.5  2004/07/13 04:56:55  tinytoony
 * primitive types where not retrieved (call to javaTypeToClass not used)
 *
 * Revision 1.4  2004/07/01 22:01:13  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/05/17 14:28:29  manolito
 * new configuration concept
 *
 * Revision 1.2  2004/05/11 04:24:10  dave0000
 * Bug 943166: add value coercion to ManagedBeanConfigurator
 *
 * Revision 1.1  2004/03/31 11:58:45  manolito
 * custom component refactoring
 *
 * Revision 1.12  2004/03/30 13:27:50  manolito
 * new getResourceAsStream method
 *
 */
public class ClassUtils
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Log log                  = LogFactory.getLog(ClassUtils.class);
    private static final Logger COERCION_LOGGER   = new Logger(System.out);
    
    public static final Class BOOLEAN_ARRAY_CLASS = boolean[].class;
    public static final Class BYTE_ARRAY_CLASS    = byte[].class;
    public static final Class CHAR_ARRAY_CLASS    = char[].class;
    public static final Class SHORT_ARRAY_CLASS   = short[].class;
    public static final Class INT_ARRAY_CLASS     = int[].class;
    public static final Class LONG_ARRAY_CLASS    = long[].class;
    public static final Class FLOAT_ARRAY_CLASS   = float[].class;
    public static final Class DOUBLE_ARRAY_CLASS  = double[].class;
    public static final Class OBJECT_ARRAY_CLASS  = Object[].class;
    public static final Class BOOLEAN_OBJECT_ARRAY_CLASS = Boolean[].class;
    public static final Class BYTE_OBJECT_ARRAY_CLASS = Byte[].class;
    public static final Class CHARACTER_OBJECT_ARRAY_CLASS = Character[].class;
    public static final Class SHORT_OBJECT_ARRAY_CLASS = Short[].class;
    public static final Class INTEGER_OBJECT_ARRAY_CLASS = Integer[].class;
    public static final Class LONG_OBJECT_ARRAY_CLASS = Long[].class;
    public static final Class FLOAT_OBJECT_ARRAY_CLASS = Float[].class;
    public static final Class DOUBLE_OBJECT_ARRAY_CLASS = Double[].class;
    public static final Class STRING_OBJECT_ARRAY_CLASS = String[].class;

    public static final Map COMMON_TYPES = new HashMap(64);
    static
    {
        COMMON_TYPES.put("byte", Byte.TYPE);
        COMMON_TYPES.put("char", Character.TYPE);
        COMMON_TYPES.put("double", Double.TYPE);
        COMMON_TYPES.put("float", Float.TYPE);
        COMMON_TYPES.put("int", Integer.TYPE);
        COMMON_TYPES.put("long", Long.TYPE);
        COMMON_TYPES.put("short", Short.TYPE);
        COMMON_TYPES.put("boolean", Boolean.TYPE);
        COMMON_TYPES.put("void", Void.TYPE);
        COMMON_TYPES.put("java.lang.Object", Object.class);
        COMMON_TYPES.put("java.lang.Boolean", Boolean.class);
        COMMON_TYPES.put("java.lang.Byte", Byte.class);
        COMMON_TYPES.put("java.lang.Character", Character.class);
        COMMON_TYPES.put("java.lang.Short", Short.class);
        COMMON_TYPES.put("java.lang.Integer", Integer.class);
        COMMON_TYPES.put("java.lang.Long", Long.class);
        COMMON_TYPES.put("java.lang.Float", Float.class);
        COMMON_TYPES.put("java.lang.Double", Double.class);
        COMMON_TYPES.put("java.lang.String", String.class);
        
        COMMON_TYPES.put("byte[]", BYTE_ARRAY_CLASS);
        COMMON_TYPES.put("char[]", CHAR_ARRAY_CLASS);
        COMMON_TYPES.put("double[]", DOUBLE_ARRAY_CLASS);
        COMMON_TYPES.put("float[]", FLOAT_ARRAY_CLASS);
        COMMON_TYPES.put("int[]", INT_ARRAY_CLASS);
        COMMON_TYPES.put("long[]", LONG_ARRAY_CLASS);
        COMMON_TYPES.put("short[]", SHORT_ARRAY_CLASS);
        COMMON_TYPES.put("boolean[]", BOOLEAN_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.Object[]", OBJECT_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.Boolean[]", BOOLEAN_OBJECT_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.Byte[]", BYTE_OBJECT_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.Character[]", CHARACTER_OBJECT_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.Short[]", SHORT_OBJECT_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.Integer[]", INTEGER_OBJECT_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.Long[]", LONG_OBJECT_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.Float[]", FLOAT_OBJECT_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.Double[]", DOUBLE_OBJECT_ARRAY_CLASS);
        COMMON_TYPES.put("java.lang.String[]", STRING_OBJECT_ARRAY_CLASS);
        // array of void is not a valid type
    }
    
    /** utility class, do not instantiate */
    private ClassUtils()
    {
        // utility class, disable instantiation
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Tries a Class.forName with the context class loader of the current thread first and
     * automatically falls back to the ClassUtils class loader (i.e. the loader of the
     * myfaces.jar lib) if necessary.
     * 
     * @param type fully qualified name of a non-primitive non-array class
     * @return the corresponding Class
     * @throws NullPointerException if type is null
     * @throws ClassNotFoundException
     */
    public static Class classForName(String type)
        throws ClassNotFoundException
    {
        if (type == null) throw new NullPointerException("type");
        try
        {
            // Try WebApp ClassLoader first
            return Class.forName(type,
                                 false, // do not initialize for faster startup
                                 Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException ignore)
        {
            // fallback: Try ClassLoader for ClassUtils (i.e. the myfaces.jar lib)
            return Class.forName(type,
                                 false, // do not initialize for faster startup
                                 ClassUtils.class.getClassLoader());
        }
    }


    /**
     * Same as {@link #classForName(String)}, but throws a RuntimeException
     * (FacesException) instead of a ClassNotFoundException.
     * 
     * @return the corresponding Class
     * @throws NullPointerException if type is null
     * @throws FacesException if class not found
     */
    public static Class simpleClassForName(String type)
    {
        try
        {
            return classForName(type);
        }
        catch (ClassNotFoundException e)
        {
            log.error("Class " + type + " not found", e);
            throw new FacesException(e);
        }
    }


    /**
     * Similar as {@link #classForName(String)}, but also supports primitive types
     * and arrays as specified for the JavaType element in the JavaServer Faces Config DTD.
     * 
     * @param type fully qualified class name or name of a primitive type, both optionally
     *             followed by "[]" to indicate an array type
     * @return the corresponding Class
     * @throws NullPointerException if type is null
     * @throws ClassNotFoundException
     */
    public static Class javaTypeToClass(String type)
        throws ClassNotFoundException
    {
        if (type == null) throw new NullPointerException("type");

        // try common types and arrays of common types first
        Class clazz = (Class) COMMON_TYPES.get(type);
        if (clazz != null)
        {
            return clazz;
        }

        int len = type.length();
        if (len > 2 && type.charAt(len - 1) == ']' && type.charAt(len - 2) == '[')
        {
            String componentType = type.substring(0, len - 2);
            Class componentTypeClass = classForName(componentType);
            return Array.newInstance(componentTypeClass, 0).getClass();
        }
        else
        {
            return classForName(type);
        }
    }


    /**
     * Same as {@link #javaTypeToClass(String)}, but throws a RuntimeException
     * (FacesException) instead of a ClassNotFoundException.
     * 
     * @return the corresponding Class
     * @throws NullPointerException if type is null
     * @throws FacesException if class not found
     */
    public static Class simpleJavaTypeToClass(String type)
    {
        try
        {
            return javaTypeToClass(type);
        }
        catch (ClassNotFoundException e)
        {
            log.error("Class " + type + " not found", e);
            throw new FacesException(e);
        }
    }

    public static InputStream getResourceAsStream(String resource)
    {
        InputStream stream = Thread.currentThread().getContextClassLoader()
                                .getResourceAsStream(resource);
        if (stream == null)
        {
            // fallback
            stream = ClassUtils.class.getClassLoader().getResourceAsStream(resource);
        }
        return stream;
    }


    public static Object newInstance(String type)
        throws FacesException
    {
        if (type == null) return null;
        return newInstance(simpleClassForName(type));
    }


    public static Object newInstance(Class clazz)
        throws FacesException
    {
        try
        {
            return clazz.newInstance();
        }
        catch(NoClassDefFoundError e)
        {
            log.error("Class : "+clazz.getName()+" not found.",e);
            throw new FacesException(e);
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

    public static Object convertToType(Object value, Class desiredClass)
    {
        if (value == null) return null;

        try
        {
            // Use coersion implemented by JSP EL for consistency with EL
            // expressions. Additionally, it caches some of the coersions.
            return Coercions.coerce(value, desiredClass, COERCION_LOGGER);
        }
        catch (ELException e)
        {
            String message = "Cannot coerce " + value.getClass().getName()
                + " to " + desiredClass.getName(); 
            log.error(message, e);
            throw new FacesException(message, e);
        }
    }

    /**
     * Gets the ClassLoader associated with the current thread.  Returns the class loader associated with 
     * the specified default object if no context loader is associated with the current thread.
     * 
     * @param defaultObject The default object to use to determine the class loader (if none associated with current thread.)
     * @return ClassLoader
     */    
    protected static ClassLoader getCurrentLoader(Object defaultObject)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if(loader == null)
        {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }
}

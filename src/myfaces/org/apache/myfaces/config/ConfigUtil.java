/**
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConfigUtil
{
    private static final Log log = LogFactory.getLog(ConfigUtil.class);

    public static Object newInstance(String type) throws FacesException
    {
        if (type == null)
        {
            return null;
        }

        return newInstance(classForName(type));
    }

    public static Object newInstance(Class clazz) throws FacesException
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

    public static Class classForName(String type) throws FacesException
    {
        try
        {
            return Class.forName(type, true, Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            log.error(e.getMessage(), e);
            throw new FacesException(e);
        }
    }



    public static Class javaTypeToClass(String javaType)
    {
        boolean isArray = false;
        if (javaType.endsWith("[]"))
        {
            isArray = true;
            javaType = javaType.substring(0, javaType.length() - 2);
        }

        if (javaType.equals("byte"))
        {
            return isArray ? classForName("[B") : Byte.TYPE;
        }
        else if (javaType.equals("char"))
        {
            return isArray ? classForName("[C") : Character.TYPE;
        }
        else if (javaType.equals("double"))
        {
            return isArray ? classForName("[D") : Double.TYPE;
        }
        else if (javaType.equals("float"))
        {
            return isArray ? classForName("[F") : Float.TYPE;
        }
        else if (javaType.equals("int"))
        {
            return isArray ? classForName("[I") : Integer.TYPE;
        }
        else if (javaType.equals("long"))
        {
            return isArray ? classForName("[J") : Long.TYPE;
        }
        else if (javaType.equals("short"))
        {
            return isArray ? classForName("[S") : Short.TYPE;
        }
        else if (javaType.equals("boolean"))
        {
            return isArray ? classForName("[Z") : Boolean.TYPE;
        }
        else if (javaType.equals("void"))
        {
            return isArray ? classForName("[V") : Void.TYPE;
        }

        return isArray
                ? classForName("[L" + javaType + ";")
                : classForName(javaType);
    }


    public static void main (String[]  args)
	{
        System.out.println(javaTypeToClass("int").getName());
        System.out.println(javaTypeToClass("int[]").getName());
        System.out.println(javaTypeToClass("java.lang.String").getName());
        System.out.println(javaTypeToClass("java.lang.String[]").getName());
    }

}

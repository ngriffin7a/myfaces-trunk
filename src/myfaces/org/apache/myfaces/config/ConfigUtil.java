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
package net.sourceforge.myfaces.config;

import javax.faces.FacesException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConfigUtil
{
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
            throw new FacesException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new FacesException(e);
        }
    }

    public static Class classForName(String type) throws FacesException
    {
        try
        {
            return Class.forName(type);
        }
        catch (ClassNotFoundException e)
        {
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

        try
        {
            if (javaType.equals("byte"))
            {
                return isArray ? Class.forName("[B") : Byte.TYPE;
            }
            else if (javaType.equals("char"))
            {
                return isArray ? Class.forName("[C") : Character.TYPE;
            }
            else if (javaType.equals("double"))
            {
                return isArray ? Class.forName("[D") : Double.TYPE;
            }
            else if (javaType.equals("float"))
            {
                return isArray ? Class.forName("[F") : Float.TYPE;
            }
            else if (javaType.equals("int"))
            {
                return isArray ? Class.forName("[I") : Integer.TYPE;
            }
            else if (javaType.equals("long"))
            {
                return isArray ? Class.forName("[J") : Long.TYPE;
            }
            else if (javaType.equals("short"))
            {
                return isArray ? Class.forName("[S") : Short.TYPE;
            }
            else if (javaType.equals("boolean"))
            {
                return isArray ? Class.forName("[Z") : Boolean.TYPE;
            }
            else if (javaType.equals("void"))
            {
                return isArray ? Class.forName("[V") : Void.TYPE;
            }

            return isArray
                    ? Class.forName("[L" + javaType + ";")
                    : Class.forName(javaType);
        }
        catch (ClassNotFoundException e)
        {
            throw new FacesException(e);
        }

    }


    public static void main (String[]  args)
	{
        System.out.println(javaTypeToClass("int").getName());
        System.out.println(javaTypeToClass("int[]").getName());
        System.out.println(javaTypeToClass("java.lang.String").getName());
        System.out.println(javaTypeToClass("java.lang.String[]").getName());
    }

}

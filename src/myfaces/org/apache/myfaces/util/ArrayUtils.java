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

import java.lang.reflect.Array;


/**
 * Utility class for managing arrays
 * 
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ArrayUtils
{
    //~ Constructors -------------------------------------------------------------------------------

    protected ArrayUtils()
    {
        // hide from public access
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Concatenates two Object arrays into one. If arr1 is null, returns arr2.
     * If arr2 is null, returns arr1. May return null if both arrays are null,
     * or one is empty and the other null. <br>
     * If both arrays havethe same component type, will return an array with the same component type, otherwise Object[] is returned
     *
     * @param arr1 input array
     * @param arr2 input array
     *
     * @return Object the concatenated array, elements of arr1 first
     */
    public static Object concat(Object arr1, Object arr2)
    {
        int len1 = (arr1 == null) ? (-1) : Array.getLength(arr1);

        if (len1 < 0)
        {
            return arr2;
        }

        int len2 = (arr2 == null) ? (-1) : Array.getLength(arr2);

        if (len2 < 0)
        {
            return arr1;
        }

        Class componentType = arr1.getClass().getComponentType();

        if (!componentType.equals(arr2.getClass().getComponentType()))
        {
            // REVISIT: create autoconverter to Object type
            if (componentType.isPrimitive() || arr2.getClass().getComponentType().isPrimitive())
            {
                throw new IllegalArgumentException(
                    "cannot concatenate arrays of different primitive types: " + componentType
                    + ", " + arr2.getClass().getComponentType());
            }

            // If different but not primitive, then return Object[]
            componentType = Object.class;
        }

        Object newArray = Array.newInstance(componentType, len1 + len2);

        System.arraycopy(arr1, 0, newArray, 0, len1);
        System.arraycopy(arr2, 0, newArray, len1, len2);

        return newArray;
    }

    public static Object concat(Object arr1, Object arr2, Object arr3)
    {
        // TODO: Inefficient
        return concat(
            concat(arr1, arr2),
            arr3);
    }

    public static Object concat(Object arr1, Object arr2, Object arr3, Object arr4)
    {
        // TODO: Inefficient
        return concat(
            concat(arr1, arr2),
            concat(arr3, arr4));
    }
}

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

import java.util.HashMap;
import java.util.Map;


/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HashMapUtils
{
    //~ Constructors -------------------------------------------------------------------------------

    protected HashMapUtils()
    {
        // block public access
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Calculates initial capacity needed to hold <code>size</code> elements in
     * a HashMap or Hashtable without forcing an expensive increase in internal
     * capacity. Capacity is based on the default load factor of .75.
     * <p>
     * Usage: <code>Map map = new HashMap(HashMapUtils.calcCapacity(10));<code>
     * </p>
     * @param size the number of items that will be put into a HashMap
     * @return initial capacity needed
     */
    public static final int calcCapacity(int size)
    {
        return ((size * 4) + 3) / 3;
    }

    /**
     * Creates a new <code>HashMap</code> that has all of the elements
     * of <code>map1</code> and <code>map2</code> (on key collision, the latter
     * override the former).
     *
     * @param map1 the fist hashmap to merge
     * @param map2 the second hashmap to merge
     * @return new hashmap
     */
    public static HashMap merge(Map map1, Map map2)
    {
        HashMap retval = new HashMap(calcCapacity(map1.size() + map2.size()));

        retval.putAll(map1);
        retval.putAll(map2);

        return retval;
    }
}

/*
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
     * Creates a new <code>HashMap</code> that has all of the elements
     * of <code>arg1</code> and <code>arg2</code> (on key collision, the latter
     * override the former).
     *
     * @param arg1 the fist hashmap to merge
     * @param arg2 the second hashmap to merge
     * @return new hashmap
     */
    public static HashMap merge(Map arg1, Map arg2)
    {
        HashMap retval = new HashMap(((arg1.size() + arg2.size() + 1) * 4) / 3); // adjust for load factor

        retval.putAll(arg1);
        retval.putAll(arg2);

        return retval;
    }
}

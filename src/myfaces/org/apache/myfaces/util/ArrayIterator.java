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

import java.lang.reflect.Array;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Utility class expose an array as Iterator
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ArrayIterator
implements Iterator
{
    //~ Instance fields ----------------------------------------------------------------------------

    private Object _array;
    private int    _index;
    private int    _length;

    //~ Constructors -------------------------------------------------------------------------------

    public ArrayIterator(Object arr)
    {
        if (!arr.getClass().isArray())
        {
            throw new IllegalArgumentException("Not an array: " + arr.getClass());
        }

        _array      = arr;
        _length     = Array.getLength(arr);
    }

    //~ Methods ------------------------------------------------------------------------------------

    public boolean hasNext()
    {
        return _index < _length;
    }

    public Object next()
    {
        if (!hasNext())
        {
            throw new NoSuchElementException();
        }
        else
        {
            return Array.get(_array, _index++);
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException("cannot remove() from array");
    }
}

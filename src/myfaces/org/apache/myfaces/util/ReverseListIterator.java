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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/05/17 14:28:28  manolito
 * new configuration concept
 *
 */
public class ReverseListIterator
        implements Iterator
{
    //private static final Log log = LogFactory.getLog(ReverseListIterator.class);

    private int _cursor;
    private List _list;

    public ReverseListIterator(List list)
    {
        _list = list;
        _cursor = list.size() - 1;
    }

    public boolean hasNext()
    {
        return _cursor >= 0;
    }

    public Object next()
    {
        if (_cursor < 0)
        {
            throw new NoSuchElementException();
        }
        return _list.get(_cursor--);
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}

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
package net.sourceforge.myfaces.util;

import java.util.Enumeration;
import java.util.Iterator;


/**
 * $Log$
 * Revision 1.2  2004/03/29 09:00:56  manolito
 * copyright header
 *
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class IteratorEnumeration implements Enumeration
{
    Iterator _it;

    public IteratorEnumeration(Iterator it)
    {
        _it = it;
    }

    public boolean hasMoreElements()
    {
        return _it.hasNext();
    }

    public Object nextElement()
    {
        return _it.next();
    }
}

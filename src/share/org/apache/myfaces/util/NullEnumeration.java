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

/**
 * Enumeration without elements
 *
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/04/15 04:32:58  dave0000
 * make SessionMap request session every time
 *
 *
 */
public final class NullEnumeration implements Enumeration
{
    private static final NullEnumeration s_nullEnumeration = new NullEnumeration();

    public static final NullEnumeration instance()
    {
        return s_nullEnumeration;
    }

    public boolean hasMoreElements()
    {
        return false;
    }

    public Object nextElement()
    {
        throw new UnsupportedOperationException("NullEnumeration has no elements");
    }
}

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
package net.sourceforge.myfaces.context.servlet;

import java.util.*;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class AbstractAttributeMap
    implements Map
{
    protected abstract Object getAttribute(String name);
    protected abstract void setAttribute(String name, Object newVal);
    protected abstract void removeAttribute(String name);
    protected abstract Enumeration getAttributeNames();

    AbstractAttributeMap()
    {
        // hide from public view
    }

    public int size()
    {
        int size = 0;
        Enumeration enum = getAttributeNames();
        while (enum.hasMoreElements())
        {
            size++;
            enum.nextElement();
        }
        return size;
    }

    public boolean isEmpty()
    {
        return !getAttributeNames().hasMoreElements();
    }

    public boolean containsKey(Object key)
    {
        return getAttribute(key.toString()) != null;
    }

    public boolean containsValue(Object value)
    {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key)
    {
        return getAttribute(key.toString());
    }

    public Object put(Object key, Object value)
    {
        Object oldObj = get(key);
        setAttribute(key.toString(), value);
        return oldObj;
    }

    public Object remove(Object key)
    {
        Object oldObj = get(key);
        removeAttribute(key.toString());
        return oldObj;
    }

    public void putAll(Map t)
    {
        for (Iterator it = t.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    public Set keySet()
    {
        throw new UnsupportedOperationException();
    }

    public Collection values()
    {
        throw new UnsupportedOperationException();
    }

    public Set entrySet()
    {
        throw new UnsupportedOperationException();
    }

}

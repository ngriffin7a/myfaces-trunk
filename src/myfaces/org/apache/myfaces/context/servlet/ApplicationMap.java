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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

/**
 * Wrapper object that exposes the ServletContext attributes as a collections
 * API Map interface.
 * 
 * @author Dimitry D'hondt
 * @author Anton Koinov
 */
public class ApplicationMap
    implements Map
{

    private ServletContext _servletContext;

    ApplicationMap(ServletContext ctx)
    {
        this._servletContext = ctx;
    }

    /**
     * @see java.util.Map#clear()
     */
    public void clear()
    {
        List names = new ArrayList();
        for (Enumeration e = _servletContext.getAttributeNames(); e
            .hasMoreElements();)
        {
            names.add(e.nextElement());
        }

        for (Iterator it = names.iterator(); it.hasNext();)
        {
            _servletContext.removeAttribute((String) it.next());
        }
    }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key)
    {
        return _servletContext.getAttribute(key.toString()) != null;
    }

    /**
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object findValue)
    {
        if (findValue == null)
        {
            return false;
        }

        for (Enumeration e = _servletContext.getAttributeNames(); e
            .hasMoreElements();)
        {
            Object value = _servletContext.getAttribute((String) e
                .nextElement());
            if (findValue.equals(value))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set entrySet()
    {
        Map ret = new HashMap();

        for (Enumeration e = _servletContext.getAttributeNames(); e
            .hasMoreElements();)
        {
            String key = (String) e.nextElement();
            ret.put(key, _servletContext.getAttribute(key));
        }

        return ret.entrySet();
    }

    /**
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(Object key)
    {
        return _servletContext.getAttribute(key.toString());
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty()
    {
        return !_servletContext.getAttributeNames().hasMoreElements();
    }

    /**
     * @see java.util.Map#keySet()
     */
    public Set keySet()
    {
        Set ret = new HashSet();

        for (Enumeration e = _servletContext.getAttributeNames(); e
            .hasMoreElements();)
        {
            ret.add(e.nextElement());
        }

        return ret;
    }

    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(Object key, Object value)
    {
        String key_ = key.toString();
        Object retval = _servletContext.getAttribute(key_);
        _servletContext.setAttribute(key_, value);
        return retval;
    }

    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map t)
    {
        for (Iterator it = t.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            String key = entry.getKey().toString();
            _servletContext.setAttribute(key, entry.getValue());
        }
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(Object key)
    {
        return put(key, null);
    }

    /**
     * @see java.util.Map#size()
     */
    public int size()
    {
        int ret = 0;

        for (Enumeration e = _servletContext.getAttributeNames(); e
            .hasMoreElements();)
        {
            ret++;
            e.nextElement();
        }

        return ret;
    }

    /**
     * @see java.util.Map#values()
     */
    public Collection values()
    {
        List ret = new ArrayList();

        for (Enumeration e = _servletContext.getAttributeNames(); e
            .hasMoreElements();)
        {
            ret.add(_servletContext.getAttribute((String) e.nextElement()));
        }

        return ret;
    }
}
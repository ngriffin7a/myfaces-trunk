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

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest Cookies as Map.
 * 
 * @author Dimitry D'hondt
 * @author Anton Koinov
 */
public class CookieMap extends AbstractAttributeMap
{

    final HttpServletRequest _httpServletRequest;

    CookieMap(HttpServletRequest httpServletRequest)
    {
        _httpServletRequest = httpServletRequest;
    }

    public void clear()
    {
        throw new UnsupportedOperationException(
            "Cannot clear HttpRequest Cookies");
    }

    public boolean containsKey(Object key)
    {
        Cookie[] cookies = _httpServletRequest.getCookies();
        for (int i = 0, len = cookies.length; i < len; i++)
        {
            if (cookies[i].getName().equals(key))
            {
                return true;
            }
        }

        return false;
    }

    public boolean containsValue(Object findValue)
    {
        if (findValue == null)
        {
            return false;
        }

        Cookie[] cookies = _httpServletRequest.getCookies();
        for (int i = 0, len = cookies.length; i < len; i++)
        {
            if (findValue.equals(cookies[i].getValue()))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isEmpty()
    {
        return _httpServletRequest.getCookies().length == 0;
    }

    public int size()
    {
        return _httpServletRequest.getCookies().length;
    }

    protected Object getAttribute(String key)
    {
        Cookie[] cookies = _httpServletRequest.getCookies();
        for (int i = 0, len = cookies.length; i < len; i++)
        {
            if (cookies[i].getName().equals(key))
            {
                return cookies[i].getValue();
            }
        }

        return null;
    }

    protected void setAttribute(String key, Object value)
    {
        throw new UnsupportedOperationException(
            "Cannot set HttpRequest Cookies");
    }

    protected void removeAttribute(String key)
    {
        throw new UnsupportedOperationException(
            "Cannot remove HttpRequest Cookies");
    }

    protected Enumeration getAttributeNames()
    {
        return new CookieNameEnumeration();
    }
    
    class CookieNameEnumeration implements Enumeration
    {
        private final Cookie[] _cookies = _httpServletRequest.getCookies();
        int _length = _cookies.length;
        int _index;

        public boolean hasMoreElements()
        {
            return _index < _length;
        }

        public Object nextElement()
        {
            return _cookies[_index++].getName();
        }
    }
}

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

import net.sourceforge.myfaces.util.IteratorEnumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ServletRequestMockImpl
    implements HttpServletRequest
{

    private static final Locale[] LOCALES = {Locale.US, Locale.GERMAN};

    private Map _attributes = new HashMap();
    private Cookie[] _cookies;
    private String _servletPath = "/myfaces";
    private String _pathInfo = "";

    public ServletRequestMockImpl(Cookie[] cookies)
    {
        _cookies = cookies;
    }
    
    public String getAuthType()
    {
        throw new UnsupportedOperationException();
    }

    public Cookie[] getCookies()
    {
        return _cookies;
    }

    public long getDateHeader(String name)
    {
        throw new UnsupportedOperationException();
    }

    public String getHeader(String name)
    {
        throw new UnsupportedOperationException();
    }

    public Enumeration getHeaders(String name)
    {
        throw new UnsupportedOperationException();
    }

    public Enumeration getHeaderNames()
    {
        throw new UnsupportedOperationException();
    }

    public int getIntHeader(String name)
    {
        throw new UnsupportedOperationException();
    }

    public String getMethod()
    {
        throw new UnsupportedOperationException();
    }

    public String getPathInfo()
    {
        return _pathInfo;
    }

    public String getPathTranslated()
    {
        throw new UnsupportedOperationException();
    }

    public String getContextPath()
    {
        throw new UnsupportedOperationException();
    }

    public String getQueryString()
    {
        throw new UnsupportedOperationException();
    }

    public String getRemoteUser()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isUserInRole(String name)
    {
        throw new UnsupportedOperationException();
    }

    public Principal getUserPrincipal()
    {
        throw new UnsupportedOperationException();
    }

    public String getRequestedSessionId()
    {
        throw new UnsupportedOperationException();
    }

    public String getRequestURI()
    {
        throw new UnsupportedOperationException();
    }

    public StringBuffer getRequestURL()
    {
        throw new UnsupportedOperationException();
    }

    public String getServletPath()
    {
        return _servletPath;
    }

    public HttpSession getSession(boolean b)
    {
        return null;
    }

    public HttpSession getSession()
    {
        return null;
    }

    public boolean isRequestedSessionIdValid()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isRequestedSessionIdFromURL()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isRequestedSessionIdFromUrl()
    {
        throw new UnsupportedOperationException();
    }

    public Object getAttribute(String name)
    {
        return _attributes.get(name);
    }

    public Enumeration getAttributeNames()
    {
        return new IteratorEnumeration(_attributes.keySet().iterator());
    }

    public String getCharacterEncoding()
    {
        throw new UnsupportedOperationException();
    }

    public void setCharacterEncoding(String name) throws UnsupportedEncodingException
    {
        throw new UnsupportedOperationException();
    }

    public int getContentLength()
    {
        throw new UnsupportedOperationException();
    }

    public String getContentType()
    {
        throw new UnsupportedOperationException();
    }

    public ServletInputStream getInputStream() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public String getParameter(String name)
    {
        throw new UnsupportedOperationException();
    }

    public Enumeration getParameterNames()
    {
        throw new UnsupportedOperationException();
    }

    public String[] getParameterValues(String name)
    {
        throw new UnsupportedOperationException();
    }

    public Map getParameterMap()
    {
        throw new UnsupportedOperationException();
    }

    public String getProtocol()
    {
        throw new UnsupportedOperationException();
    }

    public String getScheme()
    {
        throw new UnsupportedOperationException();
    }

    public String getServerName()
    {
        throw new UnsupportedOperationException();
    }

    public int getServerPort()
    {
        throw new UnsupportedOperationException();
    }

    public BufferedReader getReader() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public String getRemoteAddr()
    {
        throw new UnsupportedOperationException();
    }

    public String getRemoteHost()
    {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(String name, Object o)
    {
        _attributes.put(name, o);
    }

    public void removeAttribute(String name)
    {
        _attributes.remove(name);
    }

    public Locale getLocale()
    {
        return LOCALES[0];
    }

    public Enumeration getLocales()
    {
        return new IteratorEnumeration(Arrays.asList(LOCALES).iterator());
    }

    public boolean isSecure()
    {
        throw new UnsupportedOperationException();
    }

    public RequestDispatcher getRequestDispatcher(String name)
    {
        throw new UnsupportedOperationException();
    }

    public String getRealPath(String name)
    {
        throw new UnsupportedOperationException();
    }

    public int getRemotePort()
    {
        throw new UnsupportedOperationException();
    }

    public String getLocalName()
    {
        throw new UnsupportedOperationException();
    }

    public String getLocalAddr()
    {
        throw new UnsupportedOperationException();
    }

    public int getLocalPort()
    {
        throw new UnsupportedOperationException();
    }




    // mock setters

    public void setServletPath(String servletPath)
    {
        _servletPath = servletPath;
    }

    public void setPathInfo(String pathInfo)
    {
        _pathInfo = pathInfo;
    }

}

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
package net.sourceforge.myfaces.context;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.*;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ExternalContextMockImpl extends ExternalContext
{
    private Map _applicationMap = new HashMap();
    private Map _requestMap = new HashMap();
    private Map _sessionMap = new HashMap();
    private Map _requestParameterMap = new HashMap();
    private Map _requestParameterValuesMap = new HashMap();
    private Map _requestHeaderMap = new HashMap();
    private Map _requestHeaderValuesMap = new HashMap();
    private Map _requestCookieMap = new HashMap();
    private Map _initParameterMap = new HashMap();
    private Principal _principal;
    private Map _resourceMap = new HashMap();
    private String _requestServletPath;
    private String _requestPathInfo;

    //init methods
    public void addResourceMapping(String systemId, String systemIdMapping)
    {
        _resourceMap.put(systemId, systemIdMapping);
    }

    public void setPrincipal(Principal principal)
    {
        _principal = principal;
    }

    public void setRequestPathInfo(String requestPathInfo)
    {
        _requestPathInfo = requestPathInfo;
    }

    public void setRequestServletPath(String requestServletPath)
    {
        _requestServletPath = requestServletPath;
    }

    // constructor
    public ExternalContextMockImpl()
    {
    }

    // interface-methods
    public Map getApplicationMap()
    {
        return _applicationMap;
    }

    public Object getContext()
    {
        throw new UnsupportedOperationException();
    }

    public Object getRequest()
    {
        throw new UnsupportedOperationException();
    }

    public Map getRequestMap()
    {
        return _requestMap;
    }

    public Object getResponse()
    {
        throw new UnsupportedOperationException();
    }

    public Object getSession(boolean value)
    {
        throw new UnsupportedOperationException();
    }

    public Map getSessionMap()
    {
        return _sessionMap;
    }

    public Map getRequestParameterMap()
    {
        return _requestParameterMap;
    }

    public Map getRequestParameterValuesMap()
    {
        return _requestParameterValuesMap;
    }

    public Iterator getRequestParameterNames()
    {
        throw new UnsupportedOperationException();
    }

    public Map getRequestHeaderMap()
    {
        return _requestHeaderMap;
    }

    public Map getRequestHeaderValuesMap()
    {
        return _requestHeaderValuesMap;
    }

    public Map getRequestCookieMap()
    {
        return _requestCookieMap;
    }

    public Locale getRequestLocale()
    {
        throw new UnsupportedOperationException();
    }

    public String getRequestPathInfo()
    {
        return _requestPathInfo;
    }

    public String getRequestContextPath()
    {
        throw new UnsupportedOperationException();
    }

    public String getRequestServletPath()
    {
        return _requestServletPath;
    }

    public Cookie[] getRequestCookies()
    {
        throw new UnsupportedOperationException();
    }

    public String getInitParameter(String value)
    {
        throw new UnsupportedOperationException();
    }

    public Map getInitParameterMap()
    {
        return _initParameterMap;
    }

    public Set getResourcePaths(String value)
    {
        throw new UnsupportedOperationException();
    }

    public InputStream getResourceAsStream(String value)
    {
        String systemId = (String)_resourceMap.get(value);
        if (systemId == null)
        {
            systemId = value;
        }
        return ExternalContextMockImpl.class.getClassLoader().getResourceAsStream(systemId);
    }

    public String getAuthType()
    {
        throw new UnsupportedOperationException();
    }

    public String getRemoteUser()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isUserInRole(String value)
    {
        throw new UnsupportedOperationException();
    }

    public Principal getUserPrincipal()
    {
        return _principal;
    }

    public String encodeActionURL(String value)
    {
        throw new UnsupportedOperationException();
    }

    public String encodeResourceURL(String value)
    {
        throw new UnsupportedOperationException();
    }

    public String encodeNamespace(String value)
    {
        throw new UnsupportedOperationException();
    }

    public void dispatchMessage(String value) throws IOException, FacesException
    {
        throw new UnsupportedOperationException();
    }

    public void log(String value)
    {
        throw new UnsupportedOperationException();
    }

    public void log(String value, Throwable arg1)
    {
        throw new UnsupportedOperationException();
    }
}

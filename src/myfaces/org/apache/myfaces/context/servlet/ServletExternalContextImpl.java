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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.myfaces.util.EnumerationIterator;

/**
 * JSF 1.0 PRD2, 6.1.1
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ServletExternalContextImpl
    extends ExternalContext
{
    private static final String INIT_PARAMETER_MAP_ATTRIBUTE = InitParameterMap.class.getName();
    
    private ServletContext _servletContext;
    private ServletRequest _servletRequest;
    private ServletResponse _servletResponse;
    private Map _applicationMap;
    private Map _sessionMap;
    private Map _requestMap;
    private Map _requestParameterMap;
    private Map _requestParameterValuesMap;
    private Map _requestHeaderMap;
    private Map _requestHeaderValuesMap;
    private Map _requestCookieMap;
    private Map _initParameterMap;
    private boolean _isHttpServletRequest;
    private String _requestServletPath;

    public ServletExternalContextImpl(ServletContext servletContext,
                                      ServletRequest servletRequest,
                                      ServletResponse servletResponse)
    {
        _servletContext = servletContext;
        _servletRequest = servletRequest;
        _servletResponse = servletResponse;
        _applicationMap = null;
        _sessionMap = null;
        _requestMap = null;
        _requestParameterMap = null;
        _requestParameterValuesMap = null;
        _requestHeaderMap = null;
        _requestHeaderValuesMap = null;
        _requestCookieMap = null;
        _initParameterMap = null;
        _isHttpServletRequest = (servletRequest != null &&
                                 servletRequest instanceof HttpServletRequest);
        if (_isHttpServletRequest)
        {
            //HACK: MultipartWrapper scrambles the servletPath for some reason !?
            _requestServletPath = ((HttpServletRequest)servletRequest).getServletPath();
        }
    }

    public void release()
    {
        _servletContext = null;
        _servletRequest = null;
        _servletResponse = null;
        _applicationMap = null;
        _sessionMap = null;
        _requestMap = null;
        _requestParameterMap = null;
        _requestParameterValuesMap = null;
        _requestHeaderMap = null;
        _requestHeaderValuesMap = null;
        _requestCookieMap = null;
        _initParameterMap = null;
    }


    public Object getSession(boolean create)
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return ((HttpServletRequest)_servletRequest).getSession(create);
    }

    public Object getContext()
    {
        return _servletContext;
    }

    public Object getRequest()
    {
        return _servletRequest;
    }

    public Object getResponse()
    {
        return _servletResponse;
    }

    public Map getApplicationMap()
    {
        if (_applicationMap == null)
        {
            _applicationMap = new ApplicationMap(_servletContext);
        }
        return _applicationMap;
    }

    public Map getSessionMap()
    {
        if (_sessionMap == null)
        {
            if (!_isHttpServletRequest)
            {
                throw new IllegalArgumentException("Only HttpServletRequest supported");
            }
            _sessionMap = new SessionMap((HttpServletRequest) _servletRequest);
        }
        return _sessionMap;
    }

    public Map getRequestMap()
    {
        if (_requestMap == null)
        {
            _requestMap = new RequestMap(_servletRequest);
        }
        return _requestMap;
    }

    public Map getRequestParameterMap()
    {
        if (_requestParameterMap == null)
        {
            _requestParameterMap = new RequestParameterMap(_servletRequest);
        }
        return _requestParameterMap;
    }

    public Map getRequestParameterValuesMap()
    {
        if (_requestParameterValuesMap == null)
        {
            _requestParameterValuesMap = new RequestParameterValuesMap(_servletRequest);
        }
        return _requestParameterValuesMap;
    }

    public Iterator getRequestParameterNames()
    {
        final Enumeration enum = _servletRequest.getParameterNames();
        Iterator it = new Iterator()
        {
            public boolean hasNext() {
                return enum.hasMoreElements();
            }

            public Object next() {
                return enum.nextElement();
            }

            public void remove() {
                throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
            }
        };
        return it;
    }

    public Map getRequestHeaderMap()
    {
        if (_requestHeaderMap == null)
        {
            if (!_isHttpServletRequest)
            {
                throw new IllegalArgumentException("Only HttpServletRequest supported");
            }
            _requestHeaderMap = new RequestHeaderMap((HttpServletRequest)_servletRequest);
        }
        return _requestHeaderMap;
    }

    public Map getRequestHeaderValuesMap()
    {
        if (_requestHeaderValuesMap == null)
        {
            if (!_isHttpServletRequest)
            {
                throw new IllegalArgumentException("Only HttpServletRequest supported");
            }
            _requestHeaderValuesMap = new RequestHeaderValuesMap((HttpServletRequest)_servletRequest);
        }
        return _requestHeaderValuesMap;
    }

    public Map getRequestCookieMap()
    {
        if (_requestCookieMap == null)
        {
            if (!_isHttpServletRequest)
            {
                throw new IllegalArgumentException("Only HttpServletRequest supported");
            }
            _requestCookieMap = new CookieMap((HttpServletRequest)_servletRequest);
        }
        return _requestCookieMap;
    }

    public Locale getRequestLocale()
    {
        return _servletRequest.getLocale();
    }

    public String getRequestPathInfo()
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return ((HttpServletRequest)_servletRequest).getPathInfo();
    }

    public String getRequestContextPath()
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return ((HttpServletRequest)_servletRequest).getContextPath();
    }

    /*
    public Cookie[] getRequestCookies()
    {
        return new Cookie[0];  //To change body of implemented methods use Options | File Templates.
    }
    */

    public String getInitParameter(String s)
    {
        return _servletContext.getInitParameter(s);
    }

    public Map getInitParameterMap()
    {
        if (_initParameterMap == null)
        {
            // We cache it as an attribute in ServletContext itself (is this circular reference a problem?)
            if ((_initParameterMap = (Map) _servletContext.getAttribute(INIT_PARAMETER_MAP_ATTRIBUTE)) == null)
            {
                _initParameterMap = new InitParameterMap(_servletContext);
                _servletContext.setAttribute(INIT_PARAMETER_MAP_ATTRIBUTE, _initParameterMap);
            }
        }
        return _initParameterMap;
    }

    public Set getResourcePaths(String s)
    {
        return _servletContext.getResourcePaths(s);
    }

    public InputStream getResourceAsStream(String s)
    {
        return _servletContext.getResourceAsStream(s);
    }

    public String encodeActionURL(String s)
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return ((HttpServletResponse)_servletResponse).encodeURL(s);
    }

    public String encodeResourceURL(String s)
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return ((HttpServletResponse)_servletResponse).encodeURL(s);
    }

    public String encodeNamespace(String s)
    {
        return s;
    }

    public void dispatch(String requestURI) throws IOException, FacesException
    {
        RequestDispatcher requestDispatcher
            = _servletRequest.getRequestDispatcher(requestURI);
        try
        {
            requestDispatcher.forward(_servletRequest, _servletResponse);
        }
        catch (ServletException e)
        {
            throw new FacesException(e);
        }
    }

    public String getRequestServletPath()
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        //return ((HttpServletRequest)_servletRequest).getServletPath();
        //HACK: see constructor
        return _requestServletPath;
    }

    public String getAuthType()
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return ((HttpServletRequest)_servletRequest).getAuthType();
    }

    public String getRemoteUser()
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return ((HttpServletRequest)_servletRequest).getRemoteUser();
    }

    public boolean isUserInRole(String role)
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return ((HttpServletRequest)_servletRequest).isUserInRole(role);
    }

    public Principal getUserPrincipal()
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return ((HttpServletRequest)_servletRequest).getUserPrincipal();
    }

    public void log(String message) {
        _servletContext.log(message);
    }

    public void log(String message, Throwable t) {
        _servletContext.log(message, t);
    }

    public void redirect(String url) throws IOException
    {
        if (_servletResponse instanceof HttpServletResponse)
        {
            ((HttpServletResponse)_servletResponse).sendRedirect(url);
        }
        else
        {
            throw new IllegalArgumentException("Only HttpServletResponse supported");
        }
    }

    public Iterator getRequestLocales()
    {
        if (!_isHttpServletRequest)
        {
            throw new IllegalArgumentException("Only HttpServletRequest supported");
        }
        return new EnumerationIterator(((HttpServletRequest)_servletRequest).getLocales());
    }

    public URL getResource(String s) throws MalformedURLException
    {
        return _servletContext.getResource(s);
    }
}

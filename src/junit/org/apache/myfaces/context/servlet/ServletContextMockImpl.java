/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.context.servlet;

import net.sourceforge.myfaces.util.IteratorEnumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ServletContextMockImpl
    implements ServletContext
{
    private static final Log log = LogFactory.getLog(ServletContextMockImpl.class);

    private Map _attributes = new HashMap();
    private Map _initParameters = new HashMap();
    private Map _resourceMap = new HashMap();


    public ServletContext getContext(String s)
    {
        throw new UnsupportedOperationException();
    }

    public int getMajorVersion()
    {
        throw new UnsupportedOperationException();
    }

    public int getMinorVersion()
    {
        throw new UnsupportedOperationException();
    }

    public String getMimeType(String s)
    {
        throw new UnsupportedOperationException();
    }

    public Set getResourcePaths(String s)
    {
        /* is this needed by any test?
        if (s.equals("/WEB-INF/lib/"))
        {
            return Collections.singleton("/WEB-INF/lib/myfaces.jar");
        }
        else
        {
            return null;
        }
        */
        return Collections.EMPTY_SET;
    }

    public URL getResource(String s) throws MalformedURLException
    {
        String path = (String)_resourceMap.get(s);
        if (path != null)
        {
            return Thread.currentThread().getContextClassLoader()
                    .getResource(path);
        }
        else
        {
            log.warn("Resource '" + s + "' cannot be resolved.");
            return null;
        }
    }

    public InputStream getResourceAsStream(String s)
    {
        String path = (String)_resourceMap.get(s);
        if (path != null)
        {
            return Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(path);
        }
        else
        {
            log.warn("Resource '" + s + "' cannot be resolved.");
            return null;
        }
    }

    public RequestDispatcher getRequestDispatcher(String s)
    {
        throw new UnsupportedOperationException();
    }

    public RequestDispatcher getNamedDispatcher(String s)
    {
        throw new UnsupportedOperationException();
    }

    public Servlet getServlet(String s) throws ServletException
    {
        throw new UnsupportedOperationException();
    }

    public Enumeration getServlets()
    {
        throw new UnsupportedOperationException();
    }

    public Enumeration getServletNames()
    {
        throw new UnsupportedOperationException();
    }

    public void log(String s)
    {
        throw new UnsupportedOperationException();
    }

    /**@deprecated*/
    public void log(Exception exception, String s)
    {
        throw new UnsupportedOperationException();
    }

    public void log(String s, Throwable throwable)
    {
        throw new UnsupportedOperationException();
    }

    public String getRealPath(String s)
    {
        return "/junit_dummy_real_path";
    }

    public String getServerInfo()
    {
        throw new UnsupportedOperationException();
    }

    public String getInitParameter(String s)
    {
        return (String) _initParameters.get(s);
    }

    public Enumeration getInitParameterNames()
    {
        return new IteratorEnumeration(_initParameters.keySet().iterator());
    }

    public Object getAttribute(String s)
    {
        return _attributes.get(s);
    }

    public Enumeration getAttributeNames()
    {
        return new IteratorEnumeration(_attributes.keySet().iterator());
    }

    public void setAttribute(String s, Object obj)
    {
        _attributes.put(s, obj);
    }

    public void removeAttribute(String s)
    {
        _attributes.remove(s);
    }

    public String getServletContextName()
    {
        throw new UnsupportedOperationException();
    }


    // mock methods

    public void addResource(String webPath, String classPath)
    {
        _resourceMap.put(webPath, classPath);
    }


}

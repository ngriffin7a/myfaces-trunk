/*
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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

import net.sourceforge.myfaces.TestConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        if (s.equals("/WEB-INF/lib/"))
        {
            return Collections.singleton("/WEB-INF/lib/myfaces.jar");
        }
        else
        {
            return null;
        }
    }

    public URL getResource(String s) throws MalformedURLException
    {
        if (s.startsWith("/WEB-INF/"))
        {
            try {
                return new URL("file://" + 
                        new File(TestConfig.getContextPath(), s).getCanonicalPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            log.warn("Resource '" + s + "' cannot be resolved.");
            return null;
        }
    }

    public InputStream getResourceAsStream(String s)
    {
        if (s.startsWith("/WEB-INF/"))
        {
            try
            {
                return new FileInputStream(
                        new File(TestConfig.getContextPath(), s).getCanonicalFile());
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
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
        throw new UnsupportedOperationException();
    }

    public String getServerInfo()
    {
        throw new UnsupportedOperationException();
    }

    public String getInitParameter(String s)
    {
        return null;
    }

    public Enumeration getInitParameterNames()
    {
        throw new UnsupportedOperationException();
    }

    public Object getAttribute(String s)
    {
        return _attributes.get(s);
    }

    public Enumeration getAttributeNames()
    {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(String s, Object obj)
    {
        _attributes.put(s, obj);
    }

    public void removeAttribute(String s)
    {
        throw new UnsupportedOperationException();
    }

    public String getServletContextName()
    {
        throw new UnsupportedOperationException();
    }
}

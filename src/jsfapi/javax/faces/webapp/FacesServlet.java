/*
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
package javax.faces.webapp;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.*;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesServlet
        implements Servlet
{
    public static final String CONFIG_FILES_ATTR = "javax.faces.CONFIG_FILES";
    public static final String LIFECYCLE_ID_ATTR = "javax.faces.LIFECYCLE_ID";

    private static final String SERVLET_INFO = "FacesServlet of the MyFaces API implementation";
    private ServletConfig _servletConfig;
    private FacesContextFactory _facesContextFactory;
    private Lifecycle _lifecycle;

    public FacesServlet()
    {
        super();
    }

    public void destroy()
    {
        _servletConfig = null;
        _facesContextFactory = null;
        _lifecycle = null;
    }

    public ServletConfig getServletConfig()
    {
        return _servletConfig;
    }

    public String getServletInfo()
    {
        return SERVLET_INFO;
    }

    private String getLifecycleId()
    {
        String lifecycleId = _servletConfig.getInitParameter(LIFECYCLE_ID_ATTR);
        return lifecycleId != null ? lifecycleId : LifecycleFactory.DEFAULT_LIFECYCLE;
    }

    public void init(ServletConfig servletConfig)
            throws ServletException
    {
        _servletConfig = servletConfig;
        _facesContextFactory = (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        //TODO: null-check for Weblogic, that tries to initialize Servlet before ContextListener

        //Javadoc says: Lifecycle instance is shared across multiple simultaneous requests, it must be implemented in a thread-safe manner.
        //So we can acquire it here once:
        LifecycleFactory lifecycleFactory = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        _lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());
    }

    public void service(ServletRequest request,
                        ServletResponse response)
            throws IOException,
                   ServletException
    {
        FacesContext facesContext
                = _facesContextFactory.getFacesContext(_servletConfig.getServletContext(),
                                                       request,
                                                       response,
                                                       _lifecycle);
        try
        {
            _lifecycle.execute(facesContext);
            _lifecycle.render(facesContext);
        }
        catch (Throwable e)
        {
            logException(e, null);
            if (e instanceof IOException)
            {
                throw (IOException)e;
            }
            else if (e instanceof ServletException)
            {
                throw (ServletException)e;
            }
            else if (e.getMessage() != null)
            {
                throw new ServletException(e.getMessage(), e);
            }
            else
            {
                throw new ServletException(e);
            }
        }
        finally
        {
            facesContext.release();
        }
    }

    private void logException(Throwable e, String msgPrefix)
    {
        String msg;
        if (msgPrefix == null)
        {
            if (e.getMessage() == null)
            {
                msg = "Exception in FacesServlet";
            }
            else
            {
                msg = e.getMessage();
            }
        }
        else
        {
            if (e.getMessage() == null)
            {
                msg = msgPrefix;
            }
            else
            {
                msg = msgPrefix + ": " + e.getMessage();
            }
        }

         _servletConfig.getServletContext().log(msg, e);
        e.printStackTrace();

        Throwable cause = e.getCause();
        if (cause != null && cause != e)
        {
            logException(cause, "Root cause");
        }
    }


}

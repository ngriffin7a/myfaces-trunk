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
 * $Log$
 * Revision 1.12  2004/07/13 15:43:34  manolito
 * Bug #990228
 *
 * Revision 1.11  2004/07/01 22:00:54  mwessendorf
 * ASF switch
 *
 * Revision 1.10  2004/06/21 10:57:58  manolito
 * missing CVS Log keyword
 *
 * Revision 1.9  2004/06/16 23:02:20  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.8  2004/06/14 12:55:23  manolito
 * Added missing CVS Log comment
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
        String lifecycleId = _servletConfig.getServletContext().getInitParameter(LIFECYCLE_ID_ATTR);
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

        if(e instanceof ServletException)
        {
            cause = ((ServletException) e).getRootCause();

            if(cause != null && cause != e)
            {
                logException(cause, "Root cause of ServletException");
            }
        }
    }


}

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
package net.sourceforge.myfaces.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.webapp.FacesServlet;
import javax.servlet.*;
import java.io.IOException;

/**
 * Derived FacesServlet that can be used for debugging purpose
 * and to fix the Weblogic startup issue (FacesServlet is initialized before ServletContextListener).
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.18  2004/07/16 15:16:10  royalts
 * moved net.sourceforge.myfaces.webapp.webxml and net.sourceforge.util.xml to share src-tree (needed WebXml for JspTilesViewHandlerImpl)
 *
 * Revision 1.17  2004/07/01 22:05:11  mwessendorf
 * ASF switch
 *
 * Revision 1.16  2004/04/16 13:21:39  manolito
 * Weblogic startup issue
 *
 */
public class MyFacesServlet
    extends FacesServlet
{
    private static final Log log = LogFactory.getLog(MyFacesServlet.class);

    public void init(ServletConfig servletConfig)
        throws ServletException
    {
        //Check, if ServletContextListener already called
        ServletContext servletContext = servletConfig.getServletContext();
        Boolean b = (Boolean)servletContext.getAttribute(net.sourceforge.myfaces.webapp.StartupServletContextListener.FACES_INIT_DONE);
        if (b == null || b.booleanValue() == false)
        {
            log.warn("ServletContextListener not yet called");
            net.sourceforge.myfaces.webapp.StartupServletContextListener.initFaces(servletConfig.getServletContext());
        }
        super.init(servletConfig);
        log.info("MyFacesServlet for context '" + servletConfig.getServletContext().getRealPath("/") + "' initialized.");
    }

    public void service(ServletRequest request, ServletResponse response)
            throws IOException,
                   ServletException
    {
        if (log.isTraceEnabled()) log.trace("MyFacesServlet service start");
        super.service(request, response);
        if (log.isTraceEnabled()) log.trace("MyFacesServlet service finished");
    }

}

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
        Boolean b = (Boolean)servletContext.getAttribute(StartupServletContextListener.FACES_INIT_DONE);
        if (b == null || b.booleanValue() == false)
        {
            log.warn("ServletContextListener not yet called");
            StartupServletContextListener.initFaces(servletConfig.getServletContext());
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

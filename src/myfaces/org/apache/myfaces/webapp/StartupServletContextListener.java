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

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.config.configure.FacesConfigurator;
import net.sourceforge.myfaces.context.servlet.ServletExternalContextImpl;
import net.sourceforge.myfaces.webapp.webxml.WebXml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * TODO: Add listener to myfaces-core.tld instead of web.xml
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.20  2004/04/26 11:28:17  manolito
 * global navigation-rule with no from-view-id NPE bug
 *
 * Revision 1.19  2004/04/16 13:21:39  manolito
 * Weblogic startup issue
 *
 */
public class StartupServletContextListener
        implements ServletContextListener
{
    private static final Log log = LogFactory.getLog(StartupServletContextListener.class);

    static final String FACES_INIT_DONE
            = StartupServletContextListener.class.getName() + ".FACES_INIT_DONE";

    public void contextInitialized(ServletContextEvent event)
    {
        initFaces(event.getServletContext());
    }

    public static void initFaces(ServletContext servletContext)
    {
        try
        {
            Boolean b = (Boolean)servletContext.getAttribute(FACES_INIT_DONE);

            if (b == null || b.booleanValue() == false)
            {
                log.trace("Initializing MyFaces");

                //Load the configuration
                ExternalContext externalContext = new ServletExternalContextImpl(servletContext,
                                                                                 null,
                                                                                 null);
                FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(externalContext);
                FacesConfig facesConfig = fcf.getFacesConfig(externalContext);

                //And configure everything
                new FacesConfigurator(facesConfig, externalContext).configure();

                // parse web.xml
                WebXml.init(externalContext);

                servletContext.setAttribute(FACES_INIT_DONE, Boolean.TRUE);
            }
            else
            {
                log.info("MyFaces already initialized");
            }
        }
        catch (Exception ex)
        {
            log.error("Error initializing ServletContext", ex);
            ex.printStackTrace();
        }
        log.info("ServletContext '" + servletContext.getRealPath("/") + "' initialized.");
    }


    public void contextDestroyed(ServletContextEvent e)
    {
        FactoryFinder.releaseFactories();
    }
}

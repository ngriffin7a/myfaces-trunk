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
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StartupServletContextListener
        implements ServletContextListener
{
    private static final Log log = LogFactory.getLog(StartupServletContextListener.class);
    
    public static ThreadLocal s_externalContext = new ThreadLocal(); 

    public void contextInitialized(ServletContextEvent event)
    {
        try
        {
            //TODO: Add default factories to FactoryFinder before parsing config!
            
            ServletContext servletContext = event.getServletContext();
            ExternalContext externalContext = new ServletExternalContextImpl(servletContext,
                                                                             null,
                                                                             null);
            
            // set in ThreadLocal so that config methods cat use it
            s_externalContext.set(externalContext);

            FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(externalContext);
            FacesConfig facesConfig = fcf.getFacesConfig(externalContext);

            new FacesConfigurator(facesConfig, externalContext).configure();

            // parse web.xml
            WebXml.init(externalContext);
        }
        catch (Exception ex)
        {
            log.error("Error initializing ServletContext", ex);
            ex.printStackTrace();
        }
        log.info("ServletContext '" + event.getServletContext().getRealPath("/") + "' initialized.");
    }

    public void contextDestroyed(ServletContextEvent e)
    {
        FactoryFinder.releaseFactories();
    }
}

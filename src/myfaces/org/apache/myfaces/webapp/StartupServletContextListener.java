/**
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
package net.sourceforge.myfaces.webapp;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.context.ExternalContextImpl;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.faces.context.ExternalContext;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StartupServletContextListener
        implements ServletContextListener
{
    private static final Log log = LogFactory.getLog(StartupServletContextListener.class);

    public void contextInitialized(ServletContextEvent e)
    {
        try
        {
            ServletContext servletContext = e.getServletContext();

            //Set logging level
            /*
            setLoggerLevel(LogUtil.getLogger(),
                           MyFacesConfig.getLogLevel(servletContext));
            */

            FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(servletContext);
            ExternalContext externalContext = new ExternalContextImpl(servletContext, null, null);
            FacesConfig facesConfig = fcf.getFacesConfig(externalContext);
            facesConfig.configureAll();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        log.info("ServletContext '" + e.getServletContext().getRealPath("/") + "' initialized.");
    }

    public void contextDestroyed(ServletContextEvent e)
    {
        // do nothing 
    }


    /*
    private void setLoggerLevel(Logger logger, Level logLevel)
    {
        logger.setLevel(logLevel);

        Handler[] logHandlers = logger.getHandlers();
        for (int i = 0; i < logHandlers.length; i++)
        {
            logHandlers[i].setLevel(logLevel);
        }
    }
    */

}

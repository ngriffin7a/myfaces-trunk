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
package org.apache.myfaces.webapp;

import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.myfaces.config.FacesConfigurator;
import org.apache.myfaces.context.servlet.ServletExternalContextImpl;
import org.apache.myfaces.webapp.webxml.WebXml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO: Add listener to myfaces-core.tld instead of web.xml
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.28  2004/10/13 11:51:01  matze
 * renamed packages to org.apache
 *
 * Revision 1.27  2004/07/16 17:46:26  royalts
 * moved org.apache.myfaces.webapp.webxml and org.apache.util.xml to share src-tree (needed WebXml for JspTilesViewHandlerImpl)
 *
 * Revision 1.26  2004/07/16 15:16:10  royalts
 * moved org.apache.myfaces.webapp.webxml and org.apache.util.xml to share src-tree (needed WebXml for JspTilesViewHandlerImpl)
 *
 * Revision 1.25  2004/07/07 08:34:57  mwessendorf
 * removed unused import-statements
 *
 * Revision 1.24  2004/07/07 00:25:08  o_rossmueller
 * tidy up config/confignew package (moved confignew classes to package config)
 *
 * Revision 1.23  2004/07/06 23:46:01  o_rossmueller
 * tidy up config/confignew package
 *
 * Revision 1.22  2004/07/01 22:05:11  mwessendorf
 * ASF switch
 *
 * Revision 1.21  2004/06/16 23:02:25  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.20.2.2  2004/06/16 02:07:24  o_rossmueller
 * get navigation rules from RuntimeConfig
 * refactored all remaining usages of MyFacesFactoryFinder to use RuntimeConfig
 *
 * Revision 1.20.2.1  2004/06/13 15:59:08  o_rossmueller
 * started integration of new config mechanism:
 * - factories
 * - components
 * - render kits
 * - managed beans + managed properties (no list/map initialization)
 *
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
                ExternalContext externalContext = new ServletExternalContextImpl(servletContext, null, null);

                //And configure everything
                new FacesConfigurator(externalContext).configure();

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

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
package net.sourceforge.myfaces.application.jsp;

import org.apache.struts.tiles.DefinitionsFactoryConfig;
import org.apache.struts.tiles.DefinitionsFactory;
import org.apache.struts.tiles.TilesUtil;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ViewHandler;
import javax.faces.FactoryFinder;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/07/16 17:46:46  royalts
 * moved net.sourceforge.myfaces.webapp.webxml and net.sourceforge.util.xml to share src-tree (needed WebXml for JspTilesViewHandlerImpl)
 *
 */
public class JspTilesViewHandlerInitializer
    implements ServletContextListener
{
    private static final Log log = LogFactory.getLog(JspTilesViewHandlerImpl.class);

    private static final String TILES_DEF_ATTR = "tiles-definitions";

    public void contextInitialized(ServletContextEvent event)
    {
        Application app = ((ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY)).getApplication();
        ViewHandler handler = app.getViewHandler();
        if (handler instanceof JspTilesViewHandlerImpl)
        {
            try
            {
                if (log.isTraceEnabled()) log.trace("MyFacesServlet service start");
                ServletContext context = event.getServletContext();
                String tilesDefinitions = context.getInitParameter("tiles-definitions");
                if (tilesDefinitions == null)
                {
                    log.fatal("No Tiles definition found. Specify Definition files by adding "
                              + TILES_DEF_ATTR + " param in your web.xml");
                    return;
                }
                DefinitionsFactoryConfig factoryConfig = new DefinitionsFactoryConfig();
                factoryConfig.setDefinitionConfigFiles(tilesDefinitions);
                DefinitionsFactory definitionFactory = TilesUtil.createDefinitionsFactory(event.getServletContext(), factoryConfig);
                ((JspTilesViewHandlerImpl)handler).setDefinitionsFactory(definitionFactory);
            }
            catch (DefinitionsFactoryException e)
            {
                log.fatal("DefinitionsFactoryException", e);
            }
        }
    }

    public void contextDestroyed(ServletContextEvent event)
    {
    }

}

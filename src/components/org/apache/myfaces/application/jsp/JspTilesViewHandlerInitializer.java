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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/08/26 14:25:21  manolito
 * JspTilesViewHandlerInitializer no longer needed, JspTilesViewHandlerImpl is initialized automatically now
 *
 * Revision 1.1  2004/07/16 17:46:46  royalts
 * moved net.sourceforge.myfaces.webapp.webxml and net.sourceforge.util.xml to share src-tree (needed WebXml for JspTilesViewHandlerImpl)
 *
 * @deprecated No longer needed, JspTilesViewHandlerImpl initializes automatically now
 */
public class JspTilesViewHandlerInitializer
    implements ServletContextListener
{
    private static final Log log = LogFactory.getLog(JspTilesViewHandlerImpl.class);

    public void contextInitialized(ServletContextEvent event)
    {
        log.warn("You still have a JspTilesViewHandlerInitializer in your web.xml. Please remove it: deprecated and no longer needed.");
    }

    public void contextDestroyed(ServletContextEvent event)
    {
    }

}

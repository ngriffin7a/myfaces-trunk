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
package net.sourceforge.myfaces.webapp.webxml;

import net.sourceforge.myfaces.util.ClassUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.webapp.FacesServlet;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/08/10 10:57:39  manolito
 * fixed StackOverflow in ClassUtils and cleaned up ClassUtils methods
 *
 * Revision 1.1  2004/07/16 15:16:10  royalts
 * moved net.sourceforge.myfaces.webapp.webxml and net.sourceforge.util.xml to share src-tree (needed WebXml for JspTilesViewHandlerImpl)
 *
 * Revision 1.10  2004/07/01 22:05:16  mwessendorf
 * ASF switch
 *
 * Revision 1.9  2004/04/19 13:03:21  manolito
 * Log
 *
 */
public class WebXml
{
    private static final Log log = LogFactory.getLog(WebXmlParser.class);

    private Map _servlets = new HashMap();
    private Map _servletMappings = new HashMap();
    private List _facesServletMappings = null;

    void addServlet(String servletName, String servletClass)
    {
        if (_servlets.get(servletName) != null)
        {
            log.warn("Servlet " + servletName + " defined more than once, first definition will be used.");
        }
        else
        {
            _servlets.put(servletName, servletClass);
        }
    }

    boolean containsServlet(String servletName)
    {
        return _servlets.containsKey(servletName);
    }

    void addServletMapping(String servletName, String urlPattern)
    {
        List mappings = (List)_servletMappings.get(servletName);
        if (mappings == null)
        {
            mappings = new ArrayList();
            _servletMappings.put(servletName, mappings);
        }
        mappings.add(urlPattern);
    }

    public List getFacesServletMappings()
    {
        if (_facesServletMappings != null) return _facesServletMappings;

        _facesServletMappings = new ArrayList();
        for (Iterator it = _servlets.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)it.next();
            String servletName = (String)entry.getKey();
            if (null == entry.getValue())
            {
                // the value is null in the case of jsp files listed as servlets 
                // in cactus
                // <servlet>
                //   <servlet-name>JspRedirector</servlet-name>
                //   <jsp-file>/jspRedirector.jsp</jsp-file>
                // </servlet>
                continue;
            }
            Class servletClass = ClassUtils.simpleClassForName((String)entry.getValue());
            if (FacesServlet.class.isAssignableFrom(servletClass))
            {
                List urlPatterns = (List)_servletMappings.get(servletName);
                for (Iterator it2 = urlPatterns.iterator(); it2.hasNext(); )
                {
                    String urlpattern = (String)it2.next();
                    _facesServletMappings.add(new ServletMapping(servletName,
                                                                 servletClass,
                                                                 urlpattern));
                if (log.isTraceEnabled())
                    log.trace("adding mapping for servlet + " + servletName + " urlpattern = " + urlpattern);                    }
            }
            else
            {
                if (log.isTraceEnabled()) log.trace("ignoring servlet + " + servletName + " " + servletClass + " (no FacesServlet)");
            }
        }
        return _facesServletMappings;
    }


    private static final String WEB_XML_ATTR = WebXml.class.getName();
    public static WebXml getWebXml(ExternalContext context)
    {
        WebXml webXml = (WebXml)context.getApplicationMap().get(WEB_XML_ATTR);
        if (webXml == null)
        {
            log.error(WebXml.class.getName() + ".init must be called before!");
            throw new IllegalStateException(WebXml.class.getName() + ".init must be called before!");
        }
        return webXml;
    }

    /**
     * should be called when initialising Servlet
     * @param context
     */
    public static void init(ExternalContext context)
    {
        WebXmlParser parser = new WebXmlParser(context);
        WebXml webXml = parser.parse();
        context.getApplicationMap().put(WEB_XML_ATTR, webXml);
    }
}

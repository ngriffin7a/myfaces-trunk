/*
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
package net.sourceforge.myfaces.webapp.webxml;

import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.webapp.MyFacesServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.webapp.FacesServlet;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
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
            try
            {
                Class servletClass = ClassUtils.classForName((String)entry.getValue());
                if (MyFacesServlet.class.isAssignableFrom(servletClass) ||
                    FacesServlet.class.isAssignableFrom(servletClass))
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
            catch (ClassNotFoundException e)
            {
                log.error("Servlet class " + entry.getValue() + " not found", e);
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

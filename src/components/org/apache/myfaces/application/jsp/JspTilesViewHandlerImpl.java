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
import org.apache.struts.tiles.DefinitionsFactory;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ComponentDefinition;
import org.apache.struts.tiles.DefinitionsFactoryException;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import java.util.Locale;
import java.util.List;
import java.io.IOException;

import net.sourceforge.myfaces.webapp.webxml.WebXml;
import net.sourceforge.myfaces.webapp.webxml.ServletMapping;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/07/16 17:46:46  royalts
 * moved net.sourceforge.myfaces.webapp.webxml and net.sourceforge.util.xml to share src-tree (needed WebXml for JspTilesViewHandlerImpl)
 *
 */
public class JspTilesViewHandlerImpl
    extends ViewHandler
{
    private ViewHandler _viewHandler;

    private static final Log log = LogFactory.getLog(JspTilesViewHandlerImpl.class);

    private DefinitionsFactory _definitionsFactory;

    public JspTilesViewHandlerImpl(ViewHandler viewHandler)
    {
        _viewHandler = viewHandler;
    }

    public void setDefinitionsFactory(DefinitionsFactory definitionsFactory)
    {
        _definitionsFactory = definitionsFactory;
    }

    public void renderView(FacesContext facesContext, UIViewRoot viewToRender) throws IOException, FacesException
    {
        if (viewToRender == null)
        {
            log.fatal("viewToRender must not be null");
            throw new NullPointerException("viewToRender must not be null");
        }
        if (_definitionsFactory == null)
        {
            log.fatal("JspTilesViewHandlerImpl not initialized. " +
                      "Check if you added the "  + JspTilesViewHandlerInitializer.class.getName() +
                      " ServletContextListener to your web.xml. ");
            throw new NullPointerException("Tiles DefinitionsFactory must not be null");
        }

        ExternalContext externalContext = facesContext.getExternalContext();

        String viewId = facesContext.getViewRoot().getViewId();
        ServletMapping servletMapping = getServletMapping(externalContext);
        if (servletMapping.isExtensionMapping())
        {
            String defaultSuffix = externalContext.getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
            String suffix = defaultSuffix != null ? defaultSuffix : ViewHandler.DEFAULT_SUFFIX;
            if (!viewId.endsWith(suffix))
            {
                int dot = viewId.lastIndexOf('.');
                if (dot == -1)
                {
                    if (log.isTraceEnabled()) log.trace("Current viewId has no extension, appending default suffix " + suffix);
                    viewId = viewId + suffix;
                }
                else
                {
                    if (log.isTraceEnabled()) log.trace("Replacing extension of current viewId by suffix " + suffix);
                    viewId = viewId.substring(0, dot) + suffix;
                }
                facesContext.getViewRoot().setViewId(viewId);
            }
        }

        if (log.isTraceEnabled()) log.trace("Dispatching to " + viewId);

        String tilesId = viewId;
        tilesId = tilesId.substring(0, tilesId.indexOf("."));
        ServletRequest request = (ServletRequest)externalContext.getRequest();
        ServletContext servletContext = (ServletContext)externalContext.getContext();
        ComponentContext tileContext = ComponentContext.getContext(request);
        ComponentDefinition definition = null;
        try
        {
            definition = _definitionsFactory.getDefinition(tilesId, request, servletContext);
            if (definition == null)
            {
                log.error("could not find tiles-definition with name " + tilesId);
                throw new NullPointerException("could not find tiles-definition with name " + tilesId);
            }

        }
        catch (DefinitionsFactoryException e)
        {
            throw new FacesException(e);
        }
        if( tileContext == null )
        {
            tileContext = new ComponentContext(definition.getAttributes());
            ComponentContext.setContext(tileContext, request);
        }
        else
        {
            tileContext.addMissing(definition.getAttributes());
        }
        viewId = definition.getPage();
        externalContext.dispatch(viewId);
    }

    private static ServletMapping getServletMapping(ExternalContext externalContext)
    {
        String servletPath = externalContext.getRequestServletPath();
        String requestPathInfo = externalContext.getRequestPathInfo();

        WebXml webxml = WebXml.getWebXml(externalContext);
        List mappings = webxml.getFacesServletMappings();

        boolean isExtensionMapping = requestPathInfo == null;

        for (int i = 0, size = mappings.size(); i < size; i++)
        {
            ServletMapping servletMapping = (ServletMapping) mappings.get(i);
            if (servletMapping.isExtensionMapping() == isExtensionMapping)
            {
                String urlpattern = servletMapping.getUrlPattern();
                if (isExtensionMapping)
                {
                    String extension = urlpattern.substring(1, urlpattern.length());
                    if (servletPath.endsWith(extension))
                    {
                        return servletMapping;
                    }
                }
                else
                {
                    urlpattern = urlpattern.substring(0, urlpattern.length() - 2);
                    // servletPath starts with "/" except in the case where the
                    // request is matched with the "/*" pattern, in which case
                    // it is the empty string (see Servlet Sepc 2.3 SRV4.4)
                    if (servletPath.equals(urlpattern))
                    {
                        return servletMapping;
                    }
                }
            }
        }
        log.error("could not find pathMapping for servletPath = " + servletPath +
                  " requestPathInfo = " + requestPathInfo);
        throw new IllegalArgumentException("could not find pathMapping for servletPath = " + servletPath +
                  " requestPathInfo = " + requestPathInfo);
    }


    public Locale calculateLocale(FacesContext context)
    {
        return _viewHandler.calculateLocale(context);
    }

    public String calculateRenderKitId(FacesContext context)
    {
        return _viewHandler.calculateRenderKitId(context);
    }

    public UIViewRoot createView(FacesContext context, String viewId)
    {
        return _viewHandler.createView(context, viewId);
    }

    public String getActionURL(FacesContext context, String viewId)
    {
        return _viewHandler.getActionURL(context, viewId);
    }

    public String getResourceURL(FacesContext context, String path)
    {
        return _viewHandler.getResourceURL(context, path);
    }

    public UIViewRoot restoreView(FacesContext context, String viewId)
    {
        return _viewHandler.restoreView(context, viewId);
    }

    public void writeState(FacesContext context) throws IOException
    {
        _viewHandler.writeState(context);
    }

}

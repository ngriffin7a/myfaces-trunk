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

import net.sourceforge.myfaces.util.DebugUtils;
import net.sourceforge.myfaces.webapp.webxml.ServletMapping;
import net.sourceforge.myfaces.webapp.webxml.WebXml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.22  2004/07/01 22:05:20  mwessendorf
 * ASF switch
 *
 * Revision 1.21  2004/05/18 12:02:14  manolito
 * getActionURL and getResourceURL must not call encodeActionURL or encodeResourceURL
 *
 * Revision 1.20  2004/05/12 01:41:31  o_rossmueller
 * fix #951896: added state params to link URLs for ALLOW_JAVASCRIPT=false
 *
 * Revision 1.19  2004/04/27 07:37:56  manolito
 * bugfix: handleCharacterEncoding must be called before any request parameters are retrieved
 *
 * Revision 1.18  2004/04/05 09:16:24  manolito
 * javadoc header
 *
 */
public class JspViewHandlerImpl
        extends ViewHandler
{
    private static final Log log = LogFactory.getLog(JspViewHandlerImpl.class);
    public static final String FORM_STATE_MARKER     = "<!--@@JSF_FORM_STATE_MARKER@@-->";
    public static final int    FORM_STATE_MARKER_LEN = FORM_STATE_MARKER.length();


    public JspViewHandlerImpl()
    {
        if (log.isTraceEnabled()) log.trace("New ViewHandler instance created");
    }

    public Locale calculateLocale(FacesContext facesContext)
    {
        //ExternalContext.getLocales() is missing. Next Spec will define it.
        //But since we are in a JSP specific impl we can cast...
        Enumeration locales = ((ServletRequest)facesContext.getExternalContext().getRequest()).getLocales();
        while (locales.hasMoreElements())
        {
            Locale locale = (Locale) locales.nextElement();
            for (Iterator it = facesContext.getApplication().getSupportedLocales(); it.hasNext();)
            {
                Locale supportLocale = (Locale)it.next();
                // higher priority to a language match over an exact match
                // that occures further down (see Jstl Reference 1.0 8.3.1)
                if (locale.getLanguage().equals(supportLocale.getLanguage()) &&
                    (supportLocale.getCountry() == null ||
                     supportLocale.getCountry().length() == 0))
                {
                    return supportLocale;
                }
                else if (supportLocale.equals(locale))
                {
                    return supportLocale;
                }
            }
        }

        Locale defaultLocale = facesContext.getApplication().getDefaultLocale();
        return defaultLocale != null ? defaultLocale : Locale.getDefault();
    }

    public String calculateRenderKitId(FacesContext facesContext)
    {
        return RenderKitFactory.HTML_BASIC_RENDER_KIT;  //TODO: how to calculate from client?
    }

    /**
     */
    public UIViewRoot createView(FacesContext facesContext, String viewId)
    {
        Locale currentLocale = null;
        String currentRenderKitId = null;
        UIViewRoot uiViewRoot = facesContext.getViewRoot();
        if (uiViewRoot != null)
        {
            //Remember current locale and renderKitId
            currentLocale = uiViewRoot.getLocale();
            currentRenderKitId = uiViewRoot.getRenderKitId();
        }

        uiViewRoot = (UIViewRoot)facesContext.getApplication().createComponent(UIViewRoot.COMPONENT_TYPE);
        uiViewRoot.setViewId(viewId);

        if (currentLocale != null)
        {
            //set old locale
            uiViewRoot.setLocale(currentLocale);
        }
        else
        {
            //calculate locale
            uiViewRoot.setLocale(calculateLocale(facesContext));
        }

        if (currentRenderKitId != null)
        {
            //set old locale
            uiViewRoot.setRenderKitId(currentRenderKitId);
        }
        else
        {
            //calculate locale
            uiViewRoot.setRenderKitId(calculateRenderKitId(facesContext));
        }

        if (log.isTraceEnabled()) log.trace("Created view " + viewId);
        return uiViewRoot;
    }

    public String getActionURL(FacesContext facesContext, String viewId)
    {
        String path = getViewIdPath(facesContext, viewId);
        if (path.length() > 0 && path.charAt(0) == '/')
        {
            return facesContext.getExternalContext().getRequestContextPath() + path;
        }
        else
        {
            return path;
        }
    }

    public String getResourceURL(FacesContext facesContext, String path)
    {
        if (path.length() > 0 && path.charAt(0) == '/')
        {
            return facesContext.getExternalContext().getRequestContextPath() + path;
        }
        else
        {
            return path;
        }
    }

    public void renderView(FacesContext facesContext, UIViewRoot viewToRender)
            throws IOException, FacesException
    {
        if (viewToRender == null)
        {
            log.fatal("viewToRender must not be null");
            throw new NullPointerException("viewToRender must not be null");
        }

        ExternalContext externalContext = facesContext.getExternalContext();

        String viewId = facesContext.getViewRoot().getViewId();
        ServletMapping servletMapping = getServletMapping(externalContext);
        if (servletMapping.isExtensionMapping())
        {
            String defaultSuffix = externalContext.getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
            String suffix = defaultSuffix != null ? defaultSuffix : ViewHandler.DEFAULT_SUFFIX;
            DebugUtils.assertError(suffix.charAt(0) == '.',
                                   log, "Default suffix must start with a dot!");
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
        externalContext.dispatch(viewId);
    }


    public UIViewRoot restoreView(FacesContext facesContext, String viewId)
    {
        handleCharacterEncoding(facesContext);
        Application application = facesContext.getApplication();
        ViewHandler applicationViewHandler = application.getViewHandler();
        String renderKitId = applicationViewHandler.calculateRenderKitId(facesContext);
        UIViewRoot viewRoot = application.getStateManager().restoreView(facesContext,
                                                                        viewId,
                                                                        renderKitId);
        return viewRoot;
    }

    /**
     * Writes a state marker that is replaced later by one or more hidden form
     * inputs.
     * @param facesContext
     * @throws IOException
     */
    public void writeState(FacesContext facesContext) throws IOException
    {
        if (facesContext.getApplication().getStateManager().isSavingStateInClient(facesContext))
        {
            facesContext.getResponseWriter().write(FORM_STATE_MARKER);
        }
    }

    /**
     * Find character encoding and examine Content-Type header as stated in Spec. 2.5.1.2
     * @param facesContext
     */
    private void handleCharacterEncoding(FacesContext facesContext)
    {
        ExternalContext externalContext = facesContext.getExternalContext();
        String characterEncoding = null;

        String contentType = (String)externalContext.getRequestHeaderMap().get("Content-Type");
        if (contentType != null)
        {
            int charsetFind = contentType.indexOf("charset=");
            if (charsetFind != -1)
            {
                if (charsetFind == 0)
                {
                    //charset at beginning of Content-Type, curious
                    characterEncoding = contentType.substring(8);
                }
                else
                {
                    char charBefore = contentType.charAt(charsetFind - 1);
                    if (charBefore == ';' || Character.isWhitespace(charBefore))
                    {
                        //Correct charset after mime type
                        characterEncoding = contentType.substring(charsetFind + 8);
                    }
                }
                if (log.isDebugEnabled()) log.debug("Incoming request has Content-Type header with character encoding " + characterEncoding);
            }
            else
            {
                if (log.isDebugEnabled()) log.debug("Incoming request has Content-Type header without character encoding: " + contentType);
            }
        }
        else
        {
            if (log.isDebugEnabled()) log.debug("Incoming request has no Content-Type header.");
        }

        if (characterEncoding == null)
        {
            Map sessionMap = externalContext.getSessionMap();
            if (sessionMap != null)
            {
                characterEncoding = (String)sessionMap.get(ViewHandler.CHARACTER_ENCODING_KEY);
                if (log.isDebugEnabled()) log.debug("Got character encoding from session.");
            }
        }

        if (characterEncoding != null)
        {
            Object request = externalContext.getRequest();
            if (request instanceof ServletRequest)
            {
                try
                {
                    ((ServletRequest)request).setCharacterEncoding(characterEncoding);
                }
                catch (UnsupportedEncodingException e)
                {
                    if (log.isWarnEnabled()) log.warn("Request does not support character encoding " + characterEncoding);
                }
            }
            else
            {
                log.error("Request of type " + request.getClass().getName() + " not supported by ViewHandler " + getClass().getName() + ": Could not set character encoding!");
            }
        }
    }


    protected String getViewIdPath(FacesContext facescontext, String viewId)
    {
        if (viewId == null)
        {
            log.error("ViewId must not be null");
            throw new NullPointerException("ViewId must not be null");
        }
        if (!viewId.startsWith("/"))
        {
            log.error("ViewId must start with '/' (viewId = " + viewId + ")");
            throw new IllegalArgumentException("ViewId must start with '/' (viewId = " + viewId + ")");
        }

        ExternalContext externalContext = facescontext.getExternalContext();
        ServletMapping servletMapping = getServletMapping(externalContext);

        if (servletMapping.isExtensionMapping())
        {
            // extension mapping
            String urlpattern = servletMapping.getUrlPattern();
            if (urlpattern.startsWith("*"))
            {
                urlpattern = urlpattern.substring(1, urlpattern.length());
            }
            if (viewId.endsWith(urlpattern))
            {
                return viewId;
            }
            else
            {
                int idx = viewId.lastIndexOf(".");
                if (idx >= 0)
                {
                    return viewId.substring(0, idx) + urlpattern;
                }
                else
                {
                    return viewId + urlpattern;
                }

            }
        }
        else
        {
            // prefix mapping
            String urlpattern = servletMapping.getUrlPattern();
            if (urlpattern.endsWith("/*"))
            {
                urlpattern = urlpattern.substring(0, urlpattern.length() - 2);
            }
            return urlpattern + viewId;
        }
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

}

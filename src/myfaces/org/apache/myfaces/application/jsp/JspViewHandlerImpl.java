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

package net.sourceforge.myfaces.application.jsp;

import net.sourceforge.myfaces.application.MyfacesViewHandler;
import net.sourceforge.myfaces.util.DebugUtils;
import net.sourceforge.myfaces.webapp.webxml.ServletMapping;
import net.sourceforge.myfaces.webapp.webxml.WebXml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspViewHandlerImpl
    implements ViewHandler, MyfacesViewHandler
{
    private static final Log log = LogFactory.getLog(JspViewHandlerImpl.class);
    public static final String VIEW_ROOT_TYPE        = "ViewRoot";
    public static final String FORM_STATE_MARKER     = "<!--@@JSF_FORM_STATE_MARKER@@-->";
    public static final int    FORM_STATE_MARKER_LEN = FORM_STATE_MARKER.length();
    public static final String URL_STATE_MARKER      = "JSF_URL_STATE_MARKER=DUMMY";
    public static final int    URL_STATE_MARKER_LEN  = URL_STATE_MARKER.length();

    private StateManager _stateManager;

    public JspViewHandlerImpl()
    {
        _stateManager = new JspStateManagerImpl();
        if (log.isTraceEnabled()) log.trace("New ViewHandler instance created");
    }

    /**
     * MyFaces extension: if there is a current locale, this locale will be set in
     * the newly created ViewRoot instead of calling calculateLocale.
     * @param facesContext
     * @param viewId
     * @return
     */
    public UIViewRoot createView(FacesContext facesContext, String viewId)
    {
        Locale currentLocale = null;
        UIViewRoot uiViewRoot = facesContext.getViewRoot();
        if (uiViewRoot != null)
        {
            //Remember current locale
            currentLocale = uiViewRoot.getLocale();
        }

        uiViewRoot = (UIViewRoot)facesContext.getApplication().createComponent(VIEW_ROOT_TYPE);
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

        if (log.isTraceEnabled()) log.trace("Created view " + viewId);
        return uiViewRoot;
    }

    public UIViewRoot restoreView(FacesContext facesContext, String viewId)
    {
        UIViewRoot viewRoot = getStateManager().restoreView(facesContext, viewId);
        handleCharacterEncoding(facesContext);
        return viewRoot;
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


    public StateManager getStateManager()
    {
        return _stateManager;
    }


    public String getViewIdPath(FacesContext facescontext, String viewId)
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
        externalContext.dispatchMessage(viewId);
    }


    /**
     * Writes a state marker that is replaced later by one or more hidden form
     * inputs.
     * @param facesContext
     * @throws IOException
     */
    public void writeState(FacesContext facesContext) throws IOException
    {
        if (getStateManager().isSavingStateInClient(facesContext))
        {
            facesContext.getResponseWriter().write(FORM_STATE_MARKER);
        }
    }


    /**
     * MyFaces extension: Should be called from HyperlinkRenderers to encode
     * the href attribute. If client state saving is used, we add a state marker
     * to the url.
     * @param facesContext
     * @param url
     * @return
     * @throws IOException
     */
    public String encodeURL(FacesContext facesContext, String url) throws IOException
    {
        url = facesContext.getExternalContext().encodeResourceURL(url);
        if (getStateManager().isSavingStateInClient(facesContext))
        {
            if (url.indexOf('?') == -1)
            {
                return url + '?' + URL_STATE_MARKER;
            }
            else
            {
                return url + '&' + URL_STATE_MARKER;
            }
        }
        else
        {
            return url;
        }
    }

}

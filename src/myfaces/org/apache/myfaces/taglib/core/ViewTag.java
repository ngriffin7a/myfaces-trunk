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
package net.sourceforge.myfaces.taglib.core;

import net.sourceforge.myfaces.application.MyfacesStateManager;
import net.sourceforge.myfaces.application.jsp.JspViewHandlerImpl;
import net.sourceforge.myfaces.renderkit.html.HtmlLinkRendererBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.11  2004/05/12 07:57:45  manolito
 * Log in javadoc header
 *
 */
public class ViewTag
        extends UIComponentBodyTag
{
    private static final Log log = LogFactory.getLog(ViewTag.class);

    public String getComponentType()
    {
        return UIViewRoot.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }

    private String _locale;

    public void setLocale(String locale)
    {
        _locale = locale;
    }

    public int doStartTag() throws JspException
    {
        if (log.isTraceEnabled()) log.trace("entering ViewTag.doStartTag");
        super.doStartTag();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        try
        {
            responseWriter.startDocument();
        }
        catch (IOException e)
        {
            log.error("Error writing startDocument", e);
            throw new JspException(e);
        }

        StateManager stateManager = facesContext.getApplication().getStateManager();
        if (stateManager.isSavingStateInClient(facesContext))
        {
            if (log.isTraceEnabled()) log.trace("leaving ViewTag.doStartTag");
            return BodyTag.EVAL_BODY_BUFFERED;
        }
        else
        {
            if (log.isTraceEnabled()) log.trace("leaving ViewTag.doStartTag");
            return BodyTag.EVAL_BODY_INCLUDE;
        }
    }

    protected boolean isSuppressed()
    {
        return true;
    }

    public int doEndTag() throws JspException
    {
        if (log.isTraceEnabled()) log.trace("entering ViewTag.doEndTag");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResponseWriter responseWriter = facesContext.getResponseWriter();
        try
        {
            responseWriter.endDocument();
        }
        catch (IOException e)
        {
            log.error("Error writing startDocument", e);
            throw new JspException(e);
        }

        StateManager stateManager = facesContext.getApplication().getStateManager();
        if (!stateManager.isSavingStateInClient(facesContext))
        {
            //save state in server
            stateManager.saveSerializedView(facesContext);
        }

        if (log.isTraceEnabled()) log.trace("leaving ViewTag.doEndTag");
        return super.doEndTag();
    }

    public int doAfterBody() throws JspException
    {
        if (log.isTraceEnabled()) log.trace("entering ViewTag.doAfterBody");
        try
        {
            BodyContent bodyContent = getBodyContent();
            if (bodyContent != null)
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                StateManager stateManager = facesContext.getApplication().getStateManager();
                StateManager.SerializedView serializedView
                        = stateManager.saveSerializedView(facesContext);
                if (serializedView != null)
                {
                    //until now we have written to a buffer
                    ResponseWriter bufferWriter = facesContext.getResponseWriter();
                    bufferWriter.flush();
                    //now we switch to real output
                    ResponseWriter realWriter = bufferWriter.cloneWithWriter(getPreviousOut());
                    facesContext.setResponseWriter(realWriter);

                    String bodyStr = bodyContent.getString();
                    int form_marker = bodyStr.indexOf(JspViewHandlerImpl.FORM_STATE_MARKER);
                    int url_marker = bodyStr.indexOf(HtmlLinkRendererBase.URL_STATE_MARKER);
                    int lastMarkerEnd = 0;
                    while (form_marker != -1 || url_marker != -1)
                    {
                        if (url_marker == -1 || (form_marker != -1 && form_marker < url_marker))
                        {
                            //replace form_marker
                            realWriter.write(bodyStr, lastMarkerEnd, form_marker - lastMarkerEnd);
                            stateManager.writeState(facesContext, serializedView);
                            lastMarkerEnd = form_marker + JspViewHandlerImpl.FORM_STATE_MARKER_LEN;
                            form_marker = bodyStr.indexOf(JspViewHandlerImpl.FORM_STATE_MARKER, lastMarkerEnd);
                        }
                        else
                        {
                            //replace url_marker
                            realWriter.write(bodyStr, lastMarkerEnd, url_marker - lastMarkerEnd);
                            if (stateManager instanceof MyfacesStateManager)
                            {
                                ((MyfacesStateManager)stateManager).writeStateAsUrlParams(facesContext,
                                                                                          serializedView);
                            }
                            else
                            {
                                log.error("Current StateManager is no MyfacesStateManager and does not support saving state in url parameters.");
                            }
                            lastMarkerEnd = url_marker + HtmlLinkRendererBase.URL_STATE_MARKER_LEN;
                            url_marker = bodyStr.indexOf(HtmlLinkRendererBase.URL_STATE_MARKER, lastMarkerEnd);
                        }
                    }
                    realWriter.write(bodyStr, lastMarkerEnd, bodyStr.length() - lastMarkerEnd);
                }
                else
                {
                    bodyContent.writeOut(getPreviousOut());
                }
            }
        }
        catch (IOException e)
        {
            log.error("Error writing body content", e);
            throw new JspException(e);
        }
        if (log.isTraceEnabled()) log.trace("leaving ViewTag.doAfterBody");
        return super.doAfterBody();
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        if (_locale != null)
        {
            Locale locale;
            if (UIComponentTag.isValueReference(_locale))
            {
                FacesContext context = FacesContext.getCurrentInstance();
                ValueBinding vb = context.getApplication().createValueBinding(_locale);
                Class type = vb.getType(context);
                if (Locale.class.isAssignableFrom(type))
                {
                    locale = (Locale)vb.getValue(context);
                }
                else if (String.class.isAssignableFrom(type))
                {
                    locale = string2Locale((String)vb.getValue(context));
                }
                else
                {
                    throw new IllegalArgumentException("Locale or String expected");
                }
            }
            else
            {
                locale = string2Locale(_locale);
            }
            ((UIViewRoot)component).setLocale(locale);
            Config.set((ServletRequest)getFacesContext().getExternalContext().getRequest(),
                       Config.FMT_LOCALE,
                       locale);
        }
    }


    private static Locale string2Locale(String localeString)
    {
        String language;
        String country;
        String variant;
        int underscore1 = localeString.indexOf('_');
        if (underscore1 == -1)
        {
            language = localeString;
            country = "";
            variant = "";
        }
        else
        {
            language = localeString.substring(underscore1);
            int underscore2 = localeString.indexOf('_', underscore1 + 1);
            if (underscore2 == -1)
            {
                country = localeString.substring(underscore1 + 1);
                variant = "";
            }
            else
            {
                country = localeString.substring(underscore1 + 1, underscore2);
                variant = localeString.substring(underscore2 + 1);
            }
        }
        return new Locale(language, country, variant);
    }
}

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.webapp.UIComponentBodyTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ViewTag
        extends UIComponentBodyTag
{
    private static final Log log = LogFactory.getLog(ViewTag.class);

    public String getComponentType()
    {
        return "ViewRoot";
    }

    public String getRendererType()
    {
        return null;
    }

    public void setLocale()
    {
        //TODO: see 1.0 PFD 9.4.18
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
                StateManager.SerializedView serializedView = stateManager.saveSerializedView(facesContext);
                if (serializedView != null)
                {
                    //until now we have written to a buffer
                    ResponseWriter bufferWriter = facesContext.getResponseWriter();
                    //now we switch to real output
                    ResponseWriter realWriter = bufferWriter.cloneWithWriter(getPreviousOut());
                    facesContext.setResponseWriter(realWriter);

                    String bodyStr = bodyContent.getString();
                    int form_marker = bodyStr.indexOf(JspViewHandlerImpl.FORM_STATE_MARKER);
                    int url_marker = bodyStr.indexOf(JspViewHandlerImpl.URL_STATE_MARKER);
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
                            lastMarkerEnd = url_marker + JspViewHandlerImpl.URL_STATE_MARKER_LEN;
                            url_marker = bodyStr.indexOf(JspViewHandlerImpl.URL_STATE_MARKER, lastMarkerEnd);
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
}

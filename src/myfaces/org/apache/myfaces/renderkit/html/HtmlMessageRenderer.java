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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class HtmlMessageRenderer
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlMessageRenderer.class);

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent)
            throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, UIMessage.class);
        if (!RendererUtils.isVisibleOnUserRole(facesContext, component)) return;

        renderMessage(facesContext, (UIMessage)component);
    }

    /**
     * Identical to {@link HtmlMessagesRenderer#renderSingleMessage} functionality
     * but methods cannot be combined because of different component types.
     */
    private void renderMessage(FacesContext facesContext,
                               UIMessage uiMessage)
            throws IOException
    {
        String forClientId = uiMessage.getFor();
        if (forClientId == null)
        {
            throw new NullPointerException("Attribute 'for' of HtmlMessage must not be null");
        }

        UIComponent forComponent = uiMessage.findComponent(forClientId);

        if (forComponent == null)
        {
            throw new NullPointerException("Could not render Message. Component '" + forClientId + "' not found.");
        }

        Iterator messageIterator = facesContext.getMessages(forComponent.getClientId(facesContext));
        if (!messageIterator.hasNext())
        {
            // No associated message, nothing to render
            return;
        }

        // get first message
        FacesMessage facesMessage = (FacesMessage)messageIterator.next();

        // determine style and style class
        String[] tmp;
        if (uiMessage instanceof HtmlMessage)
        {
            tmp = getStyleAndStyleClass((HtmlMessage)uiMessage, facesMessage.getSeverity());
        }
        else
        {
            tmp = getStyleAndStyleClass(uiMessage, facesMessage.getSeverity());
        }

        String style = tmp[0];
        String styleClass = tmp[1];

        String summary = facesMessage.getSummary();
        String detail = facesMessage.getDetail();

        String title;
        boolean tooltip;
        if (uiMessage instanceof HtmlMessage)
        {
            title = ((HtmlMessage)uiMessage).getTitle();
            tooltip = ((HtmlMessage)uiMessage).isTooltip();
        }
        else
        {
            Map attr = uiMessage.getAttributes();
            title = (String)attr.get(JSFAttr.TITLE_ATTR);
            tooltip = RendererUtils.getBooleanAttribute(uiMessage, JSFAttr.TOOLTIP_ATTR, false);
        }

        if (title == null && tooltip)
        {
            title = summary;
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        boolean span = false;
        //Redirect output of span element to temporary writer
        StringWriter buf = new StringWriter();
        ResponseWriter bufWriter = writer.cloneWithWriter(buf);
        bufWriter.startElement(HTML.SPAN_ELEM, uiMessage);
        //universal attributes
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, uiMessage, HTML.DIR_ATTR, HTML.DIR_ATTR);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, uiMessage, HTML.LANG_ATTR, HTML.LANG_ATTR);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, HTML.TITLE_ATTR, HTML.TITLE_ATTR, title);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, HTML.STYLE_ATTR, HTML.STYLE_ATTR, style);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, HTML.STYLE_CLASS_ATTR, HTML.STYLE_CLASS_ATTR, styleClass);
        span |= HTMLUtil.renderHTMLAttributes(bufWriter, uiMessage, HTML.EVENT_HANDLER_ATTRIBUTES);
        bufWriter.close();
        if (span)
        {
            //span attribute was written, so write out span element to real writer
            writer.write(buf.toString());
        }

        boolean showSummary = (uiMessage.isShowSummary() && summary != null);
        boolean showDetail = (uiMessage.isShowDetail() && detail != null);

        if (showSummary)
        {
            if (showDetail)
            {
                writer.writeText(summary + ": ", null);
            }
            else
            {
                writer.writeText(summary, null);
            }
        }

        if (showDetail)
        {
            writer.writeText(detail, null);
        }

        if (span)
        {
            writer.endElement(HTML.SPAN_ELEM);
        }
    }


    private String[] getStyleAndStyleClass(HtmlMessage htmlMessage,
                                           FacesMessage.Severity severity)
    {
        String style = null;
        String styleClass = null;
        if (severity == FacesMessage.SEVERITY_INFO)
        {
            style = htmlMessage.getInfoStyle();
            styleClass = htmlMessage.getInfoClass();
        }
        else if (severity == FacesMessage.SEVERITY_WARN)
        {
            style = htmlMessage.getWarnStyle();
            styleClass = htmlMessage.getWarnClass();
        }
        else if (severity == FacesMessage.SEVERITY_ERROR)
        {
            style = htmlMessage.getErrorStyle();
            styleClass = htmlMessage.getErrorClass();
        }
        else if (severity == FacesMessage.SEVERITY_FATAL)
        {
            style = htmlMessage.getFatalStyle();
            styleClass = htmlMessage.getFatalClass();
        }

        if (style == null)
        {
            style = htmlMessage.getStyle();
        }

        if (styleClass == null)
        {
            styleClass = htmlMessage.getStyleClass();
        }

        return new String[] {style, styleClass};
    }

    private String[] getStyleAndStyleClass(UIMessage uiMessage,
                                           FacesMessage.Severity severity)
    {
        Map attr = uiMessage.getAttributes();
        String style = null;
        String styleClass = null;
        if (severity == FacesMessage.SEVERITY_INFO)
        {
            style = (String)attr.get(JSFAttr.INFO_STYLE_ATTR);
            styleClass = (String)attr.get(JSFAttr.INFO_CLASS_ATTR);
        }
        else if (severity == FacesMessage.SEVERITY_WARN)
        {
            style = (String)attr.get(JSFAttr.WARN_STYLE_ATTR);
            styleClass = (String)attr.get(JSFAttr.WARN_CLASS_ATTR);
        }
        else if (severity == FacesMessage.SEVERITY_ERROR)
        {
            style = (String)attr.get(JSFAttr.ERROR_STYLE_ATTR);
            styleClass = (String)attr.get(JSFAttr.ERROR_CLASS_ATTR);
        }
        else if (severity == FacesMessage.SEVERITY_FATAL)
        {
            style = (String)attr.get(JSFAttr.FATAL_STYLE_ATTR);
            styleClass = (String)attr.get(JSFAttr.FATAL_CLASS_ATTR);
        }

        if (style == null)
        {
            style = (String)attr.get(JSFAttr.STYLE_CLASS_ATTR);
        }

        if (styleClass == null)
        {
            styleClass = (String)attr.get(JSFAttr.STYLE_CLASS_ATTR);
        }

        return new String[] {style, styleClass};
    }

}

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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/04/01 12:43:18  manolito
 * html nesting bug fixed
 *
 * Revision 1.3  2004/03/31 14:51:47  manolito
 * summaryFormat and detailFormat support
 *
 * Revision 1.2  2004/03/31 13:25:24  manolito
 * locale variable renamed
 *
 * Revision 1.1  2004/03/30 17:47:36  manolito
 * Message and Messages refactored
 *
 * Revision 1.1  2004/03/30 13:24:58  manolito
 * refactoring: HtmlComponentTag moved to share and renamed to HtmlComponentTagBase
 *
 */
public abstract class HtmlMessageRendererBase
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlMessageRendererBase.class);

    protected abstract String getSummary(FacesContext facesContext,
                                         UIComponent message,
                                         FacesMessage facesMessage,
                                         String msgClientId);

    protected abstract String getDetail(FacesContext facesContext,
                                        UIComponent message,
                                        FacesMessage facesMessage,
                                        String msgClientId);

    protected void renderMessage(FacesContext facesContext,
                                 UIComponent message)
            throws IOException
    {
        String forAttr = getFor(message);
        if (forAttr == null)
        {
            throw new FacesException("Attribute 'for' of UIMessage must not be null");
        }

        UIComponent forComponent = message.findComponent(forAttr);
        if (forComponent == null)
        {
            throw new FacesException("Could not render Message. Component '" + forAttr + "' not found.");
        }

        Iterator messageIterator = facesContext.getMessages(forComponent.getClientId(facesContext));
        if (!messageIterator.hasNext())
        {
            // No associated message, nothing to render
            return;
        }

        // get first message
        FacesMessage facesMessage = (FacesMessage)messageIterator.next();

        // and render it
        renderSingleFacesMessage(facesContext, message, facesMessage, null);
    }



    protected void renderSingleFacesMessage(FacesContext facesContext,
                                            UIComponent message,
                                            FacesMessage facesMessage,
                                            String messageClientId)
            throws IOException
    {
        // determine style and style class
        String[] styleAndClass = getStyleAndStyleClass(message, facesMessage.getSeverity());
        String style = styleAndClass[0];
        String styleClass = styleAndClass[1];

        String summary = getSummary(facesContext, message, facesMessage, messageClientId);
        String detail = getDetail(facesContext, message, facesMessage, messageClientId);

        String title = getTitle(message);
        boolean tooltip = isTooltip(message);

        if (title == null && tooltip)
        {
            title = summary;
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        boolean span = HtmlRendererUtils.renderHTMLAttributesWithOptionalStartElement(
                            writer, message, HTML.SPAN_ELEM, HTML.MESSAGE_PASSTHROUGH_ATTRIBUTES);
        span |= HtmlRendererUtils.renderHTMLAttributeWithOptionalStartElement(writer, message, HTML.SPAN_ELEM, HTML.TITLE_ATTR, title, span);
        span |= HtmlRendererUtils.renderHTMLAttributeWithOptionalStartElement(writer, message, HTML.SPAN_ELEM, HTML.STYLE_ATTR, style, span);
        span |= HtmlRendererUtils.renderHTMLAttributeWithOptionalStartElement(writer, message, HTML.SPAN_ELEM, HTML.STYLE_CLASS_ATTR, styleClass, span);

        boolean showSummary = isShowSummary(message) && (summary != null);
        boolean showDetail = isShowDetail(message) && (detail != null);

        if (showSummary && !(title == null && tooltip))
        {
            writer.writeText(summary, null);
            if (showDetail)
            {
                writer.writeText(" ", null);
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


    protected String[] getStyleAndStyleClass(UIComponent message,
                                             FacesMessage.Severity severity)
    {
        String style = null;
        String styleClass = null;
        if (message instanceof HtmlMessage)
        {
            if (severity == FacesMessage.SEVERITY_INFO)
            {
                style = ((HtmlMessage)message).getInfoStyle();
                styleClass = ((HtmlMessage)message).getInfoClass();
            }
            else if (severity == FacesMessage.SEVERITY_WARN)
            {
                style = ((HtmlMessage)message).getWarnStyle();
                styleClass = ((HtmlMessage)message).getWarnClass();
            }
            else if (severity == FacesMessage.SEVERITY_ERROR)
            {
                style = ((HtmlMessage)message).getErrorStyle();
                styleClass = ((HtmlMessage)message).getErrorClass();
            }
            else if (severity == FacesMessage.SEVERITY_FATAL)
            {
                style = ((HtmlMessage)message).getFatalStyle();
                styleClass = ((HtmlMessage)message).getFatalClass();
            }

            if (style == null)
            {
                style = ((HtmlMessage)message).getStyle();
            }

            if (styleClass == null)
            {
                styleClass = ((HtmlMessage)message).getStyleClass();
            }
        }
        else
        {
            Map attr = message.getAttributes();
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
        }

        return new String[] {style, styleClass};
    }

    protected String getFor(UIComponent component)
    {
        if (component instanceof UIMessage)
        {
            return ((UIMessage)component).getFor();
        }
        else
        {
            return (String)component.getAttributes().get(JSFAttr.FOR_ATTR);
        }
    }

    protected String getTitle(UIComponent component)
    {
        if (component instanceof HtmlMessage)
        {
            return ((HtmlMessage)component).getTitle();
        }
        else
        {
            return (String)component.getAttributes().get(JSFAttr.TITLE_ATTR);
        }
    }

    protected boolean isTooltip(UIComponent component)
    {
        if (component instanceof HtmlMessage)
        {
            return ((HtmlMessage)component).isTooltip();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(component, JSFAttr.TOOLTIP_ATTR, false);
        }
    }

    protected boolean isShowSummary(UIComponent component)
    {
        if (component instanceof UIMessage)
        {
            return ((UIMessage)component).isShowSummary();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(component, JSFAttr.SHOW_SUMMARY_ATTR, false);
        }
    }

    protected boolean isShowDetail(UIComponent component)
    {
        if (component instanceof UIMessage)
        {
            return ((UIMessage)component).isShowDetail();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(component, JSFAttr.SHOW_DETAIL_ATTR, false);
        }
    }












}

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
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/03/30 13:24:58  manolito
 * refactoring: HtmlComponentTag moved to share and renamed to HtmlComponentTagBase
 *
 */
public class HtmlMessageRendererUtils
{
    //private static final Log log = LogFactory.getLog(HtmlMessageRendererUtils.class);

    private HtmlMessageRendererUtils() {} //no instance allowed


    /**
     * @param facesContext
     * @param message
     * @param colonSeparator  if true, a colon will be rendered between summary and detail
     * @throws IOException
     */
    public static void renderMessage(FacesContext facesContext,
                                     UIComponent message,
                                     boolean colonSeparator)
            throws IOException
    {
        String forClientId = getFor(message);
        if (forClientId == null)
        {
            throw new FacesException("Attribute 'for' of UIMessage must not be null");
        }

        UIComponent forComponent = message.findComponent(forClientId);
        if (forComponent == null)
        {
            throw new FacesException("Could not render Message. Component '" + forClientId + "' not found.");
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
        if (message instanceof HtmlMessage)
        {
            tmp = getStyleAndStyleClass((HtmlMessage)message, facesMessage.getSeverity());
        }
        else
        {

            tmp = getStyleAndStyleClass(message, facesMessage.getSeverity());
        }

        String style = tmp[0];
        String styleClass = tmp[1];

        String summary = facesMessage.getSummary();
        String detail = facesMessage.getDetail();

        String title;
        boolean tooltip;
        if (message instanceof HtmlMessage)
        {
            title = ((HtmlMessage)message).getTitle();
            tooltip = ((HtmlMessage)message).isTooltip();
        }
        else
        {
            Map attr = message.getAttributes();
            title = (String)attr.get(JSFAttr.TITLE_ATTR);
            tooltip = RendererUtils.getBooleanAttribute(message, JSFAttr.TOOLTIP_ATTR, false);
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
        bufWriter.startElement(HTML.SPAN_ELEM, message);
        //universal attributes
        span |= HtmlRendererUtils.renderHTMLAttribute(bufWriter, message, HTML.DIR_ATTR, HTML.DIR_ATTR);
        span |= HtmlRendererUtils.renderHTMLAttribute(bufWriter, message, HTML.LANG_ATTR, HTML.LANG_ATTR);
        span |= HtmlRendererUtils.renderHTMLAttribute(bufWriter, HTML.TITLE_ATTR, HTML.TITLE_ATTR, title);
        span |= HtmlRendererUtils.renderHTMLAttribute(bufWriter, HTML.STYLE_ATTR, HTML.STYLE_ATTR, style);
        span |= HtmlRendererUtils.renderHTMLAttribute(bufWriter, HTML.STYLE_CLASS_ATTR, HTML.STYLE_CLASS_ATTR, styleClass);
        span |= HtmlRendererUtils.renderHTMLAttributes(bufWriter, message, HTML.EVENT_HANDLER_ATTRIBUTES);
        bufWriter.flush();
        bufWriter.close();
        if (span)
        {
            //span attribute was written, so write out span element to real writer
            writer.write(buf.toString());
        }

        boolean showSummary = isShowSummary(message) && summary != null;
        boolean showDetail = isShowDetail(message) && detail != null;

        if (showSummary && !(title == null && tooltip))
        {
            writer.writeText(summary, null);
            if (showDetail && colonSeparator)
            {
                writer.writeText(": ", null);
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

    private static String[] getStyleAndStyleClass(HtmlMessage htmlMessage,
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

    private static String[] getStyleAndStyleClass(UIComponent message,
                                                  FacesMessage.Severity severity)
    {
        Map attr = message.getAttributes();
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

    private static String getFor(UIComponent component)
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

    private static boolean isShowSummary(UIComponent component)
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

    private static boolean isShowDetail(UIComponent component)
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

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

import net.sourceforge.myfaces.component.html.MyFacesHtmlMessages;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlMessagesRenderer
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlMessagesRenderer.class);

    private static final String LAYOUT_LIST  = "list";
    private static final String LAYOUT_TABLE = "table";

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
        RendererUtils.checkParamValidity(facesContext, component, HtmlMessages.class);
        if (!RendererUtils.isVisibleOnUserRole(facesContext, component)) return;

        HtmlMessages htmlMessages = (HtmlMessages)component;

        Iterator messageIterator;

        String forClientId = htmlMessages.getFor();
        if (forClientId == null)
        {
            if (htmlMessages.isGlobalOnly())
            {
                messageIterator = facesContext.getMessages(null);
            }
            else
            {
                messageIterator = facesContext.getMessages();
            }
        }
        else
        {
            messageIterator = facesContext.getMessages(forClientId);
        }

        if (messageIterator.hasNext())
        {
            String layout = htmlMessages.getLayout();
            if (layout.equalsIgnoreCase(LAYOUT_TABLE))
            {
                renderTable(facesContext, htmlMessages, messageIterator);
            }
            else
            {
                if (log.isWarnEnabled() && !layout.equalsIgnoreCase(LAYOUT_LIST))
                {
                    log.warn("Unsupported messages layout '" + layout + "' - using default layout 'list'.");
                }
                renderList(facesContext, htmlMessages, messageIterator);
            }
        }
    }


    protected void renderList(FacesContext facesContext,
                              HtmlMessages htmlMessages,
                              Iterator messagesIterator)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement("ul", htmlMessages);

        while(messagesIterator.hasNext())
        {
            writer.startElement("li", htmlMessages);
            renderSingleMessage(writer, htmlMessages, (FacesMessage)messagesIterator.next());
            writer.endElement("li");
        }

        writer.endElement("ul");
    }


    protected void renderTable(FacesContext facesContext,
                               HtmlMessages htmlMessages,
                               Iterator messagesIterator)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement("table", htmlMessages);

        while(messagesIterator.hasNext())
        {
            writer.startElement("tr", htmlMessages);
            writer.startElement("td", htmlMessages);
            renderSingleMessage(writer, htmlMessages, (FacesMessage)messagesIterator.next());
            writer.endElement("td");
            writer.endElement("tr");
        }

        writer.endElement("table");
    }


    /**
     * Identical to {@link HtmlMessageRenderer#renderMessage} functionality
     * but methods cannot be combined because of different component types.
     */
    private void renderSingleMessage(ResponseWriter writer,
                                     HtmlMessages htmlMessages,
                                     FacesMessage facesMessage)
            throws IOException
    {
        // determine style and style class
        String[] tmp = getStyleAndStyleClass(htmlMessages, facesMessage.getSeverity());
        String style = tmp[0];
        String styleClass = tmp[1];

        String summary = facesMessage.getSummary();
        String detail = facesMessage.getDetail();

        // tooltip property is missing in HtmlMessages API
        boolean tooltip;
        if (htmlMessages instanceof MyFacesHtmlMessages)
        {
            tooltip = ((MyFacesHtmlMessages)htmlMessages).isTooltip();
        }
        else
        {
            Boolean b = (Boolean)htmlMessages.getAttributes().get(JSFAttr.TOOLTIP_ATTR);
            tooltip = (b == null || b.booleanValue());
        }

        String title = htmlMessages.getTitle();
        if (title == null && tooltip)
        {
            title = summary;
        }

        boolean span = false;
        //Redirect output of span element to temporary writer
        StringWriter buf = new StringWriter();
        ResponseWriter bufWriter = writer.cloneWithWriter(buf);
        bufWriter.startElement(HTML.SPAN_ELEM, htmlMessages);
        //universal attributes
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, htmlMessages, HTML.DIR_ATTR, HTML.DIR_ATTR);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, htmlMessages, HTML.LANG_ATTR, HTML.LANG_ATTR);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, HTML.TITLE_ATTR, HTML.TITLE_ATTR, title);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, HTML.STYLE_ATTR, HTML.STYLE_ATTR, style);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, HTML.STYLE_CLASS_ATTR, HTML.STYLE_CLASS_ATTR, styleClass);
        span |= HTMLUtil.renderHTMLAttributes(bufWriter, htmlMessages, HTML.EVENT_HANDLER_ATTRIBUTES);
        bufWriter.close();
        if (span)
        {
            //span attribute was written, so write out span element to real writer
            writer.write(buf.toString());
        }

        boolean showSummary = (htmlMessages.isShowSummary() && summary != null);
        boolean showDetail = (htmlMessages.isShowDetail() && detail != null);

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


    private String[] getStyleAndStyleClass(HtmlMessages htmlMessages,
                                           FacesMessage.Severity severity)
    {
        String style = null;
        String styleClass = null;
        if (severity == FacesMessage.SEVERITY_INFO)
        {
            style = htmlMessages.getInfoStyle();
            styleClass = htmlMessages.getInfoClass();
        }
        else if (severity == FacesMessage.SEVERITY_WARN)
        {
            style = htmlMessages.getWarnStyle();
            styleClass = htmlMessages.getWarnClass();
        }
        else if (severity == FacesMessage.SEVERITY_ERROR)
        {
            style = htmlMessages.getErrorStyle();
            styleClass = htmlMessages.getErrorClass();
        }
        else if (severity == FacesMessage.SEVERITY_FATAL)
        {
            style = htmlMessages.getFatalStyle();
            styleClass = htmlMessages.getFatalClass();
        }

        if (style == null)
        {
            style = htmlMessages.getStyle();
        }

        if (styleClass == null)
        {
            styleClass = htmlMessages.getStyleClass();
        }

        return new String[] {style, styleClass};
    }


}

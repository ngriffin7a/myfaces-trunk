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

import net.sourceforge.myfaces.application.MessageUtils;
import net.sourceforge.myfaces.component.html.MyFacesHtmlMessages;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.NullIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessages;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlMessagesRenderer
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlMessagesRenderer.class);

    public static final String LAYOUT_LIST  = "list";
    public static final String LAYOUT_TABLE = "table";

    private static final String OUTPUT_LABEL_MAP = HtmlMessagesRenderer.class.getName() + ".OUTPUT_LABEL_MAP";
    private static final String IN_FIELD_MSGID = "net.sourceforge.myfaces.renderkit.html.HtmlMessagesRenderer.IN_FIELD";

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

        MessageIterator messageIterator;

        messageIterator = new MessageIterator(facesContext, htmlMessages.isGlobalOnly());

        if (messageIterator.hasNext())
        {
            String layout = htmlMessages.getLayout();
            if (layout == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No messages layout given, using default layout 'list'.");
                }
                renderList(facesContext, htmlMessages, messageIterator);
            }
            else if (layout.equalsIgnoreCase(LAYOUT_TABLE))
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
                              MessageIterator messagesIterator)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement("ul", htmlMessages);

        while(messagesIterator.hasNext())
        {
            writer.startElement("li", htmlMessages);
            renderSingleMessage(facesContext,
                                writer,
                                htmlMessages,
                                (FacesMessage)messagesIterator.next(),
                                messagesIterator.getClientId());
            writer.endElement("li");
        }

        writer.endElement("ul");
    }


    protected void renderTable(FacesContext facesContext,
                               HtmlMessages htmlMessages,
                               MessageIterator messagesIterator)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement("table", htmlMessages);

        while(messagesIterator.hasNext())
        {
            writer.startElement("tr", htmlMessages);
            writer.startElement("td", htmlMessages);
            renderSingleMessage(facesContext,
                                writer,
                                htmlMessages,
                                (FacesMessage)messagesIterator.next(),
                                messagesIterator.getClientId());
            writer.endElement("td");
            writer.endElement("tr");
        }

        writer.endElement("table");
    }


    /**
     * Identical to {@link HtmlMessageRenderer#renderMessage} functionality
     * but methods cannot be combined because of different component types.
     */
    private void renderSingleMessage(FacesContext facesContext,
                                     ResponseWriter writer,
                                     HtmlMessages htmlMessages,
                                     FacesMessage facesMessage,
                                     String forClientId)
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
            String inputLabel = null;
            if (forClientId != null &&
                htmlMessages instanceof MyFacesHtmlMessages &&
                ((MyFacesHtmlMessages)htmlMessages).isShowInputLabel())
            {
                inputLabel = findInputLabel(facesContext, forClientId);
            }

            writer.writeText(summary, null);

            if (inputLabel != null)
            {
                FacesMessage fm = MessageUtils.getMessage(FacesMessage.SEVERITY_INFO,
                                                          IN_FIELD_MSGID,
                                                          inputLabel);
                writer.writeText(fm.getSummary(), null);
            }
        }

        if (showDetail)
        {
            if (showSummary)
            {
                writer.write(": ");
            }

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


    private String findInputLabel(FacesContext facesContext, String inputClientId)
    {
        Map outputLabelMap = getOutputLabelMap(facesContext);
        HtmlOutputLabel outputLabel = (HtmlOutputLabel)outputLabelMap.get(inputClientId);
        String text = RendererUtils.getStringValue(facesContext, outputLabel);
        if (text == null)
        {
            //TODO: traverse children and append OutputText and/or OutputMessage texts
        }
        return text;
    }


    /**
     * @param facesContext
     * @return a Map that reversely maps clientIds of components to their
     *         corresponding OutputLabel component
     */
    private Map getOutputLabelMap(FacesContext facesContext)
    {
        Map map = (Map)facesContext.getExternalContext().getRequestMap().get(OUTPUT_LABEL_MAP);
        if (map == null)
        {
            map = new HashMap();
            createOutputLabelMap(facesContext.getViewRoot(), map);
            facesContext.getExternalContext().getRequestMap().put(OUTPUT_LABEL_MAP, map);
        }
        return map;
    }


    private void createOutputLabelMap(UIComponent root, Map map)
    {
        if (root.getChildCount() > 0)
        {
            for (Iterator it = root.getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                if (child instanceof HtmlOutputLabel)
                {
                    map.put(((HtmlOutputLabel)child).getFor(), child);
                }
                else
                {
                    createOutputLabelMap(child, map);
                }
            }
        }
    }



    private static class MessageIterator implements Iterator
    {
        private FacesContext _facesContext;
        private Iterator _globalMessagesIterator;
        private Iterator _clientIdsWithMessagesIterator;
        private Iterator _componentMessagesIterator = null;
        private String _clientId = null;

        public MessageIterator(FacesContext facesContext, boolean globalOnly)
        {
            _facesContext = facesContext;
            _globalMessagesIterator = facesContext.getMessages(null);
            if (globalOnly)
            {
                _clientIdsWithMessagesIterator = NullIterator.instance();
            }
            else
            {
                _clientIdsWithMessagesIterator = facesContext.getClientIdsWithMessages();
            }
            _componentMessagesIterator = null;
            _clientId = null;
        }

        public boolean hasNext()
        {
            return _globalMessagesIterator.hasNext() ||
                   _clientIdsWithMessagesIterator.hasNext() ||
                   (_componentMessagesIterator != null && _componentMessagesIterator.hasNext());
        }

        public Object next()
        {
            if (_globalMessagesIterator.hasNext())
            {
                return _globalMessagesIterator.next();
            }
            else if (_componentMessagesIterator != null && _componentMessagesIterator.hasNext())
            {
                return _componentMessagesIterator.next();
            }
            else
            {
                _clientId = (String)_clientIdsWithMessagesIterator.next();
                _componentMessagesIterator = _facesContext.getMessages(_clientId);
                return _componentMessagesIterator.next();
            }
        }

        public void remove()
        {
            throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
        }

        public String getClientId()
        {
            return _clientId;
        }
    }

}

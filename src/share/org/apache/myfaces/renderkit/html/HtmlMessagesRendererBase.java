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
import net.sourceforge.myfaces.util.NullIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.component.html.HtmlMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/03/30 17:47:36  manolito
 * Message and Messages refactored
 *
 */
public abstract class HtmlMessagesRendererBase
        extends HtmlMessageRendererBase
{
    private static final Log log = LogFactory.getLog(HtmlMessagesRendererBase.class);

    protected static final String LAYOUT_LIST  = "list";
    protected static final String LAYOUT_TABLE = "table";


    protected void renderMessages(FacesContext facesContext,
                                  UIComponent messages)
            throws IOException
    {
        MessagesIterator messagesIterator = new MessagesIterator(facesContext,
                                                                 isGlobalOnly(messages));

        if (messagesIterator.hasNext())
        {
            String layout = getLayout(messages);
            if (layout == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No messages layout given, using default layout 'list'.");
                }
                renderList(facesContext, messages, messagesIterator);
            }
            else if (layout.equalsIgnoreCase(LAYOUT_TABLE))
            {
                renderTable(facesContext, messages, messagesIterator);
            }
            else
            {
                if (log.isWarnEnabled() && !layout.equalsIgnoreCase(LAYOUT_LIST))
                {
                    log.warn("Unsupported messages layout '" + layout + "' - using default layout 'list'.");
                }
                renderList(facesContext, messages, messagesIterator);
            }
        }
    }


    private void renderList(FacesContext facesContext,
                            UIComponent messages,
                            MessagesIterator messagesIterator)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.UL_ELEM, messages);

        while(messagesIterator.hasNext())
        {
            writer.startElement(HTML.LI_ELEM, messages);
            renderSingleFacesMessage(facesContext,
                                     messages,
                                     (FacesMessage)messagesIterator.next(),
                                     messagesIterator.getClientId());
            writer.endElement(HTML.LI_ELEM);
        }

        writer.endElement(HTML.UL_ELEM);
    }


    private void renderTable(FacesContext facesContext,
                             UIComponent messages,
                             MessagesIterator messagesIterator)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, messages);

        while(messagesIterator.hasNext())
        {
            writer.startElement(HTML.TR_ELEM, messages);
            writer.startElement(HTML.TD_ELEM, messages);
            renderSingleFacesMessage(facesContext,
                                     messages,
                                     (FacesMessage)messagesIterator.next(),
                                     messagesIterator.getClientId());
            writer.endElement(HTML.TD_ELEM);
            writer.endElement(HTML.TR_ELEM);
        }

        writer.endElement(HTML.TABLE_ELEM);
    }


    protected String[] getStyleAndStyleClass(UIComponent messages,
                                             FacesMessage.Severity severity)
    {
        String style = null;
        String styleClass = null;
        if (messages instanceof HtmlMessages)
        {
            if (severity == FacesMessage.SEVERITY_INFO)
            {
                style = ((HtmlMessages)messages).getInfoStyle();
                styleClass = ((HtmlMessages)messages).getInfoClass();
            }
            else if (severity == FacesMessage.SEVERITY_WARN)
            {
                style = ((HtmlMessages)messages).getWarnStyle();
                styleClass = ((HtmlMessages)messages).getWarnClass();
            }
            else if (severity == FacesMessage.SEVERITY_ERROR)
            {
                style = ((HtmlMessages)messages).getErrorStyle();
                styleClass = ((HtmlMessages)messages).getErrorClass();
            }
            else if (severity == FacesMessage.SEVERITY_FATAL)
            {
                style = ((HtmlMessages)messages).getFatalStyle();
                styleClass = ((HtmlMessages)messages).getFatalClass();
            }

            if (style == null)
            {
                style = ((HtmlMessages)messages).getStyle();
            }

            if (styleClass == null)
            {
                styleClass = ((HtmlMessages)messages).getStyleClass();
            }
        }
        else
        {
            Map attr = messages.getAttributes();
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


    protected String getTitle(UIComponent component)
    {
        if (component instanceof HtmlMessages)
        {
            return ((HtmlMessages)component).getTitle();
        }
        else
        {
            return (String)component.getAttributes().get(JSFAttr.TITLE_ATTR);
        }
    }

    protected boolean isTooltip(UIComponent component)
    {
        if (component instanceof HtmlMessages)
        {
            return ((HtmlMessages)component).isTooltip();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(component, JSFAttr.TOOLTIP_ATTR, false);
        }
    }

    protected boolean isShowSummary(UIComponent component)
    {
        if (component instanceof UIMessages)
        {
            return ((UIMessages)component).isShowSummary();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(component, JSFAttr.SHOW_SUMMARY_ATTR, false);
        }
    }

    protected boolean isShowDetail(UIComponent component)
    {
        if (component instanceof UIMessages)
        {
            return ((UIMessages)component).isShowDetail();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(component, JSFAttr.SHOW_DETAIL_ATTR, false);
        }
    }

    protected boolean isGlobalOnly(UIComponent component)
    {
        if (component instanceof UIMessages)
        {
            return ((UIMessages)component).isGlobalOnly();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(component, JSFAttr.GLOBAL_ONLY_ATTR, false);
        }
    }

    protected String getLayout(UIComponent component)
    {
        if (component instanceof HtmlMessages)
        {
            return ((HtmlMessages)component).getLayout();
        }
        else
        {
            return (String)component.getAttributes().get(JSFAttr.LAYOUT_ATTR);
        }
    }



    private static class MessagesIterator implements Iterator
    {
        private FacesContext _facesContext;
        private Iterator _globalMessagesIterator;
        private Iterator _clientIdsWithMessagesIterator;
        private Iterator _componentMessagesIterator = null;
        private String _clientId = null;

        public MessagesIterator(FacesContext facesContext, boolean globalOnly)
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

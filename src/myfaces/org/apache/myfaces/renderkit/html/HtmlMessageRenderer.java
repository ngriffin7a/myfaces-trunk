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
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputFormat;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlMessageRenderer
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlMessageRenderer.class);

    private static final Object[] EMPTY_ARGS = new Object[0];

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
        RendererUtils.checkParamValidity(facesContext, component, null);
        if (!RendererUtils.isVisibleOnUserRole(facesContext, component)) return;

        String text;
        boolean escape;

        if (component instanceof HtmlOutputFormat)
        {
            HtmlOutputFormat htmlOutputMessage = (HtmlOutputFormat)component;
            text = getOutputMessageText(facesContext, htmlOutputMessage);
            escape = ((HtmlOutputFormat)component).isEscape();
            HtmlTextRenderer.renderOutputText(facesContext, component, text, escape);
        }
        else if (component instanceof HtmlMessage)
        {
            renderMessage(facesContext, (HtmlMessage)component);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component type " + component.getClass().getName());
        }
    }



    private String getOutputMessageText(FacesContext facesContext,
                                        HtmlOutputFormat htmlOutputMessage)
    {
        String pattern = RendererUtils.getStringValue(facesContext, htmlOutputMessage);
        Object[] args;
        if (htmlOutputMessage.getChildCount() == 0)
        {
            args = EMPTY_ARGS;
        }
        else
        {
            List argsList = new ArrayList();
            for (Iterator it = htmlOutputMessage.getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                if (child instanceof UIParameter)
                {
                    argsList.add(((UIParameter)child).getValue());
                }
            }
            args = argsList.toArray(new Object[argsList.size()]);
        }

        MessageFormat format = new MessageFormat(pattern, facesContext.getViewRoot().getLocale());
        try
        {
            return format.format(args);
        }
        catch (Exception e)
        {
            log.error("Error formatting message of component " + htmlOutputMessage.getClientId(facesContext));
            return "";
        }
    }


    /**
     * Identical to {@link HtmlMessagesRenderer#renderSingleMessage} functionality
     * but methods cannot be combined because of different component types.
     */
    private void renderMessage(FacesContext facesContext,
                               HtmlMessage htmlMessage)
            throws IOException
    {
        String forClientId = htmlMessage.getFor();
        if (forClientId == null)
        {
            throw new NullPointerException("Attribute 'for' of HtmlMessage must not be null");
        }

        Iterator messageIterator = facesContext.getMessages(forClientId);
        if (!messageIterator.hasNext())
        {
            // No associated message, nothing to render
            return;
        }

        // get first message
        FacesMessage facesMessage = (FacesMessage)messageIterator.next();

        // determine style and style class
        String[] tmp = getStyleAndStyleClass(htmlMessage, facesMessage.getSeverity());
        String style = tmp[0];
        String styleClass = tmp[1];

        String summary = facesMessage.getSummary();
        String detail = facesMessage.getDetail();

        String title = htmlMessage.getTitle();
        if (title == null && htmlMessage.isTooltip())
        {
            title = summary;
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        boolean span = false;
        //Redirect output of span element to temporary writer
        StringWriter buf = new StringWriter();
        ResponseWriter bufWriter = writer.cloneWithWriter(buf);
        bufWriter.startElement(HTML.SPAN_ELEM, htmlMessage);
        //universal attributes
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, htmlMessage, HTML.DIR_ATTR, HTML.DIR_ATTR);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, htmlMessage, HTML.LANG_ATTR, HTML.LANG_ATTR);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, HTML.TITLE_ATTR, HTML.TITLE_ATTR, title);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, HTML.STYLE_ATTR, HTML.STYLE_ATTR, style);
        span |= HTMLUtil.renderHTMLAttribute(bufWriter, HTML.STYLE_CLASS_ATTR, HTML.STYLE_CLASS_ATTR, styleClass);
        span |= HTMLUtil.renderHTMLAttributes(bufWriter, htmlMessage, HTML.EVENT_HANDLER_ATTRIBUTES);
        bufWriter.close();
        if (span)
        {
            //span attribute was written, so write out span element to real writer
            writer.write(buf.toString());
        }

        boolean showSummary = (htmlMessage.isShowSummary() && summary != null);
        boolean showDetail = (htmlMessage.isShowDetail() && detail != null);

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

    
}

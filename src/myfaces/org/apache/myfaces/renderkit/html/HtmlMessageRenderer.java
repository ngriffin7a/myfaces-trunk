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

import net.sourceforge.myfaces.component.html.MyFacesHtmlOutputMessage;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlMessageRenderer
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
        RendererUtils.checkParamValidity(facesContext, component, null);

        if (RendererUtils.isVisibleOnUserRole(facesContext, component))
        {
            if (component instanceof HtmlMessage)
            {
                renderMessage(facesContext, (HtmlMessage)component);
            }
            else if (component instanceof HtmlOutputMessage)
            {
                renderOutputMessage(facesContext, (HtmlOutputMessage)component);
            }
            else
            {
                throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
            }
        }

        /*
        ResponseWriter writer = facesContext.getResponseWriter();

        /* FIXME: see HtmlTextRenderer
        StringBuffer   buf = new StringBuffer();
        HTMLUtil.renderStyleClass(buf, component);
        HTMLUtil.renderHTMLAttributes(buf, component, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(buf, component, HTML.EVENT_HANDLER_ATTRIBUTES);

        if (buf.length() > 0)
        {
            writer.write("<span ");
            writer.write(buf.toString());
            writer.write(">");
        }
        */

        String pattern;
        String key = (String) component.getAttributes().get(JSFAttr.KEY_ATTR);

        if (key != null)
        {
            pattern =
                BundleUtils.getString(
                    facesContext, (String) component.getAttributes().get(JSFAttr.BUNDLE_ATTR), key);
        }
        else
        {
            pattern = getStringValue(facesContext, (UIOutput) component);
        }

        //FIXME
        //Locale locale = facesContext.getLocale;
        Locale locale = null;
        MessageFormat format   = new MessageFormat(pattern, locale);

        //nested parameters
        List     params   = null;
        //FIXME
        //Iterator children = component.getChildren();
        Iterator children = null;

        while (children.hasNext())
        {
            UIComponent child = (UIComponent) children.next();

            if (child instanceof UIParameter)
            {
                if (params == null)
                {
                    params = new ArrayList();
                }

                //FIXME
                //params.add(((UIParameter) child).currentValue(facesContext));
            }
        }

        String text;

        try
        {
            if (params == null)
            {
                text = format.format(EMPTY_PARAMS);
            }
            else
            {
                text = format.format(params.toArray());
            }
        }
        catch (Exception e)
        {
            log.error("Error formatting message", e);
            text = pattern;
        }

        writer.write(HTMLEncoder.encode(text, true, true));

        /* FIXME
        if (buf.length() > 0)
        {
            writer.write("</span>");
        }
        */
    }



    protected void renderOutputMessage(FacesContext facesContext,
                                       HtmlOutputMessage htmlOutputMessage)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        boolean span = false;
        //Redirect output of span element to temporary writer
        StringWriter buf = new StringWriter();
        ResponseWriter bufWriter = writer.cloneWithWriter(buf);
        bufWriter.startElement(HTML.SPAN_ELEM, htmlOutputMessage);
        span |= HTMLUtil.renderHTMLAttributes(bufWriter, htmlOutputMessage, HTML.UNIVERSAL_ATTRIBUTES);
        span |= HTMLUtil.renderHTMLAttributes(bufWriter, htmlOutputMessage, HTML.EVENT_HANDLER_ATTRIBUTES);
        bufWriter.close();
        if (span)
        {
            //span attribute was written, so write out span element to real writer
            writer.write(buf.toString());
        }

        String text = RendererUtils.getStringValue(facesContext, htmlOutputMessage);

        boolean escape;
        if (htmlOutputMessage instanceof MyFacesHtmlOutputMessage)
        {
            escape = (((MyFacesHtmlOutputMessage)htmlOutputMessage).isEscape());
        }
        else
        {
            Boolean b = (Boolean)htmlOutputMessage.getAttributes().get(JSFAttr.ESCAPE_ATTR);
            escape = (b == null || b.booleanValue());
        }
        if (escape)
        {
            writer.writeText(text, JSFAttr.VALUE_ATTR);
        }
        else
        {
            writer.write(text);
        }

        if (span)
        {
            writer.endElement(HTML.SPAN_ELEM);
        }
    }


    private String getMessageText()
    {

    }


}

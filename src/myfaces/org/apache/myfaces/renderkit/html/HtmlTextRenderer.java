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
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTextRenderer
        extends HTMLRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlTextRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        if (RendererUtils.isVisibleOnUserRole(facesContext, uiComponent))
        {
            if (uiComponent instanceof HtmlOutputText)
            {
                renderOutput(facesContext, (HtmlOutputText)uiComponent);
            }
            else if (uiComponent instanceof HtmlInputText)
            {
                renderInput(facesContext, (HtmlInputText)uiComponent);
            }
            else
            {
                throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
            }
        }
    }


    public void renderOutput(FacesContext facesContext, HtmlOutputText htmlOutput)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        boolean span = false;
        //Redirect output of span element to temporary writer
        StringWriter buf = new StringWriter();
        ResponseWriter bufWriter = writer.cloneWithWriter(buf);
        bufWriter.startElement(HTML.SPAN_ELEM, htmlOutput);
        span |= HTMLUtil.renderStyleClass(bufWriter, htmlOutput);
        span |= HTMLUtil.renderHTMLAttributes(bufWriter, htmlOutput, HTML.UNIVERSAL_ATTRIBUTES);
        span |= HTMLUtil.renderHTMLAttributes(bufWriter, htmlOutput, HTML.EVENT_HANDLER_ATTRIBUTES);
        bufWriter.close();
        if (span)
        {
            //span attribute was written, so write out span element to real writer
            writer.write(buf.toString());
        }

        String text = RendererUtils.getStringValue(facesContext, htmlOutput);
        if (htmlOutput.isEscape())
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


    public void renderInput(FacesContext facesContext, HtmlInputText htmlInput)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        String clientId = htmlInput.getClientId(facesContext);
        String value = RendererUtils.getStringValue(facesContext, htmlInput);

        writer.startElement(HTML.INPUT_ELEM, htmlInput);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        if (value != null)
        {
            writer.writeAttribute(HTML.VALUE_ATTR, value, JSFAttr.VALUE_ATTR);
        }

        HTMLUtil.renderStyleClass(writer, htmlInput);
        HTMLUtil.renderHTMLAttributes(writer, htmlInput, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, htmlInput, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, htmlInput, HTML.INPUT_ATTRIBUTES);
        HTMLUtil.renderHTMLAttribute(writer, htmlInput, JSFAttr.MAXLENGTH_ATTR, HTML.MAXLENGTH_ATTR);
        HTMLUtil.renderDisabledOnUserRole(writer, htmlInput, facesContext);

        writer.endElement(HTML.INPUT_ELEM);
    }


}

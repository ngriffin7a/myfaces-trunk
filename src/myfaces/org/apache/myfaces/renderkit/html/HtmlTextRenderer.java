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
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTextRenderer
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlTextRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent component)
        throws IOException
    {
        RendererUtils.checkParamValidity(facesContext,component,null);

        if (RendererUtils.isVisibleOnUserRole(facesContext, component))
        {
            if (component instanceof UIInput)
            {
                renderInput(facesContext, (UIInput)component);
            }
            else if (component instanceof UIOutput)
            {
                renderOutput(facesContext, (UIOutput)component);
            }
            else
            {
                throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
            }
        }
    }


    protected static void renderOutput(FacesContext facesContext, UIOutput uiOutput)
        throws IOException
    {
        String text = RendererUtils.getStringValue(facesContext, uiOutput);
        boolean escape;
        if (uiOutput instanceof HtmlOutputText)
        {
            escape = ((HtmlOutputText)uiOutput).isEscape();
        }
        else
        {
            escape = RendererUtils.getBooleanAttribute(uiOutput, JSFAttr.ESCAPE_ATTR,
                                                       true); //default is to escape
        }
        renderOutputText(facesContext, uiOutput, text, escape);
    }


    public static void renderOutputText(FacesContext facesContext,
                                        UIComponent component,
                                        String text,
                                        boolean escape)
        throws IOException
    {
        if (text != null)
        {
            ResponseWriter writer = facesContext.getResponseWriter();

            boolean span = HTMLUtil.renderHTMLAttributesWithOptionalStartElement(
                    writer, component, HTML.SPAN_ELEM, HTML.COMMON_PASSTROUGH_ATTRIBUTES);

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
    }


    protected static void renderInput(FacesContext facesContext, UIInput uiInput)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        String clientId = uiInput.getClientId(facesContext);
        String value = RendererUtils.getStringValue(facesContext, uiInput);

        writer.startElement(HTML.INPUT_ELEM, uiInput);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_TEXT, null);
        if (value != null)
        {
            writer.writeAttribute(HTML.VALUE_ATTR, value, JSFAttr.VALUE_ATTR);
        }

        HTMLUtil.renderHTMLAttributes(writer, uiInput, HTML.INPUT_PASSTHROUGH_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(writer, uiInput, facesContext);

        writer.endElement(HTML.INPUT_ELEM);
    }


    public void decode(FacesContext facesContext, UIComponent component)
    {
        RendererUtils.checkParamValidity(facesContext,component,null);

        if (component instanceof UIInput)
        {
            HtmlRendererUtils.decodeUIInput(facesContext, component);
        }
        else if (component instanceof UIOutput)
        {
            //nothing to decode
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }


    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIOutput.class);
        return RendererUtils.getConvertedUIOutputValue(facesContext,
                                                       (UIOutput)uiComponent,
                                                       submittedValue);
    }

}

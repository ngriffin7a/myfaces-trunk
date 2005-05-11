/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.renderkit.html;

import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class HtmlTextRendererBase
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlTextRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent component)
        throws IOException
    {
        RendererUtils.checkParamValidity(facesContext,component,null);

        if (component instanceof UIInput)
        {
            renderInput(facesContext, component);
        }
        else if (component instanceof UIOutput)
        {
            renderOutput(facesContext, component);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }


    protected static void renderOutput(FacesContext facesContext, UIComponent component)
        throws IOException
    {
        String text = RendererUtils.getStringValue(facesContext, component);
        boolean escape;
        if (component instanceof HtmlOutputText)
        {
            escape = ((HtmlOutputText)component).isEscape();
        }
        else
        {
            escape = RendererUtils.getBooleanAttribute(component, JSFAttr.ESCAPE_ATTR,
                                                       true); //default is to escape
        }
        renderOutputText(facesContext, component, text, escape);
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
            boolean span = false;

            if(component.getId()!=null && !component.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
            {
                span = true;

                writer.startElement(HTML.SPAN_ELEM, component);

                HtmlRendererUtils.writeIdIfNecessary(writer, component, facesContext);

                HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.COMMON_PASSTROUGH_ATTRIBUTES);

            }
            else
            {
                span = HtmlRendererUtils.renderHTMLAttributesWithOptionalStartElement(writer,component,
                        HTML.SPAN_ELEM,HTML.COMMON_PASSTROUGH_ATTRIBUTES);
            }

            if (escape)
            {
                writer.writeText(text, JSFAttr.VALUE_ATTR);
            }
            else
            {
                writer.write(text);
            }

            if(span)
            {
                writer.endElement(HTML.SPAN_ELEM);
            }
        }
    }


    protected void renderInput(FacesContext facesContext, UIComponent component)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        String clientId = component.getClientId(facesContext);
        String value = RendererUtils.getStringValue(facesContext, component);

        writer.startElement(HTML.INPUT_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_TEXT, null);
        if (value != null)
        {
            writer.writeAttribute(HTML.VALUE_ATTR, value, JSFAttr.VALUE_ATTR);
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (isDisabled(facesContext, component))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        writer.endElement(HTML.INPUT_ELEM);
    }

    protected boolean isDisabled(FacesContext facesContext, UIComponent component)
    {
        //TODO: overwrite in extended HtmlTextRenderer and check for enabledOnUserRole
        if (component instanceof HtmlInputText)
        {
            return ((HtmlInputText)component).isDisabled();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(component, HTML.DISABLED_ATTR, false);
        }
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


    public Object getConvertedValue(FacesContext facesContext, UIComponent component, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, component, UIOutput.class);
        return RendererUtils.getConvertedUIOutputValue(facesContext,
                                                       (UIOutput)component,
                                                       submittedValue);
    }

}

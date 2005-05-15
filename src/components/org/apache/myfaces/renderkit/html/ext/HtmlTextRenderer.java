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
package org.apache.myfaces.renderkit.html.ext;

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.renderkit.html.HtmlTextRendererBase;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.JSFAttr;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTextRenderer
        extends HtmlTextRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlTextRenderer.class);
    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        if (!UserRoleUtils.isEnabledOnUserRole(uiComponent))
        {
            return false;
        }
        else
        {
            return super.isDisabled(facesContext, uiComponent);
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
        throws IOException
    {
        if(component instanceof HtmlInputText)
        {
            if(((HtmlInputText)component).isDisplayValueOnly())
                renderInputValueOnly(facesContext, (HtmlInputText)component);
            else
                renderInput(facesContext, (HtmlInputText)component);
        }
        else
        {
            super.encodeEnd(facesContext, component);
        }
    }

    protected void renderInput(FacesContext facesContext, HtmlInputText inputText)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        String clientId = inputText.getClientId(facesContext);
        String value = RendererUtils.getStringValue(facesContext, inputText);

        writer.startElement(HTML.INPUT_ELEM, inputText);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_TEXT, null);
        if (value != null)
            writer.writeAttribute(HTML.VALUE_ATTR, value, JSFAttr.VALUE_ATTR);

        HtmlRendererUtils.renderHTMLAttributes(writer, inputText, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);

        if(inputText.getStyle() != null)
            writer.writeAttribute(HTML.STYLE_ATTR, inputText.getStyle(), null);
        if(inputText.getStyleClass() != null)
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, inputText.getStyleClass(), null);

        if (isDisabled(facesContext, inputText))
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);

        writer.endElement(HTML.INPUT_ELEM);
    }

    protected void renderInputValueOnly(FacesContext facesContext, HtmlInputText inputText)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        String clientId = inputText.getClientId(facesContext);

        writer.startElement(HTML.SPAN_ELEM, inputText);

        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

        HtmlRendererUtils.renderHTMLAttributes(writer, inputText, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);

        if(inputText.getDisplayValueOnlyStyle() != null)
            writer.writeAttribute(HTML.STYLE_ATTR, inputText.getDisplayValueOnlyStyle(), null);
        if(inputText.getDisplayValueOnlyStyleClass() != null)
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, inputText.getDisplayValueOnlyStyleClass(), null);

        String strValue = RendererUtils.getStringValue(facesContext, inputText);
        writer.writeText(strValue, JSFAttr.VALUE_ATTR);

        writer.endElement(HTML.SPAN_ELEM);
    }
}

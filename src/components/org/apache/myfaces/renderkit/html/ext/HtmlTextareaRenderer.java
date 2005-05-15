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
import org.apache.myfaces.component.html.ext.HtmlInputTextarea;
import org.apache.myfaces.renderkit.html.HtmlTextareaRendererBase;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.JSFAttr;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTextareaRenderer
        extends HtmlTextareaRendererBase
{
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

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIInput.class);

        if(uiComponent instanceof HtmlInputTextarea)
        {
            HtmlInputTextarea textarea = (HtmlInputTextarea) uiComponent;
            if(textarea.isDisplayValueOnly())
                encodeDisplayValueOnly(facesContext, textarea);
            else
                encodeNormal(facesContext, textarea);
        }
    }

    private void encodeNormal(FacesContext facesContext, HtmlInputTextarea textarea) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.TEXTAREA_ELEM, textarea);

        String clientId = textarea.getClientId(facesContext);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        HtmlRendererUtils.writeIdIfNecessary(writer, textarea, facesContext);

        HtmlRendererUtils.renderHTMLAttributes(writer, textarea, HTML.TEXTAREA_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);

        if(textarea.getStyle() != null)
            writer.writeAttribute(HTML.STYLE_ATTR, textarea.getStyle(), null);
        if(textarea.getStyleClass() != null)
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, textarea.getStyleClass(), null);

        if (isDisabled(facesContext, textarea))
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);

        String strValue = RendererUtils.getStringValue(facesContext, textarea);
        writer.writeText(strValue, JSFAttr.VALUE_ATTR);

        writer.endElement(HTML.TEXTAREA_ELEM);
    }

    private void encodeDisplayValueOnly(FacesContext facesContext, HtmlInputTextarea textarea) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.SPAN_ELEM, textarea);

        String clientId = textarea.getClientId(facesContext);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        HtmlRendererUtils.writeIdIfNecessary(writer, textarea, facesContext);

        HtmlRendererUtils.renderHTMLAttributes(writer, textarea, HTML.TEXTAREA_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);

        if(textarea.getDisplayValueOnlyStyle() != null)
            writer.writeAttribute(HTML.STYLE_ATTR, textarea.getDisplayValueOnlyStyle(), null);
        if(textarea.getDisplayValueOnlyStyleClass() != null)
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, textarea.getDisplayValueOnlyStyleClass(), null);

        String strValue = RendererUtils.getStringValue(facesContext, textarea);
        writer.writeText(strValue, JSFAttr.VALUE_ATTR);

        writer.endElement(HTML.SPAN_ELEM);
    }
}

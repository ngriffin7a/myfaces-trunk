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
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;


/**
 * see Spec.1.0 EA - JSF.7.6.4 Renderer Types for UIInput Components
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/10/13 11:51:01  matze
 * renamed packages to org.apache
 *
 * Revision 1.2  2004/07/01 22:00:57  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/05/18 14:31:39  manolito
 * user role support completely moved to components source tree
 *
 */
public class HtmlSecretRendererBase
        extends HtmlRenderer
{
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIInput.class);
        
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_PASSWORD, null);

        String clientId = uiComponent.getClientId(facesContext);

        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

        boolean isRedisplay;
        if (uiComponent instanceof HtmlInputSecret)
        {
            isRedisplay = ((HtmlInputSecret)uiComponent).isRedisplay();
        }
        else
        {
            isRedisplay = RendererUtils.getBooleanAttribute(uiComponent, JSFAttr.REDISPLAY_ATTR, false);
        }
        if (isRedisplay)
        {
            String strValue = RendererUtils.getStringValue(facesContext, uiComponent);
            writer.writeAttribute(HTML.VALUE_ATTR, strValue, JSFAttr.VALUE_ATTR);
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (isDisabled(facesContext, uiComponent))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }
        
        writer.endElement(HTML.INPUT_ELEM);
    }

    
    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        //TODO: overwrite in extended HtmlSecretRenderer and check for enabledOnUserRole
        if (uiComponent instanceof HtmlInputSecret)
        {
            return ((HtmlInputSecret)uiComponent).isDisabled();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
        }
    }
    

    public void decode(FacesContext facesContext, UIComponent component)
    {
        RendererUtils.checkParamValidity(facesContext, component, UIInput.class);
        HtmlRendererUtils.decodeUIInput(facesContext, component);
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIOutput.class);
        return RendererUtils.getConvertedUIOutputValue(facesContext,
                                                       (UIOutput)uiComponent,
                                                       submittedValue);
    }

}

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @author Martin Marinschek
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.15  2005/01/09 09:10:56  mmarinschek
 * prepare call-back methods for renderer
 *
 * Revision 1.14  2004/12/23 13:03:08  mmarinschek
 * id's not rendered (or not conditionally rendered); changes in jslistener to support both ie and firefox now
 *
 * Revision 1.13  2004/12/20 06:13:02  mmarinschek
 * killed bugs
 *
 * Revision 1.12  2004/10/13 11:51:00  matze
 * renamed packages to org.apache
 *
 * Revision 1.11  2004/07/01 22:05:07  mwessendorf
 * ASF switch
 *
 * Revision 1.10  2004/04/13 10:42:03  manolito
 * introduced commons codecs and fileupload
 *
 * Revision 1.9  2004/04/05 15:02:47  manolito
 * for-attribute issues
 *
 */
public class HtmlLabelRenderer
extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlLabelRenderer.class);

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        super.encodeBegin(facesContext, uiComponent);   //check for NP

        ResponseWriter writer = facesContext.getResponseWriter();

        encodeBefore(facesContext, writer, uiComponent);

        writer.startElement(HTML.LABEL_ELEM, uiComponent);
        HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.LABEL_PASSTHROUGH_ATTRIBUTES);

        String forAttr = getFor(uiComponent);
        if (forAttr == null)
        {
            throw new NullPointerException("Attribute 'for' of label component with id " + uiComponent.getClientId(facesContext));
        }

        UIComponent forComponent = uiComponent.findComponent(forAttr);
        if (forComponent == null)
        {
            if (log.isWarnEnabled())
            {
                log.warn("Unable to find component '" + forAttr + "' (calling findComponent on component '" + uiComponent.getClientId(facesContext) + "')");
            }
            if (forAttr.length() > 0 && forAttr.charAt(0) == UINamingContainer.SEPARATOR_CHAR)
            {
                //absolute id path
                writer.writeAttribute(HTML.FOR_ATTR, forAttr.substring(1), JSFAttr.FOR_ATTR);
            }
            else
            {
                //relative id path, we assume a component on the same level as the label component
                String labelClientId = uiComponent.getClientId(facesContext);
                int colon = labelClientId.lastIndexOf(UINamingContainer.SEPARATOR_CHAR);
                if (colon == -1)
                {
                    writer.writeAttribute(HTML.FOR_ATTR, forAttr, JSFAttr.FOR_ATTR);
                }
                else
                {
                    writer.writeAttribute(HTML.FOR_ATTR, labelClientId.substring(0, colon + 1) + forAttr, JSFAttr.FOR_ATTR);
                }
            }
        }
        else
        {
            writer.writeAttribute(HTML.FOR_ATTR, forComponent.getClientId(facesContext), JSFAttr.FOR_ATTR);
        }


        //MyFaces extension: Render a label text given by value
        //TODO: Move to extended component
        if (uiComponent instanceof ValueHolder)
        {
            String text = RendererUtils.getStringValue(facesContext, uiComponent);
            if(text != null)
            {
                writer.writeText(text, "value");
            }
        }

        writer.flush(); // close start tag
    }

    protected void encodeBefore(FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent)
    {
    }


    protected String getFor(UIComponent component)
    {
        if (component instanceof HtmlOutputLabel)
        {
            return ((HtmlOutputLabel)component).getFor();
        }
        else
        {
            return (String)component.getAttributes().get(JSFAttr.FOR_ATTR);
        }
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        super.encodeEnd(facesContext, uiComponent); //check for NP

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.LABEL_ELEM);

        encodeAfter(facesContext, writer, uiComponent);
    }

    protected void encodeAfter(FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent)
    {
    }
}

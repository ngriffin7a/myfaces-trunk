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

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HtmlButtonRenderer
extends HtmlRenderer
{
    private static final String IMAGE_BUTTON_SUFFIX = ".x";

    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UICommand.class);

        //super.decode must not be called, because value is handled here
        if (!isReset(uiComponent) && isSubmitted(facesContext, uiComponent))
        {
            uiComponent.queueEvent(new ActionEvent(uiComponent));
        }
    }

    private static boolean isReset(UIComponent uiComponent)
    {
        return "reset".equalsIgnoreCase((String) uiComponent.getAttributes().get(HTML.TYPE_ATTR));
    }

    private static boolean isSubmitted(FacesContext facesContext, UIComponent uiComponent)
    {
        String clientId = uiComponent.getClientId(facesContext);
        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        return paramMap.containsKey(clientId) || paramMap.containsKey(clientId + IMAGE_BUTTON_SUFFIX);
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UICommand.class);

        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);

        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

        String image = getImage(uiComponent);

        if (image != null)
        {
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_IMAGE, null);
            writer.writeAttribute(HTML.SRC_ATTR, image, JSFAttr.IMAGE_ATTR);
        }
        else
        {
            String type = getType(uiComponent);

            if (type == null)
            {
                type = HTML.INPUT_TYPE_SUBMIT;
            }
            writer.writeAttribute(HTML.TYPE_ATTR, type, JSFAttr.TYPE_ATTR);
            Object value = getValue(uiComponent);
            if (value != null)
            {
                writer.writeAttribute(HTML.VALUE_ATTR, value, null);
            }
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.BUTTON_PASSTHROUGH_ATTRIBUTES);
        HtmlRendererUtils.renderDisabledOnUserRole(writer, uiComponent, facesContext);

        //TODO: We should call the clear_<formName> javascript method on click,
        //because there could be following scenario:
        //User clicks commandLink, then browser back, then commandButton
        //--> hidden input param from commandLink still has a value and gets submitted!

        writer.endElement(HTML.INPUT_ELEM);
    }

    private String getImage(UIComponent uiComponent)
    {
        if (uiComponent instanceof HtmlCommandButton)
        {
            return ((HtmlCommandButton)uiComponent).getImage();
        }
        return (String)uiComponent.getAttributes().get(JSFAttr.IMAGE_ATTR);
    }

    private String getType(UIComponent uiComponent)
    {
        if (uiComponent instanceof HtmlCommandButton)
        {
            return ((HtmlCommandButton)uiComponent).getType();
        }
        return (String)uiComponent.getAttributes().get(JSFAttr.TYPE_ATTR);
    }

    private Object getValue(UIComponent uiComponent)
    {
        if (uiComponent instanceof ValueHolder)
        {
            return ((ValueHolder)uiComponent).getValue();
        }
        return uiComponent.getAttributes().get(JSFAttr.VALUE_ATTR);
    }
}

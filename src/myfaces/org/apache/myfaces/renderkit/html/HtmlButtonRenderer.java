/**
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

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.Map;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HtmlButtonRenderer
extends HtmlRenderer
{
    private static final String IMAGE_BUTTON_SUFFIX = ".x";
    private static final String SUBMIT_BUTTON_TYPE = "submit";
    private String IMAGE_BUTTON_TYPE = "image";

    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UICommand.class);

        //super.decode must not be called, because value is handled here
        UICommand      uiCommand      = (UICommand) uiComponent;

        ExternalContext externalContext = facesContext.getExternalContext();
        String clientId = uiCommand.getClientId(facesContext);

        boolean submitted = isSubmitted(externalContext, clientId);

        if (submitted)
        {
            uiCommand.queueEvent(new ActionEvent(uiCommand));
        }
    }

    private static boolean isSubmitted(ExternalContext externalContext, String clientId)
    {
        boolean submitted = false;

        Map paramValuesMap = externalContext.getRequestParameterValuesMap();

        //check if button has been submitted
        if(!paramValuesMap.containsKey(clientId))
        {
            if(paramValuesMap.containsKey(clientId+IMAGE_BUTTON_SUFFIX))
            {
                submitted = true;
            }
        }
        else
        {
            String[] newValues = (String[])paramValuesMap.get(clientId);

            if(newValues != null && newValues.length>0)
            {
                String firstNewValue = newValues[0];

                if(firstNewValue != null && firstNewValue.length()>0)
                {
                    submitted=true;
                }
            }
        }
        return submitted;
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlCommandButton.class);

        HtmlCommandButton htmlCommand = (HtmlCommandButton) uiComponent;
        String clientId = htmlCommand.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        //boolean hiddenParam = true;

        writer.startElement(HTML.INPUT_ELEM, uiComponent);

        String image = htmlCommand.getImage();

        if (image != null)
        {
            writer.writeAttribute(HTML.TYPE_ATTR, IMAGE_BUTTON_TYPE, null);
            writer.writeAttribute(HTML.SRC_ATTR, image, JSFAttr.IMAGE_ATTR);
        }
        else
        {
            String type = htmlCommand.getType();

            if (type == null)
            {
                type = SUBMIT_BUTTON_TYPE;
            }
            writer.writeAttribute(HTML.TYPE_ATTR, type, JSFAttr.TYPE_ATTR);
            //FIXME: htmlCommand.getTitle() or getValue()? I would think getValue()
            writer.writeAttribute(HTML.VALUE_ATTR, htmlCommand.getValue(), null);
        }

        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.BUTTON_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(writer, uiComponent, facesContext);


        /*
        if (hiddenParam)
        {
            writer.write("<input type=\"hidden\" name=\"");
            writer.write(getHiddenValueParamName(facesContext, uiComponent));
            writer.write("\" value=\"");
            String strVal = getStringValue(facesContext, uiComponent);
            writer.write(HTMLEncoder.encode(strVal, false, false));
            writer.write("\">");
        }
        */
    }
}

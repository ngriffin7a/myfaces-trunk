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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
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
 * Revision 1.9  2004/04/05 15:02:47  manolito
 * for-attribute issues
 *
 */
public class HtmlLabelRenderer
extends HtmlRenderer
{
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        super.encodeBegin(facesContext, uiComponent);   //check for NP

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.LABEL_ELEM, uiComponent);
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.LABEL_PASSTHROUGH_ATTRIBUTES);

        String forAttr = getFor(uiComponent);
        if (forAttr == null)
        {
            throw new NullPointerException("Attribute 'for' of label component with id " + uiComponent.getClientId(facesContext));
        }

        UIComponent forComponent = uiComponent.findComponent(forAttr);
        if (forComponent == null)
        {
            throw new FacesException("Unable to find component '" + forAttr + "' (calling findComponent on component '" + uiComponent.getClientId(facesContext) + "')");
        }

        writer.writeAttribute(HTML.FOR_ATTR, forComponent.getClientId(facesContext), JSFAttr.FOR_ATTR);


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
    }
}

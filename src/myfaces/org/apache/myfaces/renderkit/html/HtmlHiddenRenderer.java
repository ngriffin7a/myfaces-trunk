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

import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.JSFAttr;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import java.io.IOException;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HtmlHiddenRenderer
extends HtmlRenderer
{
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlInputHidden.class);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);

        String clientId = uiComponent.getClientId(facesContext);        
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

        ValueBinding vb = uiComponent.getValueBinding(JSFAttr.VALUE_ATTR);
        if (vb != null)
        {
            writer.writeAttribute(HTML.VALUE_ATTR, vb.getValue(facesContext), JSFAttr.VALUE_ATTR);
        }
        
        writer.endElement(HTML.INPUT_ELEM);
    }
}

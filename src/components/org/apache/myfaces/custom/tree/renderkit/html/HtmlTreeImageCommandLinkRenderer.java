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


package net.sourceforge.myfaces.custom.tree.renderkit.html;

import net.sourceforge.myfaces.custom.tree.HtmlTreeImageCommandLink;
import net.sourceforge.myfaces.custom.tree.HtmlTreeNode;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlLinkRendererBase;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.2  2004/05/27 15:06:39  manolito
 *          bugfix: node labels not rendered any more
 *
 *          Revision 1.1  2004/04/22 10:20:24  manolito
 *          tree component
 *
 */
public class HtmlTreeImageCommandLinkRenderer
        extends HtmlLinkRendererBase
{

    private static final Integer ZERO = new Integer(0);


    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);

        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String id = (String)requestParameterMap.get(component.getClientId(facesContext));

        if (id != null && id.length() > 0)
        {
            HtmlTreeNode node = (HtmlTreeNode)component.getParent();

            node.toggleExpanded();
        }
    }


    protected void renderCommandLinkStart(FacesContext facesContext,
                                          UIComponent component,
                                          String clientId,
                                          Object value,
                                          String style,
                                          String styleClass) throws IOException
    {

        super.renderCommandLinkStart(facesContext, component, clientId, value, style, styleClass);

        // render value as anchor text:
        if (value != null)
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.writeText(value.toString(), JSFAttr.VALUE_ATTR);
        }

        String url = ((HtmlTreeImageCommandLink)component).getImage();

        if ((url != null) && (url.length() > 0))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.startElement(HTML.IMG_ELEM, component);

            String src;
            if (url.startsWith(HTML.HREF_PATH_SEPARATOR))
            {
                String path = facesContext.getExternalContext().getRequestContextPath();
                src = path + url;
            }
            else
            {
                src = url;
            }
            //Encode URL
            //Although this is an url url, encodeURL is no nonsense, because the
            //actual url url could also be a dynamic servlet request:
            src = facesContext.getExternalContext().encodeResourceURL(src);
            writer.writeAttribute(HTML.SRC_ATTR, src, null);
            writer.writeAttribute(HTML.BORDER_ATTR, ZERO, null);

            HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.IMG_PASSTHROUGH_ATTRIBUTES);

            writer.endElement(HTML.IMG_ELEM);
        }
    }
}

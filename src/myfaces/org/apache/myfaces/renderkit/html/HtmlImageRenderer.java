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
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HtmlImageRenderer
extends HtmlRenderer
{
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlGraphicImage.class);

        HtmlGraphicImage img = (HtmlGraphicImage) uiComponent;

        ResponseWriter writer = facesContext.getResponseWriter();

        String value = img.getURL();

        if ((value != null) && (value.length() > 0))
        {
            writer.startElement(HTML.IMG_ELEM, img);

            String src=img.getURL();

            if (value.startsWith(HTML.HREF_PATH_SEPARATOR))
            {
                String path = facesContext.getExternalContext().getRequestContextPath();
                src = path + value;
            }

            //Encode URL
            //Although this is an image url, encodeURL is no nonsense, because the
            //actual image url could also be a dynamic servlet request:
            src = facesContext.getExternalContext().encodeResourceURL(src);

            writer.writeAttribute(HTML.SRC_ATTR, src, null);

            writer.writeAttribute(HTML.ALT_ATTR, img.getAlt(), null);

            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.IMG_PASSTHROUGH_ATTRIBUTES);
        }
    }
}

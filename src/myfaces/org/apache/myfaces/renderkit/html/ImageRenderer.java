/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ImageRenderer
    extends HTMLRenderer
{
    public static final String TYPE = "Form";

    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof UIGraphic;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIGraphic.TYPE);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        javax.faces.context.ResponseWriter writer = facesContext.getResponseWriter();

        String value = (String)uiComponent.currentValue(facesContext);
        if (value != null && value.length() > 0)
        {
            writer.write("<img src=\"");

            StringBuffer buf = new StringBuffer();
            if (value.startsWith("/"))
            {
                HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();
                buf.append(request.getContextPath())
                   .append(value);
            }
            else
            {
                buf.append(value);
            }

            //Encode URL for those still using HttpSessions... ;-)
            String href = ((HttpServletResponse)facesContext.getServletResponse()).encodeURL(buf.toString());

            writer.write(href);
            writer.write("\">");
        }



        return;

    }

}

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
package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CommonAttributes
{
    public static void renderUniversalHTMLAttributes(FacesContext context, UIComponent component)
        throws IOException
    {
        renderAttributes(context, component, CommonRendererAttributes.UNIVERSAL_ATTRIBUTES);
    }

    public static void renderHTMLEventHandlerAttributes(FacesContext context, UIComponent component)
        throws IOException
    {
        renderAttributes(context, component, CommonRendererAttributes.EVENT_HANDLER_ATTRIBUTES);
    }

    public static void renderAttributes(FacesContext context, UIComponent component, String[] attributes)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        for (int i = 0; i < attributes.length; i++)
        {
            String attrName = attributes[i];
            Object value = (Object)component.getAttribute(attrName);
            if (value != null)
            {
                if (value instanceof String &&
                    ((String)value).length() > 0)
                {
                    writer.write(" ");
                    writer.write(attrName);
                    writer.write("\"");
                    writer.write(HTMLEncoder.encode((String)value, false, false));
                    writer.write("\"");
                }
                else if (value instanceof Boolean)
                {
                    writer.write(" ");
                    writer.write(attrName);
                    writer.write("\"");
                    writer.write(HTMLEncoder.encode(
                        ((Boolean)value).booleanValue() ? "true" : "false",
                        false,
                        false));
                    writer.write("\"");
                }
            }
        }
    }
}

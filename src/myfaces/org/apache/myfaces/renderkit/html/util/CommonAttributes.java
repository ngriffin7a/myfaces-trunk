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

import net.sourceforge.myfaces.renderkit.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.attr.HTMLUniversalAttributes;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CommonAttributes
{
    /**
     * @deprecated
     */
    public static void renderHTMLUniversalAttributes(FacesContext context,
                                                     UIComponent component)
        throws IOException
    {
        renderAttributes(context,
                         component,
                         HTMLUniversalAttributes.HTML_UNIVERSAL_ATTRIBUTES);
    }

    /**
     * @deprecated
     */
    public static void renderHTMLEventHandlerAttributes(FacesContext context,
                                                        UIComponent component)
        throws IOException
    {
        renderAttributes(context,
                         component,
                         HTMLEventHandlerAttributes.HTML_EVENT_HANDLER_ATTRIBUTES);
    }

    /**
     * @deprecated
     */
    public static void renderAttributes(FacesContext context,
                                        UIComponent component,
                                        String[] attributes)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        for (int i = 0; i < attributes.length; i++)
        {
            String attrName = attributes[i];
            Object value = component.getAttribute(attrName);
            if (value != null)
            {
                if (value instanceof String &&
                    ((String)value).length() > 0)
                {
                    writer.write(" ");
                    writer.write(attrName);
                    writer.write("=\"");
                    writer.write(HTMLEncoder.encode((String)value, false, false));
                    writer.write("\"");
                }
                else if (value instanceof Boolean)
                {
                    //TODO: Some html boolean attributes must not be rendered when false
                    writer.write(" ");
                    writer.write(attrName);
                    writer.write("=\"");
                    writer.write(HTMLEncoder.encode(
                        ((Boolean)value).booleanValue() ? "true" : "false",
                        false,
                        false));
                    writer.write("\"");
                }
                else
                {
                    writer.write(" ");
                    writer.write(attrName);
                    writer.write("=\"");
                    writer.write(HTMLEncoder.encode(
                        value.toString(),
                        false,
                        false));
                    writer.write("\"");
                }
            }
        }
    }

    public static void renderHTMLAttributes(ResponseWriter writer,
                                            UIComponent component,
                                            String[] attributes)
        throws IOException
    {
        for (int i = 0; i < attributes.length; i++)
        {
            String attrName = attributes[i];
            Object value = component.getAttribute(attrName);
            if (value != null)
            {
                if (value instanceof Boolean &&
                    ((Boolean)value).booleanValue())
                {
                    writer.write(" ");
                    writer.write(attrName);
                }
                else
                {
                    writer.write(" ");
                    writer.write(attrName);
                    writer.write("=\"");
                    writer.write(value.toString());
                    writer.write("\"");
                }
            }
        }
    }

    public static void renderCssClass(ResponseWriter writer,
                                      UIComponent uiComponent,
                                      String classAttrName)
        throws IOException
    {
        String cssClass = (String)uiComponent.getAttribute(classAttrName);
        if (cssClass != null)
        {
            writer.write(" class=\"");
            writer.write(cssClass);
            writer.write("\"");
        }
    }

}

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
package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


/**
 * Utility methods for rendering HTML tags.
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HTMLUtil
{
    //~ Constructors -------------------------------------------------------------------------------

    private HTMLUtil() {} // disallow public instantiation

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * @return true, if the attribute was written
     * @throws IOException
     */
    public static boolean renderHTMLAttribute(ResponseWriter writer,
                                              String componentProperty,
                                              String attrName,
                                              Object value)
        throws IOException
    {
        if (!RendererUtils.isDefaultAttributeValue(value))
        {
            // render JSF "styleClass" attribute as "class"
            String htmlAttrName = attrName.equals(HTML.STYLE_CLASS_ATTR) ?
                                  HTML.CLASS_ATTR :
                                  attrName;
            writer.writeAttribute(htmlAttrName, value, componentProperty);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @return true, if the attribute was written
     * @throws IOException
     */
    public static boolean renderHTMLAttribute(ResponseWriter writer,
                                              UIComponent component,
                                              String componentProperty,
                                              String htmlAttrName)
        throws IOException
    {
        Object value = component.getAttributes().get(componentProperty);
        return renderHTMLAttribute(writer, componentProperty, htmlAttrName, value);
    }


    /**
     * @return true, if an attribute was written
     * @throws IOException
     */
    public static boolean renderHTMLAttributes(ResponseWriter writer,
                                               UIComponent component,
                                               String[] attributes)
    throws IOException
    {
        boolean somethingDone = false;
        for (int i = 0, len = attributes.length; i < len; i++)
        {
            String attrName = attributes[i];
            if (renderHTMLAttribute(writer, component, attrName, attrName))
            {
                somethingDone = true;
            }
        }
        return somethingDone;
    }


    public static boolean renderHTMLAttributesWithOptionalStartElement(ResponseWriter writer,
                                                                       UIComponent component,
                                                                       String elementName,
                                                                       String[] attributes)
            throws IOException
    {
        boolean startElementWritten = false;
        for (int i = 0, len = attributes.length; i < len; i++)
        {
            String attrName = attributes[i];
            Object value = component.getAttributes().get(attrName);
            if (!RendererUtils.isDefaultAttributeValue(value))
            {
                if (!startElementWritten)
                {
                    writer.startElement(elementName, component);
                    startElementWritten = true;
                }
                
                // render JSF "styleClass" attribute as "class"
                String htmlAttrName = 
                    attrName.equals(HTML.STYLE_CLASS_ATTR) ? HTML.CLASS_ATTR : attrName;
                writer.writeAttribute(htmlAttrName, value, attrName);
            }
        }
        return startElementWritten;
    }


    public static void renderDisabledOnUserRole(ResponseWriter writer,
                                                UIComponent uiComponent,
                                                FacesContext facesContext)
        throws IOException
    {
        if (!RendererUtils.isEnabledOnUserRole(facesContext, uiComponent))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, JSFAttr.ENABLED_ON_USER_ROLE_ATTR);
        }
    }

}

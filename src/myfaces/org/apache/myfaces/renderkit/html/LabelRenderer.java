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

import net.sourceforge.myfaces.renderkit.attr.*;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIOutput;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LabelRenderer
    extends HTMLRenderer
    implements CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               LabelRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Label";

    public String getRendererType()
    {
        return TYPE;
    }

    public LabelRenderer()
    {
        addAttributeDescriptor(UIOutput.TYPE, KEY_ATTR);
        addAttributeDescriptor(UIOutput.TYPE, BUNDLE_ATTR);
    }

    public boolean supportsComponentType(UIComponent component)
    {
        return component instanceof UIOutput;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIOutput.TYPE);
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<label");

        CommonAttributes.renderCssClass(writer, uiComponent, OUTPUT_CLASS_ATTR);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);

        writer.write(">");

        String text;
        String key = (String)uiComponent.getAttribute(KEY_ATTR.getName());
        if (key != null)
        {
            text = BundleUtils.getString(facesContext,
                                            (String)uiComponent.getAttribute(BUNDLE_ATTR.getName()),
                                            key);
        }
        else
        {
            text = getStringValue(facesContext, uiComponent);
        }
        writer.write(HTMLEncoder.encode(text, true, true));
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</label>");
    }
}

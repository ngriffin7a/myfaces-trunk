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
        implements CommonRendererAttributes,
                   HTMLUniversalAttributes,
                   HTMLEventHandlerAttributes,
                   HTMLImgAttributes,
                   ImageRendererAttributes,
                   UserRoleAttributes
{
    public static final String TYPE = "Image";
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

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIGraphic.TYPE, TLD_HTML_URI, "graphic_image", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIGraphic.TYPE, TLD_HTML_URI, "graphic_image", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIGraphic.TYPE, TLD_HTML_URI, "graphic_image", HTML_IMG_ATTRUBUTES);
        addAttributeDescriptors(UIGraphic.TYPE, TLD_HTML_URI, "graphic_image", GRAPHIC_IMAGE_ATTRIBUTES);
        addAttributeDescriptors(UIGraphic.TYPE, TLD_HTML_URI, "graphic_image", USER_ROLE_ATTRIBUTES);
    }



    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext context, UIComponent uiComponent) throws IOException
    {
        javax.faces.context.ResponseWriter writer = context.getResponseWriter();

        String value;

        String key = (String)uiComponent.getAttribute(KEY_ATTR);
        if (key != null)
        {
            value = BundleUtils.getString(context,
                                          (String)uiComponent.getAttribute(BUNDLE_ATTR),
                                          key);
        }
        else
        {
            value = getStringValue(context, uiComponent);
        }

        if (value != null && value.length() > 0)
        {
            writer.write("<img src=\"");

            String src;
            if (value.startsWith("/"))
            {
                HttpServletRequest request = (HttpServletRequest)context.getServletRequest();
                src = request.getContextPath() + value;
            }
            else
            {
                src = value;
            }

            //Encode URL for those still using HttpSessions... ;-)
            //Although this is an image url, encodeURL is no nonsense, because the
            //actual image url could also be a dynamic servlet request:
            src = ((HttpServletResponse)context.getServletResponse()).encodeURL(src);

            writer.write(src);
            writer.write("\"");

            String alt;
            String altKey = (String)uiComponent.getAttribute(ALT_KEY_ATTR);
            if (altKey != null)
            {
                alt = BundleUtils.getString(context,
                                              (String)uiComponent.getAttribute(ALT_BUNDLE_ATTR),
                                              altKey);
            }
            else
            {
                alt = (String)uiComponent.getAttribute(ALT_ATTR);
            }
            if (alt != null && alt.length() > 0)
            {
                writer.write(" alt=\"");
                writer.write(HTMLEncoder.encode(alt, false, false));
                writer.write("\"");
            }

            CommonAttributes.renderCssClass(writer, uiComponent, GRAPHIC_CLASS_ATTR);
            CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
            CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
            CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_IMG_ATTRUBUTES);

            writer.write(">");
        }
    }

}

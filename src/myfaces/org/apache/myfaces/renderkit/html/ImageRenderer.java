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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ImageRenderer
extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "Image";

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    /*
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
    */
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent)
    throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        String         value;

        String         key = (String) uiComponent.getAttribute(JSFAttr.KEY_ATTR);

        if (key != null)
        {
            value =
                BundleUtils.getString(
                    facesContext, (String) uiComponent.getAttribute(JSFAttr.BUNDLE_ATTR), key);
        }
        else
        {
            value = getStringValue(facesContext, (UIGraphic) uiComponent);
        }

        if ((value != null) && (value.length() > 0))
        {
            writer.write("<img src=\"");

            String src;

            if (value.startsWith("/"))
            {
                HttpServletRequest request =
                    (HttpServletRequest) facesContext.getExternalContext().getRequest();
                src = request.getContextPath() + value;
            }
            else
            {
                src = value;
            }

            //Encode URL
            //Although this is an image url, encodeURL is no nonsense, because the
            //actual image url could also be a dynamic servlet request:
            src = facesContext.getExternalContext().encodeResourceURL(src);

            writer.write(src);
            writer.write('"');

            String alt;
            String altKey = (String) uiComponent.getAttribute(JSFAttr.ALT_KEY_ATTR);

            if (altKey != null)
            {
                alt = BundleUtils.getString(
                        facesContext, (String) uiComponent.getAttribute(JSFAttr.ALT_BUNDLE_ATTR),
                        altKey);
            }
            else
            {
                alt = (String) uiComponent.getAttribute(HTML.ALT_ATTR);
            }

            if ((alt != null) && (alt.length() > 0))
            {
                writer.write(" alt=\"");
                writer.write(HTMLEncoder.encode(alt, false, false));
                writer.write('"');
            }

            HTMLUtil.renderCssClass(writer, uiComponent, JSFAttr.GRAPHIC_CLASS_ATTR);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.IMG_ATTRUBUTES);

            writer.write('>');
        }
    }
}

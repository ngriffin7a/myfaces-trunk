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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.component.UIGraphic;
import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.renderkit.html.ImageRenderer;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLImgAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.attr.*;

import javax.faces.component.UIComponent;


/**
 * see "graphic_image" tag in myfaces_html.tld
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class GraphicImageTag
    extends MyFacesTag
    implements CommonComponentAttributes,
               CommonRendererAttributes,
    HTMLUniversalAttributes,
    HTMLEventHandlerAttributes,
    HTMLImgAttributes,
               UserRoleAttributes,
               ImageRendererAttributes
{
    public UIComponent createComponent()
    {
        return new UIGraphic();
    }

    public String getRendererType()
    {
        return ImageRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIGraphic attributes

    public void setUrl(String value)
    {
        setComponentPropertyString(UIGraphic.URL_ATTR, value);
    }

    public void setGraphicClass(String v)
    {
        setRendererAttributeString(GRAPHIC_CLASS_ATTR, v);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML img attributes

    public void setAlign(String value)
    {
        setRendererAttributeString(ALIGN_ATTR, value);
    }

    public void setAlt(String value)
    {
        setRendererAttributeString(ALT_ATTR, value);
    }

    public void setBorder(String value)
    {
        setRendererAttributeString(BORDER_ATTR, value);
    }

    public void setHeight(String value)
    {
        setRendererAttributeString(HEIGHT_ATTR, value);
    }

    public void setHspace(String value)
    {
        setRendererAttributeString(HSPACE_ATTR, value);
    }

    public void setIsmap(String value)
    {
        setRendererAttributeString(ISMAP_ATTR, value);
    }

    public void setLongdesc(String value)
    {
        setRendererAttributeString(LONGDESC_ATTR, value);
    }

    public void setUsemap(String value)
    {
        setRendererAttributeString(USEMAP_ATTR, value);
    }

    public void setVspace(String value)
    {
        setRendererAttributeString(VSPACE_ATTR, value);
    }

    public void setWidth(String value)
    {
        setRendererAttributeString(WIDTH_ATTR, value);
    }



    // Image Renderer attributes

    public void setAltKey(String value)
    {
        setRendererAttributeString(ALT_KEY_ATTR, value);
    }

    public void setAltBundle(String value)
    {
        setRendererAttributeString(ALT_BUNDLE_ATTR, value);
    }


    // key and bundle attributes --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag

}

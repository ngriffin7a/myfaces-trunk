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
package net.sourceforge.myfaces.taglib.legacy;

import net.sourceforge.myfaces.component.MyFacesUIGraphic;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.ImageRenderer;


/**
 * see "graphic_image" tag in myfaces_html.tld
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class GraphicImageTag
extends MyFacesTag
{
    public String getComponentType()
    {
        return "Graphic";
    }

    public String getDefaultRendererType()
    {
        return ImageRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIGraphic attributes

    public void setUrl(String value)
    {
        setComponentPropertyString(MyFacesUIGraphic.URL_PROP, value);
    }

    public void setGraphicClass(String v)
    {
        setRendererAttributeString(JSFAttr.GRAPHIC_CLASS_ATTR, v);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML img attributes

    public void setAlign(String value)
    {
        setRendererAttributeString(HTML.ALIGN_ATTR, value);
    }

    public void setAlt(String value)
    {
        setRendererAttributeString(HTML.ALT_ATTR, value);
    }

    public void setBorder(String value)
    {
        setRendererAttributeString(HTML.BORDER_ATTR, value);
    }

    public void setHeight(String value)
    {
        setRendererAttributeString(HTML.HEIGHT_ATTR, value);
    }

    public void setHspace(String value)
    {
        setRendererAttributeString(HTML.HSPACE_ATTR, value);
    }

    public void setIsmap(String value)
    {
        setRendererAttributeString(HTML.ISMAP_ATTR, value);
    }

    public void setLongdesc(String value)
    {
        setRendererAttributeString(HTML.LONGDESC_ATTR, value);
    }

    public void setUsemap(String value)
    {
        setRendererAttributeString(HTML.USEMAP_ATTR, value);
    }

    public void setVspace(String value)
    {
        setRendererAttributeString(HTML.VSPACE_ATTR, value);
    }

    public void setWidth(String value)
    {
        setRendererAttributeString(HTML.WIDTH_ATTR, value);
    }



    // Image Renderer attributes

    public void setAltKey(String value)
    {
        setRendererAttributeString(JSFAttr.ALT_KEY_ATTR, value);
    }

    public void setAltBundle(String value)
    {
        setRendererAttributeString(JSFAttr.ALT_BUNDLE_ATTR, value);
    }


    // key and bundle attributes --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag

}

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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.LabelRenderer;


/**
 * see "output_label" tag in myfaces_html.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class OutputLabelTag
extends MyFacesTag
{
    public String getComponentType()
    {
        return "Output";
    }

    public String getDefaultRendererType()
    {
        return LabelRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIOutput attributes

    /**
     * This is a MyFaces extension. JSF.7.4.5 says nothing about a value for label.
     * @param value
     */
    public void setValue(Object value)
    {
        super.setValue(value);
    }

    public void setOutputClass(String value)
    {
        setRendererAttributeString(JSFAttr.OUTPUT_CLASS_ATTR, value);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML label attributes

    public void setAccesskey(String value)
    {
        setRendererAttributeString(HTML.ACCESSKEY_ATTR, value);
    }

    public void setOnblur(String value)
    {
        setRendererAttributeString(HTML.ONBLUR_ATTR, value);
    }

    public void setOnfocus(String value)
    {
        setRendererAttributeString(HTML.ONFOCUS_ATTR, value);
    }

    // Label Renderer attributes

    public void setFor(String value)
    {
        setRendererAttributeString(JSFAttr.FOR_ATTR, value);
    }

    // user role attributes --> already implemented in MyFacesTag

    // key and bundle attributes --> already implemented in MyFacesTag
}

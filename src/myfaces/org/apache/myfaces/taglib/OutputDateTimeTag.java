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

import net.sourceforge.myfaces.renderkit.attr.DateTimeRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.DateTimeRenderer;

import javax.faces.component.UIComponent;


/**
 * see "output_date_time" tag in myfaces_html.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class OutputDateTimeTag
    extends MyFacesTag
    implements DateTimeRendererAttributes
{
    public String getComponentType()
    {
        return "Output";
    }

    public void overrideProperties(UIComponent uiComponent)
    {
        super.overrideProperties(uiComponent);
        if (uiComponent.getConverter() == null)
        {
            uiComponent.setConverter("DateTimeConverter");
        }
    }

    public String getRendererType()
    {
        return DateTimeRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIOutput attributes

    public void setValue(Object v)
    {
        super.setValue(v);
    }

    public void setOutputClass(String value)
    {
        setRendererAttributeString(OUTPUT_CLASS_ATTR, value);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // Date Renderer attributes

    public void setDateStyle(String value)
    {
        setRendererAttributeString(DATE_STYLE_ATTR, value);
    }

    public void setTimeStyle(String value)
    {
        setRendererAttributeString(TIME_STYLE_ATTR, value);
    }

    public void setTimezone(String value)
    {
        setRendererAttributeString(TIMEZONE_ATTR, value);
    }

    public void setFormatPattern(String value)
    {
        setRendererAttributeString(FORMAT_PATTERN_ATTR, value);
    }

    // user role attributes --> already implemented in MyFacesTag

}

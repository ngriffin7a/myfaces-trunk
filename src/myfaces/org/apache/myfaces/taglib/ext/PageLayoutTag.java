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
package net.sourceforge.myfaces.taglib.ext;

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.attr.ext.LayoutRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLTableAttributes;
import net.sourceforge.myfaces.renderkit.html.ext.LayoutRenderer;
import net.sourceforge.myfaces.taglib.MyFacesBodyTag;

import javax.faces.component.UIComponent;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class PageLayoutTag
    extends MyFacesBodyTag
    implements HTMLTableAttributes,
               LayoutRendererAttributes
{
    public String getComponentType()
    {
        return "Panel";
    }

    public void overrideProperties(UIComponent uiComponent)
    {
        super.overrideProperties(uiComponent);
        UIComponentUtils.setTransient(uiComponent, true);
    }

    public String getRendererType()
    {
        return LayoutRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIPanel attributes

    public void setPanelClass(String v)
    {
        setRendererAttributeString(PANEL_CLASS_ATTR, v);
    }

    public void setLayout(String value)
    {
        setValue(value);
    }

    public void setLayoutRef(String value)
    {
        setValueRef(value);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML table attributes

    public void setAlign(String value)
    {
        setRendererAttributeString(ALIGN_ATTR, value);
    }

    public void setBgcolor(String value)
    {
        setRendererAttributeString(BGCOLOR_ATTR, value);
    }

    public void setBorder(String value)
    {
        setRendererAttributeString(BORDER_ATTR, value);
    }

    public void setCellpadding(String value)
    {
        setRendererAttributeString(CELLPADDING_ATTR, value);
    }

    public void setCellspacing(String value)
    {
        setRendererAttributeString(CELLSPACING_ATTR, value);
    }

    public void setDatafld(String value)
    {
        setRendererAttributeString(DATAFLD_ATTR, value);
    }

    public void setDatasrc(String value)
    {
        setRendererAttributeString(DATASRC_ATTR, value);
    }

    public void setDataformatas(String value)
    {
        setRendererAttributeString(DATAFORMATAS_ATTR, value);
    }

    public void setFrame(String value)
    {
        setRendererAttributeString(FRAME_ATTR, value);
    }

    public void setRules(String value)
    {
        setRendererAttributeString(RULES_ATTR, value);
    }

    public void setSummary(String value)
    {
        setRendererAttributeString(SUMMARY_ATTR, value);
    }

    public void setWidth(String value)
    {
        setRendererAttributeString(WIDTH_ATTR, value);
    }


    // Layout Renderer attributes

    public void setHeaderClass(String value)
    {
        setRendererAttributeString(HEADER_CLASS_ATTR, value);
    }

    public void setNavigationClass(String value)
    {
        setRendererAttributeString(NAVIGATION_CLASS_ATTR, value);
    }

    public void setBodyClass(String value)
    {
        setRendererAttributeString(BODY_CLASS_ATTR, value);
    }

    public void setFooterClass(String value)
    {
        setRendererAttributeString(FOOTER_CLASS_ATTR, value);
    }

}

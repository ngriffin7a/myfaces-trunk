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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.ListRenderer;


/**
 * see "panel_list" tag in myfaces_html.tld
 * @author Thomas Spiegl (latest modification by Author)
 * @version $Revision$ $Date$
 */
public class PanelListTag
extends MyFacesTag
{
    public String getComponentType()
    {
        return "Panel";
    }

    public String getRendererType()
    {
        return ListRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIPanel attributes

    public void setValue(Object v)
    {
        super.setValue(v);
    }

    public void setPanelClass(String v)
    {
        setRendererAttributeString(JSFAttr.PANEL_CLASS_ATTR, v);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML table attributes

    public void setAlign(String value)
    {
        setRendererAttributeString(HTML.ALIGN_ATTR, value);
    }

    public void setBgcolor(String value)
    {
        setRendererAttributeString(HTML.BGCOLOR_ATTR, value);
    }

    public void setBorder(String value)
    {
        setRendererAttributeString(HTML.BORDER_ATTR, value);
    }

    public void setCellpadding(String value)
    {
        setRendererAttributeString(HTML.CELLPADDING_ATTR, value);
    }

    public void setCellspacing(String value)
    {
        setRendererAttributeString(HTML.CELLSPACING_ATTR, value);
    }

    public void setDatafld(String value)
    {
        setRendererAttributeString(HTML.DATAFLD_ATTR, value);
    }

    public void setDatasrc(String value)
    {
        setRendererAttributeString(HTML.DATASRC_ATTR, value);
    }

    public void setDataformatas(String value)
    {
        setRendererAttributeString(HTML.DATAFORMATAS_ATTR, value);
    }

    public void setFrame(String value)
    {
        setRendererAttributeString(HTML.FRAME_ATTR, value);
    }

    public void setRules(String value)
    {
        setRendererAttributeString(HTML.RULES_ATTR, value);
    }

    public void setSummary(String value)
    {
        setRendererAttributeString(HTML.SUMMARY_ATTR, value);
    }

    public void setWidth(String value)
    {
        setRendererAttributeString(HTML.WIDTH_ATTR, value);
    }


    // List Renderer attributes

    public void setColumnClasses(String value)
    {
        setRendererAttributeString(JSFAttr.COLUMN_CLASSES_ATTR, value);
    }

    public void setFooterClass(String value)
    {
        setRendererAttributeString(JSFAttr.FOOTER_CLASS_ATTR, value);
    }

    public void setHeaderClass(String value)
    {
        setRendererAttributeString(JSFAttr.HEADER_CLASS_ATTR, value);
    }

    public void setRowClasses(String value)
    {
        setRendererAttributeString(JSFAttr.ROW_CLASSES_ATTR, value);
    }

    // user role attributes --> already implemented in MyFacesTag
}

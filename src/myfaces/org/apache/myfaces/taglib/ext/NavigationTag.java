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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.attr.ext.NavigationRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.ext.NavigationRenderer;
import net.sourceforge.myfaces.taglib.MyFacesTag;

/**
 * see "navigation" tag in myfaces_ext.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationTag
    extends MyFacesTag
    implements NavigationRendererAttributes
{
    public String getComponentType()
    {
        return "Navigation";
    }

    public String getRendererType()
    {
        return NavigationRenderer.TYPE;
    }

    // UIPanel attributes

    public void setValue(Object value)
    {
        super.setValue(value);
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

    // bundle attributes --> already implemented in MyFacesTag

    // Navigation Renderer attributes

    /*
    public void setLevelClasses(String value)
    {
        setRendererAttributeString(LEVEL_CLASSES, value);
    }
    */

    public void setOpenItemClass(String s)
    {
        setRendererAttributeString(OPEN_ITEM_CLASS_ATTR, s);
    }

    public void setActiveItemClass(String s)
    {
        setRendererAttributeString(ACTIVE_ITEM_CLASS_ATTR, s);
    }

    public void setItemClass(String s)
    {
        setRendererAttributeString(ITEM_CLASS_ATTR, s);
    }

    public void setSeparatorClass(String s)
    {
        setRendererAttributeString(SEPARATOR_CLASS, s);
    }
}

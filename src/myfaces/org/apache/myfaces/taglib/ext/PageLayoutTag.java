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
import net.sourceforge.myfaces.component.UIPanel;
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
    public UIComponent createComponent()
    {
        UIComponent comp = new UIPanel(false);
        UIComponentUtils.setTransient(comp, true);
        comp.setValid(true);
        return comp;
    }

    public String getRendererType()
    {
        return LayoutRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIPanel attributes

    public void setPanelClass(String v)
    {
        setRendererAttribute(PANEL_CLASS_ATTR, v);
    }

    public void setLayout(String value)
    {
        setValue(value);
    }

    public void setLayoutReference(String value)
    {
        setModelReference(value);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML table attributes

    public void setAlign(String value)
    {
        setRendererAttribute(ALIGN_ATTR, value);
    }

    public void setBgcolor(String value)
    {
        setRendererAttribute(BGCOLOR_ATTR, value);
    }

    public void setBorder(String value)
    {
        setRendererAttribute(BORDER_ATTR, value);
    }

    public void setCellpadding(boolean value)
    {
        setRendererAttribute(CELLPADDING_ATTR, value);
    }

    public void setCellspacing(String value)
    {
        setRendererAttribute(CELLSPACING_ATTR, value);
    }

    public void setDatafld(String value)
    {
        setRendererAttribute(DATAFLD_ATTR, value);
    }

    public void setDatasrc(String value)
    {
        setRendererAttribute(DATASRC_ATTR, value);
    }

    public void setDataformatas(String value)
    {
        setRendererAttribute(DATAFORMATAS_ATTR, value);
    }

    public void setFrame(String value)
    {
        setRendererAttribute(FRAME_ATTR, value);
    }

    public void setRules(String value)
    {
        setRendererAttribute(RULES_ATTR, value);
    }

    public void setSummary(String value)
    {
        setRendererAttribute(SUMMARY_ATTR, value);
    }

    public void setWidth(boolean value)
    {
        setRendererAttribute(WIDTH_ATTR, value);
    }


    // Layout Renderer attributes

    public void setHeaderClass(String value)
    {
        setRendererAttribute(HEADER_CLASS_ATTR, value);
    }

    public void setNavigationClass(String value)
    {
        setRendererAttribute(NAVIGATION_CLASS_ATTR, value);
    }

    public void setBodyClass(String value)
    {
        setRendererAttribute(BODY_CLASS_ATTR, value);
    }

    public void setFooterClass(String value)
    {
        setRendererAttribute(FOOTER_CLASS_ATTR, value);
    }

}

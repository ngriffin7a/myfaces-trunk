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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.RadioRenderer;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectOneRadioTag
extends MyFacesTag
{
    public String getComponentType()
    {
        return "SelectOne";
    }

    public String getDefaultRendererType()
    {
        return RadioRenderer.TYPE;
    }

    public void setSelectOneClass(String v)
    {
        setRendererAttributeString(JSFAttr.SELECT_ONE_CLASS_ATTR, v);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML input attributes

    public void setAccesskey(String value)
    {
        setRendererAttributeString(HTML.ACCESSKEY_ATTR, value);
    }

    public void setAlign(String value)
    {
        setRendererAttributeString(HTML.ALIGN_ATTR, value);
    }

    public void setAlt(String value)
    {
        setRendererAttributeString(HTML.ALT_ATTR, value);
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

    public void setDisabled(String value)
    {
        setRendererAttributeBoolean(HTML.DISABLED_ATTR, value);
    }

    public void setOnblur(String value)
    {
        setRendererAttributeString(HTML.ONBLUR_ATTR, value);
    }

    public void setOnchange(String value)
    {
        setRendererAttributeString(HTML.ONCHANGE_ATTR, value);
    }

    public void setOnfocus(String value)
    {
        setRendererAttributeString(HTML.ONFOCUS_ATTR, value);
    }

    public void setOnselect(String value)
    {
        setRendererAttributeString(HTML.ONSELECT_ATTR, value);
    }

    public void setReadonly(String value)
    {
        setRendererAttributeBoolean(HTML.READONLY_ATTR, value);
    }

    public void setSize(String value)
    {
        setRendererAttributeString(HTML.SIZE_ATTR, value);
    }

    public void setTabindex(String value)
    {
        setRendererAttributeString(HTML.TABINDEX_ATTR, value);
    }

    // Radio Renderer attributes

    public void setLayout(String value)
    {
        setRendererAttributeString(JSFAttr.LAYOUT_ATTR, value);
    }


    // converter attribute --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag

}

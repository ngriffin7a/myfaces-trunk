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

import net.sourceforge.myfaces.component.UIInput;
import net.sourceforge.myfaces.renderkit.attr.TextRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.TextRenderer;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLInputAttributes;

import javax.faces.component.UIComponent;

/**
 * see "input_text" tag in myfaces_html.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class InputTextTag
    extends MyFacesTag
    implements HTMLInputAttributes,
               TextRendererAttributes
{
    public String getComponentType()
    {
        return "Input";
    }

    public String getRendererType()
    {
        return TextRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIInput attributes

    public void setValue(Object v)
    {
        super.setValue(v);
    }

    public void setInputClass(String v)
    {
        setRendererAttributeString(INPUT_CLASS_ATTR, v);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML input attributes

    public void setAccesskey(String value)
    {
        setRendererAttributeString(ACCESSKEY_ATTR, value);
    }

    public void setAlign(String value)
    {
        setRendererAttributeString(ALIGN_ATTR, value);
    }

    public void setAlt(String value)
    {
        setRendererAttributeString(ALT_ATTR, value);
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

    public void setDisabled(String value)
    {
        setRendererAttributeBoolean(DISABLED_ATTR, value);
    }

    public void setOnblur(String value)
    {
        setRendererAttributeString(ONBLUR_ATTR, value);
    }

    public void setOnchange(String value)
    {
        setRendererAttributeString(ONCHANGE_ATTR, value);
    }

    public void setOnfocus(String value)
    {
        setRendererAttributeString(ONFOCUS_ATTR, value);
    }

    public void setOnselect(String value)
    {
        setRendererAttributeString(ONSELECT_ATTR, value);
    }

    public void setReadonly(String value)
    {
        setRendererAttributeBoolean(READONLY_ATTR, value);
    }

    public void setSize(String value)
    {
        setRendererAttributeString(SIZE_ATTR, value);
    }

    public void setTabindex(String value)
    {
        setRendererAttributeString(TABINDEX_ATTR, value);
    }


    // Text Renderer attributes

    public void setMaxlength(String v)
    {
        setRendererAttributeString(MAX_LENGTH_ATTR, v);
    }

    // converter attribute --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag

}

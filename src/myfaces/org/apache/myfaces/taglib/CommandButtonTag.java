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

import net.sourceforge.myfaces.component.MyFacesUICommand;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.ButtonRenderer;
import net.sourceforge.myfaces.renderkit.html.HTML;


/**
 * see "command_button" tag in myfaces_html.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CommandButtonTag
    extends MyFacesTag
{
    public String getComponentType()
    {
        return "Command";
    }

    public String getRendererType()
    {
        return ButtonRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UICommand attributes

    public void setCommandName(String v)
    {
        setComponentPropertyString(MyFacesUICommand.COMMAND_NAME_PROP, v);
    }

    public void setCommandClass(String v)
    {
        setRendererAttributeString(JSFAttr.COMMAND_CLASS_ATTR, v);
    }

    public void setAction(String v)
    {
        setComponentPropertyString(MyFacesUICommand.ACTION_PROP, v);
    }

    public void setActionRef(String v)
    {
        setComponentPropertyString(MyFacesUICommand.ACTION_REF_PROP, v);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML button attributes

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

    public void setTabindex(String value)
    {
        setRendererAttributeString(HTML.TABINDEX_ATTR, value);
    }



    // Button Renderer attributes

    public void setLabel(String v)
    {
        setRendererAttributeString(JSFAttr.LABEL_ATTR, v);
    }

    public void setType(String value)
    {
        setRendererAttributeString(JSFAttr.TYPE_ATTR, value);
    }


    // key & bundle attributes --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag



    // MyFaces extension
    public void setImmediateAction(String v)
    {
        setComponentPropertyBoolean(MyFacesUICommand.IMMEDIATE_ACTION_PROP, v);
    }

    public void setImage(String value)
    {
        setRendererAttributeString(JSFAttr.IMAGE_ATTR, value);
    }

}

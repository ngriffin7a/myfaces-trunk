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
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HyperlinkRenderer;

import javax.servlet.jsp.JspException;


/**
 * see "command_hyperlink" tag in myfaces_html.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CommandHyperlinkTag
    extends MyFacesBodyTag
{
    public String getComponentType()
    {
        return "Command";
    }

    public String getRendererType()
    {
        return HyperlinkRenderer.TYPE;
    }

    //Iteration Tag support
    public int getDoAfterBodyValue() throws JspException
    {
        return super.getDoAfterBodyValue();
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

    // HTML anchor attributes

    public void setAccesskey(String value)
    {
        setRendererAttributeString(HTML.ACCESSKEY_ATTR, value);
    }

    public void setCharset(String value)
    {
        setRendererAttributeString(HTML.CHARSET_ATTR, value);
    }

    public void setCoords(String value)
    {
        setRendererAttributeString(HTML.COORDS_ATTR, value);
    }

    public void setHreflang(String value)
    {
        setRendererAttributeString(HTML.HREFLANG_ATTR, value);
    }

    public void setRel(String value)
    {
        setRendererAttributeString(HTML.REL_ATTR, value);
    }

    public void setRev(String value)
    {
        setRendererAttributeString(HTML.REV_ATTR, value);
    }

    public void setShape(String value)
    {
        setRendererAttributeString(HTML.SHAPE_ATTR, value);
    }

    public void setTabindex(String value)
    {
        setRendererAttributeString(HTML.TABINDEX_ATTR, value);
    }

    public void setTarget(String value)
    {
        setRendererAttributeString(HTML.TARGET_ATTR, value);
    }

    public void setType(String value)
    {
        setRendererAttributeString(HTML.TYPE_ATTR, value);
    }



    // Hyperlink Renderer attributes

    public void setHref(String v)
    {
        setRendererAttributeString(HTML.HREF_ATTR, v);
    }


    // key and bundle attributes --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag


    // MyFaces extension
    public void setImmediateAction(String v)
    {
        setComponentPropertyBoolean(MyFacesUICommand.IMMEDIATE_ACTION_PROP, v);
    }

}

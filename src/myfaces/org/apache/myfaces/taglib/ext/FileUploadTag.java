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

import net.sourceforge.myfaces.component.ext.UIFileUpload;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLInputAttributes;
import net.sourceforge.myfaces.renderkit.attr.ext.FileUploadRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.ext.FileUploadRenderer;
import net.sourceforge.myfaces.taglib.MyFacesTag;

import javax.faces.component.UIComponent;

/**
 * see "file_upload" tag in myfaces_ext.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FileUploadTag
    extends MyFacesTag
    implements HTMLInputAttributes,
               FileUploadRendererAttributes
{
    public UIComponent createComponent()
    {
        return new UIFileUpload();
    }

    public String getRendererType()
    {
        return FileUploadRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIInput attributes

    public void setValue(Object v)
    {
        super.setValue(v);
    }

    public void setInputClass(String v)
    {
        setRendererAttribute(INPUT_CLASS_ATTR, v);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML input attributes

    public void setAccesskey(String value)
    {
        setRendererAttribute(ACCESSKEY_ATTR, value);
    }

    public void setAlign(String value)
    {
        setRendererAttribute(ALIGN_ATTR, value);
    }

    public void setAlt(String value)
    {
        setRendererAttribute(ALT_ATTR, value);
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

    public void setDisabled(boolean value)
    {
        setRendererAttribute(DISABLED_ATTR, value);
    }

    public void setOnblur(String value)
    {
        setRendererAttribute(ONBLUR_ATTR, value);
    }

    public void setOnchange(String value)
    {
        setRendererAttribute(ONCHANGE_ATTR, value);
    }

    public void setOnfocus(String value)
    {
        setRendererAttribute(ONFOCUS_ATTR, value);
    }

    public void setOnselect(String value)
    {
        setRendererAttribute(ONSELECT_ATTR, value);
    }

    public void setReadonly(boolean value)
    {
        setRendererAttribute(READONLY_ATTR, value);
    }

    public void setReadonly(Boolean value)
    {
        setRendererAttribute(READONLY_ATTR, value);
    }

    public void setSize(String value)
    {
        setRendererAttribute(SIZE_ATTR, value);
    }

    public void setTabindex(int value)
    {
        setRendererAttribute(TABINDEX_ATTR, value);
    }

    public void setTabindex(Integer value)
    {
        setRendererAttribute(TABINDEX_ATTR, value);
    }


    // Text Renderer attributes

    public void setAccept(String value)
    {
        setRendererAttribute(ACCEPT_ATTR, value);
    }

    public void setMaxlength(int v)
    {
        setRendererAttribute(MAX_LENGTH_ATTR, v);
    }

    public void setMaxlength(Integer v)
    {
        setRendererAttribute(MAX_LENGTH_ATTR, v);
    }



    // converter attribute --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag

}

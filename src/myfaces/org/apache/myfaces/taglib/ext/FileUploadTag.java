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
import net.sourceforge.myfaces.renderkit.html.TextRenderer;
import net.sourceforge.myfaces.renderkit.html.ext.FileUploadRenderer;
import net.sourceforge.myfaces.taglib.MyFacesTag;

import javax.faces.component.UIComponent;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FileUploadTag
        extends MyFacesTag
{
    public UIComponent createComponent()
    {
        return new UIFileUpload();
    }

    public String getRendererType()
    {
        return FileUploadRenderer.TYPE;
    }

    public void setInputClass(String value)
    {
        setRendererAttribute(TextRenderer.INPUT_CLASS_ATTR, value);
    }

    public void setAccesskey(String value)
    {
        setRendererAttribute(TextRenderer.ACCESSKEY_ATTR, value);
    }

    public void setAlign(String value)
    {
        setRendererAttribute(TextRenderer.ALIGN_ATTR, value);
    }

    public void setAlt(String value)
    {
        setRendererAttribute(TextRenderer.ALT_ATTR, value);
    }

    public void setDatafld(String value)
    {
        setRendererAttribute(TextRenderer.DATAFLD_ATTR, value);
    }

    public void setDatasrc(String value)
    {
        setRendererAttribute(TextRenderer.DATASRC_ATTR, value);
    }

    public void setDataformatas(String value)
    {
        setRendererAttribute(TextRenderer.DATAFORMATAS_ATTR, value);
    }

    public void setMaxlength(String value)
    {
        setRendererAttribute(TextRenderer.MAX_LENGTH_ATTR, value);
    }

    public void setOnblur(String value)
    {
        setRendererAttribute(TextRenderer.ONBLUR_ATTR, value);
    }

    public void setOnchange(String value)
    {
        setRendererAttribute(TextRenderer.ONCHANGE_ATTR, value);
    }

    public void setOnfocus(String value)
    {
        setRendererAttribute(TextRenderer.ONFOCUS_ATTR, value);
    }

    public void setOnselect(String value)
    {
        setRendererAttribute(TextRenderer.ONSELECT_ATTR, value);
    }

    public void setReadonly(String value)
    {
        setRendererAttribute(TextRenderer.READONLY_ATTR, value);
    }

    public void setSize(String value)
    {
        setRendererAttribute(TextRenderer.SIZE_ATTR, value);
    }

    public void setTabindex(String value)
    {
        setRendererAttribute(TextRenderer.TABINDEX_ATTR, value);
    }



    public void setAccept(String value)
    {
        setRendererAttribute(FileUploadRenderer.ACCEPT_ATTR, value);
    }

    public void setMaxUploadSize(int maxSize)
    {
        setRendererAttribute(FileUploadRenderer.MAX_UPLOAD_SIZE_ATTR, new Integer(maxSize));
    }

    public void setContentTypeReference(String value)
    {
        setComponentAttribute(UIFileUpload.CONTENT_TYPE_REFERENCE_ATTR, value);
    }


}

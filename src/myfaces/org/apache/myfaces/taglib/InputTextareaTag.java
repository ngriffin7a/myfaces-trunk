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
import net.sourceforge.myfaces.renderkit.html.TextareaRenderer;
import net.sourceforge.myfaces.taglib.common.*;

import javax.faces.component.UIComponent;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class InputTextareaTag
    extends MyFacesTag
    implements CommonAttributes,
               UIInputAttributes,
               HTMLTextareaAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes
{
    public UIComponent createComponent()
    {
        return new UIInput();
    }

    public String getRendererType()
    {
        return TextareaRenderer.TYPE;
    }

    public void setInputClass(String value)
    {
        setRendererAttribute(TextareaRenderer.INPUT_CLASS_ATTR, value);
    }

    public void setAccesskey(String value)
    {
        setRendererAttribute(TextareaRenderer.ACCESSKEY_ATTR, value);
    }

    public void setCols(int value)
    {
        setRendererAttribute(TextareaRenderer.COLS_ATTR, value);
    }

    public void setDatafld(String value)
    {
        setRendererAttribute(TextareaRenderer.DATAFLD_ATTR, value);
    }

    public void setDatasrc(String value)
    {
        setRendererAttribute(TextareaRenderer.DATASRC_ATTR, value);
    }

    public void setDataformatas(String value)
    {
        setRendererAttribute(TextareaRenderer.DATAFORMATAS_ATTR, value);
    }

    public void setDisabled(boolean value)
    {
        setRendererAttribute(TextareaRenderer.DISABLED_ATTR, value);
    }

    public void setOnblur(String value)
    {
        setRendererAttribute(TextareaRenderer.ONBLUR_ATTR, value);
    }

    public void setOnchange(String value)
    {
        setRendererAttribute(TextareaRenderer.ONCHANGE_ATTR, value);
    }

    public void setOnfocus(String value)
    {
        setRendererAttribute(TextareaRenderer.ONFOCUS_ATTR, value);
    }

    public void setOnselect(String value)
    {
        setRendererAttribute(TextareaRenderer.ONSELECT_ATTR, value);
    }

    public void setReadonly(boolean value)
    {
        setRendererAttribute(TextareaRenderer.READONLY_ATTR, value);
    }

    public void setRows(int value)
    {
        setRendererAttribute(TextareaRenderer.ROWS_ATTR, value);
    }

    public void setTabindex(int value)
    {
        setRendererAttribute(TextareaRenderer.TABINDEX_ATTR, value);
    }


    public void setText(String value)
    {
        setValue(value);
    }

}

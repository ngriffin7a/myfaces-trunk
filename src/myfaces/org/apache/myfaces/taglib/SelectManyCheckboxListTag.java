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

import net.sourceforge.myfaces.component.UISelectMany;
import net.sourceforge.myfaces.renderkit.html.CheckboxRenderer;

import javax.faces.component.UIComponent;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectManyCheckboxListTag
    extends MyFacesTag
{
    public UIComponent createComponent()
    {
        return new UISelectMany();
    }

    public String getRendererType()
    {
        return CheckboxRenderer.TYPE;
    }

    public void setChecked(Boolean value)
    {
        setValue(value);
    }

    public void setKey(String value)
    {
        setRendererAttribute(CheckboxRenderer.KEY_ATTR.getName(), value);
    }

    public void setBundle(String value)
    {
        setRendererAttribute(CheckboxRenderer.BUNDLE_ATTR, value);
    }

    public void setSelectmanyClass(String value)
    {
        setRendererAttribute(CheckboxRenderer.SELECT_BOOLEAN_CLASS_ATTR, value);
    }

    public void setAccesskey(String value)
    {
        setRendererAttribute(CheckboxRenderer.ACCESSKEY_ATTR, value);
    }

    public void setAlign(String value)
    {
        setRendererAttribute(CheckboxRenderer.ALIGN_ATTR, value);
    }

    public void setAlt(String value)
    {
        setRendererAttribute(CheckboxRenderer.ALT_ATTR, value);
    }

    public void setDatafld(String value)
    {
        setRendererAttribute(CheckboxRenderer.DATAFLD_ATTR, value);
    }

    public void setDatasrc(String value)
    {
        setRendererAttribute(CheckboxRenderer.DATASRC_ATTR, value);
    }

    public void setDataformatas(String value)
    {
        setRendererAttribute(CheckboxRenderer.DATAFORMATAS_ATTR, value);
    }

    public void setDisabled(Boolean value)
    {
        setRendererAttribute(CheckboxRenderer.DISABLED_ATTR, value);
    }

    public void setOnblur(String value)
    {
        setRendererAttribute(CheckboxRenderer.ONBLUR_ATTR, value);
    }

    public void setOnchange(String value)
    {
        setRendererAttribute(CheckboxRenderer.ONCHANGE_ATTR, value);
    }

    public void setOnfocus(String value)
    {
        setRendererAttribute(CheckboxRenderer.ONFOCUS_ATTR, value);
    }

    public void setOnselect(String value)
    {
        setRendererAttribute(CheckboxRenderer.ONSELECT_ATTR, value);
    }

    public void setReadonly(Boolean value)
    {
        setRendererAttribute(CheckboxRenderer.READONLY_ATTR, value);
    }

    public void setTabindex(String value)
    {
        setRendererAttribute(CheckboxRenderer.TABINDEX_ATTR, value);
    }

    public void setValue(String value)
    {
        setRendererAttribute(CheckboxRenderer.CB_VALUE_ATTR, value);
    }
}

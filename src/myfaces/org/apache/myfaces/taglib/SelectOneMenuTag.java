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

import net.sourceforge.myfaces.component.UISelectOne;
import net.sourceforge.myfaces.renderkit.html.MenuRenderer;
import net.sourceforge.myfaces.renderkit.attr.HTMLSelectAttributes;
import net.sourceforge.myfaces.renderkit.attr.MenuRendererAttributes;

import javax.faces.component.UIComponent;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectOneMenuTag
    extends MyFacesTag
    implements HTMLSelectAttributes,
               MenuRendererAttributes
{
    public UIComponent createComponent()
    {
        return new UISelectOne();
    }

    public String getRendererType()
    {
        return MenuRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UISelectOne attributes

    public void setValue(Object v)
    {
        super.setValue(v);
    }

    public void setSelectOneClass(String value)
    {
        setRendererAttribute(SELECT_ONE_CLASS_ATTR, value);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // HTML select attributes

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

    public void setDisabled(Boolean value)
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

    public void setTabindex(int value)
    {
        setRendererAttribute(TABINDEX_ATTR, value);
    }

    public void setTabindex(Integer value)
    {
        setRendererAttribute(TABINDEX_ATTR, value);
    }

    // Menu Renderer attributes

    public void setSize(String value)
    {
        setRendererAttribute(SIZE_ATTR, value);
    }

    // user role attributes --> already implemented in MyFacesTag

}

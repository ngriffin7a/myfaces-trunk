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
import net.sourceforge.myfaces.renderkit.html.MenuRenderer;

import javax.faces.component.UIComponent;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectOneMenuTag
    extends MyFacesTag
{
    public UIComponent createComponent()
    {
        return new UISelectOne();
    }

    public String getRendererType()
    {
        return MenuRenderer.TYPE;
    }

    public void setSize(String value)
    {
        setRendererAttribute(MenuRenderer.SIZE_ATTR, value);
    }

    public void setDatafld(String value)
    {
        setRendererAttribute(MenuRenderer.DATAFLD_ATTR, value);
    }

    public void setDatasrc(String value)
    {
        setRendererAttribute(MenuRenderer.DATASRC_ATTR, value);
    }

    public void setDataformatas(String value)
    {
        setRendererAttribute(MenuRenderer.DATAFORMATAS_ATTR, value);
    }

    public void setDisabled(String value)
    {
        setRendererAttribute(MenuRenderer.DISABLED_ATTR, value);
    }

    public void setOnblur(String value)
    {
        setRendererAttribute(MenuRenderer.ONBLUR_ATTR, value);
    }

    public void setOnchange(String value)
    {
        setRendererAttribute(MenuRenderer.ONCHANGE_ATTR, value);
    }

    public void setOnfocus(String value)
    {
        setRendererAttribute(MenuRenderer.ONFOCUS_ATTR, value);
    }

    public void setTabindex(String value)
    {
        setRendererAttribute(MenuRenderer.TABINDEX_ATTR, value);
    }

}

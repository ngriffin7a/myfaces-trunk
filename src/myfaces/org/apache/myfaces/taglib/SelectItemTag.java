/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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

import net.sourceforge.myfaces.component.MyFacesComponent;
import net.sourceforge.myfaces.component.UISelectItem;

import javax.faces.component.UIComponent;

/**
 * TODO: description
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class SelectItemTag
    extends MyFacesTag
{
    public UIComponent createComponent()
    {
        return new UISelectItem();
    }

    public String getRendererType()
    {
        return null;
    }

    public void setSelected(String v)
    {
        addRequestTimeValue(UISelectItem.SELECTED_ATTR, v);
    }

    public void setDescription(String v)
    {
        addRequestTimeValue(UISelectItem.DESCRIPTION_ATTR, v);
    }

    public void setLabel(String v)
    {
        addRequestTimeValue(MyFacesComponent.LABEL_ATTR, v);
    }
}

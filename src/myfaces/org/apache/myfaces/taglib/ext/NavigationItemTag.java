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

import net.sourceforge.myfaces.component.ext.UINavigation;
import net.sourceforge.myfaces.renderkit.html.ext.NavigationItemRenderer;
import net.sourceforge.myfaces.renderkit.attr.ext.NavigationItemRendererAttributes;
import net.sourceforge.myfaces.taglib.MyFacesTag;

import javax.faces.component.UIComponent;

/**
 * see "navigation_item" tag in myfaces_ext.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationItemTag
    extends MyFacesTag
    implements NavigationItemRendererAttributes
{
    public UIComponent createComponent()
    {
        return new UINavigation.UINavigationItem();
    }

    public String getRendererType()
    {
        return NavigationItemRenderer.TYPE;
    }

    public void setLabel(String s)
    {
        setRendererAttributeString(LABEL_ATTR, s);
    }

    // key and bundle attributes --> already implemented in MyFacesTag

    public void setTreeId(String s)
    {
        setRendererAttributeString(NavigationItemRenderer.TREE_ID_ATTR, s);
    }

    public void setOpen(String b)
    {
        setComponentPropertyBoolean(UINavigation.UINavigationItem.OPEN_ATTR, b);
    }

    // user role attributes --> already implemented in MyFacesTag
}

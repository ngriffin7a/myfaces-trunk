/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.custom.navmenu;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/06/23 13:44:31  royalts
 *          no message
 *
 */
public class NavigationMenuUtils
{
    private static final Log log = LogFactory.getLog(NavigationMenuUtils.class);

    public static List getNavigationMenuItemList(UIComponent uiComponent)
    {
        List list = new ArrayList(uiComponent.getChildCount());
        for (Iterator children = uiComponent.getChildren().iterator(); children.hasNext(); )
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UINavigationMenuItem)
            {
                NavigationMenuItem item;
                Object value = ((UINavigationMenuItem)child).getValue();
                if (value != null)
                {
                    //get NavigationMenuItem from model via value binding
                    if (!(value instanceof NavigationMenuItem))
                    {
                        FacesContext facesContext = FacesContext.getCurrentInstance();
                        throw new IllegalArgumentException("Value binding of UINavigationMenuItem with id " + child.getClientId(facesContext) + " does not reference an Object of type NavigationMenuItem");
                    }
                    item = (NavigationMenuItem)value;
                }
                else
                {
                    UINavigationMenuItem uiItem = (UINavigationMenuItem)child;
                    String label = uiItem.getItemLabel();
                    if (label == null && uiItem.getItemValue() != null)
                    {
                        label = uiItem.getItemValue().toString();
                    }
                    item = new NavigationMenuItem(uiItem.getItemValue(),
                                                  label,
                                                  uiItem.getItemDescription(),
                                                  uiItem.isItemDisabled(),
                                                  uiItem.getAction(),
                                                  uiItem.getIcon(),
                                                  uiItem.isSplit());
                }
                list.add(item);
                if (child.getChildCount() > 0)
                {
                    item.setChildren(getNavigationMenuItemList(child));
                }
            }
            else if (child instanceof UISelectItems)
            {
                Object value = ((UISelectItems)child).getValue();
                if (value instanceof NavigationMenuItem)
                {
                    list.add(value);
                }
                else if (value instanceof NavigationMenuItem[])
                {
                    for (int i = 0; i < ((NavigationMenuItem[])value).length; i++)
                    {
                        list.add(((NavigationMenuItem[])value)[i]);
                    }
                }
                else if (value instanceof Collection)
                {
                    for (Iterator it = ((Collection)value).iterator(); it.hasNext();)
                    {
                        Object item = it.next();
                        if (!(item instanceof NavigationMenuItem))
                        {
                            FacesContext facesContext = FacesContext.getCurrentInstance();
                            throw new IllegalArgumentException("Collection referenced by UINavigationMenuItems with id " + child.getClientId(facesContext) + " does not contain Objects of type NavigationMenuItem");
                        }
                        list.add(item);
                    }
                }
                else
                {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    throw new IllegalArgumentException("Value binding of UINavigationMenuItems with id " + child.getClientId(facesContext) + " does not reference an Object of type NavigationMenuItem, NavigationMenuItem[], Collection or Map");
                }
            }
            else
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                log.error("Invalid child with id " + child.getClientId(facesContext) + "of component with id : "+uiComponent.getClientId(facesContext)
                        +" : must be UINavigationMenuItem or UINavigationMenuItems, is of type : "+((child==null)?"null":child.getClass().getName()));
            }
        }

        return list;
    }

}

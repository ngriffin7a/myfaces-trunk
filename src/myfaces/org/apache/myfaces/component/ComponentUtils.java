/*
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
package net.sourceforge.myfaces.component;

import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentUtils
{
    //private static final Log log = LogFactory.getLog(ComponentUtils.class);

    /**
     * Utility method to get the model SelectItem that a UISelectItem component represents.
     */
    public static SelectItem getSelectItemFromUISelectItem(UISelectItem uiSelectItem)
    {
        SelectItem selectItem = (SelectItem)uiSelectItem.getValue();
        if (selectItem == null)
        {
            selectItem = new SelectItem(uiSelectItem.getItemValue(),
                                        uiSelectItem.getItemLabel(),
                                        uiSelectItem.getItemDescription(),
                                        MyFacesUISelectItem.isDisabled(uiSelectItem));
        }
        return selectItem;
    }

    /**
     * Utility method to add model SelectItems that a UISelectItems represents to a Collection.
     */
    public static void addSelectItemsToCollection(UISelectItems uiSelectItems,
                                                  Collection collection)
    {
        Object value = uiSelectItems.getValue();
        if (value == null)
        {
            return;
        }
        else if (value instanceof SelectItem)
        {
            collection.add(value);
        }
        else if (value instanceof SelectItem[])
        {
            SelectItem items[] = (SelectItem[])value;
            for (int i = 0, len = items.length; i < len; i++)
            {
                collection.add(items[i]);
            }
        }
        else if (value instanceof Collection)
        {
            collection.addAll((Collection)value);
        }
        else if (value instanceof Iterator)
        {
            Iterator it = (Iterator)value;
            while (it.hasNext())
            {
                collection.add(it.next());
            }
        }
        // MyFaces extension: Map is also supported.
        // (entry.key => value, entry.value => label and description)
        else if (value instanceof Map)
        {
            for (Iterator it = ((Map)value).entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = (Map.Entry)it.next();
                String label = (String)entry.getValue();
                collection.add(new SelectItem(entry.getKey(),
                                              label,
                                              label));
            }
        }
    }
}

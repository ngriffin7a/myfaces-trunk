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
package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.component.MyFacesUISelectItem;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectItemUtil
{
    public static final String LIST_ATTR = SelectItemUtil.class.getName() + ".LIST";

    public static void addSelectItems(FacesContext facesContext, UISelectItems uiSelectItems, List list)
    {
        //FIXME
        //Object value = uiSelectItems.currentValue(facesContext);
        Object value = null;
        if (value instanceof SelectItem)
        {
            list.add(value);
        }
        else if (value instanceof SelectItem[])
        {
            SelectItem items[] = (SelectItem[])value;
            for (int i = 0; i < items.length; i++)
            {
                list.add(items[i]);
            }
        }
        else if (value instanceof Collection)
        {
            list.addAll((Collection)value);
        }
        else if (value instanceof Iterator)
        {
            Iterator it = (Iterator)value;
            while (it.hasNext())
            {
                list.add(it.next());
            }
        }
        // MyFaces extension: Map is also supported.
        // (entry.key => value, entry.value => label and description)
        else if (value instanceof Map)
        {
            for (Iterator it = ((Map)value).entrySet().iterator();
                 it.hasNext();)
            {
                Map.Entry entry = (Map.Entry)it.next();
                String label = (String)entry.getValue();
                list.add(new SelectItem(entry.getKey(),
                                        label,
                                        label));
            }
        }
    }


    public static void addSelectItem(FacesContext facesContext, UISelectItem uiSelectItem, List list)
    {
        list.add(getSelectItem(facesContext, uiSelectItem));

    }

    public static SelectItem getSelectItem(FacesContext facesContext, UISelectItem uiSelectItem)
    {
        String text;
        if (uiSelectItem instanceof MyFacesUISelectItem)
        {
            String key = ((MyFacesUISelectItem)uiSelectItem).getItemKey();
            if (key != null)
            {
                text = BundleUtils.getString(facesContext,
                                             ((MyFacesUISelectItem)uiSelectItem).getItemBundle(),
                                             key);
            }
            else
            {
                text = uiSelectItem.getItemLabel();
            }
        }
        else
        {
            text = uiSelectItem.getItemLabel();
        }

        String itemValue = uiSelectItem.getItemValue();
        return new SelectItem(itemValue,
                              text,
                              uiSelectItem.getItemDescription());
    }

    private static List getSelectItemsList(FacesContext facesContext,
                                           UIComponent uiComponent)
    {
        ArrayList list = (ArrayList)uiComponent.getAttributes().get(LIST_ATTR);
        if (list != null)
        {
            return list;
        }

        list = new ArrayList(uiComponent.getChildCount());
        for(Iterator children = uiComponent.getChildren().iterator(); children.hasNext();)
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UISelectItem)
            {
                addSelectItem(facesContext, (UISelectItem)child, list);
            }
            else if (child instanceof UISelectItems)
            {
                addSelectItems(facesContext, (UISelectItems)child, list);
            }
        }
        return list;
    }

    public static int getSelectItemsCount(FacesContext context, UIComponent component)
    {
        return getSelectItemsList(context, component).size();
    }

    public static Iterator getSelectItems(FacesContext context, UIComponent component)
    {
        return getSelectItemsList(context, component).iterator();
    }


    public static Set getSelectedValuesAsStringSet(FacesContext facesContext,
                                                   UISelectMany uiSelectMany)
    {
        //FIXME
        //Object[] selectedValues = (Object[])uiSelectMany.currentValue(facesContext);
        Object[] selectedValues = null;
        if (selectedValues == null || selectedValues.length == 0)
        {
            return Collections.EMPTY_SET;
        }

        Set set = new HashSet();

        for (int i = 0; i < selectedValues.length; i++)
        {
            Object objValue = selectedValues[i];
            /*
            String strValue = ConverterUtils.getComponentValueAsString(facesContext,
                                                                       uiSelectMany,
                                                                       objValue);
                                                                       */
            String strValue = (objValue != null
                                ? objValue.toString()
                                : null);
            set.add(strValue);
        }

        return set;
    }

}

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
package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.component.UISelectItem;
import net.sourceforge.myfaces.component.UISelectItems;
import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectItemHelper
{
    public static final String LIST_ATTR = SelectItemHelper.class.getName() + ".LIST";

    /**
     *
     * @param facesContext
     * @param uiComponent
     * @param currentValue = uiComponent.currentValue
     * @param item
     * @return
     */
    public static boolean isItemSelected(FacesContext facesContext, UIComponent uiComponent, Object currentValue, SelectItem item)
    {
        Object itemValue = item.getValue();
        if (itemValue != null && currentValue != null)
        {
            if (currentValue instanceof Object[])
            {
                for (int i = 0; i < ((Object[])currentValue).length; i++)
                {
                    Object currentValueObj = ((Object[])currentValue)[i];
                    if (currentValueObj instanceof String &&
                        !(itemValue instanceof String) &&
                        itemValue.toString().equals(currentValueObj))
                    {
                        return true;
                    }
                    else
                    {
                        Converter converter = ConverterUtils.findConverter(currentValueObj.getClass());
                        if (converter != null)
                        {
                            Object convObj = converter.getAsObject(facesContext, uiComponent, currentValueObj.toString());
                            if (itemValue.equals(convObj))
                            {
                                return true;
                            }
                        }
                        else if (itemValue.equals(currentValueObj))
                        {
                            return true;
                        }
                    }
                }
            }
            else
            {
                Converter converter = ConverterUtils.findConverter(currentValue.getClass());
                if (converter != null)
                {
                    Object convObj = converter.getAsObject(facesContext, uiComponent, currentValue.toString());
                    if (itemValue.equals(convObj))
                    {
                        return true;
                    }
                }
                else if (itemValue.equals(currentValue))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getSelectItemsCount(FacesContext context, UIComponent component)
    {
        return getSelectItemsList(context, component).size();
    }

    public static Iterator getSelectItems(FacesContext context, UIComponent component)
    {
        return getSelectItemsList(context, component).iterator();
    }

    private static ArrayList getSelectItemsList(FacesContext facesContext, UIComponent uiComponent)
    {
        ArrayList list = (ArrayList)uiComponent.getAttribute(LIST_ATTR);
        if (list == null)
        {
            list = new ArrayList(uiComponent.getChildCount());
            for(Iterator children = uiComponent.getChildren(); children.hasNext();)
            {
                UIComponent child = (UIComponent)children.next();
                if (child instanceof UISelectItem)
                {
                    UISelectItem item = (UISelectItem)child;
                    String key = item.getItemKey();
                    String text;
                    if (key != null)
                    {
                        text = BundleUtils.getString(facesContext,
                                                     item.getItemBundle(),
                                                     key);
                    }
                    else
                    {
                        text = item.getItemLabel();
                    }

                    Object itemValue = item.getItemValue();
                    if (itemValue == null)
                    {
                        itemValue = item.currentValue(facesContext);
                    }

                    list.add(new SelectItem(itemValue,
                                            text,
                                            item.getItemDescription()));
                }
                else if (child instanceof UISelectItems)
                {
                    Object value = child.currentValue(facesContext);
                    if (value instanceof UISelectItem)
                    {
                        list.add(value);
                    }
                    else if (value instanceof SelectItem[])
                    {
                        SelectItem items[] = (SelectItem[])value;
                        for(int i = 0; i < items.length; i++)
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
                    // TODO: add Collection / remove Map ?? (see API-Doku)
                    else if(value instanceof Map)
                    {
                        Iterator keys = ((Map)value).keySet().iterator();
                        while (keys.hasNext())
                        {
                            Object key = keys.next();
                            if(key != null)
                            {
                                Object label = ((Map)value).get(key);
                                if(label != null)
                                {
                                    SelectItem item = new SelectItem(key.toString(), label.toString(), null);
                                    list.add(item);
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

}

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

import net.sourceforge.myfaces.renderkit.RendererUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ComponentUtils
{
    private static final Log log = LogFactory.getLog(ComponentUtils.class);

    /**
     * Utility method to get the model SelectItem that a UISelectItem component represents.
     */
    public static SelectItem getSelectItemFromUISelectItem(UISelectItem uiSelectItem)
    {
        Object v = uiSelectItem.getValue();
        if (v != null)
        {
            if (!(v instanceof SelectItem))
            {
                throw new IllegalArgumentException("Value of UISelectItem with id " + uiSelectItem.getClientId(FacesContext.getCurrentInstance()) + " is not of type SelectItem, perhaps you used value instead of itemValue by mistake?");
            }
            return (SelectItem)v;
        }
        else
        {
            Converter converter;
            UIComponent parent = uiSelectItem.getParent();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (parent instanceof UISelectMany)
            {
                converter = RendererUtils.findUISelectManyConverter(facesContext,
                                                                    (UISelectMany)parent);
            }
            else if (parent instanceof UISelectOne)
            {
                converter = RendererUtils.findUIOutputConverter(facesContext,
                                                                (UISelectOne)parent);
            }
            else
            {
                log.error("UISelectItem with id " + uiSelectItem.getClientId(facesContext) + " not nested within UISelectOne or UISelectMany");
                converter = null;
            }

            Object convertedValue;
            if (converter == null)
            {
                convertedValue = uiSelectItem.getItemValue();
            }
            else
            {
                convertedValue = converter.getAsObject(facesContext, uiSelectItem, uiSelectItem.getItemValue());
            }

            return new SelectItem(convertedValue,
                                  uiSelectItem.getItemLabel(),
                                  uiSelectItem.getItemDescription(),
                                  MyFacesUISelectItem.isDisabled(uiSelectItem));
        }
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
        else
        {
            throw new IllegalArgumentException("Unsupported UISelectItems value of type " + value.getClass().getName());
        }
    }

}

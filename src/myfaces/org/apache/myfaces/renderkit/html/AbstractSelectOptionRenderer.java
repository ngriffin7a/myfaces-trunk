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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.component.UISelectItem;
import net.sourceforge.myfaces.component.UISelectItems;
import net.sourceforge.myfaces.component.UISelectMany;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * TODO: description
 * @author Thomas Spiegl (latest modification by Author)
 * @version $Revision$ $Date$
 */
public abstract class AbstractSelectOptionRenderer
        extends HTMLRenderer
{
    public static final String LIST_ATTR = AbstractSelectOptionRenderer.class.getName() + ".LIST";

    public void encodeBegin(FacesContext context, UIComponent uicomponent)
            throws IOException
    {
    }

    public void encodeEnd(FacesContext facescontext, UIComponent uicomponent, int size)
            throws IOException
    {
        ResponseWriter writer = facescontext.getResponseWriter();

        boolean multipleSelect = uicomponent.getComponentType() == UISelectMany.TYPE ? true : false;

        Iterator it = getSelectItems(facescontext, uicomponent);
        if (it.hasNext())
        {
            writer.write("<select ");
            writer.write(" name=\"");
            writer.write(uicomponent.getCompoundId());
            writer.write("\"");

            if (size > 0)
            {
                writer.write(" size=\"");
                writer.write(new Integer(size).toString());
                writer.write("\"");
            }

            if (multipleSelect) writer.write(" multiple ");
            writer.write(">\n");

            Object currentValue = uicomponent.currentValue(facescontext);

            while (it.hasNext())
            {
                SelectItem item = (SelectItem)it.next();
                writer.write("\t\t<option");
                Object value = item.getValue();
                if (value != null)
                {
                    String str = value.toString();
                    writer.write(" value=\"");
                    writer.write(HTMLEncoder.encode(str, false, false));
                    writer.write("\"");
                    if (isItemSelected(currentValue, item))
                    {
                        writer.write(" selected");
                    }
                }
                writer.write(">");
                writer.write(HTMLEncoder.encode(item.getLabel(), true, true));
                writer.write("</option>\n");
            }

            writer.write("</select>");
        }
    }

    public boolean isItemSelected(Object currentValue, SelectItem item)
    {
        Object itemValue = item.getValue();
        if (itemValue != null && currentValue != null)
        {
            if (currentValue instanceof Object[])
            {
                for (int i = 0; i < ((Object[])currentValue).length; i++)
                {
                    Object obj = ((Object[])currentValue)[i];
                    if (itemValue.equals(obj))
                    {
                        return true;
                    }
                }
            }
            else
            {
                if (itemValue.equals(currentValue))
                {
                    return true;
                }
            }
        }
        return false;
    }

    protected int getSelectItemsCount(FacesContext context, UIComponent component)
    {
        return getSelectItemsList(context, component).size();
    }

    protected Iterator getSelectItems(FacesContext context, UIComponent component)
    {
        return getSelectItemsList(context, component).iterator();
    }

    private ArrayList getSelectItemsList(FacesContext context, UIComponent component)
    {
        ArrayList list = (ArrayList)component.getAttribute(LIST_ATTR);
        if (list == null)
        {
            list = new ArrayList(component.getChildCount());
            for(Iterator children = component.getChildren(); children.hasNext();)
            {
                UIComponent child = (UIComponent)children.next();
                if (child instanceof UISelectItem)
                {
                    UISelectItem item = (UISelectItem)child;
                    list.add(new SelectItem(item.getItemValue(),
                                            item.getItemLabel(),
                                            item.getItemDescription()));
                }
                else if (child instanceof UISelectItems)
                {
                    Object value = child.currentValue(context);
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
                        Iterator it = ((Collection)value).iterator();
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

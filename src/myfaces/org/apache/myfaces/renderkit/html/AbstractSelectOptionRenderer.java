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
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.renderkit.attr.ListboxRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.MenuRendererAttributes;
import net.sourceforge.myfaces.util.bundle.BundleUtils;
import net.sourceforge.myfaces.convert.ConverterUtils;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import java.io.IOException;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
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

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent, int size, String rendererType)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        boolean multipleSelect = uiComponent.getComponentType() == UISelectMany.TYPE ? true : false;

        Iterator it = getSelectItems(facesContext, uiComponent);
        if (it.hasNext())
        {
            writer.write("<select");
            writer.write(" name=\"");
            writer.write(uiComponent.getClientId(facesContext));
            writer.write("\"");

            CommonAttributes.renderHTMLEventHandlerAttributes(facesContext, uiComponent);
            CommonAttributes.renderUniversalHTMLAttributes(facesContext, uiComponent);
            if (rendererType.equals(ListboxRenderer.TYPE))
            {
                writer.write(" size=\"");
                writer.write(new Integer(size).toString());
                writer.write("\"");
                CommonAttributes.renderAttributes(facesContext,
                                                  uiComponent,
                                                  ListboxRendererAttributes.COMMON_LISTBOX_ATTRIBUTES);
            }
            else if (rendererType.equals(MenuRenderer.TYPE))
            {
                CommonAttributes.renderAttributes(facesContext,
                                                  uiComponent,
                                                  MenuRendererAttributes.COMMON_MENU_ATTRIBUTES);
            }
            else
            {
                throw new IllegalArgumentException("Unknown renderer-type " + rendererType);
            }

            if (multipleSelect) writer.write(" multiple ");
            writer.write(">\n");

            Object currentValue = uiComponent.currentValue(facesContext);

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
                    if (isItemSelected(facesContext, uiComponent, currentValue, item))
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

    public boolean isItemSelected(FacesContext facesContext, UIComponent uiComponent, Object currentValue, SelectItem item)
    {
        Object itemValue = item.getValue();
        if (itemValue != null && currentValue != null)
        {
            Converter converter = ConverterUtils.findConverter(itemValue.getClass());
            if (converter != null)
            {
                Object convObj = converter.getAsObject(facesContext, uiComponent, currentValue.toString());
                if (itemValue.equals(convObj))
                {
                    return true;
                }
            }
            else if (currentValue instanceof Object[])
            {
                for (int i = 0; i < ((Object[])currentValue).length; i++)
                {
                    Object obj = ((Object[])currentValue)[i];
                    converter = ConverterUtils.findConverter(obj.getClass());
                    if (converter != null)
                    {
                        Object convObj = converter.getAsObject(facesContext, uiComponent, obj.toString());
                        if (itemValue.equals(convObj))
                        {
                            return true;
                        }
                    }
                    else if (itemValue.equals(obj))
                    {
                        return true;
                    }
                }
            }
            else if (itemValue.equals(currentValue))
            {
                return true;
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
                    String key = item.getItemKey();
                    String text;
                    if (key != null)
                    {
                        text = BundleUtils.getString(context,
                                                     item.getItemBundle(),
                                                     key);
                    }
                    else
                    {
                        text = item.getItemLabel();
                    }

                    list.add(new SelectItem(item.getItemValue(),
                                            text,
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

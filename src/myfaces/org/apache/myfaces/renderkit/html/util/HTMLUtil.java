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

import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.ListboxRenderer;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLSelectAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Utility methods for rendering HTML tags.
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HTMLUtil
{
    private HTMLUtil() {}   //Utility class


    public static void renderHTMLAttribute(StringBuffer buf,
                                           UIComponent component,
                                           String rendererAttrName,
                                           String htmlAttrName)
        throws IOException
    {
        Object value = component.getAttribute(rendererAttrName);
        if (value != null)
        {
            if (value instanceof Boolean &&
                ((Boolean)value).booleanValue())
            {
                buf.append(" ");
                buf.append(htmlAttrName);
            }
            else
            {
                buf.append(" ");
                buf.append(htmlAttrName);
                buf.append("=\"");
                buf.append(value.toString());
                buf.append("\"");
            }
        }
    }

    public static void renderHTMLAttribute(ResponseWriter writer,
                                           UIComponent component,
                                           String rendererAttrName,
                                           String htmlAttrName)
        throws IOException
    {
        Object value = component.getAttribute(rendererAttrName);
        if (value != null)
        {
            if (value instanceof Boolean &&
                ((Boolean)value).booleanValue())
            {
                writer.write(" ");
                writer.write(htmlAttrName);
            }
            else
            {
                writer.write(" ");
                writer.write(htmlAttrName);
                writer.write("=\"");
                writer.write(value.toString());
                writer.write("\"");
            }
        }
    }

    public static void renderHTMLAttributes(StringBuffer buf,
                                            UIComponent component,
                                            String[] attributes)
        throws IOException
    {
        for (int i = 0; i < attributes.length; i++)
        {
            String attrName = attributes[i];
            renderHTMLAttribute(buf,
                                component,
                                attrName,
                                attrName);
        }
    }

    public static void renderHTMLAttributes(ResponseWriter writer,
                                            UIComponent component,
                                            String[] attributes)
        throws IOException
    {
        for (int i = 0; i < attributes.length; i++)
        {
            String attrName = attributes[i];
            renderHTMLAttribute(writer,
                                component,
                                attrName,
                                attrName);
        }
    }

    public static void renderCssClass(StringBuffer buf,
                                      UIComponent uiComponent,
                                      String classAttrName)
    {
        String cssClass = (String)uiComponent.getAttribute(classAttrName);
        if (cssClass != null)
        {
            buf.append(" class=\"");
            buf.append(cssClass);
            buf.append("\"");
        }
    }

    public static void renderCssClass(ResponseWriter writer,
                                      UIComponent uiComponent,
                                      String classAttrName)
        throws IOException
    {
        String cssClass = (String)uiComponent.getAttribute(classAttrName);
        if (cssClass != null)
        {
            writer.write(" class=\"");
            writer.write(cssClass);
            writer.write("\"");
        }
    }

    public static void renderDisabledOnUserRole(FacesContext facesContext,
                                                UIComponent uiComponent)
        throws IOException
    {
        if (!HTMLRenderer.isEnabledOnUserRole(facesContext, uiComponent))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.write(" disabled");
        }
    }

    public static void renderSelect(FacesContext facesContext,
                                    UIComponent uiComponent,
                                    String rendererType,
                                    int size)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        boolean selectMany = (uiComponent instanceof UISelectMany);

        writer.write("<select");
        writer.write(" name=\"");
        writer.write(uiComponent.getClientId(facesContext));
        writer.write("\"");

        if (rendererType.equals(ListboxRenderer.TYPE))
        {
            writer.write(" size=\"");
            writer.write(new Integer(size).toString());
            writer.write("\"");
        }

        renderCssClass(writer, uiComponent, selectMany
                                             ? CommonRendererAttributes.SELECT_MANY_CLASS_ATTR
                                             : CommonRendererAttributes.SELECT_ONE_CLASS_ATTR);
        renderHTMLAttributes(writer, uiComponent, HTMLUniversalAttributes.HTML_UNIVERSAL_ATTRIBUTES);
        renderHTMLAttributes(writer, uiComponent, HTMLEventHandlerAttributes.HTML_EVENT_HANDLER_ATTRIBUTES);
        renderHTMLAttributes(writer, uiComponent, HTMLSelectAttributes.HTML_SELECT_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(facesContext, uiComponent);

        if (selectMany) writer.write(" multiple ");
        writer.write(">\n");

        Iterator it = SelectItemUtil.getSelectItems(facesContext, uiComponent);
        if (it.hasNext())
        {
            String currentStrValue = null;
            Set selectedValuesSet = null;
            if (selectMany)
            {
                selectedValuesSet
                    = SelectItemUtil.getSelectedValuesAsStringSet(facesContext,
                                                                  (UISelectMany)uiComponent);
            }
            else
            {
                Object currentValue = ((UIInput)uiComponent).currentValue(facesContext);
                /*
                currentStrValue = ConverterUtils.getComponentValueAsString(facesContext,
                                                                           uiComponent,
                                                                           currentValue);
                                                                           */
                currentStrValue = (currentValue != null
                                    ? currentValue.toString()
                                    : null);
            }

            while (it.hasNext())
            {
                SelectItem item = (SelectItem)it.next();
                writer.write("\t\t<option");
                Object itemObjValue = item.getValue();
                if (itemObjValue != null)
                {
                    String itemStrValue = itemObjValue.toString();
                    writer.write(" value=\"");
                    writer.write(HTMLEncoder.encode(itemStrValue, false, false));
                    writer.write("\"");
                    if ((selectMany && selectedValuesSet.contains(itemStrValue)) ||
                        (currentStrValue != null && itemStrValue.equals(currentStrValue)))
                    {
                        writer.write(" selected");
                    }
                }
                writer.write(">");
                writer.write(HTMLEncoder.encode(item.getLabel(), true, true));

                writer.write("</option>\n");
            }

        }
        writer.write("</select>");
    }

}

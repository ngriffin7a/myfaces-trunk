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
package net.sourceforge.myfaces.renderkit.html.util;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.legacy.ListboxRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


/**
 * Utility methods for rendering HTML tags.
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HTMLUtil
{
    //~ Constructors -------------------------------------------------------------------------------

    protected HTMLUtil()
    {
        // disallow public instantiation
    }

    //~ Methods ------------------------------------------------------------------------------------

    public static int getColspan(UIComponent component)
    {
        Object value = component.getAttributes().get(JSFAttr.COLSPAN_ATTR);
        int    count;

        count = ((value != null) && (value instanceof Integer)) ? ((Integer) value).intValue() : 1;

        return count;
    }

    public static int getColumns(UIComponent component)
    {
        Object value = component.getAttributes().get(JSFAttr.COLUMNS_ATTR);
        int    count;

        count =
            ((value != null) && (value instanceof Integer)) ? ((Integer) value).intValue()
                                                            : Integer.MAX_VALUE;

        return count;
    }

    public static void encodeChildrenRecursively(FacesContext context, UIComponent component)
    throws IOException
    {
        component.encodeBegin(context);

        if (component.getRendersChildren())
        {
            component.encodeChildren(context);
        }
        else
        {
            UIComponent child;

            for (
                Iterator kids = component.getChildren().iterator(); kids.hasNext();
                        encodeChildrenRecursively(context, child))
            {
                child = (UIComponent) kids.next();
            }
        }

        component.encodeEnd(context);
    }


    /**
     * @return true, if the attribute was written
     * @throws IOException
     *
     * @deprecated styleClass is now contained in HTML.UNIVERSAL_ATTRIBUTES
     */
    public static boolean renderStyleClass(ResponseWriter writer, UIComponent uiComponent)
        throws IOException
    {
        String styleClass = (String) uiComponent.getAttributes().get(JSFAttr.STYLE_CLASS_ATTR);
        if (styleClass != null && styleClass.length() > 0)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, JSFAttr.STYLE_CLASS_ATTR);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @return true, if the attribute was written
     * @throws IOException
     */
    public static boolean renderHTMLAttribute(ResponseWriter writer,
                                              String rendererAttrName,
                                              String htmlAttrName,
                                              Object value)
        throws IOException
    {
        if (!RendererUtils.isDefaultAttributeValue(value))
        {
            writer.writeAttribute(htmlAttrName, value, rendererAttrName);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @return true, if the attribute was written
     * @throws IOException
     */
    public static boolean renderHTMLAttribute(ResponseWriter writer,
                                              UIComponent component,
                                              String rendererAttrName,
                                              String htmlAttrName)
        throws IOException
    {
        Object value = component.getAttributes().get(rendererAttrName);
        return renderHTMLAttribute(writer, rendererAttrName, htmlAttrName, value);
    }


    /**
     * @return true, if an attribute was written
     * @throws IOException
     */
    public static boolean renderHTMLAttributes(ResponseWriter writer,
                                               UIComponent component,
                                               String[] attributes)
        throws IOException
    {
        boolean somethingDone = false;
        for (int i = 0, len = attributes.length; i < len; i++)
        {
            String attrName = attributes[i];
            if (attrName.equals(HTML.STYLE_CLASS_ATTR))
            {
                //render JSF "styleClass" attribute as "class"
                if (renderHTMLAttribute(writer, component, attrName, HTML.CLASS_ATTR))
                {
                    somethingDone = true;
                }
            }
            else
            {
                if (renderHTMLAttribute(writer, component, attrName, attrName))
                {
                    somethingDone = true;
                }
            }
        }
        return somethingDone;
    }


    public static void renderSelect(
        FacesContext facesContext, UIComponent uiComponent, Class rendererClass, int size)
    throws IOException
    {
        ResponseWriter writer     = facesContext.getResponseWriter();

        boolean        selectMany = (uiComponent instanceof UISelectMany);

        writer.startElement(HTML.SELECT_ELEM, uiComponent);
        writer.writeAttribute(HTML.NAME_ATTR, uiComponent.getClientId(facesContext), null);

        if (rendererClass.isAssignableFrom (ListboxRenderer.class))
        {
            writer.writeAttribute(HTML.SIZE_ATTR, Integer.toString(size),null);
        }

        renderHTMLAttributes(writer, uiComponent, HTML.SELECT_PASSTHROUGH_ATTRIBUTES);
        renderDisabledOnUserRole(writer, uiComponent, facesContext);

        if (selectMany)
        {
            writer.writeAttribute(HTML.MULTIPLE_ATTR, HTML.MULTIPLE_ATTR, null);
        }

        Iterator it = SelectItemUtil.getSelectItems(facesContext, uiComponent);

        if (it.hasNext())
        {
            String currentStrValue   = null;
            Set    selectedValuesSet = null;

            if (selectMany)
            {
                selectedValuesSet =
                    SelectItemUtil.getSelectedValuesAsStringSet(
                        facesContext, (UISelectMany) uiComponent);
            }
            else
            {
                ValueBinding vb = uiComponent.getValueBinding(JSFAttr.VALUE_ATTR);
                Object currentValue = vb.getValue(facesContext);
                currentStrValue = ((currentValue != null) ? currentValue.toString() : null);
            }

            while (it.hasNext())
            {
                SelectItem item = (SelectItem) it.next();
                writer.write("\t\t");
                writer.startElement(HTML.OPTION_ELEM, uiComponent);

                Object itemObjValue = item.getValue();

                if (itemObjValue != null)
                {
                    String itemStrValue = itemObjValue.toString();
                    writer.writeAttribute(HTML.VALUE_ATTR, itemStrValue, null);

                    if (
                        (selectMany && selectedValuesSet.contains(itemStrValue))
                                || ((currentStrValue != null)
                                && itemStrValue.equals(currentStrValue)))
                    {
                        writer.writeAttribute(HTML.INPUT_SELECTED_VALUE,
                                HTML.INPUT_SELECTED_VALUE, null);
                    }
                }


                writer.writeText(HTMLEncoder.encode(
                        item.getLabel(),
                        true,
                        true),null);

                writer.endElement(HTML.OPTION_ELEM);
            }
        }

        writer.endElement(HTML.SELECT_ELEM);
    }


    /**@deprecated use {@link #renderDisabledOnUserRole(javax.faces.context.ResponseWriter, javax.faces.component.UIComponent, javax.faces.context.FacesContext)} instead*/
    public static void renderDisabledOnUserRole(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        if (!RendererUtils.isEnabledOnUserRole(facesContext, uiComponent))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.write(" disabled");
        }
    }


    public static void renderDisabledOnUserRole(ResponseWriter writer,
                                                UIComponent uiComponent,
                                                FacesContext facesContext)
        throws IOException
    {
        if (!RendererUtils.isEnabledOnUserRole(facesContext, uiComponent))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, JSFAttr.ENABLED_ON_USER_ROLE_ATTR);
        }
    }



    public static void renderTableRowOfOneCell(
        FacesContext context, UIComponent component, int columns, String rowClass,
        String[] columnClasses, String cellHtmlTag, String rowGroupTag)
    throws IOException
    {
        ResponseWriter writer           = context.getResponseWriter();
        int            columnClassCount = (columnClasses == null) ? 0 : columnClasses.length;

        if (rowGroupTag != null)
        {
            writer.write("\t<");
            writer.write(rowGroupTag);
            writer.write(">\n");
        }

        writer.write("\t\t<tr");

        if (rowClass != null)
        {
            writer.write(" class=\"");
            writer.write(rowClass);
            writer.write('"');
        }

        writer.write(">\n\t\t\t<");
        writer.write(cellHtmlTag);

        int colspan = getColspan(component);

        if (colspan > 1)
        {
            writer.write(" colspan=\"");
            writer.write(colspan);
            writer.write('"');
        }

        if (columnClassCount > 0)
        {
            writer.write(" class=\"");
            writer.write(columnClasses[0]);
            writer.write('"');
        }

        writer.write('>');

        encodeChildrenRecursively(context, component);

        writer.write("</");
        writer.write(cellHtmlTag);
        writer.write(">\n");

        // REVISIT: should we fill in with empty cells or not?
//        for (int i = 1; i < columns; i++)
//        {
//            writer.write("\t\t\t<");
//            writer.write(cellHtmlTag);
//
//            if (columnClassCount > 0)
//            {
//                writer.write(" class=\"");
//                writer.write(columnClasses[i % columnClassCount]);
//                writer.write('"');
//            }
//
//            writer.write(" />\n");
//        }
        writer.write("\t\t</tr>\n");

        if (rowGroupTag != null)
        {
            writer.write("\t</");
            writer.write(rowGroupTag);
            writer.write(">\n");
        }
    }

    public static int renderTableRows(
        FacesContext context, Iterator fields, int columns, String[] rowClasses,
        String[] columnClasses, String cellHtmlTag, String rowGroupTag, int row)
    throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        if (rowGroupTag != null)
        {
            writer.write("\t<");
            writer.write(rowGroupTag);
            writer.write(">\n");
        }

        int     rowClassCount    = (rowClasses == null) ? 0 : rowClasses.length;
        int     columnClassCount = (columnClasses == null) ? 0 : columnClasses.length;

        boolean closeRowTag      = false;

        for (
            int col = Integer.MAX_VALUE, colspan = 1, colClassIndex = 0; fields.hasNext();
                    col += colspan)
        {
            if (col >= columns)
            {
                col = 0;

                if (closeRowTag)
                {
                    writer.write("\t\t</tr>\n");
                }

                writer.write("\t\t<tr");

                if (rowClassCount > 0)
                {
                    writer.write(" class=\"");
                    writer.write(rowClasses[row++ % rowClassCount]);
                    writer.write('"');
                }

                writer.write(">\n");
                closeRowTag       = true;
                colClassIndex     = 0;
            }

            writer.write("\t\t\t<");
            writer.write(cellHtmlTag);

            UIComponent child = (UIComponent) fields.next();
            colspan = getColspan(child);

            if (colspan > 1)
            {
                writer.write(" colspan=\"");
                writer.write(colspan);
                writer.write('"');
            }

            if (columnClassCount > 0)
            {
                writer.write(" class=\"");
                writer.write(columnClasses[colClassIndex++]);
                writer.write('"');

                if (colClassIndex >= columnClassCount)
                {
                    colClassIndex = 0;
                }
            }

            writer.write('>');

            encodeChildrenRecursively(context, child);

            writer.write("</");
            writer.write(cellHtmlTag);
            writer.write(">\n");
        }

        if (closeRowTag)
        {
            writer.write("\t\t</tr>\n");
        }

        if (rowGroupTag != null)
        {
            writer.write("\t</");
            writer.write(rowGroupTag);
            writer.write(">\n");
        }

        return row;
    }

    public static String addAttributeToHref(String href, String name, String value)
    {
        StringBuffer buf = new StringBuffer();
        buf.append(href);

        if (href.indexOf(HTML.HREF_PATH_FROM_PARAM_SEPARATOR) == -1)
        {
            buf.append(HTML.HREF_PATH_FROM_PARAM_SEPARATOR);
        }
        else
        {
            buf.append(HTML.HREF_PARAM_SEPARATOR);
        }
        buf.append(name);
        buf.append(HTML.HREF_PARAM_NAME_FROM_VALUE_SEPARATOR);
        buf.append(value);

        return buf.toString();
    }

    public static boolean isDisabled(UIComponent component)
    {
        return "true".equalsIgnoreCase((String) component.getAttributes().get(HTML.DISABLED_ATTR));
    }

    public static boolean isReadOnly(UIComponent component)
    {
        return "true".equalsIgnoreCase((String) component.getAttributes().get(HTML.READONLY_ATTR));
    }
}

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

import net.sourceforge.myfaces.renderkit.attr.GridRendererAttributes;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class GridRenderer
        extends HTMLRenderer
        implements CallbackRenderer, GridRendererAttributes
{
    public static final String TYPE = "Grid";
    private static final String COLUMN_COUNT_ATTR = "colcount";
    private static final String STYLES_ATTR = "styles";
    private static final String ROW_COUNT_ATTR = "rowcount";
    private static final Integer ZERO = new Integer(0);

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UIPanel;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UIPanel.TYPE);
    }

    public String getRendererType()
    {
        return TYPE;
    }

    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent uiComponent) throws IOException
    {
        UIComponent gridComponent = uiComponent.getParent();
        Integer actualColumn = (Integer)gridComponent.getAttribute(COLUMN_COUNT_ATTR);
        Integer actualRow = (Integer)gridComponent.getAttribute(ROW_COUNT_ATTR);
        Integer columns = (Integer)gridComponent.getAttribute(COLUMNS_ATTR);
        if (actualColumn != null && columns != null)
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            if (actualColumn.intValue() == 0 ||
                (actualColumn.intValue()) % columns.intValue() == 0)
            {
                writer.write("<tr");
                String style = calcRowStyle(gridComponent,
                                            actualRow != null ? actualRow.intValue(): -1,
                                            actualColumn.intValue());
                if (style != null && style.length() > 0)
                {
                    writer.write(" class=\"");
                    writer.write(style);
                    writer.write("\"");
                }
                writer.write(">");
            }
            writer.write("<td>");
        }
    }

    private String calcRowStyle(UIComponent gridComponent, int actualRow, int actualColumn)
    {
        String style = null;
        if (actualRow == 0)
        {
            style = (String)gridComponent.getAttribute(HEADER_CLASS_ATTR);
        }
        if (style == null)
        {
            String columnStyles[] = (String[])gridComponent.getAttribute(STYLES_ATTR);
            if (columnStyles == null)
            {
                String columnClasses = (String)gridComponent.getAttribute(COLUMN_CLASSES_ATTR);
                if (columnClasses != null)
                {
                    StringTokenizer tokenizer = new StringTokenizer(columnClasses, ",");
                    columnStyles = new String[tokenizer.countTokens()];
                    for (int i = 0; tokenizer.hasMoreTokens(); )
                    {
                        columnStyles[i] = tokenizer.nextToken();
                    }
                    gridComponent.setAttribute(STYLES_ATTR, columnStyles);
                }
                else
                {
                    columnStyles = new String[0];
                    gridComponent.setAttribute(STYLES_ATTR, columnStyles);
                }
            }
            if (columnStyles != null && columnStyles.length > 0)
            {
                style = columnStyles[(actualColumn + 1) % columnStyles.length];
            }
        }
        return style;
    }

    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent) throws IOException
    {
        if (uiComponent != null)
        {
            UIComponent gridComponent = uiComponent.getParent();
            Integer actualColumn = (Integer)gridComponent.getAttribute(COLUMN_COUNT_ATTR);
            Integer columns = (Integer)gridComponent.getAttribute(COLUMNS_ATTR);
            if (actualColumn != null && columns != null)
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                if (actualColumn.intValue() == 0 ||
                    (actualColumn.intValue() + 1) % columns.intValue() == 0)
                {
                    Integer actualRow = (Integer)gridComponent.getAttribute(ROW_COUNT_ATTR);
                    gridComponent.setAttribute(ROW_COUNT_ATTR, new Integer(actualRow.intValue() + 1));
                    gridComponent.setAttribute(COLUMN_COUNT_ATTR, ZERO);
                    writer.write("</tr>");
                }
                writer.write("</td>");
            }
            gridComponent.setAttribute(COLUMN_COUNT_ATTR, new Integer(actualColumn.intValue() + 1));
        }
    }

    public void encodeBegin(FacesContext context, UIComponent uiComponent)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        writer.write("<table");
        String style = (String)uiComponent.getAttribute(PANEL_CLASS_ATTR.getName());
        if (style != null && style.length() > 0)
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write("\"");
        }
        writer.write(">\n");

        uiComponent.setAttribute(COLUMN_COUNT_ATTR, ZERO);
        uiComponent.setAttribute(ROW_COUNT_ATTR, ZERO);

        CallbackSupport.addChildrenCallbackRenderer(context, uiComponent, this);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</table>\n");

        CallbackSupport.removeCallbackRenderer(facesContext, uiComponent, this);

    }
}

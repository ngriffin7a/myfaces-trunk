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

import net.sourceforge.myfaces.component.CommonComponentProperties;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.GridRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLTableAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;

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
    implements CallbackRenderer,
               CommonComponentProperties,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               HTMLTableAttributes,
               GridRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "Grid";
    private static final String COLUMN_COUNT_ATTR = GridRenderer.class.getName() + ".colcount";
    private static final String ROW_COUNT_ATTR = GridRenderer.class.getName() + ".rowcount";
    private static final String ROW_CLASSES_ATTR_CACHE = GridRenderer.class.getName() + ".rowclasses";
    private static final String COLUMN_CLASSES_ATTR_CACHE = GridRenderer.class.getName() + ".colclasses";
    private static final Integer ZERO = new Integer(0);

    public String getRendererType()
    {
        return TYPE;
    }

    /*
    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UIPanel;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UIPanel.TYPE);
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_grid", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_grid", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_grid", HTML_TABLE_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_grid", PANEL_GRID_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_grid", USER_ROLE_ATTRIBUTES);
    }
    */



    private int getAttributeValue(UIComponent gridComponent, String attributeName)
    {
        Integer value = (Integer)gridComponent.getAttribute(attributeName);
        return value != null ? value.intValue() : 0;
    }

    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent uiComponent) throws IOException
    {
        UIComponent gridComponent = UIComponentUtils.getParentOrFacetOwner(uiComponent);
        int actualColumn = getAttributeValue(gridComponent, COLUMN_COUNT_ATTR);
        int actualRow = getAttributeValue(gridComponent, ROW_COUNT_ATTR);
        int columns = getAttributeValue(gridComponent, COLUMNS_ATTR);
        ResponseWriter writer = facesContext.getResponseWriter();

        // close row
        if (actualColumn > 0 && actualColumn % columns == 0)
        {
            // close row
            writer.write("</tr>\n");
            // reset actualColumn
            actualColumn = 0;
            // add row
            actualRow++;
        }

        // open row
        if (actualColumn == 0)
        {
            String style = calcRowStyle(gridComponent,
                                        actualRow,
                                        false);
            writer.write("<tr");
            if (style != null && style.length() > 0)
            {
                writer.write(" class=\"");
                writer.write(style);
                writer.write("\"");
            }
            writer.write(">");
        }

        // open column
        String style = calcColumnStyle(gridComponent,
                                       actualRow,
                                       actualColumn);
        writer.write("<td");
        if (style != null && style.length() > 0)
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write("\"");
        }
        writer.write(">");
        actualColumn++;

        // save attributes
        gridComponent.setAttribute(COLUMN_COUNT_ATTR, new Integer(actualColumn));
        gridComponent.setAttribute(ROW_COUNT_ATTR, new Integer(actualRow));
    }



    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent) throws IOException
    {
        if (uiComponent != null)
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.write("</td>");
        }
    }

    public void encodeBegin(FacesContext context, UIComponent uiComponent)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        writer.write("<table");
        String style = (String)uiComponent.getAttribute(PANEL_CLASS_ATTR);
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
        writer.write("</tr></table>\n");

        CallbackSupport.removeCallbackRenderer(facesContext, uiComponent, this);

    }


    /**
     * TODO: refactor see ListRenderer
     */
    private String calcRowStyle(UIComponent gridComponent,
                                int actualRow,
                                boolean isLastRow)
    {
        String style = null;
        String headerStyle = (String)gridComponent.getAttribute(HEADER_CLASS_ATTR);
        boolean hasHeaderStyle = headerStyle != null;

        if (actualRow == 0)
        {
            style = headerStyle;
        }
        if (isLastRow && style == null)
        {
            style = (String)gridComponent.getAttribute(FOOTER_CLASS_ATTR);
        }
        if (style == null)
        {
            String[] rowStyles = getRowClasses(gridComponent);
            if (rowStyles != null && rowStyles.length > 0)
            {
                int ref = hasHeaderStyle ? actualRow - 1 : actualRow;
                int i = (ref % rowStyles.length);
                style = rowStyles[i];
            }
        }
        return style;
    }


    /**
     * TODO: refactor see ListRenderer
     */
    private String calcColumnStyle(UIComponent gridComponent,  int actualRow, int actualColumn)
    {
        String headerStyle = (String)gridComponent.getAttribute(HEADER_CLASS_ATTR);
        boolean hasHeaderStyle = headerStyle != null;
        if (actualRow == 0 && hasHeaderStyle)
        {
            return null;
        }

        String[] columnClasses = getColumnClasses(gridComponent);

        if (columnClasses != null && columnClasses.length > 0)
        {
            return columnClasses[actualColumn % columnClasses.length];
        }

        return null;
    }

    private String[] getRowClasses(UIComponent gridComponent)
    {
        String[] rowClasses = (String[])gridComponent.getAttribute(ROW_CLASSES_ATTR_CACHE);
        if (rowClasses == null)
        {
            rowClasses = getAttributes(gridComponent, ROW_CLASSES_ATTR);
            gridComponent.setAttribute(ROW_CLASSES_ATTR_CACHE, rowClasses);
        }
        return rowClasses;
    }

    private String[] getColumnClasses(UIComponent gridComponent)
    {
        String[] rowClasses = (String[])gridComponent.getAttribute(COLUMN_CLASSES_ATTR_CACHE);
        if (rowClasses == null)
        {
            rowClasses = getAttributes(gridComponent, COLUMN_CLASSES_ATTR);
            gridComponent.setAttribute(COLUMN_CLASSES_ATTR_CACHE, rowClasses);
        }
        return rowClasses;
    }

    private static final String DELIMITER = ",";
    private String[] getAttributes(UIComponent uiComponent, String attributeName)
    {
        String[] attr = null;
        Object obj = uiComponent.getAttribute(attributeName);
        if (obj instanceof String[])
        {
            return (String[])obj;
        }
        String rowClasses = (String)obj;
        if (rowClasses != null && rowClasses.length() > 0)
        {
            StringTokenizer tokenizer = new StringTokenizer(rowClasses, DELIMITER);

            attr = new String[tokenizer.countTokens()];
            for (int i = 0; tokenizer.hasMoreTokens(); i++)
            {
                attr[i] = tokenizer.nextToken().trim();
            }
        }

        if (attr == null)
        {
            attr = new String[0];
        }

        return attr;
    }

}

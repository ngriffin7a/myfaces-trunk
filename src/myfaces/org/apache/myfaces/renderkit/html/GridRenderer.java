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

import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.renderkit.attr.GridRendererAttributes;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class GridRenderer
        extends AbstractPanelRenderer
        implements GridRendererAttributes
{
    public static final String TYPE = "Grid";

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

    public void encodeBegin(FacesContext context, UIComponent uicomponent)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        writer.write("<table");
        String style = (String)uicomponent.getAttribute(UIPanel.CLASS_ATTR);
        if (style != null && style.length() > 0)
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write("\"");
        }
        writer.write(">\n");
    }

    public void encodeChildren(FacesContext context, UIComponent uicomponent)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        Styles styles = getStyles(uicomponent);

        Integer obj = (Integer)uicomponent.getAttribute(UIPanel.COLUMNS_ATTR);

        int max_columns = obj != null ? obj.intValue() : 1;
        ArrayList componentList = new ArrayList(max_columns);

        Iterator children = uicomponent.getChildren();

        int row = 0;
        int column = 0;

        fillComponentList(componentList, children, max_columns);
        while (componentList.size() > 0)
        {
            column = 0;
            writer.write("\n<tr>");
            boolean isLastRow = !children.hasNext();

            for (int i = 0; i < max_columns; i++)
            {
                UIComponent childComponent = (UIComponent)componentList.get(i);

                writer.write("<td");
                String style = calcStyle(styles,
                                         row,
                                         column,
                                         isLastRow);

                if (style != null && style.length() > 0)
                {
                    writer.write(" class=\"");
                    writer.write(style);
                    writer.write("\"");
                }
                writer.write(">");

                encodeComponent(context, childComponent);

                writer.write("</td>\n");

                column++;
            }

            row++;
            writer.write("</tr>");
            fillComponentList(componentList, children, max_columns);
        }
    }

    private void fillComponentList(ArrayList componentList, Iterator it, int elements)
    {
        componentList.clear();
        for (int i = 0; it.hasNext() && i < elements; i++)
        {
            componentList.add(it.next());
        }
    }

    public void encodeEnd(FacesContext context, UIComponent uicomponent)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("</table>\n");
    }
}

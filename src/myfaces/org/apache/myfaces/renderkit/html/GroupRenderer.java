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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Iterator;

/**
 * TODO: description
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class GroupRenderer
        extends AbstractPanelRenderer
{
    public static final String TYPE = "Group";
    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        UIComponent parentComponent = component.getParent();

        Styles styles = getStyles(parentComponent);

        writer.write("<tr>");

        Boolean istLastChildComponent = (Boolean)parentComponent.getAttribute(ListRenderer.LAST_COMPONENT);

        Integer actualRow = (Integer)parentComponent.getAttribute(ListRenderer.ACTUAL_ROW);
        int row = actualRow != null ? actualRow.intValue() : 0;

        int column = 0;
        for (Iterator children = component.getChildren(); children.hasNext();)
        {
            writer.write("<td");
            String style = calcStyle(styles,
                                     row,
                                     column,
                                     istLastChildComponent != null && istLastChildComponent.booleanValue());


            if (style != null && style.length() > 0)
            {
                writer.write(" class=\"");
                writer.write(style);
                writer.write("\"");
            }
            writer.write(">");

            UIComponent childComponent = (UIComponent)children.next();
            encodeComponent(context, childComponent);

            writer.write("</td>\n");
            column++;
        }
        writer.write("</tr>");

        parentComponent.setAttribute(ListRenderer.ACTUAL_ROW, new Integer(row++));

    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIPanel.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UIPanel;
    }



    private String[] getAttribute(UIComponent parentComponent, String attribute)
    {
        String[] attr = null;
        if (parentComponent.getComponentType().equals(javax.faces.component.UIPanel.TYPE))
        {
            String rowClasses = (String)parentComponent.getAttribute(attribute);
            if (rowClasses != null && rowClasses.length() > 0)
            {
                StringTokenizer tokenizer = new StringTokenizer(rowClasses, ",");

                attr = new String[tokenizer.countTokens()];
                int i = 0;
                while (tokenizer.hasMoreTokens())
                {
                    attr[i] = tokenizer.nextToken().trim();
                    i++;
                }
            }
        }
        if (attr == null)
        {
            attr = new String[0];
        }
        return attr;
    }
}

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
import net.sourceforge.myfaces.renderkit.attr.ListRendererAttributes;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ListRenderer
        extends AbstractPanelRenderer
        implements ListRendererAttributes
{
    public static final String TYPE = "List";

    public static final String ITERATOR_ATTR    = ListRenderer.class.getName() + ".iterator";

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
        int actualRow = 0;
        boolean listHasHeaderRow = false;

        Styles styles = getStyles(uicomponent);

        for (Iterator children = uicomponent.getChildren(); children.hasNext();)
        {
            UIComponent childComponent = (UIComponent)children.next();
            String rendererType = childComponent.getRendererType();

            // is Component the last Component?
            boolean isLastComponent = children.hasNext() ? false : true;

            if (rendererType.equals(DataRenderer.TYPE))
            {
                actualRow = encodeData(context,
                                       uicomponent,
                                       childComponent,
                                       styles,
                                       actualRow,
                                       isLastComponent,
                                       listHasHeaderRow);
            }
            else if (rendererType.equals(GroupRenderer.TYPE))
            {
                if (actualRow == 0)
                {
                    listHasHeaderRow = true;
                }
                actualRow = encodeGroup(context,
                                        uicomponent,
                                        childComponent,
                                        styles,
                                        actualRow,
                                        isLastComponent);
            }
            else
            {
                throw new IllegalArgumentException("Illegal UIComponent! UIComponent nested within a panel component list " +
                                                   "must have renderer type in (" + DataRenderer.TYPE + ", " + GroupRenderer.TYPE + ")");

            }
        }
    }

    public int encodeGroup(FacesContext context,
                           UIComponent listComponent,
                           UIComponent groupComponent,
                           Styles styles,
                           int actualRow,
                           boolean istLastChildComponent)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        writer.write("<tr>");

        int column = 0;
        for (Iterator children = groupComponent.getChildren(); children.hasNext();)
        {
            writer.write("<td");
            String style = calcStyle(styles,
                                     actualRow,
                                     column,
                                     istLastChildComponent);


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

        return actualRow++;
    }

    public int encodeData(FacesContext context,
                          UIComponent listComponent,
                          UIComponent dataComponent,
                          Styles styles,
                          int actualRow,
                          boolean istLastChildComponent,
                          boolean listHasHeaderRow)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        String varAttr = (String)dataComponent.getAttribute(UIPanel.VAR_ATTR);

        for (Iterator it = getIterator(context, dataComponent); it.hasNext();)
        {
            Object varObj = it.next();

            // TODO: does not work with nested lists. (save varAttr in parent-list-component)
            context.getServletRequest().setAttribute(varAttr, varObj);

            writer.write("<tr>");

            int column = 0;
            for (Iterator children = dataComponent.getChildren(); children.hasNext();)
            {
                writer.write("<td");
                String style = calcStyle(styles,
                                         listHasHeaderRow ? actualRow + 1 : actualRow,
                                         column,
                                         istLastChildComponent);

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
            actualRow++;
        }

        context.getServletRequest().removeAttribute(varAttr);

        return actualRow;
    }



    public void encodeEnd(FacesContext context, UIComponent uicomponent)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("</table>\n");
    }

}



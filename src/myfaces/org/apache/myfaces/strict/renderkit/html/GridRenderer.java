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
package net.sourceforge.myfaces.strict.renderkit.html;

import net.sourceforge.myfaces.renderkit.*;
import net.sourceforge.myfaces.renderkit.html.*;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.StringUtils;

import java.io.IOException;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class GridRenderer
extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE      = "Grid";
    public static final char  CLASS_LIST_DELIMITER = ',';

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext context, UIComponent uiComponent)
    throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        writer.write("\n<table");

        String style = (String) uiComponent.getAttribute(JSFAttr.PANEL_CLASS_ATTR);

        if ((style != null) && (style.length() > 0))
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write('"');
        }

        writer.write(">\n");
    }

    public void encodeChildren(FacesContext context, UIComponent component)
    throws IOException
    {
        if (!component.isRendered())
        {
            return;
        }

        int         columns  = HTMLUtil.getColumns(component);
        UIComponent facet;
        Iterator    children;

        // Render header facet
        if ((facet = component.getFacet("header")) != null)
        {
            String   headerClass   = (String) component.getAttribute("headerClass");
            String[] columnClasses = (headerClass == null) ? null : new String[] {headerClass};
            children = facet.getChildren();

            if ((children != null) && children.hasNext())
            {
                HTMLUtil.renderTableRows(
                    context, children, columns, null, columnClasses, "th", "thead");
            }
            else
            {
                HTMLUtil.renderTableRowOfOneCell(
                    context, facet, columns, null, columnClasses, "th", "thead");
            }
        }

        // Render data rows
        children = component.getChildren();

        if ((children != null) && children.hasNext())
        {
            String[] rowClasses    =
                StringUtils.splitShortString(
                    (String) component.getAttribute(JSFAttr.ROW_CLASSES_ATTR), CLASS_LIST_DELIMITER);
            String[] columnClasses =
                StringUtils.splitShortString(
                    (String) component.getAttribute(JSFAttr.COLUMN_CLASSES_ATTR), CLASS_LIST_DELIMITER);

            HTMLUtil.renderTableRows(
                context, children, columns, rowClasses, columnClasses, "td", "tbody");
        }

        // Render footer facet
        if (null != (facet = component.getFacet("footer")))
        {
            String   footerClass   = (String) component.getAttribute("footerClass");
            String[] columnClasses = (footerClass == null) ? null : new String[] {footerClass};
            children = facet.getChildren();

            if ((children != null) && children.hasNext())
            {
                HTMLUtil.renderTableRows(
                    context, children, columns, null, columnClasses, "th", "tfoot");
            }
            else
            {
                HTMLUtil.renderTableRowOfOneCell(
                    context, facet, columns, null, columnClasses, "th", "tfoot");
            }
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</table>\n");
    }
}

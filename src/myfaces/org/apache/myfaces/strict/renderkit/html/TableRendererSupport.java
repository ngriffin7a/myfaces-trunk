/**
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
package net.sourceforge.myfaces.strict.renderkit.html;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public abstract class TableRendererSupport
extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE                 = "Grid";
    public static final char   CLASS_LIST_DELIMITER = ',';

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

        String style = (String) uiComponent.getAttributes().get(JSFAttr.PANEL_CLASS_ATTR);

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

        int columns = HTMLUtil.getColumns(component);

        renderHeader(context, component, columns);
        renderBody(context, component, columns);
        renderFooter(context, component, columns);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</table>\n");
    }

    protected abstract void renderBody(FacesContext context, UIComponent component, int columns)
    throws IOException;

    protected void renderFooter(FacesContext context, UIComponent component, int columns)
    throws IOException
    {
        UIComponent facet = component.getFacet("footer");

        if (facet != null)
        {
            String   footerClass   = (String) component.getAttributes().get("footerClass");
            String[] columnClasses = (footerClass == null) ? null : new String[] {footerClass};
            Iterator children      = facet.getChildren().iterator();

            if ((children != null) && children.hasNext())
            {
                HTMLUtil.renderTableRows(
                    context, children, columns, null, columnClasses, "th", "tfoot", 0);
            }
            else
            {
                HTMLUtil.renderTableRowOfOneCell(
                    context, facet, columns, null, columnClasses, "th", "tfoot");
            }
        }
    }

    protected void renderHeader(FacesContext context, UIComponent component, int columns)
    throws IOException
    {
        UIComponent facet = component.getFacet("header");

        if (facet != null)
        {
            String   headerClass   = (String) component.getAttributes().get("headerClass");
            String[] columnClasses = (headerClass == null) ? null : new String[] {headerClass};
            Iterator children      = facet.getChildren().iterator();

            if ((children != null) && children.hasNext())
            {
                HTMLUtil.renderTableRows(
                    context, children, columns, null, columnClasses, "th", "thead", 0);
            }
            else
            {
                HTMLUtil.renderTableRowOfOneCell(
                    context, facet, columns, null, columnClasses, "th", "thead");
            }
        }
    }
}

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

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.*;
import net.sourceforge.myfaces.renderkit.html.*;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.FacesUtils;

import java.io.IOException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

import javax.servlet.ServletRequest;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class DataRenderer
extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "Data";

    //~ Methods ------------------------------------------------------------------------------------

    public static Iterator getIterator(FacesContext facesContext, UIPanel uiPanel)
    {
        Object value = uiPanel.currentValue(facesContext);

        if (value == null)
        {
            return null;
        }

        if (value instanceof Iterator)
        {
            return (Iterator) value;
        }

        if (value instanceof Collection)
        {
            return ((Collection) value).iterator();
        }

        if (value.getClass().isArray())
        {
            // FIXME: use real array iterator
            return Arrays.asList((Object[]) value).iterator();
        }

        throw new IllegalArgumentException(
            "Value of component " + UIComponentUtils.toString(uiPanel)
            + " is neither collection nor array.");
    }

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeChildren(FacesContext context, UIComponent component)
    throws IOException
    {
        if (!component.isRendered())
        {
            return;
        }

        UIPanel grid = (UIPanel) component.getParent();

        if ((grid == null) || !GridRenderer.TYPE.equals(grid.getRendererType()))
        {
            throw new FacesException("the parent of panel_data must be panel_grid");
        }

        // init iterator
        Iterator it = getIterator(context, (UIPanel) component);

        if (it == null)
        {
            return;
        }

        String         varName       = (String) component.getAttribute(JSFAttr.VAR_ATTR);
        ServletRequest request       = FacesUtils.getRequest(context);
        int            columns       = HTMLUtil.getColumns(grid);
        String[]       rowClasses    = GridRenderer.getRowClasses(grid);
        String[]       columnClasses = GridRenderer.getColumnClasses(grid);

        // main loop on data iterator
        while (it.hasNext())
        {
            // set var
            request.setAttribute(
                varName,
                it.next());

            // FIXME: clientID must be generated with an index
            // Render data rows
            Iterator children = component.getChildren();

            if ((children != null) && children.hasNext())
            {
                HTMLUtil.renderTableRows(
                    context, children, columns, rowClasses, columnClasses, "td", "tbody");
            }
        }

        // clear var
        request.setAttribute(varName, null);
    }
}

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

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.FacesUtils;
import net.sourceforge.myfaces.util.StringUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.iterators.ArrayIterator;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ListRenderer
extends TableRendererSupport
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "List";

    //~ Methods ------------------------------------------------------------------------------------

    public static Iterator getIterator(FacesContext facesContext, UIPanel uiPanel)
    {
        //Object value = uiPanel.currentValue(facesContext);
        Object value = null;

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
            return new ArrayIterator(value);
        }

        throw new IllegalArgumentException(
            "Value of type " + value.getClass() + " of component "
            + UIComponentUtils.toString(uiPanel) + " is neither collection nor array.");
    }

    public String getRendererType()
    {
        return TYPE;
    }

    protected void renderBody(FacesContext context, UIComponent component, int columns)
    throws IOException
    {
        if (!component.isRendered())
        {
            return;
        }

        //FIXME
        /*
        if ((component.getChildCount() != 1) || !(component.getChild(0) instanceof UIPanel))
        {
            throw new FacesException("panel_list must have exactly one child tag panel_data");
        }

        UIPanel panel = (UIPanel) component.getChild(0);
        */
        UIPanel panel = null;
        if (!panel.getRendererType().equals(DataRenderer.TYPE))
        {
            throw new FacesException("panel_list must have exactly one child tag panel_data");
        }

        if ((component == null) || !ListRenderer.TYPE.equals(component.getRendererType()))
        {
            throw new FacesException("the parent of panel_data must be panel_grid");
        }

        // init iterator
        Iterator it = getIterator(context, panel);

        if (it == null)
        {
            return;
        }

        String   varName       = (String) panel.getAttributes().get(JSFAttr.VAR_ATTR);
        Map      requestMap    = FacesUtils.getRequestMap(context);
        String[] rowClasses    =
            StringUtils.trim(
                StringUtils.splitShortString(
                    (String) component.getAttributes().get(JSFAttr.ROW_CLASSES_ATTR), CLASS_LIST_DELIMITER));
        String[] columnClasses =
            StringUtils.trim(
                StringUtils.splitShortString(
                    (String) component.getAttributes().get(JSFAttr.COLUMN_CLASSES_ATTR),
                    CLASS_LIST_DELIMITER));

        // main loop on data iterator
        for (int row = 0; it.hasNext();)
        {
            // set var
            requestMap.put(
                varName,
                it.next());

            // FIXME: clientID must be generated with an index
            // Render data rows
            Iterator children = panel.getChildren().iterator();

            if ((children != null) && children.hasNext())
            {
                row = HTMLUtil.renderTableRows(
                        context, children, columns, rowClasses, columnClasses, "td", "tbody", row);
            }
        }

        // clear var
        requestMap.put(varName, null);
    }
}

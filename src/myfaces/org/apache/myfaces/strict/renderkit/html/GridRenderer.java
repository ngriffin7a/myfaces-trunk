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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class GridRenderer
extends TableRendererSupport
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "Grid";

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    protected void renderBody(FacesContext context, UIComponent component, int columns)
    throws IOException
    {
        Iterator children = component.getChildren();

        if ((children != null) && children.hasNext())
        {
            String[] rowClasses    =
                StringUtils.trim(
                    StringUtils.splitShortString(
                        (String) component.getAttribute(JSFAttr.ROW_CLASSES_ATTR),
                        CLASS_LIST_DELIMITER));
            String[] columnClasses =
                StringUtils.trim(
                    StringUtils.splitShortString(
                        (String) component.getAttribute(JSFAttr.COLUMN_CLASSES_ATTR),
                        CLASS_LIST_DELIMITER));

            HTMLUtil.renderTableRows(
                context, children, columns, rowClasses, columnClasses, "td", "tbody", 0);
        }
    }
}

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
package net.sourceforge.myfaces.taglib.ext;

import net.sourceforge.myfaces.component.ext.UISortColumn;
import net.sourceforge.myfaces.renderkit.html.ext.SortColumnRenderer;
import net.sourceforge.myfaces.taglib.legacy.CommandHyperlinkTag;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by Author)
 * @version $Revision$ $Date$
 */
public class SortColumnTag
    extends CommandHyperlinkTag
{
    public String getComponentType()
    {
        return "SortColumn";
    }

    public String getDefaultRendererType()
    {
        return SortColumnRenderer.TYPE;
    }

    public void setColumn(String v)
    {
        setComponentPropertyString(UISortColumn.COLUMN_PROP, v);
    }

    public void setDefaultAscending(String b)
    {
        setRendererAttributeBoolean(SortColumnRenderer.DEFAULT_ASCENDING_ATTR, b);
    }

    /*
    public int getDoStartValue() throws JspException
    {
        if (getCreated())
        {
            UICommand sortColumn = (UICommand)getComponent();
            UIComponent sortHeader = sortColumn.getParent();
            if (sortHeader == null ||
                !(sortHeader instanceof UISortHeader))
            {
                throw new JspException("Expected sort header component as parent");
            }
            sortColumn.addActionListener((UISortHeader)sortHeader);
        }
        return super.getDoStartValue();
    }
    */

}


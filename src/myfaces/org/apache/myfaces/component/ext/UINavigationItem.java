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
package net.sourceforge.myfaces.component.ext;

import net.sourceforge.myfaces.component.UICommand;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UINavigationItem
    extends UICommand
{
    public static final String TREE_ID_ATTR = "treeId";
    public static final String HREF_ATTR = "href";

    public static final String TYPE = UINavigationItem.class.getName();

    public String getComponentType()
    {
        return TYPE;
    }


    public String getTreeId()
    {
        return (String)getAttribute(TREE_ID_ATTR);
    }

    public void setTreeId(String treeId)
    {
        setAttribute(TREE_ID_ATTR, treeId);
    }

    public String getHref()
    {
        return (String)getAttribute(HREF_ATTR);
    }

    public void setHref(String treeId)
    {
        setAttribute(HREF_ATTR, treeId);
    }

    public boolean isOpen()
    {
        Boolean open = (Boolean)getAttribute("open");
        return open != null && open.booleanValue();
    }

    public void setOpen(boolean open)
    {
        setAttribute("open", open ? Boolean.TRUE : null);
    }

}

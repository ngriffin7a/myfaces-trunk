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
package net.sourceforge.myfaces.custom.navmenu;

import javax.faces.model.SelectItem;
import java.util.Collections;
import java.util.List;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/06/23 13:44:31  royalts
 *          no message
 *
 */
public class NavigationMenuItem
    extends SelectItem
{
    private String _icon;
    private String _action;
    boolean _split;
    private List _children = Collections.EMPTY_LIST;

    public NavigationMenuItem(Object value, String label, String action, String icon, boolean split)
    {
        super(value, label);
        _action = action;
        _icon = icon;
        _split = split;
    }

    public NavigationMenuItem(Object value,
                              String label,
                              String description,
                              boolean disabled,
                              String action,
                              String icon,
                              boolean split)
    {
        super(value, label, description, disabled);
        _action = action;
        _icon = icon;
        _split = split;
    }

    public String getAction()
    {
        return _action;
    }

    public void setAction(String action)
    {
        _action = action;
    }

    public boolean isSplit()
    {
        return _split;
    }

    public void setSplit(boolean split)
    {
        _split = split;
    }

    public String getIcon()
    {
        return _icon;
    }

    public void setIcon(String icon)
    {
        _icon = icon;
    }

    public List getChildren()
    {
        return _children;
    }

    public void setChildren(List children)
    {
        _children = children;
    }
}

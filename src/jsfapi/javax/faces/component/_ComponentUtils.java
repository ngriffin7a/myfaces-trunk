/*
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
package javax.faces.component;

import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _ComponentUtils
{
    private _ComponentUtils() {}

    static UIComponent findParentNamingContainer(UIComponent component,
                                                 boolean returnRootIfNotFound)
    {
        UIComponent parent = component.getParent();
        if (returnRootIfNotFound && parent == null)
        {
            return component;
        }
        while (parent != null)
        {
            if (parent instanceof NamingContainer) return parent;
            if (returnRootIfNotFound)
            {
                UIComponent nextParent = parent.getParent();
                if (nextParent == null)
                {
                    return parent;  //Root
                }
                parent = nextParent;
            }
            else
            {
                parent = parent.getParent();
            }
        }
        return null;
    }

    static UIComponent getRootComponent(UIComponent component)
    {
        UIComponent parent;
        for(;;)
        {
            parent = component.getParent();
            if (parent == null) return component;
            component = parent;
        }
    }

    static UIComponent findComponent(UIComponent findBase, String id)
    {
        if (id.equals(findBase.getId()))
        {
            return findBase;
        }

        for (Iterator it = findBase.getFacetsAndChildren(); it.hasNext(); )
        {
            UIComponent childOrFacet = (UIComponent)it.next();
            if (!(childOrFacet instanceof NamingContainer))
            {
                UIComponent find = findComponent(childOrFacet, id);
                if (find != null) return find;
            }
            else if (id.equals(childOrFacet.getId()))
            {
                return childOrFacet;
            }
        }

        return null;
    }


}

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
package net.sourceforge.myfaces.renderkit.html.state;

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.tree.TreeUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StateUtils
{
    private StateUtils() {}

    public static void discardInternalAttributes(FacesContext facesContext,
                                                 Tree tree)
    {
        if (MyFacesConfig.isDiscardInternalAttributes(((ServletContext)facesContext.getExternalContext().getContext())))
        {
            List lst = new ArrayList();
            for (Iterator treeIt = TreeUtils.treeIterator(tree); treeIt.hasNext();)
            {
                UIComponent comp = (UIComponent)treeIt.next();
                lst.clear();
                for (Iterator attrIt = comp.getAttributeNames(); attrIt.hasNext();)
                {
                    String attrName = (String)attrIt.next();
                    if (UIComponentUtils.isInternalAttribute(attrName))
                    {
                        lst.add(attrName);
                    }
                }
                for (int i = 0, len = lst.size(); i < len; i++)
                {
                    comp.setAttribute((String)lst.get(i), null);
                }
            }
        }
    }


}

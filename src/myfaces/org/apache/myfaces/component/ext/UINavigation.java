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

import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UINavigation
    extends UIPanel
{
    public static final String TYPE = UINavigation.class.getName();

    public String getComponentType()
    {
        return TYPE;
    }

    public boolean getRendersChildren()
    {
        return true;
    }

    public static class ClickEvent
        extends FacesEvent
    {
        public ClickEvent(UIComponent source)
        {
            super(source);
            if (!(source instanceof UINavigationItem))
            {
                throw new IllegalArgumentException();
            }
        }

        public UINavigationItem getUINavigationItem()
        {
            return (UINavigationItem)super.getSource();
        }
    }

    public boolean processEvent(FacesContext context, FacesEvent event)
    {
        if (event instanceof ClickEvent)
        {
            UINavigationItem item = ((ClickEvent)event).getUINavigationItem();
            if (item.getChildCount() > 0)
            {
                //group
                if (item.isOpen())
                {
                    //close group
                    closeAllChildren(item.getChildren());//close group children
                    item.setOpen(false);
                }
                else
                {
                    closeAllChildren(this.getChildren());//close all items
                    item.setOpen(true);
                    //open all parents
                    UIComponent p = item.getParent();
                    while (p != null && p instanceof UINavigationItem)
                    {
                        ((UINavigationItem)p).setOpen(true);
                        p = p.getParent();
                    }
                }
            }
            else
            {
                //single item
                closeAllChildren(item.getParent().getChildren());//close siblings
                item.setOpen(true);
            }

            String treeId = item.getTreeId();
            String href = item.getHref();
            if (treeId == null)
            {
                if (href != null)
                {
                    treeId = href;
                }
            }
            else
            {
                if (href != null)
                {
                    LogUtil.getLogger().warning("UINavigationItem " + item.getCompoundId() + " has both attributes 'href' and 'treeId'!");
                }
            }

            if (treeId != null)
            {
                TreeFactory tf = (TreeFactory)FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
                Tree responseTree = tf.getTree(context.getServletContext(), treeId);
                responseTree.getRoot().addChild(this);  //HACK(?): Add Navigation, so that response tree has current state info
                context.setResponseTree(responseTree);
            }

            return true;
        }
        return false;
    }

    private void closeAllChildren(Iterator children)
    {
        while (children.hasNext())
        {
            UINavigationItem ni = (UINavigationItem)children.next();
            ni.setOpen(false);
            if (ni.getChildCount() > 0)
            {
                closeAllChildren(ni.getChildren());
            }
        }
    }

}

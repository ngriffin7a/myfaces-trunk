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


package net.sourceforge.myfaces.custom.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 * Default implementation of {@link MutableTreeNode}.
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/04/22 10:20:23  manolito
 *          tree component
 *
 */
public class DefaultMutableTreeNode
        implements MutableTreeNode
{

    private ArrayList children = new ArrayList();
    private Object userObject;
    MutableTreeNode parent;
    private boolean allowsChildren = true;


    public DefaultMutableTreeNode(Object userObject)
    {
        this.userObject = userObject;
    }


    public DefaultMutableTreeNode(ArrayList children, boolean allowsChildren)
    {
        this.children = children;
        this.allowsChildren = allowsChildren;
    }


    public DefaultMutableTreeNode(Object userObject, MutableTreeNode parent, boolean allowsChildren)
    {
        this.userObject = userObject;
        this.parent = parent;
        this.allowsChildren = allowsChildren;
    }


    public void insert(MutableTreeNode child)
    {
        children.add(child);
        child.setParent(this);
    }


    public void insert(MutableTreeNode child, int index)
    {
        children.add(index, child);
        child.setParent(this);
    }


    public void remove(int index)
    {
        MutableTreeNode child = (MutableTreeNode)children.remove(index);
        child.setParent(null);
    }


    public void remove(MutableTreeNode node)
    {
        if (children.remove(node))
        {
            node.setParent(null);
        }
    }


    public void setUserObject(Object object)
    {
        this.userObject = object;
    }


    public Object getUserObject()
    {
        return userObject;
    }


    public void removeFromParent()
    {
        if (parent == null)
        {
            return;
        }
        parent.remove(this);
    }


    public void setParent(MutableTreeNode parent)
    {
        this.parent = parent;
    }


    public TreeNode getChildAt(int index)
    {
        return (TreeNode)children.get(index);
    }


    public int getChildCount()
    {
        return children.size();
    }


    public TreeNode getParent()
    {
        return parent;
    }


    public int getIndex(TreeNode node)
    {
        return children.indexOf(node);
    }


    public boolean getAllowsChildren()
    {
        return allowsChildren;
    }


    public boolean isLeaf()
    {
        return children.isEmpty();
    }


    public Iterator children()
    {
        return Collections.unmodifiableCollection(children).iterator();
    }


    public String toString()
    {
        if (userObject != null)
        {
            return userObject.toString();
        }
        return super.toString();
    }
}

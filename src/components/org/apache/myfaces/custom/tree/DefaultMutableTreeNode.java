/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 *          Revision 1.2  2004/07/01 21:53:07  mwessendorf
 *          ASF switch
 *
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

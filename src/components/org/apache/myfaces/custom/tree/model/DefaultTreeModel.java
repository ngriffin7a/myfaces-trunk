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


package net.sourceforge.myfaces.custom.tree.model;

import net.sourceforge.myfaces.custom.tree.DefaultMutableTreeNode;
import net.sourceforge.myfaces.custom.tree.MutableTreeNode;
import net.sourceforge.myfaces.custom.tree.TreeNode;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/04/22 10:20:24  manolito
 *          tree component
 *
 */
public class DefaultTreeModel
        implements TreeModel
{

    private TreeNode root;
    private ArrayList listeners = new ArrayList();


    public DefaultTreeModel()
    {
        this(new DefaultMutableTreeNode("Root"));
    }


    public DefaultTreeModel(TreeNode root)
    {
        this.root = root;
    }


    public Object getRoot()
    {
        return root;
    }


    public Object getChild(Object parent, int index)
    {
        return ((TreeNode)parent).getChildAt(index);
    }


    public int getChildCount(Object parent)
    {
        return ((TreeNode)parent).getChildCount();
    }


    public boolean isLeaf(Object node)
    {
        return ((TreeNode)node).isLeaf();
    }


    public void valueForPathChanged(TreePath path, Object newValue)
    {
        MutableTreeNode node = (MutableTreeNode)path.getLastPathComponent();

        node.setUserObject(newValue);
    }


    public int getIndexOfChild(Object parent, Object child)
    {
        return ((TreeNode)parent).getIndex((TreeNode)child);
    }


    public void addTreeModelListener(TreeModelListener l)
    {
        listeners.add(l);
    }


    public void removeTreeModelListener(TreeModelListener l)
    {
        listeners.remove(l);
    }


    /**
     * Invoke this method after you've changed how node is to be
     * represented in the tree.
     */
    public void nodeChanged(TreeNode node)
    {
        if (!listeners.isEmpty())
        {
            // nobody cares
            return;
        }

        if (node != null)
        {
            TreeNode parent = node.getParent();

            if (parent != null)
            {
                int index = parent.getIndex(node);
                if (index != -1)
                {
                    int[] childIndices = new int[1];

                    childIndices[0] = index;
                    nodesChanged(parent, childIndices);
                }
            }
            else if (node == getRoot())
            {
                nodesChanged(node, null);
            }
        }
    }


    /**
     * Invoke this method after you've changed how the children identified by
     * childIndicies are to be represented in the tree.
     */
    public void nodesChanged(TreeNode node, int[] childIndices)
    {
        if (!listeners.isEmpty())
        {
            // nobody cares
            return;
        }

        if (node != null)
        {
            if (childIndices != null)
            {
                int count = childIndices.length;

                if (count > 0)
                {
                    Object[] children = new Object[count];

                    for (int i = 0; i < count; i++)
                    {
                        children[i] = node.getChildAt(childIndices[i]);
                    }
                    fireTreeNodesChanged(this, getPathToRoot(node), childIndices, children);
                }
            }
            else if (node == root)
            {
                fireTreeNodesChanged(this, getPathToRoot(node), null, null);
            }
        }
    }


    /**
     * Invoke this method if you've totally changed the children of
     * node and its childrens children...  This will post a
     * treeStructureChanged event.
     */
    public void nodeStructureChanged(TreeNode node)
    {
        if (!listeners.isEmpty())
        {
            // nobody cares
            return;
        }

        if (node != null)
        {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }


    /**
     * Invoke this method after you've inserted some TreeNodes into
     * node.  childIndices should be the index of the new elements and
     * must be sorted in ascending order.
     */
    public void nodesWereInserted(TreeNode node, int[] childIndices)
    {
        if (listeners.isEmpty())
        {
            // nobody cares
            return;
        }
        if (node != null && childIndices != null && childIndices.length > 0)
        {
            int cCount = childIndices.length;
            Object[] newChildren = new Object[cCount];

            for (int counter = 0; counter < cCount; counter++)
            {
                newChildren[counter] = node.getChildAt(childIndices[counter]);
            }
            fireTreeNodesInserted(this, getPathToRoot(node), childIndices,
                                  newChildren);
        }
    }


    /**
     * Invoke this method after you've removed some TreeNodes from
     * node.  childIndices should be the index of the removed elements and
     * must be sorted in ascending order. And removedChildren should be
     * the array of the children objects that were removed.
     */
    public void nodesWereRemoved(TreeNode node, int[] childIndices, Object[] removedChildren)
    {
        if (listeners.isEmpty())
        {
            // nobody cares
            return;
        }
        if (node != null && childIndices != null)
        {
            fireTreeNodesRemoved(this, getPathToRoot(node), childIndices, removedChildren);
        }
    }


    /**
     * Collect all parent nodes up to the root node.
     *
     * @param node the TreeNode to get the path for
     */
    public TreeNode[] getPathToRoot(TreeNode node)
    {
        return getPathToRoot(node, 0);
    }


    /**
     * Recursivly collect parent nodes up the the root node.
     *
     * @param node  the TreeNode to get the path for
     * @param depth number of steps already taken towards the root (on recursive calls)
     * @return an array giving the path from the root to the specified node
     */
    protected TreeNode[] getPathToRoot(TreeNode node, int depth)
    {
        TreeNode[] answer;

        if (node == null)
        {
            if (depth == 0)
            {
                // nothing to do
                return null;
            }
            else
            {
                // end recursion
                answer = new TreeNode[depth];
            }
        }
        else
        {
            // recurse
            depth++;

            if (node == root)
            {
                // we found the root node
                answer = new TreeNode[depth];
            }
            else
            {
                answer = getPathToRoot(node.getParent(), depth);
            }
            answer[answer.length - depth] = node;
        }
        return answer;
    }


    /**
     * Notify all listeners of a node change.
     *
     * @param source       the node being changed
     * @param path         the path to the root node
     * @param childIndices the indices of the changed elements
     * @param children     the changed elements
     */
    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children)
    {
        TreeModelEvent event = null;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
        {
            TreeModelListener listener = (TreeModelListener)iterator.next();
            if (event == null)
            {
                event = new TreeModelEvent(source, path, childIndices, children);
            }
            listener.treeNodesChanged(event);
        }
    }


    /**
     * Notify all listeners of structure change.
     *
     * @param source       the node where new elements are being inserted
     * @param path         the path to the root node
     * @param childIndices the indices of the new elements
     * @param children     the new elements
     */
    protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children)
    {
        TreeModelEvent event = null;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
        {
            TreeModelListener listener = (TreeModelListener)iterator.next();
            if (event == null)
            {
                event = new TreeModelEvent(source, path, childIndices, children);
            }
            listener.treeNodesInserted(event);
        }
    }


    /**
     * Notify all listeners of structure change.
     *
     * @param source       the node where elements are being removed
     * @param path         the path to the root node
     * @param childIndices the indices of the removed elements
     * @param children     the removed elements
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children)
    {
        TreeModelEvent event = null;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
        {
            TreeModelListener listener = (TreeModelListener)iterator.next();
            if (event == null)
            {
                event = new TreeModelEvent(source, path, childIndices, children);
            }
            listener.treeNodesRemoved(event);
        }
    }


    /**
     * Notify all listeners of structure change.
     *
     * @param source       the node where the tree model has changed
     * @param path         the path to the root node
     * @param childIndices the indices of the affected elements
     * @param children     the affected elements
     */
    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children)
    {
        TreeModelEvent event = null;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
        {
            TreeModelListener listener = (TreeModelListener)iterator.next();
            if (event == null)
            {
                event = new TreeModelEvent(source, path, childIndices, children);
            }
            listener.treeStructureChanged(event);
        }
    }


    /**
     * Notify all listeners of structure change.
     *
     * @param source the node where the tree model has changed
     * @param path   the path to the root node
     */
    protected void fireTreeStructureChanged(Object source, TreePath path)
    {
        TreeModelEvent event = null;
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
        {
            TreeModelListener listener = (TreeModelListener)iterator.next();
            if (event == null)
            {
                event = new TreeModelEvent(source, path);
            }
            listener.treeStructureChanged(event);
        }
    }

}

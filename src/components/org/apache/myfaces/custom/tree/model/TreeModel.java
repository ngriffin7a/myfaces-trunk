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


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/04/22 10:20:24  manolito
 *          tree component
 *
 */
public interface TreeModel
{

    /**
     * Return the root of the tree.
     *
     * @return the root of the tree or null, it this tree has no nodes
     */
    public Object getRoot();


    /**
     * Return the child of <code>parent</code> at index <code>index</code>
     * in the parent's child array.
     *
     * @param parent a node in the tree
     * @return the child of <code>parent</code> at index <code>index</code>
     */
    public Object getChild(Object parent, int index);


    /**
     * Answer the number of children of <code>parent</code>.
     *
     * @param parent a node in the tree
     * @return the number of children of the node <code>parent</code>
     */
    public int getChildCount(Object parent);


    /**
     * Answer <code>true</code> if <code>node</code> is a leaf.
     *
     * @param node a node in the tree
     * @return true if <code>node</code> is a leaf
     */
    public boolean isLeaf(Object node);


    /**
     * Called when the value for the item identified
     * by <code>path</code> has changed to <code>newValue</code>.
     * If <code>newValue</code> signifies a truly new value
     * the model should post a <code>treeNodesChanged</code> event.
     *
     * @param path     path to the node that has been altered
     * @param newValue the new value from the TreeCellEditor
     */
    public void valueForPathChanged(TreePath path, Object newValue);


    /**
     * Return the index of child in parent.
     *
     * @param parent a node in the tree
     * @param child  the node we are interested in
     * @return the index of the child in the parent, or -1 if either
     *         <code>child</code> or <code>parent</code> are <code>null</code>
     */
    public int getIndexOfChild(Object parent, Object child);


    /**
     * Adds a listener for the <code>TreeModelEvent</code>
     * posted after the tree changes.
     *
     * @param l the listener to add
     */
    void addTreeModelListener(TreeModelListener l);


    /**
     * Removes a listener previously added with
     * <code>addTreeModelListener</code>.
     *
     * @param l the listener to remove
     */
    void removeTreeModelListener(TreeModelListener l);

}

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

/**
 * Defines the requirements for a tree node object that can change -- by adding or removing
 * child nodes, or by changing the contents of a user object stored in the node.
 * (inspired by javax.swing.tree.MutableTreeNode).
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/04/22 10:20:23  manolito
 *          tree component
 *
 */
public interface MutableTreeNode
        extends TreeNode
{

    /**
     * Add the given child to the children of this node.
     * This will set this node as the parent of the child using {#setParent}.
     */
    void insert(MutableTreeNode child);


    /**
     * Add the given child to the children of this node at index.
     * This will set this node as the parent of the child using {#setParent}.
     */
    void insert(MutableTreeNode child, int index);


    /**
     * Remove the child at the given index.
     */
    void remove(int index);


    /**
     * Remove the given node.
     */
    void remove(MutableTreeNode node);


    /**
     * Sets the user object of this node.
     */
    void setUserObject(Object object);


    /**
     * Remove this node from its parent.
     */
    void removeFromParent();


    /**
     * Set the parent node.
     */
    void setParent(MutableTreeNode parent);
}

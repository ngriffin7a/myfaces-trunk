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

import java.util.Iterator;

/**
 * Defines the requirements for an object that can be used as a tree node for {@link HtmlTree}.
 * (inspired by javax.swing.tree.TreeNode).
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/04/22 10:20:23  manolito
 *          tree component
 *
 */
public interface TreeNode
{

    /**
     * Answer the child at the given index.
     */
    TreeNode getChildAt(int childIndex);


    /**
     * Answer the number of children this node contains.
     */
    int getChildCount();


    /**
     * Answer the parent of this node.
     */
    TreeNode getParent();


    /**
     * Answer the index of the given node in this node's children.
     */
    int getIndex(TreeNode node);


    /**
     * Answer true if this node allows children.
     */
    boolean getAllowsChildren();


    /**
     * Answer true if this is a leaf node.
     */
    boolean isLeaf();


    /**
     * Answer the children of the receiver. The base collection is unmodifyable.
     */
    Iterator children();

}

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

import java.io.Serializable;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/04/22 10:20:24  manolito
 *          tree component
 *
 */
public final class TreePath
        extends Object
        implements Serializable
{


    private Object[] elements;


    /**
     * Construct a pathElements from an array of Objects
     *
     * @param pathElements an array of Objects representing the pathElements to a node
     */
    public TreePath(Object[] pathElements)
    {
        if (pathElements == null || pathElements.length == 0)
        {
            throw new IllegalArgumentException("pathElements must be non null and not empty");
        }

        elements = pathElements;
    }


    /**
     * Construct a new TreePath, which is the path identified by
     * parent ending in lastElement.
     */
    protected TreePath(TreePath parent, Object lastElement)
    {
        elements = new Object[parent.elements.length + 1];

        System.arraycopy(parent.elements, 0, elements, 0, elements.length - 1);
        elements[elements.length - 1] = lastElement;
    }


    /**
     * Construct a new TreePath from an array of objects.
     *
     * @param pathElements path elements
     * @param length       lenght of the new path
     */
    protected TreePath(Object[] pathElements, int length)
    {
        Object[] elements = new Object[length];

        System.arraycopy(pathElements, 0, elements, 0, length);
    }


    /**
     * Return an array of Objects containing the components of this
     * TreePath.
     *
     * @return an array of Objects representing the TreePath
     */
    public Object[] getPath()
    {
        Object[] answer = new Object[elements.length];
        System.arraycopy(elements, 0, answer, 0, elements.length);
        return answer;
    }


    /**
     * Returns the last component of this path.
     *
     * @return the Object at the end of the path
     */
    public Object getLastPathComponent()
    {
        return elements[elements.length - 1];
    }


    /**
     * Return the number of elements in the path.
     *
     * @return an int giving a count of items the path
     */
    public int getPathCount()
    {
        return elements.length;
    }


    /**
     * Return the path component at the specified index.
     *
     * @param index int specifying an index in the path
     * @return the Object at that index location
     * @throws IllegalArgumentException if the index is beyond the length
     *                                  of the path
     */
    public Object getPathComponent(int index)
    {
        if (index < 0 || index >= elements.length)
        {
            throw new IllegalArgumentException("Index " + index + " is out of range");
        }

        return elements[index];
    }


    /**
     * Test two TreePaths for equality by checking each element of the
     * paths for equality. Two paths are considered equal if they are of
     * the same length and all element positions are equal.
     *
     * @param o the Object to compare
     */
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (!(o instanceof TreePath))
        {
            return false;
        }
        TreePath other = (TreePath)o;

        if (elements.length != other.elements.length)
        {
            return false;
        }

        for (int i = 0; i < elements.length; i++)
        {
            Object thisElement = elements[i];
            Object otherElement = other.elements[i];

            if (!thisElement.equals(otherElement))
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Return the hashCode for the object. The hash code of a TreePath
     * is defined to be the hash code of the last component in the path.
     *
     * @return the hashCode for the object
     */
    public int hashCode()
    {
        return elements[elements.length - 1].hashCode();
    }


    /**
     * Return true if <code>path</code> is a
     * descendant of this
     * TreePath. A TreePath P1 is a descendent of a TreePath P2
     * if P1 contains all of the components that make up
     * P2's path. If P1 and P2 are equal P2 is not considered a descendant of
     * P1.
     *
     * @return true if <code>path</code> is a descendant of this path
     */
    public boolean isDescendant(TreePath path)
    {
        if (path == null)
        {
            return false;
        }

        if (elements.length < path.elements.length)
        {
            // Can't be a descendant, has fewer components in the path.
            return false;
        }

        for (int i = 0; i < elements.length; i++)
        {
            Object thisElement = elements[i];
            Object otherElement = path.elements[i];

            if (!thisElement.equals(otherElement))
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Return a new path by appending child to this path.
     *
     * @param child element to append
     * @return new path
     * @throws NullPointerException if child is null
     */
    public TreePath pathByAddingChild(Object child)
    {
        if (child == null)
        {
            throw new NullPointerException("Null child not allowed");
        }

        return new TreePath(this, child);
    }


    /**
     * Return a path containing all the elements of this object, except
     * the last path component.
     */
    public TreePath getParentPath()
    {
        return new TreePath(elements, elements.length - 1);
    }


    /**
     * Return a string that displays and identifies this
     * object's properties.
     *
     * @return a String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer("[");

        for (int i = 0; i < elements.length; i++)
        {
            if (i > 0)
            {
                buffer.append(", ");
            }
            buffer.append(elements[i]);

        }

        buffer.append("]");
        return buffer.toString();
    }
}


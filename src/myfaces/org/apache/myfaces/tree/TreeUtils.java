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
package net.sourceforge.myfaces.tree;

import net.sourceforge.myfaces.util.bean.BeanUtils;

import javax.faces.component.UIComponent;
import javax.faces.tree.Tree;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TreeUtils
{
    private TreeUtils() {}

    public static Iterator treeIterator(Tree tree)
    {
        return treeIterator(tree.getRoot());
    }

    public static Iterator treeIterator(UIComponent root)
    {
        return new TreeIterator(root);
        //return getSimpleIterator(root);
    }

    private static class TreeIterator
        implements Iterator
    {
        private UIComponent _next = null;
        private boolean _mayHaveNext = true;
        private UIComponent _current = null;
        private Stack _stack = new Stack();

        public TreeIterator(UIComponent root)
        {
            _next = root;
            _current = null;
        }

        public boolean hasNext()
        {
            return getNext() != null;
        }

        public Object next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            _current = _next;
            _next = null;
            return _current;
        }

        private UIComponent getNext()
        {
            if (_next == null && _mayHaveNext)
            {
                //has child?
                Iterator children = _current.getFacetsAndChildren();
                if (children.hasNext())
                {
                    _next = (UIComponent)children.next();
                    //push siblings
                    _stack.push(children);
                }
                else
                {
                    //has next sibling?
                    for (;;)
                    {
                        if (_stack.empty())
                        {
                            _next = null;
                            _mayHaveNext = false;
                            break;
                        }

                        Iterator currentSiblings = (Iterator)_stack.peek();
                        if (currentSiblings.hasNext())
                        {
                            _next = (UIComponent)currentSiblings.next();
                            break;
                        }
                        else
                        {
                            _stack.pop();
                        }
                    }
                }
            }
            return _next;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }



    /*
    private static Iterator getSimpleIterator(UIComponent comp)
    {
        List list = new ArrayList();
        addComponentToList(list, comp);
        return list.iterator();
    }

    private static void addComponentToList(List list, UIComponent comp)
    {
        list.add(comp);
        for (Iterator it = comp.getFacetsAndChildren(); it.hasNext();)
        {
            addComponentToList(list, (UIComponent)it.next());
        }
    }
    */

    public static void printTree(Tree tree)
    {
        printTree(tree, System.out);
    }

    public static void printTree(Tree tree, PrintStream stream)
    {
        printComponent(tree.getRoot(), stream, 0, true);
    }

    public static void printComponent(UIComponent comp, PrintStream stream)
    {
        printComponent(comp, stream, 0, false);
    }

    private static void printComponent(UIComponent comp,
                                     PrintStream stream,
                                     int indent,
                                     boolean recursive)
    {
        printIndent(stream, indent);
        stream.print('<');
        stream.print(comp.getClass().getName());

        /*
        stream.print(" component=\"");
        stream.print(comp);
        stream.print("\"");
        */

        //printProperty(stream, comp, CommonComponentProperties.COMPONENT_ID_ATTR, "id");
        //printProperty(stream, comp, CommonComponentProperties.RENDERER_TYPE_ATTR, "rendererType");
        /*
        if (comp instanceof UIOutput)
        {
            //printProperty(stream, comp, CommonComponentProperties.VALUE_PROP);
            //printProperty(stream, comp, CommonComponentProperties.VALUE_REF_PROP);
            printProperty(stream, comp, net.sourceforge.myfaces.component.UIOutput.VALUE_PROP);
            printProperty(stream, comp, net.sourceforge.myfaces.component.UIOutput.VALUE_REF_PROP);
        }
        if (comp instanceof UICommand)
        {
            printProperty(stream, comp, net.sourceforge.myfaces.component.UICommand.ACTION_PROP);
            printProperty(stream, comp, net.sourceforge.myfaces.component.UICommand.ACTION_REF_PROP);
        }
        */

        BeanInfo beanInfo = BeanUtils.getBeanInfo(comp);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            if (propertyDescriptor.getReadMethod() != null &&
                propertyDescriptor.getWriteMethod() != null)
            {
                printProperty(stream, comp, propertyDescriptor.getName());
            }
        }

        for (Iterator it = comp.getAttributeNames(); it.hasNext();)
        {
            String attrName = (String)it.next();
            printAttribute(stream, comp, attrName);
        }

        /*
        if (comp instanceof UICommand)
        {
            List[] listeners = (((UICommand)comp).getListeners());
            if (listeners != null)
            {
                stream.println();
                stream.println(listeners.length + " Listeners");
            }
        }
        */

        if (recursive)
        {
            Iterator children = comp.getFacetsAndChildren();
            if (children.hasNext())
            {
                stream.println('>');
                while (children.hasNext())
                {
                    UIComponent child = (UIComponent)children.next();
                    printComponent(child, stream, indent + 1, true);
                }
                printIndent(stream, indent);
                stream.print("</");
                stream.print(comp.getClass().getName());
                stream.println('>');
            }
            else
            {
                stream.println("/>");
            }
        }
        else
        {
            stream.println("/>");
        }
    }

    private static void printAttribute(PrintStream stream,
                                       UIComponent comp,
                                       String attrName)
    {
        printAttribute(stream, comp, attrName, attrName);
    }

    private static void printAttribute(PrintStream stream,
                                       UIComponent comp,
                                       String attrName,
                                       String prettyAttrName)
    {
        Object v = comp.getAttribute(attrName);
        if (v != null)
        {
            stream.print(' ');
            stream.print(prettyAttrName);
            stream.print("=\"");
            stream.print(v.toString());
            stream.print("\"");
        }
    }

    private static void printProperty(PrintStream stream,
                                      UIComponent comp,
                                      String propName)
    {
        printProperty(stream, comp, propName, propName);
    }

    private static void printProperty(PrintStream stream,
                                       UIComponent comp,
                                       String propName,
                                       String prettyPropName)
    {
        Object v = null;
        try
        {
            v = BeanUtils.getBeanPropertyValue(comp, propName);
            if (v == null) v = "NULL";
        }
        catch (Exception e)
        {
        }
        stream.print(' ');
        stream.print(prettyPropName);
        stream.print("=\"");
        if (v != null)
        {
            stream.print(v.toString());
        }
        else
        {
            stream.print("?");
        }
        stream.print("\"");
    }

    private static void printIndent(PrintStream stream, int depth)
    {
        for (int i = 0; i < depth; i++)
        {
            stream.print("  ");
        }
    }

}

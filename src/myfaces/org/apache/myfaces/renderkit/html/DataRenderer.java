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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.component.UIPanel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DataRenderer
        extends HTMLRenderer
{
    public static final String ITERATOR_ATTR = DataRenderer.class.getName() + ".ITERATOR";

    public static final String TYPE = "Data";
    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException
    {
        encodeSubTree(context, component);
        String varAttr = (String)component.getAttribute(UIPanel.VAR_ATTR);

        for (Iterator it = getIterator(context, component); it.hasNext();)
        {
            Object varObj = it.next();

            // TODO: implement varAttr as a stack? (nested lists)
            context.getServletRequest().setAttribute(varAttr, varObj);

            ResponseWriter writer = context.getResponseWriter();

            UIComponent parentComponent = component.getParent();

            String style = null;
            if (parentComponent.getComponentType().equals(javax.faces.component.UIPanel.TYPE))
            {
                String rowClasses = (String)parentComponent.getAttribute(UIPanel.ROW_CLASSES_ATTR);
                if (rowClasses != null && rowClasses.length() > 0)
                {
                    StringTokenizer tokenizer = new StringTokenizer(rowClasses, ",");
                    if (tokenizer.hasMoreTokens())
                    {
                        style = tokenizer.nextToken();
                    }
                    else
                    {
                        style = rowClasses;
                    }
                }
            }

            writer.write("<tr>");

            for (Iterator children = component.getChildren(); children.hasNext();)
            {
                writer.write("<td");
                if (style != null && style.length() > 0)
                {
                    writer.write(" class=\"");
                    writer.write(style);
                    writer.write("\"");
                }
                writer.write(">");


                UIComponent childComponent = (UIComponent)children.next();
                encodeComponent(context, childComponent);

                writer.write("</td>\n");
            }

            writer.write("</tr>");
        }

        context.getServletRequest().removeAttribute(varAttr);
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIPanel.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UIPanel;
    }


    private Iterator getIterator(FacesContext facesContext, UIComponent uiComponent)
    {
        Iterator iterator = (Iterator)uiComponent.getAttribute(ITERATOR_ATTR);
        if (iterator == null)
        {
            Object v = uiComponent.currentValue(facesContext);
            if (v instanceof Iterator)
            {
                iterator = (Iterator)v;
            }
            else if (v instanceof Collection)
            {
                iterator = ((Collection)v).iterator();
            }
            else if (v instanceof Object[])
            {
                iterator = Arrays.asList((Object[])v).iterator();
            }
            else if (v instanceof Iterator)
            {
                iterator = (Iterator)v;
            }
            else
            {
                throw new IllegalArgumentException("Value of component " + uiComponent.getCompoundId() + " is neither collection nor array.");
            }
            uiComponent.setAttribute(ITERATOR_ATTR, iterator);
        }
        return iterator;
    }

}

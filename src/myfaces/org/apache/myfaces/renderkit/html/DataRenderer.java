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

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DataRenderer
        extends HTMLRenderer
{
    public static final String ITERATOR_ATTR = DataRenderer.class.getName() + ".ITERATOR";

    public static final String TYPE = "DataRenderer";
    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        Iterator it = getIterator(context, component);
        String varAttr = (String)component.getAttribute(UIPanel.VAR_ATTR);
        if (it.hasNext())
        {
            Object varObj = it.next();
            context.getServletRequest().setAttribute(varAttr, varObj);
            ResponseWriter writer = context.getResponseWriter();
            writer.write("<tr>");
        }
        else
        {
            context.getServletRequest().removeAttribute(varAttr);
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("</tr>");
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
            else
            {
                throw new IllegalArgumentException("Value of component " + uiComponent.getCompoundId() + " is neither collection nor array.");
            }
            uiComponent.setAttribute(ITERATOR_ATTR, iterator);
        }
        return iterator;
    }

}

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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.component.ext.UINavigation;
import net.sourceforge.myfaces.component.ext.UINavigationItem;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationRenderer
    extends HTMLRenderer
{
    public static final String TYPE = "NavigationRenderer";

    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UINavigation.TYPE);
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof UINavigation;
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<table border=\"0\">");
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</table>");
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        renderChildren(facesContext, 0, uiComponent.getChildren());
    }

    protected void renderChildren(FacesContext facesContext, int level, Iterator children)
        throws IOException
    {
        while(children.hasNext())
        {
            UIComponent child = (UIComponent)children.next();
            if (child.getComponentType().equals(UINavigationItem.TYPE))
            {
                renderMenuItem(facesContext, level, (UINavigationItem)child);
            }
            else
            {
                throw new FacesException("Unexpected component of type " + child.getComponentType());
            }
        }
    }

    protected void renderMenuItem(FacesContext facesContext, int level, UINavigationItem item)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("\n<tr><td>");
        for (int i = 0; i < level; i++)
        {
            writer.write("&nbsp;");
        }
        if (level > 0)
        {
            writer.write("<font size=\"-" + level + "\">");
        }

        item.encodeBegin(facesContext);
        writer.write(HTMLEncoder.encode(item.getLabel(), true, true));
        item.encodeEnd(facesContext);

        if (level > 0)
        {
            writer.write("</font>");
        }
        writer.write("</td></tr>");

        if (item.isOpen())
        {
            renderChildren(facesContext, level + 1, item.getChildren());
        }

    }


}

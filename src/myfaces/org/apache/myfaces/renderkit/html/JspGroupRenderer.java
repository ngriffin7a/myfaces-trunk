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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspGroupRenderer
    extends AbstractPanelRenderer
{
    public static final String TYPE = "Group";

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UIPanel;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UIPanel.TYPE);
    }

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext context, UIComponent uicomponent)
            throws IOException
    {
        UIComponent parent = uicomponent.getParent();

        if (parent.getRendererType().equals(ListRenderer.TYPE))
        {
            // new row
            writeNewRow(context);
        }
        else if (parent.getRendererType().equals(JspDataRenderer.TYPE) &&
                 parent.getParent().getRendererType().equals(ListRenderer.TYPE))
        {
            writeColumnStart(context, uicomponent);
        }
        else
        {
            if (parent.getRendererType().equals(GroupRenderer.TYPE) &&
                parent.getParent().getRendererType().equals(ListRenderer.TYPE))
            {
                writeColumnStart(context, parent);
            }

            // renderer can never be null ;)
            Renderer renderer = getOriginalRenderer(context, uicomponent);
            // TODO: Refactor
            restoreRenderKit(context, uicomponent);
            renderer.encodeBegin(context, uicomponent);
            storeRenderKit(context, uicomponent);
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uicomponent) throws IOException
    {
        UIComponent parent = uicomponent.getParent();

        if (parent.getRendererType().equals(ListRenderer.TYPE))
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.write("</tr>\n");
        }
        else if (parent.getRendererType().equals(JspDataRenderer.TYPE) &&
                 parent.getParent().getRendererType().equals(ListRenderer.TYPE))
        {

        }
        else
        {
            // renderer can never be null ;)
            Renderer renderer = getOriginalRenderer(facesContext, uicomponent);
            // TODO: Refactor
            restoreRenderKit(facesContext, uicomponent);
            renderer.encodeEnd(facesContext, uicomponent);
            storeRenderKit(facesContext, uicomponent);

            if ((parent.getRendererType().equals(GroupRenderer.TYPE)) &&
                parent.getParent().getRendererType().equals(ListRenderer.TYPE))
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write("</td>\n");
            }
        }
    }
}



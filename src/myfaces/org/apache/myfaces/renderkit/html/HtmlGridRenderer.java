/**
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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlGridRenderer
    extends HtmlRenderer
{
    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(context, component,
                HtmlPanelGrid.class);

        HtmlPanelGrid grid = (HtmlPanelGrid) component;

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, grid);

        List children = grid.getChildren();

        int i=0;

        //FIXME: render attributes...
        for (; i < children.size(); i++)
        {
            if((i%grid.getColumns())==0)
            {
                writer.startElement(HTML.TR_ELEM, grid);
            }
            else if(((i+1)%grid.getColumns())==0)
            {
                writer.endElement(HTML.TR_ELEM);
            }

            writer.startElement(HTML.TD_ELEM, grid);

            UIComponent uiComponent = (UIComponent) children.get(i);

            renderChild(context, uiComponent);

            writer.endElement(HTML.TD_ELEM);
        }

        if(i>0)
            writer.endElement(HTML.TR_ELEM);

        renderChild(context, component);
        writer.endElement(HTML.TABLE_ELEM);

    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException
    {
    }

    protected void renderChild(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.renderChild(facesContext, component);
    }
}

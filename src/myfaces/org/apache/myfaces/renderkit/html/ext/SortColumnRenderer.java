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

import net.sourceforge.myfaces.renderkit.html.HyperlinkRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * DOCUMENT ME!
 *
 * The value of the associated UICommand
 *
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SortColumnRenderer
    extends HyperlinkRenderer
{
    public static final String ASCENDING_REFERENCE_ATTR = "ascendingReference";

    public static final String TYPE = "SortColumn";
    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        super.encodeBegin(facesContext, uiComponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        super.encodeEnd(facesContext, uiComponent);

        Boolean asc = getSortAscending(facesContext, uiComponent);
        if (asc != null)
        {
            if (asc.booleanValue())
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write("&#x2191;");
            }
            else
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write("&#x2193;");
            }
        }
    }

    private Boolean getSortAscending(FacesContext facesContext, UIComponent uiComponent)
    {
        UIComponent parent = uiComponent.getParent();

        String column = (String)uiComponent.currentValue(facesContext);
        String currentSortColumn = (String)parent.currentValue(facesContext);
        String ascendingRef = (String)parent.getAttribute(ASCENDING_REFERENCE_ATTR);
        if (currentSortColumn == null || ascendingRef == null)
        {
            throw new IllegalArgumentException("Parent has no value or ascendingReference attribute");
        }

        if (column.equals(currentSortColumn))
        {
            Boolean asc = (Boolean)facesContext.getModelValue(ascendingRef);
            return asc;
        }
        else
        {
            return null;
        }
    }


}

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

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SortColumnRenderer
    extends HyperlinkRenderer
{
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

        int sort = getActualSort(facesContext, uiComponent);
        switch (sort)
        {
            case SORT_COLUMN_ASC:
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write("&#x2191;");
                break;
            }
            case SORT_COLUMN_DESC:
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write("&#x2193;");
                break;
            }
        }
    }

    private static final int NO_SORT_COLUMN = 0;
    private static final int SORT_COLUMN_ASC = 1;
    private static final int SORT_COLUMN_DESC = 2;

    private int getActualSort(FacesContext facesContext, UIComponent uiComponent)
    {
        String modRef = uiComponent.getParent().getModelReference();
        if (modRef != null)
        {
            Object sort = facesContext.getModelValue(modRef);
            if (uiComponent instanceof UICommand)
            {
                String commandName = ((UICommand)uiComponent).getCommandName();
                if (commandName != null
                    && sort != null &&
                    sort.toString().startsWith(commandName))
                {
                    if (sort.equals(commandName))
                    {
                        return SORT_COLUMN_ASC;
                    }
                    return SORT_COLUMN_DESC;
                }
            }
        }
        return NO_SORT_COLUMN;
    }


}

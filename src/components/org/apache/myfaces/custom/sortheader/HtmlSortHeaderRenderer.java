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
package net.sourceforge.myfaces.custom.sortheader;

import net.sourceforge.myfaces.component.UserRoleUtils;
import net.sourceforge.myfaces.component.html.ext.HtmlDataTable;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HtmlLinkRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/05/18 14:31:38  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.2  2004/04/22 09:20:55  manolito
 * derive from HtmlLinkRendererBase instead of HtmlLinkRenderer
 *
 */
public class HtmlSortHeaderRenderer
        extends HtmlLinkRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlSortHeaderRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, HtmlCommandSortHeader.class);

        if (UserRoleUtils.isEnabledOnUserRole(component))
        {
            HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader)component;
            HtmlDataTable dataTable = sortHeader.findParentDataTable();

            if (sortHeader.getColumnName().equals(dataTable.getSortColumn()))
            {
                ResponseWriter writer = facesContext.getResponseWriter();

                if (dataTable.isSortAscending())
                {
                    writer.write("&#x2191;");
                }
                else
                {
                    writer.write("&#x2193;");
                }
            }
        }
        super.encodeEnd(facesContext, component);
    }

}

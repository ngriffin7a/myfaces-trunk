/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * Revision 1.5  2004/07/01 21:53:10  mwessendorf
 * ASF switch
 *
 * Revision 1.4  2004/06/04 12:10:35  royalts
 * added check on isArrow
 *
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

            if (sortHeader.isArrow() && sortHeader.getColumnName().equals(dataTable.getSortColumn()))
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

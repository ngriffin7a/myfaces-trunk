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

package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.component.UserRoleUtils;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HtmlLinkRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/06/08 02:05:14  o_rossmueller
 * as for JSF 1.1 value is rendered as link text by standard link renderer
 *
 * Revision 1.1  2004/05/18 14:31:38  manolito
 * user role support completely moved to components source tree
 *
 */
public class HtmlLinkRenderer
        extends HtmlLinkRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlLinkRenderer.class);

    protected void renderCommandLinkStart(FacesContext facesContext,
                                          UIComponent component,
                                          String clientId,
                                          Object value,
                                          String style,
                                          String styleClass) throws IOException
    {
        //if link is disabled we render the nested components without the anchor
        if (UserRoleUtils.isEnabledOnUserRole(component))
        {
            super.renderCommandLinkStart(facesContext, component, clientId, value, style, styleClass);
        }
    }

    protected void renderOutputLinkStart(FacesContext facesContext, UIOutput output) throws IOException
    {
        //if link is disabled we render the nested components without the anchor
        if (UserRoleUtils.isEnabledOnUserRole(output))
        {
            super.renderOutputLinkStart(facesContext, output);
        }
    }

    protected void renderLinkEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        //if link is disabled we render the nested components without the anchor
        if (UserRoleUtils.isEnabledOnUserRole(component))
        {
            super.renderLinkEnd(facesContext, component);
        }
    }

}

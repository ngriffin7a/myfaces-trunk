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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.component.UserRoleUtils;
import net.sourceforge.myfaces.renderkit.html.HtmlLinkRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/07/01 21:53:06  mwessendorf
 * ASF switch
 *
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

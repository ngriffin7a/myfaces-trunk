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
package org.apache.myfaces.renderkit.html.ext;

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.renderkit.html.HtmlTextRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/10/13 11:50:59  matze
 * renamed packages to org.apache
 *
 * Revision 1.2  2004/07/01 21:53:06  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/05/18 14:31:38  manolito
 * user role support completely moved to components source tree
 *
 */
public class HtmlTextRenderer
        extends HtmlTextRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlTextRenderer.class);
    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        if (!UserRoleUtils.isEnabledOnUserRole(uiComponent))
        {
            return false;
        }
        else
        {
            return super.isDisabled(facesContext, uiComponent);
        }
    }
}

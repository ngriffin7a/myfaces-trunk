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
package org.apache.myfaces.custom.tree.renderkit.html;

import org.apache.myfaces.custom.tree.HtmlTreeNode;
import org.apache.myfaces.renderkit.html.HtmlLinkRendererBase;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *
 * 
 *          $Log$
 *          Revision 1.8  2004/11/26 12:14:09  oros
 *          MYFACES-8: applied tree table patch by David Le Strat
 *
 *          Revision 1.7  2004/10/13 11:50:58  matze
 *          renamed packages to org.apache
 *
 *          Revision 1.6  2004/07/01 21:53:10  mwessendorf
 *          ASF switch
 *
 *          Revision 1.5  2004/06/09 20:32:16  o_rossmueller
 *          fix: removed duplicate output of value
 *
 *          Revision 1.4  2004/06/03 12:57:03  o_rossmueller
 *          modified link renderer to use one hidden field for all links according to 1.1 renderkit docs
 *          added onclick=clear_XXX to button
 *
 *          Revision 1.3  2004/05/27 15:06:39  manolito
 *          bugfix: node labels not rendered any more
 *
 *          Revision 1.2  2004/04/29 18:48:07  o_rossmueller
 *          node selection handling
 *
 *          Revision 1.1  2004/04/22 10:20:24  manolito
 *          tree component
 *
 */
public class HtmlTreeNodeRenderer
        extends HtmlLinkRendererBase
{


    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
        String clientId = component.getClientId(facesContext);
        String reqValue = (String)facesContext.getExternalContext().getRequestParameterMap().get(HtmlRendererUtils.getHiddenCommandLinkFieldName(HtmlRendererUtils.getFormName(component, facesContext)));
        if (reqValue != null && reqValue.equals(clientId))
        {
            HtmlTreeNode node = (HtmlTreeNode)component;

            node.setSelected(true);
        }
    }
}

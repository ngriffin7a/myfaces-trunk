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
package org.apache.myfaces.renderkit.html;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * 
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.13  2004/10/13 11:51:00  matze
 * renamed packages to org.apache
 *
 * Revision 1.12  2004/07/01 22:05:06  mwessendorf
 * ASF switch
 *
 * Revision 1.11  2004/03/31 14:51:46  manolito
 * summaryFormat and detailFormat support
 *
 * Revision 1.10  2004/03/30 17:47:34  manolito
 * Message and Messages refactored
 *
 * Revision 1.9  2004/03/30 13:24:57  manolito
 * refactoring: HtmlComponentTag moved to share and renamed to HtmlComponentTagBase
 *
 */
public class HtmlMessagesRenderer
        extends HtmlMessagesRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlMessagesRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        super.encodeEnd(facesContext, component);   //check for NP
        renderMessages(facesContext, component);
    }

    protected String getSummary(FacesContext facesContext,
                                UIComponent message,
                                FacesMessage facesMessage,
                                String msgClientId)
    {
        return facesMessage.getSummary();
    }

    protected String getDetail(FacesContext facesContext,
                               UIComponent message,
                               FacesMessage facesMessage,
                               String msgClientId)
    {
        return facesMessage.getDetail();
    }
}

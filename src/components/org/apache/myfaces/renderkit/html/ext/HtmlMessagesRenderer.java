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

import org.apache.myfaces.component.html.ext.HtmlMessage;
import org.apache.myfaces.component.html.ext.HtmlMessages;
import org.apache.myfaces.renderkit.html.HtmlMessagesRendererBase;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.6  2005/01/22 19:47:44  mmarinschek
 * Message rendering updated - if a validation exception needs to be rendered, the id of the component is replaced with a label.
 *
 * Revision 1.5  2004/10/13 11:50:59  matze
 * renamed packages to org.apache
 *
 * Revision 1.4  2004/07/01 21:53:06  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/04/01 14:34:22  manolito
 * new globalSummaryFormat attribute
 *
 * Revision 1.2  2004/03/31 14:51:46  manolito
 * summaryFormat and detailFormat support
 *
 * Revision 1.1  2004/03/30 17:47:32  manolito
 * Message and Messages refactored
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
        String msgSummary = facesMessage.getSummary();
        if (msgSummary == null) return null;

        String inputLabel = null;
        if (msgClientId != null) inputLabel = HtmlMessageRenderer.findInputLabel(facesContext, msgClientId);
        if (inputLabel == null) inputLabel = "";

        if(((message instanceof HtmlMessages && ((HtmlMessages) message).isReplaceIdWithLabel()) ||
                (message instanceof HtmlMessage && ((HtmlMessage) message).isReplaceIdWithLabel()))&&
                inputLabel.length()!=0)
            msgSummary = msgSummary.replaceAll(HtmlMessageRenderer.findInputId(facesContext, msgClientId),inputLabel);


        String summaryFormat;
        if (msgClientId == null)
        {
            summaryFormat = getGlobalSummaryFormat(message);
            if (summaryFormat == null)
            {
                summaryFormat = getSummaryFormat(message);
            }
        }
        else
        {
            summaryFormat = getSummaryFormat(message);
        }
        if (summaryFormat == null) return msgSummary;

        MessageFormat format = new MessageFormat(summaryFormat, facesContext.getViewRoot().getLocale());
        return format.format(new Object[] {msgSummary, inputLabel});
    }


    private String getSummaryFormat(UIComponent message)
    {
        if (message instanceof HtmlMessages)
        {
            return ((HtmlMessages)message).getSummaryFormat();
        }
        else
        {
            return (String)message.getAttributes().get("summaryFormat");
        }
    }

    private String getGlobalSummaryFormat(UIComponent message)
    {
        if (message instanceof HtmlMessages)
        {
            return ((HtmlMessages)message).getGlobalSummaryFormat();
        }
        else
        {
            return (String)message.getAttributes().get("globalSummaryFormat");
        }
    }

    protected String getDetail(FacesContext facesContext,
                               UIComponent message,
                               FacesMessage facesMessage,
                               String msgClientId)
    {
        String msgDetail = facesMessage.getDetail();
        if (msgDetail == null) return null;

        String inputLabel = null;
        if (msgClientId != null) inputLabel = HtmlMessageRenderer.findInputLabel(facesContext, msgClientId);
        if (inputLabel == null) inputLabel = "";

        if(((message instanceof HtmlMessages && ((HtmlMessages) message).isReplaceIdWithLabel()) ||
                (message instanceof HtmlMessage && ((HtmlMessage) message).isReplaceIdWithLabel()))&&
                inputLabel.length()!=0)
            msgDetail = msgDetail.replaceAll(HtmlMessageRenderer.findInputId(facesContext, msgClientId),inputLabel);

        String detailFormat;
        if (message instanceof HtmlMessage)
        {
            detailFormat = ((HtmlMessage)message).getDetailFormat();
        }
        else
        {
            detailFormat = (String)message.getAttributes().get("detailFormat");
        }

        if (detailFormat == null) return msgDetail;

        MessageFormat format = new MessageFormat(detailFormat, facesContext.getViewRoot().getLocale());
        return format.format(new Object[] {msgDetail, inputLabel});
    }


}

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

import net.sourceforge.myfaces.component.html.ext.HtmlMessage;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HtmlMessageRendererBase;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/03/31 14:51:46  manolito
 * summaryFormat and detailFormat support
 *
 * Revision 1.2  2004/03/30 17:47:32  manolito
 * Message and Messages refactored
 *
 * Revision 1.1  2004/03/30 13:27:05  manolito
 * extended Message component
 *
 */
public class HtmlMessageRenderer
        extends HtmlMessageRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlMessageRenderer.class);

    private static final String OUTPUT_LABEL_MAP = HtmlMessageRenderer.class.getName() + ".OUTPUT_LABEL_MAP";

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        super.encodeEnd(facesContext, component);   //check for NP
        renderMessage(facesContext, component);
    }

    protected String getSummary(FacesContext facesContext,
                                UIComponent message,
                                FacesMessage facesMessage,
                                String msgClientId)
    {
        String msgSummary = facesMessage.getSummary();
        if (msgSummary == null) return null;

        String summaryFormat;
        if (message instanceof HtmlMessage)
        {
            summaryFormat = ((HtmlMessage)message).getSummaryFormat();
        }
        else
        {
            summaryFormat = (String)message.getAttributes().get("summaryFormat");
        }

        if (summaryFormat == null) return msgSummary;

        String inputLabel = null;
        if (msgClientId != null) inputLabel = findInputLabel(facesContext, msgClientId);
        if (inputLabel == null) inputLabel = "";

        MessageFormat format = new MessageFormat(summaryFormat, facesContext.getViewRoot().getLocale());
        return format.format(new Object[] {msgSummary, inputLabel});
    }

    protected String getDetail(FacesContext facesContext,
                               UIComponent message,
                               FacesMessage facesMessage,
                               String msgClientId)
    {
        String msgDetail = facesMessage.getDetail();
        if (msgDetail == null) return null;

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

        String inputLabel = null;
        if (msgClientId != null) inputLabel = findInputLabel(facesContext, msgClientId);
        if (inputLabel == null) inputLabel = "";

        MessageFormat format = new MessageFormat(detailFormat, facesContext.getViewRoot().getLocale());
        return format.format(new Object[] {msgDetail, inputLabel});
    }


    public static String findInputLabel(FacesContext facesContext, String inputClientId)
    {
        Map outputLabelMap = getOutputLabelMap(facesContext);
        return (String)outputLabelMap.get(inputClientId);
    }

    /**
     * @param facesContext
     * @return a Map that reversely maps clientIds of components to their
     *         corresponding OutputLabel component
     */
    private static Map getOutputLabelMap(FacesContext facesContext)
    {
        Map map = (Map)facesContext.getExternalContext().getRequestMap().get(OUTPUT_LABEL_MAP);
        if (map == null)
        {
            map = new HashMap();
            createOutputLabelMap(facesContext, facesContext.getViewRoot(), map);
            facesContext.getExternalContext().getRequestMap().put(OUTPUT_LABEL_MAP, map);
        }
        return map;
    }

    private static void createOutputLabelMap(FacesContext facesContext,
                                             UIComponent root,
                                             Map map)
    {
        for (Iterator it = root.getFacetsAndChildren(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof HtmlOutputLabel)
            {
                String forAttr = ((HtmlOutputLabel)child).getFor();
                UIComponent input = child.findComponent(forAttr);
                map.put(input.getClientId(facesContext),
                        getLabelText(facesContext, (HtmlOutputLabel)child));
            }
            else
            {
                createOutputLabelMap(facesContext, child, map);
            }
        }
    }

    private static String getLabelText(FacesContext facesContext, HtmlOutputLabel label)
    {
        String text = RendererUtils.getStringValue(facesContext, label);
        if (text == null)
        {
            //TODO: traverse children and append OutputText and/or OutputMessage texts
        }
        return text;
    }
}

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

import net.sourceforge.myfaces.component.html.ext.HtmlMessages;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HtmlMessagesRendererBase;

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
 * Revision 1.1  2004/03/30 17:47:32  manolito
 * Message and Messages refactored
 *
 */
public class HtmlMessagesRenderer
        extends HtmlMessagesRendererBase 
{
    //private static final Log log = LogFactory.getLog(HtmlMessagesRenderer.class);
    
    private static final String OUTPUT_LABEL_MAP = HtmlMessagesRenderer.class.getName() + ".OUTPUT_LABEL_MAP";

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        super.encodeEnd(facesContext, component);   //check for NP
        renderMessage(facesContext, component);
    }

    protected String getSummaryDetailSeparator(FacesContext facesContext,
                                               UIComponent messages,
                                               String msgClientId)
    {
        String separator;
        if (messages instanceof HtmlMessages)
        {
            separator = ((HtmlMessages)messages).getSummaryDetailSeparator();
        }
        else
        {
            separator = (String)messages.getAttributes().get("summaryDetailSeparator");
        }

        String labelFormat;
        if (messages instanceof HtmlMessages)
        {
            labelFormat = ((HtmlMessages)messages).getLabelFormat();
        }
        else
        {
            labelFormat = (String)messages.getAttributes().get("labelFormat");
        }
        
        if (msgClientId != null && labelFormat != null)
        {
            String inputLabel = findInputLabel(facesContext, msgClientId);
            if (inputLabel != null)
            {
                MessageFormat format = new MessageFormat(labelFormat,
                                                         facesContext.getViewRoot().getLocale());
                return format.format(new Object[] {inputLabel}) + separator;
            }
        }

        return separator;
    }


    private String findInputLabel(FacesContext facesContext, String inputClientId)
    {
        Map outputLabelMap = getOutputLabelMap(facesContext);
        HtmlOutputLabel outputLabel = (HtmlOutputLabel)outputLabelMap.get(inputClientId);
        String text = RendererUtils.getStringValue(facesContext, outputLabel);
        if (text == null)
        {
            //TODO: traverse children and append OutputText and/or OutputMessage texts
        }
        return text;
    }

    
    /**
     * @param facesContext
     * @return a Map that reversely maps clientIds of components to their
     *         corresponding OutputLabel component
     */
    private Map getOutputLabelMap(FacesContext facesContext)
    {
        Map map = (Map)facesContext.getExternalContext().getRequestMap().get(OUTPUT_LABEL_MAP);
        if (map == null)
        {
            map = new HashMap();
            createOutputLabelMap(facesContext.getViewRoot(), map);
            facesContext.getExternalContext().getRequestMap().put(OUTPUT_LABEL_MAP, map);
        }
        return map;
    }

    private void createOutputLabelMap(UIComponent root, Map map)
    {
        if (root.getChildCount() > 0)
        {
            for (Iterator it = root.getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                if (child instanceof HtmlOutputLabel)
                {
                    map.put(((HtmlOutputLabel)child).getFor(), child);
                }
                else
                {
                    createOutputLabelMap(child, map);
                }
            }
        }
    }
    
}

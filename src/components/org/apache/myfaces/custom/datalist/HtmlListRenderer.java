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
package net.sourceforge.myfaces.custom.datalist;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.7  2004/09/02 17:23:25  tinytoony
 * fix for the span-element for other than the output-text
 *
 * Revision 1.6  2004/08/20 07:12:00  manolito
 * rowIndexVar and rowCountVar now handled correctly by component itself instead of renderer
 *
 * Revision 1.5  2004/08/09 11:47:09  manolito
 * CSS style support also for non OL or UL layout
 *
 * Revision 1.4  2004/08/09 09:10:39  manolito
 * RFE #990814 - dataList component ignores styleClass attribute
 *
 */
public class HtmlListRenderer
    extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlListRenderer.class);

    public static final String LAYOUT_SIMPLE = "simple";
    public static final String LAYOUT_UL = "unorderedList";
    public static final String LAYOUT_OL = "orderedList";

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);
        ResponseWriter writer = facesContext.getResponseWriter();
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        String layout = getLayout(uiComponent);
        if (layout != null)
        {
            if (layout.equals(LAYOUT_UL))
            {
                writer.startElement(HTML.UL_ELEM, uiComponent);
                HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                                                       HTML.COMMON_PASSTROUGH_ATTRIBUTES);
            }
            else if (layout.equals(LAYOUT_OL))
            {
                writer.startElement(HTML.OL_ELEM, uiComponent);
                HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent,
                                                       HTML.COMMON_PASSTROUGH_ATTRIBUTES);
            }
            else
            {
                if(uiComponent.getId()!=null && !uiComponent.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
                {
                    writer.startElement(HTML.SPAN_ELEM, uiComponent);

                    writer.writeAttribute(HTML.ID_ATTR, uiComponent.getClientId(facesContext),null);

                    HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.COMMON_PASSTROUGH_ATTRIBUTES);

                }
                else
                {
                    HtmlRendererUtils.renderHTMLAttributesWithOptionalStartElement(writer,uiComponent,
                            HTML.SPAN_ELEM,HTML.COMMON_PASSTROUGH_ATTRIBUTES);
                }
            }
        }
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, UIData.class);

        UIData uiData = (UIData)component;
        String layout = getLayout(component);
        //Map requestMap = facesContext.getExternalContext().getRequestMap();

        ResponseWriter writer = facesContext.getResponseWriter();

        int first = uiData.getFirst();
        int rows = uiData.getRows();
        int rowCount = uiData.getRowCount();
        if (rows <= 0)
        {
            rows = rowCount - first;
        }
        int last = first + rows;
        if (last > rowCount) last = rowCount;

        /*
        String rowIndexVar = getRowIndexVar(component);
        String rowCountVar = getRowCountVar(component);

        if (rowCountVar != null)
        {
            requestMap.put(rowCountVar, new Integer(rowCount));
        }
        */

        for (int i = first; i < last; i++)
        {
            uiData.setRowIndex(i);
            if (uiData.isRowAvailable())
            {
                /*
                if (rowIndexVar != null)
                {
                    requestMap.put(rowIndexVar, new Integer(i));
                }
                */

                HtmlRendererUtils.writePrettyLineSeparator(facesContext);
                if (layout != null && (layout.equals(LAYOUT_UL) || (layout.equals(LAYOUT_OL))))
                {
                    writer.startElement(HTML.LI_ELEM, component);
                }

                RendererUtils.renderChildren(facesContext, component);

                if (layout != null && (layout.equals(LAYOUT_UL) || (layout.equals(LAYOUT_OL))))
                {
                    writer.endElement(HTML.LI_ELEM);
                }

                /*
                if (rowIndexVar != null)
                {
                    requestMap.remove(rowIndexVar);
                }
                */
            }
        }

        /*
        if (rowCountVar != null)
        {
            requestMap.remove(rowCountVar);
        }
        */
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);
        ResponseWriter writer = facesContext.getResponseWriter();
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        String layout = getLayout(uiComponent);
        if (layout != null)
        {
            if (layout.equals(LAYOUT_UL))
            {
                writer.endElement(HTML.UL_ELEM);
            }
            else if (layout.equals(LAYOUT_OL))
            {
                writer.endElement(HTML.OL_ELEM);
            }
            else
            {
                if(uiComponent.getId()!=null && !uiComponent.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
                {    
                    writer.endElement(HTML.SPAN_ELEM);
                }
                else
                {
                    HtmlRendererUtils.renderOptionalEndElement(writer,
                                                           uiComponent,
                                                           HTML.SPAN_ELEM,
                                                           HTML.COMMON_PASSTROUGH_ATTRIBUTES);
                }
            }
        }
    }


    private String getLayout(UIComponent component)
    {
        if (component instanceof HtmlDataList)
        {
            return ((HtmlDataList)component).getLayout();
        }
        else
        {
            return (String)component.getAttributes().get(JSFAttr.LAYOUT_ATTR);
        }
    }

    /*
    private String getRowIndexVar(UIComponent component)
    {
        if (component instanceof HtmlDataList)
        {
            return ((HtmlDataList)component).getRowIndexVar();
        }
        else
        {
            return (String)component.getAttributes().get("rowIndexVar");
        }
    }

    private String getRowCountVar(UIComponent component)
    {
        if (component instanceof HtmlDataList)
        {
            return ((HtmlDataList)component).getRowCountVar();
        }
        else
        {
            return (String)component.getAttributes().get("rowCountVar");
        }
    }
    */

}

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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTableRenderer
    extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlTableRenderer.class);

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataTable.class);

        HtmlDataTable dataTable = (HtmlDataTable)uiComponent;

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.write("\n");
        writer.startElement(HTML.TABLE_ELEM, dataTable);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);

        writeFacet(facesContext, dataTable, "header");
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataTable.class);

        ResponseWriter writer = facesContext.getResponseWriter();
        HtmlDataTable uiData = (HtmlDataTable)uiComponent;

        writer.startElement("tbody", uiComponent);

        int first = uiData.getFirst();
        int rowCount = uiData.getRowCount();
        int maxRows = uiData.getRows();
        if (maxRows <= 0)
        {
            maxRows = rowCount - first;
        }

        Styles styles = new Styles(uiData.getRowClasses(), uiData.getColumnClasses());

        for (int i = first; i < rowCount && i < maxRows; i++)
        {
            uiData.setRowIndex(i);
            if (!uiData.isRowAvailable())
            {
                log.error("Row is not available. Rowindex = " + i);
                return;
            }

            writer.startElement("tr", uiComponent);
            if (styles.hasRowStyle())
            {
                String rowStyle = styles.getRowStyle(i);
                writer.writeAttribute("class", rowStyle, null);
            }

            List children = uiComponent.getChildren();
            for (int j = 0, size = uiComponent.getChildCount(); j < size; j++)
            {
                UIColumn uiColumn = (UIColumn)children.get(j);
                if (uiColumn.isRendered())
                {
                    writer.startElement("td", uiComponent);
                    if (styles.hasColumnStyle())
                    {
                        String columnStyle = styles.getColumnStyle(j);
                        writer.writeAttribute("class", columnStyle, null);
                    }
                    RendererUtils.renderChild(facesContext, uiColumn);
                    writer.endElement("td");
                }
            }
            writer.endElement("tr");
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        }
        writer.endElement("tbody");
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataTable.class);

        ResponseWriter writer = facesContext.getResponseWriter();
        writeFacet(facesContext, (HtmlDataTable)uiComponent, "footer");
        writer.endElement(HTML.TABLE_ELEM);
    }

    private void writeFacet(FacesContext facesContext, HtmlDataTable uiData, String facetName) throws IOException
    {
        boolean foundFacet = false;
        for (Iterator it = uiData.getChildren().iterator(); it.hasNext(); )
        {
            UIColumn uiColumn = (UIColumn)it.next();
            if (uiColumn.isRendered())
            {
                UIComponent facetComp = uiColumn.getFacet(facetName);
                if (facetComp != null)
                {
                    foundFacet =  true;
                    break;
                }
            }
        }
        if (foundFacet)
        {
            ResponseWriter writer = facesContext.getResponseWriter();

            boolean isHeader = facetName.equals("header");
            String elemName = isHeader ? "thead" : "tfoot";
            writer.startElement(elemName, uiData);
            writer.startElement("tr", uiData);
            String style = isHeader ? uiData.getHeaderClass() : uiData.getFooterClass();
            if (style != null && style.length() > 0)
            {
                writer.writeAttribute("class", style, null);
            }
            for (Iterator it = uiData.getChildren().iterator(); it.hasNext(); )
            {
                UIColumn uiColumn = (UIColumn)it.next();
                if (uiColumn.isRendered())
                {
                    UIComponent facetComp = (UIComponent)uiColumn.getFacet(facetName);

                    writer.startElement("td", uiData);
                    if (facetComp != null)
                    {
                        RendererUtils.renderChild(facesContext, facetComp);
                    }
                    writer.endElement("td");
                }
            }
            writer.endElement("tr");
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.endElement(elemName);
        }
    }


    //-------------------------------------------------------------
    // Helper class Styles
    //-------------------------------------------------------------
    private static class Styles
    {
        //~ Instance fields ------------------------------------------------------------------------

        private String[] _columnStyle;
        private String[] _rowStyle;

        //~ Constructors ---------------------------------------------------------------------------

        Styles(String rowStyles, String columnStyles)
        {
            if (rowStyles != null)
            {
                StringTokenizer tokenizer = new StringTokenizer(rowStyles, ",");
                _rowStyle = new String[tokenizer.countTokens()];
                for (int i = 0; tokenizer.hasMoreTokens(); i++)
                {
                    _rowStyle[i] = tokenizer.nextToken().trim();
                }
            }
            else
            {
                _rowStyle = new String[0];
            }
            if (columnStyles != null)
            {
                StringTokenizer tokenizer = new StringTokenizer(columnStyles, ",");
                _columnStyle = new String[tokenizer.countTokens()];
                for (int i = 0; tokenizer.hasMoreTokens(); i++)
                {
                    _columnStyle[i] = tokenizer.nextToken().trim();
                }
            }
            else
            {
                _columnStyle = new String[0];
            }
        }

        public String getRowStyle(int idx)
        {
            if (!hasRowStyle())
            {
                return null;
            }
            return _rowStyle[idx % _rowStyle.length];
        }

        public String getColumnStyle(int idx)
        {
            if (!hasColumnStyle())
            {
                return null;
            }
            return _columnStyle[idx % _columnStyle.length];
        }

        public boolean hasRowStyle()
        {
            return _rowStyle.length > 0;
        }

        public boolean hasColumnStyle()
        {
            return _columnStyle.length > 0;
        }

    }


}

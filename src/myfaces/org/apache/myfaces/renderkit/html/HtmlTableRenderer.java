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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
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
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.write("\n");
        writer.startElement(HTML.TABLE_ELEM, uiComponent);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);

        writeFacet(facesContext, uiComponent, "header");
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, UIData.class);

        UIData uiData = (UIData)component;

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement("tbody", component);

        String rowClasses;
        String columnClasses;
        if (component instanceof HtmlDataTable)
        {
            rowClasses = ((HtmlDataTable)component).getRowClasses();
            columnClasses = ((HtmlDataTable)component).getColumnClasses();
        }
        else
        {
            rowClasses = (String)component.getAttributes().get(JSFAttr.ROW_CLASSES_ATTR);
            columnClasses = (String)component.getAttributes().get(JSFAttr.COLUMN_CLASSES_ATTR);
        }
        Styles styles = new Styles(rowClasses, columnClasses);

        int first = uiData.getFirst();
        int rows = uiData.getRows();
        int rowCount = uiData.getRowCount();
        if (rows <= 0)
        {
            rows = rowCount - first;
        }
        int last = first + rows;
        if (last > rowCount) last = rowCount;

        for (int i = first; i < last; i++)
        {
            uiData.setRowIndex(i);
            if (!uiData.isRowAvailable())
            {
                log.error("Row is not available. Rowindex = " + i);
                return;
            }

            writer.startElement("tr", component);
            if (styles.hasRowStyle())
            {
                String rowStyle = styles.getRowStyle(i);
                writer.writeAttribute("class", rowStyle, null);
            }

            List children = component.getChildren();
            for (int j = 0, size = component.getChildCount(); j < size; j++)
            {
                UIColumn uiColumn = (UIColumn)children.get(j);
                if (uiColumn.isRendered())
                {
                    writer.startElement("td", component);
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
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);

        ResponseWriter writer = facesContext.getResponseWriter();
        writeFacet(facesContext, uiComponent, "footer");
        writer.endElement(HTML.TABLE_ELEM);
    }

    private void writeFacet(FacesContext facesContext, UIComponent component, String facetName) throws IOException
    {
        boolean foundFacet = false;
        for (Iterator it = component.getChildren().iterator(); it.hasNext(); )
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
            writer.startElement(elemName, component);
            writer.startElement("tr", component);
            String style = isHeader ? getHeaderClass(component) : getFooterClass(component);
            if (style != null && style.length() > 0)
            {
                writer.writeAttribute("class", style, null);
            }
            for (Iterator it = component.getChildren().iterator(); it.hasNext(); )
            {
                UIColumn uiColumn = (UIColumn)it.next();
                if (uiColumn.isRendered())
                {
                    UIComponent facetComp = (UIComponent)uiColumn.getFacet(facetName);

                    writer.startElement("td", component);
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


    private static String getHeaderClass(UIComponent component)
    {
        if (component instanceof HtmlDataTable)
        {
            return ((HtmlDataTable)component).getHeaderClass();
        }
        else
        {
            return (String)component.getAttributes().get(JSFAttr.HEADER_CLASS_ATTR);
        }
    }

    private static String getFooterClass(UIComponent component)
    {
        if (component instanceof HtmlDataTable)
        {
            return ((HtmlDataTable)component).getFooterClass();
        }
        else
        {
            return (String)component.getAttributes().get(JSFAttr.FOOTER_CLASS_ATTR);
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

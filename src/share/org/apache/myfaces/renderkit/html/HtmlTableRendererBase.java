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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.util.ArrayUtils;
import net.sourceforge.myfaces.util.StringUtils;

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

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/08/20 07:14:43  manolito
 * HtmlDataTable now also supports rowIndexVar and rowCountVar
 *
 *
 *
 * old cvs log (HtmlTableRenderer in myfaces impl):
 *
 * Revision 1.20  2004/08/09 08:01:08  manolito
 * bug #1004896 - id attribute not rendered
 *
 * Revision 1.19  2004/07/01 22:05:06  mwessendorf
 * ASF switch
 *
 * Revision 1.18  2004/06/22 15:26:04  prophecyslides
 * headerClass does not apply to the <tr> in spec, so removed it.
 *
 * Revision 1.17  2004/06/21 23:12:46  prophecyslides
 * headerClass style propogates to the <th> elements, not just the <tr>
 *
 * Revision 1.16  2004/06/21 10:57:59  manolito
 * missing CVS Log keyword
 */
public class HtmlTableRendererBase
    extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlTableRendererBase.class);

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);

        ResponseWriter writer = facesContext.getResponseWriter();

        beforeTable(facesContext, (UIData)uiComponent);

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TABLE_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, uiComponent.getClientId(facesContext), null);
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);

        renderFacet(facesContext, writer, (UIData)uiComponent, true);
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, UIData.class);

        UIData uiData = (UIData)component;

        ResponseWriter writer = facesContext.getResponseWriter();

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TBODY_ELEM, component);

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

            beforeRow(facesContext, uiData);

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.startElement(HTML.TR_ELEM, component);
            if (styles.hasRowStyle())
            {
                String rowStyle = styles.getRowStyle(i);
                writer.writeAttribute(HTML.CLASS_ATTR, rowStyle, null);
            }

            List children = component.getChildren();
            for (int j = 0, size = component.getChildCount(); j < size; j++)
            {
                UIComponent child = (UIComponent)children.get(j);
                if (child instanceof UIColumn && ((UIColumn)child).isRendered())
                {
                    writer.startElement(HTML.TD_ELEM, component);
                    if (styles.hasColumnStyle())
                    {
                        String columnStyle = styles.getColumnStyle(j);
                        writer.writeAttribute(HTML.CLASS_ATTR, columnStyle, null);
                    }
                    RendererUtils.renderChild(facesContext, child);
                    writer.endElement(HTML.TD_ELEM);
                }
            }
            writer.endElement(HTML.TR_ELEM);

            afterRow(facesContext, uiData);
        }
        writer.endElement(HTML.TBODY_ELEM);
    }


    /**
     * Convenient method for derived table renderers.
     */
    protected void beforeTable(FacesContext facesContext, UIData uiData) throws IOException
    {
    }

    /**
     * Convenient method for derived table renderers.
     */
    protected void beforeRow(FacesContext facesContext, UIData uiData) throws IOException
    {
    }

    /**
     * Convenient method for derived table renderers.
     */
    protected void afterRow(FacesContext facesContext, UIData uiData) throws IOException
    {
    }

    /**
     * Convenient method for derived table renderers.
     */
    protected void afterTable(FacesContext facesContext, UIData uiData) throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);

        ResponseWriter writer = facesContext.getResponseWriter();
        renderFacet(facesContext, writer, (UIData)uiComponent, false);
        writer.endElement(HTML.TABLE_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        afterTable(facesContext, (UIData)uiComponent);
    }

    private void renderFacet(FacesContext facesContext,
                             ResponseWriter writer,
                             UIData uiData,
                             boolean header) throws IOException
    {
        int colspan = 0;
        boolean hasColumnFacet = false;
        for (Iterator it = uiData.getChildren().iterator(); it.hasNext(); )
        {
            UIComponent uiComponent = (UIComponent)it.next();
            if (uiComponent instanceof UIColumn &&
                ((UIColumn)uiComponent).isRendered())
            {
                colspan++;
                if (!hasColumnFacet)
                {
                    hasColumnFacet = header ? ((UIColumn)uiComponent).getHeader() != null :
                        ((UIColumn)uiComponent).getFooter() != null;
                }
            }
        }

        UIComponent facet = header ? uiData.getHeader() : uiData.getFooter();
        if (facet != null || hasColumnFacet)
        {
            // Header or Footer present
            String elemName = header ? HTML.THEAD_ELEM : HTML.TFOOT_ELEM;
            String columnElemName = header ? HTML.TH_ELEM : HTML.TD_ELEM;
            String style = header ? getHeaderClass(uiData) : getFooterClass(uiData);

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.startElement(elemName, uiData);
            if (header)
            {
                renderTableFacet(facesContext, writer, uiData, facet, style, columnElemName, colspan);
                renderColumnFacet(facesContext, writer, uiData, style, columnElemName, hasColumnFacet, header);
            }
            else
            {
                renderColumnFacet(facesContext, writer, uiData, style, columnElemName, hasColumnFacet, header);
                renderTableFacet(facesContext, writer, uiData, facet, style, columnElemName, colspan);
            }
            writer.endElement(elemName);
        }
    }

    private void renderColumnFacet(FacesContext facesContext,
                                   ResponseWriter writer,
                                   UIData uiData,
                                   String style,
                                   String colElementName,
                                   boolean hasColumnFacet,
                                   boolean header)
        throws IOException
    {
        if (!hasColumnFacet)
        {
            return;
        }
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TR_ELEM, uiData);
        for (Iterator it = uiData.getChildren().iterator(); it.hasNext(); )
        {
            UIComponent uiComponent = (UIComponent)it.next();
            if (uiComponent instanceof UIColumn &&
                ((UIColumn)uiComponent).isRendered())
            {
                UIComponent facet = header ?
                    ((UIColumn)uiComponent).getHeader() : ((UIColumn)uiComponent).getFooter();
                writer.startElement(colElementName, uiComponent);
                if(style != null)
                {
                     writer.writeAttribute(HTML.CLASS_ATTR, style, null);
                }
                if (facet != null)
                {
                    RendererUtils.renderChild(facesContext, facet);
                }
                writer.endElement(colElementName);
            }
        }
        writer.endElement(HTML.TR_ELEM);
    }

    private void renderTableFacet(FacesContext facesContext,
                                  ResponseWriter writer,
                                  UIData uiData,
                                  UIComponent facet,
                                  String style,
                                  String colElementName,
                                  int colspan)
        throws IOException
    {
        if (facet == null)
        {
            return;
        }
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TR_ELEM, uiData);
        if (style != null)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, style, null);
        }
        writer.startElement(colElementName, uiData);
        if (colElementName.equals(HTML.TH_ELEM))
        {
            writer.writeAttribute(HTML.SCOPE_ATTR, HTML.SCOPE_COLGROUP_VALUE, null);
        }
        writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(colspan), null);
        if (facet != null)
        {
            RendererUtils.renderChild(facesContext, facet);
        }
        writer.endElement(colElementName);
        writer.endElement(HTML.TR_ELEM);
    }

    private static String getHeaderClass(UIData component)
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

    private static String getFooterClass(UIData component)
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
            _rowStyle = (rowStyles == null)
                ? ArrayUtils.EMPTY_STRING_ARRAY
                : StringUtils.trim(
                    StringUtils.splitShortString(rowStyles, ','));
            _columnStyle = (columnStyles == null)
                ? ArrayUtils.EMPTY_STRING_ARRAY
                : StringUtils.trim(
                    StringUtils.splitShortString(columnStyles, ','));
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

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

import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.util.ArrayUtils;
import org.apache.myfaces.util.StringUtils;

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
 *
 *
 *          $Log$
 *          Revision 1.7  2005/01/19 11:49:20  matzew
 *          MYFACES-83. Refactored HtmlTableRendererBase supported by "power-user" Heath Borders-Wing
 *
 *          Revision 1.6  2004/12/23 13:03:09  mmarinschek
 *          id's not rendered (or not conditionally rendered); changes in jslistener to support both ie and firefox now
 *
 *          Revision 1.5  2004/11/26 12:14:10  oros
 *          MYFACES-8: applied tree table patch by David Le Strat
 *
 * 
 */
public class HtmlTableRendererBase extends HtmlRenderer
{
    /** Header facet name. */
    protected static final String HEADER_FACET_NAME = "header";

    /** Footer facet name. */
    protected static final String FOOTER_FACET_NAME = "footer";

    /** The logger. */
    private static final Log log = LogFactory.getLog(HtmlTableRendererBase.class);

	/**
	 * @see javax.faces.render.Renderer#getRendersChildren()
	 */
    public boolean getRendersChildren()
    {
        return true;
    }

	/**
	 * @see javax.faces.render.Renderer#encodeBegin(FacesContext, UIComponent)
	 */
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);

        ResponseWriter writer = facesContext.getResponseWriter();

        beforeTable(facesContext, (UIData) uiComponent);

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TABLE_ELEM, uiComponent);
        HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);

        renderFacet(facesContext, writer, (UIData) uiComponent, true);
    }

	/**
	 * @see javax.faces.render.Renderer#encodeChildren(FacesContext, UIComponent)
	 */
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, UIData.class);

        UIData uiData = (UIData) component;

        ResponseWriter writer = facesContext.getResponseWriter();

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TBODY_ELEM, component);

        String rowClasses;
        String columnClasses;
        if (component instanceof HtmlDataTable)
        {
            rowClasses = ((HtmlDataTable) component).getRowClasses();
            columnClasses = ((HtmlDataTable) component).getColumnClasses();
        }
        else
        {
            rowClasses = (String) component.getAttributes().get(JSFAttr.ROW_CLASSES_ATTR);
            columnClasses = (String) component.getAttributes().get(JSFAttr.COLUMN_CLASSES_ATTR);
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
        if (last > rowCount)
            last = rowCount;

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
            renderRowStart(facesContext, writer, uiData, styles.getRowStyle(i));

            List children = component.getChildren();
            for (int j = 0, size = component.getChildCount(); j < size; j++)
            {
                UIComponent child = (UIComponent) children.get(j);
                if (child instanceof UIColumn && ((UIColumn) child).isRendered())
                {
                	String columnStyle = styles.getColumnStyle(j);
                    renderColumnBody(facesContext, writer, uiData, (UIColumn) child, columnStyle);
                }
            }
            renderRowEnd(facesContext, writer, uiData);

            afterRow(facesContext, uiData);
        }
        writer.endElement(HTML.TBODY_ELEM);
    }
    
    /**
     * Renders the body of a given <code>UIColumn</code> (everything but the header and footer facets).
     * @param facesContext the <code>FacesContext</code>.
     * @param writer the <code>ResponseWriter</code>.
     * @param uiData the <code>UIData</code> being rendered.
     * @param column the <code>UIColumn</code> to render.
     * @param columnStyleClass the styleClass of the <code>UIColumn</code> or <code>null</code> if
     * there is none.
     * @throws IOException if an exception occurs.
     */
	protected void renderColumnBody(
		FacesContext facesContext,
		ResponseWriter writer,
		UIData uiData,
		UIColumn column,
		String columnStyleClass) throws IOException
	{
		writer.startElement(HTML.TD_ELEM, uiData);
		if (columnStyleClass != null)
		{
			writer.writeAttribute(HTML.CLASS_ATTR, columnStyleClass, null);
		}
		
        RendererUtils.renderChild(facesContext, column);
        writer.endElement(HTML.TD_ELEM);
	}
    
    /**
     * Renders the start of a new row of body content.
     * @param facesContext the <code>FacesContext</code>.
     * @param writer the <code>ResponseWriter</code>.
     * @param uiData the <code>UIData</code> being rendered.
     * @param rowStyleClass te styleClass of the row or <code>null</code> if there is none.
     * @throws IOException if an exceptoin occurs.
     */
	protected void renderRowStart(
		FacesContext facesContext,
		ResponseWriter writer,
		UIData uiData,
		String rowStyleClass) throws IOException
	{
		writer.startElement(HTML.TR_ELEM, uiData);
        if (rowStyleClass != null)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, rowStyleClass, null);
        }
	}
	
	/**
     * Renders the end of a row of body content.
     * @param facesContext the <code>FacesContext</code>.
     * @param writer the <code>ResponseWriter</code>.
     * @param uiData the <code>UIData</code> being rendered.
     * @throws IOException if an exceptoin occurs.
     */
	protected void renderRowEnd(
		FacesContext facesContext,
		ResponseWriter writer,
		UIData uiData) throws IOException
	{
		writer.endElement(HTML.TR_ELEM);
	}

    /**
     * Convenient method for derived table renderers.
     * @param facesContext the <code>FacesContext</code>.
     * @param uiData the <code>UIData</code> being rendered.
     */
    protected void beforeTable(FacesContext facesContext, UIData uiData) throws IOException
    {
    }

    /**
     * Convenient method for derived table renderers.
     * @param facesContext the <code>FacesContext</code>.
     * @param uiData the <code>UIData</code> being rendered.
     */
    protected void beforeRow(FacesContext facesContext, UIData uiData) throws IOException
    {
    }

    /**
     * Convenient method for derived table renderers.
     * @param facesContext the <code>FacesContext</code>.
     * @param uiData the <code>UIData</code> being rendered.
     */
    protected void afterRow(FacesContext facesContext, UIData uiData) throws IOException
    {
    }

    /**
     * Convenient method for derived table renderers.
     * @param facesContext the <code>FacesContext</code>.
     * @param uiData the <code>UIData</code> being rendered.
     */
    protected void afterTable(FacesContext facesContext, UIData uiData) throws IOException
    {
    }

	/**
	 * @see javax.faces.render.Renderer#encodeEnd(FacesContext, UIComponent)
	 */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);

        ResponseWriter writer = facesContext.getResponseWriter();
        renderFacet(facesContext, writer, (UIData) uiComponent, false);
        writer.endElement(HTML.TABLE_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        afterTable(facesContext, (UIData) uiComponent);
    }

	/**
	 * Renders either the header or the footer facets.
	 * @param facesContext the <code>FacesContext</code>.
	 * @param writer the <code>ResponseWriter</code>.
	 * @param component the parent <code>UIComponent</code> containing the facets.
	 * @param header whether this is the header facet (if not, then the footer facet).
	 * @throws IOException if an exception occurs.
	 */
    protected void renderFacet(FacesContext facesContext, ResponseWriter writer, UIComponent component, boolean header)
            throws IOException
    {
        int colspan = 0;
        boolean hasColumnFacet = false;
        for (Iterator it = component.getChildren().iterator(); it.hasNext();)
        {
            UIComponent uiComponent = (UIComponent) it.next();
            if (uiComponent instanceof UIColumn && ((UIColumn) uiComponent).isRendered())
            {
                colspan++;
                if (!hasColumnFacet)
                {
                    hasColumnFacet = header ? ((UIColumn) uiComponent).getHeader() != null : ((UIColumn) uiComponent)
                            .getFooter() != null;
                }
            }
        }

        UIComponent facet = header ? (UIComponent) component.getFacets().get(HEADER_FACET_NAME)
                : (UIComponent) component.getFacets().get(FOOTER_FACET_NAME);
        if (facet != null || hasColumnFacet)
        {
            // Header or Footer present
            String elemName = header ? HTML.THEAD_ELEM : HTML.TFOOT_ELEM;

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.startElement(elemName, component);
            if (header)
            {
                String headerStyleClass = getHeaderClass(component);
                if (facet != null)
                    renderTableHeaderRow(facesContext, writer, component, facet, headerStyleClass, colspan);
                if (hasColumnFacet)
                    renderColumnHeaderRow(facesContext, writer, component, headerStyleClass);
            }
            else
            {
                String footerStyleClass = getFooterClass(component);
                if (hasColumnFacet)
                    renderColumnFooterRow(facesContext, writer, component, footerStyleClass);
                if (facet != null)
                    renderTableFooterRow(facesContext, writer, component, facet, footerStyleClass, colspan);
            }
            writer.endElement(elemName);
        }
    }

	/**
	 * Renders the header row of the table being rendered.
	 * @param facesContext the <code>FacesContext</code>.
	 * @param writer the <code>ResponseWriter</code>.
	 * @param component the <code>UIComponent</code> for whom a table is being rendered.
	 * @param headerFacet the facet for the header.
	 * @param headerStyleClass the styleClass of the header.
	 * @param colspan the number of columns the header should span.  Typically, this is
	 * the number of columns in the table.
	 * @throws IOException if an exception occurs.
	 */
    protected void renderTableHeaderRow(FacesContext facesContext, ResponseWriter writer, UIComponent component,
            UIComponent headerFacet, String headerStyleClass, int colspan) throws IOException
    {
        renderTableHeaderOrFooterRow(facesContext, writer, component, headerFacet, headerStyleClass, HTML.TH_ELEM,
                colspan);
    }

	/**
	 * Renders the footer row of the table being rendered.
	 * @param facesContext the <code>FacesContext</code>.
	 * @param writer the <code>ResponseWriter</code>.
	 * @param component the <code>UIComponent</code> for whom a table is being rendered.
	 * @param headerFacet the facet for the header.
	 * @param headerStyleClass the styleClass of the header.
	 * @param colspan the number of columns the header should span.  Typically, this is
	 * the number of columns in the table.
	 * @throws IOException if an exception occurs.
	 */
    protected void renderTableFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent component,
            UIComponent footerFacet, String footerStyleClass, int colspan) throws IOException
    {
        renderTableHeaderOrFooterRow(facesContext, writer, component, footerFacet, footerStyleClass, HTML.TD_ELEM,
                colspan);
    }

	/**
	 * Renders the header row for the columns, which is a separate row from the header row for the
	 * <code>UIData</code> header facet.
	 * @param facesContext the <code>FacesContext</code>.
	 * @param writer the <code>ResponseWriter</code>.
	 * @param component the <code>UIComponent</code> for whom a table is being rendered.
	 * @param headerStyleClass the styleClass of the header 
	 * @throws IOException if an exception occurs.
	 */
    protected void renderColumnHeaderRow(FacesContext facesContext, ResponseWriter writer, UIComponent component,
            String headerStyleClass) throws IOException
    {
        renderColumnHeaderOrFooterRow(facesContext, writer, component, headerStyleClass, true);
    }

	/**
	 * Renders the footer row for the columns, which is a separate row from the footer row for the
	 * <code>UIData</code> footer facet.
	 * @param facesContext the <code>FacesContext</code>.
	 * @param writer the <code>ResponseWriter</code>.
	 * @param component the <code>UIComponent</code> for whom a table is being rendered.
	 * @param footerStyleClass the styleClass of the footerStyleClass 
	 * @throws IOException if an exception occurs.
	 */
    protected void renderColumnFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent component,
            String footerStyleClass) throws IOException
    {
        renderColumnHeaderOrFooterRow(facesContext, writer, component, footerStyleClass, false);
    }

    private void renderTableHeaderOrFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent component,
            UIComponent facet, String styleClass, String colElementName, int colspan) throws IOException
    {
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TR_ELEM, component);
        writer.startElement(colElementName, component);
        if (colElementName.equals(HTML.TH_ELEM))
        {
            writer.writeAttribute(HTML.SCOPE_ATTR, HTML.SCOPE_COLGROUP_VALUE, null);
        }
        writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(colspan), null);
        if (styleClass != null)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }
        if (facet != null)
        {
            RendererUtils.renderChild(facesContext, facet);
        }
        writer.endElement(colElementName);
        writer.endElement(HTML.TR_ELEM);
    }

    private void renderColumnHeaderOrFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent component,
            String styleClass, boolean header) throws IOException
    {
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        writer.startElement(HTML.TR_ELEM, component);
        for (Iterator it = component.getChildren().iterator(); it.hasNext();)
        {
            UIComponent uiComponent = (UIComponent) it.next();
            if (uiComponent instanceof UIColumn && ((UIColumn) uiComponent).isRendered())
            {
                if (header)
                {
                    renderColumnHeaderCell(facesContext, writer, (UIColumn) uiComponent, styleClass, 0);
                }
                else
                {
                    renderColumnFooterCell(facesContext, writer, (UIColumn) uiComponent, styleClass, 0);
                }
            }
        }
        writer.endElement(HTML.TR_ELEM);
    }

	/**
	 * Renders the header facet for the given <code>UIColumn</code>.
	 * @param facesContext the <code>FacesContext</code>.
	 * @param writer the <code>ResponseWriter</code>.
	 * @param uiColumn the <code>UIColumn</code>.
	 * @param footerStyleClass the styleClass of the header facet.
	 * @param colspan the colspan for the tableData element in which the header facet
	 * will be wrapped.
	 * @throws IOException
	 */
    protected void renderColumnHeaderCell(FacesContext facesContext, ResponseWriter writer, UIColumn uiColumn,
            String headerStyleClass, int colspan) throws IOException
    {
        writer.startElement(HTML.TH_ELEM, uiColumn);
        if (colspan > 1)
        {
            writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(colspan), null);
        }
        if (headerStyleClass != null)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, headerStyleClass, null);
        }
        UIComponent facet = uiColumn.getHeader();
        if (facet != null)
        {
            RendererUtils.renderChild(facesContext, facet);
        }
        writer.endElement(HTML.TH_ELEM);
    }

	/**
	 * Renders the footer facet for the given <code>UIColumn</code>.
	 * @param facesContext the <code>FacesContext</code>.
	 * @param writer the <code>ResponseWriter</code>.
	 * @param uiColumn the <code>UIColumn</code>.
	 * @param footerStyleClass the styleClass of the footer facet.
	 * @param colspan the colspan for the tableData element in which the footer facet
	 * will be wrapped.
	 * @throws IOException
	 */
    protected void renderColumnFooterCell(FacesContext facesContext, ResponseWriter writer, UIColumn uiColumn,
            String footerStyleClass, int colspan) throws IOException
    {
        writer.startElement(HTML.TD_ELEM, uiColumn);
        if (colspan > 1)
        {
            writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(colspan), null);
        }
        if (footerStyleClass != null)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, footerStyleClass, null);
        }
        UIComponent facet = uiColumn.getFooter();
        if (facet != null)
        {
            RendererUtils.renderChild(facesContext, facet);
        }
        writer.endElement(HTML.TD_ELEM);
    }

	/**
	 * Gets the headerClass attribute of the given <code>UIComponent</code>.
	 * @param component the <code>UIComponent</code>.
	 * @return the headerClass attribute of the given <code>UIComponent</code>.
	 */
    protected static String getHeaderClass(UIComponent component)
    {
        if (component instanceof HtmlDataTable)
        {
            return ((HtmlDataTable) component).getHeaderClass();
        }
        else
        {
            return (String) component.getAttributes().get(JSFAttr.HEADER_CLASS_ATTR);
        }
    }

	/**
	 * Gets the footerClass attribute of the given <code>UIComponent</code>.
	 * @param component the <code>UIComponent</code>.
	 * @return the footerClass attribute of the given <code>UIComponent</code>.
	 */
    protected static String getFooterClass(UIComponent component)
    {
        if (component instanceof HtmlDataTable)
        {
            return ((HtmlDataTable) component).getFooterClass();
        }
        else
        {
            return (String) component.getAttributes().get(JSFAttr.FOOTER_CLASS_ATTR);
        }
    }

    //-------------------------------------------------------------
    // Helper class Styles
    //-------------------------------------------------------------
    private static class Styles
    {
        //~ Instance fields
        // ------------------------------------------------------------------------

        private String[] _columnStyle;

        private String[] _rowStyle;

        //~ Constructors
        // ---------------------------------------------------------------------------
        Styles(String rowStyles, String columnStyles)
        {
            _rowStyle = (rowStyles == null) ? ArrayUtils.EMPTY_STRING_ARRAY : StringUtils.trim(StringUtils
                    .splitShortString(rowStyles, ','));
            _columnStyle = (columnStyles == null) ? ArrayUtils.EMPTY_STRING_ARRAY : StringUtils.trim(StringUtils
                    .splitShortString(columnStyles, ','));
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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTableRenderer
    extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlTableRenderer.class);

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataTable.class);

        HtmlDataTable dataTable = (HtmlDataTable)uiComponent;

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.TABLE_ELEM, dataTable);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);

        writeFacet(facesContext, dataTable, "header");
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        UIData uiData = (UIData)uiComponent;
        uiData.getRows();

        writer.startElement("tbody", uiComponent);

        int displayRows = uiData.getRows();
        if (displayRows == 0)
        {
            displayRows = uiData.getRowCount();
        }

        for (int i = 0; i < displayRows; i++)
        {
            uiData.setRowIndex(i);
            if (!uiData.isRowAvailable())
            {
                log.error("Row is not available. Rowindex = " + i);
                return;
            }

            writer.startElement("tr", uiComponent);
            for (Iterator it = uiComponent.getChildren().iterator(); it.hasNext(); )
            {
                UIColumn uiColumn = (UIColumn)it.next();
                if (uiColumn.isRendered())
                {
                    writer.startElement("td", uiComponent);
                    encodeRecursive(facesContext, uiColumn);
                    writer.endElement("td");
                }
            }
            writer.endElement("tr");
        }
        writer.write("tbody");
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writeFacet(facesContext, uiComponent, "footer");
        writer.endElement(HTML.TABLE_ELEM);
    }

    private void writeFacet(FacesContext facesContext, UIComponent uiData, String facetName) throws IOException
    {
        boolean hasFacets = false;
        for (Iterator it = uiData.getChildren().iterator(); it.hasNext(); )
        {
            UIColumn uiColumn = (UIColumn)it.next();
            if (uiColumn.isRendered())
            {
                UIComponent facetComp = uiColumn.getFacet(facetName);
                if (facetComp != null)
                {
                    hasFacets =  true;
                    break;
                }
            }
        }
        if (hasFacets)
        {
            ResponseWriter writer = facesContext.getResponseWriter();

            String elemName = facetName.equals("header") ? "thead" : "tfoot";
            writer.startElement(elemName, uiData);
            writer.startElement("tr", uiData);
            for (Iterator it = uiData.getChildren().iterator(); it.hasNext(); )
            {
                UIColumn uiColumn = (UIColumn)it.next();
                if (uiColumn.isRendered())
                {
                    UIComponent headerComp = (UIComponent)uiColumn.getFacet("header");

                    writer.startElement("td", uiData);
                    if (headerComp != null)
                    {
                        encodeRecursive(facesContext, headerComp);
                    }
                    writer.endElement("td");
                }
            }
            writer.endElement("tr");
            writer.endElement(elemName);
        }

    }

    private void encodeRecursive(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        uiComponent.encodeBegin(facesContext);
        if (uiComponent.getRendersChildren())
        {
            uiComponent.encodeChildren(facesContext);
        }
        else
        {
            List childs = uiComponent.getChildren();
            for (int i = 0, size = childs.size(); i < size; i++)
            {
                encodeRecursive (facesContext, (UIComponent)childs.get(i));
            }
        }
        uiComponent.encodeEnd(facesContext);
    }

}

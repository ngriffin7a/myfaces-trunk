/**
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
import net.sourceforge.myfaces.util.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * X-CHECKED: tlddoc h:panelGrid 1.0 final
 *
 * $Log$
 * Revision 1.6  2004/03/25 12:42:24  manolito
 * x-checked and corrected GridRenderer
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class HtmlGridRenderer
    extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlGridRenderer.class);

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent component)
            throws IOException
    {
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, UIPanel.class);

        int columns;
        if (component instanceof HtmlPanelGrid)
        {
            columns = ((HtmlPanelGrid)component).getColumns();
        }
        else
        {
            Integer i = (Integer)component.getAttributes().get(JSFAttr.COLUMNS_ATTR);
            columns = i != null ? i.intValue() : 0;
        }

        if (log.isErrorEnabled())
        {
            if (columns <= 0)
            {
                log.error("Wrong columns attribute for PanelGrid " + component.getClientId(facesContext) + ": " + columns);
                columns = 1;
            }
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.TABLE_ELEM, component);
        HTMLUtil.renderHTMLAttributes(writer, component, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
        writer.flush();

        renderHeaderOrFooter(facesContext, writer, component, columns, true);   //Header facet

        renderChildren(facesContext, writer, component, columns);

        renderHeaderOrFooter(facesContext, writer, component, columns, false);  //Footer facet

        writer.endElement(HTML.TABLE_ELEM);
    }


    private void renderHeaderOrFooter(FacesContext context,
                                      ResponseWriter writer,
                                      UIComponent component,
                                      int columns,
                                      boolean header)
        throws IOException
    {
        UIComponent facet = component.getFacet(header ? "header" : "footer");
        if (facet == null) return;

        HtmlRendererUtils.writePrettyLineSeparator(context);
        writer.startElement(header ? HTML.THEAD_ELEM : HTML.TFOOT_ELEM, component);
        writer.startElement(HTML.TR_ELEM, component);
        writer.startElement(header ? HTML.TH_ELEM : HTML.TD_ELEM, component);

        String styleClass;
        if (component instanceof HtmlPanelGrid)
        {
            styleClass = header ?
                         ((HtmlPanelGrid)component).getHeaderClass() :
                         ((HtmlPanelGrid)component).getFooterClass();
        }
        else
        {
            styleClass = header ?
                         (String)component.getAttributes().get(JSFAttr.HEADER_CLASS_ATTR) :
                         (String)component.getAttributes().get(JSFAttr.FOOTER_CLASS_ATTR);
        }
        if (styleClass != null)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass,
                                  header ? JSFAttr.HEADER_CLASS_ATTR : JSFAttr.FOOTER_CLASS_ATTR);
        }

        if (header)
        {
            writer.writeAttribute(HTML.SCOPE_ATTR, HTML.SCOPE_COLGROUP_VALUE, null);
        }

        writer.writeAttribute(HTML.COLSPAN_ATTR, Integer.toString(columns), null);

        HtmlRendererUtils.writePrettyLineSeparator(context);
        RendererUtils.renderChild(context, facet);

        HtmlRendererUtils.writePrettyLineSeparator(context);
        writer.endElement(header ? HTML.TH_ELEM : HTML.TD_ELEM);
        writer.endElement(HTML.TR_ELEM);
        writer.endElement(header ? HTML.THEAD_ELEM : HTML.TFOOT_ELEM);
    }


    private void renderChildren(FacesContext context,
                                ResponseWriter writer,
                                UIComponent component,
                                int columns)
        throws IOException
    {
        writer.startElement(HTML.TBODY_ELEM, component);

        String columnClasses;
        if (component instanceof HtmlPanelGrid)
        {
            columnClasses = ((HtmlPanelGrid)component).getColumnClasses();
        }
        else
        {
            columnClasses = (String)component.getAttributes().get(JSFAttr.COLUMN_CLASSES_ATTR);
        }

        String[] columnClassesArray;
        int columnClassesCount;
        if (columnClasses == null)
        {
            columnClassesCount = 0;
            columnClassesArray = ArrayUtils.EMPTY_STRING_ARRAY;
        }
        else
        {
            StringTokenizer st = new StringTokenizer(columnClasses, ",");
            columnClassesCount = st.countTokens();
            columnClassesArray = new String[columnClassesCount];
            for (int i = 0; i < columnClassesCount; i++)
            {
                columnClassesArray[i] = st.nextToken().trim();
            }
        }

        int childCount = component.getChildCount();
        if (childCount > 0)
        {
            int c = 0;
            boolean rowStarted = false;
            for (Iterator it = component.getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                if (child.isRendered())
                {
                    if (c == 0)
                    {
                        //start of new/next row
                        if (rowStarted)
                        {
                            //do we have to close the last row?
                            writer.endElement(HTML.TR_ELEM);
                            HtmlRendererUtils.writePrettyLineSeparator(context);
                        }
                        writer.startElement(HTML.TR_ELEM, component);
                        rowStarted = true;
                    }

                    writer.startElement(HTML.TD_ELEM, component);
                    if (c < columnClassesCount)
                    {
                        writer.writeAttribute(HTML.CLASS_ATTR, columnClassesArray[c], null);
                    }
                    RendererUtils.renderChild(context, child);
                    writer.endElement(HTML.TD_ELEM);

                    c++;
                    if (c >= columns) c = 0;
                }
            }

            if (rowStarted)
            {
                if (c > 0)
                {
                    if (log.isWarnEnabled()) log.warn("PanelGrid " + component.getClientId(context) + " has not enough children. Child count should be a multiple of the columns attribute.");
                    //Render empty columns, so that table is correct
                    for ( ; c < columns; c++)
                    {
                        writer.startElement(HTML.TD_ELEM, component);
                        writer.endElement(HTML.TD_ELEM);
                    }
                }
                writer.endElement(HTML.TR_ELEM);
                HtmlRendererUtils.writePrettyLineSeparator(context);
            }
        }

        writer.endElement(HTML.TBODY_ELEM);
    }

}

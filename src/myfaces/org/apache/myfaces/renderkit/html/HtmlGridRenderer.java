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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
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

        if (log.isErrorEnabled() && columns == 0)
        {
            log.error("Wrong columns attribute for PanelGrid " + component.getClientId(facesContext) + ": " + columns);
        }

        int childCount = component.getChildCount();
        if (childCount > 0)
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.startElement(HTML.TABLE_ELEM, component);
            HTMLUtil.renderHTMLAttributes(writer, component, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
            writer.flush();
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);

            int c = 0;
            boolean rowStarted = false;
            for (Iterator it = component.getChildren().iterator(); it.hasNext(); )
            {
                if (c == 0)
                {
                    if (rowStarted)
                    {
                        writer.endElement(HTML.TR_ELEM);
                        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
                    }
                    writer.startElement(HTML.TR_ELEM, component);
                    rowStarted = true;
                }

                writer.startElement(HTML.TD_ELEM, component);
                RendererUtils.renderChild(facesContext, (UIComponent)it.next());
                writer.endElement(HTML.TD_ELEM);

                c++;
                if (c >= columns) c = 0;
            }

            if (rowStarted)
            {
                if (c > 0)
                {
                    if (log.isWarnEnabled()) log.warn("PanelGrid " + component.getClientId(facesContext) + " has not enough children. Child count should be a multiple of the columns attribute.");
                    for ( ; c < columns; c++)
                    {
                        writer.startElement(HTML.TD_ELEM, component);
                        writer.endElement(HTML.TD_ELEM);
                    }
                }
                writer.endElement(HTML.TR_ELEM);
                HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            }

            writer.endElement(HTML.TABLE_ELEM);
        }
    }

}

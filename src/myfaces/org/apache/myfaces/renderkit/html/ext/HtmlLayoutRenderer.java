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

import net.sourceforge.myfaces.component.ext.HtmlPanelLayout;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlLayoutRenderer
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlLayoutRenderer.class);

    public static final String CLASSIC_LAYOUT = "classic";
    public static final String NAV_RIGHT_LAYOUT = "navigationRight";
    public static final String UPSIDE_DOWN_LAYOUT = "upsideDown";

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException
    {
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, HtmlPanelLayout.class);

        HtmlPanelLayout panelLayout = (HtmlPanelLayout)component;

        String layout = panelLayout.getLayout();
        if (layout == null || layout.equals(CLASSIC_LAYOUT))
        {
            renderClassic(facesContext, panelLayout);
        }
        else if (layout.equals(NAV_RIGHT_LAYOUT))
        {
            renderNavRight(facesContext, panelLayout);
        }
        else if (layout.equals(UPSIDE_DOWN_LAYOUT))
        {
            renderUpsideDown(facesContext, panelLayout);
        }
        else
        {
            log.error("Unknown panel layout '" + layout + "'!");
        }
    }

    protected void renderClassic(FacesContext facesContext, HtmlPanelLayout panelLayout)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        UIComponent header = panelLayout.getHeader();
        UIComponent navigation = panelLayout.getNavigation();
        UIComponent body = panelLayout.getBody();
        UIComponent footer = panelLayout.getFooter();

        writer.startElement(HTML.TABLE_ELEM, null);
        HTMLUtil.renderHTMLAttributes(writer, panelLayout, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
        if (header != null)
        {
            writer.startElement(HTML.TR_ELEM, null);
            renderTableCell(facesContext, writer, header,
                            (navigation != null && body != null) ? 2 : 1,
                            panelLayout.getHeaderClass(),
                            panelLayout.getHeaderStyle());
            writer.endElement(HTML.TR_ELEM);
        }
        if (navigation != null || body != null)
        {
            writer.startElement(HTML.TR_ELEM, null);
            if (navigation != null)
            {
                renderTableCell(facesContext, writer, navigation, 0,
                                panelLayout.getNavigationClass(),
                                panelLayout.getNavigationStyle());
            }
            if (body != null)
            {
                renderTableCell(facesContext, writer, body, 0,
                                panelLayout.getBodyClass(),
                                panelLayout.getBodyStyle());
            }
            writer.endElement(HTML.TR_ELEM);
        }
        if (footer != null)
        {
            writer.startElement(HTML.TR_ELEM, null);
            renderTableCell(facesContext, writer, footer,
                            (navigation != null && body != null) ? 2 : 1,
                            panelLayout.getFooterClass(),
                            panelLayout.getFooterStyle());
            writer.endElement(HTML.TR_ELEM);
        }
        writer.endElement(HTML.TABLE_ELEM);
    }


    protected void renderNavRight(FacesContext facesContext, HtmlPanelLayout panelLayout)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        UIComponent header = panelLayout.getHeader();
        UIComponent navigation = panelLayout.getNavigation();
        UIComponent body = panelLayout.getBody();
        UIComponent footer = panelLayout.getFooter();

        writer.startElement(HTML.TABLE_ELEM, null);
        HTMLUtil.renderHTMLAttributes(writer, panelLayout, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
        if (header != null)
        {
            writer.startElement(HTML.TR_ELEM, null);
            renderTableCell(facesContext, writer, header,
                            (navigation != null && body != null) ? 2 : 1,
                            panelLayout.getHeaderClass(),
                            panelLayout.getHeaderStyle());
            writer.endElement(HTML.TR_ELEM);
        }
        if (navigation != null || body != null)
        {
            writer.startElement(HTML.TR_ELEM, null);
            if (body != null)
            {
                renderTableCell(facesContext, writer, body, 0,
                                panelLayout.getBodyClass(),
                                panelLayout.getBodyStyle());
            }
            if (navigation != null)
            {
                renderTableCell(facesContext, writer, navigation, 0,
                                panelLayout.getNavigationClass(),
                                panelLayout.getNavigationStyle());
            }
            writer.endElement(HTML.TR_ELEM);
        }
        if (footer != null)
        {
            writer.startElement(HTML.TR_ELEM, null);
            renderTableCell(facesContext, writer, footer,
                            (navigation != null && body != null) ? 2 : 1,
                            panelLayout.getFooterClass(),
                            panelLayout.getFooterStyle());
            writer.endElement(HTML.TR_ELEM);
        }
        writer.endElement(HTML.TABLE_ELEM);
    }


    protected void renderUpsideDown(FacesContext facesContext, HtmlPanelLayout panelLayout)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        UIComponent header = panelLayout.getHeader();
        UIComponent navigation = panelLayout.getNavigation();
        UIComponent body = panelLayout.getBody();
        UIComponent footer = panelLayout.getFooter();

        writer.startElement(HTML.TABLE_ELEM, null);
        HTMLUtil.renderHTMLAttributes(writer, panelLayout, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
        if (footer != null)
        {
            writer.startElement(HTML.TR_ELEM, null);
            renderTableCell(facesContext, writer, footer,
                            (navigation != null && body != null) ? 2 : 1,
                            panelLayout.getFooterClass(),
                            panelLayout.getFooterStyle());
            writer.endElement(HTML.TR_ELEM);
        }
        if (navigation != null || body != null)
        {
            writer.startElement(HTML.TR_ELEM, null);
            if (navigation != null)
            {
                renderTableCell(facesContext, writer, navigation, 0,
                                panelLayout.getNavigationClass(),
                                panelLayout.getNavigationStyle());
            }
            if (body != null)
            {
                renderTableCell(facesContext, writer, body, 0,
                                panelLayout.getBodyClass(),
                                panelLayout.getBodyStyle());
            }
            writer.endElement(HTML.TR_ELEM);
        }
        if (header != null)
        {
            writer.startElement(HTML.TR_ELEM, null);
            renderTableCell(facesContext, writer, header,
                            (navigation != null && body != null) ? 2 : 1,
                            panelLayout.getHeaderClass(),
                            panelLayout.getHeaderStyle());
            writer.endElement(HTML.TR_ELEM);
        }
        writer.endElement(HTML.TABLE_ELEM);
    }


    protected void renderTableCell(FacesContext facesContext,
                                   ResponseWriter writer,
                                   UIComponent component,
                                   int colspan,
                                   String styleClass,
                                   String style)
            throws IOException
    {
        writer.startElement(HTML.TD_ELEM, null);
        if (colspan > 0)
        {
            writer.writeAttribute(HTML.COLSPAN_ATTR, Integer.toString(colspan), null);
        }
        if (styleClass != null)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }
        if (style != null)
        {
            writer.writeAttribute(HTML.STYLE_ATTR, style, null);
        }

        RendererUtils.renderChild(facesContext, component);

        writer.endElement(HTML.TD_ELEM);
    }

}

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

import net.sourceforge.myfaces.component.ext.HtmlCommandNavigation;
import net.sourceforge.myfaces.component.ext.HtmlOutputNavigation;
import net.sourceforge.myfaces.component.ext.HtmlPanelNavigation;
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
import java.util.Iterator;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlNavigationRenderer
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlNavigationRenderer.class);

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
        RendererUtils.checkParamValidity(facesContext, component, HtmlPanelNavigation.class);
        ResponseWriter writer = facesContext.getResponseWriter();
        HtmlPanelNavigation panelNav = (HtmlPanelNavigation)component;

        if (panelNav.getChildCount() > 0)
        {
            writer.startElement(HTML.TABLE_ELEM, null);
            HTMLUtil.renderHTMLAttributes(writer, panelNav, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
            if (panelNav.getStyle() == null && panelNav.getStyleClass() == null)
            {
                writer.writeAttribute(HTML.BORDER_ATTR, panelNav, null);
            }

            renderChildren(facesContext, writer, panelNav, panelNav.getChildren(), 0);

            writer.endElement(HTML.TABLE_ELEM);
        }
        else
        {
            if (log.isWarnEnabled()) log.warn("Navigation panel without children.");
        }
    }


    protected void renderChildren(FacesContext facesContext,
                                  ResponseWriter writer,
                                  HtmlPanelNavigation panelNav,
                                  List children,
                                  int level)
            throws IOException
    {
        for (Iterator it = children.iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (!child.isRendered()) continue;
            if (child instanceof HtmlCommandNavigation)
            {
                //navigation item
                writer.startElement(HTML.TR_ELEM, null);
                writer.startElement(HTML.TD_ELEM, null);
                writeItemCellAttributes(writer, panelNav, (HtmlCommandNavigation)child);
                indent(writer, level);
                child.encodeBegin(facesContext);
                child.encodeEnd(facesContext);
                if (child.getChildCount() > 0)
                {
                    renderChildren(facesContext, writer, panelNav, child.getChildren(), level + 1);
                }
                writer.endElement(HTML.TD_ELEM);
                writer.endElement(HTML.TR_ELEM);
            }
            else if (child instanceof HtmlOutputNavigation)
            {
                //separator
                writer.startElement(HTML.TR_ELEM, null);
                writer.startElement(HTML.TD_ELEM, null);
                writeSeparatorAttributes(writer, panelNav, (HtmlOutputNavigation)child);
                indent(writer, level);
                child.encodeBegin(facesContext);
                child.encodeEnd(facesContext);
                if (child.getChildCount() > 0)
                {
                    renderChildren(facesContext, writer, panelNav, child.getChildren(), level + 1);
                }
                writer.endElement(HTML.TD_ELEM);
                writer.endElement(HTML.TR_ELEM);
            }
            else
            {
                //unknown
                if (log.isWarnEnabled()) log.warn("Unsupported navigation item with id " + child.getClientId(facesContext) + " (renderer type " + child.getRendererType() + ").");
                writer.startElement(HTML.TR_ELEM, null);
                writer.startElement(HTML.TD_ELEM, null);
                indent(writer, level);
                RendererUtils.renderChild(facesContext, child);
                writer.endElement(HTML.TD_ELEM);
                writer.endElement(HTML.TR_ELEM);
            }

            if (log.isDebugEnabled())
            {
                writer.write("\n");    
            }
        }
    }


    protected void indent(ResponseWriter writer, int level)
        throws IOException
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < level; i++)
        {
            buf.append("&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        writer.write(buf.toString());
    }


    protected void writeItemCellAttributes(ResponseWriter writer,
                                            HtmlPanelNavigation navPanel,
                                            HtmlCommandNavigation navItem)
            throws IOException
    {
        String style;
        String styleClass;
        if (navItem.isActive())
        {
            style = navPanel.getActiveItemStyle();
            styleClass = navPanel.getActiveItemClass();
        }
        else if (navItem.isOpen())
        {
            style = navPanel.getOpenItemStyle();
            styleClass = navPanel.getOpenItemClass();
        }
        else
        {
            style = navPanel.getItemStyle();
            styleClass = navPanel.getItemClass();
        }

        if (style != null)
        {
            writer.writeAttribute(HTML.STYLE_ATTR, style, null);
        }

        if (styleClass != null)
        {
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, styleClass, null);
        }
    }

    protected void writeSeparatorAttributes(ResponseWriter writer,
                                             HtmlPanelNavigation navPanel,
                                             HtmlOutputNavigation navItem)
            throws IOException
    {
        String style = navPanel.getSeparatorStyle();
        String styleClass = navPanel.getSeparatorClass();

        if (style != null)
        {
            writer.writeAttribute(HTML.STYLE_ATTR, style, null);
        }

        if (styleClass != null)
        {
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, styleClass, null);
        }
    }

}

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
package net.sourceforge.myfaces.custom.navigation;

import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;
import net.sourceforge.myfaces.renderkit.html.ext.HtmlLinkRenderer;

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
 * $Log$
 * Revision 1.2  2004/05/18 17:08:21  manolito
 * no message
 *
 */
public class HtmlNavigationRenderer
        extends HtmlLinkRenderer
{
    private static final Log log = LogFactory.getLog(HtmlNavigationRenderer.class);

    private static final Integer ZERO_INTEGER = new Integer(0);

    public boolean getRendersChildren()
    {
        return true;
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        if (component instanceof HtmlCommandNavigation)
        {
            //HtmlCommandNavigation
            super.decode(facesContext, component);
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException
    {
        if (component instanceof HtmlCommandNavigation)
        {
            //HtmlCommandNavigation
            super.encodeBegin(facesContext, component);
        }
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {
        if (component instanceof HtmlCommandNavigation)
        {
            //HtmlCommandNavigation
            super.encodeChildren(facesContext, component);
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        if (component instanceof HtmlCommandNavigation)
        {
            //HtmlCommandNavigation
            super.encodeEnd(facesContext, component);
            return;
        }

        RendererUtils.checkParamValidity(facesContext, component, HtmlPanelNavigation.class);
        ResponseWriter writer = facesContext.getResponseWriter();
        HtmlPanelNavigation panelNav = (HtmlPanelNavigation)component;

        if (panelNav.getChildCount() > 0)
        {
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.startElement(HTML.TABLE_ELEM, null);
            HtmlRendererUtils.renderHTMLAttributes(writer, panelNav, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
            if (panelNav.getStyle() == null && panelNav.getStyleClass() == null)
            {
                writer.writeAttribute(HTML.BORDER_ATTR, ZERO_INTEGER, null);
            }

            renderChildren(facesContext, writer, panelNav, panelNav.getChildren(), 0);

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
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
                HtmlRendererUtils.writePrettyLineSeparator(facesContext);

                String style = getNavigationItemStyle(panelNav, (HtmlCommandNavigation)child);
                String styleClass = getNavigationItemClass(panelNav, (HtmlCommandNavigation)child);

                writer.startElement(HTML.TR_ELEM, null);
                writer.startElement(HTML.TD_ELEM, null);
                writeStyleAttributes(writer, style, styleClass);

                if (style != null || styleClass != null)
                {
                    writer.startElement(HTML.SPAN_ELEM, null);
                    writeStyleAttributes(writer, style, styleClass);
                }
                indent(writer, level);
                child.encodeBegin(facesContext);
                child.encodeEnd(facesContext);
                if (style != null || styleClass != null)
                {
                    writer.endElement(HTML.SPAN_ELEM);
                }

                writer.endElement(HTML.TD_ELEM);
                writer.endElement(HTML.TR_ELEM);

                if (child.getChildCount() > 0)
                {
                    renderChildren(facesContext, writer, panelNav, child.getChildren(), level + 1);
                }
            }
            else
            {
                //separator
                HtmlRendererUtils.writePrettyLineSeparator(facesContext);

                String style = panelNav.getSeparatorStyle();
                String styleClass = panelNav.getSeparatorClass();

                writer.startElement(HTML.TR_ELEM, null);
                writer.startElement(HTML.TD_ELEM, null);
                writeStyleAttributes(writer, style, styleClass);

                if (style != null || styleClass != null)
                {
                    writer.startElement(HTML.SPAN_ELEM, null);
                    writeStyleAttributes(writer, style, styleClass);
                }
                indent(writer, level);
                RendererUtils.renderChild(facesContext, child);
                if (style != null || styleClass != null)
                {
                    writer.endElement(HTML.SPAN_ELEM);
                }

                writer.endElement(HTML.TD_ELEM);
                writer.endElement(HTML.TR_ELEM);
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



    protected String getNavigationItemStyle(HtmlPanelNavigation navPanel,
                                            HtmlCommandNavigation navItem)
    {
        if (navItem.isActive())
        {
            return navPanel.getActiveItemStyle();
        }
        else if (navItem.isOpen())
        {
            return navPanel.getOpenItemStyle();
        }
        else
        {
            return navPanel.getItemStyle();
        }
    }

    protected String getNavigationItemClass(HtmlPanelNavigation navPanel,
                                            HtmlCommandNavigation navItem)
    {
        if (navItem.isActive())
        {
            return navPanel.getActiveItemClass();
        }
        else if (navItem.isOpen())
        {
            return navPanel.getOpenItemClass();
        }
        else
        {
            return navPanel.getItemClass();
        }
    }



    protected void writeStyleAttributes(ResponseWriter writer,
                                        String style,
                                        String styleClass)
            throws IOException
    {
        HtmlRendererUtils.renderHTMLAttribute(writer, HTML.STYLE_ATTR, HTML.STYLE_ATTR, style);
        HtmlRendererUtils.renderHTMLAttribute(writer, HTML.STYLE_CLASS_ATTR, HTML.STYLE_CLASS_ATTR, styleClass);
    }


    protected String getStyle(FacesContext facesContext, UIComponent link)
    {
        if (!(link instanceof HtmlCommandNavigation))
        {
            throw new IllegalArgumentException();
        }

        UIComponent navPanel = link.getParent();
        while (navPanel != null && !(navPanel instanceof HtmlPanelNavigation))
        {
            navPanel = navPanel.getParent();
        }
        if (navPanel == null)
        {
            throw new IllegalStateException("HtmlCommandNavigation not nested in HtmlPanelNavigation!?");
        }

        HtmlCommandNavigation navItem = (HtmlCommandNavigation)link;
        if (navItem.isActive())
        {
            return ((HtmlPanelNavigation)navPanel).getActiveItemStyle();
        }
        else if (navItem.isOpen())
        {
            return ((HtmlPanelNavigation)navPanel).getOpenItemStyle();
        }
        else
        {
            return ((HtmlPanelNavigation)navPanel).getItemStyle();
        }
    }


    protected String getStyleClass(FacesContext facesContext, UIComponent link)
    {
        if (!(link instanceof HtmlCommandNavigation))
        {
            throw new IllegalArgumentException();
        }

        UIComponent navPanel = link.getParent();
        while (navPanel != null && !(navPanel instanceof HtmlPanelNavigation))
        {
            navPanel = navPanel.getParent();
        }
        if (navPanel == null)
        {
            throw new IllegalStateException("HtmlCommandNavigation not nested in HtmlPanelNavigation!?");
        }

        HtmlCommandNavigation navItem = (HtmlCommandNavigation)link;
        if (navItem.isActive())
        {
            return ((HtmlPanelNavigation)navPanel).getActiveItemClass();
        }
        else if (navItem.isOpen())
        {
            return ((HtmlPanelNavigation)navPanel).getOpenItemClass();
        }
        else
        {
            return ((HtmlPanelNavigation)navPanel).getItemClass();
        }
    }



}

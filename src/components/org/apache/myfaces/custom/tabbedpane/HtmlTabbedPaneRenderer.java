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
package net.sourceforge.myfaces.custom.tabbedpane;

import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HtmlRenderer;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.application.ViewHandler;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/05/18 12:02:13  manolito
 * getActionURL and getResourceURL must not call encodeActionURL or encodeResourceURL
 *
 */
public class HtmlTabbedPaneRenderer
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlTabbedPaneRenderer.class);

    private static final String TABLE_STYLE =
        "border-style: none; " +
        "padding: 0px; " +
        "border-spacing: 0px; " +
        "empty-cells: show; ";

    private static final String ACTIVE_HEADER_CELL_STYLE =
        "border-top: 2px outset #CCCCCC; " +
        "border-right: 2px outset #CCCCCC; " +
        "border-bottom: 0px none; " +
        "border-left: 2px outset #CCCCCC; " +
        "text-align: center; ";

    private static final String INACTIVE_HEADER_CELL_STYLE =
        "border-top: 1px outset #CCCCCC; " +
        "border-right: 1px outset #CCCCCC; " +
        "border-bottom: 0px none; " +
        "border-left: 1px outset #CCCCCC; " +
        "text-align: center; " +
        "background-color: #CCCCCC; ";

    private static final String EMPTY_HEADER_CELL_STYLE =
        "border-top: 0px none; " +
        "border-right: 0px none; " +
        "border-bottom: 0px none; " +
        "border-left: 0px none; ";

    private static final String SUB_HEADER_CELL_STYLE =
        "height: 2px; " +
        "line-height: 0px; font-size: 0px; " +
        "border-bottom: 0px none; ";

    private static final String TAB_CELL_STYLE =
        "border-top: 0px none; " +
        "border-right: 2px outset #CCCCCC; " +
        "border-bottom: 2px outset #CCCCCC; " +
        "border-left: 2px outset #CCCCCC; " +
        "padding: 10px; ";

    private static final String NO_BORDER_STYLE =
        "0px none; ";

    private static final String BORDER_STYLE =
        "2px outset #CCCCCC; ";

    private static final String BUTTON_STYLE_ACTIVE
        = "border-style:none; width:100%; cursor:pointer;";
    private static final String BUTTON_STYLE_INACTIVE
        = "border-style:none; width:100%; cursor:pointer; background-color:#CCCCCC;";

    private static final String DEFAULT_BG_COLOR = "#FFFFFF";

    private static final String AUTO_FORM_SUFFIX = ".autoform";


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
    }

    public boolean getRendersChildren()
    {
        return true;
    }
    
    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent) throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlPanelTabbedPane.class);

        ResponseWriter writer = facesContext.getResponseWriter();

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        HtmlPanelTabbedPane tabbedPane = (HtmlPanelTabbedPane)uiComponent;
        int selectedIndex = tabbedPane.getSelectedIndex();

        if (tabbedPane.getBgcolor() == null)
        {
            tabbedPane.setBgcolor(DEFAULT_BG_COLOR);
        }

        UIForm parentForm = RendererUtils.findParentForm(tabbedPane);
        if (parentForm == null)
        {
            writeFormStart(writer, facesContext, tabbedPane);
        }

        writeTableStart(writer, facesContext, tabbedPane);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TR_ELEM, uiComponent);

        //Tab headers
        int tabIdx = 0;
        List children = tabbedPane.getChildren();
        for (int i = 0, len = children.size(); i < len; i++)
        {
            UIComponent child = getUIComponent((UIComponent)children.get(i));
            if (child instanceof HtmlPanelTab)
            {
                writeHeaderCell(writer, facesContext, tabbedPane,
                                (HtmlPanelTab)child, tabIdx, tabIdx == selectedIndex);
                tabIdx++;
            }
        }
        int tabCount = tabIdx;

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        HtmlRendererUtils.writePrettyIndent(facesContext);
        writer.startElement(HTML.TD_ELEM, uiComponent);
        writer.writeAttribute(HTML.STYLE_ATTR, EMPTY_HEADER_CELL_STYLE, null);
        writer.write("&nbsp;");
        writer.endElement(HTML.TD_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TR_ELEM);

        //Sub header cells
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TR_ELEM, uiComponent);
        writeSubHeaderCells(writer,  facesContext, tabbedPane, tabCount, selectedIndex);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TR_ELEM);

        //Tab
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TR_ELEM, uiComponent);
        writeTabCell(writer,  facesContext, tabbedPane, tabCount, selectedIndex);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TR_ELEM);

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TABLE_ELEM);

        if (parentForm == null)
        {
            writeFormEnd(writer, facesContext);
        }
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlPanelTabbedPane.class);

        HtmlPanelTabbedPane tabbedPane = (HtmlPanelTabbedPane)uiComponent;

        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();

        int tabIdx = 0;
        List children = tabbedPane.getChildren();
        for (int i = 0, len = children.size(); i < len; i++)
        {
            UIComponent child = getUIComponent((UIComponent)children.get(i));
            if (child instanceof HtmlPanelTab)
            {
                String paramName = tabbedPane.getClientId(facesContext) + "." + tabIdx;
                String paramValue = (String)paramMap.get(paramName);
                if (paramValue != null && paramValue.length() > 0)
                {
                    tabbedPane.queueEvent(new TabChangeEvent(tabbedPane,
                                                             tabbedPane.getSelectedIndex(),
                                                             tabIdx));
                    return;
                }
                tabIdx++;
            }
        }
    }


    protected void writeFormStart(ResponseWriter writer,
                                  FacesContext facesContext,
                                  UIComponent tabbedPane)
        throws IOException
    {
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String viewId = facesContext.getViewRoot().getViewId();
        String actionURL = viewHandler.getActionURL(facesContext, viewId);

        //write out auto form
        writer.startElement(HTML.FORM_ELEM, null);
        writer.writeAttribute(HTML.NAME_ATTR, tabbedPane.getClientId(facesContext) + AUTO_FORM_SUFFIX, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "display:inline", null);
        writer.writeAttribute(HTML.METHOD_ATTR, "post", null);
        writer.writeURIAttribute(HTML.ACTION_ATTR,
                                 facesContext.getExternalContext().encodeActionURL(actionURL),
                                 null);
        writer.flush();
    }


    protected void writeTableStart(ResponseWriter writer,
                                   FacesContext facesContext,
                                   HtmlPanelTabbedPane tabbedPane)
        throws IOException
    {
        String oldStyle = tabbedPane.getStyle();
        if (oldStyle == null)
        {
            tabbedPane.setStyle(TABLE_STYLE);
        }
        else
        {
            tabbedPane.setStyle(TABLE_STYLE + "; " + oldStyle);
        }

        String oldBgColor = tabbedPane.getBgcolor();
        tabbedPane.setBgcolor(null);

        writer.startElement(HTML.TABLE_ELEM, tabbedPane);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
        HtmlRendererUtils.renderHTMLAttributes(writer, tabbedPane, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
        writer.flush();

        tabbedPane.setStyle(oldStyle);
        tabbedPane.setBgcolor(oldBgColor);
    }


    protected void writeHeaderCell(ResponseWriter writer,
                                   FacesContext facesContext,
                                   HtmlPanelTabbedPane tabbedPane,
                                   HtmlPanelTab tab,
                                   int tabIndex,
                                   boolean active)
        throws IOException
    {
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        HtmlRendererUtils.writePrettyIndent(facesContext);
        writer.startElement(HTML.TD_ELEM, tabbedPane);
        if (active)
        {
            writer.writeAttribute(HTML.STYLE_ATTR,
                                  ACTIVE_HEADER_CELL_STYLE + "background-color:" + tabbedPane.getBgcolor(),
                                  null);
        }
        else
        {
            writer.writeAttribute(HTML.STYLE_ATTR,
                                  INACTIVE_HEADER_CELL_STYLE,
                                  null);
        }

        //Button
        writer.startElement(HTML.INPUT_ELEM, tabbedPane);
        writer.writeAttribute(HTML.TYPE_ATTR, "submit", null);
        writer.writeAttribute(HTML.NAME_ATTR, tabbedPane.getClientId(facesContext) + "." + tabIndex, null);

        String label = tab.getLabel();
        if (label == null || label.length() == 0)
        {
            label = "Tab " + tabIndex;
        }
        writer.writeAttribute(HTML.VALUE_ATTR, label, null);



        if (active)
        {
            writer.writeAttribute(HTML.STYLE_ATTR,
                                  BUTTON_STYLE_ACTIVE + "background-color:" + tabbedPane.getBgcolor(),
                                  null);
        }
        else
        {
            writer.writeAttribute(HTML.STYLE_ATTR,
                                  BUTTON_STYLE_INACTIVE,
                                  null);
        }
        writer.endElement(HTML.INPUT_ELEM);

        writer.endElement(HTML.TD_ELEM);
    }


    protected void writeSubHeaderCells(ResponseWriter writer,
                                       FacesContext facesContext,
                                       HtmlPanelTabbedPane tabbedPane,
                                       int tabCount,
                                       int selectedIndex)
            throws IOException
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0, cnt = tabCount + 1; i < cnt; i++)
        {
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            HtmlRendererUtils.writePrettyIndent(facesContext);
            writer.startElement(HTML.TD_ELEM, tabbedPane);
            buf.setLength(0);
            buf.append(SUB_HEADER_CELL_STYLE);
            buf.append("border-top:").append(i == selectedIndex ? NO_BORDER_STYLE : BORDER_STYLE);
            buf.append("border-right:").append(i + 1 < cnt ? NO_BORDER_STYLE : BORDER_STYLE);
            buf.append("border-left:").append(i > 0 ? NO_BORDER_STYLE : BORDER_STYLE);
            buf.append("background-color:").append(tabbedPane.getBgcolor());
            writer.writeAttribute(HTML.STYLE_ATTR, buf.toString(), null);
            writer.write("&nbsp;");
            writer.endElement(HTML.TD_ELEM);
        }
    }


    protected void writeTabCell(ResponseWriter writer,
                                FacesContext facesContext,
                                HtmlPanelTabbedPane tabbedPane,
                                int tabCount,
                                int selectedIndex)
        throws IOException
    {
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        HtmlRendererUtils.writePrettyIndent(facesContext);
        writer.startElement(HTML.TD_ELEM, tabbedPane);
        writer.writeAttribute(HTML.COLSPAN_ATTR, Integer.toString(tabCount + 1), null);
        writer.writeAttribute(HTML.STYLE_ATTR, TAB_CELL_STYLE + "background-color:" + tabbedPane.getBgcolor(), null);

        int tabIdx = 0;
        List children = tabbedPane.getChildren();
        for (int i = 0, len = children.size(); i < len; i++)
        {
            UIComponent child = getUIComponent((UIComponent)children.get(i));
            if (child instanceof HtmlPanelTab)
            {
                if (tabIdx == selectedIndex)
                {
                    RendererUtils.renderChild(facesContext, child);
                }
                tabIdx++;
            }
            else
            {
                RendererUtils.renderChild(facesContext, child);
            }
        }

        writer.endElement(HTML.TD_ELEM);
    }

    private UIComponent getUIComponent(UIComponent uiComponent)
    {
        if (uiComponent instanceof NamingContainer)
        {
            List children = uiComponent.getChildren();
            for (int i = 0, len = children.size(); i < len; i++)
            {
                uiComponent = getUIComponent((UIComponent)children.get(i));
            }
        }
        return uiComponent;
    }


    protected void writeFormEnd(ResponseWriter writer,
                                FacesContext facesContext)
        throws IOException
    {
        //write state marker
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        viewHandler.writeState(facesContext);

        writer.endElement(HTML.FORM_ELEM);
    }

}

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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.attr.ext.TabbedPaneRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.GroupRenderer;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.legacy.FormRenderer;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

import javax.faces.FactoryFinder;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TabbedPaneRenderer
    extends GroupRenderer
    implements TabbedPaneRendererAttributes
{
    private static final String OLD_SELECTED_INDEX_ATTR
        = TabbedPaneRenderer.class.getName() + ".OLD_SELECTED_INDEX";

    protected static final String TAB_START_TOKEN = "[TAB-START]";
    protected static final String TAB_END_TOKEN = "[/TAB-END]";

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


    public static final String TYPE = "TabbedPane";
    public String getRendererType()
    {
        return TYPE;
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiTabbedPane)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        int tabCount = getTabCount(uiTabbedPane);
        int selectedIndex = getSelectedIndex(facesContext, uiTabbedPane);

        boolean hasParentForm = hasParentForm(uiTabbedPane);

        if (!hasParentForm)
        {
            writeFormStart(writer, facesContext, uiTabbedPane);
        }

        writeTableStart(writer, facesContext, uiTabbedPane);

        //Tab headers
        writer.write("\n<tr>");
        int tabIdx = 0;
        for (int i = 0, len = uiTabbedPane.getChildCount(); i < len; i++)
        {
            //FIXME
            //UIComponent child = uiTabbedPane.getChild(i);
            UIComponent child = null;
            if (child.getRendererType().equals(TabRenderer.TYPE))
            {
                writeHeaderCell(writer, facesContext, uiTabbedPane, child, tabIdx, tabIdx == selectedIndex);
                tabIdx++;
            }
        }
        writer.write("<td style=\"");
        writer.write(EMPTY_HEADER_CELL_STYLE);
        writer.write("\">&nbsp;</td>");
        writer.write("</tr>");

        //Sub header cells
        writer.write("\n<tr>");
        writeSubHeaderCells(writer,  facesContext, uiTabbedPane, tabCount, selectedIndex);
        writer.write("</tr>");

        //Tab
        writer.write("\n<tr>");
        writeTabCell(writer,  facesContext, uiTabbedPane, tabCount, selectedIndex);
        writer.write("</tr>");

        writeTableEnd(writer, facesContext);

        if (!hasParentForm)
        {
            writeFormEnd(writer, facesContext);
        }

    }


    protected int getTabCount(UIComponent uiTabbedPane)
    {
        int cnt = 0;
        for (Iterator it = uiTabbedPane.getChildren().iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child.getRendererType().equals(TabRenderer.TYPE))
            {
                cnt++;
            }
        }
        return cnt;
    }

    protected int getSelectedIndex(FacesContext facesContext, UIComponent uiTabbedPane)
    {
        Integer intObj;
        if (facesContext.getMaximumSeverity().getOrdinal() >= FacesMessage.SEVERITY_INFO.getOrdinal())
        {
            intObj = (Integer)uiTabbedPane.getAttributes().get(OLD_SELECTED_INDEX_ATTR);
            uiTabbedPane.getAttributes().put(SELECTED_INDEX_ATTR, intObj);
        }
        else
        {
            intObj = (Integer)uiTabbedPane.getAttributes().get(SELECTED_INDEX_ATTR);
        }
        return intObj == null ? 0 : intObj.intValue();
    }


    protected boolean hasParentForm(UIComponent uiTabbedPane)
    {
        UIComponent parent = uiTabbedPane.getParent();
        while (parent != null && !(parent instanceof UIForm))
        {
            parent = parent.getParent();
        }
        return (parent != null);
    }

    protected String getBgColor(UIComponent uiTabbedPane)
    {
        String bgColor = (String)uiTabbedPane.getAttributes().get(HTML.BGCOLOR_ATTR);
        return bgColor == null ? DEFAULT_BG_COLOR : bgColor;
    }


    protected void writeFormStart(Writer writer,
                                  FacesContext facesContext,
                                  UIComponent uiTabbedPane)
        throws IOException
    {
        writer.write("<form method=\"post\" action=\"");
        writer.write(getFormActionStr(facesContext));
        writer.write("\" style=\"display:inline;\" name=\"");
        writer.write(uiTabbedPane.getClientId(facesContext));
        writer.write(".AutoForm\">");
    }


    protected void writeFormEnd(Writer writer,
                                FacesContext facesContext)
        throws IOException
    {
        //Encode state as hidden parameters
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        //FIXME
        //RenderKit renderKit = rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
        RenderKit renderKit = null;
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        UIComponent dummy = new UIForm();
        dummy.setRendererType(FormRenderer.TYPE);
        renderer.encodeChildren(facesContext, dummy);

        writer.write("</form>");
    }


    protected String getFormActionStr(FacesContext facesContext)
    {
        HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();

        ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();
        //FIXME
        /*
        ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
        ServletMapping sm = smf.getServletMapping(servletContext);
        String treeURL = sm.encodeTreeIdForURL(facesContext, facesContext.getTree().getTreeId());

        String action = request.getContextPath() + treeURL;

        //Encode URL
        action = facesContext.getExternalContext().encodeURL(action);

        return action;
        */
        throw new UnsupportedOperationException("fixme");
    }


    protected void writeTableStart(ResponseWriter writer,
                                   FacesContext facesContext,
                                   UIComponent uiComponent)
        throws IOException
    {
        String oldStyle = (String)uiComponent.getAttributes().get(HTML.STYLE_ATTR);
        if (oldStyle == null)
        {
            uiComponent.getAttributes().put(HTML.STYLE_ATTR, TABLE_STYLE);
        }
        else
        {
            uiComponent.getAttributes().put(HTML.STYLE_ATTR, TABLE_STYLE + "; " + oldStyle);
        }

        String oldBgColor = (String)uiComponent.getAttributes().get(HTML.BGCOLOR_ATTR);
        uiComponent.getAttributes().put(HTML.BGCOLOR_ATTR, null);

        writer.write("<table cellspacing=\"0\"");
        HTMLUtil.renderStyleClass(writer, uiComponent);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.TABLE_ATTRIBUTES);
        writer.write(">");

        uiComponent.getAttributes().put(HTML.STYLE_ATTR, oldStyle);
        uiComponent.getAttributes().put(HTML.BGCOLOR_ATTR, oldBgColor);
    }


    protected void writeTableEnd(ResponseWriter writer,
                                 FacesContext facesContext)
        throws IOException
    {
        writer.write("</table>");
    }


    protected void writeHeaderCell(ResponseWriter writer,
                                   FacesContext facesContext,
                                   UIComponent tabbedPane,
                                   UIComponent tab,
                                   int tabIndex,
                                   boolean active)
        throws IOException
    {
        writer.write("\n\t<td style=\"");
        if (active)
        {
            writer.write(ACTIVE_HEADER_CELL_STYLE);
            writer.write("background-color: ");
            writer.write(getBgColor(tabbedPane));
            writer.write("; ");
        }
        else
        {
            writer.write(INACTIVE_HEADER_CELL_STYLE);
        }
        writer.write("\">");

        //Button
        writer.write("<input type=\"submit\" name=\"");
        writer.write(tabbedPane.getClientId(facesContext) + "." + tabIndex);
        writer.write("\"");
        writer.write(" value=\"");

        String label;
        String key = (String)tab.getAttributes().get(JSFAttr.KEY_ATTR);
        if (key != null)
        {
            label = BundleUtils.getString(facesContext,
                                          (String)tab.getAttributes().get(JSFAttr.BUNDLE_ATTR),
                                          key);
        }
        else
        {
            label = (String)tab.getAttributes().get(JSFAttr.LABEL_ATTR);
        }
        writer.write(HTMLEncoder.encode(label, false, false));
        writer.write("\"");

        String style;
        if (active)
        {
            style = BUTTON_STYLE_ACTIVE + "background-color:" + getBgColor(tabbedPane) + "; ";
        }
        else
        {
            style = BUTTON_STYLE_INACTIVE;
        }

        String oldStyle = (String)tab.getAttributes().get(HTML.STYLE_ATTR);
        if (oldStyle == null)
        {
            tab.getAttributes().put(HTML.STYLE_ATTR, style);
        }
        else
        {
            tab.getAttributes().put(HTML.STYLE_ATTR, active ? style + oldStyle : style + oldStyle);
        }

        HTMLUtil.renderStyleClass(writer, tab);//TODO: ?
        HTMLUtil.renderHTMLAttributes(writer, tab, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, tab, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, tab, HTML.BUTTON_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(facesContext, tab);
        writer.write(">");

        tab.getAttributes().put(HTML.STYLE_ATTR, oldStyle);

        writer.write("</td>");
    }


    protected void writeSubHeaderCells(ResponseWriter writer,
                                       FacesContext facesContext,
                                       UIComponent tabbedPane,
                                       int tabCount,
                                       int selectedIndex)
         throws IOException
    {
         for (int i = 0, cnt = tabCount + 1; i < cnt; i++)
         {
             writer.write("\n\t<td style=\"");
             writer.write(SUB_HEADER_CELL_STYLE);
             writer.write("border-top:");   writer.write(i == selectedIndex ? NO_BORDER_STYLE : BORDER_STYLE);
             writer.write("border-right:"); writer.write(i + 1 < cnt ? NO_BORDER_STYLE : BORDER_STYLE);
             writer.write("border-left:");  writer.write(i > 0 ? NO_BORDER_STYLE : BORDER_STYLE);
             writer.write("background-color:");writer.write(getBgColor(tabbedPane));writer.write(";");
             writer.write("\">&nbsp;</td>");
         }
    }


    protected void writeTabCell(ResponseWriter writer,
                                FacesContext facesContext,
                                UIComponent tabbedPane,
                                int tabCount,
                                int selectedIndex)
        throws IOException
    {
        writer.write("<td colspan=\"");
        writer.write(Integer.toString(tabCount + 1));
        writer.write("\" style=\"");
        writer.write(TAB_CELL_STYLE);
        writer.write("background-color:");writer.write(getBgColor(tabbedPane));writer.write(";");
        writer.write("\">");

        BodyContent bodyContent = getBodyContent(facesContext, tabbedPane);
        if (bodyContent == null)
        {
            throw new IllegalStateException("No BodyContent!?");
        }
        String bodyStr = bodyContent.getString();

        int firstStartIdx = bodyStr.indexOf(TAB_START_TOKEN);
        if (firstStartIdx == -1)
        {
            throw new IllegalStateException("Tab start token #0 not found!");
        }

        int startIdx = firstStartIdx;
        for (int i = 0; i < selectedIndex; i++)
        {
            startIdx = bodyStr.indexOf(TAB_START_TOKEN, startIdx + 1);
            if (startIdx == -1)
            {
                throw new IllegalStateException("Tab start token #" + i + " not found!");
            }
        }

        int endIdx = bodyStr.indexOf(TAB_END_TOKEN, startIdx);
        if (endIdx == -1)
        {
            throw new IllegalStateException("Tab end token not found!");
        }

        int lastEndIdx = bodyStr.lastIndexOf(TAB_END_TOKEN);
        if (lastEndIdx == -1)
        {
            throw new IllegalStateException("Last tab end token not found!");
        }

        writer.write(bodyStr.substring(0, firstStartIdx));
        writer.write(bodyStr.substring(startIdx + TAB_START_TOKEN.length(), endIdx));
        writer.write(bodyStr.substring(lastEndIdx + TAB_END_TOKEN.length()));

        writer.write("</td>");
    }





    public void decode(FacesContext facesContext, UIComponent uiTabbedPane)
    {
        //super.decode must not be called, because tabbed pane has no value

        ServletRequest servletRequest = (ServletRequest)facesContext.getExternalContext().getRequest();

        int tabIdx = 0;
        for (Iterator it = uiTabbedPane.getChildren().iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child.getRendererType().equals(TabRenderer.TYPE))
            {
                String paramName = uiTabbedPane.getClientId(facesContext) + "." + tabIdx;
                String paramValue = servletRequest.getParameter(paramName);
                if (paramValue != null && paramValue.length() > 0)
                {
                    uiTabbedPane.getAttributes().put(OLD_SELECTED_INDEX_ATTR,
                                              uiTabbedPane.getAttributes().get(SELECTED_INDEX_ATTR));
                    uiTabbedPane.getAttributes().put(SELECTED_INDEX_ATTR, new Integer(tabIdx));
                    //FIXME
                    //((UICommand)child).fireActionEvent(facesContext);
                    return;
                }
                tabIdx++;
            }
        }
    }


}

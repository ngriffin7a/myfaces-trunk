/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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

import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.ListRendererAttributes;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class AbstractPanelRenderer
        extends HTMLRenderer
        implements CommonRendererAttributes
{
    private static final String ACTUAL_ROW_ATTR = AbstractPanelRenderer.class.getName() + ".actrow";
    private static final String ACTUAL_COLUMN_ATTR = AbstractPanelRenderer.class.getName() + ".actcol";
    private static final String RENDERKIT_ATTR = AbstractPanelRenderer.class.getName() + ".renderkit";
    private static final String COMPONENT_COUNT_ATTR = AbstractPanelRenderer.class.getName() + ".compcount";


    //-------------------------------------------------------------
    // write methods
    //-------------------------------------------------------------

    protected void openNewRow(FacesContext context)
        throws IOException
    {
        UIComponent listComponent = peekListComponent(context);

        ResponseWriter writer = context.getResponseWriter();
        boolean isLastChildcomponent =
            listComponent.getChildCount() == getComponentCountAttr(context) ? true : false;

        String style = calcRowStyle(context,
                                    isLastChildcomponent);

        writer.write("<tr");
        if (style != null && style.length() > 0)
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write("\"");
        }
        writer.write(">");

        incrementRowAttr(context);
    }

    protected void openNewColumn(FacesContext context)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        String style = calcColumnStyle(context);
        writer.write("<td");

        if (style != null && style.length() > 0)
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write("\"");
        }
        writer.write(">");

        incrementColumnAttr(context);
    }

    //-------------------------------------------------------------
    // store / restore RenderKit
    //-------------------------------------------------------------

    protected void storeRenderKit(FacesContext context, UIComponent uiComponent)
    {

        UIComponent listComponent = peekListComponent(context);
        String renderKitId = context.getTree().getRenderKitId();

        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(renderKitId, context);
        RenderKit listRenderKit;
        try
        {
            listRenderKit = renderkitFactory.getRenderKit(ListRenderer.ListRenderKitImpl.ID);
        }
        catch (Exception e)
        {
            listRenderKit = new ListRenderer.ListRenderKitImpl(renderKit);
            renderkitFactory.addRenderKit(ListRenderer.ListRenderKitImpl.ID, listRenderKit);
        }

        context.getTree().setRenderKitId(ListRenderer.ListRenderKitImpl.ID);

        listComponent.setAttribute(RENDERKIT_ATTR, renderKitId);
    }

    protected void restoreRenderKit(FacesContext context, UIComponent uicomponent)
    {
        String renderKitId = getOriginalRenderKitId(context, uicomponent);
        context.getTree().setRenderKitId(renderKitId);
    }

    private String getOriginalRenderKitId(FacesContext context, UIComponent uicomponent)
    {
        if (uicomponent.getRendererType().equals(ListRenderer.TYPE))
        {
            return (String)uicomponent.getAttribute(RENDERKIT_ATTR);
        }
        else
        {
            UIComponent listComponent = peekListComponent(context);
            return (String)listComponent.getAttribute(RENDERKIT_ATTR);
        }
    }

    protected void encodeBeginWithOriginalRenderer(FacesContext context, UIComponent uicomponent)
        throws IOException
    {
        restoreRenderKit(context, uicomponent);

        String renderKitId = context.getTree().getRenderKitId();
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(renderKitId, context);
        Renderer renderer = renderKit.getRenderer(uicomponent.getRendererType());
        renderer.encodeBegin(context, uicomponent);

        storeRenderKit(context, uicomponent);
    }

    protected void encodeEndWithOriginalRenderer(FacesContext context, UIComponent uicomponent)
        throws IOException
    {
        restoreRenderKit(context, uicomponent);

        String renderKitId = context.getTree().getRenderKitId();
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(renderKitId, context);
        Renderer renderer = renderKit.getRenderer(uicomponent.getRendererType());
        renderer.encodeEnd(context, uicomponent);

        storeRenderKit(context, uicomponent);
    }

    //-------------------------------------------------------------
    // count visited childs of a ListCompoent
    //-------------------------------------------------------------

    protected void incrementComponentCountAttr(FacesContext context)
    {
        UIComponent listComponent = peekListComponent(context);
        listComponent.setAttribute(COMPONENT_COUNT_ATTR, new Integer(getComponentCountAttr(context) + 1));
    }

    private int getComponentCountAttr(FacesContext context)
    {
        UIComponent listComponent = peekListComponent(context);
        Integer i = (Integer)listComponent.getAttribute(COMPONENT_COUNT_ATTR);
        return i == null ? 0 : i.intValue();
    }

    //-------------------------------------------------------------
    // stack for ListComponents
    //-------------------------------------------------------------

    protected void pushListComponent(FacesContext context, UIComponent listComponent)
    {
        getListComponentStack(context).push(listComponent);
    }

    protected void popListComponent(FacesContext context)
    {
        getListComponentStack(context).pop();
    }

    protected UIComponent peekListComponent(FacesContext context)
    {
        return (UIComponent)getListComponentStack(context).peek();
    }

    public static final String LIST_STACK_ATTR = ListRenderer.class.getName() + ".liststack";
    private Stack getListComponentStack(FacesContext context)
    {
        ServletRequest request = context.getServletRequest();
        Stack stack = (Stack)request.getAttribute(LIST_STACK_ATTR);
        if (stack == null)
        {
            stack = new Stack();
            request.setAttribute(LIST_STACK_ATTR, stack);
        }
        return stack;
    }

    //-------------------------------------------------------------
    // count rows and Columns
    //-------------------------------------------------------------

    private static final Integer INITIAL_VALUE = new Integer(0);

    protected int incrementColumnAttr(FacesContext context)
    {
        Integer value = getActualColumnAttr(context);
        context.getServletRequest().setAttribute(ACTUAL_COLUMN_ATTR, new Integer(value.intValue() + 1));
        return value.intValue();
    }

    protected void resetColumnAttr(FacesContext context)
    {
        context.getServletRequest().setAttribute(ACTUAL_COLUMN_ATTR, INITIAL_VALUE);
    }

    protected Integer getActualColumnAttr(FacesContext context)
    {
        ServletRequest request = context.getServletRequest();
        Integer value = (Integer)request.getAttribute(ACTUAL_COLUMN_ATTR);
        return value == null ? INITIAL_VALUE : new Integer(value.intValue());
    }

    protected int incrementRowAttr(FacesContext context)
    {
        Integer value = getActualRowAttr(context);
        context.getServletRequest().setAttribute(ACTUAL_ROW_ATTR, new Integer(value.intValue() + 1));
        return value.intValue();
    }

    protected Integer getActualRowAttr(FacesContext context)
    {
        ServletRequest request = context.getServletRequest();
        Integer value = (Integer)request.getAttribute(ACTUAL_ROW_ATTR);
        return value == null ? INITIAL_VALUE : new Integer(value.intValue());
    }

    //-------------------------------------------------------------
    // Styles
    //-------------------------------------------------------------

    private String calcRowStyle(FacesContext context,
                                boolean isLastRow)
    {
        String style = null;
        int row = getActualRowAttr(context).intValue();

        Styles styles = getStyles(context);
        if (styles == null)
        {
            return null;
        }

        boolean hasHeaderStyle = styles.getHeaderStyle().length() > 0;
        if (row == 0 && hasHeaderStyle)
        {
            style = styles.getHeaderStyle();
        }
        else if (isLastRow && styles.getFooterStyle().length() > 0)
        {
            style = styles.getFooterStyle();
        }
        else if (styles.getRowStyle().length > 0)
        {
            int ref = hasHeaderStyle ? row - 1 : row;
            int i = (ref % styles.getRowStyle().length);
            style = styles.getRowStyle()[i];
        }
        return style;
    }

    private String calcColumnStyle(FacesContext context)
    {
        String style = null;
        int column = getActualColumnAttr(context).intValue();

        Styles styles = getStyles(context);
        if (styles == null)
        {
            return null;
        }

        if (styles.getColumnStyle().length > 0)
        {
            int i = (column % styles.getColumnStyle().length);
            style = styles.getColumnStyle()[i];
        }
        return style;
    }

    private Styles getStyles(FacesContext context)
    {
        UIComponent listComponent = peekListComponent(context);
        if (listComponent.getComponentType().equals(javax.faces.component.UIPanel.TYPE))
        {
            if (!listComponent.getRendererType().equals(ListRenderer.TYPE))
            {
                // Parent should have RenderType "List"
                listComponent = listComponent.getParent();
            }

            String headerStyle = getAttribute(listComponent, ListRendererAttributes.HEADER_CLASS_ATTR);
            String footerStyle = getAttribute(listComponent, ListRendererAttributes.FOOTER_CLASS_ATTR);
            String[] rowStyle = getAttributes(listComponent, ROW_CLASSES_ATTR);
            String[] columnStyle = getAttributes(listComponent, COLUMN_CLASSES_ATTR);

            return new Styles(headerStyle, rowStyle, columnStyle, footerStyle);
        }
        return null;
    }

    private String[] getAttributes(UIComponent parentComponent, String attribute)
    {
        String[] attr = null;
        String rowClasses = (String)parentComponent.getAttribute(attribute);
        if (rowClasses != null && rowClasses.length() > 0)
        {
            StringTokenizer tokenizer = new StringTokenizer(rowClasses, ",");

            attr = new String[tokenizer.countTokens()];
            int i = 0;
            while (tokenizer.hasMoreTokens())
            {
                attr[i] = tokenizer.nextToken().trim();
                i++;
            }
        }

        if (attr == null)
        {
            attr = new String[0];
        }

        return attr;
    }

    private String getAttribute(UIComponent parentComponent, String attribute)
    {
        String attr = (String)parentComponent.getAttribute(attribute);
        if (attr == null)
        {
            attr = new String("");
        }
        return attr;
    }

    private static class Styles
    {
        private String _headerStyle;
        private String _footerStyle;
        private String[] _rowStyle;
        private String[] _columnStyle;

        Styles(String headerStyle,
               String[] rowStyle,
               String[] columnStyle,
               String footerStyle)
        {
            _headerStyle = headerStyle;
            _footerStyle = footerStyle;
            _rowStyle = rowStyle;
            _columnStyle = columnStyle;
        }

        public String getHeaderStyle()
        {
            return _headerStyle;
        }

        public String getFooterStyle()
        {
            return _footerStyle;
        }

        public String[] getRowStyle()
        {
            return _rowStyle;
        }

        public String[] getColumnStyle()
        {
            return _columnStyle;
        }

    }

}

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

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.ListRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLTableAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ListRenderer
    extends HTMLRenderer
    implements CallbackRenderer,
               CommonComponentAttributes,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               HTMLTableAttributes,
               ListRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "List";
    private static final String ITERATOR_ATTR = ListRenderer.class.getName() + ".iterator";
    private static final String ACTUAL_ROW_ATTR = ListRenderer.class.getName() + ".actrow";
    private static final String ACTUAL_COLUMN_ATTR = ListRenderer.class.getName() + ".actcol";
    private static final String COMPONENT_COUNT_ATTR = ListRenderer.class.getName() + ".compcount";

    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UIPanel;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(javax.faces.component.UIPanel.TYPE);
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_list", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_list", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_list", HTML_TABLE_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_list", PANEL_LIST_ATTRIBUTES);
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_list", USER_ROLE_ATTRIBUTES);
    }


    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent uiComponent) throws IOException
    {
        UIComponent parent = UIComponentUtils.getParentOrFacetOwner(uiComponent);
        String rendererType = uiComponent.getRendererType();
        String parentRendererType = parent.getRendererType();

        if (rendererType != null && parentRendererType != null)
        {
            if (parentRendererType.equals(ListRenderer.TYPE) &&
                rendererType.equals(DataRenderer.TYPE))
            {
                Object obj = uiComponent.getAttribute(ITERATOR_ATTR);
                if (obj == null)
                {
                    // first call of encodeBegin
                    incrementComponentCountAttr(facesContext);
                }

                Iterator it = getIterator(facesContext, uiComponent);

                if (it.hasNext())
                {
                    // new row
                    closeRow(facesContext);
                    openRow(facesContext, uiComponent.getRendererType());
                }
                else
                {
                    //facesContext.setModelValue(varAttr, null);
                }
            }
            else if (parentRendererType.equals(ListRenderer.TYPE) &&
                     rendererType.equals(GroupRenderer.TYPE))
            {
                incrementComponentCountAttr(facesContext);
                // new row
                closeRow(facesContext);
                openRow(facesContext, uiComponent.getRendererType());
            }
            else
            {
                String parentParentRendererType
                   = UIComponentUtils.getParentOrFacetOwner(parent).getRendererType();
                if ((parentRendererType.equals(DataRenderer.TYPE) ||
                    parentRendererType.equals(GroupRenderer.TYPE)) &&
                    (parentParentRendererType != null &&
                     parentParentRendererType.equals(TYPE)))
                {
                    openColumn(facesContext);
                }
            }
        }
    }

    public void encodeBegin(FacesContext context, UIComponent uicomponent)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();

        if (uicomponent.getRendererType().equals(TYPE))
        {
            pushListComponent(context, uicomponent);
            writer.write("<table");
            String style = (String)uicomponent.getAttribute(PANEL_CLASS_ATTR);
            if (style != null && style.length() > 0)
            {
                writer.write(" class=\"");
                writer.write(style);
                writer.write("\"");
            }
            writer.write(">\n");
        }
        CallbackSupport.addCallbackRenderer(context, uicomponent, this);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uicomponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (uicomponent.getRendererType().equals(TYPE))
        {
            popListComponent(facesContext);

            closeRow(facesContext);
            writer.write("</table>\n");
            //restoreRenderKit(facesContext, uicomponent);
        }
        CallbackSupport.removeCallbackRenderer(facesContext, uicomponent, this);
    }

    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent) throws IOException
    {
        UIComponent parent = UIComponentUtils.getParentOrFacetOwner(uiComponent);

        String rendererType = uiComponent.getRendererType();
        String parentRendererType = parent.getRendererType();

        if (rendererType != null && parentRendererType != null)
        {
            String parentParentRendererType
                = UIComponentUtils.getParentOrFacetOwner(parent).getRendererType();
            if ((parentRendererType.equals(DataRenderer.TYPE) ||
                parentRendererType.equals(GroupRenderer.TYPE)) &&
                (parentParentRendererType != null &&
                 parentParentRendererType.equals(TYPE)))
            {
                closeColumn(facesContext);
                int column = getActualColumnAttr(facesContext).intValue();
                afterCloseColumn(facesContext, column -1);
            }
        }
    }

    protected Iterator getIterator(FacesContext facesContext, UIComponent uiComponent)
    {
        Iterator iterator = (Iterator)uiComponent.getAttribute(ITERATOR_ATTR);
        if (iterator == null)
        {
            Object v = uiComponent.currentValue(facesContext);
            if (v instanceof Iterator)
            {
                iterator = (Iterator)v;
            }
            else if (v instanceof Collection)
            {
                iterator = ((Collection)v).iterator();
            }
            else if (v instanceof Object[])
            {
                iterator = Arrays.asList((Object[])v).iterator();
            }
            else if (v instanceof Iterator)
            {
                iterator = (Iterator)v;
            }
            else
            {
                throw new IllegalArgumentException("Value of component " + UIComponentUtils.toString(uiComponent) + " is neither collection nor array.");
            }
            uiComponent.setAttribute(ITERATOR_ATTR, iterator);
        }
        return iterator;
    }

    //-------------------------------------------------------------
    // protected methods
    //-------------------------------------------------------------
    public void afterOpenRow(FacesContext facesContext, int row)
        throws IOException
    {
    }

    public void beforeCloseRow(FacesContext facesContext, int row)
        throws IOException
    {
    }

    public void beforeOpenColumn(FacesContext facesContext, int column)
        throws IOException
    {
    }

    public void afterCloseColumn(FacesContext facesContext, int column)
        throws IOException
    {
    }

    //-------------------------------------------------------------
    // write methods
    //-------------------------------------------------------------

    protected void openRow(FacesContext context, String rendererType)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        boolean isLastChildcomponent =
            rendererType.equals(GroupRenderer.TYPE) &&
            getComponentCountAttr(context) > 1 ? true : false;

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

        int row = getActualRowAttr(context).intValue();
        afterOpenRow(context, row);

        incrementRowAttr(context);
    }

    protected void closeRow(FacesContext context)
        throws IOException
    {
       int row = getActualRowAttr(context).intValue();
       if (row > 0)
       {
           beforeCloseRow(context, row);
           context.getResponseWriter().write("</tr>");
       }
    }

    protected void openColumn(FacesContext context)
        throws IOException
    {
        int column = getActualColumnAttr(context).intValue();
        beforeOpenColumn(context, column);

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

    protected void closeColumn(FacesContext context)
        throws IOException
    {
        context.getResponseWriter().write("</td>");
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

    /**
     * TODO: refactor see GridRenderer
     */
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

    /**
     * TODO: refactor see GridRenderer
     */
    private String calcColumnStyle(FacesContext context)
    {
        String style = null;
        int column = getActualColumnAttr(context).intValue();

        Styles styles = getStyles(context);

        boolean hasHeaderStyle = styles.getHeaderStyle().length() > 0;
        if (styles == null || hasHeaderStyle)
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
                listComponent = UIComponentUtils.getParentOrFacetOwner(listComponent);
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



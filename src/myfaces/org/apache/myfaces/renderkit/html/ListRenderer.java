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

import net.sourceforge.myfaces.component.CommonComponentProperties;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.ListRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ListRenderer
    extends HTMLRenderer
    implements CallbackRenderer,
               CommonComponentProperties,
               CommonRendererAttributes,
               ListRendererAttributes,
               UserRoleAttributes
{
    public static final String TYPE = "List";
    private static final String ACTUAL_ROW_ATTR = ListRenderer.class.getName() + ".actrow";
    private static final String ACTUAL_COLUMN_ATTR = ListRenderer.class.getName() + ".actcol";
    private static final String COMPONENT_COUNT_ATTR = ListRenderer.class.getName() + ".compcount";

    public String getRendererType()
    {
        return TYPE;
    }

    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent uiComponent) throws IOException
    {
        UIComponent parent = uiComponent.getParent();
        String rendererType = uiComponent.getRendererType();
        String parentRendererType = parent.getRendererType();

        if (rendererType != null && parentRendererType != null)
        {
            if (parentRendererType.equals(ListRenderer.TYPE) &&
                rendererType.equals(DataRenderer.TYPE))
            {
                Iterator it = DataRenderer.getIterator(facesContext, (UIPanel)uiComponent);
                if (it == null)
                {
                    // first call of encodeBegin
                    incrementComponentCountAttr(facesContext);
                }
                else if (it.hasNext())
                {
                    // new row
                    closeRow(facesContext, parent, uiComponent);
                    openRow(facesContext, parent, uiComponent);
                }
            }
            else if (parentRendererType.equals(ListRenderer.TYPE) &&
                     rendererType.equals(GroupRenderer.TYPE))
            {
                incrementComponentCountAttr(facesContext);
                // new row
                closeRow(facesContext, parent, uiComponent);
                openRow(facesContext, parent, uiComponent);
            }
            else
            {
                String parentParentRendererType
                   = parent.getParent().getRendererType();
                if ((parentRendererType.equals(DataRenderer.TYPE) ||
                    parentRendererType.equals(GroupRenderer.TYPE)) &&
                    (parentParentRendererType != null &&
                     parentParentRendererType.equals(TYPE)))
                {
                    incrementColumnAttr(facesContext);
                    openColumn(facesContext, uiComponent);
                }
            }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (uiComponent.getRendererType().equals(TYPE))
        {
            pushListComponent(facesContext, uiComponent);
            writer.write("<table");
            HTMLUtil.renderCssClass(writer, uiComponent, PANEL_CLASS_ATTR);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
            HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.TABLE_ATTRIBUTES);
            HTMLUtil.renderDisabledOnUserRole(facesContext, uiComponent);
            writer.write(">\n");
        }
        CallbackSupport.addCallbackRenderer(facesContext, uiComponent, this);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uicomponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (uicomponent.getRendererType().equals(TYPE))
        {
            UIComponent listComponent  = peekListComponent(facesContext);

            closeRow(facesContext, listComponent, uicomponent);
            popListComponent(facesContext);
            writer.write("</table>\n");
        }
        CallbackSupport.removeCallbackRenderer(facesContext, uicomponent, this);
    }

    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent) throws IOException
    {
        UIComponent parent = uiComponent.getParent();

        String rendererType = uiComponent.getRendererType();
        String parentRendererType = parent.getRendererType();

        if (rendererType != null && parentRendererType != null)
        {
            String parentParentRendererType
                = parent.getParent().getRendererType();
            if ((parentRendererType.equals(DataRenderer.TYPE) ||
                parentRendererType.equals(GroupRenderer.TYPE)) &&
                (parentParentRendererType != null &&
                 parentParentRendererType.equals(TYPE)))
            {
                closeColumn(facesContext, uiComponent);
                int column = getCurrentColumnAttr(facesContext).intValue();
                afterCloseColumn(facesContext, column - 1);
            }
        }
    }

    //-------------------------------------------------------------
    // protected methods
    //-------------------------------------------------------------
    protected void afterOpenRow(FacesContext facesContext, int row)
        throws IOException
    {
    }

    protected void beforeCloseRow(FacesContext facesContext, int row)
        throws IOException
    {
    }

    protected void beforeOpenColumn(FacesContext facesContext, int column)
        throws IOException
    {
    }

    protected void afterCloseColumn(FacesContext facesContext, int column)
        throws IOException
    {
    }

    //-------------------------------------------------------------
    // write methods
    //-------------------------------------------------------------

    protected void openRow(FacesContext context,
                           UIComponent listComponent,
                           UIComponent rowComponent)
        throws IOException
    {
        String rendererType = rowComponent.getRendererType();
        ResponseWriter writer = context.getResponseWriter();
        boolean isLastChildcomponent =
            rendererType.equals(GroupRenderer.TYPE) &&
            getComponentCountAttr(context) > 1 ? true : false;

        int row = incrementRowAttr(context);

        String style = calcRowStyle(context,
                                    rowComponent,
                                    row,
                                    isLastChildcomponent);

        writer.write("<tr");
        if (style != null && style.length() > 0)
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write("\"");
        }
        writer.write(">");


        afterOpenRow(context, row);

    }

    protected void closeRow(FacesContext context,
                            UIComponent listComponent,
                            UIComponent rowComponent)
        throws IOException
    {
        int row = getCurrentRowAttr(context).intValue();
        if (row >= 0)
        {
            beforeCloseRow(context, row);
            context.getResponseWriter().write("</tr>");
        }
    }

    protected void openColumn(FacesContext facesContext,
                              UIComponent uiComponent)
        throws IOException
    {
        int row = getCurrentRowAttr(facesContext).intValue();
        int column = getCurrentColumnAttr(facesContext).intValue();

        beforeOpenColumn(facesContext, column);

        ResponseWriter writer = facesContext.getResponseWriter();

        String style = calcColumnStyle(facesContext, uiComponent, column, row);
        writer.write("<td");

        if (style != null && style.length() > 0)
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write("\"");
        }
        writer.write(">");
    }

    protected void closeColumn(FacesContext context,
                               UIComponent uiComponent)
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

    protected UIComponent popListComponent(FacesContext context)
    {
        return (UIComponent)getListComponentStack(context).pop();
    }

    protected UIComponent peekListComponent(FacesContext context)
    {
        return (UIComponent)getListComponentStack(context).peek();
    }

    public static final String LIST_STACK_ATTR = ListRenderer.class.getName() + ".liststack";
    private Stack getListComponentStack(FacesContext context)
    {
        ServletRequest servletRequest = (ServletRequest)context.getExternalContext().getRequest();
        Stack stack = (Stack)servletRequest.getAttribute(LIST_STACK_ATTR);
        if (stack == null)
        {
            stack = new Stack();
            servletRequest.setAttribute(LIST_STACK_ATTR, stack);
        }
        return stack;
    }

    //-------------------------------------------------------------
    // count rows and Columns
    //-------------------------------------------------------------

    private static final Integer INITIAL_VALUE = new Integer(-1);

    protected void incrementColumnAttr(FacesContext context)
    {
        Integer value = getCurrentColumnAttr(context);
        peekListComponent(context).setAttribute(ACTUAL_COLUMN_ATTR, new Integer(value.intValue() + 1));
    }

    protected Integer getCurrentColumnAttr(FacesContext context)
    {
        Integer value = (Integer)peekListComponent(context).getAttribute(ACTUAL_COLUMN_ATTR);
        return value == null ? INITIAL_VALUE : new Integer(value.intValue());
    }

    protected int incrementRowAttr(FacesContext context)
    {
        Integer value = getCurrentRowAttr(context);
        peekListComponent(context).setAttribute(ACTUAL_ROW_ATTR, new Integer(value.intValue() + 1));
        return value.intValue() + 1;
    }

    protected Integer getCurrentRowAttr(FacesContext context)
    {
        if (peekListComponent(context) == null)
        {
            System.out.println("-->");
        }
        Integer value = (Integer)peekListComponent(context).getAttribute(ACTUAL_ROW_ATTR);
        return value == null ? INITIAL_VALUE : new Integer(value.intValue());
    }

    //-------------------------------------------------------------
    // Styles
    //-------------------------------------------------------------
    protected String calcRowStyle(FacesContext context,
                                  UIComponent rowComponent,
                                  int row,
                                  boolean isLastRow)
    {
        String style = null;

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

    protected String calcColumnStyle(FacesContext context,
                                     UIComponent columnComponent,
                                     int column,
                                     int row)
    {
        String style = null;

        Styles styles = getStyles(context);
        if (styles == null)
        {
            return null;
        }

        boolean hasHeaderStyle = styles.getHeaderStyle() != null &&
                                 styles.getHeaderStyle().length() > 0;
        if (row == 0 && hasHeaderStyle)
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
        if (listComponent instanceof UIPanel)
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



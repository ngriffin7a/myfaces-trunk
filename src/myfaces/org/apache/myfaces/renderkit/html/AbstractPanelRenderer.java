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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.FactoryFinder;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
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
    private static final String COMPONENT_INFO_ATTR = AbstractPanelRenderer.class.getName() + ".components";
    private static final String ITERATOR_ATTR = AbstractPanelRenderer.class.getName() + ".iterator";
    public static final String RENDERKIT_ATTR = ListRenderer.class.getName() + ".renderkit";

    private Styles getStyles(UIComponent component)
    {
        if (component.getComponentType().equals(javax.faces.component.UIPanel.TYPE))
        {
            if (!component.getRendererType().equals(ListRenderer.TYPE))
            {
                // Parent should have RenderType "List"
                component = component.getParent();
            }

            String headerStyle = getAttribute(component, ListRendererAttributes.HEADER_CLASS_ATTR);
            String footerStyle = getAttribute(component, ListRendererAttributes.FOOTER_CLASS_ATTR);
            String[] rowStyle = getAttributes(component, ROW_CLASSES_ATTR);
            String[] columnStyle = getAttributes(component, COLUMN_CLASSES_ATTR);

            return new Styles(headerStyle, rowStyle, columnStyle, footerStyle);
        }
        return null;
    }

    private String calcStyle(FacesContext context,
                             UIComponent listComponent,
                             boolean isLastColumn)
    {
        String style = null;
        int row = getActualRow(context).intValue();
        // TODO: implement Column
        int column = 0;

        Styles styles = getStyles(listComponent);
        if (styles == null)
        {
            return null;
        }

        if (row == 0 && styles.getHeaderStyle().length() > 0)
        {
            style = styles.getHeaderStyle();
        }
        else if (isLastColumn && styles.getFooterStyle().length() > 0)
        {
            style = styles.getFooterStyle();
        }
        else if (styles.getRowStyle().length > 0)
        {
            int i = (row % styles.getRowStyle().length);
            style = styles.getRowStyle()[i];
        }
        else if (styles.getColumnStyle().length > 0)
        {
            int i = (column % styles.getColumnStyle().length);
            style = styles.getColumnStyle()[i];
        }
        return style;
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
                throw new IllegalArgumentException("Value of component " + uiComponent.getCompoundId() + " is neither collection nor array.");
            }
            uiComponent.setAttribute(ITERATOR_ATTR, iterator);
        }
        return iterator;
    }

    protected static class Styles
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


    protected void writeNewRow(FacesContext context)
        throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        setToNextRow(context);
        writer.write("<tr>");
    }

    /**
     * uicomponent must have parent with rendererType = List
     * @param context
     * @param uicomponent
     * @throws IOException
     */
    protected void writeColumnStart(FacesContext context,
                                    UIComponent uicomponent)
        throws IOException
    {
        UIComponent[] componentInfo = findComponentInfo(uicomponent);
        if (componentInfo == null)
        {
            throw new IllegalStateException("UIComponent + " + uicomponent + " must have a antecessor component wiht renderType=" + ListRenderer.TYPE);
        }
        UIComponent listComponent = componentInfo[0];
        UIComponent listComponentsChild = componentInfo[1];

        ResponseWriter writer = context.getResponseWriter();

        String style = calcStyle(context,
                                 listComponent,
                                 isChildLastComponent(listComponent, listComponentsChild));
        writer.write("<td");

        if (style != null && style.length() > 0)
        {
            writer.write(" class=\"");
            writer.write(style);
            writer.write("\"");
        }
        writer.write(">");
    }

    private boolean isChildLastComponent(UIComponent parent,
                                         UIComponent child)
    {
        for (Iterator it = parent.getChildren(); it.hasNext(); )
        {
            UIComponent parentsChild = (UIComponent)it.next();
            if (parentsChild.getComponentId().equals(child.getComponentId()))
            {
                return !it.hasNext() ? true : false;
            }
        }
        throw new IllegalStateException("Component " + child.getComponentId() + " not found as child of component " + parent.getCompoundId());
    }

    protected UIComponent findListComponent(UIComponent uiComponent)
    {
        UIComponent[] components = findComponentInfo(uiComponent);
        if (components == null)
        {
            return null;
        }
        return components[0];
    }

    private UIComponent[] findComponentInfo(UIComponent uicomponent)
    {
        UIComponent[] components = (UIComponent[])uicomponent.getAttribute(COMPONENT_INFO_ATTR);
        if (components == null)
        {
            components = findListComponentAndChild(uicomponent);
            if (components != null)
            {
                uicomponent.setAttribute(COMPONENT_INFO_ATTR, components);
            }
        }

        return components;
    }

    private UIComponent[] findListComponentAndChild(UIComponent uicomponent)
    {
        UIComponent listComponent = uicomponent.getParent();
        UIComponent listComponentsChild = uicomponent;
        boolean found = false;
        while (listComponent != null)
        {
            if (listComponent.getRendererType().equals(ListRenderer.TYPE))
            {
                found = true;
                break;
            }
            listComponentsChild = listComponent;
            listComponent = listComponent.getParent();
        }

        if (!found)
        {
            return null;
        }

        UIComponent[] components = {listComponent, listComponentsChild};
        return components;
    }


    protected int setToNextRow(FacesContext context)
    {
        Integer value = getActualRow(context);
        context.getServletRequest().setAttribute(ACTUAL_ROW_ATTR, new Integer(value.intValue() + 1));
        return value.intValue();
    }

    protected Integer getActualRow(FacesContext context)
    {
        ServletRequest request = context.getServletRequest();
        Integer value = (Integer)request.getAttribute(ACTUAL_ROW_ATTR);
        return value == null ? new Integer(-1) : new Integer(value.intValue());
    }

    protected Renderer getOriginalRenderer(FacesContext context, UIComponent component)
    {
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(getOriginalRenderKitId(context, component), context);
        return renderKit.getRenderer(component.getRendererType());
    }

    protected String getOriginalRenderKitId(FacesContext context, UIComponent uicomponent)
    {
        if (uicomponent.getRendererType().equals(ListRenderer.TYPE))
        {
            return (String)uicomponent.getAttribute(RENDERKIT_ATTR);
        }
        else
        {
            UIComponent listComponent = findListComponent(uicomponent);
            return (String)listComponent.getAttribute(RENDERKIT_ATTR);
        }
    }



}

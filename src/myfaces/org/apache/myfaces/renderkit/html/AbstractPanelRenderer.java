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
    protected Styles getStyles(UIComponent component)
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

    protected String calcStyle(Styles styles,
                               int row,
                               int column,
                               boolean lastColumn)
    {
        String style = null;
        if (styles == null)
        {
            return null;
        }

        if (row == 0 && styles.getHeaderStyle().length() > 0)
        {
            style = styles.getHeaderStyle();
        }
        else if (lastColumn && styles.getFooterStyle().length() > 0)
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
        Iterator iterator = (Iterator)uiComponent.getAttribute(ListRenderer.ITERATOR_ATTR);
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
            uiComponent.setAttribute(ListRenderer.ITERATOR_ATTR, iterator);
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

}

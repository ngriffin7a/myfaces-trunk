/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler & Thomas Spiegl
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

import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.component.UIPanel;

import javax.faces.component.UIComponent;
import java.util.StringTokenizer;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class AbstractPanelRenderer
        extends HTMLRenderer
{
    protected Styles getStyles(UIComponent component)
    {
        if (component.getComponentType().equals(javax.faces.component.UIPanel.TYPE))
        {
            String headerStyle = getAttribute(component, UIPanel.HEADER_CLASS_ATTR);
            String footerStyle = getAttribute(component, UIPanel.FOOTER_CLASS_ATTR);
            String[] rowStyle = getAttributes(component, UIPanel.ROW_CLASSES_ATTR);
            String[] columnStyle = getAttributes(component, UIPanel.COLUMN_CLASSES_ATTR);

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

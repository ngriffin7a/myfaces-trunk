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
package net.sourceforge.myfaces.taglib.html;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataTableTag
        extends HtmlComponentBodyTag
{
    //private static final Log log = LogFactory.getLog(HtmlDataTableTag.class);

    protected String getDefaultRendererType()
    {
        return "Table";
    }

    public String getComponentType()
    {
        return "DataTable";
    }

    // UIComponent attributes --> already implemented in MyfacesComponentTag

    // user role attributes --> already implemented in MyfacesComponentTag

    // HTML universal attributes --> already implemented in HtmlComponentTag

    // HTML event handler attributes --> already implemented in HtmlComponentTag

    // HTML table attributes relevant for Table
    private String _align;
    private String _border;
    private String _bgcolor;
    private String _cellpadding;
    private String _cellspacing;
    private String _datafld;
    private String _datasrc;
    private String _dataformatas;
    private String _frame;
    private String _rules;
    private String _summary;
    private String _width;

    // UIPanel attributes
    // value and converterId --> already implemented in MyfacesComponentTag

    // HtmlPanelGrid attributes
    private String _columnClasses;
    private String _columns;
    private String _footerClass;
    private String _headerClass;
    private String _rowClasses;
    private String _rows;
    private String _var;


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HTML.ALIGN_ATTR, _align);
        setIntegerProperty(component, HTML.BORDER_ATTR, _border);
        setStringProperty(component, HTML.BGCOLOR_ATTR, _bgcolor);
        setStringProperty(component, HTML.CELLPADDING_ATTR, _cellpadding);
        setStringProperty(component, HTML.CELLSPACING_ATTR, _cellspacing);
        setStringProperty(component, HTML.DATAFLD_ATTR, _datafld);
        setStringProperty(component, HTML.DATASRC_ATTR, _datasrc);
        setStringProperty(component, HTML.DATAFORMATAS_ATTR, _dataformatas);
        setStringProperty(component, HTML.FRAME_ATTR, _frame);
        setStringProperty(component, HTML.RULES_ATTR, _rules);
        setStringProperty(component, HTML.SUMMARY_ATTR, _summary);
        setStringProperty(component, HTML.WIDTH_ATTR, _width);

        setStringProperty(component, JSFAttr.COLUMN_CLASSES_ATTR, _columnClasses);
        setIntegerProperty(component, JSFAttr.COLUMNS_ATTR, _columns);
        setStringProperty(component, JSFAttr.FOOTER_CLASS_ATTR, _footerClass);
        setStringProperty(component, JSFAttr.HEADER_CLASS_ATTR, _headerClass);
        setStringProperty(component, JSFAttr.ROW_CLASSES_ATTR, _rowClasses);

        setIntegerProperty(component, JSFAttr.ROWS_ATTR, _rows);

        setStringProperty(component, JSFAttr.VAR_ATTR, _var);
    }


    public void setAlign(String align)
    {
        _align = align;
    }

    public void setBorder(String border)
    {
        _border = border;
    }

    public void setBgcolor(String bgcolor)
    {
        _bgcolor = bgcolor;
    }

    public void setCellpadding(String cellpadding)
    {
        _cellpadding = cellpadding;
    }

    public void setCellspacing(String cellspacing)
    {
        _cellspacing = cellspacing;
    }

    public void setDatafld(String datafld)
    {
        _datafld = datafld;
    }

    public void setDatasrc(String datasrc)
    {
        _datasrc = datasrc;
    }

    public void setDataformatas(String dataformatas)
    {
        _dataformatas = dataformatas;
    }

    public void setFrame(String frame)
    {
        _frame = frame;
    }

    public void setRules(String rules)
    {
        _rules = rules;
    }

    public void setSummary(String summary)
    {
        _summary = summary;
    }

    public void setWidth(String width)
    {
        _width = width;
    }

    public void setColumnClasses(String columnClasses)
    {
        _columnClasses = columnClasses;
    }

    public void setColumns(String columns)
    {
        _columns = columns;
    }

    public void setFooterClass(String footerClass)
    {
        _footerClass = footerClass;
    }

    public void setHeaderClass(String headerClass)
    {
        _headerClass = headerClass;
    }

    public void setRowClasses(String rowClasses)
    {
        _rowClasses = rowClasses;
    }

    public void setRows(String rows)
    {
        _rows = rows;
    }

    public void setVar(String var)
    {
        _var = var;
    }
}

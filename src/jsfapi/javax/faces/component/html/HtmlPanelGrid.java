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

package javax.faces.component.html;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlPanelGrid extends UIPanel
{
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.HtmlPanelGrid";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Grid";
    private static final int DEFAULT_BORDER = -2147483648;
    private static final int DEFAULT_COLUMNS = -2147483648;

    private String _bgcolor = null;
    private Integer _border = null;
    private String _cellpadding = null;
    private String _cellspacing = null;
    private String _columnClasses = null;
    private Integer _columns = null;
    private String _dir = null;
    private String _footerClass = null;
    private String _frame = null;
    private String _headerClass = null;
    private String _lang = null;
    private String _onclick = null;
    private String _ondblclick = null;
    private String _onkeydown = null;
    private String _onkeypress = null;
    private String _onkeyup = null;
    private String _onmousedown = null;
    private String _onmousemove = null;
    private String _onmouseout = null;
    private String _onmouseover = null;
    private String _onmouseup = null;
    private String _rowClasses = null;
    private String _rules = null;
    private String _style = null;
    private String _styleClass = null;
    private String _summary = null;
    private String _title = null;
    private String _width = null;

    public HtmlPanelGrid()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }


    public void setBgcolor(String bgcolor)
    {
        _bgcolor = bgcolor;
    }

    public String getBgcolor()
    {
        if (_bgcolor != null) return _bgcolor;
        ValueBinding vb = getValueBinding("bgcolor");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setBorder(int border)
    {
        _border = new Integer(border);
    }

    public int getBorder()
    {
        if (_border != null) return _border.intValue();
        ValueBinding vb = getValueBinding("border");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : DEFAULT_BORDER;
    }

    public void setCellpadding(String cellpadding)
    {
        _cellpadding = cellpadding;
    }

    public String getCellpadding()
    {
        if (_cellpadding != null) return _cellpadding;
        ValueBinding vb = getValueBinding("cellpadding");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setCellspacing(String cellspacing)
    {
        _cellspacing = cellspacing;
    }

    public String getCellspacing()
    {
        if (_cellspacing != null) return _cellspacing;
        ValueBinding vb = getValueBinding("cellspacing");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setColumnClasses(String columnClasses)
    {
        _columnClasses = columnClasses;
    }

    public String getColumnClasses()
    {
        if (_columnClasses != null) return _columnClasses;
        ValueBinding vb = getValueBinding("columnClasses");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setColumns(int columns)
    {
        _columns = new Integer(columns);
    }

    public int getColumns()
    {
        if (_columns != null) return _columns.intValue();
        ValueBinding vb = getValueBinding("columns");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : DEFAULT_COLUMNS;
    }

    public void setDir(String dir)
    {
        _dir = dir;
    }

    public String getDir()
    {
        if (_dir != null) return _dir;
        ValueBinding vb = getValueBinding("dir");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setFooterClass(String footerClass)
    {
        _footerClass = footerClass;
    }

    public String getFooterClass()
    {
        if (_footerClass != null) return _footerClass;
        ValueBinding vb = getValueBinding("footerClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setFrame(String frame)
    {
        _frame = frame;
    }

    public String getFrame()
    {
        if (_frame != null) return _frame;
        ValueBinding vb = getValueBinding("frame");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setHeaderClass(String headerClass)
    {
        _headerClass = headerClass;
    }

    public String getHeaderClass()
    {
        if (_headerClass != null) return _headerClass;
        ValueBinding vb = getValueBinding("headerClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setLang(String lang)
    {
        _lang = lang;
    }

    public String getLang()
    {
        if (_lang != null) return _lang;
        ValueBinding vb = getValueBinding("lang");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnclick(String onclick)
    {
        _onclick = onclick;
    }

    public String getOnclick()
    {
        if (_onclick != null) return _onclick;
        ValueBinding vb = getValueBinding("onclick");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOndblclick(String ondblclick)
    {
        _ondblclick = ondblclick;
    }

    public String getOndblclick()
    {
        if (_ondblclick != null) return _ondblclick;
        ValueBinding vb = getValueBinding("ondblclick");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnkeydown(String onkeydown)
    {
        _onkeydown = onkeydown;
    }

    public String getOnkeydown()
    {
        if (_onkeydown != null) return _onkeydown;
        ValueBinding vb = getValueBinding("onkeydown");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnkeypress(String onkeypress)
    {
        _onkeypress = onkeypress;
    }

    public String getOnkeypress()
    {
        if (_onkeypress != null) return _onkeypress;
        ValueBinding vb = getValueBinding("onkeypress");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnkeyup(String onkeyup)
    {
        _onkeyup = onkeyup;
    }

    public String getOnkeyup()
    {
        if (_onkeyup != null) return _onkeyup;
        ValueBinding vb = getValueBinding("onkeyup");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmousedown(String onmousedown)
    {
        _onmousedown = onmousedown;
    }

    public String getOnmousedown()
    {
        if (_onmousedown != null) return _onmousedown;
        ValueBinding vb = getValueBinding("onmousedown");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmousemove(String onmousemove)
    {
        _onmousemove = onmousemove;
    }

    public String getOnmousemove()
    {
        if (_onmousemove != null) return _onmousemove;
        ValueBinding vb = getValueBinding("onmousemove");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmouseout(String onmouseout)
    {
        _onmouseout = onmouseout;
    }

    public String getOnmouseout()
    {
        if (_onmouseout != null) return _onmouseout;
        ValueBinding vb = getValueBinding("onmouseout");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmouseover(String onmouseover)
    {
        _onmouseover = onmouseover;
    }

    public String getOnmouseover()
    {
        if (_onmouseover != null) return _onmouseover;
        ValueBinding vb = getValueBinding("onmouseover");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmouseup(String onmouseup)
    {
        _onmouseup = onmouseup;
    }

    public String getOnmouseup()
    {
        if (_onmouseup != null) return _onmouseup;
        ValueBinding vb = getValueBinding("onmouseup");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRowClasses(String rowClasses)
    {
        _rowClasses = rowClasses;
    }

    public String getRowClasses()
    {
        if (_rowClasses != null) return _rowClasses;
        ValueBinding vb = getValueBinding("rowClasses");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRules(String rules)
    {
        _rules = rules;
    }

    public String getRules()
    {
        if (_rules != null) return _rules;
        ValueBinding vb = getValueBinding("rules");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setStyle(String style)
    {
        _style = style;
    }

    public String getStyle()
    {
        if (_style != null) return _style;
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    public String getStyleClass()
    {
        if (_styleClass != null) return _styleClass;
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSummary(String summary)
    {
        _summary = summary;
    }

    public String getSummary()
    {
        if (_summary != null) return _summary;
        ValueBinding vb = getValueBinding("summary");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setTitle(String title)
    {
        _title = title;
    }

    public String getTitle()
    {
        if (_title != null) return _title;
        ValueBinding vb = getValueBinding("title");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setWidth(String width)
    {
        _width = width;
    }

    public String getWidth()
    {
        if (_width != null) return _width;
        ValueBinding vb = getValueBinding("width");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[29];
        values[0] = super.saveState(context);
        values[1] = _bgcolor;
        values[2] = _border;
        values[3] = _cellpadding;
        values[4] = _cellspacing;
        values[5] = _columnClasses;
        values[6] = _columns;
        values[7] = _dir;
        values[8] = _footerClass;
        values[9] = _frame;
        values[10] = _headerClass;
        values[11] = _lang;
        values[12] = _onclick;
        values[13] = _ondblclick;
        values[14] = _onkeydown;
        values[15] = _onkeypress;
        values[16] = _onkeyup;
        values[17] = _onmousedown;
        values[18] = _onmousemove;
        values[19] = _onmouseout;
        values[20] = _onmouseover;
        values[21] = _onmouseup;
        values[22] = _rowClasses;
        values[23] = _rules;
        values[24] = _style;
        values[25] = _styleClass;
        values[26] = _summary;
        values[27] = _title;
        values[28] = _width;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _bgcolor = (String)values[1];
        _border = (Integer)values[2];
        _cellpadding = (String)values[3];
        _cellspacing = (String)values[4];
        _columnClasses = (String)values[5];
        _columns = (Integer)values[6];
        _dir = (String)values[7];
        _footerClass = (String)values[8];
        _frame = (String)values[9];
        _headerClass = (String)values[10];
        _lang = (String)values[11];
        _onclick = (String)values[12];
        _ondblclick = (String)values[13];
        _onkeydown = (String)values[14];
        _onkeypress = (String)values[15];
        _onkeyup = (String)values[16];
        _onmousedown = (String)values[17];
        _onmousemove = (String)values[18];
        _onmouseout = (String)values[19];
        _onmouseover = (String)values[20];
        _onmouseup = (String)values[21];
        _rowClasses = (String)values[22];
        _rules = (String)values[23];
        _style = (String)values[24];
        _styleClass = (String)values[25];
        _summary = (String)values[26];
        _title = (String)values[27];
        _width = (String)values[28];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

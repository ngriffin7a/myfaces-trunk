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
package net.sourceforge.myfaces.taglib.ext;

import net.sourceforge.myfaces.component.ext.HtmlDataList;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.taglib.html.HtmlComponentBodyTag;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataListTag
        extends HtmlComponentBodyTag
{
    //private static final Log log = LogFactory.getLog(MyfacesHtmlDataTableTag.class);

    public String getComponentType()
    {
        return HtmlDataList.COMPONENT_TYPE;
    }

    protected String getDefaultRendererType()
    {
        return "net.sourceforge.myfaces.List";
    }

    // UIComponent attributes --> already implemented in MyfacesComponentTag

    // user role attributes --> already implemented in MyfacesComponentTag

    // HTML universal attributes --> already implemented in HtmlComponentTag

    // HTML event handler attributes --> already implemented in HtmlComponentTag

    // UIData attributes
    private String _rows;
    private String _var;
    private String _first;

    // HtmlDataList attributes
    private String _layout;
    private String _firstRowFlag;
    private String _lastRowFlag;


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setIntegerProperty(component, JSFAttr.ROWS_ATTR, _rows);
        setStringProperty(component, JSFAttr.VAR_ATTR, _var);
        setIntegerProperty(component, JSFAttr.FIRST_ATTR, _first);

        setStringProperty(component, JSFAttr.LAYOUT_ATTR, _layout);
        setStringProperty(component, "firstRowFlag", _firstRowFlag);
        setStringProperty(component, "lastRowFlag", _lastRowFlag);
    }

    public void setRows(String rows)
    {
        _rows = rows;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public void setFirst(String first)
    {
        _first = first;
    }

    public void setLayout(String layout)
    {
        _layout = layout;
    }

    public void setFirstRowFlag(String firstRowFlag)
    {
        _firstRowFlag = firstRowFlag;
    }

    public void setLastRowFlag(String lastRowFlag)
    {
        _lastRowFlag = lastRowFlag;
    }
}

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

import net.sourceforge.myfaces.taglib.html.HtmlDataTableTag;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyfacesHtmlDataTableTag
        extends HtmlDataTableTag
{
    //private static final Log log = LogFactory.getLog(MyfacesHtmlDataTableTag.class);

    public String getComponentType()
    {
        return "net.sourceforge.myfaces.HtmlDataTable";
    }

    protected String getDefaultRendererType()
    {
        return "net.sourceforge.myfaces.Table";
    }

    private String _preserveDataModel;
    private String _sortColumn;
    private String _sortAscending;
    private String _preserveSort;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        if (_preserveDataModel == null) _preserveDataModel = "true";
        if (_preserveSort == null) _preserveSort = "true";

        setBooleanProperty(component, "preserveDataModel", _preserveDataModel);
        setValueBinding(component, "sortColumn", _sortColumn);
        setValueBinding(component, "sortAscending", _sortAscending);
        setBooleanProperty(component, "preserveSort", _preserveSort);
    }

    public void setPreserveDataModel(String preserveDataModel)
    {
        _preserveDataModel = preserveDataModel;
    }

    public void setSortColumn(String sortColumn)
    {
        _sortColumn = sortColumn;
    }

    public void setSortAscending(String sortAscending)
    {
        _sortAscending = sortAscending;
    }

    public void setPreserveSort(String preserveSort)
    {
        _preserveSort = preserveSort;
    }
}

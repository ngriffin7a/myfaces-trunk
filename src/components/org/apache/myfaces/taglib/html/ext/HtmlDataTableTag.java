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
package net.sourceforge.myfaces.taglib.html.ext;

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.component.html.ext.HtmlDataTable;
import net.sourceforge.myfaces.taglib.html.HtmlDataTableTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.6  2004/05/21 10:39:27  manolito
 * new renderedIfEmpty attribute in ext. HtmlDataTable component
 *
 * Revision 1.5  2004/05/18 15:07:11  manolito
 * no message
 *
 * Revision 1.4  2004/05/18 14:31:38  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.3  2004/04/05 11:04:55  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.2  2004/04/01 12:57:42  manolito
 * additional extended component classes for user role support
 *
 * Revision 1.1  2004/03/31 12:15:28  manolito
 * custom component refactoring
 *
 */
public class HtmlDataTableTag
        extends HtmlDataTableTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlDataTableTag.class);

    public String getComponentType()
    {
        return HtmlDataTable.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "javax.faces.Table";
    }

    private String _preserveDataModel;
    private String _sortColumn;
    private String _sortAscending;
    private String _preserveSort;
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;
    private String _renderedIfEmpty;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        if (_preserveDataModel == null) _preserveDataModel = "true";
        if (_preserveSort == null) _preserveSort = "true";

        setBooleanProperty(component, "preserveDataModel", _preserveDataModel);
        setValueBinding(component, "sortColumn", _sortColumn);
        setValueBinding(component, "sortAscending", _sortAscending);
        setBooleanProperty(component, "preserveSort", _preserveSort);
        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
        setBooleanProperty(component, "renderedIfEmpty", _renderedIfEmpty);
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

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public void setRenderedIfEmpty(String renderedIfEmpty)
    {
        _renderedIfEmpty = renderedIfEmpty;
    }
}

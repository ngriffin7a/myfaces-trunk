/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * Revision 1.9  2004/09/10 14:15:00  manolito
 * new previousRowDataVar attribute in extended HtmlDataTable
 *
 * Revision 1.8  2004/08/20 07:14:41  manolito
 * HtmlDataTable now also supports rowIndexVar and rowCountVar
 *
 * Revision 1.7  2004/07/01 21:53:06  mwessendorf
 * ASF switch
 *
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
        return "net.sourceforge.myfaces.Table";
    }

    private String _preserveDataModel;
    private String _sortColumn;
    private String _sortAscending;
    private String _preserveSort;
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;
    private String _renderedIfEmpty;
    private String _rowIndexVar;
    private String _rowCountVar;
    private String _previousRowDataVar;

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
        setStringProperty(component, "rowIndexVar", _rowIndexVar);
        setStringProperty(component, "rowCountVar", _rowCountVar);
        setStringProperty(component, "previousRowDataVar", _previousRowDataVar);
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

    public void setRowIndexVar(String rowIndexVar)
    {
        _rowIndexVar = rowIndexVar;
    }

    public void setRowCountVar(String rowCountVar)
    {
        _rowCountVar = rowCountVar;
    }

    public void setPreviousRowDataVar(String previousRowDataVar)
    {
        _previousRowDataVar = previousRowDataVar;
    }
}

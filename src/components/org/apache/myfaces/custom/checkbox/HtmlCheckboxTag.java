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
package net.sourceforge.myfaces.custom.checkbox;

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/05/18 14:31:37  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.2  2004/04/05 11:04:52  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.1  2004/04/02 13:57:10  manolito
 * extended HtmlSelectManyCheckbox with layout "spread" and custom Checkbox component
 *
 */
public class HtmlCheckboxTag
        extends UIComponentTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlInputFileUploadTag.class);

    public String getComponentType()
    {
        return HtmlCheckbox.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }

    // UIComponent attributes --> already implemented in UIComponentTagBase

    // user role attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in HtmlComponentTagBase

    // HtmlCheckbox attributes
    private String _for;
    private String _index;

    // User Role support
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HtmlCheckbox.FOR_ATTR, _for);
        setIntegerProperty(component, HtmlCheckbox.INDEX_ATTR, _index);

        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    public String getFor()
    {
        return _for;
    }

    public void setFor(String aFor)
    {
        _for = aFor;
    }

    public String getIndex()
    {
        return _index;
    }

    public void setIndex(String index)
    {
        _index = index;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }
}

/**
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
package net.sourceforge.myfaces.custom.navmenu;

import net.sourceforge.myfaces.component.UserRoleUtils;
import net.sourceforge.myfaces.component.UserRoleAware;

import javax.faces.component.UISelectItem;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/06/23 13:44:31  royalts
 *          no message
 *
 */
public class UINavigationMenuItem
    extends UISelectItem
    implements UserRoleAware
{


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.NavigationMenuItem";
    public static final String COMPONENT_FAMILY = "javax.faces.SelectItem";

    private String _icon = null;
    private Boolean _split = null;
    private String _action = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;

    public UINavigationMenuItem()
    {
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setIcon(String icon)
    {
        _icon = icon;
    }

    public String getIcon()
    {
        if (_icon != null) return _icon;
        ValueBinding vb = getValueBinding("icon");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSplit(boolean split)
    {
        _split = Boolean.valueOf(split);
    }

    public boolean isSplit()
    {
        if (_split != null) return _split.booleanValue();
        ValueBinding vb = getValueBinding("split");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    public void setAction(String action)
    {
        _action = action;
    }

    public String getAction()
    {
        if (_action != null) return _action;
        ValueBinding vb = getValueBinding("action");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null) return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null) return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = _icon;
        values[2] = _split;
        values[3] = _action;
        values[4] = _enabledOnUserRole;
        values[5] = _visibleOnUserRole;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _icon = (String)values[1];
        _split = (Boolean)values[2];
        _action = (String)values[3];
        _enabledOnUserRole = (String)values[4];
        _visibleOnUserRole = (String)values[5];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

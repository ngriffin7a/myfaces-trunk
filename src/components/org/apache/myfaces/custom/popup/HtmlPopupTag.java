/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.apache.myfaces.custom.popup;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.taglib.UIComponentTagBase;
import org.apache.myfaces.taglib.html.HtmlComponentTagBase;
import org.apache.myfaces.custom.datascroller.HtmlDataScroller;
import org.apache.myfaces.custom.datascroller.HtmlDataScrollerRenderer;

import javax.faces.component.UIComponent;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/11/16 16:25:52  mmarinschek
 * new popup - component; not yet finished
 *
 *
 */
public class HtmlPopupTag
        extends HtmlComponentTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlDataScrollerTag.class);

    // UIComponent attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in HtmlComponentTagBase

    // User Role support
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    public String getComponentType()
    {
        return HtmlPopup.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return HtmlPopupRenderer.RENDERER_TYPE;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    // userrole attributes
    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }
}

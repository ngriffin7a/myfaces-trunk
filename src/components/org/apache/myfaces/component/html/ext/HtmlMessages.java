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
package net.sourceforge.myfaces.component.html.ext;

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.component.UserRoleUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class HtmlMessages
        extends javax.faces.component.html.HtmlMessages
        implements UserRoleAware
{
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlMessages";
    public static final String COMPONENT_FAMILY = "javax.faces.Messages";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Messages";
    private static final String DEFAULT_SUMMARYDETAILSEPARATOR = ": ";
    private static final String DEFAULT_LABELFORMAT = " in {0}";

    private String _summaryDetailSeparator = null;
    private String _labelFormat = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;

    public HtmlMessages()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setSummaryDetailSeparator(String summaryDetailSeparator)
    {
        _summaryDetailSeparator = summaryDetailSeparator;
    }

    public String getSummaryDetailSeparator()
    {
        if (_summaryDetailSeparator != null) return _summaryDetailSeparator;
        ValueBinding vb = getValueBinding("summaryDetailSeparator");
        return vb != null ? (String)vb.getValue(getFacesContext()) : DEFAULT_SUMMARYDETAILSEPARATOR;
    }

    public void setLabelFormat(String labelFormat)
    {
        _labelFormat = labelFormat;
    }

    public String getLabelFormat()
    {
        if (_labelFormat != null) return _labelFormat;
        ValueBinding vb = getValueBinding("labelFormat");
        return vb != null ? (String)vb.getValue(getFacesContext()) : DEFAULT_LABELFORMAT;
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
        if (!UserRoleUtils.isUserInRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = _summaryDetailSeparator;
        values[2] = _labelFormat;
        values[3] = _enabledOnUserRole;
        values[4] = _visibleOnUserRole;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _summaryDetailSeparator = (String)values[1];
        _labelFormat = (String)values[2];
        _enabledOnUserRole = (String)values[3];
        _visibleOnUserRole = (String)values[4];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

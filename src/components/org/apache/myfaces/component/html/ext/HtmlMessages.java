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
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.Messages";

    private String _summaryFormat = null;
    private String _globalSummaryFormat = null;
    private String _detailFormat = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;

    public HtmlMessages()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }


    public void setSummaryFormat(String summaryFormat)
    {
        _summaryFormat = summaryFormat;
    }

    public String getSummaryFormat()
    {
        if (_summaryFormat != null) return _summaryFormat;
        ValueBinding vb = getValueBinding("summaryFormat");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setGlobalSummaryFormat(String globalSummaryFormat)
    {
        _globalSummaryFormat = globalSummaryFormat;
    }

    public String getGlobalSummaryFormat()
    {
        if (_globalSummaryFormat != null) return _globalSummaryFormat;
        ValueBinding vb = getValueBinding("globalSummaryFormat");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setDetailFormat(String detailFormat)
    {
        _detailFormat = detailFormat;
    }

    public String getDetailFormat()
    {
        if (_detailFormat != null) return _detailFormat;
        ValueBinding vb = getValueBinding("detailFormat");
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
        if (!UserRoleUtils.isUserInRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = _summaryFormat;
        values[2] = _globalSummaryFormat;
        values[3] = _detailFormat;
        values[4] = _enabledOnUserRole;
        values[5] = _visibleOnUserRole;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _summaryFormat = (String)values[1];
        _globalSummaryFormat = (String)values[2];
        _detailFormat = (String)values[3];
        _enabledOnUserRole = (String)values[4];
        _visibleOnUserRole = (String)values[5];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

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
package javax.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * see Javadoc of JSF Specification
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIMessages
        extends UIComponentBase
{
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.Messages";
    public static final String COMPONENT_FAMILY = "javax.faces.Messages";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Messages";
    private static final boolean DEFAULT_GLOBALONLY = false;
    private static final boolean DEFAULT_SHOWDETAIL = false;
    private static final boolean DEFAULT_SHOWSUMMARY = true;

    private Boolean _globalOnly = null;
    private Boolean _showDetail = null;
    private Boolean _showSummary = null;

    public UIMessages()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setGlobalOnly(boolean globalOnly)
    {
        _globalOnly = Boolean.valueOf(globalOnly);
    }

    public boolean isGlobalOnly()
    {
        if (_globalOnly != null) return _globalOnly.booleanValue();
        ValueBinding vb = getValueBinding("globalOnly");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_GLOBALONLY;
    }

    public void setShowDetail(boolean showDetail)
    {
        _showDetail = Boolean.valueOf(showDetail);
    }

    public boolean isShowDetail()
    {
        if (_showDetail != null) return _showDetail.booleanValue();
        ValueBinding vb = getValueBinding("showDetail");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_SHOWDETAIL;
    }

    public void setShowSummary(boolean showSummary)
    {
        _showSummary = Boolean.valueOf(showSummary);
    }

    public boolean isShowSummary()
    {
        if (_showSummary != null) return _showSummary.booleanValue();
        ValueBinding vb = getValueBinding("showSummary");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_SHOWSUMMARY;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _globalOnly;
        values[2] = _showDetail;
        values[3] = _showSummary;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _globalOnly = (Boolean)values[1];
        _showDetail = (Boolean)values[2];
        _showSummary = (Boolean)values[3];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

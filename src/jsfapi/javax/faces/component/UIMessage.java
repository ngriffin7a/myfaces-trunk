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
 * Javadoc says default for showDetail is false, but Specs say it is true!?
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.6  2004/03/30 17:47:33  manolito
 * Message and Messages refactored
 *
 */
public class UIMessage
        extends UIComponentBase
{
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.Message";
    public static final String COMPONENT_FAMILY = "javax.faces.Message";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Message";
    private static final boolean DEFAULT_SHOWDETAIL = true;
    private static final boolean DEFAULT_SHOWSUMMARY = false;

    private String _for = null;
    private Boolean _showDetail = null;
    private Boolean _showSummary = null;

    public UIMessage()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setFor(String forValue)
    {
        _for = forValue;
    }

    public String getFor()
    {
        if (_for != null) return _for;
        ValueBinding vb = getValueBinding("for");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
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
        if (vb != null)
        {
            Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
            return v != null ? v.booleanValue() : DEFAULT_SHOWSUMMARY;
        }
        // default
        return true;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = _showDetail;
        values[3] = _showSummary;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _for = (String)values[1];
        _showDetail = (Boolean)values[2];
        _showSummary = (Boolean)values[3];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

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
package net.sourceforge.myfaces.component.ext;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataList
        extends UIData
{
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlDataList";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.List";

    private String _layout = null;
    private String _firstRowFlag = null;
    private String _lastRowFlag = null;

    public HtmlDataList()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }


    public void setLayout(String layout)
    {
        _layout = layout;
    }

    public String getLayout()
    {
        if (_layout != null) return _layout;
        ValueBinding vb = getValueBinding("layout");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setFirstRowFlag(String firstRowFlag)
    {
        _firstRowFlag = firstRowFlag;
    }

    public String getFirstRowFlag()
    {
        if (_firstRowFlag != null) return _firstRowFlag;
        ValueBinding vb = getValueBinding("firstRowFlag");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setLastRowFlag(String lastRowFlag)
    {
        _lastRowFlag = lastRowFlag;
    }

    public String getLastRowFlag()
    {
        if (_lastRowFlag != null) return _lastRowFlag;
        ValueBinding vb = getValueBinding("lastRowFlag");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _layout;
        values[2] = _firstRowFlag;
        values[3] = _lastRowFlag;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _layout = (String)values[1];
        _firstRowFlag = (String)values[2];
        _lastRowFlag = (String)values[3];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

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
package net.sourceforge.myfaces.custom.calendar;

import net.sourceforge.myfaces.component.UserRoleUtils;

import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlInputCalendar
        extends HtmlInputText
{
    //private static final Log log = LogFactory.getLog(HtmlInputCalendar.class);

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlInputCalendar";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.Calendar";

    private String _monthYearRowClass = null;
    private String _weekRowClass = null;
    private String _dayCellClass = null;
    private String _currentDayCellClass = null;
    private Boolean _renderAsPopup = null;
    private String _popupDateFormat = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;
    private String _popupButtonString = null;
    private String _popupTheme = null;

    public HtmlInputCalendar()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setMonthYearRowClass(String monthYearRowClass)
    {
        _monthYearRowClass = monthYearRowClass;
    }

    public String getMonthYearRowClass()
    {
        if (_monthYearRowClass != null) return _monthYearRowClass;
        ValueBinding vb = getValueBinding("monthYearRowClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setWeekRowClass(String weekRowClass)
    {
        _weekRowClass = weekRowClass;
    }

    public String getWeekRowClass()
    {
        if (_weekRowClass != null) return _weekRowClass;
        ValueBinding vb = getValueBinding("weekRowClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setDayCellClass(String dayCellClass)
    {
        _dayCellClass = dayCellClass;
    }

    public String getDayCellClass()
    {
        if (_dayCellClass != null) return _dayCellClass;
        ValueBinding vb = getValueBinding("dayCellClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setCurrentDayCellClass(String currentDayCellClass)
    {
        _currentDayCellClass = currentDayCellClass;
    }

    public String getCurrentDayCellClass()
    {
        if (_currentDayCellClass != null) return _currentDayCellClass;
        ValueBinding vb = getValueBinding("currentDayCellClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRenderAsPopup(boolean renderAsPopup)
    {
        _renderAsPopup = Boolean.valueOf(renderAsPopup);
    }

    public boolean isRenderAsPopup()
    {
        if (_renderAsPopup != null) return _renderAsPopup.booleanValue();
        ValueBinding vb = getValueBinding("renderAsPopup");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    public void setPopupDateFormat(String popupDateFormat)
    {
        _popupDateFormat = popupDateFormat;
    }

    public String getPopupDateFormat()
    {
        if (_popupDateFormat != null) return _popupDateFormat;
        ValueBinding vb = getValueBinding("popupDateFormat");
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

    public void setPopupButtonString(String popupButtonString)
    {
        _popupButtonString = popupButtonString;
    }

    public String getPopupButtonString()
    {
        if (_popupButtonString != null) return _popupButtonString;
        ValueBinding vb = getValueBinding("popupButtonString");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupTheme(String popupTheme)
    {
        _popupTheme = popupTheme;
    }

    public String getPopupTheme()
    {
        if (_popupTheme != null) return _popupTheme;
        ValueBinding vb = getValueBinding("popupTheme");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[11];
        values[0] = super.saveState(context);
        values[1] = _monthYearRowClass;
        values[2] = _weekRowClass;
        values[3] = _dayCellClass;
        values[4] = _currentDayCellClass;
        values[5] = _renderAsPopup;
        values[6] = _popupDateFormat;
        values[7] = _enabledOnUserRole;
        values[8] = _visibleOnUserRole;
        values[9] = _popupButtonString;
        values[10] = _popupTheme;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _monthYearRowClass = (String)values[1];
        _weekRowClass = (String)values[2];
        _dayCellClass = (String)values[3];
        _currentDayCellClass = (String)values[4];
        _renderAsPopup = (Boolean)values[5];
        _popupDateFormat = (String)values[6];
        _enabledOnUserRole = (String)values[7];
        _visibleOnUserRole = (String)values[8];
        _popupButtonString = (String)values[9];
        _popupTheme = (String)values[10];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

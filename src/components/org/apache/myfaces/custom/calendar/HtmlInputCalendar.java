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


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = _monthYearRowClass;
        values[2] = _weekRowClass;
        values[3] = _dayCellClass;
        values[4] = _currentDayCellClass;
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
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

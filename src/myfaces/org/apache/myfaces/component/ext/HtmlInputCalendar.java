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

import javax.faces.component.html.HtmlInputText;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

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

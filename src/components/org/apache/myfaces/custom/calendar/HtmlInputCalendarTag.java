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
package net.sourceforge.myfaces.custom.calendar;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.taglib.html.HtmlInputTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/04/05 11:04:51  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.2  2004/04/01 12:57:39  manolito
 * additional extended component classes for user role support
 *
 * Revision 1.1  2004/03/31 12:15:25  manolito
 * custom component refactoring
 *
 */
public class HtmlInputCalendarTag
        extends HtmlInputTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlInputCalendarTag.class);

    public String getComponentType()
    {
        return HtmlInputCalendar.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "net.sourceforge.myfaces.Calendar";
    }

    // UIComponent attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in MyFacesTag    

    // UIOutput attributes
    // value and converterId --> already implemented in UIComponentTagBase

    // UIInput attributes
    // --> already implemented in HtmlInputTagBase

    // HtmlCalendar attributes
    private String _monthYearRowClass;
    private String _weekRowClass;
    private String _dayCellClass;
    private String _currentDayCellClass;

    // User Role support
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "monthYearRowClass", _monthYearRowClass);
        setStringProperty(component, "weekRowClass", _weekRowClass);
        setStringProperty(component, "dayCellClass", _dayCellClass);
        setStringProperty(component, "currentDayCellClass", _currentDayCellClass);

        setStringProperty(component, JSFAttr.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, JSFAttr.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    public void setMonthYearRowClass(String monthYearRowClass)
    {
        _monthYearRowClass = monthYearRowClass;
    }

    public void setWeekRowClass(String weekRowClass)
    {
        _weekRowClass = weekRowClass;
    }

    public void setDayCellClass(String dayCellClass)
    {
        _dayCellClass = dayCellClass;
    }

    public void setCurrentDayCellClass(String currentDayCellClass)
    {
        _currentDayCellClass = currentDayCellClass;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }
}

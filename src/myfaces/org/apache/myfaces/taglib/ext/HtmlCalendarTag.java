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
package net.sourceforge.myfaces.taglib.ext;

import net.sourceforge.myfaces.component.ext.HtmlInputFileUpload;
import net.sourceforge.myfaces.component.ext.HtmlInputCalendar;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.taglib.UIInputTag;
import net.sourceforge.myfaces.taglib.MyfacesComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlCalendarTag
        extends UIInputTag
{
    //private static final Log log = LogFactory.getLog(HtmlCalendarTag.class);

    public String getComponentType()
    {
        return HtmlInputCalendar.COMPONENT_TYPE;
    }

    protected String getDefaultRendererType()
    {
        return "net.sourceforge.myfaces.Calendar";
    }

    // UIComponent attributes --> already implemented in MyfacesComponentTag

    // HTML universal attributes --> already implemented in HtmlComponentTag

    // HTML event handler attributes --> already implemented in MyFacesTag    

    // UIOutput attributes
    // value and converterId --> already implemented in MyfacesComponentTag

    // UIInput attributes
    // --> already implemented in UIInputTag

    // HtmlCalendar attributes
    private String _monthYearRowClass;
    private String _weekRowClass;
    private String _dayCellClass;
    private String _currentDayCellClass;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "monthYearRowClass", _monthYearRowClass);
        setStringProperty(component, "weekRowClass", _weekRowClass);
        setStringProperty(component, "dayCellClass", _dayCellClass);
        setStringProperty(component, "currentDayCellClass", _currentDayCellClass);
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
}

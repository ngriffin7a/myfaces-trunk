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

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.taglib.html.HtmlInputTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.8  2004/07/28 18:00:47  tinytoony
 * calendar; revisited again for complete i18
 *
 * Revision 1.7  2004/07/27 16:48:02  tinytoony
 * new calendar popup, revisited
 *
 * Revision 1.6  2004/07/27 06:28:32  tinytoony
 * new calendar component as a popup
 *
 * Revision 1.5  2004/07/01 21:53:11  mwessendorf
 * ASF switch
 *
 * Revision 1.4  2004/05/18 14:31:36  manolito
 * user role support completely moved to components source tree
 *
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
    private String _renderAsPopup;
    private String _popupDateFormat;
    private String _popupButtonString;
    private String _popupGotoString = null;
    private String _popupTodayString = null;
    private String _popupWeekString = null;
    private String _popupScrollLeftMessage = null;
    private String _popupScrollRightMessage = null;
    private String _popupSelectMonthMessage = null;
    private String _popupSelectYearMessage = null;
    private String _popupSelectDateMessage = null;

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
        setBooleanProperty(component,"renderAsPopup",_renderAsPopup);
        setStringProperty(component,"popupDateFormat",_popupDateFormat);
        setStringProperty(component,"popupButtonString",_popupButtonString);
        setStringProperty(component,"popupGotoString",_popupGotoString);
        setStringProperty(component,"popupTodayString",_popupTodayString);
        setStringProperty(component,"popupWeekString",_popupWeekString);
        setStringProperty(component,"popupScrollLeftMessage",_popupScrollLeftMessage);
        setStringProperty(component,"popupScrollRightMessage",_popupScrollRightMessage);
        setStringProperty(component,"popupSelectMonthMessage",_popupSelectMonthMessage);
        setStringProperty(component,"popupSelectYearMessage",_popupSelectYearMessage);
        setStringProperty(component,"popupSelectDateMessage",_popupSelectDateMessage);


        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
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

    public void setRenderAsPopup(String renderAsPopup)
    {
        _renderAsPopup = renderAsPopup;
    }

    public void setPopupDateFormat(String popupDateFormat)
    {
        _popupDateFormat = popupDateFormat;
    }

    public void setPopupButtonString(String popupButtonString)
    {
        _popupButtonString = popupButtonString;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public void setPopupGotoString(String popupGotoString)
    {
        _popupGotoString = popupGotoString;
    }

    public void setPopupScrollLeftMessage(String popupScrollLeftMessage)
    {
        _popupScrollLeftMessage = popupScrollLeftMessage;
    }

    public void setPopupScrollRightMessage(String popupScrollRightMessage)
    {
        _popupScrollRightMessage = popupScrollRightMessage;
    }

    public void setPopupSelectDateMessage(String popupSelectDateMessage)
    {
        _popupSelectDateMessage = popupSelectDateMessage;
    }

    public void setPopupSelectMonthMessage(String popupSelectMonthMessage)
    {
        _popupSelectMonthMessage = popupSelectMonthMessage;
    }

    public void setPopupSelectYearMessage(String popupSelectYearMessage)
    {
        _popupSelectYearMessage = popupSelectYearMessage;
    }

    public void setPopupTodayString(String popupTodayString)
    {
        _popupTodayString = popupTodayString;
    }

    public void setPopupWeekString(String popupWeekString)
    {
        _popupWeekString = popupWeekString;
    }
}

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
package org.apache.myfaces.custom.date;

import javax.faces.component.UIComponent;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.taglib.html.HtmlInputTextTagBase;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$ $Log:
 *          HtmlInputCalendarTag.java,v $
 *  
 */
public class HtmlInputDateTag extends HtmlInputTextTagBase {
    public String getComponentType() {
        return HtmlInputDate.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.apache.myfaces.Date";
    }
    
    // HtmlInputDate attributes
    private String _type;

    // UIComponent attributes --> already implemented in UIComponentTagBase
    // HTML universal attributes --> already implemented in HtmlComponentTagBase
    // HTML event handler attributes --> already implemented in MyFacesTag

    // UIOutput attributes
    // value and converterId --> already implemented in UIComponentTagBase

    // UIInput attributes --> already implemented in HtmlInputTagBase
    // UIHTML Input attributes --> already implemented in HtmlInputTextTagBase

    // User Role support
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
        setStringProperty(component, "type", _type);
    }
    
    public void setType(String type){
        _type = type;
    }
    
    public void setEnabledOnUserRole(String enabledOnUserRole){
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole){
        _visibleOnUserRole = visibleOnUserRole;
    }
}
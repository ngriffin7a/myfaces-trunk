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
package org.apache.myfaces.custom.radio;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.7  2004/10/13 11:50:57  matze
 * renamed packages to org.apache
 *
 * Revision 1.6  2004/07/01 21:53:08  mwessendorf
 * ASF switch
 *
 * Revision 1.5  2004/05/18 14:31:38  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.4  2004/04/05 11:04:54  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.3  2004/04/02 13:57:10  manolito
 * extended HtmlSelectManyCheckbox with layout "spread" and custom Checkbox component
 *
 */
public class HtmlRadioTag
        extends UIComponentTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlInputFileUploadTag.class);

    public String getComponentType()
    {
        return HtmlRadio.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }

    // UIComponent attributes --> already implemented in UIComponentTagBase

    // user role attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in HtmlComponentTagBase

    // HtmlRadio attributes
    private String _for;
    private String _index;

    // User Role support
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HtmlRadio.FOR_ATTR, _for);
        setIntegerProperty(component, HtmlRadio.INDEX_ATTR, _index);

        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    public String getFor()
    {
        return _for;
    }

    public void setFor(String aFor)
    {
        _for = aFor;
    }

    public String getIndex()
    {
        return _index;
    }

    public void setIndex(String index)
    {
        _index = index;
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

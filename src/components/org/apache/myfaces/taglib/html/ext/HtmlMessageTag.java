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
package org.apache.myfaces.taglib.html.ext;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.html.ext.HtmlMessage;
import org.apache.myfaces.taglib.html.HtmlMessageTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.8  2004/10/13 11:50:59  matze
 * renamed packages to org.apache
 *
 * Revision 1.7  2004/07/01 21:53:06  mwessendorf
 * ASF switch
 *
 * Revision 1.6  2004/05/18 14:31:38  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.5  2004/04/05 11:04:55  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.4  2004/04/01 12:57:42  manolito
 * additional extended component classes for user role support
 *
 * Revision 1.3  2004/03/31 14:51:47  manolito
 * summaryFormat and detailFormat support
 *
 * Revision 1.2  2004/03/30 17:47:32  manolito
 * Message and Messages refactored
 *
 * Revision 1.1  2004/03/30 13:27:05  manolito
 * extended Message component
 *
 */
public class HtmlMessageTag
        extends HtmlMessageTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlOutputFormatTag.class);

    public String getComponentType()
    {
        return HtmlMessage.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.Message";
    }

    private String _summaryFormat;
    private String _detailFormat;
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "summaryFormat", _summaryFormat);
        setStringProperty(component, "detailFormat", _detailFormat);
        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    public void setSummaryFormat(String summaryFormat)
    {
        _summaryFormat = summaryFormat;
    }

    public void setDetailFormat(String detailFormat)
    {
        _detailFormat = detailFormat;
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

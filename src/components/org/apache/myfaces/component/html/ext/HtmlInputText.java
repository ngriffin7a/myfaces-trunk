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
package org.apache.myfaces.component.html.ext;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.component.html.util.HtmlComponentUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.7  2005/01/16 20:09:53  matzew
 * added patch form Sean Schofield. forceId for reuse of "legacy JavaScript" (MyFaces-70)
 *
 * Revision 1.6  2005/01/13 09:24:53  matzew
 * added patch form Sean Schofield. forceId for reuse of "legacy JavaScript" (MyFaces-70)
 *
 * Revision 1.5  2005/01/10 08:08:14  matzew
 * added patch form sean schofield. forceId for reuse of "legacy JavaScript" (MyFaces-70)
 *
 * Revision 1.4  2004/10/13 11:50:56  matze
 * renamed packages to org.apache
 *
 * Revision 1.3  2004/07/01 21:53:05  mwessendorf
 * ASF switch
 *
 * Revision 1.2  2004/05/18 14:31:36  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.1  2004/04/01 09:23:12  manolito
 * more extended components
 *
 */
public class HtmlInputText
        extends javax.faces.component.html.HtmlInputText
        implements UserRoleAware
{
        
    public String getClientId(FacesContext context)
    {
        String clientId = HtmlComponentUtils.getClientId(this, getRenderer(context), context);
        if (clientId == null)
        {
            clientId = super.getClientId(context);
        }
        
        return clientId;
    }
    
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlInputText";

    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;

    public HtmlInputText()
    {
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


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _enabledOnUserRole;
        values[2] = _visibleOnUserRole;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _enabledOnUserRole = (String)values[1];
        _visibleOnUserRole = (String)values[2];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

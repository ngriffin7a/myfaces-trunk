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
package net.sourceforge.myfaces.custom.datascroller;

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.5  2004/07/01 21:53:07  mwessendorf
 * ASF switch
 *
 * Revision 1.4  2004/06/23 13:44:30  royalts
 * no message
 *
 * Revision 1.3  2004/06/21 12:15:23  royalts
 * no message
 *
 * Revision 1.2  2004/06/21 09:46:58  royalts
 * no message
 *
 * Revision 1.1  2004/06/18 12:31:41  royalts
 * DataScroller implementation
 *
 * Revision 1.4  2004/05/18 14:31:37  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.3  2004/04/05 11:04:52  manolito
 * setter for renderer type removed, no more default renderer type needed
 *
 * Revision 1.2  2004/04/01 12:57:40  manolito
 * additional extended component classes for user role support
 *
 * Revision 1.1  2004/03/31 12:15:26  manolito
 * custom component refactoring
 *
 */
public class HtmlDataScrollerTag
        extends UIComponentTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlDataScrollerTag.class);

    private static final String FOR_ATTR = "for".intern();
    private static final String FAST_STEP_ATTR = "fastStep".intern();
    private static final String PAGE_INDEX_ATTR = "pageIndexVar".intern();
    private static final String PAGE_COUNT_ATTR = "pageCountVar".intern();



    private String _for;
    private String _fastStep;
    private String _pageIndexVar;
    private String _pageCountVar;


    // User Role support
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    public String getComponentType()
    {
        return HtmlDataScroller.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return HtmlDataScrollerRenderer.RENDERER_TYPE;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, FOR_ATTR, _for);
        setIntegerProperty(component, FAST_STEP_ATTR, _fastStep);
        setStringProperty(component, PAGE_INDEX_ATTR, _pageIndexVar);
        setStringProperty(component, PAGE_COUNT_ATTR, _pageCountVar);

        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    // datascroller attributes
    public void setFor(String aFor)
    {
        _for = aFor;
    }

    public void setFastStep(String fastStep)
    {
        _fastStep = fastStep;
    }

    public void setPageCountVar(String pageCountVar)
    {
        _pageCountVar = pageCountVar;
    }

    public void setPageIndexVar(String pageIndexVar)
    {
        _pageIndexVar = pageIndexVar;
    }


    // userrole attributes
    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }
}

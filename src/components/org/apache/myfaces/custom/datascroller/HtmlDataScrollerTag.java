/**
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
package net.sourceforge.myfaces.custom.datascroller;

import net.sourceforge.myfaces.component.UserRoleAware;
import net.sourceforge.myfaces.taglib.html.HtmlComponentBodyTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
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
        extends HtmlComponentBodyTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlDataScrollerTag.class);

    private static final String FOR_ATTR = "for".intern();
    private static final String FAST_STEP_ATTR = "fastStep".intern();



    private String _for;
    private String _fastStep;

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

        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    public void setFor(String aFor)
    {
        _for = aFor;
    }

    public void setFastStep(String fastStep)
    {
        _fastStep = fastStep;
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

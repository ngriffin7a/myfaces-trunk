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
package net.sourceforge.myfaces.component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.StringTokenizer;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/05/18 14:31:36  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.1  2004/03/31 11:58:33  manolito
 * custom component refactoring
 *
 */
public class UserRoleUtils
{
    //private static final Log log = LogFactory.getLog(UserRoleUtils.class);

    /**
     * Gets the comma separated list of visibility user roles from the given component
     * and checks if current user is in one of these roles.
     * @param component a user role aware component
     * @return true if no user roles are defined for this component or
     *              user is in one of these roles, false otherwise
     */
    public static boolean isVisibleOnUserRole(UIComponent component)
    {
        String userRole;
        if (component instanceof UserRoleAware)
        {
            userRole = ((UserRoleAware)component).getVisibleOnUserRole();
        }
        else
        {
            userRole = (String)component.getAttributes().get(UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR);
        }

        if (userRole == null)
        {
            // no restriction
            return true;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        StringTokenizer st = new StringTokenizer(userRole, ",");
        while (st.hasMoreTokens())
        {
            if (facesContext.getExternalContext().isUserInRole(st.nextToken().trim()))
            {
                return true;
            }
        }
        return false;
    }


    /**
     * Gets the comma separated list of enabling user roles from the given component
     * and checks if current user is in one of these roles.
     * @param component a user role aware component
     * @return true if no user roles are defined for this component or
     *              user is in one of these roles, false otherwise
     */
    public static boolean isEnabledOnUserRole(UIComponent component)
    {
        String userRole;
        if (component instanceof UserRoleAware)
        {
            userRole = ((UserRoleAware)component).getEnabledOnUserRole();
        }
        else
        {
            userRole = (String)component.getAttributes().get(UserRoleAware.ENABLED_ON_USER_ROLE_ATTR);
        }

        if (userRole == null)
        {
            // no restriction
            return true;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        StringTokenizer st = new StringTokenizer(userRole, ",");
        while (st.hasMoreTokens())
        {
            if (facesContext.getExternalContext().isUserInRole(st.nextToken().trim()))
            {
                return true;
            }
        }
        return false;
    }

}

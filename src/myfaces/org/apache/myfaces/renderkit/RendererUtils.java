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
package net.sourceforge.myfaces.renderkit;

import net.sourceforge.myfaces.component.UserRoleSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.ConvertibleValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RendererUtils
{
    private static final Log log = LogFactory.getLog(RendererUtils.class);
    
    public static String getStringValue(FacesContext facesContext,
                                        UIComponent component)
    {
        if (!(component instanceof ConvertibleValueHolder))
        {
            throw new IllegalArgumentException("Not a ConvertibleValueHolder");
        }

        Object value = ((ConvertibleValueHolder)component).getValue();

        Converter converter = ((ConvertibleValueHolder)component).getConverter();
        if (converter == null && value != null)
        {
            try
            {
                converter = facesContext.getApplication().createConverter(value.getClass());
            }
            catch (FacesException e)
            {
                if (log.isWarnEnabled()) log.warn("No converter for class " + value.getClass().getName() + " found (component id=" + component.getId() + ").");
                converter = null;
            }
        }

        if (converter == null)
        {
            if (value == null)
            {
                return "";
            }
            else
            {
                return value.toString();
            }
        }
        else
        {
            return converter.getAsString(facesContext, (UIComponent)component, value);
        }
    }



    public static boolean isEnabledOnUserRole(FacesContext facesContext, UIComponent component)
    {
        String userRole;
        if (component instanceof UserRoleSupport)
        {
            userRole = ((UserRoleSupport)component).getEnabledOnUserRole();
        }
        else
        {
            userRole = (String)component.getAttributes().get(JSFAttr.ENABLED_ON_USER_ROLE_ATTR);
        }

        if (userRole == null)
        {
            //no restriction
            return true;
        }
        else
        {
            return facesContext.getExternalContext().isUserInRole(userRole);
        }
    }


    public static boolean isVisibleOnUserRole(FacesContext facesContext, UIComponent component)
    {
        String userRole;
        if (component instanceof UserRoleSupport)
        {
            userRole = ((UserRoleSupport)component).getVisibleOnUserRole();
        }
        else
        {
            userRole = (String)component.getAttributes().get(JSFAttr.VISIBLE_ON_USER_ROLE_ATTR);
        }

        if (userRole == null)
        {
            //no restriction
            return true;
        }
        else
        {
            return facesContext.getExternalContext().isUserInRole(userRole);
        }
    }


}

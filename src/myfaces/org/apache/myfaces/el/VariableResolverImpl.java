/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.el;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.config.ConfigUtil;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.config.ManagedBeanConfig;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * JSF 1.0 PRD2, 5.2.1
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class VariableResolverImpl
    extends VariableResolver
{
    public Object resolveVariable(FacesContext facesContext, String name)
    {
        Object obj;

        //Request context
        ServletRequest servletrequest
            = (ServletRequest)facesContext.getExternalContext().getRequest();
        obj = servletrequest.getAttribute(name);
        if (obj != null)
        {
            return obj;
        }

        //Session context
        if (servletrequest instanceof HttpServletRequest)
        {
            HttpSession session = ((HttpServletRequest)servletrequest).getSession(false);
            if (session != null)
            {
                obj = session.getAttribute(name);
                if (obj != null)
                {
                    return obj;
                }
            }
        }

        //Application context
        ServletContext servletcontext
            = (ServletContext)facesContext.getExternalContext().getContext();
        obj = servletcontext.getAttribute(name);
        if (obj != null)
        {
            return obj;
        }

        //ManagedBean
        FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(servletcontext);
        FacesConfig facesConfig = fcf.getFacesConfig(servletcontext);
        ManagedBeanConfig mbc = facesConfig.getManagedBeanConfig(name);
        if (mbc != null)
        {
            obj = ConfigUtil.newInstance(mbc.getManagedBeanClass());
            String scope = mbc.getManagedBeanScope();
            if (scope.equals("request"))
            {
                servletrequest.setAttribute(name, obj);
            }
            else if (scope.equals("session"))
            {
                HttpSession session = ((HttpServletRequest)servletrequest).getSession(true);
                session.setAttribute(name, obj);
            }
            else if (scope.equals("application"))
            {
                servletcontext.setAttribute(name, obj);
            }
            else if (!scope.equals("none"))
            {
                LogUtil.getLogger().severe("Managed bean '" + name + "' has illegal scope: " + scope);
            }

            //TODO: managed bean properties
        }

        return null;
    }
}

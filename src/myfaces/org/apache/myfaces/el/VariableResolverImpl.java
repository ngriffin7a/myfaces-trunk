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
import net.sourceforge.myfaces.config.*;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * JSF 1.0 PRD2, 5.2.1
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class VariableResolverImpl
    extends VariableResolver
{
    public Object resolveVariable(FacesContext facesContext, String name)
    {
    	if (name == null || name.length() == 0)
    		throw new ReferenceSyntaxException("Varible name is null");
    	
        Object obj;

        //Implicit objects
        obj = getImplicitObject(facesContext, name);
        if (obj != null)
        {
            return obj;
        }

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
        // REVISIT: shouldn't we get the registered factory finder?
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

            setManagedBeanProperties(facesContext, obj, mbc);

            return obj;
        }

        LogUtil.getLogger().info("Variable '" + name + "' could not be resolved.");
        return null;
    }


    private void setManagedBeanProperties(FacesContext facesContext,
                                          Object bean,
                                          ManagedBeanConfig mbc)
    {
    	List managedPropertyConfigList = mbc.getManagedPropertyConfigList();
        for (int i = 0, len = managedPropertyConfigList.size(); i < len; i++)
        {
            ManagedPropertyConfig propConfig = 
            	(ManagedPropertyConfig)managedPropertyConfigList.get(i);

            if (propConfig.getMapEntriesConfig() != null)
            {
                setMapEntries(bean, propConfig);
            }
            else if (propConfig.getValuesConfig() != null)
            {
                setValues(bean, propConfig);
            }

            Object value;
            if (propConfig.getValueRef() != null)
            {
                ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                ValueBinding vb = af.getApplication().getValueBinding(propConfig.getValueRef());
                value = vb.getValue(facesContext);
            }
            else
            {
                value = propConfig.getValue();
            }

            BeanUtils.setBeanPropertyValue(bean,
                                           propConfig.getPropertyName(),
                                           value);
        }
    }

    private void setMapEntries(Object bean,
                               ManagedPropertyConfig propConfig)
    {
        throw new UnsupportedOperationException("Not yet implemented"); //TODO
    }

    private void setValues(Object bean,
                           ManagedPropertyConfig propConfig)
    {
        throw new UnsupportedOperationException("Not yet implemented"); //TODO
    }



    protected Object getImplicitObject(FacesContext facesContext, String name)
    {
        if (name.equals("applicationScope"))
        {
            return facesContext.getExternalContext().getApplicationMap();
        }

        if (name.equals("cookie"))
        {
            return facesContext.getExternalContext().getRequestCookieMap();
        }

        if (name.equals("facesContext"))
        {
            return facesContext;
        }

        if (name.equals("header"))
        {
            return facesContext.getExternalContext().getRequestHeaderMap();
        }

        if (name.equals("headerValues"))
        {
            return facesContext.getExternalContext().getRequestHeaderValuesMap();
        }

        if (name.equals("initParam"))
        {
            return facesContext.getExternalContext().getInitParameterMap();
        }

        if (name.equals("param"))
        {
            return facesContext.getExternalContext().getRequestParameterMap();
        }

        if (name.equals("paramValues"))
        {
            return facesContext.getExternalContext().getRequestParameterValuesMap();
        }

        if (name.equals("requestScope"))
        {
            return facesContext.getExternalContext().getRequestMap();
        }

        if (name.equals("sessionScope"))
        {
            return facesContext.getExternalContext().getSessionMap();
        }

        if (name.equals("tree"))
        {
            return facesContext.getTree().getRoot();
        }

        return null;
    }

}

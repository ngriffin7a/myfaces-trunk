/*
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
import net.sourceforge.myfaces.util.FacesUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.VariableResolver;

import javax.servlet.ServletContext;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class VariableResolverImpl
    extends VariableResolver
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Log log              = LogFactory.getLog(VariableResolverImpl.class);
    
    // WARNING: this implementation is thread safe because is does not update/add 
    //          to IMPLICIT_OBJECTS and SCOPES. If you need to add your own implicit objects/scopes,
    //          either extend and add more in a static block, or add proper sychronization
    static final Map         IMPLICIT_OBJECTS = new HashMap(32);
    static final Map         SCOPES           = new HashMap(16);

    static
    {
        IMPLICIT_OBJECTS.put(
            "applicationScope",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getApplicationMap();
                }
            });
        IMPLICIT_OBJECTS.put(
            "cookie",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestCookieMap();
                }
            });
        IMPLICIT_OBJECTS.put(
            "facesContext",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext;
                }
            });
        IMPLICIT_OBJECTS.put(
            "header",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestHeaderMap();
                }
            });
        IMPLICIT_OBJECTS.put(
            "headerValues",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestHeaderValuesMap();
                }
            });
        IMPLICIT_OBJECTS.put(
            "initParam",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getInitParameterMap();
                }
            });
        IMPLICIT_OBJECTS.put(
            "param",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestParameterMap();
                }
            });
        IMPLICIT_OBJECTS.put(
            "paramValues",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestParameterValuesMap();
                }
            });
        IMPLICIT_OBJECTS.put(
            "requestScope",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestMap();
                }
            });
        IMPLICIT_OBJECTS.put(
            "sessionScope",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getSessionMap();
                }
            });
        IMPLICIT_OBJECTS.put(
            "view",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getViewRoot();
                }
            });

        SCOPES.put(
            "request",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    extContext.getRequestMap().put(name, obj);
                }
            });
        SCOPES.put(
            "session",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    extContext.getSessionMap().put(name, obj);
                }
            });
        SCOPES.put(
            "application",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    ((ServletContext) extContext.getContext()).setAttribute(name, obj);
                }
            });
        SCOPES.put(
            "none",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    // do nothing
                }
            });
    }

    //~ Methods ------------------------------------------------------------------------------------

    public Object resolveVariable(FacesContext facesContext, String name)
    {
        if ((name == null) || (name.length() == 0))
        {
            throw new ReferenceSyntaxException("Varible name is null");
        }

        //Implicit objects
        ImplicitObject implicitObject = (ImplicitObject) IMPLICIT_OBJECTS.get(name);

        if (implicitObject != null)
        {
            return implicitObject.get(facesContext);
        }

        ExternalContext extContext = facesContext.getExternalContext();

        //Request context
        Map    requestMap = extContext.getRequestMap();
        Object obj = requestMap.get(name);

        if (obj != null)
        {
            return obj;
        }

        //Session context (try to get without creating a new session)
        Object session = extContext.getSession(false);

        if (session != null)
        {
            obj = extContext.getSessionMap().get(name);

            if (obj != null)
            {
                return obj;
            }
        }

        //Application context
        ServletContext servletContext = (ServletContext) extContext.getContext();
        obj = servletContext.getAttribute(name);

        if (obj != null)
        {
            return obj;
        }

        //ManagedBean
        FacesConfigFactory fcf         = MyFacesFactoryFinder.getFacesConfigFactory(servletContext);
        FacesConfig        facesConfig = fcf.getFacesConfig(servletContext);
        ManagedBeanConfig  mbc         = facesConfig.getManagedBeanConfig(name);

        if (mbc != null)
        {
            obj = ConfigUtil.newInstance(mbc.getManagedBeanClass());

            setManagedBeanProperties(facesContext, obj, mbc);

            Scope scope = (Scope) SCOPES.get(mbc.getManagedBeanScope());

            if (scope != null)
            {
                scope.put(extContext, name, obj);
            }
            else
            {
                log.error("Managed bean '" + name + "' has illegal scope: " + scope);
            }

            return obj;
        }

        log.warn("Variable '" + name + "' could not be resolved.");

        return null;
    }

    protected void setManagedBeanProperties(
        FacesContext facesContext, Object bean, ManagedBeanConfig mbc)
    {
        List managedPropertyConfigList = mbc.getManagedPropertyConfigList();

        if (managedPropertyConfigList == null)
        {
            return;
        }

        for (int i = 0, len = managedPropertyConfigList.size(); i < len; i++)
        {
            ManagedPropertyConfig propConfig =
                (ManagedPropertyConfig) managedPropertyConfigList.get(i);

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
                value = FacesUtils.getValueRef(
                        facesContext,
                        propConfig.getValueRef());
            }
            else
            {
                value = propConfig.getValue();
            }

            PropertyResolverImpl.setProperty(
                bean,
                propConfig.getPropertyName(),
                value);
        }
    }

    protected void setMapEntries(Object bean, ManagedPropertyConfig propConfig)
    {
        throw new UnsupportedOperationException("Not yet implemented"); // TODO
    }

    protected void setValues(Object bean, ManagedPropertyConfig propConfig)
    {
        throw new UnsupportedOperationException("Not yet implemented"); // TODO
    }
}


interface ImplicitObject
{
    //~ Methods ------------------------------------------------------------------------------------

    public Object get(FacesContext facesContext);
}


interface Scope
{
    //~ Methods ------------------------------------------------------------------------------------

    public void put(ExternalContext extContext, String name, Object obj);
}

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
package net.sourceforge.myfaces.el;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.config.ManagedBeanConfig;
import net.sourceforge.myfaces.config.configure.ManagedBeanConfigurator;
import net.sourceforge.myfaces.util.ClassUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.VariableResolver;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.27  2004/05/12 07:57:43  manolito
 * Log in javadoc header
 *
 */
public class VariableResolverImpl
    extends VariableResolver
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Log log              = LogFactory.getLog(VariableResolverImpl.class);

    //~ Instance fields ----------------------------------------------------------------------------

    public static final Map s_standardImplicitObjects = new HashMap(32);
    static {
        s_standardImplicitObjects.put(
            "applicationScope",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getApplicationMap();
                }
            });
        s_standardImplicitObjects.put(
            "cookie",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestCookieMap();
                }
            });
        s_standardImplicitObjects.put(
            "facesContext",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext;
                }
            });
        s_standardImplicitObjects.put(
            "header",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestHeaderMap();
                }
            });
        s_standardImplicitObjects.put(
            "headerValues",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestHeaderValuesMap();
                }
            });
        s_standardImplicitObjects.put(
            "initParam",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getInitParameterMap();
                }
            });
        s_standardImplicitObjects.put(
            "param",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestParameterMap();
                }
            });
        s_standardImplicitObjects.put(
            "paramValues",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestParameterValuesMap();
                }
            });
        s_standardImplicitObjects.put(
            "requestScope",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestMap();
                }
            });
        s_standardImplicitObjects.put(
            "sessionScope",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getSessionMap();
                }
            });
        s_standardImplicitObjects.put(
            "view",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getViewRoot();
                }
            });
    }
    
    /**
     * Stores all implicit objects defined for this instance of <code>VariableResolver</code>
     * <p>
     * Can store instances of <code>ImplicitObject</code> which have the ability to
     * dynamically resolve against FacesContext. Can also store any other object
     * which itself is the value for the implicit object (this in effect will be
     * a static object).
     * </p>
     * <p>
     * WARNING: this implementation is not serialized as it is thread safe because
     *          it does not update/add to _implicitObjects after object initialization.
     *          If you need to add your own implicit objects, either extend and add more
     *          in an initialization block, or add proper sychronization
     * </p>
     */
    protected final Map _implicitObjects = new HashMap(32);
    {
        _implicitObjects.putAll(s_standardImplicitObjects);
    }

    protected static final Map s_standardScopes = new HashMap(16);
    static {
        s_standardScopes.put(
            "request",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    extContext.getRequestMap().put(name, obj);
                }
            });
        s_standardScopes.put(
            "session",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    extContext.getSessionMap().put(name, obj);
                }
            });
        s_standardScopes.put(
            "application",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    extContext.getApplicationMap().put(name, obj);
                }
            });
        s_standardScopes.put(
            "none",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    // do nothing
                }
            });
    }
    
    /**
     * Stores all scopes defined for this instance of <code>VariableResolver</code>
     * <p>
     * Can store instances of <code>Scope</code> which have the ability to
     * dynamically resolve against ExternalContext for put operations.
     * </p>
     * <p>
     * WARNING: this implementation is not serialized as it is thread safe because
     *          it does not update/add to _scopes after object initialization.
     *          If you need to add your own scopes, either extend and add more
     *          in an initialization block, or add proper sychronization
     * </p>
     */
    protected final Map _scopes = new HashMap(16);
    {
        _scopes.putAll(s_standardScopes);
    }

    /**
     * FacesConfig is instantiated once per servlet and never changes--we can
     * safely cache it
     */
    private FacesConfig _facesConfig;
    
    //~ Methods ---------------------------------------------------------------

    public Object resolveVariable(FacesContext facesContext, String name)
    {
        if ((name == null) || (name.length() == 0))
        {
            throw new ReferenceSyntaxException("Varible name is null or empty");
        }

        // Implicit objects
        Object implicitObject = _implicitObjects.get(name);
        if (implicitObject != null)
        {
            if (implicitObject instanceof ImplicitObject)
            {
                // a complex runtime object
                return ((ImplicitObject) implicitObject).get(facesContext);
            }
            else
            {
                // a simple object
                return implicitObject;
            }
        }

        ExternalContext externalContext = facesContext.getExternalContext();

        // Request context
        Map    requestMap = externalContext.getRequestMap();
        Object obj = requestMap.get(name);
        if (obj != null)
        {
            return obj;
        }

        // Session context (try to get without creating a new session)
        Object session = externalContext.getSession(false);
        if (session != null)
        {
            obj = externalContext.getSessionMap().get(name);
            if (obj != null)
            {
                return obj;
            }
        }

        // Application context
        obj = externalContext.getApplicationMap().get(name);
        if (obj != null)
        {
            return obj;
        }

        // ManagedBean
        ManagedBeanConfig mbc = 
            getFacesConfig(facesContext).getManagedBeanConfig(name);
        if (mbc != null)
        {
            obj = ClassUtils.newInstance(mbc.getManagedBeanClass());
            ManagedBeanConfigurator configurator = 
                ManagedBeanConfigurator.getInstance();
            configurator.configure(facesContext, mbc, obj);

            // put in scope
            String scopeKey = mbc.getManagedBeanScope();
            
            // find the scope handler object
            Scope scope = (Scope) _scopes.get(scopeKey);
            if (scope == null)
            {
                log.error("Managed bean '" + name + "' has illegal scope: "
                    + scopeKey);
            }
            else
            {
                scope.put(externalContext, name, obj);
            }

            return obj;
        }

        log.error("Variable '" + name + "' could not be resolved.");
        return null;
    }
    
    protected FacesConfig getFacesConfig(FacesContext facesContext)
    {
        if (_facesConfig == null)
        {
            ExternalContext externalContext = 
                facesContext.getExternalContext();
            FacesConfigFactory facesConfigFactory =
                MyFacesFactoryFinder.getFacesConfigFactory(externalContext);
            _facesConfig = facesConfigFactory.getFacesConfig(externalContext);
        }
        return _facesConfig;
    }
}


interface ImplicitObject
{
    //~ Methods ---------------------------------------------------------------

    public Object get(FacesContext facesContext);
}


interface Scope
{
    //~ Methods ---------------------------------------------------------------

    public void put(ExternalContext extContext, String name, Object obj);
}

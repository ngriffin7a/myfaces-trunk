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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.VariableResolver;


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

    //~ Instance fields ----------------------------------------------------------------------------

    /**
     * Stores all implicit objects defined for this instance of <code>VariableResolver</code>
     * <p>
     * Can store instances of <code>ImplicitObject</code> which have the ability to
     * dynamically resolve against FacesContext. Can also store any other abject
     * which itself is the value for the implicit object (this is effect will be
     * a static object)
     * </p>
     * <p>
     * WARNING: this implementation is not serialized as it is thread safe because
     *          it does not update/add to _implicitObjects after object initialization.
     *          If you need to add your own implicit objects, either extend and add more
     *          in an initialization block, or add proper sychronization
     * </p>
     */
    protected final Map _implicitObjects = new HashMap(32);

    /**
     * Stores all scopes defined for this instance of <code>VariableResolver</code>
     * <p>
     * Can store instances of <code>Scope</code> which have the ability to
     * dynamically resolve against ExterncalContext.
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
        _implicitObjects.put(
            "applicationScope",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getApplicationMap();
                }
            });
        _implicitObjects.put(
            "cookie",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestCookieMap();
                }
            });
        _implicitObjects.put(
            "facesContext",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext;
                }
            });
        _implicitObjects.put(
            "header",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestHeaderMap();
                }
            });
        _implicitObjects.put(
            "headerValues",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestHeaderValuesMap();
                }
            });
        _implicitObjects.put(
            "initParam",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getInitParameterMap();
                }
            });
        _implicitObjects.put(
            "param",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestParameterMap();
                }
            });
        _implicitObjects.put(
            "paramValues",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestParameterValuesMap();
                }
            });
        _implicitObjects.put(
            "requestScope",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getRequestMap();
                }
            });
        _implicitObjects.put(
            "sessionScope",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getExternalContext().getSessionMap();
                }
            });
        _implicitObjects.put(
            "view",
            new ImplicitObject()
            {
                public Object get(FacesContext facesContext)
                {
                    return facesContext.getViewRoot();
                }
            });

        _scopes.put(
            "request",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    extContext.getRequestMap().put(name, obj);
                }
            });
        _scopes.put(
            "session",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    extContext.getSessionMap().put(name, obj);
                }
            });
        _scopes.put(
            "application",
            new Scope()
            {
                public void put(ExternalContext extContext, String name, Object obj)
                {
                    extContext.getApplicationMap().put(name, obj);
                }
            });
        _scopes.put(
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
        FacesConfigFactory fcf         =
            MyFacesFactoryFinder.getFacesConfigFactory(externalContext);
        FacesConfig        facesConfig = fcf.getFacesConfig(externalContext);
        ManagedBeanConfig  mbc         = facesConfig.getManagedBeanConfig(name);
        if (mbc != null)
        {
            return mbc.createBean(facesContext);
        }

        log.warn("Variable '" + name + "' could not be resolved.");
        return null;
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

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
package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.el.MethodBindingImpl;
import net.sourceforge.myfaces.el.ValueBindingImpl;
import net.sourceforge.myfaces.util.BiLevelCacheMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.*;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class ApplicationImpl
    extends Application
{
    private static final Log log = LogFactory.getLog(ApplicationImpl.class);

    //~ Instance fields ----------------------------------------------------------------------------

    private final FacesConfig    _facesConfig;
    private final Map            _valueBindingCache =
        new BiLevelCacheMap(256, 128, 100)
        {
            protected Object newInstance(Object key)
            {
                return new ValueBindingImpl(ApplicationImpl.this, (String) key);
            }
        };

    private final ExternalContext _externalContext;
    private Collection           _supportedLocales;
    private Locale               _defaultLocale;
    private String               _messageBundle;
    private ViewHandler          _viewHandler;

    //~ Constructors -------------------------------------------------------------------------------

    public ApplicationImpl(ExternalContext externalContext)
    {
        _externalContext = externalContext;

        FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(_externalContext);
        _facesConfig = fcf.getFacesConfig(_externalContext);
        if (log.isTraceEnabled()) log.trace("New Application instance created");
    }

    //~ Methods ------------------------------------------------------------------------------------

    public void setActionListener(ActionListener actionListener)
    {
        if (actionListener == null)
        {
            throw new NullPointerException("actionListener");
        }
        getFacesConfig().getApplicationConfig().setActionListener(actionListener);
    }

    public ActionListener getActionListener()
    {
        return getFacesConfig().getApplicationConfig().getActionListener();
    }

    public Iterator getComponentTypes()
    {
        return getFacesConfig().getComponentTypes();
    }

    public Iterator getConverterIds()
    {
        return getFacesConfig().getConverterIds();
    }

    public Iterator getConverterTypes()
    {
        return getFacesConfig().getConverterTypes();
    }

    public void setDefaultLocale(Locale locale)
    {
        if (locale == null)
        {
            log.error("setting locale to null is not allowed");
            throw new NullPointerException("locale");
        }
        _defaultLocale = locale;
    }

    public Locale getDefaultLocale()
    {
        return _defaultLocale;
    }

    public void setMessageBundle(String messageBundle)
    {
        if (messageBundle == null)
        {
            log.error("setting messageBundle to null is not allowed");
            throw new NullPointerException("messageBundle");
        }
        _messageBundle = messageBundle;
    }

    public String getMessageBundle()
    {
        return _messageBundle;
    }

    public void setNavigationHandler(NavigationHandler navigationHandler)
    {
        if (navigationHandler == null)
        {
            log.error("setting navigationHandler to null is not allowed");
            throw new NullPointerException("navigationHandler");
        }
        getFacesConfig().getApplicationConfig().setNavigationHandler(navigationHandler);
    }

    public NavigationHandler getNavigationHandler()
    {
        return getFacesConfig().getApplicationConfig().getNavigationHandler();
    }

    public void setPropertyResolver(PropertyResolver propertyResolver)
    {
        if (propertyResolver == null)
        {
            log.error("setting propertyResolver to null is not allowed");
            throw new NullPointerException("propertyResolver");
        }
        getFacesConfig().getApplicationConfig().setPropertyResolver(propertyResolver);
    }

    public PropertyResolver getPropertyResolver()
    {
        return getFacesConfig().getApplicationConfig().getPropertyResolver();
    }

    public void setSupportedLocales(Collection locales)
    {
        if (locales == null)
        {
            log.error("setting supportedLocales to null is not allowed");
            throw new NullPointerException("locales");
        }
        _supportedLocales = locales;
    }

    public Iterator getSupportedLocales()
    {
        return _supportedLocales.iterator();
    }

    public Iterator getValidatorIds()
    {
        return getFacesConfig().getValidatorIds();
    }

    public void setVariableResolver(VariableResolver variableResolver)
    {
        if (variableResolver == null)
        {
            log.error("setting variableResolver to null is not allowed");
            throw new NullPointerException("variableResolver");
        }
        getFacesConfig().getApplicationConfig().setVariableResolver(variableResolver);
    }

    public VariableResolver getVariableResolver()
    {
        return getFacesConfig().getApplicationConfig().getVariableResolver();
    }

    public void setViewHandler(ViewHandler viewHandler)
    {
        if (viewHandler == null)
        {
            log.error("setting viewHandler to null is not allowed");
            throw new NullPointerException("viewHandler");
        }
        _viewHandler = viewHandler;
    }

    public ViewHandler getViewHandler()
    {
        return _viewHandler;
    }

    public void addComponent(String componentType, String componentClass)
    {
        if ((componentType == null) || (componentType.length() == 0))
        {
            log.error("addComponent: componentType = null ist not allowed");
            throw new NullPointerException("componentType");
        }
        if ((componentClass == null) || (componentClass.length() == 0))
        {
            log.error("addComponent: component = null is not allowed");
            throw new NullPointerException("componentClass");
        }
        getFacesConfig().addComponent(componentType, componentClass);
    }

    public void addConverter(String converterId, String converterClass)
    {
        if ((converterId == null) || (converterId.length() == 0))
        {
            log.error("addConverter: converterId = null ist not allowed");
            throw new NullPointerException("converterId");
        }
        if ((converterClass == null) || (converterClass.length() == 0))
        {
            log.error("addConverter: converterClass = null ist not allowed");
            throw new NullPointerException("converterClass");
        }
        getFacesConfig().addConverter(converterId, converterClass);
    }

    public void addConverter(Class targetClass, String converterClass)
    {
        if ((targetClass == null))
        {
            log.error("addConverter: targetClass = null ist not allowed");
            throw new NullPointerException("targetClass");
        }
        if ((converterClass == null) || (converterClass.length() == 0))
        {
            log.error("addConverter: converterClass = null ist not allowed");
            throw new NullPointerException("converterClass");
        }
        getFacesConfig().addConverter(targetClass, converterClass);
    }

    public void addValidator(String validatorId, String validatorClass)
    {
        if ((validatorId == null) || (validatorId.length() == 0))
        {
            log.error("addValidator: validatorId = null ist not allowed");
            throw new NullPointerException("validatorId");
        }
        if ((validatorClass == null) || (validatorClass.length() == 0))
        {
            log.error("addValidator:  validatorClass = null ist not allowed");
            throw new NullPointerException("validatorClass");
        }
        getFacesConfig().addValidator(validatorId, validatorClass);
    }

    public UIComponent createComponent(String componentType)
    throws FacesException
    {
        if ((componentType == null) || (componentType.length() == 0))
        {
            log.error("createComponent: componentType = null is not allowed");
            throw new NullPointerException("componentType");
        }
        return getFacesConfig().getComponent(componentType);
    }

    public UIComponent createComponent(
        ValueBinding valueBinding, FacesContext facesContext, String componentType)
    throws FacesException
    {
        if ((valueBinding == null))
        {
            log.error("createComponent: valueBinding = null ist not allowed");
            throw new NullPointerException("valueBinding");
        }
        if ((facesContext == null))
        {
            log.error("createComponent: facesContext = null ist not allowed");
            throw new NullPointerException("facesContext");
        }
        if ((componentType == null) || (componentType.length() == 0))
        {
            log.error("createComponent: componentType = null ist not allowed");
            throw new NullPointerException("componentType");
        }

        Object obj = valueBinding.getValue(facesContext);
        if (obj instanceof UIComponent)
        {
            return (UIComponent) obj;
        }

        UIComponent component = createComponent(componentType);
        valueBinding.setValue(facesContext, component);
        return component;
    }

    public Converter createConverter(String converterId)
    {
        if ((converterId == null) || (converterId.length() == 0))
        {
            log.error("createConverter: converterId = null ist not allowed");
            throw new NullPointerException("converterId");
        }
        return getFacesConfig().getConverter(converterId);
    }

    public Converter createConverter(Class targetClass)
    {
        if (targetClass == null)
        {
            log.error("createConverter: targetClass = null ist not allowed");
            throw new NullPointerException("targetClass");
        }
        return getFacesConfig().getConverter(targetClass);
    }

    public synchronized MethodBinding createMethodBinding(String reference, Class[] params)
    throws ReferenceSyntaxException
    {
        if ((reference == null) || (reference.length() == 0))
        {
            log.error("createMethodBinding: reference = null ist not allowed");
            throw new NullPointerException("reference");
        }

        // We choose to instantiate a new MethodBinding every time as this is much easier 
        // and about as efficient as implementing a cache specifically for MethodBinding,
        // which is complicated by the need to use a conposite key=(reference, params)
        // (significant part of MethodBinding is already cached by ValueBinding implicitly)
        return new MethodBindingImpl(this, reference, params);
    }

    public Validator createValidator(String validatorId)
    throws FacesException
    {
        if ((validatorId == null) || (validatorId.length() == 0))
        {
            log.error("createValidator: validatorId = null ist not allowed");
            throw new NullPointerException("validatorId");
        }
        return getFacesConfig().getValidator(validatorId);
    }

    public ValueBinding createValueBinding(String reference)
    throws ReferenceSyntaxException
    {
        if ((reference == null) || (reference.length() == 0))
        {
            log.error("createValueBinding: reference = null ist not allowed");
            throw new NullPointerException("reference");
        }
        return (ValueBinding) _valueBindingCache.get(reference);
    }

    protected FacesConfig getFacesConfig()
    {
//        Moved _facesConfig initialization to the constructor, hope that works better
//        
//        if (_facesConfig == null)
//        {
//            FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(_servletContext);
//            _facesConfig = fcf.getFacesConfig(_servletContext);
//        }
        return _facesConfig;
    }
}

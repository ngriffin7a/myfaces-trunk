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
package net.sourceforge.myfaces.application;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.el.MethodBindingImpl;
import net.sourceforge.myfaces.el.ValueBindingImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;

import javax.servlet.ServletContext;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ApplicationImpl
    extends Application
{
    //~ Instance fields ----------------------------------------------------------------------------

    private final FacesConfig    _facesConfig;
    private final Map            _methodBindingCache = new HashMap();
    private final Map            _valueBindingCache  = new HashMap();
    private final ServletContext _servletContext;
    private Collection           _supportedLocales;
    private Locale               _defaultLocale;
    private String               _messageBundle;
    private ViewHandler          _viewHandler;

    //~ Constructors -------------------------------------------------------------------------------

    public ApplicationImpl(ServletContext servletContext)
    {
        _servletContext = servletContext;

        FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(_servletContext);
        _facesConfig = fcf.getFacesConfig(_servletContext);
    }

    //~ Methods ------------------------------------------------------------------------------------

    public void setActionListener(ActionListener actionListener)
    {
        if (actionListener == null)
        {
            throw new NullPointerException();
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
        return getFacesConfig().getConverterTypes();
    }

    public Iterator getConverterTypes()
    {
        return getFacesConfig().getConverterIds();
    }

    public void setDefaultLocale(Locale locale)
    {
        if (locale == null)
        {
            throw new NullPointerException();
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
            throw new NullPointerException();
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
            throw new NullPointerException();
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
            throw new NullPointerException();
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
            throw new NullPointerException();
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
            throw new NullPointerException();
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
            throw new NullPointerException();
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
            throw new NullPointerException("componentType");
        }

        if ((componentClass == null) || (componentClass.length() == 0))
        {
            throw new NullPointerException("componentClass");
        }

        getFacesConfig().addComponent(componentType, componentClass);
    }

    public void addConverter(String converterId, String converterClass)
    {
        if ((converterId == null) || (converterId.length() == 0))
        {
            throw new NullPointerException("converterId");
        }

        if ((converterClass == null) || (converterClass.length() == 0))
        {
            throw new NullPointerException("converterClass");
        }

        getFacesConfig().addConverter(converterId, converterClass);
    }

    public void addConverter(Class targetClass, String converterClass)
    {
        if ((targetClass == null))
        {
            throw new NullPointerException("targetClass");
        }

        if ((converterClass == null) || (converterClass.length() == 0))
        {
            throw new NullPointerException("converterClass");
        }

        // FIXME Auto-generated method stub
    }

    public void addValidator(String validatorId, String validatorClass)
    {
        if ((validatorId == null) || (validatorId.length() == 0))
        {
            throw new NullPointerException("validatorId");
        }

        if ((validatorClass == null) || (validatorClass.length() == 0))
        {
            throw new NullPointerException("validatorClass");
        }

        getFacesConfig().addValidator(validatorId, validatorClass);
    }

    public UIComponent createComponent(String componentType)
    throws FacesException
    {
        if ((componentType == null) || (componentType.length() == 0))
        {
            throw new NullPointerException("converterClass");
        }

        return getFacesConfig().getComponent(componentType);
    }

    public UIComponent createComponent(
        ValueBinding valueBinding, FacesContext facesContext, String componentType)
    throws FacesException
    {
        if ((valueBinding == null))
        {
            throw new NullPointerException("valueBinding");
        }

        if ((facesContext == null))
        {
            throw new NullPointerException("facesContext");
        }

        if ((componentType == null) || (componentType.length() == 0))
        {
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
            throw new NullPointerException("componentType");
        }

        return getFacesConfig().getConverter(converterId);
    }

    public Converter createConverter(Class targetClass)
    {
        if (targetClass == null)
        {
            throw new NullPointerException();
        }

        // FIXME Auto-generated method stub
        return null;
    }

    public synchronized MethodBinding createMethodBinding(String reference, Class[] params)
    throws ReferenceSyntaxException
    {
        if ((reference == null) || (reference.length() == 0))
        {
            throw new NullPointerException("reference");
        }

        // TODO: redesign to avoid synchronization issues
        // FIXME: this caching does not work when overriden functions with different params are used
        MethodBinding mb = (MethodBinding) _methodBindingCache.get(reference);

        if (mb == null)
        {
            mb = new MethodBindingImpl(this, reference, params);
            _valueBindingCache.put(reference, mb);
        }

        return mb;
    }

    public Validator createValidator(String validatorId)
    throws FacesException
    {
        if ((validatorId == null) || (validatorId.length() == 0))
        {
            throw new NullPointerException("validatorId");
        }

        return getFacesConfig().getValidator(validatorId);
    }

    public synchronized ValueBinding createValueBinding(String reference)
    throws ReferenceSyntaxException
    {
        if ((reference == null) || (reference.length() == 0))
        {
            throw new NullPointerException("reference");
        }

        // TODO: redesign to avoid synchronization issues
        ValueBinding vb = (ValueBinding) _valueBindingCache.get(reference);

        if (vb == null)
        {
            // Note: we cannot cache VariableResolver and PropertyResolver directly in ValueBinding since those can change through the set methods
            vb = new ValueBindingImpl(reference, this);
            _valueBindingCache.put(reference, vb);
        }

        return vb;
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

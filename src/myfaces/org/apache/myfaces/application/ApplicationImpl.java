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

import net.sourceforge.myfaces.application.jsp.JspStateManagerImpl;
import net.sourceforge.myfaces.application.jsp.JspViewHandlerImpl;
import net.sourceforge.myfaces.el.MethodBindingImpl;
import net.sourceforge.myfaces.el.PropertyResolverImpl;
import net.sourceforge.myfaces.el.ValueBindingImpl;
import net.sourceforge.myfaces.el.VariableResolverImpl;
import net.sourceforge.myfaces.util.BiLevelCacheMap;
import net.sourceforge.myfaces.util.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.*;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import java.util.*;


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

    private final Map            _valueBindingCache =
        new BiLevelCacheMap(90)
        {
            protected Object newInstance(Object key)
            {
                return new ValueBindingImpl(ApplicationImpl.this, (String) key);
            }
        };

    private Collection           _supportedLocales = Collections.EMPTY_SET;
    private Locale               _defaultLocale = Locale.getDefault();
    private String               _messageBundle;

    private ViewHandler          _viewHandler;
    private NavigationHandler    _navigationHandler;
    private VariableResolver     _variableResolver;
    private PropertyResolver     _propertyResolver;
    private ActionListener       _actionListener;
    private String               _defaultRenderKitId;
    private StateManager         _stateManager;

    // components, converters, and validators can be added at runtime--must synchronize
    private final Map _converterMap = Collections.synchronizedMap(new HashMap());
    private final Map _converterTypeMap = Collections.synchronizedMap(new HashMap());
    private final Map _componentClassMap = Collections.synchronizedMap(new HashMap());
    private final Map _validatorClassMap = Collections.synchronizedMap(new HashMap());


    //~ Constructors -------------------------------------------------------------------------------

    public ApplicationImpl()
    {
        // set default implementation in constructor
        // pragmatic approach, no syncronizing will be needed in get methods
        _viewHandler = new JspViewHandlerImpl();
        _navigationHandler = new NavigationHandlerImpl();
        _variableResolver = new VariableResolverImpl();
        _propertyResolver = new PropertyResolverImpl();
        _actionListener = new ActionListenerImpl();
        _defaultRenderKitId = null;
        _stateManager = new JspStateManagerImpl();
        if (log.isTraceEnabled()) log.trace("New Application instance created");
    }

    //~ Methods ------------------------------------------------------------------------------------

    public void setActionListener(ActionListener actionListener)
    {
        if (actionListener == null)
        {
            log.error("setting actionListener to null is not allowed");
            throw new NullPointerException("setting actionListener to null is not allowed");
        }
        _actionListener = actionListener;
        if (log.isTraceEnabled()) log.trace("set actionListener = " + actionListener.getClass().getName());
    }

    public ActionListener getActionListener()
    {
        return _actionListener;
    }

    public Iterator getComponentTypes()
    {
        return _componentClassMap.keySet().iterator();
    }

    public Iterator getConverterIds()
    {
        return _converterMap.keySet().iterator();
    }

    public Iterator getConverterTypes()
    {
        return _converterTypeMap.keySet().iterator();
    }

    public void setDefaultLocale(Locale locale)
    {
        if (locale == null)
        {
            log.error("setting locale to null is not allowed");
            throw new NullPointerException("setting locale to null is not allowed");
        }
        _defaultLocale = locale;
        if (log.isTraceEnabled()) log.trace("set defaultLocale = " + locale.getCountry() + " " + locale.getLanguage());
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
            throw new NullPointerException("setting messageBundle to null is not allowed");
        }
        _messageBundle = messageBundle;
        if (log.isTraceEnabled()) log.trace("set MessageBundle = " + messageBundle);
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
            throw new NullPointerException("setting navigationHandler to null is not allowed");
        }
        _navigationHandler = navigationHandler;
        if (log.isTraceEnabled()) log.trace("set NavigationHandler = " + navigationHandler.getClass().getName());
    }

    public NavigationHandler getNavigationHandler()
    {
        return _navigationHandler;
    }

    public void setPropertyResolver(PropertyResolver propertyResolver)
    {
        if (propertyResolver == null)
        {
            log.error("setting propertyResolver to null is not allowed");
            throw new NullPointerException("setting propertyResolver to null is not allowed");
        }
        _propertyResolver = propertyResolver;
        if (log.isTraceEnabled()) log.trace("set PropertyResolver = " + propertyResolver.getClass().getName());
   }

    public PropertyResolver getPropertyResolver()
    {
        return _propertyResolver;
    }

    public void setSupportedLocales(Collection locales)
    {
        if (locales == null)
        {
            log.error("setting supportedLocales to null is not allowed");
            throw new NullPointerException("setting supportedLocales to null is not allowed");
        }
        _supportedLocales = locales;
        if (log.isTraceEnabled()) log.trace("set SupportedLocales");
    }

    public Iterator getSupportedLocales()
    {
        return _supportedLocales.iterator();
    }

    public Iterator getValidatorIds()
    {
        return _validatorClassMap.keySet().iterator();
    }

    public void setVariableResolver(VariableResolver variableResolver)
    {
        if (variableResolver == null)
        {
            log.error("setting variableResolver to null is not allowed");
            throw new NullPointerException("setting variableResolver to null is not allowed");
        }
        _variableResolver = variableResolver;
        if (log.isTraceEnabled()) log.trace("set VariableResolver = " + variableResolver.getClass().getName());
    }

    public VariableResolver getVariableResolver()
    {
        return _variableResolver;
    }

    public void setViewHandler(ViewHandler viewHandler)
    {
        if (viewHandler == null)
        {
            log.error("setting viewHandler to null is not allowed");
            throw new NullPointerException("setting viewHandler to null is not allowed");
        }
        _viewHandler = viewHandler;
        if (log.isTraceEnabled()) log.trace("set ViewHandler = " + viewHandler.getClass().getName());
    }

    public ViewHandler getViewHandler()
    {
        return _viewHandler;
    }

    public void addComponent(String componentType, String componentClassName)
    {
        if ((componentType == null) || (componentType.length() == 0))
        {
            log.error("addComponent: componentType = null is not allowed");
            throw new NullPointerException("addComponent: componentType = null ist not allowed");
        }
        if ((componentClassName == null) || (componentClassName.length() == 0))
        {
            log.error("addComponent: component = null is not allowed");
            throw new NullPointerException("addComponent: component = null is not allowed");
        }
        
        try
        {
            _componentClassMap.put(componentType, ClassUtils.classForName(componentClassName));
            if (log.isTraceEnabled()) log.trace("add Component class = " + componentClassName +
                                                " for type = " + componentType);
        }
        catch (Exception e)
        {
            log.error("Component class " + componentClassName + " not found", e);
        }
    }

    public void addConverter(String converterId, String converterClass)
    {
        if ((converterId == null) || (converterId.length() == 0))
        {
            log.error("addConverter: converterId = null is not allowed");
            throw new NullPointerException("addConverter: converterId = null ist not allowed");
        }
        if ((converterClass == null) || (converterClass.length() == 0))
        {
            log.error("addConverter: converterClass = null is not allowed");
            throw new NullPointerException("addConverter: converterClass = null ist not allowed");
        }

        
        try
        {
            _converterMap.put(converterId, ClassUtils.classForName(converterClass));
            if (log.isTraceEnabled()) log.trace("add Converter id = " + converterId +
                    " converterClass = " + converterClass);
           }
        catch (Exception e)
        {
            log.error("Converter class " + converterClass + " not found", e);
        }
    }

    public void addConverter(Class targetClass, String converterClass)
    {
        if ((targetClass == null))
        {
            log.error("addConverter: targetClass = null is not allowed");
            throw new NullPointerException("addConverter: targetClass = null ist not allowed");
        }
        if ((converterClass == null) || (converterClass.length() == 0))
        {
            log.error("addConverter: converterClass = null is not allowed");
            throw new NullPointerException("addConverter: converterClass = null ist not allowed");
        }
        
        try
        {
            _converterTypeMap.put(targetClass, ClassUtils.classForName(converterClass));
            if (log.isTraceEnabled()) log.trace("add Converter for class = " + targetClass +
                    " converterClass = " + converterClass);
        }
        catch (Exception e)
        {
            log.error("Converter class " + converterClass + " not found", e);
        }
    }

    public void addValidator(String validatorId, String validatorClass)
    {
        if ((validatorId == null) || (validatorId.length() == 0))
        {
            log.error("addValidator: validatorId = null is not allowed");
            throw new NullPointerException("addValidator: validatorId = null ist not allowed");
        }
        if ((validatorClass == null) || (validatorClass.length() == 0))
        {
            log.error("addValidator:  validatorClass = null is not allowed");
            throw new NullPointerException("addValidator:  validatorClass = null ist not allowed");
        }
        
        try
        {
            _validatorClassMap.put(validatorId, ClassUtils.classForName(validatorClass));
            if (log.isTraceEnabled()) log.trace("add Validator id = " + validatorId +
                                            " class = " + validatorClass);
        }
        catch (Exception e)
        {
            log.error("Validator class " + validatorClass + " not found", e);
        }
    }

    public UIComponent createComponent(String componentType)
        throws FacesException
    {
        if ((componentType == null) || (componentType.length() == 0))
        {
            log.error("createComponent: componentType = null is not allowed");
            throw new NullPointerException("createComponent: componentType = null is not allowed");
        }

        Class componentClass;
        synchronized (_componentClassMap)
        {
            componentClass = (Class) _componentClassMap.get(componentType);
        }
        if (componentClass == null)
        {
            log.error("Undefined component type " + componentType);
            throw new FacesException("Undefined component type " + componentType);
        }

        try
        {
            return (UIComponent) componentClass.newInstance();
        }
        catch (Exception e)
        {
            log.error("Could not instantiate component componentType = " + componentType, e);
            throw new FacesException("Could not instantiate component componentType = " + componentType, e);
        }
    }

    public UIComponent createComponent(ValueBinding valueBinding,
                                       FacesContext facesContext,
                                       String componentType)
        throws FacesException
    {
        if ((valueBinding == null))
        {
            log.error("createComponent: valueBinding = null is not allowed");
            throw new NullPointerException("createComponent: valueBinding = null ist not allowed");
        }
        if ((facesContext == null))
        {
            log.error("createComponent: facesContext = null is not allowed");
            throw new NullPointerException("createComponent: facesContext = null ist not allowed");
        }
        if ((componentType == null) || (componentType.length() == 0))
        {
            log.error("createComponent: componentType = null is not allowed");
            throw new NullPointerException("createComponent: componentType = null ist not allowed");
        }

        Object obj = valueBinding.getValue(facesContext);
        if (obj instanceof UIComponent)
        {
            return (UIComponent) obj;
        }
        else
        {
            UIComponent component = createComponent(componentType);
            valueBinding.setValue(facesContext, component);
            return component;
        }
    }

    public Converter createConverter(String converterId)
    {
        if ((converterId == null) || (converterId.length() == 0))
        {
            log.error("createConverter: converterId = null is not allowed");
            throw new NullPointerException("createConverter: converterId = null ist not allowed");
        }

        Class converterClass = (Class) _converterMap.get(converterId);
        if (converterClass == null)
        {
            log.error("Unknown converter id '" + converterId + "'.");
            throw new FacesException("Unknown converter id '" + converterId + "'.");
        }

        try
        {
            return (Converter) converterClass.newInstance();
        }
        catch (Exception e)
        {
            log.error("Could not instantiate converter " + converterClass, e);
            throw new FacesException("Could not instantiate converter: " + converterClass, e);
        }
    }


    public Converter createConverter(Class targetClass)
    {
        if (targetClass == null)
        {
            log.error("createConverter: targetClass = null is not allowed");
            throw new NullPointerException("createConverter: targetClass = null ist not allowed");
        }

        Converter converter = internalCreateConverter(targetClass);
        if (converter == null)
        {
            throw new FacesException("There is no registered converter for class " + targetClass.getName());
        }

        return converter;
    }


    private Converter internalCreateConverter(Class targetClass)
    {
        // Locate a Converter registered for the target class itself.
        Class converterClass = (Class)_converterTypeMap.get(targetClass);

        //Locate a Converter registered for interfaces that are
        // implemented by the target class (directly or indirectly).
        if (converterClass == null)
        {
            Class interfaces[] = targetClass.getInterfaces();
            if (interfaces != null)
            {
                for (int i = 0; i < interfaces.length; i++)
                {
                    converterClass = (Class)_converterTypeMap.get(interfaces[i]);
                    if(converterClass != null)
                    {
                        break;
                    }
                }
            }
        }

        if (converterClass != null)
        {
            try
            {
                return (Converter) converterClass.newInstance();
            }
            catch (Exception e)
            {
                log.error("Could not instantiate converter " + converterClass, e);
                throw new FacesException("Could not instantiate converter: " + converterClass, e);
            }
        }

        //   locate converter for primitive types
        if (targetClass == Long.TYPE)
        {
            return internalCreateConverter(Long.class);
        } else if (targetClass == Boolean.TYPE)
        {
            return internalCreateConverter(Boolean.class);
        } else if (targetClass == Double.TYPE)
        {
            return internalCreateConverter(Double.class);
        } else if (targetClass == Byte.TYPE)
        {
            return internalCreateConverter(Byte.class);
        } else if (targetClass == Short.TYPE)
        {
            return internalCreateConverter(Short.class);
        } else if (targetClass == Integer.TYPE)
        {
            return internalCreateConverter(Integer.class);
        } else if (targetClass == Float.TYPE)
        {
            return internalCreateConverter(Float.class);
        } else if (targetClass == Character.TYPE)
        {
            return internalCreateConverter(Character.class);
        }


        //Locate a Converter registered for the superclass (if any) of the target class,
        // recursively working up the inheritance hierarchy.
        Class superClazz = targetClass.getSuperclass();
        if (superClazz != null)
        {
            return internalCreateConverter(superClazz);
        }
        else
        {
            return null;
        }

    }


    public synchronized MethodBinding createMethodBinding(String reference, Class[] params)
        throws ReferenceSyntaxException
    {
        if ((reference == null) || (reference.length() == 0))
        {
            log.error("createMethodBinding: reference = null is not allowed");
            throw new NullPointerException("createMethodBinding: reference = null ist not allowed");
        }

        // We choose to instantiate a new MethodBinding every time as this is much easier 
        // and about as efficient as implementing a cache specifically for MethodBinding,
        // which is complicated by the need to use a conposite key=(reference, params)
        // (significant part of MethodBinding is already cached by ValueBinding implicitly)
        return new MethodBindingImpl(this, reference, params);
    }

    public Validator createValidator(String validatorId) throws FacesException
    {
        if ((validatorId == null) || (validatorId.length() == 0))
        {
            log.error("createValidator: validatorId = null is not allowed");
            throw new NullPointerException("createValidator: validatorId = null ist not allowed");
        }
        
        Class validatorClass = (Class) _validatorClassMap.get(validatorId);
        if (validatorClass == null)
        {
            String message = "Unknown converter id '" + validatorId + "'."; 
            log.error(message);
            throw new FacesException(message);
        }
        
        try
        {
            return (Validator) validatorClass.newInstance();
        }
        catch (Exception e)
        {
            log.error("Could not instantiate converter " + validatorClass, e);
            throw new FacesException("Could not instantiate converter: " + validatorClass, e);
        }
    }

    public ValueBinding createValueBinding(String reference) throws ReferenceSyntaxException
    {
        if ((reference == null) || (reference.length() == 0))
        {
            log.error("createValueBinding: reference = null is not allowed");
            throw new NullPointerException("createValueBinding: reference = null is not allowed");
        }
        return (ValueBinding) _valueBindingCache.get(reference);
    }


    public String getDefaultRenderKitId()
    {
        return _defaultRenderKitId;
    }

    public void setDefaultRenderKitId(String defaultRenderKitId)
    {
        _defaultRenderKitId = defaultRenderKitId;
    }

    public StateManager getStateManager()
    {
        return _stateManager;
    }

    public void setStateManager(StateManager stateManager)
    {
        _stateManager = stateManager;
    }
}

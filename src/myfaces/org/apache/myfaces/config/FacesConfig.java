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
package net.sourceforge.myfaces.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class FacesConfig
    implements Config
{
    private static final Log log = LogFactory.getLog(FacesConfig.class);

    private ApplicationConfig _applicationConfig;
    private FactoryConfig _factoryConfig;
    private LifecycleConfig _lifecycleConfig;
    private final Map _converterMap = new HashMap();
    private final Map _converterTypeMap = new HashMap();
    private final Map _componentClassMap = new HashMap();
    private final Map _validatorClassMap = new HashMap();
    private final Map _managedBeanConfigMap = new HashMap();
    private final List _navigationRuleConfigList = new ArrayList();
    private final Map _referencedBeanConfigMap = new HashMap();
    private final Map _renderKitConfigMap = new HashMap();


    public ApplicationConfig getApplicationConfig()
    {
        return _applicationConfig;
    }

    /**
     * Replaces the current ApplicationConfig by a new one.
     * @param applicationConfig
     */
    public void setApplicationConfig(ApplicationConfig applicationConfig)
    {
        _applicationConfig = applicationConfig;
    }

    /**
     * Other than in {@link #setApplicationConfig} the current ApplicationConfig
     * is not replaced, but rather all non-null properties of the given
     * ApplicationConfig are copied to the current ApplicationConfig.
     * @param applicationConfig
     */
    public void addApplicationConfig(ApplicationConfig applicationConfig)
    {
        if (_applicationConfig == null)
        {
            _applicationConfig = applicationConfig;
        }
        else
        {
            _applicationConfig.update(applicationConfig);
        }
    }


    public void addConverterConfig(ConverterConfig converterConfig)
    {
        getConverterMap().put(converterConfig.getConverterId(),
                              converterConfig.newConverter());
    }

    public void addConverter(Class targetClass, String converterClass)
    {
        Converter converter = (Converter)ConfigUtil.newInstance(converterClass);
        _converterTypeMap.put(targetClass, converter);
    }

    public void addConverter(String converterId, String converterClass)
    {
        Converter converter = (Converter)ConfigUtil.newInstance(converterClass);
        getConverterMap().put(converterId, converter);
    }

    public Converter getConverter(String converterId) throws FacesException
    {
        Converter converter = (Converter)getConverterMap().get(converterId);
        if (converter == null)
        {
            if (log.isErrorEnabled()) log.error("Unknown converter id '" + converterId + "'.");
            throw new FacesException("Unknown converter id '" + converterId + "'.");
        }
        return converter;
    }

    public Converter getConverter(Class targetClass) throws FacesException
    {
        Converter converter = (Converter)getConverterTypeMap().get(targetClass);
        if (converter == null)
        {
            if (log.isErrorEnabled()) log.error("Unknown converter targetClass '" + targetClass.getName() + "'.");
            throw new FacesException("Unknown converter targetClass '" + targetClass.getName() + "'.");
        }
        return converter;
    }

    public Iterator getConverterIds()
    {
        return getConverterMap().keySet().iterator();
    }

    public Iterator getConverterTypes()
    {
        return getConverterTypeMap().keySet().iterator();
    }

    private Map getConverterMap()
    {
        return _converterMap;
    }

    private Map getConverterTypeMap()
    {
        return _converterTypeMap;
    }

    public void addComponentConfig(ComponentConfig componentConfig)
    {
        addComponent(componentConfig.getComponentType(),
                     componentConfig.getComponentClass());
    }

    public void addComponent(String componentType, String componentClass)
    {
        getComponentClassMap().put(componentType,
                                   ConfigUtil.classForName(componentClass));
    }

    public UIComponent getComponent(String componentType) throws FacesException
    {
        Class componentClass = (Class)getComponentClassMap().get(componentType);
        if (componentClass == null)
        {
            throw new FacesException("Unknown component type '" + componentType + "'.");
        }
        return (UIComponent)ConfigUtil.newInstance(componentClass);
    }

    public Iterator getComponentTypes()
    {
        return getComponentClassMap().keySet().iterator();
    }

    private Map getComponentClassMap()
    {
        return _componentClassMap;
    }

    public void addValidatorConfig(ValidatorConfig validatorConfig)
    {
        addValidator(validatorConfig.getValidatorId(),
                     validatorConfig.getValidatorClass());
    }

    public void addValidator(String validatorId, String validatorClass)
    {
        getValidatorClassMap().put(validatorId,
                                   ConfigUtil.classForName(validatorClass));
    }

    public Validator getValidator(String validatorId) throws FacesException
    {
        Class clazz = (Class)getValidatorClassMap().get(validatorId);
        if (clazz == null)
        {
            throw new FacesException("Unknown validator id '" + validatorId + "'.");
        }
        return (Validator)ConfigUtil.newInstance(clazz);
    }

    public Iterator getValidatorIds()
    {
        return getValidatorClassMap().keySet().iterator();
    }

    private Map getValidatorClassMap()
    {
        return _validatorClassMap;
    }



    public void addManagedBeanConfig(ManagedBeanConfig managedBeanConfig)
    {
        getManagedBeanConfigMap().put(managedBeanConfig.getManagedBeanName(), managedBeanConfig);
    }

    public ManagedBeanConfig getManagedBeanConfig(String managedBeanName)
    {
        return (ManagedBeanConfig)getManagedBeanConfigMap().get(managedBeanName);
    }

    private Map getManagedBeanConfigMap()
    {
        return _managedBeanConfigMap;
    }



    public void addNavigationRuleConfig(NavigationRuleConfig navigationRuleConfig)
    {
        _navigationRuleConfigList.add(navigationRuleConfig);
    }

    public List getNavigationRuleConfigList()
    {
        return _navigationRuleConfigList == null
                ? Collections.EMPTY_LIST
                : _navigationRuleConfigList;
    }



    public void addReferencedBeanConfig(ReferencedBeanConfig referencedBeanConfig)
    {
        getReferencedBeanConfigMap().put(referencedBeanConfig.getReferencedBeanName(),
                                         referencedBeanConfig);
    }

    public ReferencedBeanConfig getReferencedBeanConfig(String referencedBeanName)
    {
        return (ReferencedBeanConfig)getReferencedBeanConfigMap().get(referencedBeanName);
    }

    public Iterator getReferencedBeanNames()
    {
        return getReferencedBeanConfigMap().keySet().iterator();
    }

    private Map getReferencedBeanConfigMap()
    {
        return _referencedBeanConfigMap;
    }



    public void addRenderKitConfig(RenderKitConfig newRKC)
    {
        RenderKitConfig oldRKC = getRenderKitConfig(newRKC.getRenderKitId());
        if (oldRKC == null)
        {
            getRenderKitConfigMap().put(newRKC.getRenderKitId(),
                                        newRKC);
        }
        else
        {
            //merge RenderKitConfigs

            //check consistence
            if (oldRKC.getRenderKitClass() != null &&
                newRKC.getRenderKitClass() != null &&
                oldRKC.getRenderKitClass().equals(newRKC.getRenderKitClass()))
            {
                log.warn("RenderKit '" + newRKC.getRenderKitId() + "' defined twice with different classes!");
            }
            else if (oldRKC.getRenderKitClass() == null)
            {
                oldRKC.setRenderKitClass(newRKC.getRenderKitClass());
            }

            for (Iterator it = newRKC.getRendererTypes(); it.hasNext(); )
            {
                RendererConfig rc = newRKC.getRendererConfig((String)it.next());
                oldRKC.addRendererConfig(rc);
            }
        }
    }

    public RenderKitConfig getRenderKitConfig(String renderKitId)
    {
        return (RenderKitConfig)getRenderKitConfigMap().get(renderKitId);
    }

    public Iterator getRenderKitIds()
    {
        return getRenderKitConfigMap().keySet().iterator();
    }

    public Iterator getRenderKitConfigs()
    {
        return getRenderKitConfigMap().values().iterator();
    }

    private Map getRenderKitConfigMap()
    {
        return _renderKitConfigMap;
    }

    public void configureAll()
    {
        configureRenderKits();
    }

    /**
     * Registers the RenderKits that are defined in this configuration in the
     * RenderKitFactory and configures them (adds new renderers, etc.).
     */
    public void configureRenderKits()
    {
        RenderKitFactory rkf
            = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        for (Iterator it = getRenderKitIds(); it.hasNext(); )
        {
            String id = (String)it.next();
            RenderKitConfig rkc = getRenderKitConfig(id);
            RenderKit rk;
            if (renderKitFactoryContains(rkf, id))
            {
                rk = rkf.getRenderKit(id);
            }
            else
            {
                rk = rkc.newRenderKit();
                rkf.addRenderKit(id, rk);
            }
            rkc.configureRenderers(rk);
        }
    }


    /**
     * Helper for {@link #configureRenderKits}.
     */
    private static boolean renderKitFactoryContains(RenderKitFactory rkf, String renderKitId)
    {
        for (Iterator it = rkf.getRenderKitIds(); it.hasNext(); )
        {
            if (it.next().equals(renderKitId))
            {
                return true;
            }
        }
        return false;
    }

    public FactoryConfig getFactoryConfig()
    {
        return _factoryConfig;
    }
    
    public void setFactoryConfig(FactoryConfig factoryConfig)
    {
        _factoryConfig = factoryConfig;
    }
    
    public void addFactoryConfig(FactoryConfig factoryConfig)
    {
        if (_factoryConfig == null)
        {
            _factoryConfig = factoryConfig;
        }
        else
        {
            _factoryConfig.update(factoryConfig);
        }
    }
    
    public LifecycleConfig getLifecycleConfig()
    {
        return _lifecycleConfig;
    }
    
    public void setLifecycleConfig(LifecycleConfig lifecycleConfig)
    {
        _lifecycleConfig = lifecycleConfig;
    }

    public void addLifecycleConfig(LifecycleConfig lifecycleConfig)
    {
        if ( _lifecycleConfig == null)
        {
            _lifecycleConfig = lifecycleConfig;
        }
        else
        {
            _lifecycleConfig.update(lifecycleConfig);
        }
    }
    }

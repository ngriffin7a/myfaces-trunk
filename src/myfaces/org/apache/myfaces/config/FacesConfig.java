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
package net.sourceforge.myfaces.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.*;


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

    private FactoryConfig _factoryConfig;
    private LifecycleConfig _lifecycleConfig;

    // Application specific
    private ApplicationConfig _applicationConfig;
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
        _converterMap.put(converterConfig.getConverterId(), converterConfig.getConverterClass());
    }

    public void addComponentConfig(ComponentConfig componentConfig)
    {
        _componentClassMap.put(componentConfig.getComponentType(),
                               componentConfig.getComponentClass());
    }

    public void addValidatorConfig(ValidatorConfig validatorConfig)
    {
        addValidator(validatorConfig.getValidatorId(),
                     validatorConfig.getValidatorClass());
    }

    public void addValidator(String validatorId, String validatorClass)
    {
        _validatorClassMap.put(validatorId, validatorClass);
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
        configureFactoryFinder();
        configureRenderKits();
        configureApplication();
    }

    public void configureApplication()
    {
        Application app = ((ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY))
            .getApplication();
        ApplicationConfig appConfig = getApplicationConfig();

        NavigationHandler navigationHandler = appConfig.getNavigationHandler();
        if (navigationHandler != null)
        {
            app.setNavigationHandler(navigationHandler);
        }

        PropertyResolver propertyResolver = appConfig.getPropertyResolver();
        if (propertyResolver != null)
        {
            app.setPropertyResolver(propertyResolver);
        }

        VariableResolver variableResolver = appConfig.getVariableResolver();
        if (variableResolver != null)
        {
            app.setVariableResolver(variableResolver);
        }

        ViewHandler viewHandler = appConfig.getViewHandler();
        if (viewHandler != null)
        {
            app.setViewHandler(viewHandler);
        }

        ActionListener actionListener = app.getActionListener();
        if (actionListener != null)
        {
            app.setActionListener(actionListener);
        }

        for (Iterator it = _converterMap.keySet().iterator(); it.hasNext();)
        {
            String converterId = (String)it.next();
            String converterClass = (String)_converterMap.get(converterId);
            app.addConverter(converterId, converterClass);
        }
        for (Iterator it = _converterTypeMap.keySet().iterator(); it.hasNext();)
        {
            Class targetClass = (Class)it.next();
            String converterClass = (String)_converterTypeMap.get(targetClass);
            app.addConverter(targetClass, converterClass);
        }
        for (Iterator it = _componentClassMap.keySet().iterator(); it.hasNext();)
        {
            String componentType = (String)it.next();
            String componentClass = (String)_componentClassMap.get(componentType);
            app.addComponent(componentType, componentClass);
        }
        for (Iterator it = _validatorClassMap.keySet().iterator(); it.hasNext();)
        {
            String validatorId = (String)it.next();
            String validatorClass = (String)_validatorClassMap.get(validatorId);
            app.addValidator(validatorId, validatorClass);
        }

    }

    public void configureFactoryFinder()
    {
        FactoryConfig config = getFactoryConfig();
        if (config == null)
        {
            log.error("Could not find factory configuration in faces-config");
            throw new NullPointerException("Could not find factory configuration in faces-config");
        }
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
                                 getFactoryConfig().getApplicationFactory());
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                                 getFactoryConfig().getFacesContextFactory());
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                                 getFactoryConfig().getLifecycleFactory());
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                                 getFactoryConfig().getRenderKitFactory());
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

    public void addLifecycleConfig(LifecycleConfig lifecycleConfig)
    {
        if (_lifecycleConfig == null)
        {
            _lifecycleConfig = lifecycleConfig;
        }
        else
        {
            _lifecycleConfig.addPhaseListenerClasses(lifecycleConfig.getPhaseListenerClasses());
        }
    }
}

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

package net.sourceforge.myfaces.confignew.impl.digester;

import java.util.*;

import net.sourceforge.myfaces.confignew.FacesConfigDispenser;
import net.sourceforge.myfaces.confignew.impl.digester.elements.*;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class DigesterFacesConfigDispenserImpl implements FacesConfigDispenser
{

    private List configs = new ArrayList();
    private List applicationFactories = new ArrayList();
    private List facesContextFactories = new ArrayList();
    private List lifecycleFactories = new ArrayList();
    private List renderKitFactories = new ArrayList();
    private Map components = new HashMap();
    private Map validators = new HashMap();
    private String defaultRenderKitId;
    private LocaleConfig localeConfig;
    private List actionListeners = new ArrayList();
    private List lifecyclePhaseListeners = new ArrayList();
    private String messageBundle;
    private List navigationHandlers = new ArrayList();
    private List viewHandlers = new ArrayList();
    private List stateManagers = new ArrayList();
    private List propertyResolver = new ArrayList();
    private List variableResolver = new ArrayList();
    private Map converterById = new HashMap();
    private Map converterByClass = new HashMap();
    private Map renderKits = new HashMap();
    private List managedBeans = new ArrayList();
    private List navigationRules = new ArrayList();


    /**
     * Add another unmarshalled faces config object.
     *
     * @param facesConfig unmarshalled faces config object
     */
    public void feed(Object facesConfig)
    {
        FacesConfig config = (FacesConfig) facesConfig;
        configs.add(config);
        for (Iterator iterator = config.getFactories().iterator(); iterator.hasNext();)
        {
            Factory factory = (Factory) iterator.next();
            applicationFactories.addAll(factory.getApplicationFactory());
            facesContextFactories.addAll(factory.getFacesContextFactory());
            lifecycleFactories.addAll(factory.getLifecycleFactory());
            renderKitFactories.addAll(factory.getRenderkitFactory());
        }
        components.putAll(config.getComponents());
        validators.putAll(config.getValidators());

        for (Iterator iterator = config.getApplications().iterator(); iterator.hasNext();)
        {
            Application application = (Application) iterator.next();
            if (!application.getDefaultRenderkitId().isEmpty())
            {
                defaultRenderKitId = (String) application.getDefaultRenderkitId().get(application.getDefaultRenderkitId().size() - 1);
            }
            if (!application.getMessageBundle().isEmpty())
            {
                messageBundle = (String) application.getMessageBundle().get(application.getMessageBundle().size() - 1);
            }
            if (!application.getLocaleConfig().isEmpty())
            {
                localeConfig = (LocaleConfig) application.getLocaleConfig().get(application.getLocaleConfig().size() - 1);
            }
            actionListeners.addAll(application.getActionListener());
            navigationHandlers.addAll(application.getNavigationHandler());
            viewHandlers.addAll(application.getViewHandler());
            stateManagers.addAll(application.getStateManager());
            propertyResolver.addAll(application.getPropertyResolver());
            variableResolver.addAll(application.getVariableResolver());
        }
        for (Iterator iterator = config.getConverters().iterator(); iterator.hasNext();)
        {
            Converter converter = (Converter) iterator.next();

            if (converter.getConverterId() != null)
            {
                converterById.put(converter.getConverterId(), converter.getConverterClass());
            } else
            {
                converterByClass.put(converter.getForClass(), converter.getConverterClass());
            }
        }

        for (Iterator iterator = config.getRenderKits().iterator(); iterator.hasNext();)
        {
            RenderKit renderKit = (RenderKit) iterator.next();
            RenderKit existing = (RenderKit) renderKits.get(renderKit.getId());

            if (existing == null) {
                renderKits.put(renderKit.getId(), renderKit);
            } else {
                existing.merge(renderKit);
            }
        }
        lifecyclePhaseListeners.addAll(config.getLifecyclePhaseListener());
        managedBeans.addAll(config.getManagedBeans());
        navigationRules.addAll(config.getNavigationRules());
    }


    /**
     * Add another ApplicationFactory class name
     *
     * @param factoryClassName a class name
     */
    public void feedApplicationFactory(String factoryClassName)
    {
        applicationFactories.add(factoryClassName);
    }


    /**
     * Add another FacesContextFactory class name
     *
     * @param factoryClassName a class name
     */
    public void feedFacesContextFactory(String factoryClassName)
    {
        lifecycleFactories.add(factoryClassName);
    }


    /**
     * Add another LifecycleFactory class name
     *
     * @param factoryClassName a class name
     */
    public void feedLifecycleFactory(String factoryClassName)
    {
        lifecycleFactories.add(factoryClassName);
    }


    /**
     * Add another RenderKitFactory class name
     *
     * @param factoryClassName a class name
     */
    public void feedRenderKitFactory(String factoryClassName)
    {
        renderKitFactories.add(factoryClassName);
    }


    /**
     * @return Iterator over ApplicationFactory class names
     */
    public Iterator getApplicationFactoryIterator()
    {
        return applicationFactories.iterator();
    }


    /**
     * @return Iterator over FacesContextFactory class names
     */
    public Iterator getFacesContextFactoryIterator()
    {
        return facesContextFactories.iterator();
    }


    /**
     * @return Iterator over LifecycleFactory class names
     */
    public Iterator getLifecycleFactoryIterator()
    {
        return lifecycleFactories.iterator();
    }


    /**
     * @return Iterator over RenderKit factory class names
     */
    public Iterator getRenderKitFactoryIterator()
    {
        return renderKitFactories.iterator();
    }


    /**
     * @return Iterator over ActionListener class names
     */
    public Iterator getActionListenerIterator()
    {
        List listeners = new ArrayList(actionListeners);
        return listeners.iterator();
    }


    /**
     * @return the default render kit id
     */
    public String getDefaultRenderKitId()
    {
        return defaultRenderKitId;
    }


    /**
     * @return Iterator over message bundle names
     */
    public String getMessageBundle()
    {
        return messageBundle;
    }


    /**
     * @return Iterator over NavigationHandler class names
     */
    public Iterator getNavigationHandlerIterator()
    {
        List handlers = new ArrayList(navigationHandlers);
        return handlers.iterator();
    }


    /**
     * @return Iterator over ViewHandler class names
     */
    public Iterator getViewHandlerIterator()
    {
        List handlers = new ArrayList(viewHandlers);
        return handlers.iterator();
    }


    /**
     * @return Iterator over StateManager class names
     */
    public Iterator getStateManagerIterator()
    {
        List managers = new ArrayList(stateManagers);
        return managers.iterator();
    }


    /**
     * @return Iterator over PropertyResolver class names
     */
    public Iterator getPropertyResolverIterator()
    {
        List resolver = new ArrayList(propertyResolver);
        return resolver.iterator();
    }


    /**
     * @return Iterator over VariableResolver class names
     */
    public Iterator getVariableResolverIterator()
    {
        List resolver = new ArrayList(variableResolver);

        return resolver.iterator();
    }


    /**
     * @return the default locale name
     */
    public String getDefaultLocale()
    {
        if (localeConfig != null)
        {
            return localeConfig.getDefaultLocale();
        }
        return null;
    }


    /**
     * @return Iterator over supported locale names
     */
    public Iterator getSupportedLocalesIterator()
    {
        if (localeConfig != null)
        {
            return localeConfig.getSupportedLocales().iterator();
        }
        return Collections.EMPTY_LIST.iterator();
    }


    /**
     * @return Iterator over all defined component types
     */
    public Iterator getComponentTypes()
    {
        return components.keySet().iterator();
    }


    /**
     * @return component class that belongs to the given component type
     */
    public String getComponentClass(String componentType)
    {
        return (String) components.get(componentType);
    }


    /**
     * @return Iterator over all defined converter ids
     */
    public Iterator getConverterIds()
    {
        return converterById.keySet().iterator();
    }


    /**
     * @return Iterator over all classes with an associated converter
     */
    public Iterator getConverterClasses()
    {
        return converterByClass.keySet().iterator();
    }


    /**
     * @return converter class that belongs to the given converter id
     */
    public String getConverterClassById(String converterId)
    {
        return (String) converterById.get(converterId);
    }


    /**
     * @return converter class that is associated with the given class name
     */
    public String getConverterClassByClass(String className)
    {
        return (String) converterByClass.get(className);
    }


    /**
     * @return Iterator over all defined validator ids
     */
    public Iterator getValidatorIds()
    {
        return validators.keySet().iterator();
    }


    /**
     * @return validator class name that belongs to the given validator id
     */
    public String getValidatorClass(String validatorId)
    {
        return (String) validators.get(validatorId);
    }


    /**
     * @return Iterator over {@link net.sourceforge.myfaces.confignew.element.ManagedBean ManagedBean}s
     */
    public Iterator getManagedBeans()
    {
        return managedBeans.iterator();
    }


    /**
     * @return Iterator over {@link net.sourceforge.myfaces.confignew.element.NavigationRule NavigationRule}s
     */
    public Iterator getNavigationRules()
    {
        return navigationRules.iterator();
    }


    /**
     * @return Iterator over all defined renderkit ids
     */
    public Iterator getRenderKitIds()
    {
        return renderKits.keySet().iterator();
    }


    /**
     * @return renderkit class name for given renderkit id
     */
    public String getRenderKitClass(String renderKitId)
    {
        RenderKit renderKit = (RenderKit) renderKits.get(renderKitId);
        return renderKit.getRenderKitClass();
    }


    /**
     * @return Iterator over {@link net.sourceforge.myfaces.confignew.element.Renderer Renderer}s for the given renderKitId
     */
    public Iterator getRenderers(String renderKitId)
    {
        RenderKit renderKit = (RenderKit) renderKits.get(renderKitId);
        return renderKit.getRenderer().iterator();
    }


    /**
     * @return Iterator over {@link javax.faces.event.PhaseListener} implementation class names
     */
    public Iterator getLifecyclePhaseListeners()
    {
        return lifecyclePhaseListeners.iterator();
    }

}

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

import net.sourceforge.myfaces.renderkit.html.jspinfo.TLDInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import java.util.*;
import java.util.Map.Entry;


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

    protected static final String TLD_HTML_URI = "http://java.sun.com/jsf/html";
    protected static final String TLD_EXT_URI = "http://myfaces.sourceforge.net/tld/myfaces_ext_0_4.tld";

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

    public void configureAll(ExternalContext externalContext)
    {
        configureFactoryFinder();
        configureApplication();
        configureRenderKits(externalContext);
    }

    public void configureApplication()
    {
        ApplicationConfig appConfig = getApplicationConfig();

        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application app = af.getApplication();

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

        ActionListener actionListener = appConfig.getActionListener();
        if (actionListener != null)
        {
            app.setActionListener(actionListener);
        }

        String messageBundle = appConfig.getMessageBundle();
        if (messageBundle != null)
        {
            app.setMessageBundle(messageBundle);
        }

        LocaleConfig localeConfig = appConfig.getLocaleConfig();
        if (localeConfig != null)
        {
            if (localeConfig.getDefaultLocale() != null)
            {
                app.setDefaultLocale(localeConfig.getDefaultLocale());
            }
            else
            {
                if (localeConfig.getSupportedLocales() != null &&
                    !localeConfig.getSupportedLocales().isEmpty())
                {
                    Locale first = (Locale)localeConfig.getSupportedLocales().iterator().next();
                    log.warn("No default locale defined. Using first supported locale " + first);
                    app.setDefaultLocale(first);
                }
                else
                {
                    log.error("No default locale and no supported locales defined. Using java default locale.");
                    app.setDefaultLocale(Locale.getDefault());
                }
            }

            if (localeConfig.getSupportedLocales() != null &&
                !localeConfig.getSupportedLocales().isEmpty())
            {
                app.setSupportedLocales(localeConfig.getSupportedLocales());
            }
            else
            {
                log.warn("No supported locales defined.");
                app.setSupportedLocales(Collections.EMPTY_SET);
            }
        }

        for (Iterator it = _converterMap.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            String converterId = (String) entry.getKey();
            String converterClass = (String) entry.getValue();
            app.addConverter(converterId, converterClass);
        }
        
        for (Iterator it = _converterTypeMap.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            Class targetClass = (Class) entry.getKey();
            String converterClass = (String) entry.getValue();
            app.addConverter(targetClass, converterClass);
        }
        
        for (Iterator it = _componentClassMap.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            String componentType = (String) entry.getKey();
            String componentClass = (String) entry.getValue();
            app.addComponent(componentType, componentClass);
        }
        
        for (Iterator it = _validatorClassMap.entrySet().iterator(); it.hasNext();)
        {
            Entry entry = (Entry) it.next();
            String validatorId = (String) entry.getKey();
            String validatorClass = (String) entry.getValue();
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
    public void configureRenderKits(ExternalContext externalContext)
    {
        completeRendererComponentClasses();

        completeRendererAttributesByTLD(externalContext, TLD_HTML_URI);
        completeRendererAttributesByTLD(externalContext, TLD_EXT_URI);

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
     * Adds for each renderer's componentType the corresponding componentClass
     * if not already defined.
     */
    private void completeRendererComponentClasses()
    {
        for (Iterator rkIt = getRenderKitConfigs(); rkIt.hasNext(); )
        {
            RenderKitConfig rkc = (RenderKitConfig)rkIt.next();
            for (Iterator rendIt = rkc.getRendererConfigs(); rendIt.hasNext(); )
            {
                RendererConfig rc = (RendererConfig)rendIt.next();
                for (Iterator ctIt = rc.getSupportedComponentTypes(); ctIt.hasNext(); )
                {
                    String compType = (String)ctIt.next();
                    String className = (String)_componentClassMap.get(compType);
                    if (className == null)
                    {
                        log.error("Undefined component type " + compType + ", cannot complete renderer config " + rc.getRendererType());
                    }
                    else
                    {
                        Class clazz = ConfigUtil.classForName(className);
                        if (!rc.supportsComponentClass(clazz))
                        {
                            rc.addSupportedComponentClass(clazz);
                        }
                    }
                }
            }
        }

    }

    /**
     * Reads additional renderer attribute information from the given
     * Taglib descriptor.
     */
    protected void completeRendererAttributesByTLD(ExternalContext context,
                                                   String taglibURI)
    {
        TagLibraryInfo tagLibraryInfo = TLDInfo.getTagLibraryInfo(context,
                                                                  taglibURI);
        TagInfo[] tagInfos = tagLibraryInfo.getTags();
        for (int i = 0; i < tagInfos.length; i++)
        {
            TagInfo tagInfo = tagInfos[i];
            completeRendererAttributesByTagInfo(tagInfo);
        }
    }

    private void completeRendererAttributesByTagInfo(TagInfo tagInfo)
    {
        Tag tag = (Tag) ConfigUtil.newInstance(tagInfo.getTagClassName());
        if (!(tag instanceof UIComponentTag))
        {
            return;
        }

        String rendererType = ((UIComponentTag)tag).getRendererType();
        TagAttributeInfo[] tagAttributeInfos = tagInfo.getAttributes();

        for (Iterator rkIt = getRenderKitConfigs(); rkIt.hasNext();)
        {
            RenderKitConfig rkc = (RenderKitConfig)rkIt.next();
            RendererConfig rc = rkc.getRendererConfig(rendererType);
            if (rc != null)
            {
                for (int i = 0; i < tagAttributeInfos.length; i++)
                {
                    TagAttributeInfo tagAttributeInfo = tagAttributeInfos[i];
                    addRendererAttribute(rc, tagAttributeInfo);
                }
            }
        }
    }

    private void addRendererAttribute(RendererConfig rendererConfig,
                                      TagAttributeInfo tagAttributeInfo)
    {
        String name = tagAttributeInfo.getName();
        if (name.equals("id"))
        {
            return;
        }

        String className = tagAttributeInfo.getTypeName();
        if (className == null)
        {
            className = String.class.getName(); //TODO: or Object?
        }

        AttributeConfig attributeConfig = rendererConfig.getAttributeConfig(name);
        if (attributeConfig == null)
        {
            attributeConfig = new AttributeConfig();
            attributeConfig.setAttributeName(name);
            attributeConfig.setAttributeClass(className);
            rendererConfig.addAttributeConfig(attributeConfig);
//System.out.println("Added renderer attribute '" + name + "' for Renderer '" + rendererConfig.getRendererType() + "'.");
        }
        else
        {
            String attributeClassName = attributeConfig.getAttributeClass();
            if (attributeClassName == null)
            {
                attributeConfig.setAttributeClass(className);
            }
            else if (!attributeClassName.equals(className))
            {
                log.warn("Error in faces-config.xml - inconsistency with TLD: Attribute '" + name + "' of renderer '" +  rendererConfig.getRendererType() + "' has different class in Taglib descriptor.");
            }
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

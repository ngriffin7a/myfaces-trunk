/**
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

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.component.UIComponent;
import javax.faces.context.MessageResources;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;
import java.util.*;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesConfig
    implements Config
{
    private ApplicationConfig _applicationConfig;
    private Map _converterMap;
    private Map _componentClassMap;
    private Map _messageRessourcesMap;
    private Map _validatorMap;
    private Map _managedBeanConfigMap;
    private List _navigationRuleConfigList;
    private Map _referencedBeanConfigMap;
    private Map _renderKitConfigMap;


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
            if (applicationConfig.getActionListener() != null)
            {
                _applicationConfig.setActionListener(applicationConfig.getActionListener());
            }

            if (applicationConfig.getNavigationHandler() != null)
            {
                _applicationConfig.setNavigationHandler(applicationConfig.getNavigationHandler());
            }

            if (applicationConfig.getPropertyResolver() != null)
            {
                _applicationConfig.setPropertyResolver(applicationConfig.getPropertyResolver());
            }

            if (applicationConfig.getVariableResolver() != null)
            {
                _applicationConfig.setVariableResolver(applicationConfig.getVariableResolver());
            }
        }
    }


    public void addConverterConfig(ConverterConfig converterConfig)
    {
        getConverterMap().put(converterConfig.getConverterId(),
                              converterConfig.newConverter());
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
            throw new FacesException("Unknown converter id '" + converterId + "'.");
        }
        return converter;
    }

    public Iterator getConverterIds()
    {
        return getConverterMap().keySet().iterator();
    }

    private Map getConverterMap()
    {
        if (_converterMap == null)
        {
            _converterMap = new HashMap();
        }
        return _converterMap;
    }





    public void addComponentConfig(ComponentConfig componentConfig)
    {
        addComponent(componentConfig.getComponentType(),
                     componentConfig.getComponentClass());
    }

    public void addComponent(String componentType, String componentClass)
    {
        getComponentClassMap().put(componentType, componentClass);
    }

    public UIComponent getComponent(String componentType) throws FacesException
    {
        String componentClass = (String)getComponentClassMap().get(componentType);
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
        if (_componentClassMap == null)
        {
            _componentClassMap = new HashMap();
        }
        return _componentClassMap;
    }




    public void addMessageResourcesConfig(MessageResourcesConfig newMessageResourcesConfig)
    {
        String id = newMessageResourcesConfig.getMessageResourcesId();
        MessageResources oldMR = (MessageResources)getMessageResourcesMap().get(id);
        if (oldMR == null)
        {
            getMessageResourcesMap().put(id, newMessageResourcesConfig);
        }
        else
        {
            if (!(oldMR instanceof MessageResourcesConfig))
            {
                throw new FacesException("Duplicate MessageResources id '" + id + "'");
            }

            MessageResourcesConfig oldMessageResourcesConfig = (MessageResourcesConfig)oldMR;
            for (Iterator it = newMessageResourcesConfig.getMessageConfigMap().values().iterator(); it.hasNext(); )
            {
                MessageConfig mc = (MessageConfig)it.next();
                oldMessageResourcesConfig.addMessageConfig(mc);
            }
        }
    }

    public void addMessageResources(String id, String messageResourcesClass)
    {
        if (getMessageResourcesMap().put(id,
                                         ConfigUtil.newInstance(messageResourcesClass)) != null)
        {
            throw new FacesException("Duplicate MessageResources id '" + id + "'");
        }

    }

    public MessageResources getMessageResources(String id) throws FacesException
    {
        MessageResources mr = (MessageResources)getMessageResourcesMap().get(id);
        if (mr == null)
        {
            throw new FacesException("Unknown MessageResources id '" + id + "'.");
        }
        return mr;
    }

    public Iterator getMessageResourcesIds()
    {
        return getMessageResourcesMap().keySet().iterator();
    }

    private Map getMessageResourcesMap()
    {
        if (_messageRessourcesMap == null)
        {
            _messageRessourcesMap = new HashMap();
        }
        return _messageRessourcesMap;
    }



    public void addValidatorConfig(ValidatorConfig validatorConfig)
    {
        addValidator(validatorConfig.getValidatorId(),
                     validatorConfig.getValidatorClass());
    }

    public void addValidator(String validatorId, String validatorClass)
    {
        Validator validator = (Validator)ConfigUtil.newInstance(validatorClass);
        getValidatorMap().put(validatorId, validator);
    }

    public Validator getValidator(String validatorId) throws FacesException
    {
        Validator validator = (Validator)getValidatorMap().get(validatorId);
        if (validator == null)
        {
            throw new FacesException("Unknown validator id '" + validatorId + "'.");
        }
        return validator;
    }

    public Iterator getValidatorIds()
    {
        return getValidatorMap().keySet().iterator();
    }

    private Map getValidatorMap()
    {
        if (_validatorMap == null)
        {
            _validatorMap = new HashMap();
        }
        return _validatorMap;
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
        if (_managedBeanConfigMap == null)
        {
            _managedBeanConfigMap = new HashMap();
        }
        return _managedBeanConfigMap;
    }



    public void addNavigationRuleConfig(NavigationRuleConfig navigationRuleConfig)
    {
        if (_navigationRuleConfigList == null)
        {
            _navigationRuleConfigList = new ArrayList();
        }
        _navigationRuleConfigList.add(navigationRuleConfig);
    }

    public List getNavigationCaseConfigList()
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
        if (_referencedBeanConfigMap == null)
        {
            _referencedBeanConfigMap = new HashMap();
        }
        return _referencedBeanConfigMap;
    }



    public void addRenderKitConfig(RenderKitConfig renderKitConfig)
    {
        getRenderKitConfigMap().put(renderKitConfig.getRenderKitId(),
                                    renderKitConfig);
    }

    public RenderKitConfig getRenderKitConfig(String renderKitId)
    {
        return (RenderKitConfig)getRenderKitConfigMap().get(renderKitId);
    }

    public Iterator getRenderKitIds()
    {
        return getRenderKitConfigMap().keySet().iterator();
    }

    private Map getRenderKitConfigMap()
    {
        if (_renderKitConfigMap == null)
        {
            _renderKitConfigMap = new HashMap();
        }
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


}

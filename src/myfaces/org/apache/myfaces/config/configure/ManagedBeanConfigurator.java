/*
 * MyFaces - the free JSF implementation Copyright (C) 2003, 2004 The MyFaces
 * Team (http://myfaces.sourceforge.net)
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.config.configure;

import net.sourceforge.myfaces.config.*;
import net.sourceforge.myfaces.el.PropertyResolverImpl;
import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.util.HashMapUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ValueBinding;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ManagedBeanConfigurator
{
    //private static final Log log =
    // LogFactory.getLog(ManagedBeanConfigurator.class);
    private static final ManagedBeanConfigurator _instance = new ManagedBeanConfigurator();

    private ManagedBeanConfigurator()
    {
        // singleton
    }

    public static ManagedBeanConfigurator getInstance()
    {
        return _instance;
    }

    public void configure(FacesContext facesContext,
        ManagedBeanConfig managedBeanConfig, Object bean)
    {
        switch (managedBeanConfig.getType())
        {
            case ManagedBeanConfig.TYPE_PROPERTIES:
                for (Iterator it = managedBeanConfig
                    .getManagedPropertyConfigList().iterator(); it.hasNext();)
                {
                    ManagedPropertyConfig propertyConfig = 
                        (ManagedPropertyConfig) it.next();
                    setBeanProperty(facesContext, propertyConfig, bean);
                }
                break;

            case ManagedBeanConfig.TYPE_MAP:
                if (!(bean instanceof Map))
                {
                    throw new IllegalArgumentException(
                        "Class " + bean.getClass().getName() 
                        +  " of managed bean " 
                        + managedBeanConfig.getManagedBeanName() 
                        + " is not instance of Map.");
                }
                configureMap(facesContext, managedBeanConfig
                    .getMapEntriesConfig(), (Map) bean);
                break;

            case ManagedBeanConfig.TYPE_LIST:
                if (!(bean instanceof List))
                {
                    throw new IllegalArgumentException(
                        "Class " + bean.getClass().getName() 
                        +  " of managed bean " 
                        + managedBeanConfig.getManagedBeanName() 
                        + " is not instance of List.");
                }
                configureList(facesContext, managedBeanConfig
                    .getListEntriesConfig(), (List) bean);
                break;

            case ManagedBeanConfig.TYPE_NO_INIT:
                // no init values
                break;

            default:
                throw new IllegalStateException(
                    "Unknown managed bean type " 
                    + bean.getClass().getName() + " for managed bean "
                    + managedBeanConfig.getManagedBeanName() + '.');
        }
    }


    private void setBeanProperty(FacesContext facesContext,
        ManagedPropertyConfig propertyConfig, Object bean)
    {
        switch (propertyConfig.getType())
        {
            case ManagedPropertyConfig.TYPE_OBJECT:
                PropertyResolver propertyResolver = facesContext
                    .getApplication().getPropertyResolver();

                if (propertyConfig.getPropertyClass() == null)
                {
                    // If class was not specified in the config,
                    // we determine the property type here, coerce the value,
                    // and "cache" the result. This can only be done here,
                    // since otherwise we may not have an active Application
                    // instance, and thus cannot request the PropertyResolver.
                    // WARNING: this does not work if PR changes during execution
                    // Note: concurrent update is OK since the result is 
                    //       the same for each thread
                    Class type = propertyResolver.getType(
                        bean, propertyConfig.getPropertyName());
                    Object coercedValue = ClassUtils.convertToType(
                        propertyConfig.getValue(), type);
                    
                    // Note: must call updateValue() first (we check on type!)
                    propertyConfig.setValue(coercedValue);
                    propertyConfig.setPropertyClass(type);
                }

                propertyResolver.setValue(bean, propertyConfig
                    .getPropertyName(), propertyConfig.getValue());
                break;

            case ManagedPropertyConfig.TYPE_VALUE_BINDING:
                if (propertyConfig.getValueBinding() == null)
                {
                    // similar as above
                    Class type = propertyConfig.getPropertyClass();
                    if (type == null)
                    {
                        propertyResolver = facesContext
                            .getApplication().getPropertyResolver();
                        type = propertyResolver.getType(
                            bean, propertyConfig.getPropertyName());
                        propertyConfig.setPropertyClass(type);
                    }
                    
                    propertyConfig.setValueBinding(facesContext.getApplication()
                        .createValueBinding((String) propertyConfig.getValue()));
                }
                
                ValueBinding vb = propertyConfig.getValueBinding();
                Object value = vb.getValue(facesContext);
                PropertyResolverImpl.setProperty(bean,
                    propertyConfig.getPropertyName(), 
                    ClassUtils.convertToType(
                        value, propertyConfig.getPropertyClass())); 
                break;

            case ManagedPropertyConfig.TYPE_LIST:
                setListProperty(facesContext, propertyConfig, bean);
                break;

            case ManagedPropertyConfig.TYPE_MAP:
                setMapProperty(facesContext, propertyConfig, bean);
                break;

            case ManagedPropertyConfig.TYPE_UNKNOWN:
            default:
                throw new IllegalArgumentException(
                    "Unsupported ManagedPropertyConfig type for managed property "
                        + propertyConfig.getPropertyName());
        }
    }


    private void setListProperty(FacesContext facesContext,
        ManagedPropertyConfig propertyConfig, Object bean)
    {
        ListEntriesConfig listEntriesConfig = 
            propertyConfig.getListEntriesConfig();
        PropertyResolver propertyResolver = 
            facesContext.getApplication().getPropertyResolver();
        List list = (List) propertyResolver.getValue(
            bean, propertyConfig.getPropertyName());
        if (list == null)
        {
            // list is null, create a new one
            list = new ArrayList(listEntriesConfig.getValues().size());
            propertyResolver.setValue(
                bean, propertyConfig.getPropertyName(), list);
        }

        //add list entries
        configureList(facesContext, listEntriesConfig, list);
    }


    private void configureList(FacesContext facesContext,
        ListEntriesConfig listEntriesConfig, List list)
    {
        if (listEntriesConfig.isContainsValueBindings())
        {
            // copy one by one
            for (Iterator it = listEntriesConfig.getValues().iterator(); it
                .hasNext();)
            {
                Object value = it.next();
                if (value instanceof ValueBindingExpression)
                {
                    ValueBinding vb = ((ValueBindingExpression) value)
                        .getValueBinding(facesContext, listEntriesConfig
                            .getValueClass());
                    list.add(vb.getValue(facesContext));
                }
                else
                {
                    list.add(value);
                }
            }
        }
        else
        {
            list.addAll(listEntriesConfig.getValues());
        }
    }


    private void setMapProperty(FacesContext facesContext, 
        ManagedPropertyConfig propertyConfig, Object bean)
    {
        MapEntriesConfig mapEntriesConfig = 
            propertyConfig.getMapEntriesConfig();
        PropertyResolver propertyResolver = 
            facesContext.getApplication().getPropertyResolver();
        Map map = (Map) propertyResolver.getValue(
            bean, propertyConfig.getPropertyName());
        if (map == null)
        {
            // map is null, create a new one
            map = new HashMap(HashMapUtils.calcCapacity(
                mapEntriesConfig.getMap().size()));
            propertyResolver.setValue(
                bean, propertyConfig.getPropertyName(), map);
        }

        //add map entries
        configureMap(facesContext, mapEntriesConfig, map);
    }


    private void configureMap(FacesContext facesContext,
        MapEntriesConfig mapEntriesConfig, Map map)
    {
        if (mapEntriesConfig.isContainsValueBindings())
        {
            for (Iterator it = mapEntriesConfig.getMap().entrySet().iterator(); 
                it.hasNext();)
            {
                Map.Entry mapEntry = (Map.Entry) it.next();
                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();
                if (value instanceof ValueBindingExpression)
                {
                    ValueBinding vb = ((ValueBindingExpression) value)
                        .getValueBinding(facesContext, mapEntriesConfig
                            .getValueClass());
                    map.put(key, vb.getValue(facesContext));
                }
                else
                {
                    map.put(key, value);
                }
            }
        }
        else
        {
            map.putAll(mapEntriesConfig.getMap());
        }
    }

}
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
package net.sourceforge.myfaces.config.configure;

import net.sourceforge.myfaces.config.*;
import net.sourceforge.myfaces.el.PropertyResolverImpl;
import net.sourceforge.myfaces.util.HashMapUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ManagedBeanConfigurator
{
    //private static final Log log = LogFactory.getLog(ManagedBeanConfigurator.class);

    private ManagedBeanConfig _managedBeanConfig;

    public ManagedBeanConfigurator(ManagedBeanConfig managedBeanConfig)
    {
        _managedBeanConfig = managedBeanConfig;
    }

    public void configure(FacesContext facesContext, Object bean)
    {
        switch (_managedBeanConfig.getType())
        {
            case ManagedBeanConfig.TYPE_PROPERTIES:
                for (Iterator it = _managedBeanConfig.getManagedPropertyConfigList().iterator(); it.hasNext(); )
                {
                    ManagedPropertyConfig propertyConfig = (ManagedPropertyConfig)it.next();
                    setBeanProperty(facesContext, bean, propertyConfig);
                }
                break;

            case ManagedBeanConfig.TYPE_MAP:
                if (!(bean instanceof Map))
                {
                    throw new IllegalArgumentException("Class of managed bean " + _managedBeanConfig.getManagedBeanName() + " is no Map.");
                }
                configureMap(facesContext, _managedBeanConfig.getMapEntriesConfig(), (Map)bean);
                break;

            case ManagedBeanConfig.TYPE_LIST:
                if (!(bean instanceof List))
                {
                    throw new IllegalArgumentException("Class of managed bean " + _managedBeanConfig.getManagedBeanName() + " is no List.");
                }
                configureList(facesContext, _managedBeanConfig.getListEntriesConfig(), (List)bean);
                break;

            case ManagedBeanConfig.TYPE_NO_INIT:
                // no init values
                break;

            default:
                throw new IllegalStateException("Unknown managed bean type for bean " + _managedBeanConfig.getManagedBeanName());
        }
    }

    
    private void setBeanProperty(FacesContext facesContext,
                                 Object bean,
                                 ManagedPropertyConfig propertyConfig)
    {
        switch (propertyConfig.getType())
        {
            case ManagedPropertyConfig.TYPE_OBJECT:
                PropertyResolverImpl.setProperty(bean,
                        propertyConfig.getPropertyName(), propertyConfig.getValue());
                break;

            case ManagedPropertyConfig.TYPE_VALUE_BINDING:
                ValueBinding vb = facesContext.getApplication()
                                        .createValueBinding((String)propertyConfig.getValue());
                ConfigUtils.checkValueBindingType(facesContext, vb, propertyConfig.getPropertyClass());
                PropertyResolverImpl.setProperty(bean,
                        propertyConfig.getPropertyName(), vb.getValue(facesContext));
                break;

            case ManagedPropertyConfig.TYPE_LIST:
                setListProperty(facesContext, bean, propertyConfig);
                break;

            case ManagedPropertyConfig.TYPE_MAP:
                setMapProperty(facesContext, bean, propertyConfig);
                break;

            case ManagedPropertyConfig.TYPE_UNKNOWN:
            default:
                throw new IllegalArgumentException("Unsupported ManagedPropertyConfig type for managed property " + propertyConfig.getPropertyName());
        }
    }


    private void setListProperty(FacesContext facesContext,
                                 Object bean,
                                 ManagedPropertyConfig propertyConfig)
    {
        ListEntriesConfig listEntriesConfig = propertyConfig.getListEntriesConfig();

        List list = (List) PropertyResolverImpl.getProperty(bean, propertyConfig.getPropertyName());
        if (list == null)
        {
            // list is null, create a new one
            list = new ArrayList(listEntriesConfig.getValues().size());
            PropertyResolverImpl.setProperty(bean, propertyConfig.getPropertyName(), list);
        }

        //add list entries
        configureList(facesContext, listEntriesConfig, list);
    }


    private void configureList(FacesContext facesContext,
                               ListEntriesConfig listEntriesConfig,
                               List list)
    {
        if (listEntriesConfig.isContainsValueBindings())
        {
            // copy one by one
            for (Iterator it = listEntriesConfig.getValues().iterator(); it.hasNext();)
            {
                Object value = it.next();
                if (value instanceof ValueBindingExpression)
                {
                    ValueBinding vb = ((ValueBindingExpression)value).getValueBinding(facesContext,
                                                                                      listEntriesConfig.getValueClass());
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
                                Object bean,
                                ManagedPropertyConfig propertyConfig)
    {
        MapEntriesConfig mapEntriesConfig = propertyConfig.getMapEntriesConfig();
        Map map = (Map) PropertyResolverImpl.getProperty(bean, propertyConfig.getPropertyName());
        if (map == null)
        {
            // map is null, create a new one
            map = new HashMap(HashMapUtils.calcCapacity(mapEntriesConfig.getMap().size()));
            PropertyResolverImpl.setProperty(bean, propertyConfig.getPropertyName(), map);
        }

        //add map entries
        configureMap(facesContext, mapEntriesConfig, map);
    }


    private void configureMap(FacesContext facesContext,
                              MapEntriesConfig mapEntriesConfig,
                              Map map)
    {
        if (mapEntriesConfig.isContainsValueBindings())
        {
            for (Iterator it = mapEntriesConfig.getMap().entrySet().iterator(); it.hasNext();)
            {
                Map.Entry mapEntry = (Map.Entry)it.next();
                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();
                if (value instanceof ValueBindingExpression)
                {
                    ValueBinding vb = ((ValueBindingExpression)value).getValueBinding(facesContext,
                                                                                      mapEntriesConfig.getValueClass());
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

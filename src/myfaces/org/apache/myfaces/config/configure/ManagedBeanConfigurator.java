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
import net.sourceforge.myfaces.util.HashMapUtils;
import net.sourceforge.myfaces.util.bean.BeanUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.util.*;

/**
 * @author Manfred Geiler (latest modification by $Author$)
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
                else
                {
                    MapEntriesConfigurator configurator
                            = new MapEntriesConfigurator(_managedBeanConfig.getMapEntriesConfig());
                    configurator.configure(facesContext, (Map)bean);
                }
                break;

            case ManagedBeanConfig.TYPE_LIST:
                if (!(bean instanceof List))
                {
                    throw new IllegalArgumentException("Class of managed bean " + _managedBeanConfig.getManagedBeanName() + " is no List.");
                }
                else
                {
                    ListEntriesConfigurator configurator
                            = new ListEntriesConfigurator(_managedBeanConfig.getListEntriesConfig());
                    configurator.configure(facesContext, (List)bean);
                }
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
                BeanUtils.setBeanPropertyValue(bean,
                                               propertyConfig.getPropertyName(),
                                               propertyConfig.getValue());
                break;

            case ManagedPropertyConfig.TYPE_VALUE_BINDING:
                ValueBinding vb = facesContext.getApplication()
                                        .createValueBinding((String)propertyConfig.getValue());
                ConfigUtils.checkValueBindingType(facesContext, vb, propertyConfig.getPropertyClass());
                BeanUtils.setBeanPropertyValue(bean,
                                               propertyConfig.getPropertyName(),
                                               vb.getValue(facesContext));
                break;

            case ManagedPropertyConfig.TYPE_LIST:
                ListEntriesConfig listEntriesConfig = propertyConfig.getListEntriesConfig();
                if (listEntriesConfig.isContainsValueBindings())
                {
                    List list = new ArrayList(listEntriesConfig.getValues().size());
                    ListEntriesConfigurator listEntriesConfigurator
                            = new ListEntriesConfigurator(listEntriesConfig);
                    listEntriesConfigurator.configure(facesContext, list);
                }
                else
                {
                    BeanUtils.setBeanPropertyValue(bean,
                                                   propertyConfig.getPropertyName(),
                                                   listEntriesConfig.getValues());
                }
                break;

            case ManagedPropertyConfig.TYPE_MAP:
                MapEntriesConfig mapEntriesConfig = propertyConfig.getMapEntriesConfig();
                if (mapEntriesConfig.isContainsValueBindings())
                {
                    Map map = new HashMap(HashMapUtils.calcCapacity(mapEntriesConfig.getMap().size()));
                    MapEntriesConfigurator mapEntriesConfigurator
                            = new MapEntriesConfigurator(mapEntriesConfig);
                    mapEntriesConfigurator.configure(facesContext, map);
                }
                else
                {
                    BeanUtils.setBeanPropertyValue(bean,
                                                   propertyConfig.getPropertyName(),
                                                   mapEntriesConfig.getMap());
                }
                break;

            case ManagedPropertyConfig.TYPE_UNKNOWN:
            default:
                throw new IllegalArgumentException("Unsupported ManagedPropertyConfig type for managed property " + propertyConfig.getPropertyName());
        }
    }

}

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

package net.sourceforge.myfaces.confignew;

import java.util.*;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.webapp.UIComponentTag;
import javax.faces.el.ValueBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.context.FacesContext;

import net.sourceforge.myfaces.confignew.element.*;
import net.sourceforge.myfaces.util.ClassUtils;
import net.sourceforge.myfaces.config.ManagedPropertyConfig;


/**
 * Create and initialize managed beans
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class ManagedBeanBuilder
{

    public Object buildManagedBean(FacesContext facesContext, ManagedBean beanConfiguration) throws FacesException
    {
        Object bean = ClassUtils.newInstance(beanConfiguration.getManagedBeanClass());

        switch (beanConfiguration.getInitMode())
        {
            case ManagedBean.INIT_MODE_PROPERTIES:
                initializeProperties(facesContext, beanConfiguration.getManagedProperties(), bean);
                break;

            case ManagedBean.INIT_MODE_MAP:
                if (!(bean instanceof Map))
                {
                    throw new IllegalArgumentException("Class " + bean.getClass().getName()
                        + " of managed bean "
                        + beanConfiguration.getManagedBeanName()
                        + " is not a Map.");
                }
                initializeMap(facesContext, beanConfiguration.getMapEntries(), (Map) bean);
                break;

            case ManagedBean.INIT_MODE_LIST:
                if (!(bean instanceof List))
                {
                    throw new IllegalArgumentException("Class " + bean.getClass().getName()
                        + " of managed bean "
                        + beanConfiguration.getManagedBeanName()
                        + " is not a List.");
                }
                initializeList(facesContext, beanConfiguration.getListEntries(), (List) bean);
                break;

            case ManagedBean.INIT_MODE_NO_INIT:
                // no init values
                break;

            default:
                throw new IllegalStateException("Unknown managed bean type "
                    + bean.getClass().getName() + " for managed bean "
                    + beanConfiguration.getManagedBeanName() + '.');
        }
        return bean;
    }


    private void initializeProperties(FacesContext facesContext, Iterator managedProperties, Object bean)
    {
        while (managedProperties.hasNext())
        {
            ManagedProperty property = (ManagedProperty) managedProperties.next();
            Object value = null;

            switch (property.getType())
            {
                case ManagedProperty.TYPE_LIST:
                    value = new ArrayList();
                    initializeList(facesContext, property.getListEntries(), (List) value);
                    break;
                case ManagedProperty.TYPE_MAP:
                    value = new HashMap();
                    initializeMap(facesContext, property.getMapEntries(), (Map) value);
                    break;
                case ManagedProperty.TYPE_NULL:
                    value = null;
                    break;
                case ManagedProperty.TYPE_VALUE:
                    ValueBinding valueBinding = property.getValueBinding();

                    if (valueBinding == null)
                    {
                        if (UIComponentTag.isValueReference(property.getValue()))
                        {
                            valueBinding = facesContext.getApplication().createValueBinding(property.getValue());
                            property.setValueBinding(valueBinding);
                        }
                    }

                    if (valueBinding == null)
                    {
                        value = property.getValue();
                    } else
                    {
                        value = valueBinding.getValue(facesContext);
                    }

            }
            PropertyResolver propertyResolver = facesContext.getApplication().getPropertyResolver();
            Class propertyClass = null;

            if (property.getPropertyClass() == null)
            {
                propertyClass = propertyResolver.getType(bean, property.getPropertyName());
            } else
            {
                propertyClass = ClassUtils.classForName(property.getPropertyClass());
            }
            Object coercedValue = ClassUtils.convertToType(value, propertyClass);
            propertyResolver.setValue(bean, property.getPropertyName(), coercedValue);
        }
    }


    private void initializeMap(FacesContext facesContext, MapEntries mapEntries, Map map)
    {
        Application application = facesContext.getApplication();
        Class keyClass = mapEntries.getKeyClass() == null ? String.class : ClassUtils.classForName(mapEntries.getKeyClass());
        Class valueClass = mapEntries.getValueClass() == null ? String.class : ClassUtils.classForName(mapEntries.getValueClass());
        ValueBinding valueBinding;

        for (Iterator iterator = mapEntries.getMapEntries(); iterator.hasNext();)
        {
            MapEntry entry = (MapEntry) iterator.next();
            Object key = entry.getKey();

            if (UIComponentTag.isValueReference((String) key))
            {
                valueBinding = application.createValueBinding((String) key);
                key = valueBinding.getValue(facesContext);
            }

            if (entry.isNullValue())
            {
                map.put(ClassUtils.convertToType(key, keyClass), null);
            } else
            {
                Object value = entry.getValue();
                if (UIComponentTag.isValueReference((String) value))
                {
                    valueBinding = application.createValueBinding((String) value);
                    value = valueBinding.getValue(facesContext);
                }
                map.put(ClassUtils.convertToType(key, keyClass), ClassUtils.convertToType(value, valueClass));
            }
        }
    }


    private void initializeList(FacesContext facesContext, ListEntries listEntries, List list)
    {
        Application application = facesContext.getApplication();
        Class valueClass = listEntries.getValueClass() == null ? String.class : ClassUtils.classForName(listEntries.getValueClass());
        ValueBinding valueBinding;

        for (Iterator iterator = listEntries.getListEntries(); iterator.hasNext();)
        {
            ListEntry entry = (ListEntry) iterator.next();
            if (entry.isNullValue())
            {
                list.add(null);
            } else
            {
                Object value = entry.getValue();
                if (UIComponentTag.isValueReference((String) value))
                {
                    valueBinding = application.createValueBinding((String) value);
                    value = valueBinding.getValue(facesContext);
                }
                list.add(ClassUtils.convertToType(value, valueClass));
            }
        }
    }
}

/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.config;

import net.sourceforge.myfaces.config.element.*;
import net.sourceforge.myfaces.util.ClassUtils;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import java.util.*;


/**
 * Create and initialize managed beans
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 *
 * $Log$
 * Revision 1.2  2004/08/10 10:57:38  manolito
 * fixed StackOverflow in ClassUtils and cleaned up ClassUtils methods
 *
 * Revision 1.1  2004/07/07 00:25:05  o_rossmueller
 * tidy up config/confignew package (moved confignew classes to package config)
 *
 */
public class ManagedBeanBuilder
{

    public Object buildManagedBean(FacesContext facesContext, ManagedBean beanConfiguration) throws FacesException
    {
        Object bean = ClassUtils.newInstance(beanConfiguration.getManagedBeanClassName());

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
            }
            else
            {
                propertyClass = ClassUtils.simpleJavaTypeToClass(property.getPropertyClass());
            }
            Object coercedValue = ClassUtils.convertToType(value, propertyClass);
            propertyResolver.setValue(bean, property.getPropertyName(), coercedValue);
        }
    }


    private void initializeMap(FacesContext facesContext, MapEntries mapEntries, Map map)
    {
        Application application = facesContext.getApplication();
        Class keyClass = mapEntries.getKeyClass() == null ? String.class : ClassUtils.simpleJavaTypeToClass(mapEntries.getKeyClass());
        Class valueClass = mapEntries.getValueClass() == null ? String.class : ClassUtils.simpleJavaTypeToClass(mapEntries.getValueClass());
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
        Class valueClass = listEntries.getValueClass() == null ? String.class : ClassUtils.simpleJavaTypeToClass(listEntries.getValueClass());
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

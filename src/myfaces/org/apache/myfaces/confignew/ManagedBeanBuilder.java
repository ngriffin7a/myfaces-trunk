/*
 * $Id$
 *
 * Copyright 2004 Oliver Rossmueller
 *
 * This file is part of tuxerra.
 *
 * tuxerra is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 * tuxerra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with welofunc; if not, mailto:oliver@tuxerra.com or have a look at
 * http://www.gnu.org/licenses/licenses.html#GPL
 */
package net.sourceforge.myfaces.confignew;

import java.util.*;
import javax.faces.FacesException;
import javax.faces.webapp.UIComponentTag;
import javax.faces.el.ValueBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.context.FacesContext;

import net.sourceforge.myfaces.confignew.element.ManagedBean;
import net.sourceforge.myfaces.confignew.element.ListEntries;
import net.sourceforge.myfaces.confignew.element.MapEntries;
import net.sourceforge.myfaces.confignew.element.ManagedProperty;
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
                case ManagedProperty.TYPE_MAP:
                    value = new HashMap();
                    initializeMap(facesContext, property.getMapEntries(), (Map) value);
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

    }


    private void initializeList(FacesContext facesContext, ListEntries listEntries, List list)
    {

    }
}

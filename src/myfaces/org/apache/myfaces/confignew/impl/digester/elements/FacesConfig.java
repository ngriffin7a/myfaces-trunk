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

package net.sourceforge.myfaces.confignew.impl.digester.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class FacesConfig
{

    private List applications = new ArrayList();
    private List factories = new ArrayList();
    private Map components = new HashMap();
    private List converters = new ArrayList();
    private List managedBeans = new ArrayList();
    private List navigationRules = new ArrayList();
    private List renderKits = new ArrayList();
    private List lifecyclePhaseListener = new ArrayList();
    private Map validators = new HashMap();


    public void addApplication(Application application)
    {
        applications.add(application);
    }


    public void addFactory(Factory factory)
    {
        factories.add(factory);
    }


    public void addComponent(String componentType, String componentClass)
    {
        components.put(componentType, componentClass);
    }


    public void addConverter(Converter converter)
    {
        converters.add(converter);
    }


    public void addManagedBean(ManagedBean bean)
    {
        managedBeans.add(bean);
    }


    public void addNavigationRule(NavigationRule rule)
    {
        navigationRules.add(rule);
    }


    public void addRenderKit(RenderKit renderKit)
    {
        renderKits.add(renderKit);
    }


    public void addlifecyclePhaseListener(String value)
    {
        lifecyclePhaseListener.add(value);
    }


    public void addValidator(String id, String validatorClass)
    {
        validators.put(id, validatorClass);
    }


    public List getApplications()
    {
        return applications;
    }


    public List getFactories()
    {
        return factories;
    }


    public Map getComponents()
    {
        return components;
    }


    public List getConverters()
    {
        return converters;
    }


    public List getManagedBeans()
    {
        return managedBeans;
    }


    public List getNavigationRules()
    {
        return navigationRules;
    }


    public List getRenderKits()
    {
        return renderKits;
    }


    public List getLifecyclePhaseListener()
    {
        return lifecyclePhaseListener;
    }


    public Map getValidators()
    {
        return validators;
    }
}

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

import net.sourceforge.myfaces.config.element.ManagedBean;
import net.sourceforge.myfaces.config.element.NavigationRule;
import net.sourceforge.myfaces.config.element.ManagedBean;

import javax.faces.context.ExternalContext;
import java.util.*;

/**
 * Holds all configuration information that is needed later during runtime. 
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/07/07 00:25:05  o_rossmueller
 * tidy up config/confignew package (moved confignew classes to package config)
 *
 * Revision 1.4  2004/07/01 22:05:09  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/06/16 23:02:24  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.2.2.1  2004/06/16 01:25:52  o_rossmueller
 * refactorings: FactoryFinder, decorator creation, dispenser (removed reverse order)
 * bug fixes
 * additional tests
 *
 * Revision 1.2  2004/06/08 20:50:09  o_rossmueller
 * completed configurator
 *
 * Revision 1.1  2004/05/17 14:28:28  manolito
 * new configuration concept
 *
 */
public class RuntimeConfig
{
    private static final String APPLICATION_MAP_PARAM_NAME = RuntimeConfig.class.getName();

    private Collection _navigationRules = new ArrayList();
    private Map _managedBeans = new HashMap();


    public static RuntimeConfig getCurrentInstance(ExternalContext externalContext)
    {
        RuntimeConfig runtimeConfig
                = (RuntimeConfig)externalContext.getApplicationMap().get(APPLICATION_MAP_PARAM_NAME);
        if (runtimeConfig == null)
        {
            runtimeConfig = new RuntimeConfig();
            externalContext.getApplicationMap().put(APPLICATION_MAP_PARAM_NAME, runtimeConfig);            
        }
        return runtimeConfig;
    }

    /**
     * Return the navigation rules that can be used by the NavigationHandler implementation.
     * @return a Collection of {@link net.sourceforge.myfaces.config.element.NavigationRule NavigationRule}s
     */
    public Collection getNavigationRules()
    {
        return Collections.unmodifiableCollection(_navigationRules);
    }

    public void addNavigationRule(NavigationRule navigationRule)
    {
        _navigationRules.add(navigationRule);
    }

    /**
     * Return the managed bean info that can be used by the VariableResolver implementation.
     * @return a {@link net.sourceforge.myfaces.config.element.ManagedBean ManagedBean}
     */
    public ManagedBean getManagedBean(String name)
    {
        return (ManagedBean)_managedBeans.get(name);
    }

    public void addManagedBean(String name, ManagedBean managedBean)
    {
        _managedBeans.put(name, managedBean);
    }
}

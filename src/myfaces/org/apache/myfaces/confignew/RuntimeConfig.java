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

import net.sourceforge.myfaces.confignew.element.ManagedBean;
import net.sourceforge.myfaces.confignew.element.NavigationRule;

import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds all configuration information that is needed later during runtime. 
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
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
     * @return a Collection of {@link net.sourceforge.myfaces.confignew.element.NavigationRule NavigationRule}s
     */
    public Collection getNavigationRules()
    {
        return _navigationRules;
    }

    public void addNavigationRule(NavigationRule navigationRule)
    {
        _navigationRules.add(navigationRule);
    }

    /**
     * Return the managed bean info that can be used by the VariableResolver implementation.
     * @return a {@link net.sourceforge.myfaces.confignew.element.ManagedBean ManagedBean}
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

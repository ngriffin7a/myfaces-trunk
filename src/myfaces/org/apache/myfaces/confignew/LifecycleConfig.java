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
 * Holds lifecycle configuration information that is used by {@link javax.faces.webapp.FacesServlet]
 * to configure the lifecycle instance used.
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/06/08 20:50:09  o_rossmueller
 *          completed configurator
 *
 *          Revision 1.1  2004/05/17 14:28:28  manolito
 *          new configuration concept
 */
public class LifecycleConfig
{

    private static final String APPLICATION_MAP_PARAM_NAME = LifecycleConfig.class.getName();

    private Collection lifecyclePhaseListeners = new ArrayList();


    public static LifecycleConfig getCurrentInstance(ExternalContext externalContext)
    {
        LifecycleConfig lifecycleConfig
            = (LifecycleConfig) externalContext.getApplicationMap().get(APPLICATION_MAP_PARAM_NAME);
        if (lifecycleConfig == null)
        {
            lifecycleConfig = new LifecycleConfig();
            externalContext.getApplicationMap().put(APPLICATION_MAP_PARAM_NAME, lifecycleConfig);
        }
        return lifecycleConfig;
    }


    public void addLifecyclePhaseListener(PhaseListener listener)
    {
        lifecyclePhaseListeners.add(listener);
    }


    /**
     * Answer all phase listeners to be registered with the used lifecycle instance
     *
     * @return
     */
    public Collection getLifecyclePhaseListeners()
    {
        return lifecyclePhaseListeners;
    }
}

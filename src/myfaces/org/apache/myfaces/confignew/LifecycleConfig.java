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
package net.sourceforge.myfaces.confignew;

import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseListener;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Holds lifecycle configuration information that is used by {@link javax.faces.webapp.FacesServlet]
 * to configure the lifecycle instance used.
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.2  2004/07/01 22:05:08  mwessendorf
 *          ASF switch
 *
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

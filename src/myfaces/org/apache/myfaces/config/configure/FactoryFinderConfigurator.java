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

import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FactoryConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FactoryFinderConfigurator
{
    private static final Log log = LogFactory.getLog(FactoryFinderConfigurator.class);

    private FacesConfig _facesConfig;

    public FactoryFinderConfigurator(FacesConfig facesConfig)
    {
        _facesConfig = facesConfig;
    }

    public void configure()
    {
        if (log.isDebugEnabled()) log.debug("Configuring FactoryFinder");

        FactoryConfig factoryConfig = _facesConfig.getFactoryConfig();
        if (factoryConfig == null)
        {
            log.error("Could not find factory configuration in faces-factoryConfig");
            throw new NullPointerException("Could not find factory configuration in faces-factoryConfig");
        }
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
                                 factoryConfig.getApplicationFactory());
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                                 factoryConfig.getFacesContextFactory());
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                                 factoryConfig.getLifecycleFactory());
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                                 factoryConfig.getRenderKitFactory());

    }

}

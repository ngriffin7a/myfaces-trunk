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
package net.sourceforge.myfaces.config;

import javax.faces.FactoryFinder;

/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FactoryConfig
    implements Config
{
    //~ Instance fields ----------------------------------------------------------------------------

    private String  _applicationFactory;
    private String _facesContextFactory;
    private String _lifecycleFactory;
    private String _renderKitFactory;

    //~ Methods ------------------------------------------------------------------------------------

    public void setFactory(String factoryName, String className) {
		// FIXME: Need to stop being lazy and do this via reflection...
		if (factoryName.equals(FactoryFinder.APPLICATION_FACTORY)) {
			setApplicationFactory(className);
		}
		if(factoryName.equals(FactoryFinder.FACES_CONTEXT_FACTORY)) {
			setFacesContextFactory(className);
		}
		if(factoryName.equals(FactoryFinder.LIFECYCLE_FACTORY)) {
			setLifecycleFactory(className);
		}
		if(factoryName.equals(FactoryFinder.RENDER_KIT_FACTORY)) {
			setRenderKitFactory(className);
		}
	}
    
    public void setApplicationFactory(String className)
    {
        if (className == null) return;
        _applicationFactory = className.intern();
    }

    public void setFacesContextFactory(String className)
    {
        if (className == null) return;
        _facesContextFactory = className.intern();
    }

    public void setLifecycleFactory(String className)
    {
        if (className == null) return;
        _lifecycleFactory = className.intern();
    }

    public void setRenderKitFactory(String className)
    {
        if (className == null) return;
        _renderKitFactory = className.intern();
    }

    public String getApplicationFactory()
    {
        return _applicationFactory;
    }

    public String getFacesContextFactory()
    {
        return _facesContextFactory;
    }

    public String getLifecycleFactory()
    {
        return _lifecycleFactory;
    }

    public String getRenderKitFactory()
    {
        return _renderKitFactory;
    }

    public void update(FactoryConfig config)
    {
        setApplicationFactory(config.getApplicationFactory());
        setFacesContextFactory(config.getFacesContextFactory());
        setLifecycleFactory(config.getLifecycleFactory());
        setRenderKitFactory(config.getRenderKitFactory());
    }
}

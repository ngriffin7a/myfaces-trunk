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

import javax.faces.FactoryFinder;

/**
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.9  2004/07/01 22:05:08  mwessendorf
 * ASF switch
 *
 * Revision 1.8  2004/04/13 08:26:49  manolito
 * Log
 *
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

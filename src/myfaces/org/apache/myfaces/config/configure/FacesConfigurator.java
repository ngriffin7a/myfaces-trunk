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
package net.sourceforge.myfaces.config.configure;

import net.sourceforge.myfaces.config.FacesConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.ExternalContext;
import javax.faces.render.RenderKitFactory;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesConfigurator
{
    private static final Log log = LogFactory.getLog(FacesConfigurator.class);

    private FacesConfig _facesConfig;
    private ExternalContext _externalContext;

    public FacesConfigurator(FacesConfig facesConfig, ExternalContext externalContext)
    {
        _facesConfig = facesConfig;
        _externalContext = externalContext;
    }

    public void configure()
    {
        if (log.isDebugEnabled()) log.debug("Configuring MyFaces - start");

        new FactoryFinderConfigurator(_facesConfig).configure();

        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = af.getApplication();
        new ApplicationConfigurator(_facesConfig).configure(application);

        RenderKitFactory rkf = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        new RenderKitFactoryConfigurator(_facesConfig).configure(rkf, _externalContext);

        if (log.isDebugEnabled()) log.debug("Configuring MyFaces - finished");
    }

}

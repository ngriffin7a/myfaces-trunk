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

import net.sourceforge.myfaces.config.RenderKitConfig;
import net.sourceforge.myfaces.config.RendererConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RenderKitConfigurator
{
    private static final Log log = LogFactory.getLog(RenderKitConfigurator.class);

    private RenderKitConfig _renderKitConfig;

    public RenderKitConfigurator(RenderKitConfig renderKitConfig)
    {
        _renderKitConfig = renderKitConfig;
    }

    public void configure(ExternalContext externalContext, RenderKit renderKit, String renderKitId)
    {
        if (log.isDebugEnabled()) log.debug("Completing RenderKit " + renderKitId);

        for (Iterator it = _renderKitConfig.getRendererConfigs(); it.hasNext();)
        {
            RendererConfig rendererConfig = (RendererConfig)it.next();
            Renderer       renderer       = rendererConfig.newRenderer(externalContext);
            renderKit.addRenderer(rendererConfig.getComponentFamily(),
                                  rendererConfig.getRendererType(),
                                  renderer);
        }
    }

}

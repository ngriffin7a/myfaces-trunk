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

import net.sourceforge.myfaces.config.RenderKitConfig;
import net.sourceforge.myfaces.config.RendererConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    public void configure(RenderKit renderKit, String renderKitId)
    {
        if (log.isDebugEnabled()) log.debug("Completing RenderKit " + renderKitId);

        for (Iterator it = _renderKitConfig.getRendererTypes(); it.hasNext();)
        {
            String         rendererType   = (String) it.next();
            RendererConfig rendererConfig = _renderKitConfig.getRendererConfig(rendererType);
            Renderer       renderer       = rendererConfig.newRenderer();
            renderKit.addRenderer(rendererType, renderer);
        }
    }

}

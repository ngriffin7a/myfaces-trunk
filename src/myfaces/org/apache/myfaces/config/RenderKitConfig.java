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

import net.sourceforge.myfaces.renderkit.html.RenderKitImpl;

import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RenderKitConfig
    implements Config
{
    private static final String DEFAULT_RENDER_KIT_CLASS = RenderKitImpl.class.getName();

    private String _renderKitId = RenderKitFactory.DEFAULT_RENDER_KIT;
    private String _renderKitClass = null;
    private Map _rendererConfigMap = null;

    public String getRenderKitId()
    {
        return _renderKitId;
    }

    public void setRenderKitId(String renderKitId)
    {
        _renderKitId = renderKitId;
    }

    public String getRenderKitClass()
    {
        return _renderKitClass;
    }

    public void setRenderKitClass(String renderKitClass)
    {
        _renderKitClass = renderKitClass;
    }


    public void addRendererConfig(RendererConfig rendererConfig)
    {
        getRendererConfigMap().put(rendererConfig.getRendererType(),
                                   rendererConfig);
    }

    public RendererConfig getRendererConfig(String rendererType)
    {
        return (RendererConfig)getRendererConfigMap().get(rendererType);
    }

    public Iterator getRendererTypes()
    {
        return getRendererConfigMap().keySet().iterator();
    }

    public Iterator getRendererConfigs()
    {
        return getRendererConfigMap().values().iterator();
    }

    private Map getRendererConfigMap()
    {
        if (_rendererConfigMap == null)
        {
            _rendererConfigMap = new HashMap();
        }
        return _rendererConfigMap;
    }

    public RenderKit newRenderKit()
    {
        String clazz = getRenderKitClass();
        if (clazz == null)
        {
            clazz = DEFAULT_RENDER_KIT_CLASS;
        }
        return (RenderKit)ConfigUtil.newInstance(clazz);
    }

    public void configureRenderers(RenderKit renderKit)
    {
        for (Iterator it = getRendererTypes(); it.hasNext(); )
        {
            String rendererType = (String)it.next();
            RendererConfig rendererConfig = getRendererConfig(rendererType);
            Renderer renderer = rendererConfig.newRenderer();
            renderKit.addRenderer(rendererType, renderer);
        }
    }
}

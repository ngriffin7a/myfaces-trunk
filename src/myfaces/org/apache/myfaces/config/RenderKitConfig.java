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

import net.sourceforge.myfaces.renderkit.html.HTMLRenderKitImpl;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class RenderKitConfig implements Config
{
    //~ Static fields/initializers -----------------------------------------------------------------

    private static final Class DEFAULT_RENDER_KIT_CLASS = HTMLRenderKitImpl.class;

    //~ Instance fields ----------------------------------------------------------------------------

// ignore        
//    private String     _description;
//    private String     _displayName;
//    private IconConfig _iconConfig;
    private Class  _renderKitClass;
    private Map    _rendererConfigMap;
    private String _renderKitId = RenderKitFactory.DEFAULT_RENDER_KIT;

    //~ Methods ------------------------------------------------------------------------------------

    public void setDescription(String description)
    {
// ignore        
//        _description = description;
    }

    public void setDisplayName(String displayName)
    {
// ignore        
//        _displayName = displayName;
    }

    public void setIconConfig(IconConfig iconConfig)
    {
// ignore        
//        _iconConfig = iconConfig;
    }

    public void setRenderKitClass(Class renderKitClass)
    {
        _renderKitClass = renderKitClass;
    }

    public void setRenderKitClass(String renderKitClass)
    {
        _renderKitClass = ConfigUtil.classForName(renderKitClass);
    }

    public Class getRenderKitClass()
    {
        return _renderKitClass;
    }

    public void setRenderKitId(String renderKitId)
    {
        _renderKitId = renderKitId.intern();
    }

    public String getRenderKitId()
    {
        return _renderKitId;
    }

    public RendererConfig getRendererConfig(String rendererType)
    {
        return (RendererConfig) getRendererConfigMap().get(rendererType);
    }

    public Iterator getRendererConfigs()
    {
        return getRendererConfigMap().values().iterator();
    }

    public Iterator getRendererTypes()
    {
        return getRendererConfigMap().keySet().iterator();
    }

    public void addRendererConfig(RendererConfig rendererConfig)
    {
        getRendererConfigMap().put(rendererConfig.getRendererType(), rendererConfig);
    }

    public void configureRenderers(RenderKit renderKit)
    {
        for (Iterator it = getRendererTypes(); it.hasNext();)
        {
            String         rendererType   = (String) it.next();
            RendererConfig rendererConfig = getRendererConfig(rendererType);
            Renderer       renderer       = rendererConfig.newRenderer();
            renderKit.addRenderer(rendererType, renderer);
        }
    }

    public RenderKit newRenderKit()
    {
        Class clazz = (_renderKitClass != null) ? _renderKitClass : DEFAULT_RENDER_KIT_CLASS;

        try
        {
            return (RenderKit) _renderKitClass.newInstance();
        }
        catch (Exception e)
        {
            LogFactory.getLog(RenderKitConfig.class).error(
                "Error in faces-config.xml - Class not found: " + clazz);
            throw new FacesException(e);
        }
    }

    private Map getRendererConfigMap()
    {
        if (_rendererConfigMap == null)
        {
            _rendererConfigMap = new HashMap();
        }
        return _rendererConfigMap;
    }
}

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

import net.sourceforge.myfaces.renderkit.html.HtmlRenderKitImpl;
import net.sourceforge.myfaces.util.ClassUtils;

import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
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

    private static final Class DEFAULT_RENDER_KIT_CLASS = HtmlRenderKitImpl.class;

    //~ Instance fields ----------------------------------------------------------------------------

// ignore        
//    private String     _description;
//    private String     _displayName;
//    private IconConfig _iconConfig;
    private Class  _renderKitClass;
    private Map    _rendererConfigMap;
    private String _renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT;

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
        _renderKitClass = ClassUtils.classForName(renderKitClass);
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

    public RendererConfig getRendererConfig(String componentFamily, String rendererType)
    {
        return (RendererConfig) getRendererConfigMap().get(componentFamily + "/" + rendererType);
    }

    public Iterator getRendererConfigs()
    {
        return getRendererConfigMap().values().iterator();
    }

    /*
    public Iterator getRendererTypes()
    {
        return getRendererConfigMap().keySet().iterator();
    }
    */

    public void addRendererConfig(RendererConfig rendererConfig)
    {
        getRendererConfigMap().put(rendererConfig.getComponentFamily() + "/" + rendererConfig.getRendererType(),
                                   rendererConfig);
    }

    public RenderKit newRenderKit()
    {
        Class clazz = (_renderKitClass != null) ? _renderKitClass : DEFAULT_RENDER_KIT_CLASS;
        return (RenderKit)ClassUtils.newInstance(clazz);
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

/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.renderkit.callback;

import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.AttributeDescriptor;
import java.io.IOException;
import java.util.Map;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class RendererWrapper
    extends Renderer
{
    private String _rendererType;

    public RendererWrapper(String rendererType)
    {
        _rendererType = rendererType;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        //TODO: uiComponent may be null!
        Renderer renderer = CallbackSupport.getOriginalRenderKit(facesContext)
                                                .getRenderer(_rendererType);

        Map map = CallbackSupport.getCallbackRendererInfoMap(facesContext);
        for (Iterator it = map.values().iterator(); it.hasNext(); )
        {
            CallbackRendererInfo callbackInfo = (CallbackRendererInfo)it.next();
            if (!callbackInfo._onlyChildren ||
                uiComponent == null ||
                uiComponent.getParent() == callbackInfo._component)
            {
                callbackInfo._callbackRenderer
                                .beforeEncodeBegin(facesContext,
                                                   renderer,
                                                   uiComponent);
            }
        }

        renderer.encodeBegin(facesContext, uiComponent);
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        Renderer renderer = CallbackSupport.getOriginalRenderKit(facesContext)
                                                .getRenderer(_rendererType);
        renderer.encodeChildren(facesContext, uiComponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        Renderer renderer = CallbackSupport.getOriginalRenderKit(facesContext)
                                                .getRenderer(_rendererType);
        renderer.encodeEnd(facesContext, uiComponent);

        Map map = CallbackSupport.getCallbackRendererInfoMap(facesContext);
        for (Iterator it = map.values().iterator(); it.hasNext();)
        {
            CallbackRendererInfo callbackInfo = (CallbackRendererInfo)it.next();
            if (!callbackInfo._onlyChildren ||
                uiComponent == null ||
                uiComponent.getParent() == callbackInfo._component)
            {
                callbackInfo._callbackRenderer
                    .afterEncodeEnd(facesContext,
                                    renderer,
                                    uiComponent);
            }
        }
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        Renderer renderer = CallbackSupport.getOriginalRenderKit(facesContext).getRenderer(_rendererType);
        renderer.decode(facesContext, uiComponent);
    }

    public AttributeDescriptor getAttributeDescriptor(UIComponent uiComponent, String name)
    {
        return getAttributeDescriptor(uiComponent.getComponentType(), name);
    }

    public AttributeDescriptor getAttributeDescriptor(String componentType, String name)
    {
        Renderer renderer = CallbackSupport.getOriginalRenderKit(FacesContext.getCurrentInstance()).getRenderer(_rendererType);
        return renderer.getAttributeDescriptor(componentType, name);
    }

    public Iterator getAttributeNames(UIComponent uiComponent)
    {
        return getAttributeNames(uiComponent.getComponentType());
    }

    public Iterator getAttributeNames(String componentType)
    {
        Renderer renderer = CallbackSupport.getOriginalRenderKit(FacesContext.getCurrentInstance()).getRenderer(_rendererType);
        return renderer.getAttributeNames(componentType);
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return supportsComponentType(uiComponent.getComponentType());
    }

    public boolean supportsComponentType(String componentType)
    {
        Renderer renderer = CallbackSupport.getOriginalRenderKit(FacesContext.getCurrentInstance()).getRenderer(_rendererType);
        return renderer.supportsComponentType(componentType);
    }

    public String getClientId(FacesContext facesContext, UIComponent uiComponent)
    {
        Renderer renderer = CallbackSupport.getOriginalRenderKit(facesContext).getRenderer(_rendererType);
        return renderer.getClientId(facesContext, uiComponent);
    }
}

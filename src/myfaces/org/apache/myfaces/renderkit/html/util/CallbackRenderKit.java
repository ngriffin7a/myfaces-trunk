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
package net.sourceforge.myfaces.renderkit.html.util;

import javax.faces.FactoryFinder;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.tree.Tree;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class CallbackRenderKit
    extends RenderKit
{
    protected static final String ID = CallbackRenderKit.class.getName() + ".ID";

    private Map _rendererWrappers = new HashMap();

    public CallbackRenderKit()
    {
    }

    public Renderer getRenderer(String rendererType)
    {
        Renderer rendererWrapper = (Renderer)_rendererWrappers.get(rendererType);
        if (rendererWrapper == null)
        {
            rendererWrapper = new RendererWrapper(rendererType);
            _rendererWrappers.put(rendererType, rendererWrapper);
        }
        return rendererWrapper;
    }

    public void addRenderer(String s, Renderer renderer)
    {
        throw new UnsupportedOperationException();
    }

    public void addComponentClass(Class aClass)
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getComponentClasses()
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getRendererTypes()
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getRendererTypes(String s)
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getRendererTypes(UIComponent uiComponent)
    {
        throw new UnsupportedOperationException();
    }


    private static final String ORIGINAL_RENDER_KIT_ID_ATTR
        = CallbackRenderKit.class.getName() + ".ORIGINAL_RENDER_ID_KIT";
    private static final String ORIGINAL_RENDER_KIT_ATTR
        = CallbackRenderKit.class.getName() + ".ORIGINAL_RENDER_KIT";

    protected static RenderKit getOriginalRenderKit(FacesContext facesContext)
    {
        return (RenderKit)facesContext.getServletRequest().getAttribute(ORIGINAL_RENDER_KIT_ATTR);
    }

    protected static void wrapRenderKit(FacesContext facesContext)
    {
        Tree tree = facesContext.getTree();
        String originalRenderKitId = tree.getRenderKitId();

        if (originalRenderKitId.equals(ID))
        {
            //already wrapped
            return;
        }

        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit originalRenderKit = renderkitFactory.getRenderKit(originalRenderKitId,
                                                                    facesContext);
        //TODO: original renderKit should also be on a stack, in the meantime we support no nested RenderKits
        if (getOriginalRenderKit(facesContext) != null)
        {
            throw new IllegalStateException();
        }
        facesContext.getServletRequest().setAttribute(ORIGINAL_RENDER_KIT_ID_ATTR,
                                                      originalRenderKitId);
        facesContext.getServletRequest().setAttribute(ORIGINAL_RENDER_KIT_ATTR,
                                                      originalRenderKit);

        // lookup CallbackRenderKit in RenderKitFactory...
        RenderKit callbackRenderKit = null;
        try
        {
            callbackRenderKit = renderkitFactory.getRenderKit(ID, facesContext);
        }
        catch (Exception e) {}
        // ...and add to RenderKitFactory if not yet registered
        if (callbackRenderKit == null)
        {
            callbackRenderKit = new CallbackRenderKit();
            renderkitFactory.addRenderKit(ID, callbackRenderKit);
        }

        // set tree to wrapper
        tree.setRenderKitId(ID);
    }

    protected static void unwrapRenderKit(FacesContext facesContext)
    {
        String originalRenderKitId = (String)facesContext.getServletRequest().getAttribute(ORIGINAL_RENDER_KIT_ID_ATTR);
        facesContext.getTree().setRenderKitId(originalRenderKitId);
    }




    private static final String CALLBACK_MAP_ATTR
        = CallbackRenderKit.class.getName() + ".CALLBACK_MAP";

    protected static Map getCallbackMap(FacesContext facesContext)
    {
        Map map = (Map)facesContext.getServletRequest().getAttribute(CALLBACK_MAP_ATTR);
        if (map == null)
        {
            map = new HashMap();
            facesContext.getServletRequest().setAttribute(CALLBACK_MAP_ATTR, map);
        }
        return map;
    }


    /**
     * Register a new CallbackRenderer, whose callback-functions are called when rendering each
     * component in the subtree of the given component.
     * @param facesContext
     * @param component
     * @param callbackRenderer
     */
    public static void addCallbackRenderer(FacesContext facesContext,
                                           UIComponent component,
                                           CallbackRenderer callbackRenderer)
    {
        //Make sure, that original RenderKit is wrapped
        wrapRenderKit(facesContext);

        Map map = getCallbackMap(facesContext);
        map.put(component.getClientId(facesContext),
                new CallbackInfo(component,
                                 callbackRenderer,
                                 false));
    }

    public static void removeCallbackRenderer(FacesContext facesContext,
                                              UIComponent component,
                                              CallbackRenderer callbackRenderer)
    {
        Map map = getCallbackMap(facesContext);
        map.remove(component.getClientId(facesContext));
        if (map.isEmpty())
        {
            unwrapRenderKit(facesContext);
        }
    }

    /**
     * Register a new CallbackRenderer, whose callback-functions are called when rendering each
     * direct child component of the given component.
     * @param facesContext
     * @param component
     * @param callbackRenderer
     */
    public static void addChildrenCallbackRenderer(FacesContext facesContext,
                                                   UIComponent component,
                                                   CallbackRenderer callbackRenderer)
    {
        //Make sure, that original RenderKit is wrapped
        wrapRenderKit(facesContext);

        Map map = getCallbackMap(facesContext);
        map.put(component.getClientId(facesContext),
                new CallbackInfo(component,
                                 callbackRenderer,
                                 true));
    }

    public static void removeChildrenCallbackRenderer(FacesContext facesContext,
                                                      UIComponent component,
                                                      CallbackRenderer callbackRenderer)
    {
        removeCallbackRenderer(facesContext, component, callbackRenderer);
    }


    private static class CallbackInfo
    {
        private UIComponent _component;
        private CallbackRenderer _callbackRenderer;
        private boolean _onlyChildren;

        public CallbackInfo(UIComponent component,
                            CallbackRenderer callbackRenderer,
                            boolean onlyChildren)
        {
            _component = component;
            _callbackRenderer = callbackRenderer;
            _onlyChildren = onlyChildren;
        }
    }



    private class RendererWrapper
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
            Renderer renderer = getOriginalRenderKit(facesContext).getRenderer(_rendererType);

            Map map = getCallbackMap(facesContext);
            for (Iterator it = map.values().iterator(); it.hasNext(); )
            {
                CallbackInfo callbackInfo = (CallbackInfo)it.next();
                if (!callbackInfo._onlyChildren ||
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
            Renderer renderer = getOriginalRenderKit(facesContext).getRenderer(_rendererType);
            renderer.encodeChildren(facesContext, uiComponent);
        }

        public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
        {
            Renderer renderer = getOriginalRenderKit(facesContext).getRenderer(_rendererType);
            renderer.encodeEnd(facesContext, uiComponent);

            Map map = getCallbackMap(facesContext);
            for (Iterator it = map.values().iterator(); it.hasNext();)
            {
                CallbackInfo callbackInfo = (CallbackInfo)it.next();
                if (!callbackInfo._onlyChildren ||
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
            Renderer renderer = getOriginalRenderKit(facesContext).getRenderer(_rendererType);
            renderer.decode(facesContext, uiComponent);
        }

        public AttributeDescriptor getAttributeDescriptor(UIComponent uiComponent, String name)
        {
            return getAttributeDescriptor(uiComponent.getComponentType(), name);
        }

        public AttributeDescriptor getAttributeDescriptor(String componentType, String name)
        {
            Renderer renderer = getOriginalRenderKit(FacesContext.getCurrentInstance()).getRenderer(_rendererType);
            return renderer.getAttributeDescriptor(componentType, name);
        }

        public Iterator getAttributeNames(UIComponent uiComponent)
        {
            return getAttributeNames(uiComponent.getComponentType());
        }

        public Iterator getAttributeNames(String componentType)
        {
            Renderer renderer = getOriginalRenderKit(FacesContext.getCurrentInstance()).getRenderer(_rendererType);
            return renderer.getAttributeNames(componentType);
        }

        public boolean supportsComponentType(UIComponent uiComponent)
        {
            return supportsComponentType(uiComponent.getComponentType());
        }

        public boolean supportsComponentType(String componentType)
        {
            Renderer renderer = getOriginalRenderKit(FacesContext.getCurrentInstance()).getRenderer(_rendererType);
            return renderer.supportsComponentType(componentType);
        }

        public String getClientId(FacesContext facesContext, UIComponent uiComponent)
        {
            Renderer renderer = getOriginalRenderKit(facesContext).getRenderer(_rendererType);
            return renderer.getClientId(facesContext, uiComponent);
        }
    };

}

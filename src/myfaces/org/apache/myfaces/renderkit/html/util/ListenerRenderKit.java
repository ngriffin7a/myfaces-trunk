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

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.tree.Tree;
import java.io.IOException;
import java.util.*;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class ListenerRenderKit
    extends RenderKit
{
    protected static final String ID = ListenerRenderKit.class.getName() + ".ID";

    private Map _rendererWrappers = new HashMap();

    public ListenerRenderKit()
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


    private static final String ORIGINAL_RENDER_KIT_ATTR
        = ListenerRenderKit.class.getName() + ".ORIGINAL_RENDER_KIT";

    protected static RenderKit getOriginalRenderKit(FacesContext facesContext)
    {
        return (RenderKit)facesContext.getServletRequest().getAttribute(ORIGINAL_RENDER_KIT_ATTR);
    }

    protected static void wrapRenderKit(FacesContext facesContext)
    {
        Tree tree = facesContext.getResponseTree();
        String originalRenderKitId = tree.getRenderKitId();

        if (originalRenderKitId.equals(ID))
        {
            //already wrapped
            return;
        }

        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit originalRenderKit = renderkitFactory.getRenderKit(originalRenderKitId,
                                                                    facesContext);
        //TODO: original renderKit should also be on a stack, in the meantime
        //we support no nested RenderKits
        if (getOriginalRenderKit(facesContext) != null)
        {
            throw new IllegalStateException();
        }
        facesContext.getServletRequest().setAttribute(ORIGINAL_RENDER_KIT_ATTR,
                                                      originalRenderKit);

        // lookup ListenerRenderKit in RenderKitFactory...
        RenderKit listenerRenderKit = null;
        try
        {
            listenerRenderKit = renderkitFactory.getRenderKit(ID, facesContext);
        }
        catch (FacesException e)
        {
        }
        // ...and add to RenderKitFactory if not yet registered
        if (listenerRenderKit == null)
        {
            listenerRenderKit = new ListenerRenderKit();
            renderkitFactory.addRenderKit(ID, listenerRenderKit);
        }

        // set tree to wrapper
        tree.setRenderKitId(ID);
    }





    private static final String LISTENERS_ATTR
        = ListenerRenderKit.class.getName() + ".LISTENERS";

    protected static Map getListenerMap(FacesContext facesContext)
    {
        Map map = (Map)facesContext.getServletRequest().getAttribute(LISTENERS_ATTR);
        if (map == null)
        {
            map = new HashMap();
            facesContext.getServletRequest().setAttribute(LISTENERS_ATTR, map);
        }
        return map;
    }



    public static void addListener(FacesContext facesContext,
                                   UIComponent component,
                                   RendererListener listener)
    {
        //Make sure, that original RenderKit is wrapped
        wrapRenderKit(facesContext);

        Map map = getListenerMap(facesContext);
        map.put(component.getCompoundId(),
                new ListenerItem(component,
                                 listener,
                                 false));
    }

    public static void removeListener(FacesContext facesContext,
                                      UIComponent component,
                                      RendererListener listener)
    {
        Map map = getListenerMap(facesContext);
        map.remove(component.getCompoundId());
    }

    public static void addChildrenListener(FacesContext facesContext,
                                           UIComponent component,
                                           RendererListener listener)
    {
        //Make sure, that original RenderKit is wrapped
        wrapRenderKit(facesContext);

        Map map = getListenerMap(facesContext);
        map.put(component.getCompoundId(),
                new ListenerItem(component,
                                 listener,
                                 true));
    }

    public static void removeChildrenListener(FacesContext facesContext,
                                              UIComponent component,
                                              RendererListener listener)
    {
        removeListener(facesContext, component, listener);
    }


    private static class ListenerItem
    {
        private UIComponent _component;
        private RendererListener _rendererListener;
        private boolean _onlyChildren;

        public ListenerItem(UIComponent component,
                            RendererListener rendererListener,
                            boolean onlyChildren)
        {
            _component = component;
            _rendererListener = rendererListener;
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
            Renderer renderer = getOriginalRenderKit(facesContext).getRenderer(_rendererType);

            Map map = getListenerMap(facesContext);
            for (Iterator it = map.values().iterator(); it.hasNext(); )
            {
                ListenerItem listenerItem = (ListenerItem)it.next();
                if (!listenerItem._onlyChildren ||
                    uiComponent.getParent() == listenerItem._component)
                {
                    listenerItem._rendererListener
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

            Map map = getListenerMap(facesContext);
            for (Iterator it = map.values().iterator(); it.hasNext();)
            {
                ListenerItem listenerItem = (ListenerItem)it.next();
                if (!listenerItem._onlyChildren ||
                    uiComponent.getParent() == listenerItem._component)
                {
                    listenerItem._rendererListener
                        .afterEncodeEnd(facesContext,
                                        renderer,
                                        uiComponent);
                }
            }
        }

        public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
        {
            throw new UnsupportedOperationException();
        }

        public AttributeDescriptor getAttributeDescriptor(UIComponent uiComponent, String s)
        {
            throw new UnsupportedOperationException();
        }

        public AttributeDescriptor getAttributeDescriptor(String s, String s1)
        {
            throw new UnsupportedOperationException();
        }

        public Iterator getAttributeNames(UIComponent uiComponent)
        {
            throw new UnsupportedOperationException();
        }

        public Iterator getAttributeNames(String s)
        {
            throw new UnsupportedOperationException();
        }

        public boolean supportsComponentType(UIComponent uiComponent)
        {
            throw new UnsupportedOperationException();
        }

        public boolean supportsComponentType(String s)
        {
            throw new UnsupportedOperationException();
        }
    };

}

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
import java.util.Iterator;
import java.util.Stack;

/**
 * This RenderKit can be used to wrap an existing RenderKit, so that
 * all getRenderer calls can be redirected to a special "masterRenderer"
 * or a number of special renderers, that want to handle all rendering
 * until the original RenderKit is restored.
 * This special RenderKit is useful for complex layout renderers, that must
 * have full control over the rendering of their children.
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class RenderKitWrapper
    extends RenderKit
{
    //TODO: Save as Request-attribute
    private RenderKit _originalRenderKit;
    //TODO: Save as Request-attribute
    private Stack _redirectionStack = new Stack();

    public RenderKitWrapper(RenderKit originalRenderKit)
    {
        _originalRenderKit = originalRenderKit;
    }

    public Renderer getRenderer(String rendererType)
    {
        return new RendererWrapper(rendererType);
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
        return _originalRenderKit.getComponentClasses();
    }

    public Iterator getRendererTypes()
    {
        return _originalRenderKit.getRendererTypes();
    }

    public Iterator getRendererTypes(String s)
    {
        return _originalRenderKit.getRendererTypes(s);
    }

    public Iterator getRendererTypes(UIComponent uiComponent)
    {
        return _originalRenderKit.getRendererTypes(uiComponent);
    }



    public void startChildrenRedirection(UIComponent currentComponent,
                                         Renderer currentRenderer,
                                         Renderer redirectionRenderer)
    {
        _redirectionStack.push(new Redirection(Redirection.START,
                                               currentComponent,
                                               currentRenderer,
                                               redirectionRenderer));
    }


    public void suspendChildrenRedirection(UIComponent currentComponent,
                                           Renderer currentRenderer)
    {
        _redirectionStack.push(new Redirection(Redirection.SUSPEND,
                                               currentComponent,
                                               currentRenderer,
                                               null));
    }

    public RenderKit getOriginalRenderKit()
    {
        return _originalRenderKit;
    }




    private static class Redirection
    {
        public static final int START = 1;
        public static final int SUSPEND = 2;
        public int redirectionType;
        public UIComponent redirectingComponent;
        public Renderer redirectingRenderer;
        public Renderer redirectionRenderer;

        public Redirection(int redirectionType,
                           UIComponent redirectingComponent,
                           Renderer redirectingRenderer,
                           Renderer redirectionRenderer)
        {
            this.redirectionType = redirectionType;
            this.redirectingComponent = redirectingComponent;
            this.redirectingRenderer = redirectingRenderer;
            this.redirectionRenderer = redirectionRenderer;
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
            if (!_redirectionStack.isEmpty())
            {
                Redirection redirection = (Redirection)_redirectionStack.peek();
                if (redirection.redirectingComponent == uiComponent)
                {
                    if (redirection.redirectingRenderer != null)
                    {
                        redirection.redirectingRenderer.encodeBegin(facesContext,
                                                                    uiComponent);
                    }
                    else
                    {
                        redirection.redirectingComponent.encodeBegin(facesContext);
                    }
                }
                else if (redirection.redirectionType == Redirection.START)
                {
                    redirection.redirectionRenderer.encodeBegin(facesContext,
                                                                uiComponent);
                }
            }
            else
            {
                _originalRenderKit.getRenderer(_rendererType).encodeBegin(facesContext,
                                                                          uiComponent);
            }
        }

        public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException
        {
            if (!_redirectionStack.isEmpty())
            {
                Redirection redirection = (Redirection)_redirectionStack.peek();
                if (redirection.redirectingComponent == uiComponent)
                {
                    if (redirection.redirectingRenderer != null)
                    {
                        redirection.redirectingRenderer.encodeChildren(facesContext,
                                                                    uiComponent);
                    }
                    else
                    {
                        redirection.redirectingComponent.encodeChildren(facesContext);
                    }
                }
                else if (redirection.redirectionType == Redirection.START)
                {
                    redirection.redirectionRenderer.encodeChildren(facesContext,
                                                                uiComponent);
                }
            }
            else
            {
                _originalRenderKit.getRenderer(_rendererType).encodeChildren(facesContext,
                                                                          uiComponent);
            }
        }

        public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
        {
            if (!_redirectionStack.isEmpty())
            {
                Redirection redirection = (Redirection)_redirectionStack.peek();
                if (redirection.redirectingComponent == uiComponent)
                {
                    if (redirection.redirectingRenderer != null)
                    {
                        redirection.redirectingRenderer.encodeEnd(facesContext,
                                                                       uiComponent);
                    }
                    else
                    {
                        redirection.redirectingComponent.encodeEnd(facesContext);
                    }
                    _redirectionStack.pop();
                }
                else if (redirection.redirectionType == Redirection.START)
                {
                    redirection.redirectionRenderer.encodeEnd(facesContext,
                                                                uiComponent);
                }
            }
            else
            {
                _originalRenderKit.getRenderer(_rendererType).encodeEnd(facesContext,
                                                                          uiComponent);
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



    private static final String ORIGINAL_RENDER_KIT_ID_ATTR
        = RenderKitWrapper.class.getName() + ".ORIGINAL_RENDER_KIT_ID";

    public static void wrapRenderKit(FacesContext facesContext,
                                     UIComponent currentComponent,
                                     String wrapRenderKitId)
    {
        Tree tree = facesContext.getResponseTree();
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        String originalRenderKitId = tree.getRenderKitId();
        RenderKit originalRenderKit = renderkitFactory.getRenderKit(originalRenderKitId,
                                                                    facesContext);

        String newRenderKitId = originalRenderKitId + "_" + wrapRenderKitId;
        // lookup new RenderKit in RenderKitFactory...
        RenderKit wrapRenderKit = null;
        try
        {
            wrapRenderKit = renderkitFactory.getRenderKit(newRenderKitId,
                                                          facesContext);
        }
        catch (FacesException e)
        {
        }
        // ...and add to RenderKitFactory if not yet registered
        if (wrapRenderKit == null)
        {
            wrapRenderKit = new RenderKitWrapper(originalRenderKit);
            renderkitFactory.addRenderKit(wrapRenderKitId, wrapRenderKit);
        }

        // remember original renderKitId in current component
        currentComponent.setAttribute(ORIGINAL_RENDER_KIT_ID_ATTR,
                                      originalRenderKitId);

        // set tree to new renderKitId
        tree.setRenderKitId(newRenderKitId);
    }


    public static void unwrapRenderKit(FacesContext facesContext,
                                       UIComponent currentComponent)
    {
        // get original renderKitId via component attribute
        String originalRenderKitId
            = (String)currentComponent.getAttribute(ORIGINAL_RENDER_KIT_ID_ATTR);

        // remove attribute
        currentComponent.setAttribute(ORIGINAL_RENDER_KIT_ID_ATTR, null);

        // set tree to original renderKitId
        Tree tree = facesContext.getResponseTree();
        tree.setRenderKitId(originalRenderKitId);
    }


    public static void startChildrenRedirection(FacesContext facesContext,
                                                UIComponent currentComponent,
                                                Renderer currentRenderer,
                                                Renderer redirectionRenderer)
    {
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId(),
                                                            facesContext);
        if (!(renderKit instanceof RenderKitWrapper))
        {
            throw new IllegalStateException();
        }

        ((RenderKitWrapper)renderKit).startChildrenRedirection(currentComponent,
                                                               currentRenderer,
                                                               redirectionRenderer);
    }


    public static void suspendChildrenRedirection(FacesContext facesContext,
                                                  UIComponent currentComponent,
                                                  Renderer currentRenderer)
    {
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId(),
                                                            facesContext);
        if (!(renderKit instanceof RenderKitWrapper))
        {
            throw new IllegalStateException();
        }

        ((RenderKitWrapper)renderKit).suspendChildrenRedirection(currentComponent,
                                                                 currentRenderer);
    }


    public static void originalEncodeBegin(FacesContext facesContext,
                                           UIComponent uiComponent)
        throws IOException
    {
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId(),
                                                            facesContext);
        if (!(renderKit instanceof RenderKitWrapper))
        {
            throw new IllegalStateException();
        }

        Renderer originalRenderer = ((RenderKitWrapper)renderKit).getOriginalRenderKit().getRenderer(uiComponent.getRendererType());
        originalRenderer.encodeBegin(facesContext, uiComponent);
    }

    public static void originalEncodeChildren(FacesContext facesContext,
                                           UIComponent uiComponent)
        throws IOException
    {
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId(),
                                                            facesContext);
        if (!(renderKit instanceof RenderKitWrapper))
        {
            throw new IllegalStateException();
        }

        Renderer originalRenderer = ((RenderKitWrapper)renderKit).getOriginalRenderKit().getRenderer(uiComponent.getRendererType());
        originalRenderer.encodeChildren(facesContext, uiComponent);
    }

    public static void originalEncodeEnd(FacesContext facesContext,
                                           UIComponent uiComponent)
        throws IOException
    {
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderkitFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId(),
                                                            facesContext);
        if (!(renderKit instanceof RenderKitWrapper))
        {
            throw new IllegalStateException();
        }

        Renderer originalRenderer = ((RenderKitWrapper)renderKit).getOriginalRenderKit().getRenderer(uiComponent.getRendererType());
        originalRenderer.encodeEnd(facesContext, uiComponent);
    }

}

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
import java.util.Map;
import java.util.HashMap;

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
    protected static final String ID = RenderKitWrapper.class.getName() + ".ID";

    private Map _rendererWrappers = new HashMap();

    public RenderKitWrapper()
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



    private static final String REDIRECTION_STACK_ATTR
        = RenderKitWrapper.class.getName() + ".REDIRECTION_STACK";
    protected static Stack getRedirectionStack(FacesContext facesContext)
    {
        Stack stack = (Stack)facesContext.getServletRequest().getAttribute(REDIRECTION_STACK_ATTR);
        if (stack == null)
        {
            stack = new Stack();
            facesContext.getServletRequest().setAttribute(REDIRECTION_STACK_ATTR,
                                                          stack);
        }
        return stack;
    }


    private static final String ORIGINAL_RENDER_KIT_ATTR
        = RenderKitWrapper.class.getName() + ".ORIGINAL_RENDER_KIT";
    protected static RenderKit getOriginalRenderKit(FacesContext facesContext)
    {
        return (RenderKit)facesContext.getServletRequest().getAttribute(ORIGINAL_RENDER_KIT_ATTR);
    }



    public static void startChildrenRedirection(FacesContext facesContext,
                                                UIComponent currentComponent,
                                                Renderer currentRenderer,
                                                Renderer redirectionRenderer)
    {
        Stack redirStack = getRedirectionStack(facesContext);
        redirStack.push(new Redirection(Redirection.START,
                                        currentComponent,
                                        currentRenderer,
                                        redirectionRenderer));
    }


    public static void suspendChildrenRedirection(FacesContext facesContext,
                                                  UIComponent currentComponent,
                                                  Renderer currentRenderer)
    {
        Stack redirStack = getRedirectionStack(facesContext);
        if (redirStack.isEmpty())
        {
            throw new IllegalStateException();
        }
        Redirection currRedir = (Redirection)redirStack.peek();
        if (currRedir.redirectionType == Redirection.SUSPEND)
        {
            //already suspended
            return;
        }
        redirStack.push(new Redirection(Redirection.SUSPEND,
                                        currentComponent,
                                        currentRenderer,
                                        null));
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
            Stack redirectionStack = getRedirectionStack(facesContext);
            if (!redirectionStack.isEmpty())
            {
                Redirection redirection = (Redirection)redirectionStack.peek();
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
                getOriginalRenderKit(facesContext)
                    .getRenderer(_rendererType).encodeBegin(facesContext,
                                                            uiComponent);
            }
        }

        public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException
        {
            Stack redirectionStack = getRedirectionStack(facesContext);
            if (!redirectionStack.isEmpty())
            {
                Redirection redirection = (Redirection)redirectionStack.peek();
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
                getOriginalRenderKit(facesContext)
                    .getRenderer(_rendererType).encodeChildren(facesContext,
                                                               uiComponent);
            }
        }

        public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
        {
            Stack redirectionStack = getRedirectionStack(facesContext);
            if (!redirectionStack.isEmpty())
            {
                Redirection redirection = (Redirection)redirectionStack.peek();
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
                    //cancel redirection:
                    redirectionStack.pop();
                }
                else if (redirection.redirectionType == Redirection.START)
                {
                    redirection.redirectionRenderer.encodeEnd(facesContext,
                                                                uiComponent);
                }
            }
            else
            {
                getOriginalRenderKit(facesContext)
                    .getRenderer(_rendererType).encodeEnd(facesContext,
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
        //TODO: original renderKit should also be on a stack
        facesContext.getServletRequest().setAttribute(ORIGINAL_RENDER_KIT_ATTR,
                                                      originalRenderKit);

        // lookup RenderKitWrapper in RenderKitFactory...
        RenderKit renderKitWrapper = null;
        try
        {
            renderKitWrapper = renderkitFactory.getRenderKit(ID, facesContext);
        }
        catch (FacesException e) {}
        // ...and add to RenderKitFactory if not yet registered
        if (renderKitWrapper == null)
        {
            renderKitWrapper = new RenderKitWrapper();
            renderkitFactory.addRenderKit(ID, renderKitWrapper);
        }

        // set tree to wrapper
        tree.setRenderKitId(ID);
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

        Renderer originalRenderer = getOriginalRenderKit(facesContext).getRenderer(uiComponent.getRendererType());
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

        Renderer originalRenderer = getOriginalRenderKit(facesContext).getRenderer(uiComponent.getRendererType());
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

        Renderer originalRenderer = getOriginalRenderKit(facesContext).getRenderer(uiComponent.getRendererType());
        originalRenderer.encodeEnd(facesContext, uiComponent);
    }

}

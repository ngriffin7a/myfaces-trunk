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

import javax.faces.component.UIComponent;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKitFactory;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.faces.FactoryFinder;
import javax.faces.FacesException;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;

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
    private String _originalRenderKitId;
    private Map _masterRenderers;
    private Renderer _defaultMasterRenderer;

    /**
     * @param originalRenderKitId id of wrapped RenderKit
     * @param masterRenderer      renderer, that should be returned by getRenderer
     *                            instead of any of the wrapped renderers
     */
    public RenderKitWrapper(String originalRenderKitId,
                            Renderer masterRenderer)
    {
        this(originalRenderKitId, null, masterRenderer);
    }

    /**
     * @param originalRenderKitId   id of wrapped RenderKit
     * @param masterRenderers       renderers, that should be returned by getRenderer
     *                              instead of any of the wrapped renderers
     * @param defaulMasterRenderer  renderer, that should be returned for all
     *                              types, not mapped by masterRenderers
     */
    public RenderKitWrapper(String originalRenderKitId,
                            Map masterRenderers,
                            Renderer defaulMasterRenderer)
    {
        _originalRenderKitId = originalRenderKitId;
        _masterRenderers = masterRenderers;
        _defaultMasterRenderer = defaulMasterRenderer;
    }

    public Renderer getRenderer(String rendererType)
    {
        Renderer renderer = null;
        if (_masterRenderers != null)
        {
            renderer = (Renderer)_masterRenderers.get(rendererType);
        }
        return renderer != null ? renderer : _defaultMasterRenderer;
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
        return getOriginalRenderKit().getComponentClasses();
    }

    public Iterator getRendererTypes()
    {
        return getOriginalRenderKit().getRendererTypes();
    }

    public Iterator getRendererTypes(String s)
    {
        return getOriginalRenderKit().getRendererTypes(s);
    }

    public Iterator getRendererTypes(UIComponent uiComponent)
    {
        return getOriginalRenderKit().getRendererTypes(uiComponent);
    }

    public String getOriginalRenderKitId()
    {
        return _originalRenderKitId;
    }

    public RenderKit getOriginalRenderKit()
    {
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        return renderkitFactory.getRenderKit(_originalRenderKitId);
    }



    private static final String ORIGINAL_RENDER_KIT_ID_ATTR = RenderKitWrapper.class.getName() + ".ORIGINAL_RENDER_KIT_ID";

    public static void wrapRenderKit(FacesContext facesContext,
                                     UIComponent currentComponent,
                                     String wrapRenderKitId,
                                     Renderer masterRenderer)
    {
        wrapRenderKit(facesContext,
                      currentComponent,
                      wrapRenderKitId,
                      null,
                      masterRenderer);
    }

    public static void wrapRenderKit(FacesContext facesContext,
                                     UIComponent currentComponent,
                                     String wrapRenderKitId,
                                     Map masterRenderers,
                                     Renderer defaulMasterRenderer)
    {
        Tree tree = facesContext.getResponseTree();
        RenderKitFactory renderkitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        String originalRenderKitId = tree.getRenderKitId();

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
            wrapRenderKit = new RenderKitWrapper(originalRenderKitId,
                                                 masterRenderers,
                                                 defaulMasterRenderer);
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
        // get original renderKitId
        String originalRenderKitId = (String)currentComponent.getAttribute(ORIGINAL_RENDER_KIT_ID_ATTR);

        // remove attribute
        currentComponent.setAttribute(ORIGINAL_RENDER_KIT_ID_ATTR, null);

        // set tree to original renderKitId
        Tree tree = facesContext.getResponseTree();
        tree.setRenderKitId(originalRenderKitId);
    }

    public static RenderKit getOriginalRenderKit(FacesContext facesContext)
    {
        Tree tree = facesContext.getResponseTree();
        RenderKitFactory renderKitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(tree.getRenderKitId(),
                                                            facesContext);
        if (!(renderKit instanceof RenderKitWrapper))
        {
            throw new IllegalStateException("Current renderKit is not a RenderKitWrapper.");
        }

        return renderKitFactory.getRenderKit(((RenderKitWrapper)renderKit).getOriginalRenderKitId(),
                                             facesContext);
    }

    public static void originalEncodeBegin(FacesContext facesContext,
                                           UIComponent uiComponent)
        throws IOException
    {
        String rendererType = uiComponent.getRendererType();
        if (rendererType == null)
        {
            throw new IllegalArgumentException("Component has no renderer type.");
        }
        RenderKit renderKit = getOriginalRenderKit(facesContext);
        Renderer renderer = renderKit.getRenderer(rendererType);
        renderer.encodeBegin(facesContext, uiComponent);
    }

    public static void originalEncodeChildren(FacesContext facesContext,
                                              UIComponent uiComponent)
        throws IOException
    {
        String rendererType = uiComponent.getRendererType();
        if (rendererType == null)
        {
            throw new IllegalArgumentException("Component has no renderer type.");
        }
        RenderKit renderKit = getOriginalRenderKit(facesContext);
        Renderer renderer = renderKit.getRenderer(rendererType);
        renderer.encodeChildren(facesContext, uiComponent);
    }

    public static void originalEncodeEnd(FacesContext facesContext,
                                           UIComponent uiComponent)
        throws IOException
    {
        String rendererType = uiComponent.getRendererType();
        if (rendererType == null)
        {
            throw new IllegalArgumentException("Component has no renderer type.");
        }
        RenderKit renderKit = getOriginalRenderKit(facesContext);
        Renderer renderer = renderKit.getRenderer(rendererType);
        renderer.encodeEnd(facesContext, uiComponent);
    }

}

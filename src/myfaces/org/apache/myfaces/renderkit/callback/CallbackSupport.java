/**
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
package net.sourceforge.myfaces.renderkit.callback;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * By means of the CallbackSupport you can register a renderer, that wants to get
 * informed whenever one of it's children (or grand-children) is rendered.
 *
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class CallbackSupport
{
    private CallbackSupport() {}

    private static final String ORIGINAL_RENDER_KIT_ID_ATTR
        = CallbackSupport.class.getName() + ".ORIGINAL_RENDER_ID_KIT";
    private static final String ORIGINAL_RENDER_KIT_ATTR
        = CallbackSupport.class.getName() + ".ORIGINAL_RENDER_KIT";



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

        Map map = getCallbackRendererInfoMap(facesContext);
        map.put(component.getClientId(facesContext),
                new CallbackRendererInfo(component,
                                 callbackRenderer,
                                 false));
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

        Map map = getCallbackRendererInfoMap(facesContext);
        map.put(component.getClientId(facesContext),
                new CallbackRendererInfo(component,
                                 callbackRenderer,
                                 true));
    }


    public static void removeCallbackRenderer(FacesContext facesContext,
                                              UIComponent component,
                                              CallbackRenderer callbackRenderer)
    {
        Map map = getCallbackRendererInfoMap(facesContext);
        map.remove(component.getClientId(facesContext));
        if (map.isEmpty())
        {
            unwrapRenderKit(facesContext);
        }
    }

    public static void removeChildrenCallbackRenderer(FacesContext facesContext,
                                                      UIComponent component,
                                                      CallbackRenderer callbackRenderer)
    {
        removeCallbackRenderer(facesContext, component, callbackRenderer);
    }





    protected static RenderKit getOriginalRenderKit(FacesContext facesContext)
    {
        return (RenderKit)((ServletRequest)facesContext.getExternalContext().getRequest()).getAttribute(ORIGINAL_RENDER_KIT_ATTR);
    }

    protected static void wrapRenderKit(FacesContext facesContext)
    {
        //FIXME
        //Tree tree = facesContext.getTree();
        //String currentRenderKitId = tree.getRenderKitId();
        String currentRenderKitId = null;
        if (currentRenderKitId.equals(CallbackRenderKit.ID))
        {
            //already wrapped
            return;
        }

        RenderKitFactory renderkitFactory
            = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit currentRenderKit = renderkitFactory.getRenderKit(currentRenderKitId,
                                                                   facesContext);
        if (getOriginalRenderKit(facesContext) != null)
        {
            //RenderKit was already wrapped, but CallbackRenderKit was again replaced
            //by another RenderKit in the meantime.
            throw new IllegalStateException("CallbackRenderKit has been replaced by another RenderKit.");
        }
        ((ServletRequest)facesContext.getExternalContext().getRequest())
            .setAttribute(ORIGINAL_RENDER_KIT_ID_ATTR, currentRenderKitId);
        ((ServletRequest)facesContext.getExternalContext().getRequest())
            .setAttribute(ORIGINAL_RENDER_KIT_ATTR, currentRenderKit);

        // lookup CallbackRenderKit in RenderKitFactory...
        synchronized (renderkitFactory)
        {
            RenderKit callbackRenderKit = null;
            try
            {
                callbackRenderKit = renderkitFactory.getRenderKit(CallbackRenderKit.ID,
                                                                  facesContext);
            }
            catch (Exception e) {}
            // ...and add to RenderKitFactory if not yet registered
            if (callbackRenderKit == null)
            {
                callbackRenderKit = new CallbackRenderKit();
                renderkitFactory.addRenderKit(CallbackRenderKit.ID, callbackRenderKit);
            }
        }

        // set tree to wrapper (= CallbackRenderKit)
        //FIXME
        //tree.setRenderKitId(CallbackRenderKit.ID);
    }

    protected static void unwrapRenderKit(FacesContext facesContext)
    {
        String originalRenderKitId = (String)((ServletRequest)facesContext.getExternalContext().getRequest()).getAttribute(ORIGINAL_RENDER_KIT_ID_ATTR);
        //FIXME
        //facesContext.getTree().setRenderKitId(originalRenderKitId);
        ((ServletRequest)facesContext.getExternalContext().getRequest()).setAttribute(ORIGINAL_RENDER_KIT_ATTR, null);
    }




    private static final String CALLBACK_RENDERER_INFO_MAP_ATTR
        = CallbackSupport.class.getName() + ".CALLBACK_RENDERER_INFO_MAP";

    protected static Map getCallbackRendererInfoMap(FacesContext facesContext)
    {
        Map map = (Map)((ServletRequest)facesContext.getExternalContext().getRequest()).getAttribute(CALLBACK_RENDERER_INFO_MAP_ATTR);
        if (map == null)
        {
            map = new HashMap();
            ((ServletRequest)facesContext.getExternalContext().getRequest()).setAttribute(CALLBACK_RENDERER_INFO_MAP_ATTR, map);
        }
        return map;
    }


}

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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.component.ext.UINavigation;
import net.sourceforge.myfaces.component.ext.UINavigationItem;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.CommandEvent;
import javax.faces.event.FacesEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationItemRenderer
    extends HTMLRenderer
{
    public static final String TYPE = "NavigationItemRenderer";
    private static final String DECODED_ATTR = NavigationItemRenderer.class.getName() + ".DECODED";

    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UINavigationItem.TYPE);
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof UINavigationItem;
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        //super.decode must not be called, because value never comes from request

        //Remember, that we have decoded
        uiComponent.setAttribute(DECODED_ATTR, Boolean.TRUE);

        //decode
        String paramName = uiComponent.getCompoundId();
        String paramValue = facesContext.getServletRequest().getParameter(paramName);
        if (paramValue != null)
        {
            //link was clicked
            String commandName = paramValue;
            FacesEvent event = new CommandEvent(uiComponent, commandName);
            facesContext.addApplicationEvent(event);

            //find parent UINavigation
            UINavigation uiNavigation = findUINavigation(uiComponent);
            if (uiNavigation == null)
            {
                throw new FacesException("No parent UINavigation found!");
            }

            facesContext.addRequestEvent(uiNavigation, new UINavigation.ClickEvent(uiComponent));
        }
    }


    protected UINavigation findUINavigation(UIComponent uiComponent)
    {
        UIComponent parent = uiComponent.getParent();
        while (parent != null)
        {
            if (parent instanceof UINavigation)
            {
                return (UINavigation)parent;
            }
            parent = parent.getParent();
        }
        return null;
    }



    /**
     * UINavigationItem components do not render themselves. Method is directly called
     * by NavigationRenderer.
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        Boolean b = (Boolean)uiComponent.getAttribute(DECODED_ATTR);
        if (b == null || !b.booleanValue())
        {
            //There was no decoding, so we can assume that the sate has not been restored and we can
            //explicitly restore state for that component
            RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId());
            Renderer stateRenderer = null;
            try
            {
                stateRenderer = renderKit.getRenderer(StateRenderer.TYPE);
            }
            catch (Exception e)
            {
                //No StateRenderer
            }
            if (stateRenderer != null)
            {
                stateRenderer.decode(facesContext, uiComponent);
            }
        }


        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<a href=\"");

        //Modify URL for the faces servlet mapping:
        ServletContext servletContext = facesContext.getServletContext();
        ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
        ServletMapping sm = smf.getServletMapping(servletContext);
        String treeURL = sm.encodeTreeIdForURL(facesContext, facesContext.getResponseTree().getTreeId());

        HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();
        String href = request.getContextPath() + treeURL;

        //Encode URL for those still using HttpSessions... ;-)
        href = ((HttpServletResponse)facesContext.getServletResponse()).encodeURL(href);

        writer.write(href);

        //value
        writer.write('?');
        writer.write(uiComponent.getCompoundId());
        writer.write("=1");

        //state:
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory("javax.faces.render.RenderKitFactory");
        RenderKit renderKit = rkFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        renderer.encodeChildren(facesContext, uiComponent);

        writer.write("\">");

        if (((UINavigationItem)uiComponent).isOpen())
        {
            writer.write("<b>");
        }
    }

    /**
     * UINavigationItem components do not render themselves. Method is directly called
     * by NavigationRenderer.
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        if (((UINavigationItem)uiComponent).isOpen())
        {
            writer.write("</b>");
        }
        writer.write("</a>");
    }

}

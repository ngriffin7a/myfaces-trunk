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
import net.sourceforge.myfaces.component.UICommand;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.ext.UINavigation;
import net.sourceforge.myfaces.renderkit.attr.ext.NavigationItemRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.util.bundle.BundleUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationItemRenderer
    extends HTMLRenderer
    implements NavigationItemRendererAttributes
{
    public static final String TYPE = "NavigationItem";

    protected RenderKitFactory _rkFactory;

    public NavigationItemRenderer()
    {
        _rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    }

    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UICommand.TYPE);
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof javax.faces.component.UICommand;
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        //super.decode must not be called, because value never comes from request

        //Remember, that we have decoded
        //uiComponent.setAttribute(DECODED_ATTR, Boolean.TRUE);

        uiComponent.setValid(true);

        //decode
        String paramName = uiComponent.getClientId(facesContext);
        String paramValue = facesContext.getServletRequest().getParameter(paramName);
        if (paramValue != null)
        {
            //item was clicked
            ((UICommand)uiComponent).fireActionEvent(facesContext);
        }
    }

    /**
     * UINavigationItem common do not render themselves. Method is directly called
     * by NavigationRenderer.
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        /*
        Boolean b = (Boolean)uiComponent.getAttribute(DECODED_ATTR);
        if (b == null || !b.booleanValue())
        {
            //There was no decoding, so we can assume that the state has not been restored yet and we can
            //explicitly restore state for that component
            RenderKit renderKit = _rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
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
        */


        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<a href=\"");

        //Modify URL for the faces servlet mapping:
        ServletContext servletContext = facesContext.getServletContext();
        ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
        ServletMapping sm = smf.getServletMapping(servletContext);
        String treeURL = sm.encodeTreeIdForURL(facesContext, facesContext.getTree().getTreeId());

        HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();
        String href = request.getContextPath() + treeURL;

        //Encode URL for those still using HttpSessions... ;-)
        href = ((HttpServletResponse)facesContext.getServletResponse()).encodeURL(href);

        writer.write(href);

        //value
        writer.write('?');
        writer.write(uiComponent.getClientId(facesContext));
        writer.write("=1");

        //state:
        RenderKit renderKit = _rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        renderer.encodeChildren(facesContext, uiComponent);

        writer.write("\">");

        String label;
        String key = (String)uiComponent.getAttribute(NavigationItemRenderer.KEY_ATTR);
        if (key != null)
        {
            String bundle = (String)uiComponent.getAttribute(NavigationItemRenderer.BUNDLE_ATTR);
            if (bundle == null)
            {
                UIComponent parent = uiComponent.getParent();
                while (bundle == null && parent != null)
                {
                    if (parent.getRendererType().equals(NavigationRenderer.TYPE))
                    {
                        bundle = (String)parent.getAttribute(NavigationItemRenderer.BUNDLE_ATTR);
                        break;
                    }
                    parent = parent.getParent();
                }
            }
            if (bundle == null)
            {
                LogUtil.getLogger().warning("No bundle defined for component " + UIComponentUtils.toString(uiComponent));
                label = key;
            }
            else
            {
                label = BundleUtils.getString(facesContext, bundle, key);
            }
        }
        else
        {
            label = (String)uiComponent.getAttribute(NavigationItemRenderer.LABEL_ATTR);
        }

        boolean open = UIComponentUtils.getBooleanAttribute(uiComponent,
                                                            UINavigation.UINavigationItem.OPEN_ATTR,
                                                            false);
        renderLabel(facesContext, writer, uiComponent, label, open);

        writer.write("</a>");

    }

    /**
     * UINavigationItem common do not render themselves. Method is directly called
     * by NavigationRenderer.
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
    }


    /**
     * Convenience method to be overwritten by derived Renderers.
     * @param facesContext
     * @param writer
     * @param uiComponent
     * @param label
     * @param open
     * @throws IOException
     */
    protected void renderLabel(FacesContext facesContext, ResponseWriter writer,
                               UIComponent uiComponent, String label, boolean open)
        throws IOException
    {
        if (open)
        {
            writer.write("<b>");
        }

        writer.write(HTMLEncoder.encode(label, true, true));

        if (open)
        {
            writer.write("</b>");
        }

    }



}

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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.ext.UINavigationItem;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.attr.ext.NavigationRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HyperlinkRenderer;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.bundle.BundleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationItemRenderer
extends HyperlinkRenderer
{
    private static final Log log = LogFactory.getLog(NavigationItemRenderer.class);

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

    /**
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        String label = null;
        String key = (String)uiComponent.getAttributes().get(JSFAttr.KEY_ATTR);
        if (key == null)
        {
            label = (String)uiComponent.getAttributes().get(JSFAttr.LABEL_ATTR);
        }

        if (label == null && key == null)
        {
            writer.write("&nbsp;");
            return;
        }

        writer.write("<a href=\"");

        //Modify URL for the faces servlet mapping:
        ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();
        //TODO: fixme
        /*
        ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
        ServletMapping sm = smf.getServletMapping(servletContext);
        String treeURL = sm.encodeTreeIdForURL(facesContext, facesContext.getTree().getTreeId());

        HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
        String href = request.getContextPath() + treeURL;
        */
        String href = null; // TODO
        throw new UnsupportedOperationException("fixme");



        //Encode URL
        href = facesContext.getExternalContext().encodeActionURL(href);

        writer.write(href);

        //value
        if (href.indexOf('?') == -1)
        {
            writer.write('?');
        }
        else
        {
            writer.write('&');
        }
        writer.write(uiComponent.getClientId(facesContext));
        writer.write("=1");

        //state:
        RenderKit renderKit = _rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        renderer.encodeChildren(facesContext, uiComponent);

        writer.write("\"");

        // HTML-Attributes
        // commandClass rendered by NavigationRenderer
        HTMLUtil.renderCssClass(writer, uiComponent, JSFAttr.COMMAND_CLASS_ATTR);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.ANCHOR_ATTRIBUTES);

        writer.write(">");

        UIComponent navigationComponent = uiComponent.getParent();
        while (navigationComponent != null &&
            !navigationComponent.getRendererType().equals(NavigationRenderer.TYPE))
        {
            navigationComponent = navigationComponent.getParent();
        }

        if (key != null)
        {
            String bundle = (String)uiComponent.getAttributes().get(JSFAttr.BUNDLE_ATTR);
            if (bundle == null && navigationComponent != null)
            {
                bundle = (String)navigationComponent.getAttributes().get(JSFAttr.BUNDLE_ATTR);
            }
            if (bundle == null)
            {
                log.error("No bundle defined for component " + UIComponentUtils.toString(uiComponent));
                label = key;
            }
            else
            {
                label = BundleUtils.getString(facesContext, bundle, key);
            }
        }

        boolean open = ((UINavigationItem)uiComponent).isOpen();
        boolean active = ((UINavigationItem)uiComponent).isActive();

        renderLabel(facesContext, writer, uiComponent, navigationComponent, label, open, active);

        writer.write("</a>");

    }


    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent)
        throws IOException
    {
    }


    /**
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
    }


    /**
     * Convenience method to be overwritten by derived Renderers.
      */
    protected void renderLabel(FacesContext facesContext,
                               ResponseWriter writer,
                               UIComponent uiComponent,
                               UIComponent navigatioComponent,
                               String label,
                               boolean open,
                               boolean active)
        throws IOException
    {
        if (navigatioComponent != null)
        {
            String style;
            if (active)
            {
                style = (String)navigatioComponent.getAttributes().get(NavigationRendererAttributes.ACTIVE_ITEM_CLASS_ATTR);
            }
            else
            {
                style = open ? (String)navigatioComponent.getAttributes().get(NavigationRendererAttributes.OPEN_ITEM_CLASS_ATTR) :
                                    (String)navigatioComponent.getAttributes().get(NavigationRendererAttributes.ITEM_CLASS_ATTR);;
            }
            if (style != null)
            {
                writer.write("<span class=\"");
                writer.write(style);
                writer.write("\">");
                writer.write(HTMLEncoder.encode(label, true, true));
                writer.write("</span>");
            }
            else
            {
                writer.write(HTMLEncoder.encode(label, true, true));
            }
        }
        else
        {
            writer.write(HTMLEncoder.encode(label, true, true));
        }
    }



}

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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.component.UIParameter;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.state.JspInfo;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FactoryFinder;
import javax.faces.tree.Tree;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.CommandEvent;
import javax.faces.event.FacesEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HyperlinkRenderer
        extends HTMLRenderer
{
    public static final String TYPE = "HyperlinkRenderer";
    public String getRendererType()
    {
        return TYPE;
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        //super.decode must not be called, because value never comes from request

        String paramName = uiComponent.getCompoundId();
        String paramValue = facesContext.getServletRequest().getParameter(paramName);
        if (paramValue != null)
        {
            //link was clicked
            String commandName = paramValue;
            FacesEvent event = new CommandEvent(uiComponent, commandName);
            facesContext.addApplicationEvent(event);
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<a href=\"");
        String href = (String)uiComponent.getAttribute(net.sourceforge.myfaces.component.UICommand.HREF_ATTR);
        if (href == null)
        {
            //Modify URL for the faces servlet mapping:
            ServletContext servletContext = facesContext.getServletContext();
            ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
            ServletMapping sm = smf.getServletMapping(servletContext);
            String treeURL = sm.encodeTreeIdForURL(facesContext, facesContext.getResponseTree().getTreeId());

            HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();
            href = request.getContextPath() + treeURL;
        }

        //Encode URL for those still using HttpSessions... ;-)
        href = ((HttpServletResponse)facesContext.getServletResponse()).encodeURL(href);

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
        writer.write(uiComponent.getCompoundId());
        writer.write('=');
        writer.write(URLEncoder.encode(getStringValue(facesContext, uiComponent), "UTF-8"));

        //nested parameters
        Iterator children = uiComponent.getChildren();
        if (!children.hasNext())
        {
            //no children can mean that the tree is not yet built
            //so, we try the static tree
            Tree staticTree = JspInfo.getStaticTree(facesContext,
                                                    facesContext.getResponseTree().getTreeId());
            UIComponent staticComp = null;
            try
            {
                staticComp = staticTree.getRoot().findComponent(uiComponent.getCompoundId());
            }
            catch (IllegalArgumentException e) {}

            if (staticComp == null)
            {
                LogUtil.getLogger().warning("Component " + uiComponent.getCompoundId() + " not found in static tree!?");
            }
            else
            {
                children = staticComp.getChildren();
            }
        }
        while (children.hasNext())
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                if (name == null)
                {
                    name = child.getCompoundId();
                }
                writer.write('&');
                writer.write(name);
                writer.write('=');
                writer.write(URLEncoder.encode(getStringValue(facesContext, child), "UTF-8"));
            }
        }

        //state:
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = rkFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        renderer.encodeChildren(facesContext, uiComponent);

        writer.write("\">");
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</a>");
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UICommand.TYPE);
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof UICommand;
    }
}

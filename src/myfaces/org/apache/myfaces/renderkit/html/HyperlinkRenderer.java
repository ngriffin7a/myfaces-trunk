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

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.component.UIParameter;
import net.sourceforge.myfaces.renderkit.attr.HyperlinkRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ApplicationEvent;
import javax.faces.event.CommandEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HyperlinkRenderer
        extends HTMLRenderer
        implements HyperlinkRendererAttributes
{
    public static final String TYPE = "Hyperlink";
    public String getRendererType()
    {
        return TYPE;
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        //super.decode must not be called, because value never comes from request

        String paramName = uiComponent.getClientId(facesContext);
        String paramValue = facesContext.getServletRequest().getParameter(paramName);
        if (paramValue != null)
        {
            //link was clicked
            String commandName = paramValue;

            uiComponent.setValue(paramValue);
            uiComponent.setValid(true);

            //Old event processing:
            ApplicationEvent event = new CommandEvent(uiComponent, commandName);
            facesContext.addApplicationEvent(event);

            //New event processing:
            if (uiComponent instanceof UICommand)
            {
                ((UICommand)uiComponent).fireActionEvent(facesContext);
            }
            else
            {
                LogUtil.getLogger().warning("Component " + uiComponent.getClientId(facesContext) + "is no UICommand.");
            }
        }
        else
        {
            uiComponent.setValid(true);
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<a href=\"");
        String href = (String)uiComponent.getAttribute(HREF_ATTR);
        if (href == null)
        {
            //Modify URL for the faces servlet mapping:
            ServletContext servletContext = facesContext.getServletContext();
            ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
            ServletMapping sm = smf.getServletMapping(servletContext);
            String treeURL = sm.encodeTreeIdForURL(facesContext, facesContext.getTree().getTreeId());

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
        writer.write(uiComponent.getClientId(facesContext));
        writer.write('=');
        writer.write(URLEncoder.encode(getStringValue(facesContext, uiComponent), "UTF-8"));

        //nested parameters
        Iterator children = uiComponent.getChildren();
        while (children.hasNext())
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                if (name == null)
                {
                    name = child.getClientId(facesContext);
                }
                writer.write('&');
                writer.write(name);
                writer.write('=');
                writer.write(URLEncoder.encode(getStringValue(facesContext, child), "UTF-8"));
            }
        }

        //state:
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        renderer.encodeChildren(facesContext, uiComponent);

        writer.write("\"");

        //css class:
        String cssClass = (String)uiComponent.getAttribute(COMMAND_CLASS_ATTR);
        if (cssClass != null)
        {
            writer.write(" class=\"");
            writer.write(cssClass);
            writer.write("\"");
        }

        writer.write(">");
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

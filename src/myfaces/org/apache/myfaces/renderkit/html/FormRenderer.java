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
import net.sourceforge.myfaces.renderkit.attr.FormRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
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
public class FormRenderer
        extends HTMLRenderer
{
    public static final String TYPE = "Form";
    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<form method=\"post\" action=\"");
        writer.write(getActionStr(context, component));
        writer.write("\"");
        CommonAttributes.renderEventHandlerAttributes(context, component);
        CommonAttributes.renderUniversalAttributes(context, component);
        CommonAttributes.renderAttributes(context, component, FormRendererAttributes.COMMON_FORM_ATTRIBUTES);
        writer.write(">");
    }

    private String getActionStr(FacesContext facesContext, UIComponent form)
        throws IOException
    {
        HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();
        HttpServletResponse response = (HttpServletResponse)facesContext.getServletResponse();

        ServletContext servletContext = facesContext.getServletContext();
        ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
        ServletMapping sm = smf.getServletMapping(servletContext);
        String treeURL = sm.encodeTreeIdForURL(facesContext, facesContext.getTree().getTreeId());

        String action = request.getContextPath() + treeURL;

        //Encode URL for those still using HttpSessions... ;-)
        action = response.encodeURL(action);

        return action;
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        renderer.encodeChildren(facesContext, component);

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</form>");
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIForm.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UIForm;
    }

}

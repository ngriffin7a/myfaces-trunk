/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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

import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class FormRenderer
        extends HTMLRenderer
{
    public static final String TYPE = "FormRenderer";
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
        writer.write("\">");
    }

    private String getActionStr(FacesContext context, UIComponent form)
    {
        HttpServletRequest request = (HttpServletRequest)context.getServletRequest();
        HttpServletResponse response = (HttpServletResponse)context.getServletResponse();
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append("/faces");    //TODO: hardcoded?
        sb.append(context.getResponseTree().getTreeId());
        return response.encodeURL(sb.toString());
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = rkFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId());
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

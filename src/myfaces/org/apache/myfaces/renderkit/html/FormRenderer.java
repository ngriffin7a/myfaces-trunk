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
import net.sourceforge.myfaces.renderkit.*;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import java.io.IOException;

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


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class FormRenderer
extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "Form";

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<form method=\"post\" action=\"");
        writer.write(getActionStr(facesContext));
        writer.write('"');
        HTMLUtil.renderCssClass(writer, uiComponent, JSFAttr.FORM_CLASS_ATTR);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiComponent, HTML.FORM_ATTRIBUTES);

        String formName = ((UIForm) uiComponent).getFormName();

        if (formName != null)
        {
            writer.write(" name=\"");
            writer.write(formName);
            writer.write('"');
        }

        writer.write('>');
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        RenderKitFactory rkFactory =
            (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit        renderKit =
            rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
        Renderer         renderer  = renderKit.getRenderer(StateRenderer.TYPE);
        renderer.encodeChildren(facesContext, uiComponent);

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("</form>");
    }

    private String getActionStr(FacesContext facesContext)
    {
        HttpServletRequest    request        =
            (HttpServletRequest) facesContext.getExternalContext().getRequest();

        ServletContext        servletContext =
            (ServletContext) facesContext.getExternalContext().getContext();
        ServletMappingFactory smf            =
            MyFacesFactoryFinder.getServletMappingFactory(servletContext);
        ServletMapping        sm             = smf.getServletMapping(servletContext);
        String                treeURL        =
            sm.encodeTreeIdForURL(
                facesContext,
                facesContext.getTree().getTreeId());

        String                action         = request.getContextPath() + treeURL;

        //Encode URL
        action = facesContext.getExternalContext().encodeURL(action);

        return action;
    }
}

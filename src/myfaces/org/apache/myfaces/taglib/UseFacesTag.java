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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.webapp.JspResponseWriter;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FactoryFinder;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class UseFacesTag
        extends BodyTagSupport
{
    protected FacesContext getFacesContext()
    {
        //FacesServlet saves the FacesContext as request attribute:
        FacesContext facesContext
            = (FacesContext)super.pageContext.getAttribute("javax.faces.context.FacesContext",
                                                           PageContext.REQUEST_SCOPE);
        if (facesContext == null)
        {
            throw new IllegalStateException("No faces context!?");
        }
        return facesContext;
    }


    public int doStartTag() throws JspException
    {
        if (MyFacesConfig.isStateEncodingOnTheFly())
        {
            ResponseWriter writer = new JspResponseWriter(super.pageContext.getOut());
            getFacesContext().setResponseWriter(writer);
            return BodyTag.EVAL_BODY_INCLUDE;
        }
        else
        {
            //ResponseWriter will be set in doInitBody()
            return BodyTag.EVAL_BODY_BUFFERED;
        }
    }

    public void doInitBody() throws JspException
    {
        ResponseWriter writer = new JspResponseWriter(getBodyContent());
        getFacesContext().setResponseWriter(writer);
    }

    public int doAfterBody() throws JspException
    {
        if (MyFacesConfig.isStateEncodingOnTheFly())
        {
            //nothing to do
        }
        else
        {
            FacesContext facesContext = getFacesContext();

            RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit(facesContext.getResponseTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
            if (renderer == null)
            {
                LogUtil.getLogger().info("UseFacesTag.doAfterBody() - No StateRenderer found.");
                BodyContent bodyContent = getBodyContent();
                try
                {
                    bodyContent.writeOut(bodyContent.getEnclosingWriter());
                }
                catch (IOException e)
                {
                    throw new JspException(e);
                }
            }
            else
            {
                try
                {
                    facesContext.getServletRequest().setAttribute(StateRenderer.BODY_CONTENT_REQUEST_ATTR,
                                                                  getBodyContent());
                    renderer.encodeEnd(facesContext, null);
                }
                catch (IOException e)
                {
                    throw new JspException(e);
                }
            }
        }

        return BodyTag.SKIP_BODY;
    }
}

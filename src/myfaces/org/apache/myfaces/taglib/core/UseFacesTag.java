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
package net.sourceforge.myfaces.taglib.core;

import net.sourceforge.myfaces.MyFacesConfig;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.renderkit.html.state.client.ClientStateSaver;
import net.sourceforge.myfaces.taglib.MyFacesBodyTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.webapp.JspResponseWriter;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UseFacesTag
    extends MyFacesBodyTag
{
    private static final Log log = LogFactory.getLog(UseFacesTag.class);

    public String getComponentType()
    {
        //Should not be called normally
        return "Root";
    }

    public String getDefaultRendererType()
    {
        return null;
    }

    protected FacesContext getFacesContext()
    {
        //FacesServlet saves the FacesContext as request attribute:
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null)
        {
            throw new IllegalStateException("No faces context!?");
        }
        return facesContext;
    }


    public int getDoStartValue() throws JspException
    {
        //ResponseWriter will be set in doInitBody()
        int mode = MyFacesConfig.getStateSavingMode(super.pageContext.getServletContext());
        if (mode == MyFacesConfig.STATE_SAVING_MODE__SERVER_SESSION)
        {
            return BodyTag.EVAL_BODY_INCLUDE;
        }
        else
        {
            return BodyTag.EVAL_BODY_BUFFERED;
        }
    }

    public void doInitBody() throws JspException
    {
        ResponseWriter writer = new JspResponseWriter(super.pageContext);
        getFacesContext().setResponseWriter(writer);
    }

    public int doAfterBody() throws JspException
    {
        FacesContext facesContext = getFacesContext();

        int mode = MyFacesConfig.getStateSavingMode(super.pageContext.getServletContext());

        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(StateRenderer.TYPE);
        if (renderer == null)
        {
            if (log.isInfoEnabled()) log.info("UseFacesTag.doAfterBody() - No StateRenderer found.");
            if (mode != MyFacesConfig.STATE_SAVING_MODE__SERVER_SESSION)
            {
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
        }
        else
        {
            try
            {
                if (mode != MyFacesConfig.STATE_SAVING_MODE__SERVER_SESSION)
                {
                    ((ServletRequest)facesContext.getExternalContext().getRequest()).setAttribute(ClientStateSaver.BODY_CONTENT_REQUEST_ATTR,
                                                                  getBodyContent());
                }
                renderer.encodeEnd(facesContext, null);
            }
            catch (IOException e)
            {
                throw new JspException(e);
            }
        }

        return BodyTag.SKIP_BODY;
    }
}

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
package net.sourceforge.myfaces.lifecycle;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.tree.Tree;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.io.IOException;

/**
 * ViewHandler for dispatching JSP pages.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ViewHandlerJspImpl
        implements ViewHandler
{
    public void renderView(FacesContext facesContext)
        throws IOException, ServletException
    {
        ServletRequest request = facesContext.getServletRequest();
        ServletContext servletContext = facesContext.getServletContext();
        Tree tree = facesContext.getTree();

        //Build component tree from parsed JspInfo so that all components
        //already exist in case a component needs it's children prior to
        //rendering it's body
        /*
        Tree staticTree = JspInfo.getTree(facesContext, tree.getTreeId());
        TreeCopier tc = new TreeCopier(facesContext);
        tc.setOverwriteComponents(false);
        tc.setOverwriteAttributes(false);
        tc.copyTree(staticTree, tree);
        */

        //Look for a StateRenderer and prepare for state saving
        RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = rkFactory.getRenderKit(tree.getRenderKitId());
        Renderer renderer = null;
        try
        {
            renderer = renderKit.getRenderer(StateRenderer.TYPE);
        }
        catch (Exception e)
        {
            //No StateRenderer
        }
        if (renderer != null)
        {
            try
            {
                LogUtil.getLogger().finest("StateRenderer found, calling encodeBegin...");
                renderer.encodeBegin(facesContext, null);
            }
            catch (IOException e)
            {
                throw new FacesException("Error saving state", e);
            }
        }

        //forward request to JSP page
        ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
        ServletMapping sm = smf.getServletMapping(servletContext);
        String forwardURL = sm.mapTreeIdToFilename(servletContext, tree.getTreeId());

        RequestDispatcher requestDispatcher
            = facesContext.getServletRequest().getRequestDispatcher(forwardURL);
        try
        {
            requestDispatcher.forward(request, facesContext.getServletResponse());
        }
        catch(IOException ioe)
        {
            throw new IOException(ioe.getMessage());
        }
        catch(ServletException se)
        {
            throw new ServletException(se.getMessage());
        }
    }

}

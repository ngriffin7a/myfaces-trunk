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
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.tree.Tree;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.io.IOException;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ViewHandlerImpl
        implements ViewHandler
{
    public void renderView(FacesContext facesContext)
        throws IOException, ServletException
    {
        ServletRequest request = facesContext.getServletRequest();
        ServletContext servletContext = facesContext.getServletContext();
        Tree responseTree = facesContext.getResponseTree();

        ServletMappingFactory smf = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
        ServletMapping sm = smf.getServletMapping(servletContext);
        String forwardURL = sm.mapTreeIdToFilename(servletContext, responseTree.getTreeId());

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

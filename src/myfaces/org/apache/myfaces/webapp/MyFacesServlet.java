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
package net.sourceforge.myfaces.webapp;

import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.MyFacesConfig;

import javax.faces.webapp.FacesServlet;
import javax.servlet.*;
import javax.servlet.jsp.JspFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesServlet
    implements Servlet
{

    private final FacesServlet _facesServlet = new FacesServlet();

    public void destroy()
    {
        _facesServlet.destroy();
    }

    public ServletConfig getServletConfig()
    {
        return _facesServlet.getServletConfig();
    }

    public String getServletInfo()
    {
        return _facesServlet.getServletInfo();
    }

    public void init(ServletConfig servletConfig)
        throws ServletException
    {
        synchronized (System.out)
        {
            PrintStream oldOut = System.out;
            //Sun's copyright is nice and important, but drives me crazy during testing...
            //Uncomment the following line during development if you feel the same:
            System.setOut(new PrintStream(new ByteArrayOutputStream()));
            _facesServlet.init(servletConfig);
            System.setOut(oldOut);

            //Wrap JspFactory to enhance Logging of ServletExceptions
            if (MyFacesConfig.isWrapPageContext(servletConfig.getServletContext()))
            {
                wrapJspFactory();
            }
        }

        LogUtil.getLogger().info("MyFacesServlet for context '" + servletConfig.getServletContext().getRealPath("/") + "' initialized.");
    }

    public void service(ServletRequest request, ServletResponse response)
        throws IOException, ServletException
    {
        try
        {
            _facesServlet.service(request, response);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            Throwable t = e.getCause();
            while (t != null)
            {
                System.err.println("Root cause:");
                t.printStackTrace(System.err);
                t = t.getCause();
            }
            throw new ServletException(e);
        }
    }

    private void wrapJspFactory()
    {
        final JspFactory defaultFactory = JspFactory.getDefaultFactory();
        JspFactory.setDefaultFactory(new JspFactoryWrapper(defaultFactory));
    }
}

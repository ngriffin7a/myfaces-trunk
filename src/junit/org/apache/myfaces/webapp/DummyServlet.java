/*
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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

import javax.servlet.*;
import java.io.IOException;

/**
 * Used by JspViewHandlerImplTest to test servlet mappings.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/05/26 09:24:35  manolito
 * no message
 *
 */
public class DummyServlet
        implements Servlet
{
    //private static final Log log = LogFactory.getLog(DummyServlet.class);

    private ServletConfig _servletConfig;

    public String getServletInfo()
    {
        return "Dummy servlet for testing servlet mappings";
    }

    public void init(ServletConfig servletConfig) throws ServletException
    {
        _servletConfig = servletConfig;
    }

    public ServletConfig getServletConfig()
    {
        return _servletConfig;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException,
                                                                                               IOException
    {
        //do nothing
    }

    public void destroy()
    {
    }
}

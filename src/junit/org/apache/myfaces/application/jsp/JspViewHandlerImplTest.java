/**
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
package net.sourceforge.myfaces.application.jsp;

import net.sourceforge.myfaces.MyFacesBaseTest;
import net.sourceforge.myfaces.context.servlet.ServletContextMockImpl;
import net.sourceforge.myfaces.context.servlet.ServletFacesContextImpl;
import net.sourceforge.myfaces.context.servlet.ServletRequestMockImpl;

import javax.servlet.ServletContext;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspViewHandlerImplTest
    extends MyFacesBaseTest
{
    private static final String WEB_XML_SYSTEM_ID = "/WEB-INF/web.xml";
    private static final String WEB_XML_PATH_TEST = 
        "net.sourceforge.myfaces.resource".replace('.','/') + "/servletmapping_web.xml";

    public JspViewHandlerImplTest(String name)
    {
        super(name);
    }

    protected ServletContext setUpServletContext()
    {
        ServletContextMockImpl servletContextMock
                = (ServletContextMockImpl)super.setUpServletContext();
        servletContextMock.addResource(WEB_XML_SYSTEM_ID, WEB_XML_PATH_TEST);
        return servletContextMock;
    }

    public void testViewIdPathSimple() throws Exception
    {
        testViewIdPath("/myfaces", "/test", "/abc.jsp", "/myfaces/abc.jsp");
    }

    public void testViewIdPathNoServletPath() throws Exception {
        testViewIdPath("", "/test.jsf", "/xyz.jsp", "/xyz.jsp");
    }

    public void testViewIdPathJSFExtensionWithJSP() throws Exception {
        testViewIdPath("/extension/test.jsf", null, "/myfaces/abc.jsp", "/myfaces/abc.jsf");
    }

    public void testViewIdPathJSFExtension() throws Exception {
        testViewIdPath("/extension/test.jsf", null, "/extension/bde", "/extension/bde.jsf");
    }

    private void testViewIdPath(String servletPath, String pathInfo, String viewId, String viewIdexp)
    {
        ((ServletRequestMockImpl)_httpServletRequest).setServletPath(servletPath);
        ((ServletRequestMockImpl)_httpServletRequest).setPathInfo(pathInfo);

        //Standard implementation caches servlet path, so we must create a new FacesContext
        //for each test:
        _facesContext = new ServletFacesContextImpl(_servletContext,
                                                    _httpServletRequest,
                                                    _httpServletResponse);

        JspViewHandlerImpl viewHandler = new JspViewHandlerImpl();
        String viewpath = viewHandler.getViewIdPath(_facesContext, viewId);
        assertEquals(viewIdexp, viewpath);
    }

}

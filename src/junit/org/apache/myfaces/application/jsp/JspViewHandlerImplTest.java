/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    /*
    public void testStrutsPath() throws Exception {
        testViewIdPath("/start.do", null, "/abc.jsp", "/extension/bde.jsf");
    }
    */

    private void testViewIdPath(String servletPath,
                                String pathInfo,
                                String viewId,
                                String expectedViewIdPath)
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
        assertEquals(expectedViewIdPath, viewpath);
    }



}

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

import javax.faces.context.FacesContext;

import junit.framework.TestCase;
import net.sourceforge.myfaces.application.jsp.JspViewHandlerImpl;
import net.sourceforge.myfaces.context.ExternalContextMockImpl;
import net.sourceforge.myfaces.context.FacesContextMockImpl;
import net.sourceforge.myfaces.webapp.webxml.WebXml;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspViewHandlerImplTest
    extends TestCase
{
    private FacesContext _facesContext;

    private static final String WEB_XML_SYSTEM_ID = "/WEB-INF/web.xml";    
    private static final String WEB_XML_PATH_TEST = 
        "net.sourceforge.myfaces.resource".replace('.','/') + "/servletmapping_web.xml";

    protected void setUp() throws Exception
    {
        _facesContext = new FacesContextMockImpl();
        ExternalContextMockImpl externalContext = (ExternalContextMockImpl)_facesContext.getExternalContext();
        externalContext.addResourceMapping(WEB_XML_SYSTEM_ID, WEB_XML_PATH_TEST);
        externalContext.setRequestPathInfo(null);
        WebXml.init(externalContext);
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
        ExternalContextMockImpl externalContext = (ExternalContextMockImpl)_facesContext.getExternalContext();
        JspViewHandlerImpl viewHandler = new JspViewHandlerImpl();
        externalContext.setRequestServletPath(servletPath);
        externalContext.setRequestPathInfo(pathInfo);
        String viewpath = viewHandler.getViewIdPath(_facesContext, viewId);
        assertEquals(viewIdexp, viewpath);
    }

    protected void tearDown() throws Exception
    {
        _facesContext = null;
    }
}

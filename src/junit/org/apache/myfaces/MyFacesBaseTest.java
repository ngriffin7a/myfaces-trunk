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
package net.sourceforge.myfaces;

import junit.framework.TestCase;
import net.sourceforge.myfaces.context.servlet.ServletContextMockImpl;
import net.sourceforge.myfaces.context.servlet.ServletFacesContextImpl;
import net.sourceforge.myfaces.context.servlet.ServletRequestMockImpl;
import net.sourceforge.myfaces.context.servlet.ServletResponseMockImpl;
import net.sourceforge.myfaces.webapp.StartupServletContextListener;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class MyFacesBaseTest
    extends TestCase
{
    //private static final Log log = LogFactory.getLog(MyFacesBaseTest.class);

    private static final String RESOURCE_PATH = "net.sourceforge.myfaces.resource".replace('.', '/');

    protected Application _application;
    protected ServletContext _servletContext;
    protected HttpServletRequest _httpServletRequest;
    protected HttpServletResponse _httpServletResponse;
    protected Lifecycle _lifecycle;
    protected FacesContext _facesContext;


    public MyFacesBaseTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        _servletContext = setUpServletContext();
        StartupServletContextListener.initFaces(_servletContext);

        _httpServletRequest = new ServletRequestMockImpl(getCookies());
        _httpServletResponse = new ServletResponseMockImpl();

        _facesContext = new ServletFacesContextImpl(_servletContext,
                                                    _httpServletRequest,
                                                    _httpServletResponse);
        _application = _facesContext.getApplication();
    }


    protected ServletContext setUpServletContext()
    {
        ServletContextMockImpl servletContext = new ServletContextMockImpl();
        servletContext.addResource("/WEB-INF/faces-config.xml",
                                   RESOURCE_PATH + "/junit-faces-config.xml");
        servletContext.addResource("/WEB-INF/web.xml",
                                   RESOURCE_PATH + "/junit-web.xml");
        return servletContext;
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        _application = null;
        _servletContext = null;
        _httpServletRequest = null;
        _httpServletResponse = null;
        _lifecycle = null;
        _facesContext = null;
    }

    /** override where necessary */
    protected Cookie[] getCookies()
    {
        return new Cookie[0];
    }
}

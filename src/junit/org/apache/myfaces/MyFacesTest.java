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
package net.sourceforge.myfaces;

import junit.framework.TestCase;
import net.sourceforge.myfaces.application.ApplicationImpl;
import net.sourceforge.myfaces.config.FacesConfig;
import net.sourceforge.myfaces.config.FacesConfigFactory;
import net.sourceforge.myfaces.context.FacesContextMockImpl;
import net.sourceforge.myfaces.context.ServletContextMockImpl;
import net.sourceforge.myfaces.context.ServletRequestMockImpl;
import net.sourceforge.myfaces.context.ServletResponseMockImpl;
import net.sourceforge.myfaces.context.servlet.ServletExternalContextImpl;
import net.sourceforge.myfaces.el.VariableResolverImpl;
import net.sourceforge.myfaces.lifecycle.LifecycleImpl;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesTest
    extends TestCase
{
    protected Application _application;
    protected ServletContext _servletContext;
    protected HttpServletRequest _httpServletRequest;
    protected HttpServletResponse _httpServletResponse;
    protected Lifecycle _lifecycle;
    protected FacesContext _facesContext;


    public MyFacesTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        _servletContext = new ServletContextMockImpl();
        _application = new ApplicationImpl();
        _application.setVariableResolver(new VariableResolverImpl());
        
        _httpServletRequest = new ServletRequestMockImpl();
        _httpServletResponse = new ServletResponseMockImpl();

//        FIXME
//        LifecycleFactory lf = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
//        _lifecycle = lf.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        _lifecycle = new LifecycleImpl();
        
        _facesContext = new FacesContextMockImpl(_servletContext,
                                            _httpServletRequest,
                                            _httpServletResponse,
                                            _lifecycle,
                                            _application);
        
        ServletContext servletContext = new ServletContextMockImpl();
//        FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(servletContext);
//        ExternalContext externalContext = new ServletExternalContextImpl(servletContext, null, null);
//        FacesConfig facesConfig = fcf.getFacesConfig(externalContext);
//        facesConfig.configureAll(externalContext);
        
//        FacesContextFactory fcf = (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
//        _facesContext = fcf.getFacesContext(_servletContext,
//                                            _httpServletRequest,
//                                            _httpServletResponse,
//                                            _lifecycle);
//
//        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
//        af.setApplication(_application);
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
}

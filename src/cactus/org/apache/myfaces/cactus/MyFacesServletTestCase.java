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

package net.sourceforge.myfaces.cactus;

import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FactoryFinder;
import javax.faces.webapp.FacesServlet;

import org.apache.cactus.ServletTestCase;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class MyFacesServletTestCase extends ServletTestCase
{

    private FacesContext context;


    public MyFacesServletTestCase()
    {
    }


    public MyFacesServletTestCase(String string)
    {
        super(string);
    }


    protected void setUp() throws Exception
    {
        super.setUp();
        context = performFacesContextConfig();
    }


    protected FacesContext getContext()
    {
        return context;
    }


    protected FacesContext performFacesContextConfig()
    {
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());
        FacesContextFactory facesCtxFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        FacesContext ctx = facesCtxFactory.getFacesContext(config.getServletContext(), request, response, lifecycle);
        return ctx;
    }


    protected String getLifecycleId()
    {
        String lifecycleId = this.config.getServletContext().getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);
        return lifecycleId != null ? lifecycleId : LifecycleFactory.DEFAULT_LIFECYCLE;
    }

}

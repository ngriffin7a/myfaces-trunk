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

package net.sourceforge.myfaces.confignew;

import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FactoryFinder;
import javax.faces.el.VariableResolver;
import javax.faces.webapp.FacesServlet;

import org.apache.cactus.ServletTestCase;
import net.sourceforge.myfaces.cactus.TestBean;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class ManagedBeanCactusTest extends ServletTestCase
{

    private FacesContext context;
    private VariableResolver variableResolver;


    protected void setUp() throws Exception
    {
        super.setUp();
        context = performFacesContextConfig();
        variableResolver = context.getApplication().getVariableResolver();

    }


    public void testManagedProperties()
    {
        TestBean testManagedProperties = (TestBean) variableResolver.resolveVariable(context, "testManagedProperties");

        assertNotNull(testManagedProperties);
        assertEquals(5, testManagedProperties.getNumberPrimitiveLong());
        assertEquals(99L, testManagedProperties.getNumberLong().longValue());
        assertNull(testManagedProperties.getValueWithDefault());

        List list = testManagedProperties.getList();

        assertNotNull(list);
        assertEquals(4, list.size());
        assertEquals(29, ((Integer) list.get(0)).intValue());
        assertEquals(99, ((Integer) list.get(1)).intValue());
        assertNull(list.get(2));
        assertEquals(23, ((Integer) list.get(3)).intValue());

        Map map = testManagedProperties.getMap();

        assertNotNull(map);
        assertEquals(4, map.size());
        assertEquals(25, ((Integer) map.get("key1")).intValue());
        assertEquals(29, ((Integer) map.get("test")).intValue());
        assertEquals(99, ((Integer) map.get("test44")).intValue());
        assertNull(map.get("nullValue"));
        assertTrue(map.containsKey("nullValue"));
    }


    public void testManagedList()
    {
        List list = (List) variableResolver.resolveVariable(context, "testList");

        assertNotNull(list);
        assertEquals(4, list.size());
        assertEquals(29, ((Integer) list.get(0)).intValue());
        assertEquals(99, ((Integer) list.get(1)).intValue());
        assertNull(list.get(2));
        assertEquals(23, ((Integer) list.get(3)).intValue());
    }


    public void testManagedMap()
    {
        Map map = (Map) variableResolver.resolveVariable(context, "testMap");

        assertNotNull(map);
        assertEquals(4, map.size());
        assertEquals(25, ((Integer) map.get("key1")).intValue());
        assertEquals(29, ((Integer) map.get("test")).intValue());
        assertEquals(99, ((Integer) map.get("test44")).intValue());
        assertNull(map.get("nullValue"));
        assertTrue(map.containsKey("nullValue"));
    }


    private FacesContext performFacesContextConfig()
    {
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lifecycleFactory.getLifecycle(getLifecycleId());
        FacesContextFactory facesCtxFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        FacesContext ctx = facesCtxFactory.getFacesContext(config.getServletContext(), request, response, lifecycle);
        return ctx;
    }


    private String getLifecycleId()
    {
        String lifecycleId = this.config.getServletContext().getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);
        return lifecycleId != null ? lifecycleId : LifecycleFactory.DEFAULT_LIFECYCLE;
    }

}

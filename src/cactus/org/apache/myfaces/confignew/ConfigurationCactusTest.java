/*
 * Copyright 2002,2004 The Apache Software Foundation.
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
package net.sourceforge.myfaces.confignew;

import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.webapp.FacesServlet;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletContext;

import net.sourceforge.myfaces.application.NavigationHandlerImpl;
import net.sourceforge.myfaces.application.jsp.JspStateManagerImpl;
import net.sourceforge.myfaces.application.jsp.JspViewHandlerImpl;
import net.sourceforge.myfaces.el.PropertyResolverImpl;
import net.sourceforge.myfaces.el.VariableResolverImpl;
import net.sourceforge.myfaces.confignew.element.ManagedBean;
import net.sourceforge.myfaces.cactus.MyFacesServletTestCase;


public class ConfigurationCactusTest extends MyFacesServletTestCase
{

    protected void setUp() throws Exception
    {
        super.setUp();
    }


    protected void tearDown() throws Exception
    {
        super.tearDown();
    }


    public ConfigurationCactusTest(String name)
    {
        super(name);
    }


    public void testConfiguration() throws Exception
    {
        ExternalContext externalContext = getContext().getExternalContext();

        // test decorator pattern support

        // application factory
        ApplicationFactory applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        assertEquals(TestApplicationFactory.class, applicationFactory.getClass());

        javax.faces.application.Application application = applicationFactory.getApplication();

        assertEquals("messageBundleA", application.getMessageBundle());

        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        assertEquals(TestLifecycleFactory.class, lifecycleFactory.getClass());

        // view handler
        ViewHandler viewHandler = application.getViewHandler();

        assertEquals(TestViewHandlerA.class, viewHandler.getClass());
        assertEquals(TestViewHandlerB.class, ((TestViewHandlerA) viewHandler).getDelegate().getClass());
        assertEquals(JspViewHandlerImpl.class, ((TestViewHandlerB) ((TestViewHandlerA) viewHandler).getDelegate()).getDelegate().getClass());

        // navigation handler
        NavigationHandler navigationHandler = application.getNavigationHandler();

        assertEquals(TestNavigationHandler.class, navigationHandler.getClass());
        assertEquals(NavigationHandlerImpl.class, ((TestNavigationHandler) navigationHandler).getDelegate().getClass());

        // state manager
        StateManager stateManager = application.getStateManager();

        assertEquals(TestStateManager.class, stateManager.getClass());
        assertEquals(JspStateManagerImpl.class, ((TestStateManager) stateManager).getDelegate().getClass());

        // PropvertyResolver
        PropertyResolver propertyResolver = application.getPropertyResolver();

        assertEquals(TestPropertyResolver.class, propertyResolver.getClass());
        assertEquals(PropertyResolverImpl.class, ((TestPropertyResolver) propertyResolver).getDelegate().getClass());

        // VariableResolver
        VariableResolver variableResolver = application.getVariableResolver();

        assertEquals(TestVariableResolver.class, variableResolver.getClass());
        assertEquals(VariableResolverImpl.class, ((TestVariableResolver) variableResolver).getDelegate().getClass());


        // RuntimeConfig

        RuntimeConfig runtimeConfig = RuntimeConfig.getCurrentInstance(externalContext);

        ManagedBean bean = runtimeConfig.getManagedBean("testMap");
        assertNotNull(bean);
        assertNull(runtimeConfig.getManagedBean("doesNotExist"));

        // Lifecycle config
        ServletContext context = (ServletContext) externalContext.getContext();
        String lifecycleId = context.getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);

        if (lifecycleId == null)
        {
            lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
        }

        Lifecycle lifecycle = (Lifecycle) lifecycleFactory.getLifecycle(lifecycleId);

        javax.faces.event.PhaseListener[] phaseListeners = lifecycle.getPhaseListeners();

        assertEquals(2, phaseListeners.length);

        for (int i = 0; i < phaseListeners.length; i++)
        {
            PhaseListener listener = phaseListeners[i];

            assertTrue(listener instanceof TestPhaseListenerA || listener instanceof TestPhaseListenerB);
        }
    }


    public void testRenderKit()
    {
        RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit basicHtml = renderKitFactory.getRenderKit(getContext(), RenderKitFactory.HTML_BASIC_RENDER_KIT);

        Renderer renderer = basicHtml.getRenderer("javax.faces.Command", "javax.faces.Button");
        assertEquals(net.sourceforge.myfaces.renderkit.html.HtmlButtonRenderer.class, renderer.getClass());

        RenderKit testRenderKit = renderKitFactory.getRenderKit(getContext(), "TEST_RENDER_KIT");

        assertNull(basicHtml.getRenderer("test.Command", "test.Button"));
        renderer = testRenderKit.getRenderer("test.Command", "test.Button");
        assertEquals(net.sourceforge.myfaces.renderkit.html.HtmlButtonRenderer.class, renderer.getClass());

    }


    public void testNavigationRules()
    {
        // TODO:
    }
}

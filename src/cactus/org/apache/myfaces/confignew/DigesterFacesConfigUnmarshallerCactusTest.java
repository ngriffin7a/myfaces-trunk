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

import java.io.InputStream;

import net.sourceforge.myfaces.confignew.impl.digester.DigesterFacesConfigUnmarshallerImpl;
import net.sourceforge.myfaces.confignew.impl.digester.elements.Application;
import net.sourceforge.myfaces.confignew.impl.digester.elements.Factory;
import net.sourceforge.myfaces.confignew.impl.digester.elements.LocaleConfig;
import org.apache.cactus.ServletTestCase;



public class DigesterFacesConfigUnmarshallerCactusTest extends ServletTestCase
{

    protected void setUp() throws Exception
    {
        super.setUp();
    }


    protected void tearDown() throws Exception
    {
        super.tearDown();
    }


    public DigesterFacesConfigUnmarshallerCactusTest(String name)
    {
        super(name);
    }


    public void testParse() throws Exception
    {
        DigesterFacesConfigUnmarshallerImpl parser = new DigesterFacesConfigUnmarshallerImpl();
        InputStream in = getClass().getClassLoader().getResourceAsStream("net/sourceforge/myfaces/confignew/faces-config1.xml");

        net.sourceforge.myfaces.confignew.impl.digester.elements.FacesConfig config = (net.sourceforge.myfaces.confignew.impl.digester.elements.FacesConfig) parser.getFacesConfig(in, "");

        assertEquals(1, config.getApplications().size());

        Application application = (Application) config.getApplications().get(0);

        assertEquals(1, application.getActionListener().size());
        assertEquals("net.sourceforge.myfaces.application.ActionListenerImpl", application.getActionListener().get(0));
        assertEquals(0, application.getDefaultRenderkitId().size());
        assertEquals(0, application.getMessageBundle().size());
        assertEquals(1, application.getNavigationHandler().size());
        assertEquals("net.sourceforge.myfaces.application.NavigationHandlerImpl", application.getNavigationHandler().get(0));
        assertEquals(1, application.getPropertyResolver().size());
        assertEquals("net.sourceforge.myfaces.el.PropertyResolverImpl", application.getPropertyResolver().get(0));
        assertEquals(0, application.getStateManager().size());
        assertEquals(1, application.getVariableResolver().size());
        assertEquals("net.sourceforge.myfaces.el.VariableResolverImpl", application.getVariableResolver().get(0));
        assertEquals(0, application.getViewHandler().size());
        assertEquals(1, application.getLocaleConfig().size());

        LocaleConfig locale = (LocaleConfig) application.getLocaleConfig().get(0);
        assertEquals("en", locale.getDefaultLocale());
        assertTrue(locale.getSupportedLocales().contains("en"));
        assertTrue(locale.getSupportedLocales().contains("de"));
        assertTrue(locale.getSupportedLocales().contains("fr"));

        assertEquals(1, config.getFactories().size());

        Factory factory = (Factory) config.getFactories().get(0);
        assertEquals(1, factory.getApplicationFactory().size());
        assertEquals("net.sourceforge.myfaces.application.ApplicationFactoryImpl", factory.getApplicationFactory().get(0));
        assertEquals(1, factory.getFacesContextFactory().size());
        assertEquals("net.sourceforge.myfaces.context.FacesContextFactoryImpl", factory.getFacesContextFactory().get(0));
        assertEquals(1, factory.getLifecycleFactory().size());
        assertEquals("net.sourceforge.myfaces.lifecycle.LifecycleFactoryImpl", factory.getLifecycleFactory().get(0));
        assertEquals(1, factory.getRenderkitFactory().size());
        assertEquals("net.sourceforge.myfaces.renderkit.RenderKitFactoryImpl", factory.getRenderkitFactory().get(0));

        assertEquals(42, config.getComponents().size());

        String componentClass = (String) config.getComponents().get("javax.faces.HtmlCommandButton");

        assertEquals("javax.faces.component.html.HtmlCommandButton", componentClass);

        componentClass = (String) config.getComponents().get("javax.faces.HtmlCommandLink");
        assertEquals("javax.faces.component.html.HtmlCommandLink", componentClass);

        assertEquals(28, config.getConverters().size());
        assertEquals(3, config.getValidators().size());
        assertEquals(1, config.getRenderKits().size());
    }

}

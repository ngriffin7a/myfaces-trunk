/*
 * Created on Apr 2, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.myfaces.confignew;

import java.io.InputStream;
import java.util.List;

import org.apache.cactus.ServletTestCase;
import net.sourceforge.myfaces.confignew.impl.digester.DigesterFacesConfigUnmarshallerImpl;
import net.sourceforge.myfaces.confignew.impl.digester.elements.*;



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

        Component component = (Component) config.getComponents().get(0);

        assertEquals("javax.faces.HtmlCommandButton", component.getComponentType());
        assertEquals("javax.faces.component.html.HtmlCommandButton", component.getComponentClass());
        assertEquals(0, component.getAttribute().size());
        assertEquals(0, component.getProperty().size());
        assertEquals(0, component.getComponentExtension().size());

        component = (Component) config.getComponents().get(1);
        assertEquals("javax.faces.HtmlCommandLink", component.getComponentType());
        assertEquals("javax.faces.component.html.HtmlCommandLink", component.getComponentClass());
        assertEquals(2, component.getAttribute().size());
        assertEquals("attribute1", ((Attribute)component.getAttribute().get(0)).getAttributeName());
        assertEquals("attribute2", ((Attribute)component.getAttribute().get(1)).getAttributeName());
        assertEquals(1, component.getProperty().size());
        assertEquals(2, component.getComponentExtension().size());
        assertTrue(component.getComponentExtension().contains("extensionx"));
        assertTrue(component.getComponentExtension().contains("extensiony"));
        assertTrue(!component.getComponentExtension().contains("extensionz"));

        assertEquals(28, config.getConverters().size());
        assertEquals(3, config.getValidators().size());
        assertEquals(1, config.getRenderKits().size());
    }

}

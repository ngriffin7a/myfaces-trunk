/*
 * Created on Apr 2, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.myfaces.config;

import javax.faces.FactoryFinder;

import org.apache.cactus.ServletTestCase;

public class FacesConfigFactoryBaseCactusTest extends ServletTestCase {
  private static final String APP_FAC_NAME="";

  public static void main(String[] args) {
    junit.textui.TestRunner.run(FacesConfigFactoryBaseCactusTest.class);
  }

  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public FacesConfigFactoryBaseCactusTest(String name) {
    super(name);
  }

  public void testMetaInfLookup() throws Exception {
    assertEquals("net.sourceforge.myfaces.confignew.TestApplicationFactory",
        FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY).getClass().getName());
    assertEquals("net.sourceforge.myfaces.renderkit.RenderKitFactoryImpl",
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY).getClass().getName());
    assertEquals("net.sourceforge.myfaces.context.FacesContextFactoryImpl",
        FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY).getClass().getName());
    assertEquals("net.sourceforge.myfaces.confignew.TestLifecycleFactory",
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY).getClass().getName());
  }

}

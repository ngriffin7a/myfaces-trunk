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

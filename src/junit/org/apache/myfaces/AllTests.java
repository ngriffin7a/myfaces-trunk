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

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.myfaces.application.ApplicationTest;
import net.sourceforge.myfaces.context.servlet.ApplicationMapTest;
import net.sourceforge.myfaces.context.servlet.CookieMapTest;
import net.sourceforge.myfaces.el.MethodBindingTest;
import net.sourceforge.myfaces.el.SetValueBindingTest;
import net.sourceforge.myfaces.el.ValueBindingTest;


public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for net.sourceforge.myfaces.application");
        
        //$JUnit-BEGIN$
        suite.addTestSuite(ApplicationTest.class);
        suite.addTestSuite(ValueBindingTest.class);
        suite.addTestSuite(SetValueBindingTest.class);
        suite.addTestSuite(MethodBindingTest.class);
        suite.addTestSuite(ApplicationMapTest.class);
        suite.addTestSuite(CookieMapTest.class);
        //$JUnit-END$
        
        return suite;
    }
}

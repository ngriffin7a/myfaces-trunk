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
package org.apache.myfaces;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.myfaces.application.ApplicationTest;
import org.apache.myfaces.context.servlet.ApplicationMapTest;
import org.apache.myfaces.context.servlet.CookieMapTest;
import org.apache.myfaces.el.MethodBindingTest;
import org.apache.myfaces.el.SetValueBindingTest;
import org.apache.myfaces.el.ValueBindingTest;


public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for org.apache.myfaces.application");
        
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

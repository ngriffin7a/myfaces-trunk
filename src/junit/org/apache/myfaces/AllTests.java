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

package net.sourceforge.myfaces;

import net.sourceforge.myfaces.application.ApplicationTest;
import net.sourceforge.myfaces.el.ValueBindingTest;
import junit.framework.Test;
import junit.framework.TestSuite;


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
        //$JUnit-END$
        return suite;
    }
}

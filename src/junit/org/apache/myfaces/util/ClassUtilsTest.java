package net.sourceforge.myfaces.util;

import junit.framework.TestCase;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ClassUtilsTest
        extends TestCase
{
    //private static final Log log = LogFactory.getLog(ClassUtilsTest.class);

    public void testClassForName()
        throws ClassNotFoundException
    {
        assertEquals(ClassUtils.classForName("java.lang.String"), String.class);

        try
        {
            ClassUtils.classForName("x.y.NotFound");
            assertTrue("No ClassNotFoundException?", false);
        }
        catch (ClassNotFoundException e)
        {}

        try
        {
            ClassUtils.classForName("java.lang.String[]");
            assertTrue("No ClassNotFoundException?", false);
        }
        catch (ClassNotFoundException e)
        {}

        try
        {
            ClassUtils.classForName("int");
            assertTrue("No ClassNotFoundException?", false);
        }
        catch (ClassNotFoundException e)
        {}
    }


    public void testJavaTypeToClass()
        throws ClassNotFoundException
    {
        assertEquals(ClassUtils.javaTypeToClass("java.lang.String"), String.class);

        try
        {
            ClassUtils.javaTypeToClass("x.y.NotFound");
            assertTrue("No ClassNotFoundException?", false);
        }
        catch (ClassNotFoundException e)
        {}

        assertEquals(ClassUtils.javaTypeToClass("java.lang.String[]"), (new String[0]).getClass());
        assertEquals(ClassUtils.javaTypeToClass("int"), Integer.TYPE);
        assertEquals(ClassUtils.javaTypeToClass("int[]"), (new int[0]).getClass());

        try
        {
            ClassUtils.javaTypeToClass("int[][]");
            assertTrue("No ClassNotFoundException?", false);
        }
        catch (ClassNotFoundException e)
        {}
    }


}

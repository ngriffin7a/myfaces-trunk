package net.sourceforge.myfaces.util;

import junit.framework.TestCase;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/10/05 22:34:20  dave0000
 * bug 1021656 with related improvements
 *
 */
public class ClassUtilsTest
        extends TestCase
{
    public void testClassForName()
        throws ClassNotFoundException
    {
        assertEquals(ClassUtils.classForName("java.lang.String"), String.class);

        try
        {
            ClassUtils.classForName("x.y.NotFound");
            assertTrue("ClassNotFoundException expected", false);
        }
        catch (ClassNotFoundException e)
        {
            // ignore, must trow this exception
        }

        try
        {
            ClassUtils.classForName("java.lang.String[]");
            assertTrue("ClassNotFoundException expected", false);
        }
        catch (ClassNotFoundException e)
        {
            // ignore, must trow this exception
        }

        try
        {
            ClassUtils.classForName("int");
            assertTrue("ClassNotFoundException expected", false);
        }
        catch (ClassNotFoundException e)
        {
            // ignore, must trow this exception
        }
    }


    public void testJavaTypeToClass()
        throws ClassNotFoundException
    {
        assertEquals(ClassUtils.javaTypeToClass("java.lang.String"), String.class);

        try
        {
            ClassUtils.javaTypeToClass("x.y.NotFound");
            assertTrue("ClassNotFoundException expected", false);
        }
        catch (ClassNotFoundException e)
        {
            // ignore, must trow this exception
        }

        assertEquals(ClassUtils.javaTypeToClass("java.lang.String[]"), (new String[0]).getClass());
        assertEquals(ClassUtils.javaTypeToClass("int"), Integer.TYPE);
        assertEquals(ClassUtils.javaTypeToClass("int[]"), (new int[0]).getClass());

        try
        {
            ClassUtils.javaTypeToClass("int[][]");
            assertTrue("ClassNotFoundException expected", false);
        }
        catch (ClassNotFoundException e)
        {
            // ignore, must trow this exception
        }
    }
}

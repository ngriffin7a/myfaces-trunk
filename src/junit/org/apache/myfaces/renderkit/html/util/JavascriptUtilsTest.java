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

package net.sourceforge.myfaces.renderkit.html.util;

import junit.framework.TestCase;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/04/29 14:25:20  manolito
 * javascript function name bugfix
 *
 */
public class JavascriptUtilsTest
        extends TestCase
{
    //private static final Log log = LogFactory.getLog(JavascriptUtilsTest.class);

    public JavascriptUtilsTest(String s)
    {
        super(s);
    }

    public void testGetValidJavascriptName()
    {
        String s, r;

        s = "x";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("x", r);

        s = "x1";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("x1", r);

        s = "x:y";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("x_3Ay", r);

        s = "x-y";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("x_2Dy", r);

        s = "x_y";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("x_5Fy", r);

        s = "x-_";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("x_2D_5F", r);

        s = "a:b:c:d";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("a_3Ab_3Ac_3Ad", r);

        s = "x-1:y-2";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("x_2D1_3Ay_2D2", r);

        s = "x\r\n";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("x_0D_0A", r);

        s = "x\u2297";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("x_E28A97", r);

        s = "if";
        r = JavascriptUtils.getValidJavascriptName(s, false);
        System.out.println(s + " --> " + r);
        assertEquals("if", r);

        s = "if";
        r = JavascriptUtils.getValidJavascriptName(s, true);
        System.out.println(s + " --> " + r);
        assertEquals("if_", r);

    }

}

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
package net.sourceforge.myfaces.renderkit.html.util;

import junit.framework.TestCase;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/07/01 22:01:09  mwessendorf
 * ASF switch
 *
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

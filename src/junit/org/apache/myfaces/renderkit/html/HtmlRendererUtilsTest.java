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
package org.apache.myfaces.renderkit.html;

import junit.framework.TestCase;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/10/13 11:50:59  matze
 * renamed packages to org.apache
 *
 * Revision 1.2  2004/07/01 22:01:37  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/04/29 14:25:21  manolito
 * javascript function name bugfix
 *
 */
public class HtmlRendererUtilsTest
        extends TestCase
{
    //private static final Log log = LogFactory.getLog(HtmlRendererUtilsTest.class);

    public HtmlRendererUtilsTest(String s)
    {
        super(s);
    }

    public void testGetClearHiddenCommandFormParamsFunctionName()
    {
        String s, r;

        //standard cases
        s = "form1";
        r = HtmlRendererUtils.getClearHiddenCommandFormParamsFunctionName(s);
        System.out.println(s + " --> " + r);
        assertEquals("clear_form1", r);

        s = "container:form1";
        r = HtmlRendererUtils.getClearHiddenCommandFormParamsFunctionName(s);
        System.out.println(s + " --> " + r);
        assertEquals("clear_container_3Aform1", r);
    }

}

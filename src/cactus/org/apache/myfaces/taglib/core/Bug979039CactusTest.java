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
package org.apache.myfaces.taglib.core;

import javax.servlet.RequestDispatcher;

import org.apache.cactus.ServletTestCase;

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/10/13 11:50:56  matze
 * renamed packages to org.apache
 *
 * Revision 1.2  2004/07/01 21:57:59  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/06/26 00:34:49  o_rossmueller
 * fix #979039: default type = number for convertNumber
 *
 * Revision 1.1  2004/05/26 17:19:57  o_rossmueller
 * test for bug 948626
 *
 * Revision 1.1  2004/05/04 06:36:20  manolito
 * Bugfix #947302
 *
 */
public class Bug979039CactusTest
        extends ServletTestCase
{
    public Bug979039CactusTest(String name) {
        super(name);
    }

    public void testBug() throws Exception
    {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher(
                "/Bug979039CactusTest.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }

    public void endBug948626(WebResponse response)
            throws Exception
    {
        assertEquals(-1, response.getText().indexOf("Cannot get NumberFormat, either type or pattern needed."));
        WebForm form = response.getFormWithID("testForm");
        assertEquals("0", form.getParameterValue("testForm:input"));
    }
}

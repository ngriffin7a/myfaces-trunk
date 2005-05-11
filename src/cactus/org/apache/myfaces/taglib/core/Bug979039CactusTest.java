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

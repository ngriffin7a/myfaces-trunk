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
package org.apache.myfaces.renderkit.html;

import javax.servlet.RequestDispatcher;

import org.apache.cactus.ServletTestCase;

import com.meterware.httpunit.WebResponse;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class Bug995085CactusTest extends ServletTestCase
{

    public Bug995085CactusTest(String string)
    {
        super(string);
    }


    public void testBug() throws Exception
    {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher("/Bug995085CactusTest.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }


    public void endBug(WebResponse response)
        throws Exception
    {
        assertTrue(response.getText().indexOf("logo_value.jpg") != -1);
        assertTrue(response.getText().indexOf("logo_url.jpg") != -1);
    }
}

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
package net.sourceforge.myfaces.renderkit.html;

import javax.servlet.RequestDispatcher;

import org.apache.cactus.ServletTestCase;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.SubmitButton;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class Bug972165CactusTest extends ServletTestCase
{

    public Bug972165CactusTest(String string)
    {
        super(string);
    }


    public void testBug972165() throws Exception
    {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher("/Bug972165CactusTest.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }


    public void endBug972165(WebResponse response)
        throws Exception
    {
        WebConversation conversation = new WebConversation();
        response = conversation.getResponse(response.getURL().toExternalForm());
        WebForm form = response.getFormWithID("testForm");
        SubmitButton submitButton = form.getSubmitButtonWithID("testForm:submit");

        assertTrue(response.getText().indexOf("CheckDisabled:true") != -1);
        assertEquals("true", form.getParameterValue("testForm:checkDisabled"));
        response = form.submit(submitButton);
        assertTrue(response.getText().indexOf("CheckDisabled:true") != -1);
        assertEquals("true", form.getParameterValue("testForm:checkDisabled"));
    }
}

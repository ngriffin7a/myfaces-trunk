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

import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/07/01 21:57:56  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/05/26 17:19:57  o_rossmueller
 * test for bug 948626
 *
 * Revision 1.1  2004/05/04 06:36:20  manolito
 * Bugfix #947302
 *
 */
public class HtmlInputHiddenCactusTest
        extends ServletTestCase
{
    //private static final Log log = LogFactory.getLog(HtmlInputHiddenTagCactusTest.class);

    public HtmlInputHiddenCactusTest(String name) {
        super(name);
    }

    public void testBug948626() throws Exception
    {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher(
                "/HtmlInputHiddenTagCactusTest.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }

    public void endBug948626(WebResponse response)
            throws Exception
    {
        WebConversation conversation = new WebConversation();
        response = conversation.getResponse(response.getURL().toExternalForm());
        WebForm form = response.getFormWithID("testForm");
        SubmitButton submitButton = form.getSubmitButtonWithID("testForm:submit");
        assertEquals("0", form.getParameterValue("testForm:hidden"));
        assertTrue(response.getText().indexOf("false") != -1);
        assertTrue(response.getText().indexOf("true") == -1);
        response = form.submit(submitButton);
        assertTrue(response.getText().indexOf("false") == -1);
        assertTrue(response.getText().indexOf("true") != -1);
    }
}

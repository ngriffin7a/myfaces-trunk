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

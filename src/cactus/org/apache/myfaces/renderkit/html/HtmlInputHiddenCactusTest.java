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

import com.meterware.httpunit.*;

import org.apache.cactus.ServletTestCase;

import javax.servlet.RequestDispatcher;

import net.sourceforge.myfaces.cactus.CommonModelBean;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
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

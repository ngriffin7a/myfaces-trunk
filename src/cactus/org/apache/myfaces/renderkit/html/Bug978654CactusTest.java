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
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/06/28 22:12:11  o_rossmueller
 * fix #978654: do not coerce null
 *
 * Revision 1.1  2004/05/26 17:19:57  o_rossmueller
 * test for bug 948626
 *
 * Revision 1.1  2004/05/04 06:36:20  manolito
 * Bugfix #947302
 *
 */
public class Bug978654CactusTest
        extends ServletTestCase
{
    public Bug978654CactusTest(String name) {
        super(name);
    }

    public void testBug() throws Exception
    {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher(
                "/Bug978654CactusTest.jsf");
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
        assertEquals("", form.getParameterValue("testForm:hidden"));
        response = form.submit(submitButton);
        form = response.getFormWithID("testForm");
        assertEquals("", form.getParameterValue("testForm:hidden"));
    }
}

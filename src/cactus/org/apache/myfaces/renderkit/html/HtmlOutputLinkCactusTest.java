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

import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

import org.apache.cactus.ServletTestCase;

import javax.servlet.RequestDispatcher;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/05/04 06:36:20  manolito
 * Bugfix #947302
 *
 */
public class HtmlOutputLinkCactusTest
        extends ServletTestCase
{
    //private static final Log log = LogFactory.getLog(HtmlInputHiddenTagCactusTest.class);

    public HtmlOutputLinkCactusTest(String name) {
        super(name);
    }

    public void testSimpleRender() throws Exception
    {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher(
                "/HtmlOutputLinkCactusTest.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }

    public void endSimpleRender(WebResponse response)
            throws Exception
    {
        WebLink link;
        link = response.getLinkWithID("outputLink1");
        assertNotNull(link);
        assertEquals(link.getURLString(), "http://www.myfaces.org/?p1=v1&p2=v2&p3=v3");

        link = response.getLinkWithID("outputLink2");
        assertNotNull(link);
        assertEquals(link.getURLString(), "http://www.myfaces.org/?p1=v1&p2=v2&p3=v3");
    }
}

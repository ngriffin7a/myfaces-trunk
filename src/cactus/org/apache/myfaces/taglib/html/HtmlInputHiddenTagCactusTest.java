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

package net.sourceforge.myfaces.taglib.html;

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.cactus.ServletTestCase;

import javax.servlet.RequestDispatcher;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/04/23 15:12:14  manolito
 * inputHidden bug reported by Travis
 *
 */
public class HtmlInputHiddenTagCactusTest
        extends ServletTestCase
{
    //private static final Log log = LogFactory.getLog(HtmlInputHiddenTagCactusTest.class);

    public HtmlInputHiddenTagCactusTest(String name) {
        super(name);
    }

    public void testSimpleRender() throws Exception {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher(
                "/HtmlInputHiddenTagCactusTest.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }

    public void endSimpleRender(WebResponse response)
            throws Exception
    {
        WebForm form = response.getFormWithID("testForm");
        Node node = form.getDOMSubtree();
        NodeList lst = node.getChildNodes();
        for (int i = 0, len = lst.getLength(); i < len; i++)
        {
            Node child = lst.item(i);
            System.out.println(child.getNodeName());
        }
    }
}

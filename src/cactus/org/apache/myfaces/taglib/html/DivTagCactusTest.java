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
package org.apache.myfaces.taglib.html;

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.cactus.ServletTestCase;

import javax.servlet.RequestDispatcher;

/**
 * @author Bill Dudney (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/11/08 13:33:50  bdudney
 * adding a cactus test for the new div element
 *
 */
public class DivTagCactusTest
        extends ServletTestCase
{

    public DivTagCactusTest(String name) {
        super(name);
    }

    public void testSimpleRender() throws Exception {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher(
                "/DivTagCactusTest.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }

    public void endSimpleRender(WebResponse response)
            throws Exception
    {
        WebForm form = response.getFormWithID("testForm");
        Node node = form.getDOMSubtree();
        NodeList lst = node.getChildNodes();
        assertTrue(2 <= lst.getLength());
        Node div = lst.item(0);
        assertEquals("bar", div.getAttributes().getNamedItem("style").getNodeValue());
        div = lst.item(1);
        assertEquals("foo", div.getAttributes().getNamedItem("class").getNodeValue());
    }
}

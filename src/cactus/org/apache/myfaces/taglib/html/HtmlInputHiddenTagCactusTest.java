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
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/10/13 11:50:56  matze
 * renamed packages to org.apache
 *
 * Revision 1.2  2004/07/01 21:57:58  mwessendorf
 * ASF switch
 *
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

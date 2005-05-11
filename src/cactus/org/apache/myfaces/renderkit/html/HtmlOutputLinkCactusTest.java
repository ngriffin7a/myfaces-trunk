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

import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

import org.apache.cactus.ServletTestCase;

import javax.servlet.RequestDispatcher;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
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

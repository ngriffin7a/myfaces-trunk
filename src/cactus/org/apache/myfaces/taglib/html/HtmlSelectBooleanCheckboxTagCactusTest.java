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
package net.sourceforge.myfaces.taglib.html;

import com.meterware.httpunit.HTMLElement;
import com.meterware.httpunit.WebResponse;

import org.apache.cactus.ServletTestCase;

import javax.servlet.RequestDispatcher;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/07/01 21:57:58  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/04/23 15:12:14  manolito
 * inputHidden bug reported by Travis
 *
 * Revision 1.2  2004/04/23 13:57:54  manolito
 * bug #940740
 *
 * Revision 1.1  2004/04/23 12:09:02  manolito
 * cactus problems
 *
 */
public class HtmlSelectBooleanCheckboxTagCactusTest
        extends ServletTestCase
{
    //private static final Log log = LogFactory.getLog(HtmlSelectBooleanCheckboxTagCactusTest.class);

    public HtmlSelectBooleanCheckboxTagCactusTest(String name) {
        super(name);
    }

    public void testSimpleRender() throws Exception {
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher(
                "/HtmlSelectBooleanCheckboxTagCactusTest.jsf");
        // render the page for the first time
        rd.forward(request, response);
    }

    public void endSimpleRender(WebResponse response)
            throws Exception
    {
        HTMLElement element = response.getElementWithID("testForm:selectBooleanCheckbox1");
        assertNotNull(element);
    }
}

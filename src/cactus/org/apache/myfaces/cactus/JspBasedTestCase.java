/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.myfaces.cactus;

import javax.servlet.RequestDispatcher;

import org.apache.cactus.ServletTestCase;


/**
 * This is a convenient base class for cactus tests based on a JSP. The jsp filename is extracted from the
 * class name.
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class JspBasedTestCase extends ServletTestCase
{

    public JspBasedTestCase(String string)
    {
        super(string);
    }


    public void testBug() throws Exception
    {
        String className = getClass().getName();
        int index = className.lastIndexOf('.');
        String jspName = className.substring(index + 1);
        RequestDispatcher rd = config.getServletContext().getRequestDispatcher("/" + jspName + ".jsf");
        // render the page for the first time
        rd.forward(request, response);
    }
}

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
package org.apache.myfaces.webapp;

import javax.servlet.*;
import java.io.IOException;

/**
 * Used by JspViewHandlerImplTest to test servlet mappings.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DummyServlet
        implements Servlet
{
    //private static final Log log = LogFactory.getLog(DummyServlet.class);

    private ServletConfig _servletConfig;

    public String getServletInfo()
    {
        return "Dummy servlet for testing servlet mappings";
    }

    public void init(ServletConfig servletConfig) throws ServletException
    {
        _servletConfig = servletConfig;
    }

    public ServletConfig getServletConfig()
    {
        return _servletConfig;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException,
                                                                                               IOException
    {
        //do nothing
    }

    public void destroy()
    {
    }
}

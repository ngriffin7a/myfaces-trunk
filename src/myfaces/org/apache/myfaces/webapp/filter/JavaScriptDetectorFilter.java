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
package org.apache.myfaces.webapp.filter;

import org.apache.myfaces.context.servlet.ServletExternalContextImpl;
import org.apache.myfaces.renderkit.html.util.JavascriptUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 *
 * Filter to handle javascript detection redirect. This is an EXPERIMENTAL feature.
 *
 * @author Oliver Rossmueller (latest modification by $Author$)
 *
 * $Log$
 * Revision 1.4  2004/10/13 11:51:01  matze
 * renamed packages to org.apache
 *
 * Revision 1.3  2004/09/08 09:31:25  manolito
 * moved isJavascriptDetected from MyFacesConfig to JavascriptUtils class
 *
 * Revision 1.2  2004/09/01 18:32:57  mwessendorf
 * Organize Imports
 *
 * Revision 1.1  2004/08/05 22:10:44  o_rossmueller
 * EXPERIMENTAL: JavaScript detection
 *
 */
public class JavaScriptDetectorFilter implements Filter
{
    private static final Log log = LogFactory.getLog(JavaScriptDetectorFilter.class);

    private ServletContext _servletContext;

    public void init(FilterConfig filterConfig) throws ServletException
    {
        _servletContext = filterConfig.getServletContext();
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        ExternalContext externalContext = new ServletExternalContextImpl(_servletContext,
                                                                         servletRequest,
                                                                         servletResponse);
        JavascriptUtils.setJavascriptDetected(externalContext, true); // mark the session to use javascript

        log.info("Enabled JavaScript for session - redirect to" + request.getParameter("goto"));
        response.sendRedirect(request.getParameter("goto"));
    }


    public void destroy()
    {

    }
}

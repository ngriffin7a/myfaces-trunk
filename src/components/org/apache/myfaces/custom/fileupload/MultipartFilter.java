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
package org.apache.myfaces.custom.fileupload;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MultipartFilter
    implements Filter
{

    private int maxFileSize = 10 * 1024 * 1024; // 10 MB


    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig)
    {
        String param = filterConfig.getInitParameter("maxFileSize");

        if (param != null)
        {
            param = param.toLowerCase();
            int factor = 1;
            String number = param;

            if (param.endsWith("m"))
            {
                factor = 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            } else if (param.endsWith("k"))
            {
                factor = 1024;
                number = param.substring(0, param.length() - 1);
            }

            maxFileSize = Integer.parseInt(number) * factor;
        }
    }


    /**
     * Add the expires Header
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        if (!(response instanceof HttpServletResponse))
        {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String type = httpRequest.getHeader("Content-Type");

        // Process only multipart/form-data requests
        if (type != null)
            if (type.startsWith("multipart/form-data"))
            {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                MultipartRequestWrapper requestWrapper = new MultipartRequestWrapper(httpRequest, maxFileSize);
                chain.doFilter(requestWrapper, response);
                return;
            }

        // Standard request
        chain.doFilter(request, response);
    }


    /**
     * Destroy method for this filter
     */
    public void destroy()
    {
    }
}

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

import org.apache.commons.fileupload.FileUpload;

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

    private int maxFileSize = 100 * 1024 * 1024; // 10 MB
    private int thresholdSize = 1 * 1024 * 1024; // 1 MB
    private String repositoryPath = null; //standard temp directory


    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig)
    {

        String param = filterConfig.getInitParameter("maxFileSize");

        maxFileSize = resolveSize(param, maxFileSize);

        param = filterConfig.getInitParameter("thresholdSize");

        thresholdSize = resolveSize(param, thresholdSize);

        repositoryPath = filterConfig.getInitParameter("repositoryPath");
    }

    private int resolveSize(String param, int defaultValue)
    {
        int numberParam = defaultValue;

        if (param != null)
        {
            param = param.toLowerCase();
            int factor = 1;
            String number = param;

            if (param.endsWith("g"))
            {
                factor = 1024 * 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            } else if (param.endsWith("m"))
            {
                factor = 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            } else if (param.endsWith("k"))
            {
                factor = 1024;
                number = param.substring(0, param.length() - 1);
            }

            numberParam = Integer.parseInt(number) * factor;
        }
        return numberParam;
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

        // Process only multipart/form-data requests
        if(FileUpload.isMultipartContent(httpRequest))
        {
            MultipartRequestWrapper requestWrapper = new MultipartRequestWrapper(
                    httpRequest, maxFileSize,thresholdSize,repositoryPath);
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

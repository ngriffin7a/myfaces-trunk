/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.webapp.filter;

import java.io.IOException;
import java.io.File;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Due to the manner in which the JSP / servlet lifecycle
 * functions, it is not currently possible to specify default
 * welcome files for a web application and map them to the
 * MyFacesServlet.  Normally they will be mapped to the
 * default servlet for the JSP container.  To offset this
 * shortcoming, we utilize a servlet Filter which examines
 * the URI of all incoming requests.
 *
 * @author lebowitz
 * @version $Revision$ $Date${DATE} ${TIME} $
 */
public class MyFacesFilter implements Filter
{
    private FilterConfig config;
    private ServletContext context;
    private String[] welcomeFiles = new String[0];
    private StringBuffer sb = new StringBuffer();

    public MyFacesFilter()
    {
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
        config = null;
        context = null;
        welcomeFiles = null;
        sb = null;
    }

    /**
     *
     * If the URI indicates a context, or a subdirectory within a particular
     * context, but does not specify a filename, the request is redirected to
     * one of the default welcome files, assuming they are provided in the web.xml file.
     * If no welcome files are specified, or if none of the welcome files
     * actually exists, then the request is redirected to a file named "index.jsp" for
     * that context or subdirectory with the current context.  If the index.jsp file
     * does not exist, the servlet will return a File Not Found Error 404 message.
     *
     * A well configured servlet should provide a means of handling this type of
     * error, along with a link to an appropriate help page.
     *
     * A URI is thought to represent a context and/or subdirectory(s) if
     * it lacks a suffix following the pattern <b>.<suffix></b>.
     *
     */
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain)
        throws IOException, ServletException
    {
        if (config == null)
        {
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String uri = httpRequest.getRequestURI();
        //		context.log("URI is " + uri);
        // if the uri does not contain a suffix, we consider
        // it to represent a directory / context, not a file.
        // file has suffix.  No need to search for welcome file
        if (uri.lastIndexOf('.') > uri.lastIndexOf('/'))
        {
            chain.doFilter(request, response);
            return;
        }
        String contextPath = httpRequest.getContextPath();
        String welcomeFile = null;
        sb.setLength(0);
        sb.append(uri);
        if (!uri.endsWith("/"))
        {
            sb.append('/');
        }
        String baseURI = sb.delete(0, contextPath.length()).toString();
        for (int i = 0; i < welcomeFiles.length; i++)
        {
            sb.setLength(0);
            sb.append(baseURI).append(welcomeFiles[i]);
            File file = new File(context.getRealPath(sb.toString()));
            //			context.log("Welcome File: " + file.getAbsolutePath());
            if (file.exists())
            {
                if (welcomeFiles[i].endsWith(".jsp"))
                {
                    // alter the name of the file we are requesting to
                    // force it through the MyFacesServlet
                    sb.replace(sb.lastIndexOf(".jsp"), sb.length(), ".jsf");
                    welcomeFile = sb.toString();
                }
                // we have discovered a filename;
                // stop the loop
                break;
            }
        }
        if (welcomeFile == null)
        {
            sb.setLength(0);
            sb.append(baseURI);
            sb.append("index.jsf");
            welcomeFile = sb.toString();
        }
        RequestDispatcher rd = httpRequest.getRequestDispatcher(welcomeFile);
        rd.forward(request, response);
        return;
    }

    /**
     * During the init method, we have to get any predefined welcome files
     * for the current ServletContext.
     * @throws javax.servlet.ServletException
     * @param config
     */
    public void init(FilterConfig config) throws ServletException
    {
        if (config == null)
        {
            return;
        }
        this.config = config;
        this.context = config.getServletContext();
        welcomeFiles = WelcomeFileFactory.getWelcomeFiles(context);
    }
}

/*
 * MyFaces - the free JSF implementation Copyright (C) 2003, 2004 The MyFaces
 * Team (http://myfaces.sourceforge.net)
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.custom.fileupload2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/04/13 10:42:04  manolito
 * introduced commons codecs and fileupload
 *
 */
public class MultipartFilter
        implements Filter
{
    /**
	 * Init method for this filter
	 */
    public void init(FilterConfig filterConfig) {
    }

	/**
	 * Add the expires Header
	 */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	if(! (response instanceof HttpServletResponse) ){
    		chain.doFilter(request, response);
    		return;
    	}
    	
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
    	String type = httpRequest.getHeader("Content-Type");
    	
    	// If this is not a multipart/form-data request continue
    	if (type != null )
	    	if( !type.startsWith("multipart/form-data") ) {
	    		chain.doFilter(request, response);
	    		return;
	    	}
	    	
		//HttpServletResponse httpResponse = (HttpServletResponse) response;
    	
    	// Ok, multipart request.
    	MultipartRequestWrapper requestWrapper = new MultipartRequestWrapper( httpRequest );
    	chain.doFilter(requestWrapper, response);
    }

    /**
	 * Destroy method for this filter
	 */
    public void destroy() {
    }
}


/*
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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
package javax.faces.context;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * DOCUMENT ME!
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class ExternalContext
{
    public static final String BASIC_AUTH = "BASIC";
    public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";
    public static final String DIGEST_AUTH = "DIGEST";
    public static final String FORM_AUTH = "FORM";

    public abstract void dispatch(String path)
            throws java.io.IOException;

    public abstract String encodeActionURL(String url);

    public abstract String encodeNamespace(String name);

    public abstract String encodeResourceURL(String url);

    public abstract Map getApplicationMap();

    public abstract String getAuthType();

    public abstract Object getContext();

    public abstract String getInitParameter(String name);

    public abstract Map getInitParameterMap();

    public abstract String getRemoteUser();

    public abstract Object getRequest();

    public abstract String getRequestContextPath();

    public abstract Map getRequestCookieMap();

    public abstract Map getRequestHeaderMap();

    public abstract Map getRequestHeaderValuesMap();

    public abstract Locale getRequestLocale();

    public abstract Iterator getRequestLocales();

    public abstract Map getRequestMap();

    public abstract Map getRequestParameterMap();

    public abstract Iterator getRequestParameterNames();

    public abstract Map getRequestParameterValuesMap();

    public abstract String getRequestPathInfo();

    public abstract String getRequestServletPath();

    public abstract java.net.URL getResource(String path)
            throws java.net.MalformedURLException;

    public abstract java.io.InputStream getResourceAsStream(String path);

    public abstract Set getResourcePaths(String path);

    public abstract Object getResponse();

    public abstract Object getSession(boolean create);

    public abstract Map getSessionMap();

    public abstract java.security.Principal getUserPrincipal();

    public abstract boolean isUserInRole(String role);

    public abstract void log(String message);

    public abstract void log(String message,
                             Throwable exception);

    public abstract void redirect(String url)
            throws java.io.IOException;
}

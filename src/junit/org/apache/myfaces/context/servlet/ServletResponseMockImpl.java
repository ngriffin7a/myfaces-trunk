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
package org.apache.myfaces.context.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ServletResponseMockImpl
    implements HttpServletResponse
{
    private Locale _locale = Locale.getDefault();

    public void addCookie(Cookie cookie)
    {
        throw new UnsupportedOperationException();
    }

    public boolean containsHeader(String name)
    {
        throw new UnsupportedOperationException();
    }

    public String encodeURL(String name)
    {
        throw new UnsupportedOperationException();
    }

    public String encodeRedirectURL(String name)
    {
        throw new UnsupportedOperationException();
    }

    public String encodeUrl(String name)
    {
        throw new UnsupportedOperationException();
    }

    public String encodeRedirectUrl(String name)
    {
        throw new UnsupportedOperationException();
    }

    public void sendError(int i, String name) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public void sendError(int i) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public void sendRedirect(String name) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public void setDateHeader(String name, long l)
    {
        throw new UnsupportedOperationException();
    }

    public void addDateHeader(String name, long l)
    {
        throw new UnsupportedOperationException();
    }

    public void setHeader(String name, String name1)
    {
        throw new UnsupportedOperationException();
    }

    public void addHeader(String name, String name1)
    {
        throw new UnsupportedOperationException();
    }

    public void setIntHeader(String name, int i)
    {
        throw new UnsupportedOperationException();
    }

    public void addIntHeader(String name, int i)
    {
        throw new UnsupportedOperationException();
    }

    public void setStatus(int i)
    {
        throw new UnsupportedOperationException();
    }

    public void setStatus(int i, String name)
    {
        throw new UnsupportedOperationException();
    }

    public String getCharacterEncoding()
    {
        throw new UnsupportedOperationException();
    }

    public ServletOutputStream getOutputStream() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public PrintWriter getWriter() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public void setContentLength(int i)
    {
        throw new UnsupportedOperationException();
    }

    public void setContentType(String name)
    {
        throw new UnsupportedOperationException();
    }

    public void setBufferSize(int i)
    {
        throw new UnsupportedOperationException();
    }

    public int getBufferSize()
    {
        throw new UnsupportedOperationException();
    }

    public void flushBuffer() throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public void resetBuffer()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isCommitted()
    {
        throw new UnsupportedOperationException();
    }

    public void reset()
    {
        throw new UnsupportedOperationException();
    }

    public Locale getLocale()
    {
        return _locale;
    }

    public void setLocale(Locale locale)
    {
        _locale = locale;
    }

    public String getContentType()
    {
        throw new UnsupportedOperationException();
    }

    public void setCharacterEncoding(String s)
    {
        throw new UnsupportedOperationException();
    }
}

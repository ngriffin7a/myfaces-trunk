/**
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
package net.sourceforge.myfaces.context;

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
}

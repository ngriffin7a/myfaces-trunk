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
package javax.faces.webapp;

import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Writer;

/**
 * This Writer writes always to the current pageContext.getOut() Writer.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class _PageContextOutWriter
        extends Writer
{
    private PageContext _pageContext;

    public _PageContextOutWriter(PageContext pageContext)
    {
        _pageContext = pageContext;
    }

    public void close() throws IOException
    {
        _pageContext.getOut().close();
    }

    public void flush() throws IOException
    {
        _pageContext.getOut().flush();
    }

    public void write(char cbuf[], int off, int len) throws IOException
    {
        _pageContext.getOut().write(cbuf, off, len);
    }

    public void write(int c) throws IOException
    {
        _pageContext.getOut().write(c);
    }

    public void write(char cbuf[]) throws IOException
    {
        _pageContext.getOut().write(cbuf);
    }

    public void write(String str) throws IOException
    {
        _pageContext.getOut().write(str);
    }

    public void write(String str, int off, int len) throws IOException
    {
        _pageContext.getOut().write(str, off, len);
    }

}

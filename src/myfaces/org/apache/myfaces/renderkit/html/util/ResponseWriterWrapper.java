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
package net.sourceforge.myfaces.renderkit.html.util;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class ResponseWriterWrapper
        extends ResponseWriter
{
    protected ResponseWriter _responseWriter;

    public ResponseWriterWrapper(ResponseWriter responseWriter)
    {
        _responseWriter = responseWriter;
    }

    public String getContentType()
    {
        return _responseWriter.getContentType();
    }

    public String getCharacterEncoding()
    {
        return _responseWriter.getCharacterEncoding();
    }

    public void flush() throws IOException
    {
        _responseWriter.flush();
    }

    public void startDocument() throws IOException
    {
        _responseWriter.startDocument();
    }

    public void endDocument() throws IOException
    {
        _responseWriter.endDocument();
    }

    public void startElement(String s, UIComponent uicomponent) throws IOException
    {
        _responseWriter.startElement(s, uicomponent);
    }

    public void endElement(String s) throws IOException
    {
        _responseWriter.endElement(s);
    }

    public void writeAttribute(String s, Object obj, String s1) throws IOException
    {
        _responseWriter.writeAttribute(s, obj, s1);
    }

    public void writeURIAttribute(String s, Object obj, String s1) throws IOException
    {
        _responseWriter.writeURIAttribute(s, obj, s1);
    }

    public void writeComment(Object obj) throws IOException
    {
        _responseWriter.writeComment(obj);
    }

    public void writeText(Object obj, String s) throws IOException
    {
        _responseWriter.writeText(obj, s);
    }

    public void writeText(char ac[], int i, int j) throws IOException
    {
        _responseWriter.writeText(ac, i, j);
    }

    public abstract ResponseWriter cloneWithWriter(Writer writer);

    public void close() throws IOException
    {
        _responseWriter.close();
    }

    public void write(char cbuf[], int off, int len) throws IOException
    {
        _responseWriter.write(cbuf, off, len);
    }

    public void write(int c) throws IOException
    {
        _responseWriter.write(c);
    }

    public void write(char cbuf[]) throws IOException
    {
        _responseWriter.write(cbuf);
    }

    public void write(String str) throws IOException
    {
        _responseWriter.write(str);
    }

    public void write(String str, int off, int len) throws IOException
    {
        _responseWriter.write(str, off, len);
    }
}

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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;

import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;


/**
 * DOCUMENT ME!
 * @author Anton Koinov (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HTMLResponseWriter extends ResponseWriter
{
    //~ Instance fields ----------------------------------------------------------------------------

    private final String _contentType;
    private final String _encoding;
    private final Writer _writer;
    private boolean      _encodeText;
    private boolean      _tagOpen;

    //~ Constructors -------------------------------------------------------------------------------

    public HTMLResponseWriter(Writer writer, String contentType, String encoding)
    throws FacesException
    {
        _writer          = writer;
        _contentType     = (contentType == null) ? "text/html" : contentType;
        _encoding        = encoding;
    }

    //~ Methods ------------------------------------------------------------------------------------

    public String getCharacterEncoding()
    {
        return _encoding;
    }

    public String getContentType()
    {
        return _contentType;
    }

    public ResponseWriter cloneWithWriter(Writer writer)
    {
        return new HTMLResponseWriter(writer, getContentType(), getCharacterEncoding());
    }

    public void close()
    throws IOException
    {
        closeTag();
        _writer.close();
    }

    public void endDocument()
    throws IOException
    {
        _writer.write("</html>");
        _writer.flush();
    }

    public void endElement(String name)
    throws IOException
    {
        if (name == null)
        {
            throw new NullPointerException("name");
        }

        if (_tagOpen)
        {
            _writer.write(" />");
        }
        else
        {
            _writer.write("</");
            _writer.write(name);
            _writer.write('>');
        }
    }

    public void flush()
    throws IOException
    {
        closeTag();
    }

    public void startDocument()
    throws IOException
    {
        _writer.write("<html>");
    }

    public void startElement(String name, UIComponent componentForElement)
    throws IOException
    {
        if (name == null)
        {
            throw new NullPointerException("name");
        }

        closeTag();

        char firstChar = name.charAt(0);
        _encodeText =
            ((firstChar == 's') || (firstChar == 'S'))
                    && ("script".equalsIgnoreCase(name) || "style".equalsIgnoreCase(name));

        _writer.write("<");
        _writer.write(name);
        _tagOpen = true;
    }

    public void write(char[] cbuf, int off, int len)
    throws IOException
    {
        closeTag();
        _writer.write(cbuf, off, len);
    }

    public void write(int i)
    throws IOException
    {
        closeTag();
        _writer.write(i);
    }

    public void write(String str)
    throws IOException
    {
        closeTag();
        _writer.write(str);
    }

    public void write(String str, int off, int len)
    throws IOException
    {
        closeTag();
        _writer.write(str, off, len);
    }

    public void writeAttribute(String name, Object value, String componentPropertyName)
    throws IOException
    {
        if (name == null)
        {
            throw new NullPointerException("name");
        }
        if (value == null)
        {
            throw new NullPointerException("value");
        }
        if (!_tagOpen)
        {
            throw new IllegalStateException("Start tag not open. Attribute: " + name);
        }

        if (value instanceof Boolean)
        {
            if (((Boolean) value).booleanValue())
            {
                // in XHTML format
                _writer.write(' ');
                _writer.write(name);
                _writer.write("=\"");
                _writer.write(name);
                _writer.write('"');
            }
        }
        else
        {
            _writer.write(' ');
            _writer.write(name);
            _writer.write("=\"");
            _writer.write(HTMLEncoder.encode(value.toString()));
            _writer.write('"');
        }
    }

    public void writeComment(Object comment)
    throws IOException
    {
        if (comment == null)
        {
            throw new NullPointerException("comment");
        }

        closeTag();

        _writer.write("<!-- ");
        _writer.write(comment.toString());
        _writer.write(" -->");

        return;
    }

    public void writeText(Object value, String componentPropertyName)
    throws IOException
    {
        if (value == null)
        {
            throw new NullPointerException("value");
        }

        closeTag();

        if (_encodeText)
        {
            _writer.write(HTMLEncoder.encode(value.toString()));
        }
        else
        {
            _writer.write(value.toString());
        }
    }

    public void writeText(char[] cbuf, int off, int len)
    throws IOException
    {
        if (cbuf == null)
        {
            throw new NullPointerException("cbuf");
        }
        if ((off < 0) || (off > cbuf.length) || (len < 0) || (len > cbuf.length))
        {
            throw new IndexOutOfBoundsException();
        }

        closeTag();

        if (_encodeText)
        {
            // TODO: add encoder function to work on cbuf
            // TODO: make encoder handle different encodings
            _writer.write(HTMLEncoder.encode(new String(cbuf)));
        }
        else
        {
            _writer.write(cbuf, off, len);
        }
    }

    public void writeURIAttribute(String name, Object value, String componentPropertyName)
    throws IOException
    {
        if (name == null)
        {
            throw new NullPointerException("name");
        }
        if (value == null)
        {
            throw new NullPointerException("value");
        }
        if (!_tagOpen)
        {
            throw new IllegalStateException("Start tag not open. Attribute: " + name);
        }

        _writer.write(' ');
        _writer.write(name);
        _writer.write("=\"");

        String stringValue = value.toString();
        if (stringValue.startsWith("javascript:"))
        {
            _writer.write(HTMLEncoder.encode(stringValue));
        }
        else
        {
            _writer.write(URLEncoder.encode(stringValue, _encoding));
        }
        _writer.write('"');
    }

    private void closeTag()
    throws IOException
    {
        if (_tagOpen)
        {
            _writer.write(">");
            _tagOpen = false;
        }
    }
}

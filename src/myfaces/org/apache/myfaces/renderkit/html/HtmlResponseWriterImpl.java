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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HtmlResponseWriterImpl
        extends ResponseWriter
{
    private static final Log log = LogFactory.getLog(HtmlResponseWriterImpl.class);

    private static final String DEFAUL_CONTENT_TYPE = "text/html";
    private static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
    private static final Set SUPPORTED_CONTENT_TYPES
            = Collections.singleton(DEFAUL_CONTENT_TYPE);

    private Writer _writer;
    private String _contentType;
    private String _characterEncoding;

// currently not used
//    private UIComponent _currentComponent = null;
    /** 
     * If null, then the element has been closed. This allows empty elements to be either
     * explicitly closed by calling endElement() (strongly recommeded for avoiding nesting errors)
     * or implicitly closed by closing an outer element. 
     */
    private String _startElementName;
    
    private static final Set EMPTY_ELEMENTS = new HashSet();

    static
    {
        EMPTY_ELEMENTS.add("area");
        EMPTY_ELEMENTS.add("br");
        EMPTY_ELEMENTS.add("base");
        EMPTY_ELEMENTS.add("basefont");
        EMPTY_ELEMENTS.add("col");
        EMPTY_ELEMENTS.add("frame");
        EMPTY_ELEMENTS.add("hr");
        EMPTY_ELEMENTS.add("img");
        EMPTY_ELEMENTS.add("input");
        EMPTY_ELEMENTS.add("isindex");
        EMPTY_ELEMENTS.add("link");
        EMPTY_ELEMENTS.add("meta");
        EMPTY_ELEMENTS.add("param");
    }

    public HtmlResponseWriterImpl(Writer writer, String contentType, String characterEncoding)
    {
        _writer = writer;
        _contentType = contentType;
        if (_contentType == null)
        {
            if (log.isInfoEnabled()) log.info("No content type given, using default content type " + DEFAUL_CONTENT_TYPE);
            _contentType = DEFAUL_CONTENT_TYPE;
        }
        _characterEncoding = characterEncoding;
        if (_characterEncoding == null)
        {
            if (log.isInfoEnabled()) log.info("No character encoding given, using default character encoding " + DEFAULT_CHARACTER_ENCODING);
            _characterEncoding = DEFAULT_CHARACTER_ENCODING;
        }
    }

    public static boolean supportsContentType(String contentType)
    {
        return SUPPORTED_CONTENT_TYPES.contains(contentType);   //TODO: Match according to Section 14.1 of RFC 2616
    }

    public String getContentType()
    {
        return _contentType;
    }

    public String getCharacterEncoding()
    {
        return _characterEncoding;
    }

    public void flush() throws IOException
    {
        // API doc says we should not flush the underlying writer
        //_writer.flush();
        // but rather clear any values buffered by this ResponseWriter:
        closeStartElementIfNecessary();
    }

    public void startDocument()
    {
        // do nothing
    }

    public void endDocument() throws IOException
    {
        closeStartElementIfNecessary();
        _writer.flush();
    }

    public void startElement(String name, UIComponent uiComponent) throws IOException
    {
        closeStartElementIfNecessary();
        _writer.write('<');
        _writer.write(name);
//        _currentComponent = uiComponent;
        _startElementName = name;
    }

    private void closeStartElementIfNecessary() throws IOException
    {
        if (_startElementName != null)
        {
            if (EMPTY_ELEMENTS.contains(_startElementName))
            {
                _writer.write(" />");
            }
            else
            {    
                _writer.write('>');
            }
            _startElementName = null;
        }
    }

    public void endElement(String name) throws IOException
    {
        if(_startElementName != null)
        {
            // we will get here only if no text was written after the start element was opened
            _writer.write(" />");
        }
        
        // If we are closing an outer element, write the end tag.
        if (!name.equals(_startElementName))
        {    
            _writer.write("</");
            _writer.write(name);
            _writer.write('>');
        }
        _startElementName = null;
//        _currentComponent = null;
    }

    public void writeAttribute(String name, Object value, String componentPropertyName) throws IOException
    {
        if (_startElementName == null)
        {
            throw new IllegalStateException("Must be called before the start element is closed");
        }
        if (value instanceof Boolean)
        {
            if (((Boolean)value).booleanValue())
            {
                // name as value for XHTML compatibility
                _writer.write(' ');
                _writer.write(name);
                _writer.write("=\"");
                _writer.write(name);
                _writer.write('"');
            }
        }
        else
        {
            String strValue = value.toString(); //TODO: Use converter for value
            _writer.write(' ');
            _writer.write(name);
            _writer.write("=\"");
            _writer.write(HTMLEncoder.encode(strValue, false, false));
            _writer.write('"');
        }
    }

    public void writeURIAttribute(String name, Object value, String componentPropertyName) throws IOException
    {
        if (_startElementName == null)
        {
            throw new IllegalStateException("Must be called before the start element is closed");
        }
        String strValue = value.toString(); //TODO: Use converter for value
        _writer.write(' ');
        _writer.write(name);
        _writer.write("=\"");
        if (strValue.toLowerCase().startsWith("javascript:"))
        {
            _writer.write(HTMLEncoder.encode(strValue, false, false));
        }
        else
        {
            /*
            int queryStringIdx = strValue.indexOf('?');
            if (queryStringIdx == -1)
            {
                _writer.write(strValue);
            }
            else
            {
                _writer.write(strValue, 0, queryStringIdx + 1);
                _writer.write(URLEncoder.encode(strValue.substring(queryStringIdx + 1),
                                                _characterEncoding));
            }
            */
            _writer.write(strValue);
        }
        _writer.write('"');
    }

    public void writeComment(Object value) throws IOException
    {
        closeStartElementIfNecessary();
        _writer.write("<!--");
        _writer.write(value.toString());    //TODO: Escaping: must not have "-->" inside!
        _writer.write("-->");
    }

    public void writeText(Object value, String componentPropertyName) throws IOException
    {
        closeStartElementIfNecessary();
        //TODO: do not escape text within script or style

        if(value == null)
            return;

        String strValue = value.toString(); //TODO: Use converter for value?
        _writer.write(HTMLEncoder.encode(strValue, false, false));
    }

    public void writeText(char cbuf[], int off, int len) throws IOException
    {
        closeStartElementIfNecessary();
        //TODO: do not escape text within script or style
        String strValue = new String(cbuf, off, len);
        _writer.write(HTMLEncoder.encode(strValue, false, false));  //TODO: Make HTMLEncoder support char arrays directly
    }

    public ResponseWriter cloneWithWriter(Writer writer)
    {
        return new HtmlResponseWriterImpl(writer, getContentType(), getCharacterEncoding());
    }


    // Writer methods

    public void close() throws IOException
    {
        if (_startElementName != null)
        {
            // we will get here only if no text was written after the start element was opened
            _writer.write(" />");
        }
        _writer.close();
    }

    public void write(char cbuf[], int off, int len) throws IOException
    {
        closeStartElementIfNecessary();
        _writer.write(cbuf, off, len);
    }

    public void write(int c) throws IOException
    {
        closeStartElementIfNecessary();
        _writer.write(c);
    }

    public void write(char cbuf[]) throws IOException
    {
        closeStartElementIfNecessary();
        _writer.write(cbuf);
    }

    public void write(String str) throws IOException
    {
        closeStartElementIfNecessary();
        // empty string commonly used to force the start tag to be closed,
        // do not call down the writer chain
        if (str.length() > 0)
        {
            _writer.write(str);
        }
    }

    public void write(String str, int off, int len) throws IOException
    {
        closeStartElementIfNecessary();
        _writer.write(str, off, len);
    }
}

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

import net.sourceforge.myfaces.application.jsp.JspViewHandlerImpl;
import net.sourceforge.myfaces.renderkit.html.util.DummyFormResponseWriter;
import net.sourceforge.myfaces.renderkit.html.util.DummyFormUtils;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
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
        implements DummyFormResponseWriter
{
    private static final Log log = LogFactory.getLog(HtmlResponseWriterImpl.class);

    private static final String DEFAUL_CONTENT_TYPE = "text/html";
    private static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";
    private static final Set SUPPORTED_CONTENT_TYPES
            = Collections.singleton(DEFAUL_CONTENT_TYPE);

    private boolean _writeDummyForm = false;
    private Set _dummyFormParams = null;

    private Writer _writer;
    private String _contentType;
    private String _characterEncoding;
    private String _startElementName;
    private boolean _startTagOpen;

    private static final Set s_emptyHtmlElements = new HashSet();
    static
    {
        s_emptyHtmlElements.add("area");
        s_emptyHtmlElements.add("br");
        s_emptyHtmlElements.add("base");
        s_emptyHtmlElements.add("basefont");
        s_emptyHtmlElements.add("col");
        s_emptyHtmlElements.add("frame");
        s_emptyHtmlElements.add("hr");
        s_emptyHtmlElements.add("img");
        s_emptyHtmlElements.add("input");
        s_emptyHtmlElements.add("isindex");
        s_emptyHtmlElements.add("link");
        s_emptyHtmlElements.add("meta");
        s_emptyHtmlElements.add("param");
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
        //return SUPPORTED_CONTENT_TYPES.contains(contentType);   //TODO: Match according to Section 14.1 of RFC 2616
        return true;
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
        flush();
        if (_writeDummyForm)
        {
            DummyFormUtils.writeDummyForm(this, _dummyFormParams);
        }
        _writer.flush();
    }

    public void startElement(String name, UIComponent uiComponent) throws IOException
    {
        closeStartElementIfNecessary();
        _writer.write('<');
        _writer.write(name);
        _startElementName = name;
        _startTagOpen = true;
    }

    private void closeStartElementIfNecessary() throws IOException
    {
        if (_startTagOpen)
        {
            if (s_emptyHtmlElements.contains(_startElementName.toLowerCase()))
            {
                _writer.write(" />");
                // make null, this will cause NullPointer in some invalid element nestings
                // (better than doing nothing)
                _startElementName = null;
            }
            else
            {
                _writer.write('>');
            }
            _startTagOpen = false;
        }
    }

    public void endElement(String name) throws IOException
    {
        if(_startTagOpen)
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

        _startTagOpen = false;
//        _currentComponent = null;
    }

    public void writeAttribute(String name, Object value, String componentPropertyName) throws IOException
    {
        if (!_startTagOpen)
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
        if (!_startTagOpen)
        {
            throw new IllegalStateException("Must be called before the start element is closed");
        }
        String strValue = value.toString(); //TODO: Use converter for value?
        _writer.write(' ');
        _writer.write(name);
        _writer.write("=\"");
        if (strValue.toLowerCase().startsWith("javascript:"))
        {
            _writer.write(HTMLEncoder.encode(strValue, false, false));
        }
        else
        {
            if (_startElementName.equalsIgnoreCase(HTML.ANCHOR_ELEM) && //TODO: Also support image and button urls ?
                name.equalsIgnoreCase(HTML.HREF_ATTR) &&
                !strValue.startsWith("#"))
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                if (facesContext.getApplication().getStateManager().isSavingStateInClient(facesContext))
                {
                    //save state in client
                    if (facesContext.getApplication().getStateManager().isSavingStateInClient(facesContext))
                    {
                        //TODO/HACK: saving state in url depends on the work together
                        // of 3 (theoretically) pluggable components:
                        // ViewHandler, ResponseWriter and ViewTag
                        // We should try to make this HtmlResponseWriterImpl able
                        // to handle this alone!
                        if (strValue.indexOf('?') < 0)
                        {
                            strValue = strValue + '?' + JspViewHandlerImpl.URL_STATE_MARKER;
                        }
                        else
                        {
                            strValue = strValue + '&' + JspViewHandlerImpl.URL_STATE_MARKER;
                        }
                    }
                }
            }

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
        if(value == null)
            return;

        String strValue = value.toString(); //TODO: Use converter for value?

        if (isScriptOrStyle())
        {
            _writer.write(strValue);
        }
        else
        {
            _writer.write(HTMLEncoder.encode(strValue, false, false));
        }
    }

    public void writeText(char cbuf[], int off, int len) throws IOException
    {
        closeStartElementIfNecessary();

        if (isScriptOrStyle())
        {
            _writer.write(cbuf, off, len);
        }
        else
        {
            // TODO: Make HTMLEncoder support char arrays directly
            String strValue = new String(cbuf, off, len);
            _writer.write(HTMLEncoder.encode(strValue, false, false));
        }
    }

    public ResponseWriter cloneWithWriter(Writer writer)
    {
        HtmlResponseWriterImpl newWriter
                = new HtmlResponseWriterImpl(writer, getContentType(), getCharacterEncoding());
        newWriter._writeDummyForm = _writeDummyForm;
        newWriter._dummyFormParams = _dummyFormParams;
        return newWriter;
    }


    // Writer methods

    public void close() throws IOException
    {
        if (_startTagOpen)
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
        // empty string commonly used to force the start tag to be closed.
        // in such case, do not call down the writer chain
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

    private boolean isScriptOrStyle()
    {
        return _startElementName != null &&
               (_startElementName.equalsIgnoreCase(HTML.SCRIPT_ELEM)
               || _startElementName.equalsIgnoreCase(HTML.STYLE_ELEM)));
    }


    // DummyFormResponseWriter support

    public void setWriteDummyForm(boolean writeDummyForm)
    {
        _writeDummyForm = writeDummyForm;
    }

    public String getDummyFormName()
    {
        return DummyFormUtils.DUMMY_FORM_NAME;
    }

    public void addDummyFormParameter(String paramName)
    {
        if (_dummyFormParams == null)
        {
            _dummyFormParams = new HashSet();
        }
        _dummyFormParams.add(paramName);
    }

}

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
package net.sourceforge.myfaces.util.xml;

import org.apache.commons.logging.Log;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * Convenient error handler for xml sax parsing.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesErrorHandler
        implements ErrorHandler
{
    private Log _log;

    public MyFacesErrorHandler(Log log)
    {
        _log = log;
    }

    public void warning(SAXParseException exception)
    {
        if (_log.isWarnEnabled()) _log.warn(getMessage(exception), exception);
    }

    public void error(SAXParseException exception)
    {
        _log.error(getMessage(exception), exception);
    }

    public void fatalError(SAXParseException exception)
    {
        _log.fatal(getMessage(exception), exception);
    }

    private String getMessage(SAXParseException exception)
    {
        StringBuffer buf = new StringBuffer();
        buf.append("SAXParseException at");
        buf.append(" URI=");
        buf.append(exception.getSystemId());
        buf.append(" Line=");
        buf.append(exception.getLineNumber());
        buf.append(" Column=");
        buf.append(exception.getColumnNumber());
        return buf.toString();
    }

}

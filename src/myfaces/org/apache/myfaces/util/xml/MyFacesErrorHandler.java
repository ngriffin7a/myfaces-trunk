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
package net.sourceforge.myfaces.util.xml;

import org.apache.commons.logging.Log;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

/**
 * Convenient error handler for xml sax parsing.
 * @author gem (latest modification by $Author$)
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

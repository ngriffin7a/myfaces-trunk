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

import java.io.Writer;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class ResponseWriter
        extends Writer
{
    public abstract java.lang.String getContentType();

    public abstract java.lang.String getCharacterEncoding();

    public abstract void flush()
            throws java.io.IOException;

    public abstract void startDocument()
            throws java.io.IOException;

    public abstract void endDocument()
            throws java.io.IOException;

    public abstract void startElement(java.lang.String name,
                                      javax.faces.component.UIComponent component)
            throws java.io.IOException;

    public abstract void endElement(java.lang.String name)
            throws java.io.IOException;

    public abstract void writeAttribute(java.lang.String name,
                                        java.lang.Object value,
                                        java.lang.String property)
            throws java.io.IOException;

    public abstract void writeURIAttribute(java.lang.String name,
                                           java.lang.Object value,
                                           java.lang.String property)
            throws java.io.IOException;

    public abstract void writeComment(java.lang.Object comment)
            throws java.io.IOException;

    public abstract void writeText(java.lang.Object text,
                                   java.lang.String property)
            throws java.io.IOException;

    public abstract void writeText(char[] text,
                                   int off,
                                   int len)
            throws java.io.IOException;

    public abstract javax.faces.context.ResponseWriter cloneWithWriter(java.io.Writer writer);
}

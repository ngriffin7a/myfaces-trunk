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
package net.sourceforge.myfaces.convert;

import net.sourceforge.myfaces.application.MessageUtils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesConverterException
    extends ConverterException
{
    private FacesContext _facesContext;
    private String _messageId;
    private String _stringValue;

    public MyFacesConverterException(FacesContext facesContext,
                                     String messageId,
                                     String stringValue)
    {
        super("Converter exception " + messageId);
        _facesContext = facesContext;
        _messageId = messageId;
        _stringValue = stringValue;
    }

    public String getMessage()
    {
        return getFacesMessage().getDetail();
    }

    public String getLocalizedMessage()
    {
        return getFacesMessage().getDetail();
    }

    /**
     * Release reference to FacesException to help garbage collector.
     */
    public void release()
    {
        _facesContext = null;
        _messageId = null;
        _stringValue = null;
    }

    public FacesMessage getFacesMessage()
    {
        return MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,
                                       _messageId,
                                       new Object[] {_stringValue});
    }
}

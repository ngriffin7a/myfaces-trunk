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
package javax.faces.validator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class ValidatorException
        extends FacesException
{
    private FacesMessage _facesMessage;

    public ValidatorException(FacesMessage message)
    {
        super(facesMessageToString(message));
        _facesMessage = message;
    }

    public ValidatorException(FacesMessage message,
                              Throwable cause)
    {
        super(facesMessageToString(message), cause);
        _facesMessage = message;
    }

    public FacesMessage getFacesMessage()
    {
        return _facesMessage;

    }

    private static String facesMessageToString(FacesMessage message)
    {
        if (message.getSummary() != null)
        {
            if (message.getDetail() != null)
            {
                return message.getSummary() + ": " + message.getDetail();
            }
            else
            {
                return message.getSummary();
            }
        }
        else
        {
            if (message.getDetail() != null)
            {
                return message.getDetail();
            }
            else
            {
                return "";
            }
        }
    }

}

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
package javax.faces.convert;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConverterException
        extends FacesException
{
    // FIELDS
    private FacesMessage _facesMessage;

    // CONSTRUCTORS
    public ConverterException()
    {
        super();
    }

    public ConverterException(FacesMessage facesMessage)
    {
        super(facesMessage.getSummary());
        _facesMessage = facesMessage;
    }

    public ConverterException(FacesMessage facesMessage, Throwable cause)
    {
        super(cause);
        _facesMessage = facesMessage;
    }

    public ConverterException(String message)
    {
        super(message);
    }

    public ConverterException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ConverterException(Throwable cause)
    {
        super(cause);
    }

    // METHODS
    public FacesMessage getFacesMessage()
    {
        return _facesMessage;
    }

}

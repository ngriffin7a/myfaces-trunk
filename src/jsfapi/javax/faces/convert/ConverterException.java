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
package javax.faces.convert;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;

/**
 * $Log$
 * Revision 1.7  2004/07/01 22:00:51  mwessendorf
 * ASF switch
 *
 * Revision 1.6  2004/03/26 12:08:41  manolito
 * Exceptions in getAsString now catched and
 * more relaxed Number casting in all number converters
 *
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

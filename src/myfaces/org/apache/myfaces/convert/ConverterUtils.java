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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.text.DateFormat;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConverterUtils
{
    private static final Log log = LogFactory.getLog(ConverterUtils.class);

    //TODO: Define in Bundle
    private static final String DEFAULT_CONVERTER_EXCEPTION_MSG_ID
            = "net.sourceforge.myfaces.convert.Converter.EXCEPTION";

    private static final int DEFAULT_DATE_STYLE = DateFormat.SHORT;
    private static final int DEFAULT_TIME_STYLE = DateFormat.SHORT;

    private ConverterUtils() {}



    public static int convertToInt(Object value)
    {
        if (value instanceof Number)
        {
            return ((Number)value).intValue();
        }
        else if (value instanceof String)
        {
            try
            {
                return Integer.parseInt((String)value);
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("Cannot convert " + value.toString() + " to int");
            }
        }
        else
        {
            throw new IllegalArgumentException("Cannot convert " + value.toString() + " to int");
        }
    }

    public static long convertToLong(Object value)
    {
        if (value instanceof Number)
        {
            return ((Number)value).longValue();
        }
        else if (value instanceof String)
        {
            try
            {
                return Long.parseLong((String)value);
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("Cannot convert " + value.toString() + " to long");
            }
        }
        else
        {
            throw new IllegalArgumentException("Cannot convert " + value.toString() + " to long");
        }
    }

    public static double convertToDouble(Object value)
    {
        if (value instanceof Number)
        {
            return ((Number)value).doubleValue();
        }
        else if (value instanceof String)
        {
            try
            {
                return Double.parseDouble((String)value);
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("Cannot convert " + value.toString() + " to double");
            }
        }
        else
        {
            throw new IllegalArgumentException("Cannot convert " + value.toString() + " to double");
        }
    }


    /**
     * Converts a String value using the given Converter and handles a
     * possible ConverterException by adding a FacesMessage to the current FacesContext.
     * @param facesContext
     * @param uiComponent
     * @param converter  Converter to use
     * @param strValue   String value to convert
     * @return converted value
     * @throws ConverterException
     *
     * @deprecated 
     */
    public static Object getAsObjectWithErrorHandling(FacesContext facesContext,
                                                      UIComponent uiComponent,
                                                      Converter converter,
                                                      String strValue)
        throws ConverterException
    {
        try
        {
            return converter.getAsObject(facesContext, uiComponent, strValue);
        }
        catch (ConverterException e)
        {
            if (log.isInfoEnabled()) log.info("Converter exception", e);
            FacesMessage msg = e.getFacesMessage();
            if (msg != null)
            {
                facesContext.addMessage(uiComponent.getClientId(facesContext), msg);
            }
            else
            {
                MessageUtils.addMessage(FacesMessage.SEVERITY_ERROR,
                                        DEFAULT_CONVERTER_EXCEPTION_MSG_ID,
                                        null,
                                        uiComponent.getClientId(facesContext));
            }
            throw e;
        }
    }


}

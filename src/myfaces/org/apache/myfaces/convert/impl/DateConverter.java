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
package net.sourceforge.myfaces.convert.impl;

import net.sourceforge.myfaces.convert.ConverterUtils;
import net.sourceforge.myfaces.convert.MyFacesConverterException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DateConverter
    implements Converter
{
    private static final String CONVERTER_EXCEPTION_MSG_ID = DateConverter.class.getName() + ".EXCEPTION";
    private static final String CONVERTER_EXCEPTION_TYPE_ERROR_MSG_ID = DateConverter.class.getName() + ".TYPE_ERROR";

    public Object getAsObject(FacesContext context, UIComponent component, String value)
            throws ConverterException
    {
        if (value == null || value.length() == 0)
        {
            return null;
        }

        //FIXME
        //Locale locale = context.getLocale();
        Locale locale = null;
        try
        {
            return parse(component, value, DateFormat.SHORT, locale);
        }
        catch (ParseException ex1)
        {
            try
            {
                return parse(component, value, DateFormat.MEDIUM, locale);
            }
            catch (ParseException ex2)
            {
                try
                {
                    return parse(component, value, DateFormat.LONG, locale);
                }
                catch (ParseException ex3)
                {
                    try
                    {
                        return parse(component, value, DateFormat.FULL, locale);
                    }
                    catch (ParseException ex4)
                    {
                        try
                        {
                            DateFormat format = ConverterUtils.getDateFormat(
                                    component, locale);
                            format.setTimeZone(
                                    ConverterUtils.getTimeZone(component));

                            return format.parse(value);
                        }
                        catch (ParseException ex5)
                        {
                        }
                    }
                }
            }
            throw new MyFacesConverterException(context,
                                                component,
                                                CONVERTER_EXCEPTION_MSG_ID,
                                                value);
        }
    }


    private static Object parse(UIComponent component, String value, int dateStyle, Locale locale)
        throws ParseException
    {
        DateFormat format = DateFormat.getDateInstance(dateStyle, locale);
        format.setTimeZone(ConverterUtils.getTimeZone(component));
        return format.parse(value);
    }


    public String getAsString(FacesContext context, UIComponent component, Object value)
            throws ConverterException
    {
        if (value == null)
        {
            return "";
        }
        else if (value instanceof Date)
        {
            //FIXME
            //Locale locale = context.getLocale();
            Locale locale = null;
            DateFormat format = ConverterUtils.getDateFormat(component, locale);
            return format.format(value);
        }
        else if (value instanceof Number)
        {
            //FIXME
            //Locale locale = context.getLocale();
            Locale locale = null;
            Date dateValue = new Date(((Number)value).longValue());
            DateFormat format = ConverterUtils.getDateFormat(component, locale);
            return format.format(dateValue);
        }
        else
        {
            throw new MyFacesConverterException(context,
                                                component,
                                                CONVERTER_EXCEPTION_TYPE_ERROR_MSG_ID,
                                                value.getClass().getName());
        }
    }

}

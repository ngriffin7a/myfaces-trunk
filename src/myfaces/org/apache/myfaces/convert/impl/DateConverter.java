/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2002 Manfred Geiler, Thomas Spiegl
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

import net.sourceforge.myfaces.convert.Converter;
import net.sourceforge.myfaces.convert.ConverterException;
import net.sourceforge.myfaces.convert.ConverterUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class DateConverter
    implements Converter
{
    private static final String CONVERTER_ID = "DateConverter";
    public String getConverterId()
    {
        return CONVERTER_ID;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value)
            throws ConverterException
    {
        if (value == null || value.length() == 0)
        {
            return null;
        }

        Locale locale = context.getLocale();
        try
        {
            return parse(value, DateFormat.SHORT, locale);
        }
        catch (ParseException ex1)
        {
            try
            {
                return parse(value, DateFormat.MEDIUM, locale);
            }
            catch (ParseException ex2)
            {
                try
                {
                    return parse(value, DateFormat.LONG, locale);
                }
                catch (ParseException ex3)
                {
                    try
                    {
                        return parse(value, DateFormat.FULL, locale);
                    }
                    catch (ParseException ex4)
                    {
                    }
                }
            }
            throw new ConverterException(ex1);
        }
    }


    private static Object parse(String value, int dateStyle, Locale locale)
        throws ParseException
    {
        DateFormat format = DateFormat.getDateInstance(dateStyle, locale);
        return format.parse(value);
    }


    public String getAsString(FacesContext context, UIComponent component, Object value)
            throws ConverterException
    {
        if (value == null)
        {
            return "";
        }
        DateFormat format = ConverterUtils.getDateFormat(component, context.getLocale());
        return format.format(value);
    }

}

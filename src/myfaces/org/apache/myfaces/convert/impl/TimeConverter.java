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

/**
 * TODO: description
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class TimeConverter
    implements Converter
{
    private static final String CONVERTER_ID = "TimeConverter";
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

        DateFormat format = ConverterUtils.getTimeFormat(component, context.getLocale());
        try
        {
            return format.parse(value);
        }
        catch (ParseException e)
        {
            throw new ConverterException(e);
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object value)
            throws ConverterException
    {
        if (value == null)
        {
            return "";
        }
        DateFormat format = ConverterUtils.getTimeFormat(component, context.getLocale());
        return format.format(value);
    }

}

/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class BooleanConverter
    implements Converter
{
    private static final String CONVERTER_EXCEPTION_MSG_ID = BooleanConverter.class.getName() + ".EXCEPTION";

    private static final String CONVERTER_ID = "BooleanConverter";
    public String getConverterId()
    {
        return CONVERTER_ID;
    }

    private static final Map BOOLEAN_MAP = new HashMap();
    static
    {
        BOOLEAN_MAP.put("0", Boolean.FALSE);
        BOOLEAN_MAP.put("1", Boolean.TRUE);

        BOOLEAN_MAP.put("no", Boolean.FALSE);
        BOOLEAN_MAP.put("yes", Boolean.TRUE);

        BOOLEAN_MAP.put("false", Boolean.FALSE);
        BOOLEAN_MAP.put("true", Boolean.TRUE);
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value)
            throws ConverterException
    {
        if (value == null || value.length() == 0)
        {
            return null;
        }

        Boolean b = (Boolean)BOOLEAN_MAP.get(value);
        if (b == null)
        {
            throw new ConverterException(CONVERTER_EXCEPTION_MSG_ID, value);
        }
        return b;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value)
            throws ConverterException
    {
        if (value == null)
        {
            return "";
        }
        Boolean b = (Boolean)value;
        if (b != null && b.booleanValue())
        {
            return "1";
        }
        else
        {
            return "0";
        }
    }

}

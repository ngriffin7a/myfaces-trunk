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
import java.util.StringTokenizer;

/**
 * TODO: Support for ',' character within StringArray?
 *
 * @author Thomas Spiegl (latest modification by Author)
 * @version $Revision$ $Date$
 */
public class StringArrayConverter
    implements Converter
{
    private static final String CONVERTER_ID = "StringArrayConverter";
    public String getConverterId()
    {
        return CONVERTER_ID;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value)
        throws ConverterException
    {
        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        String[] newValue = new String[tokenizer.countTokens()];
        for (int i = 0; tokenizer.hasMoreTokens(); i++)
        {
            newValue[i] = tokenizer.nextToken();
        }
        return newValue;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value)
            throws ConverterException
    {
        return getAsString((String[])value);
    }


    public static String getAsString(String[] strings)
    {
        if (strings == null || strings.length == 0)
        {
            return null;
        }
        else if (strings.length == 1)
        {
            return strings[0];
        }
        else
        {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < strings.length; i++)
            {
                if (i > 0)
                {
                    buf.append(',');
                }
                String s = strings[i];
                if (s.indexOf(',') != -1)
                {
                    throw new IllegalArgumentException("Commas within StringArrays are not supported!");
                }
                buf.append(s);
            }
            return buf.toString();
        }
    }

}

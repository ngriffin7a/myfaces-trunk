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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.StringTokenizer;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StringArrayConverter
    implements Converter
{
    public Object getAsObject(FacesContext context, UIComponent component, String value)
        throws ConverterException
    {
        try
        {
            StringTokenizer tokenizer = new StringTokenizer(value, ",");
            String[] newValue = new String[tokenizer.countTokens()];
            for (int i = 0; tokenizer.hasMoreTokens(); i++)
            {
                newValue[i] = URLDecoder.decode(tokenizer.nextToken(), "UTF-8");
            }
            return newValue;
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object value)
            throws ConverterException
    {
        return getAsString((String[])value,
                           true);   //escapeCommas
    }


    public static String getAsString(String[] strings,
                                     boolean escapeCommas)
    {
        try
        {
            if (strings == null || strings.length == 0)
            {
                return null;
            }
            else if (strings.length == 1)
            {

                return escapeCommas
                        ? URLEncoder.encode(strings[0], "UTF-8") //Encode, so that commas within Strings are escaped
                        : strings[0];
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
                    if (escapeCommas)
                    {
                        //Encode, so that commas within Strings are escaped
                        s = URLEncoder.encode(s, "UTF-8");
                    }
                    buf.append(s);
                }
                return buf.toString();
            }
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

}

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


/**
 * TODO: Move to util package and rename to better name
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public final class ConverterUtils
{
    //private static final Log log = LogFactory.getLog(ConverterUtils.class);

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

	public static boolean convertToBoolean(Object value)
	{
		if (value instanceof Boolean)
		{
			return ((Boolean)value).booleanValue();
		}
		else if (value instanceof String)
		{
			try
			{
				return new Boolean((String)value).booleanValue();
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException("Cannot convert " + value.toString() + " to boolean");
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


}

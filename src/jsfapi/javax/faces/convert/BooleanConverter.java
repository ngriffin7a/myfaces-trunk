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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * $Log$
 * Revision 1.8  2004/06/07 13:40:37  mwessendorf
 * solved Feature Request #966892
 *
 * Revision 1.7  2004/03/26 12:08:41  manolito
 * Exceptions in getAsString now catched and
 * more relaxed Number casting in all number converters
 *
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class BooleanConverter
        implements Converter
{
    private static final String CONVERSION_MESSAGE_ID = "javax.faces.convert.BooleanConverter.CONVERSION";

    // FIELDS
    public static final String CONVERTER_ID = "javax.faces.Boolean";

    // CONSTRUCTORS
    public BooleanConverter()
    {
    }

    // METHODS
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value)
    {
        if (facesContext == null) throw new NullPointerException("facesContext");
        if (uiComponent == null) throw new NullPointerException("uiComponent");

        if (value != null)
        {
            value = value.trim();
            if (value.length() > 0)
            {
                try
                {
                    return Boolean.valueOf(value);
                }
                catch (Exception e)
                {
                    throw new ConverterException(_MessageUtils.getErrorMessage(facesContext,
                                                                               CONVERSION_MESSAGE_ID,
                                                                               new Object[]{value,uiComponent.getId()}), e);
                }
            }
        }
        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value)
    {
        if (facesContext == null) throw new NullPointerException("facesContext");
        if (uiComponent == null) throw new NullPointerException("uiComponent");

        if (value == null)
        {
            return "";
        }
        if (value instanceof String)
        {
            return (String)value;
        }
        try
        {
            return ((Boolean)value).toString();
        }
        catch (Exception e)
        {
            throw new ConverterException(e);
        }
    }
}

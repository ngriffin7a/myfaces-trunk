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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DateTimeConverter
        implements Converter
{
    private static final String CONVERSION_MESSAGE_ID = "javax.faces.convert.DateTimeConverter.CONVERSION";

    // FIELDS
    public static final String CONVERTER_ID = "javax.faces.DateTime";

    private String _dateStyle;
    private Locale _locale;
    private String _pattern;
    private String _timeStyle;
    private TimeZone _timeZone;
    private String _type;
    private boolean _transient;

    // CONSTRUCTORS
    public DateTimeConverter()
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
                DateFormat format = getDateFormat(facesContext);
                format.setLenient(true);
                if (_timeZone != null)
                {
                    format.setTimeZone(_timeZone);
                }
                try
                {
                    return format.parse(value);
                }
                catch (ParseException e)
                {
                    throw new ConverterException(_MessageUtils.getErrorMessage(facesContext,
                                                                               CONVERSION_MESSAGE_ID,
                                                                               new Object[]{value}), e);
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

        DateFormat format = getDateFormat(facesContext);
        if (_timeZone != null)
        {
            format.setTimeZone(_timeZone);
        }
        try
        {
            return format.format(value);
        }
        catch (Exception e)
        {
            throw new ConverterException("Cannot convert value '" + value + "'");
        }
    }

    private DateFormat getDateFormat(FacesContext facesContext)
    {
        Locale lokale = _locale != null ? _locale : facesContext.getViewRoot().getLocale();

        if (_pattern == null && _type == null)
        {
            throw new ConverterException("Cannot get DateFormat, either type or pattern needed.");
        }

        DateFormat format = null;
        if (_pattern != null)
        {
            format = new SimpleDateFormat(_pattern, lokale);
        }
        else if (_type.equals("date"))
        {
            format = DateFormat.getDateInstance(calcStyle(_dateStyle), lokale);
        }
        else if (_type.equals("time"))
        {
            format = DateFormat.getTimeInstance(calcStyle(_timeStyle), lokale);
        }
        else if (_type.equals("both"))
        {
            format = DateFormat.getDateTimeInstance(calcStyle(_dateStyle),
                                                    calcStyle(_timeStyle),
                                                    lokale);
        }
        else
        {
            throw new ConverterException("invalid type '" + _type + "'");
        }
        return format;
    }

    private int calcStyle(String name)
    {
        if (name.equals("default"))
        {
            return DateFormat.DEFAULT;
        }
        if (name.equals("medium"))
        {
            return DateFormat.MEDIUM;
        }
        if (name.equals("short"))
        {
            return DateFormat.SHORT;
        }
        if (name.equals("long"))
        {
            return DateFormat.LONG;
        }
        if (name.equals("full"))
        {
            return DateFormat.FULL;
        }

        throw new ConverterException("invalid style '" + name + "'");
    }

    // STATE SAVE/RESTORE
    public void restoreState(FacesContext facesContext, Object state)
    {
        Object[] values = (Object[])state;
        _dateStyle = (String)values[0];
        _locale = (Locale)values[0];
        _pattern = (String)values[0];
        _timeStyle = (String)values[0];
        _timeZone = (TimeZone)values[0];
        _type = (String)values[0];
    }

    public Object saveState(FacesContext facesContext)
    {
        Object[] values = new Object[6];
        values[0] = _dateStyle;
        values[1] = _locale;
        values[2] = _pattern;
        values[3] = _timeStyle;
        values[4] = _timeZone;
        values[5] = _type;
        return values;
    }

    // GETTER & SETTER
    public String getDateStyle()
    {
        return _dateStyle;
    }

    public void setDateStyle(String dateStyle)
    {
        _dateStyle = dateStyle;
    }

    public Locale getLocale()
    {
        return _locale;
    }

    public void setLocale(Locale locale)
    {
        _locale = locale;
    }

    public String getPattern()
    {
        return _pattern;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

    public String getTimeStyle()
    {
        return _timeStyle;
    }

    public void setTimeStyle(String timeStyle)
    {
        _timeStyle = timeStyle;
    }

    public TimeZone getTimeZone()
    {
        return _timeZone;
    }

    public void setTimeZone(TimeZone timeZone)
    {
        _timeZone = timeZone;
    }

    public boolean isTransient()
    {
        return _transient;
    }

    public void setTransient(boolean aTransient)
    {
        _transient = aTransient;
    }

    public String getType()
    {
        return _type;
    }

    public void setType(String type)
    {
        _type = type;
    }
}

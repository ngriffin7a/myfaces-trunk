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
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.10  2004/04/16 09:08:41  royalts
 * fixed getAsString: getTimeZone() may not be used here!!.
 *
 * Revision 1.9  2004/04/01 10:47:00  royalts
 * bugfix in restoreState
 *
 * Revision 1.8  2004/04/01 10:39:53  royalts
 * implements StateHoder was missing
 *
 * Revision 1.7  2004/03/26 12:08:42  manolito
 * Exceptions in getAsString now catched and
 * more relaxed Number casting in all number converters
 *
 */
public class DateTimeConverter
        implements Converter, StateHolder
{
    private static final String CONVERSION_MESSAGE_ID = "javax.faces.convert.DateTimeConverter.CONVERSION";

    // FIELDS
    public static final String CONVERTER_ID = "javax.faces.DateTime";
    private static final String TYPE_DATE = "date";
    private static final String TYPE_TIME = "time";
    private static final String TYPE_BOTH = "both";
    private static final String STYLE_DEFAULT = "default";
    private static final String STYLE_MEDIUM = "medium";
    private static final String STYLE_SHORT = "short";
    private static final String STYLE_LONG = "long";
    private static final String STYLE_FULL = "full";
    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");

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
                DateFormat format = getDateFormat();
                format.setLenient(true);
                format.setTimeZone(getTimeZone());
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

        DateFormat format = getDateFormat();
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

    private DateFormat getDateFormat()
    {
        String type = getType();
        DateFormat format = null;
        if (_pattern != null)
        {
            format = new SimpleDateFormat(_pattern, getLocale());
        }
        else if (type.equals(TYPE_DATE))
        {
            format = DateFormat.getDateInstance(calcStyle(getDateStyle()), getLocale());
        }
        else if (type.equals(TYPE_TIME))
        {
            format = DateFormat.getTimeInstance(calcStyle(getTimeStyle()), getLocale());
        }
        else if (type.equals(TYPE_BOTH))
        {
            format = DateFormat.getDateTimeInstance(calcStyle(getDateStyle()),
                                                    calcStyle(getTimeStyle()),
                                                    getLocale());
        }
        else
        {
            throw new ConverterException("invalid type '" + _type + "'");
        }
        return format;
    }

    private int calcStyle(String name)
    {
        if (name.equals(STYLE_DEFAULT))
        {
            return DateFormat.DEFAULT;
        }
        if (name.equals(STYLE_MEDIUM))
        {
            return DateFormat.MEDIUM;
        }
        if (name.equals(STYLE_SHORT))
        {
            return DateFormat.SHORT;
        }
        if (name.equals(STYLE_LONG))
        {
            return DateFormat.LONG;
        }
        if (name.equals(STYLE_FULL))
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
        _locale = (Locale)values[1];
        _pattern = (String)values[2];
        _timeStyle = (String)values[3];
        _timeZone = (TimeZone)values[4];
        _type = (String)values[5];
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
        return _dateStyle != null ? _dateStyle : STYLE_DEFAULT;
    }

    public void setDateStyle(String dateStyle)
    {
        //TODO: validate timeStyle
        _dateStyle = dateStyle;
    }

    public Locale getLocale()
    {
        if (_locale != null) return _locale;
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getViewRoot().getLocale();
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
        return _timeStyle != null ? _timeStyle : STYLE_DEFAULT;
    }

    public void setTimeStyle(String timeStyle)
    {
        //TODO: validate timeStyle
        _timeStyle = timeStyle;
    }

    public TimeZone getTimeZone()
    {
        return _timeZone != null ? _timeZone : DEFAULT_TIME_ZONE;
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
        return _type != null ? _type : TYPE_DATE;
    }

    public void setType(String type)
    {
        //TODO: validate type
        _type = type;
    }
}

/*
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
package net.sourceforge.myfaces.taglib.core;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.ConverterTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/04/16 15:35:59  manolito
 * Log
 *
 */
public class ConvertDateTimeTag
        extends ConverterTag
{
    private String _dateStyle = null;
    private String _locale = null;
    private String _pattern = null;
    private String _timeStyle = null;
    private String _timeZone = null;
    private String _type = null;

    public ConvertDateTimeTag()
    {
        setConverterId(DateTimeConverter.CONVERTER_ID);
    }

    public void setDateStyle(String dateStyle)
    {
        _dateStyle = dateStyle;
    }

    public void setLocale(String locale)
    {
        _locale = locale;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

    public void setTimeStyle(String timeStyle)
    {
        _timeStyle = timeStyle;
    }

    public void setTimeZone(String timeZone)
    {
        _timeZone = timeZone;
    }

    public void setType(String type)
    {
        _type = type;
    }

    protected Converter createConverter() throws JspException
    {
        DateTimeConverter converter = (DateTimeConverter)super.createConverter();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        setConverterDateStyle(facesContext, converter, _dateStyle);
        setConverterLocale(facesContext, converter, _locale);
        setConverterPattern(facesContext, converter, _pattern);
        setConverterTimeStyle(facesContext, converter, _timeStyle);
        setConverterTimeZone(facesContext, converter, _timeZone);
        setConverterType(facesContext, converter, _type);

        return converter;
    }
    
    
    private static void setConverterDateStyle(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setDateStyle((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setDateStyle(value);
        }
    }

    private static void setConverterLocale(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setLocale((Locale)vb.getValue(facesContext));
        }
        else
        {
            throw new IllegalArgumentException("Attribute locale of type String not supported");
        }
    }

    private static void setConverterPattern(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setPattern((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setPattern(value);
        }
    }

    private static void setConverterTimeStyle(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setTimeStyle((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setTimeStyle(value);
        }
    }

    private static void setConverterTimeZone(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setTimeZone((TimeZone)vb.getValue(facesContext));
        }
        else
        {
            converter.setTimeZone(TimeZone.getTimeZone(value));
        }
    }

    private static void setConverterType(FacesContext facesContext,
                                              DateTimeConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setType((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setType(value);
        }
    }

}

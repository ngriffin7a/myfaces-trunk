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
import javax.faces.convert.NumberConverter;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.ConverterTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import java.util.Locale;

/**
 * $Log$
 * Revision 1.4  2004/06/27 19:26:18  mwessendorf
 * added default value, needed by jsf-spec
 *
 * Revision 1.3  2004/06/26 00:34:23  o_rossmueller
 * fix #979039: default type = number for convertNumber
 *
 * Revision 1.2  2004/03/26 11:47:10  manolito
 * NPE fixed
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConvertNumberTag
        extends ConverterTag
{
    private String _currencyCode = null;
    private String _currencySymbol = null;
    private String _groupingUsed = "true"; // default value as required by the spec
    private String _integerOnly = "false"; // default value as required by the spec
    private String _locale = null;
    private String _maxFractionDigits = null;
    private String _maxIntegerDigits = null;
    private String _minFractionDigits = null;
    private String _minIntegerDigits = null;
    private String _pattern = null;
    private String _type = "number"; // default value as required by the spec

    public ConvertNumberTag()
    {
        setConverterId(NumberConverter.CONVERTER_ID);
    }

    public void setCurrencyCode(String currencyCode)
    {
        _currencyCode = currencyCode;
    }

    public void setCurrencySymbol(String currencySymbol)
    {
        _currencySymbol = currencySymbol;
    }

    public void setGroupingUsed(String groupingUsed)
    {
        _groupingUsed = groupingUsed;
    }

    public void setIntegerOnly(String integerOnly)
    {
        _integerOnly = integerOnly;
    }

    public void setLocale(String locale)
    {
        _locale = locale;
    }

    public void setMaxFractionDigits(String maxFractionDigits)
    {
        _maxFractionDigits = maxFractionDigits;
    }

    public void setMaxIntegerDigits(String maxIntegerDigits)
    {
        _maxIntegerDigits = maxIntegerDigits;
    }

    public void setMinFractionDigits(String minFractionDigits)
    {
        _minFractionDigits = minFractionDigits;
    }

    public void setMinIntegerDigits(String minIntegerDigits)
    {
        _minIntegerDigits = minIntegerDigits;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

    public void setType(String type)
    {
        _type = type;
    }

    protected Converter createConverter() throws JspException
    {
        NumberConverter converter = (NumberConverter)super.createConverter();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        setConverterCurrencyCode(facesContext, converter, _currencyCode);
        setConverterCurrencySymbol(facesContext, converter, _currencySymbol);
        setConverterGroupingUsed(facesContext, converter, _groupingUsed);
        setConverterIntegerOnly(facesContext, converter, _integerOnly);
        setConverterLocale(facesContext, converter, _locale);
        setConverterMaxFractionDigits(facesContext, converter, _maxFractionDigits);
        setConverterMaxIntegerDigits(facesContext, converter, _maxIntegerDigits);
        setConverterMinFractionDigits(facesContext, converter, _minFractionDigits);
        setConverterMinIntegerDigits(facesContext, converter, _minIntegerDigits);
        setConverterPattern(facesContext, converter, _pattern);
        setConverterType(facesContext, converter, _type);

        return converter;
    }


    private static void setConverterCurrencyCode(FacesContext facesContext,
                                              NumberConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setCurrencyCode((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setCurrencyCode(value);
        }
    }

    private static void setConverterCurrencySymbol(FacesContext facesContext,
                                              NumberConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            converter.setCurrencySymbol((String)vb.getValue(facesContext));
        }
        else
        {
            converter.setCurrencySymbol(value);
        }
    }

    private static void setConverterGroupingUsed(FacesContext facesContext,
                                              NumberConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            Boolean b = (Boolean)vb.getValue(facesContext);
            if (b != null)
            {
                converter.setGroupingUsed(b.booleanValue());
            }
        }
        else
        {
            converter.setGroupingUsed(Boolean.valueOf(value).booleanValue());
        }
    }

    private static void setConverterIntegerOnly(FacesContext facesContext,
                                              NumberConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            Boolean b = (Boolean)vb.getValue(facesContext);
            if (b != null)
            {
                converter.setIntegerOnly(b.booleanValue());
            }
        }
        else
        {
            converter.setIntegerOnly(Boolean.valueOf(value).booleanValue());
        }
    }

    private static void setConverterLocale(FacesContext facesContext,
                                              NumberConverter converter,
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

    private static void setConverterMaxFractionDigits(FacesContext facesContext,
                                              NumberConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            Integer i = (Integer)vb.getValue(facesContext);
            if (i != null)
            {
                converter.setMaxFractionDigits(i.intValue());
            }
        }
        else
        {
            converter.setMaxFractionDigits(Integer.parseInt(value));
        }
    }

    private static void setConverterMaxIntegerDigits(FacesContext facesContext,
                                              NumberConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            Integer i = (Integer)vb.getValue(facesContext);
            if (i != null)
            {
                converter.setMaxIntegerDigits(i.intValue());
            }
        }
        else
        {
            converter.setMaxIntegerDigits(Integer.parseInt(value));
        }
    }

    private static void setConverterMinFractionDigits(FacesContext facesContext,
                                              NumberConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            Integer i = (Integer)vb.getValue(facesContext);
            if (i != null)
            {
                converter.setMinFractionDigits(i.intValue());
            }
        }
        else
        {
            converter.setMinFractionDigits(Integer.parseInt(value));
        }
    }

    private static void setConverterMinIntegerDigits(FacesContext facesContext,
                                              NumberConverter converter,
                                              String value)
    {
        if (value == null) return;
        if (UIComponentTag.isValueReference(value))
        {
            ValueBinding vb = facesContext.getApplication().createValueBinding(value);
            Integer i = (Integer)vb.getValue(facesContext);
            if (i != null)
            {
                converter.setMinIntegerDigits(i.intValue());
            }
        }
        else
        {
            converter.setMinIntegerDigits(Integer.parseInt(value));
        }
    }

    private static void setConverterPattern(FacesContext facesContext,
                                              NumberConverter converter,
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

    private static void setConverterType(FacesContext facesContext,
                                              NumberConverter converter,
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

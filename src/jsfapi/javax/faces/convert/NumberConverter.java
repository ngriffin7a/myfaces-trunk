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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.10  2004/06/07 13:40:37  mwessendorf
 * solved Feature Request #966892
 *
 * Revision 1.9  2004/04/01 10:39:53  royalts
 * implements StateHoder was missing
 *
 * Revision 1.8  2004/03/26 12:08:42  manolito
 * Exceptions in getAsString now catched and
 * more relaxed Number casting in all number converters
 *
 */
public class NumberConverter
        implements Converter, StateHolder
{
    private static final String CONVERSION_MESSAGE_ID = "javax.faces.convert.NumberConverter.CONVERSION";

    // FIELDS
    public static final String CONVERTER_ID = "javax.faces.Number";

    public static final boolean JAVA_VERSION_14;

    static
    {
        JAVA_VERSION_14 = checkJavaVersion14();
    }

    private String _currencyCode;
    private String _currencySymbol;
    private Locale _locale;
    private int _maxFractionDigits;
    private int _maxIntegerDigits;
    private int _minFractionDigits;
    private int _minIntegerDigits;
    private String _pattern;
    private String _type;
    private boolean _groupingUsed = true;
    private boolean _integerOnly = false;
    private boolean _transient;

    private boolean _maxFractionDigitsSet;
    private boolean _maxIntegerDigitsSet;
    private boolean _minFractionDigitsSet;
    private boolean _minIntegerDigitsSet;


    // CONSTRUCTORS
    public NumberConverter()
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
                NumberFormat format = getNumberFormat(facesContext);
                format.setParseIntegerOnly(_integerOnly);
                try
                {
                    return format.parse(value);
                }
                catch (ParseException e)
                {
                    throw new ConverterException(_MessageUtils.getErrorMessage(facesContext,
                                                                               CONVERSION_MESSAGE_ID,
                                                                               new Object[]{uiComponent.getId(),value}), e);
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

        NumberFormat format = getNumberFormat(facesContext);
        format.setGroupingUsed(_groupingUsed);
        if (_maxFractionDigitsSet) format.setMaximumFractionDigits(_maxFractionDigits);
        if (_maxIntegerDigitsSet) format.setMaximumIntegerDigits(_maxIntegerDigits);
        if (_minFractionDigitsSet) format.setMinimumFractionDigits(_minFractionDigits);
        if (_minIntegerDigitsSet) format.setMinimumIntegerDigits(_minIntegerDigits);
        formatCurrency(format);
        try
        {
            return format.format(value);
        }
        catch (Exception e)
        {
            throw new ConverterException("Cannot convert value '" + value + "'");
        }
    }

    private NumberFormat getNumberFormat(FacesContext facesContext)
    {
        Locale lokale = _locale != null ? _locale : facesContext.getViewRoot().getLocale();

        if (_pattern == null && _type == null)
        {
            throw new ConverterException("Cannot get NumberFormat, either type or pattern needed.");
        }

        // pattern
        if (_pattern != null)
        {
            return new DecimalFormat(_pattern, new DecimalFormatSymbols(lokale));
        }

        // type
        if (_type.equals("number"))
        {
            return NumberFormat.getNumberInstance(lokale);
        }
        else if (_type.equals("currency"))
        {
            return NumberFormat.getCurrencyInstance(lokale);
        }
        else if (_type.equals("percent"))
        {
            return NumberFormat.getPercentInstance(lokale);
        }
        throw new ConverterException("Cannot get NumberFormat, illegal type " + _type);
    }

    private void formatCurrency(NumberFormat format)
    {
        if (_currencyCode == null && _currencySymbol == null)
        {
            return;
        }

        boolean useCurrencyCode;
        if (JAVA_VERSION_14)
        {
            useCurrencyCode = _currencyCode != null;
        }
        else
        {
            useCurrencyCode = _currencySymbol == null;
        }

        if (useCurrencyCode)
        {
            // set Currency
            try
            {
                format.setCurrency(Currency.getInstance(_currencyCode));
            }
            catch (Exception e)
            {
                throw new ConverterException("Unable to get Currency instance for currencyCode " +
                                             _currencyCode);
            }
        }
        else if (format instanceof DecimalFormat)

        {
            DecimalFormat dFormat = (DecimalFormat)format;
            DecimalFormatSymbols symbols = dFormat.getDecimalFormatSymbols();
            symbols.setCurrencySymbol(_currencySymbol);
            dFormat.setDecimalFormatSymbols(symbols);
        }
    }

    // STATE SAVE/RESTORE
    public void restoreState(FacesContext facesContext, Object state)
    {
        Object values[] = (Object[])state;
        _currencyCode = (String)values[0];
        _currencySymbol = (String)values[1];
        _locale = (Locale)values[2];
        Integer value = (Integer)values[3];
        _maxFractionDigits = value != null ? value.intValue() : 0;
        value = (Integer)values[4];
        _maxIntegerDigits = value != null ? value.intValue() : 0;
        value = (Integer)values[5];
        _minFractionDigits = value != null ? value.intValue() : 0;
        value = (Integer)values[6];
        _minIntegerDigits = value != null ? value.intValue() : 0;
        _pattern = (String)values[7];
        _type = (String)values[8];
        _groupingUsed = ((Boolean)values[9]).booleanValue();
        _integerOnly = ((Boolean)values[10]).booleanValue();
        _maxFractionDigitsSet = ((Boolean)values[11]).booleanValue();
        _maxIntegerDigitsSet = ((Boolean)values[12]).booleanValue();
        _minFractionDigitsSet = ((Boolean)values[13]).booleanValue();
        _minIntegerDigitsSet = ((Boolean)values[14]).booleanValue();
    }

    public Object saveState(FacesContext facesContext)
    {
        Object values[] = new Object[15];
        values[0] = _currencyCode;
        values[1] = _currencySymbol;
        values[2] = _locale;
        values[3] = _maxFractionDigitsSet ? new Integer(_maxFractionDigits) : null;
        values[4] = _maxIntegerDigitsSet ? new Integer(_maxIntegerDigits) : null;
        values[5] = _minFractionDigitsSet ? new Integer(_minFractionDigits) : null;
        values[6] = _minIntegerDigitsSet ? new Integer(_minIntegerDigits) : null;
        values[7] = _pattern;
        values[8] = _type;
        values[9] = _groupingUsed ? Boolean.TRUE : Boolean.FALSE;
        values[10] = _integerOnly ? Boolean.TRUE : Boolean.FALSE;
        values[11] = _maxFractionDigitsSet ? Boolean.TRUE : Boolean.FALSE;
        values[12] = _maxIntegerDigitsSet ? Boolean.TRUE : Boolean.FALSE;
        values[13] = _minFractionDigitsSet ? Boolean.TRUE : Boolean.FALSE;
        values[14] = _minIntegerDigitsSet ? Boolean.TRUE : Boolean.FALSE;
        return values;
    }

    // GETTER & SETTER
    public String getCurrencyCode()
    {
        return _currencyCode != null ?
               _currencyCode :
               getDecimalFormatSymbols().getInternationalCurrencySymbol();
    }

    public void setCurrencyCode(String currencyCode)
    {
        _currencyCode = currencyCode;
    }

    public String getCurrencySymbol()
    {
        return _currencySymbol != null ?
               _currencySymbol :
               getDecimalFormatSymbols().getCurrencySymbol();
    }

    public void setCurrencySymbol(String currencySymbol)
    {
        _currencySymbol = currencySymbol;
    }

    public boolean isGroupingUsed()
    {
        return _groupingUsed;
    }

    public void setGroupingUsed(boolean groupingUsed)
    {
        _groupingUsed = groupingUsed;
    }

    public boolean isIntegerOnly()
    {
        return _integerOnly;
    }

    public void setIntegerOnly(boolean integerOnly)
    {
        _integerOnly = integerOnly;
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

    public int getMaxFractionDigits()
    {
        return _maxFractionDigits;
    }

    public void setMaxFractionDigits(int maxFractionDigits)
    {
        _maxFractionDigitsSet = true;
        _maxFractionDigits = maxFractionDigits;
    }

    public int getMaxIntegerDigits()
    {
        return _maxIntegerDigits;
    }

    public void setMaxIntegerDigits(int maxIntegerDigits)
    {
        _maxIntegerDigitsSet = true;
        _maxIntegerDigits = maxIntegerDigits;
    }

    public int getMinFractionDigits()
    {
        return _minFractionDigits;
    }

    public void setMinFractionDigits(int minFractionDigits)
    {
        _minFractionDigitsSet = true;
        _minFractionDigits = minFractionDigits;
    }

    public int getMinIntegerDigits()
    {
        return _minIntegerDigits;
    }

    public void setMinIntegerDigits(int minIntegerDigits)
    {
        _minIntegerDigitsSet = true;
        _minIntegerDigits = minIntegerDigits;
    }

    public String getPattern()
    {
        return _pattern;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
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
        //TODO: validate type
        _type = type;
    }

    private static boolean checkJavaVersion14()
    {
        String version = System.getProperty("java.version");
        if (version == null)
        {
            return false;
        }
        byte java14 = 0;
        for (int idx = version.indexOf('.'), i = 0; idx > 0 || version != null; i++)
        {
            if (idx > 0)
            {
                byte value = Byte.parseByte(version.substring(0, 1));
                version = version.substring(idx + 1, version.length());
                idx = version.indexOf('.');
                switch (i)
                {
                    case 0:
                        if (value == 1)
                        {
                            java14 = 1;
                            break;
                        }
                        else if (value > 1)
                        {
                            java14 = 2;
                        }
                    case 1:
                        if (java14 > 0 && value >= 4)
                        {
                            java14 = 2;
                        }
                        ;
                    default:
                        idx = 0;
                        version = null;
                        break;
                }
            }
            else
            {
                byte value = Byte.parseByte(version.substring(0, 1));
                if (java14 > 0 && value >= 4)
                {
                    java14 = 2;
                }
                break;
            }
        }
        return java14 == 2;
    }


    public DecimalFormatSymbols getDecimalFormatSymbols()
    {
        return new DecimalFormatSymbols(getLocale());
    }
}

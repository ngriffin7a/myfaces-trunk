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
package net.sourceforge.myfaces.convert;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.component.MyFacesComponent;
import net.sourceforge.myfaces.convert.map.ConverterMapFactory;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;
import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConverterUtils
{
    private static final int DEFAULT_DATE_STYLE = DateFormat.MEDIUM;
    private static final int DEFAULT_TIME_STYLE = DateFormat.MEDIUM;

    private ConverterUtils() {}

    public static Converter getConverter(ServletContext servletContext, Class c)
        throws IllegalArgumentException
    {
        ConverterMapFactory convMapFactory = MyFacesFactoryFinder.getConverterMapFactory(servletContext);
        String converterId = convMapFactory.getConverterMap().getConverterId(c);
        ConverterFactory convFactory = MyFacesFactoryFinder.getConverterFactory(servletContext);
        return convFactory.getConverter(converterId);
    }


    public static Converter getConverter(FacesContext facesContext,
                                         UIComponent uicomponent)
        throws IllegalArgumentException
    {
        String converterId = (String)uicomponent.getAttribute(MyFacesComponent.CONVERTER_ATTR);
        if (converterId != null)
        {
            ConverterFactory convFactory = MyFacesFactoryFinder.getConverterFactory(facesContext.getServletContext());
            return convFactory.getConverter(converterId);
        }

        String modelRef = uicomponent.getModelReference();
        if (modelRef != null)
        {
            Class c = facesContext.getModelType(modelRef);
            ConverterMapFactory convMapFactory = MyFacesFactoryFinder.getConverterMapFactory(facesContext.getServletContext());
            converterId = convMapFactory.getConverterMap().getConverterId(c);
            ConverterFactory convFactory = MyFacesFactoryFinder.getConverterFactory(facesContext.getServletContext());
            return convFactory.getConverter(converterId);
        }

        return null;
    }


    public static Converter findConverter(ServletContext servletContext,
                                          Class c)
    {
        try
        {
            return getConverter(servletContext, c);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }


    public static Converter findConverter(FacesContext facesContext,
                                          UIComponent uicomponent)
    {
        try
        {
            return getConverter(facesContext, uicomponent);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }


    public static Object getAsObject(FacesContext facescontext, UIComponent uicomponent, String value)
        throws ConverterException
    {
        Converter converter = findConverter(facescontext, uicomponent);
        if (converter != null)
        {
            return converter.getAsObject(facescontext, uicomponent, value);
        }
        else
        {
            return value;
        }
    }


    public static String getAsString(FacesContext facescontext, UIComponent component, Object obj)
        throws ConverterException
    {
        if (obj == null)
        {
            return null;
        }

        Converter converter = ConverterUtils.findConverter(facescontext, component);
        if (converter != null)
        {
            return converter.getAsString(facescontext, component, obj);
        }
        else
        {
            if (obj instanceof String)
            {
                return (String)obj;
            }
            else
            {
                throw new IllegalArgumentException("Cannot convert Object to String for component " + component.getCompoundId() + ": No converter found for class " + obj.getClass().getName() + ".");
            }
        }
    }


    public static NumberFormat getNumberFormat(UIComponent component, Locale locale)
    {
        NumberFormat format;
        String numberStyle = (String)component.getAttribute(Converter.NUMBER_STYLE);
        if (numberStyle != null)
        {
            if (numberStyle.equals(Converter.NUMBER_STYLE_CURRENCY))
            {
                format = NumberFormat.getCurrencyInstance(locale);
            }
            else if (numberStyle.equals(Converter.NUMBER_STYLE_INTEGER))
            {
                format = NumberFormat.getIntegerInstance(locale);
            }
            else if (numberStyle.equals(Converter.NUMBER_STYLE_NUMBER))
            {
                format = NumberFormat.getNumberInstance(locale);
            }
            else if (numberStyle.equals(Converter.NUMBER_STYLE_PERCENT))
            {
                format = NumberFormat.getPercentInstance(locale);
            }
            else
            {
                throw new IllegalArgumentException("Unknown number style " + numberStyle);
            }
        }
        else
        {
            String pattern = (String)component.getAttribute(MyFacesComponent.FORMAT_PATTERN_ATTR);
            if (pattern != null)
            {
                format = new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
            }
            else
            {
                format = NumberFormat.getInstance(locale);
            }
        }

        return format;
    }


    public static DateFormat getDateFormat(UIComponent component, Locale locale)
    {
        return DateFormat.getDateInstance(getDateStyle(component), locale);
    }


    public static DateFormat getTimeFormat(UIComponent component, Locale locale)
    {
        return DateFormat.getTimeInstance(getTimeStyle(component), locale);
    }


    public static DateFormat getDateTimeFormat(UIComponent component, Locale locale)
    {
        return DateFormat.getDateTimeInstance(getDateStyle(component),
                                              getTimeStyle(component),
                                              locale);
    }


    private static int getDateStyle(UIComponent component)
    {
        String dateStyle = (String)component.getAttribute(Converter.DATE_STYLE);
        if (dateStyle != null)
        {
            if (dateStyle.equals(Converter.DATE_STYLE_SHORT))
            {
                return DateFormat.SHORT;
            }
            else if (dateStyle.equals(Converter.DATE_STYLE_MEDIUM))
            {
                return DateFormat.MEDIUM;
            }
            else if (dateStyle.equals(Converter.DATE_STYLE_LONG))
            {
                return DateFormat.LONG;
            }
            else if (dateStyle.equals(Converter.DATE_STYLE_FULL))
            {
                return DateFormat.FULL;
            }
            else
            {
                throw new IllegalArgumentException("Unknown number style " + dateStyle);
            }
        }
        return DEFAULT_DATE_STYLE;
    }


    private static int getTimeStyle(UIComponent component)
    {
        String timeStyle = (String)component.getAttribute(Converter.TIME_STYLE);
        if (timeStyle != null)
        {
            if (timeStyle.equals(Converter.TIME_STYLE_SHORT))
            {
                return DateFormat.SHORT;
            }
            else if (timeStyle.equals(Converter.TIME_STYLE_MEDIUM))
            {
                return DateFormat.MEDIUM;
            }
            else if (timeStyle.equals(Converter.TIME_STYLE_LONG))
            {
                return DateFormat.LONG;
            }
            else if (timeStyle.equals(Converter.TIME_STYLE_FULL))
            {
                return DateFormat.FULL;
            }
            else
            {
                throw new IllegalArgumentException("Unknown number style " + timeStyle);
            }
        }
        return DEFAULT_TIME_STYLE;
    }


    private static final String SERIALIZE_CHARSET = "ISO-8859-1";
    private static final String SERIALIZE_ENCODING = "base64";

    public static String serialize(Object obj)
        throws FacesException
    {
        /*
        String encodeEol = System.getProperty("mail.mime.encodeeol.strict");
        System.out.println(encodeEol);
        System.setProperty("mail.mime.encodeeol.strict", "true");
        */
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream encStream = MimeUtility.encode(baos, SERIALIZE_ENCODING);
            ObjectOutputStream oos = new ObjectOutputStream(encStream);
            oos.writeObject(obj);
            oos.close();
            encStream.close();
            baos.close();
            return baos.toString(SERIALIZE_CHARSET);
        }
        catch (MessagingException e)
        {
            e.printStackTrace(System.err);
            throw new FacesException(e);
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            throw new FacesException(e);
        }
        finally
        {
            /*
            System.setProperty("mail.mime.encodeeol.strict",
                               encodeEol != null ? encodeEol : "false");
                               */
        }
    }

    public static Object deserialize(String value)
        throws FacesException
    {
        //System.out.println("deserialize:" + value);

        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(value.getBytes(SERIALIZE_CHARSET));
            InputStream decStream = MimeUtility.decode(bais, SERIALIZE_ENCODING);
            ObjectInputStream ois = new ObjectInputStream(decStream);
            Object obj = ois.readObject();
            ois.close();
            decStream.close();
            bais.close();
            return obj;
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace(System.err);
            throw new FacesException(e);
        }
        catch (MessagingException e)
        {
            e.printStackTrace(System.err);
            throw new FacesException(e);
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            throw new FacesException(e);
        }
    }

}

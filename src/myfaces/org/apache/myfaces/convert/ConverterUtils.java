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
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.config.*;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.util.Base64;
import net.sourceforge.myfaces.util.MyFacesObjectInputStream;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletContext;
import java.io.*;
import java.text.*;
import java.util.Locale;
import java.util.TimeZone;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConverterUtils
{
    private static final int DEFAULT_DATE_STYLE = DateFormat.SHORT;
    private static final int DEFAULT_TIME_STYLE = DateFormat.SHORT;

    private ConverterUtils() {}

    public static Converter getConverter(Class c)
        throws IllegalArgumentException
    {
        return getConverter(c.getName());
    }

    public static Converter findConverter(Class c)
    {
        return findConverter(c.getName());
    }

    public static Converter getConverter(String converterId)
        throws IllegalArgumentException
    {
        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        try
        {
            return af.getApplication().getConverter(converterId);
        }
        catch (FacesException e)
        {
            throw new IllegalArgumentException("Converter with id " + converterId + " not found.");
        }
    }

    public static Converter findConverter(String converterId)
    {
        try
        {
            return getConverter(converterId);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }



    /**
     * Returns the converter that is responsible for converting the value
     * of the given UIOutput.
     * The converter is determined as follows:
     * <ul>
     *  <li> if there is a "converter" attribute or the converter property is not null, return the corresponding converter
     *  <li> if the component is a UIOutput and valueRef is not null, find the value type via
     *       {@link javax.faces.el.ValueBinding#getType} and return the converter
     *       for this type
     *  <li> otherwise return null
     * </ul>
     *
     * @param facesContext
     * @param uiOutput
     * @return
     * @throws IllegalArgumentException if a converter for this value type cannot be found
     */
    public static Converter getValueConverter(FacesContext facesContext,
                                              UIOutput uiOutput)
        throws IllegalArgumentException
    {
        Object converterAttr = uiOutput.getAttribute(CommonRendererAttributes.CONVERTER_ATTR);
        if (converterAttr != null && converterAttr instanceof Converter)
        {
            //JSF 1.0 PRD2, 8.4
            //A component's "converter" attribute can also be a Converter instance.
            return (Converter)converterAttr;
        }

        String converterId;
        if (converterAttr != null)
        {
            //If "converter" attribute is not a Converter instance it must be a converter id
            converterId = (String)converterAttr;
        }
        else
        {
            converterId = uiOutput.getConverter();
        }

        if (converterId != null)
        {
            ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            return af.getApplication().getConverter(converterId);
        }

        String valueRef = uiOutput.getValueRef();
        if (valueRef != null)
        {
            ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            Class c = af.getApplication().getValueBinding(valueRef).getType(facesContext);
            return af.getApplication().getConverter(c.getName());
        }

        return null;
    }


    /**
     * Same as {@link #getValueConverter} but never throws IllegalArgumentException.
     *
     * @param facesContext
     * @param uiOutput
     * @return
     */
    public static Converter findValueConverter(FacesContext facesContext,
                                               UIOutput uiOutput)
    {
        try
        {
            return getValueConverter(facesContext, uiOutput);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }


    /**
     * Returns the converter that can be used to convert this attribute
     * of the given UIComponent.
     * @param facesContext
     * @param uiComponent
     * @param attrName
     * @return the proper Converter or null if an attribute descriptor could
     *         not be found
     */
    public static Converter findAttributeConverter(FacesContext facesContext,
                                                   UIComponent uiComponent,
                                                   String attrName)
    {
        String rendererType = uiComponent.getRendererType();
        if (rendererType == null)
        {
            //Each component must have a rendererType since Spec. 1.0 PRD2
            LogUtil.getLogger().warning("Component " + UIComponentUtils.toString(uiComponent) + " has no renderer type!");
            return null;
        }

        //Lookup the attribute descriptor
        ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();
        FacesConfigFactory fcf = MyFacesFactoryFinder.getFacesConfigFactory(servletContext);
        FacesConfig facesConfig = fcf.getFacesConfig(servletContext);
        RenderKitConfig rkc = facesConfig.getRenderKitConfig(facesContext.getTree().getRenderKitId());
        RendererConfig rc = rkc.getRendererConfig(rendererType);
        AttributeConfig ac = rc.getAttributeConfig(attrName);
        if (ac == null)
        {
            LogUtil.getLogger().info("Could not find an attribute descriptor for attribute '" + attrName + "' of component " + UIComponentUtils.toString(uiComponent) + ".");
            return null;
        }
        return findConverter(ac.getAttributeClass());
    }


    /**
     * Tries to find a proper converter for the value attribute of this
     * component. The found converter is then used to convert the given String
     * to Object.
     * @throws ConverterException
     */
    public static Object getComponentValueAsObject(FacesContext facescontext,
                                                   UIOutput uiOutput,
                                                   String value)
        throws ConverterException
    {
        Converter converter = findValueConverter(facescontext, uiOutput);
        if (converter != null)
        {
            return converter.getAsObject(facescontext, uiOutput, value);
        }
        else
        {
            return value;
        }
    }


    /**
     * Tries to find a proper converter for the value attribute of this
     * component. The found converter is then used to convert the given Object
     * to String.
     * @throws ConverterException
     */
    public static String getComponentValueAsString(FacesContext facesContext,
                                                   UIOutput uiOutput,
                                                   Object obj)
        throws ConverterException
    {
        if (obj == null)
        {
            return null;
        }

        //already a String?
        if (obj instanceof String)
        {
            return (String)obj;
        }

        //Can we find a "real" value converter
        Converter converter = findValueConverter(facesContext, uiOutput);
        if (converter != null)
        {
            return converter.getAsString(facesContext, uiOutput, obj);
        }

        //Let's try to find a proper converter for this class
        converter = findConverter(obj.getClass());
        if (converter != null)
        {
            return converter.getAsString(facesContext, uiOutput, obj);
        }

        //As the last resort we do a toString
        LogUtil.getLogger().warning("Cannot convert Object to String for component " + UIComponentUtils.toString(uiOutput) + ": No converter found for class " + obj.getClass().getName() + ".");
        return obj.toString();
    }


    public static NumberFormat getNumberFormat(UIComponent component, Locale locale)
    {
        NumberFormat format;
        String numberStyle = (String)component.getAttribute(CommonRendererAttributes.NUMBER_STYLE_ATTR);
        if (numberStyle != null)
        {
            if (numberStyle.equalsIgnoreCase(ConverterConstants.NUMBER_STYLE_CURRENCY))
            {
                format = NumberFormat.getCurrencyInstance(locale);
            }
            else if (numberStyle.equalsIgnoreCase(ConverterConstants.NUMBER_STYLE_INTEGER))
            {
                format = NumberFormat.getIntegerInstance(locale);
            }
            else if (numberStyle.equalsIgnoreCase(ConverterConstants.NUMBER_STYLE_NUMBER))
            {
                format = NumberFormat.getNumberInstance(locale);
            }
            else if (numberStyle.equalsIgnoreCase(ConverterConstants.NUMBER_STYLE_PERCENT))
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
            String pattern = (String)component.getAttribute(CommonRendererAttributes.FORMAT_PATTERN_ATTR);
            if (pattern != null)
            {
                format = new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
            }
            else
            {
                format = NumberFormat.getIntegerInstance(locale);
            }
        }

        return format;
    }


    public static DateFormat getDateFormat(UIComponent component, Locale locale)
    {
        DateFormat format;
        String pattern = (String)component.getAttribute(CommonRendererAttributes.FORMAT_PATTERN_ATTR);
        if (pattern != null)
        {
            format = new SimpleDateFormat(pattern, locale);
        }
        else
        {
            format = DateFormat.getDateInstance(getDateStyle(component), locale);
        }
        format.setTimeZone(getTimeZone(component));
        return format;
    }


    public static DateFormat getTimeFormat(UIComponent component, Locale locale)
    {
        DateFormat format;
        String pattern = (String)component.getAttribute(CommonRendererAttributes.FORMAT_PATTERN_ATTR);
        if (pattern != null)
        {
            format = new SimpleDateFormat(pattern, locale);
        }
        else
        {
            format = DateFormat.getTimeInstance(getTimeStyle(component), locale);
        }
        format.setTimeZone(getTimeZone(component));
        return format;
    }

    public static DateFormat getDateTimeFormat(UIComponent component, Locale locale)
    {
        DateFormat format;
        String pattern = (String)component.getAttribute(CommonRendererAttributes.FORMAT_PATTERN_ATTR);
        if (pattern != null)
        {
            format = new SimpleDateFormat(pattern, locale);
        }
        else
        {
            format = DateFormat.getDateTimeInstance(getDateStyle(component),
                                                    getTimeStyle(component),
                                                    locale);
        }
        format.setTimeZone(getTimeZone(component));
        return format;
    }

    private static int getDateStyle(UIComponent component)
    {
        String dateStyle = (String)component.getAttribute(CommonRendererAttributes.DATE_STYLE_ATTR);
        if (dateStyle != null)
        {
            if (dateStyle.equalsIgnoreCase(ConverterConstants.DATE_STYLE_SHORT))
            {
                return DateFormat.SHORT;
            }
            else if (dateStyle.equalsIgnoreCase(ConverterConstants.DATE_STYLE_MEDIUM))
            {
                return DateFormat.MEDIUM;
            }
            else if (dateStyle.equalsIgnoreCase(ConverterConstants.DATE_STYLE_LONG))
            {
                return DateFormat.LONG;
            }
            else if (dateStyle.equalsIgnoreCase(ConverterConstants.DATE_STYLE_FULL))
            {
                return DateFormat.FULL;
            }
            else
            {
                throw new IllegalArgumentException("Unknown date style " + dateStyle);
            }
        }
        return DEFAULT_DATE_STYLE;
    }


    private static int getTimeStyle(UIComponent component)
    {
        String timeStyle = (String)component.getAttribute(CommonRendererAttributes.TIME_STYLE_ATTR);
        if (timeStyle != null)
        {
            if (timeStyle.equalsIgnoreCase(ConverterConstants.TIME_STYLE_SHORT))
            {
                return DateFormat.SHORT;
            }
            else if (timeStyle.equalsIgnoreCase(ConverterConstants.TIME_STYLE_MEDIUM))
            {
                return DateFormat.MEDIUM;
            }
            else if (timeStyle.equalsIgnoreCase(ConverterConstants.TIME_STYLE_LONG))
            {
                return DateFormat.LONG;
            }
            else if (timeStyle.equalsIgnoreCase(ConverterConstants.TIME_STYLE_FULL))
            {
                return DateFormat.FULL;
            }
            else
            {
                throw new IllegalArgumentException("Unknown time style " + timeStyle);
            }
        }
        return DEFAULT_TIME_STYLE;
    }

    public static TimeZone getTimeZone(UIComponent component)
    {
        String tzId = (String)component.getAttribute(CommonRendererAttributes.TIMEZONE_ATTR);
        if (tzId == null)
        {
            return TimeZone.getDefault();
        }
        else
        {
            return TimeZone.getTimeZone(tzId);
        }
    }



    private static final String SERIALIZE_CHARSET = "ISO-8859-1";

    public static String serializeAndEncodeBase64(Object obj)
        throws FacesException
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream encStream = Base64.getEncoder(baos);
            ObjectOutputStream oos = new ObjectOutputStream(encStream);
            oos.writeObject(obj);
            oos.close();
            encStream.close();
            baos.close();
            return baos.toString(SERIALIZE_CHARSET);
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

    public static Object deserializeAndDecodeBase64(String value)
        throws FacesException
    {
        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(value.getBytes(SERIALIZE_CHARSET));
            InputStream decStream = Base64.getDecoder(bais);
            ObjectInputStream ois = new MyFacesObjectInputStream(decStream);
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
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            throw new FacesException(e);
        }
    }

}

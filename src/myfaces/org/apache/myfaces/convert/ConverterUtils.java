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

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.util.Base64;
import net.sourceforge.myfaces.util.bean.BeanUtils;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.ConverterFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ConverterUtils
{
    private static final int DEFAULT_DATE_STYLE = DateFormat.MEDIUM;
    private static final int DEFAULT_TIME_STYLE = DateFormat.MEDIUM;

    private ConverterUtils() {}

    public static Converter getConverter(Class c)
        throws IllegalArgumentException
    {
        ConverterFactory convFactory = (ConverterFactory)FactoryFinder.getFactory(FactoryFinder.CONVERTER_FACTORY);
        return convFactory.getConverter(c);
    }

    public static Converter findConverter(Class c)
    {
        try
        {
            return getConverter(c);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }




    /**
     * Returns the converter that is responsible for converting the value
     * of the given UIComponent.
     * The converter is determined as follows:
     * <ul>
     *  <li> if there is a "converter" attribute, return the corresponding converter
     *  <li> if there is a "modelReference" attribute, find the value type via
     *       {@link FacesContext#getModelType(String)} and return the converter
     *       for this type
     *  <li> otherwise return null
     * </ul>
     *
     * @param facesContext
     * @param uicomponent
     * @return
     * @throws IllegalArgumentException if a converter for this value type cannot be found
     */
    public static Converter getValueConverter(FacesContext facesContext,
                                              UIComponent uicomponent)
        throws IllegalArgumentException
    {
        Object converterAttr = uicomponent.getAttribute(CommonRendererAttributes.CONVERTER_ATTR);
        if (converterAttr != null && converterAttr instanceof Converter)
        {
            return (Converter)converterAttr;
        }

        String converterId = (String)converterAttr;
        if (converterId != null)
        {
            ConverterFactory convFactory = (ConverterFactory)FactoryFinder.getFactory(FactoryFinder.CONVERTER_FACTORY);
            return convFactory.getConverter(converterId);
        }

        String modelRef = uicomponent.getModelReference();
        if (modelRef != null)
        {
            Class c = facesContext.getModelType(modelRef);
            if (c == null)
            {
                throw new IllegalArgumentException("Could not find ModelDefinition for ModelReference " + modelRef);
            }
            ConverterFactory convFactory = (ConverterFactory)FactoryFinder.getFactory(FactoryFinder.CONVERTER_FACTORY);
            return convFactory.getConverter(c);
        }

        return null;
    }


    /**
     * Same as {@link #getValueConverter} but never throws IllegalArgumentException.
     *
     * @param facesContext
     * @param uicomponent
     * @return
     */
    public static Converter findValueConverter(FacesContext facesContext,
                                               UIComponent uicomponent)
    {
        try
        {
            return getValueConverter(facesContext, uicomponent);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }


    /**
     * Returns the converter that can be used to convert this attribute
     * of the given UIComponent.
     * The converter is determined as follows:
     * <ul>
     *  <li> if the component has a bean property getter for this attribute,
     *       return the converter for this property type
     *  <li> if the component has a rendererType, get the Renderer and
     *       try to find an AttributeDescriptor. If found, return the converter
     *       for this attribute's type
     *  <li> otherwise return null
     * </ul>
     *
     * @param facesContext
     * @param uiComponent
     * @param attrName
     * @return
     */
    public static Converter findAttributeConverter(FacesContext facesContext,
                                                   UIComponent uiComponent,
                                                   String attrName)
    {
        Converter conv = null;
        try
        {
            Class c = BeanUtils.getBeanPropertyType(uiComponent, attrName);
            if (c != null)
            {
                conv = ConverterUtils.findConverter(c);
            }
        }
        catch (IllegalArgumentException e)
        {
            //probably not a component attribute but a render dependent attribute
            String rendererType = uiComponent.getRendererType();
            if (rendererType != null)
            {
                //Lookup the attribute descriptor
                RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
                RenderKit renderKit = rkFactory.getRenderKit(facesContext.getTree().getRenderKitId());
                Renderer renderer = renderKit.getRenderer(rendererType);
                AttributeDescriptor attrDescr = renderer.getAttributeDescriptor(uiComponent.getComponentType(),
                                                                                attrName);
                if (attrDescr != null)
                {
                    conv = ConverterUtils.findConverter(attrDescr.getType());
                }
                else
                {
                    LogUtil.getLogger().info("Could not find an attribute descriptor for attribute '" + attrName + "' of component " + UIComponentUtils.toString(uiComponent) + ".");
                }
            }
            else
            {
                LogUtil.getLogger().info("Component " + UIComponentUtils.toString(uiComponent) + " has no bean getter method for attribute '" + attrName + "'.");
            }
        }
        return conv;
    }


    public static Object getAsObject(FacesContext facescontext, UIComponent uicomponent, String value)
        throws ConverterException
    {
        Converter converter = findValueConverter(facescontext, uicomponent);
        if (converter != null)
        {
            return converter.getAsObject(facescontext, uicomponent, value);
        }
        else
        {
            return value;
        }
    }


    public static String getAsString(FacesContext facesContext, UIComponent uiComponent, Object obj)
        throws ConverterException
    {
        if (obj == null)
        {
            return null;
        }

        Converter converter = ConverterUtils.findValueConverter(facesContext, uiComponent);
        if (converter != null)
        {
            return converter.getAsString(facesContext, uiComponent, obj);
        }
        else
        {
            if (obj instanceof String)
            {
                return (String)obj;
            }
            else
            {
                throw new IllegalArgumentException("Cannot convert Object to String for component " + UIComponentUtils.toString(uiComponent) + ": No converter found for class " + obj.getClass().getName() + ".");
            }
        }
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
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            throw new FacesException(e);
        }
    }

}

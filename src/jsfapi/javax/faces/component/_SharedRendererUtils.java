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
package javax.faces.component;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * The util methods in this class are shared between the javax.faces.component package and
 * the net.sourceforge.myfaces.renderkit package.
 * Please note: Any changes here must also apply to the class in the other package!
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/04/06 13:03:35  manolito
 * x-checked getConvertedValue method in api and impl
 *
 */
class _SharedRendererUtils
{
    static Converter findUIOutputConverter(FacesContext facesContext, UIOutput component)
    {
        // Attention!
        // This code is duplicated in myfaces implementation renderkit package.
        // If you change something here please do the same in the other class!

        Converter converter = component.getConverter();
        if (converter != null) return converter;

        //Try to find out by value binding
        ValueBinding vb = component.getValueBinding("value");
        if (vb == null) return null;

        Class valueType = vb.getType(facesContext);
        if (valueType == null) return null;

        if (String.class.equals(valueType)) return null;    //No converter needed for String type
        if (Object.class.equals(valueType)) return null;    //There is no converter for Object class

        try
        {
            return facesContext.getApplication().createConverter(valueType);
        }
        catch (FacesException e)
        {
            log(facesContext, "No Converter for type " + valueType.getName() + " found", e);
            return null;
        }
    }

    static Object getConvertedUISelectManyValue(FacesContext facesContext,
                                                UISelectMany component,
                                                String[] submittedValue)
            throws ConverterException
    {
        // Attention!
        // This code is duplicated in myfaces implementation renderkit package.
        // If you change something here please do the same in the other class!

        ValueBinding vb = component.getValueBinding("value");
        Class valueType = null;
        Class arrayComponentType = null;
        if (vb != null)
        {
            valueType = vb.getType(facesContext);
            if (valueType != null && valueType.isArray())
            {
                arrayComponentType = valueType.getComponentType();
            }
        }

        Converter converter = component.getConverter();
        if (converter == null)
        {
            if (valueType == null)
            {
                // No converter, and no idea of expected type
                // --> return the submitted String array
                return submittedValue;
            }

            if (List.class.isAssignableFrom(valueType))
            {
                // expected type is a List
                // --> according to javadoc of UISelectMany we assume that the element type
                //     is java.lang.String, and copy the String array to a new List
                int len = submittedValue.length;
                List lst = new ArrayList(len);
                for (int i = 0; i < len; i++)
                {
                    lst.add(submittedValue[i]);
                }
                return lst;
            }

            if (arrayComponentType == null)
            {
                throw new IllegalArgumentException("ValueBinding for UISelectMany must be of type List or Array");
            }

            if (String.class.equals(arrayComponentType)) return submittedValue; //No conversion needed for String type
            if (Object.class.equals(arrayComponentType)) return submittedValue; //No conversion for Object class

            try
            {
                converter = facesContext.getApplication().createConverter(arrayComponentType);
            }
            catch (FacesException e)
            {
                log(facesContext, "No Converter for type " + arrayComponentType.getName() + " found", e);
                return submittedValue;
            }
        }

        // Now, we have a converter...
        if (valueType == null)
        {
            // ...but have no idea of expected type
            // --> so let's convert it to an Object array
            int len = submittedValue.length;
            Object[] convertedValues = new Object[len];
            for (int i = 0; i < len; i++)
            {
                convertedValues[i]
                    = converter.getAsObject(facesContext, component, submittedValue[i]);
            }
            return convertedValues;
        }

        if (List.class.isAssignableFrom(valueType))
        {
            // Curious case: According to specs we should assume, that the element type
            // of this List is java.lang.String. But there is a Converter set for this
            // component. Because the user must know what he is doing, we will convert the values.
            int len = submittedValue.length;
            List lst = new ArrayList(len);
            for (int i = 0; i < len; i++)
            {
                lst.add(converter.getAsObject(facesContext, component, submittedValue[i]));
            }
            return lst;
        }

        if (arrayComponentType == null)
        {
            throw new IllegalArgumentException("ValueBinding for UISelectMany must be of type List or Array");
        }

        if (arrayComponentType.isPrimitive())
        {
            //primitive array
            int len = submittedValue.length;
            Object convertedValues = Array.newInstance(arrayComponentType, len);
            for (int i = 0; i < len; i++)
            {
                Array.set(convertedValues, i,
                          converter.getAsObject(facesContext, component, submittedValue[i]));
            }
            return convertedValues;
        }
        else
        {
            //Object array
            int len = submittedValue.length;
            Object[] convertedValues = new Object[len];
            for (int i = 0; i < len; i++)
            {
                convertedValues[i]
                    = converter.getAsObject(facesContext, component, submittedValue[i]);
            }
            return convertedValues;
        }
    }



    /**
     * This method is different in the two versions of _SharedRendererUtils.
     */
    private static void log(FacesContext context, String msg, Exception e)
    {
        context.getExternalContext().log(msg, e);
    }
}

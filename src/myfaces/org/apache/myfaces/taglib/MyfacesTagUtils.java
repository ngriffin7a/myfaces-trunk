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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.ConvertibleValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyfacesTagUtils
{
    private static final Log log = LogFactory.getLog(MyfacesTagUtils.class);

    private MyfacesTagUtils() {}    //util class, no instantiation allowed


    public static boolean isValueReference(String v)
    {
        return UIComponentTag.isValueReference(v);
    }


    public static void setIntegerProperty(FacesContext context,
                                          UIComponent component,
                                          String propName,
                                          String value)
    {
        if (value != null)
        {
            if (isValueReference(value))
            {
                ValueBinding vb = context.getApplication().createValueBinding(value);
                component.setValueBinding(propName, vb);
            }
            else
            {
                //FIXME: should use converter maybe?
                component.getAttributes().put(propName, Integer.valueOf(value));
            }
        }
    }

    public static void setStringProperty(FacesContext context,
                                     UIComponent component,
                                     String propName,
                                     String value)
    {
        if (value != null)
        {
            if (isValueReference(value))
            {
                ValueBinding vb = context.getApplication().createValueBinding(value);
                component.setValueBinding(propName, vb);
            }
            else
            {
                component.getAttributes().put(propName, value);
            }
        }
    }


    public static void setBooleanProperty(FacesContext context,
                                      UIComponent component,
                                      String propName,
                                      String value)
    {
        if (value != null)
        {
            if (isValueReference(value))
            {
                ValueBinding vb = context.getApplication().createValueBinding(value);
                component.setValueBinding(propName, vb);
            }
            else
            {
                //TODO: More sophisticated way to convert boolean value (yes/no, 1/0, on/off, etc.)
                component.getAttributes().put(propName, Boolean.valueOf(value));
            }
        }
    }


    public static void setValueProperty(FacesContext context,
                                        UIComponent component,
                                        String value)
    {
        if (value != null)
        {
            if (component instanceof ValueHolder)
            {
                if (isValueReference(value))
                {
                    ValueBinding vb = context.getApplication().createValueBinding(value);
                    component.setValueBinding(JSFAttr.VALUE_ATTR, vb);
                }
                else
                {
                    ((ValueHolder)component).setValue(value);
                }
            }
            else
            {
                log.error("Component " + component.getClass().getName() + " is no ValueHolder, cannot set value.");
            }
        }
    }


    public static void setConverterIdProperty(FacesContext context,
                                        UIComponent component,
                                        String value)
    {
        if (value != null)
        {
            if (component instanceof ConvertibleValueHolder)
            {
                if (isValueReference(value))
                {
                    ValueBinding vb = context.getApplication().createValueBinding(value);
                    component.setValueBinding(JSFAttr.CONVERTER_ATTR, vb);
                }
                else
                {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    Converter converter = facesContext.getApplication().createConverter(value);
                    ((ConvertibleValueHolder)component).setConverter(converter);
                }
            }
            else
            {
                log.error("Component " + component.getClass().getName() + " is no ValueHolder, cannot set value.");
            }
        }
    }


    public static void setValueBinding(FacesContext context,
                                       UIComponent component,
                                       String propName,
                                       String value)
    {
        if (value != null)
        {
            if (isValueReference(value))
            {
                ValueBinding vb = context.getApplication().createValueBinding(value);
                component.setValueBinding(propName, vb);
            }
            else
            {
                throw new IllegalArgumentException("Attribute " + propName + " must be a value reference");
            }
        }
    }

}

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

import net.sourceforge.myfaces.el.SimpleActionMethodBinding;
import net.sourceforge.myfaces.renderkit.JSFAttr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.webapp.UIComponentTag;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/04/16 15:13:33  manolito
 * validator attribute support and MethodBinding invoke exception handling fixed
 *
 * Revision 1.2  2004/03/30 12:16:08  manolito
 * header comments
 *
 */
public class UIComponentTagUtils
{
    private static final Log log = LogFactory.getLog(UIComponentTagUtils.class);

    private static final Class[] VALIDATOR_ARGS = {FacesContext.class,
                                                   UIComponent.class,
                                                   Object.class};
    private static final Class[] ACTION_LISTENER_ARGS = {ActionEvent.class};
    private static final Class[] VALUE_LISTENER_ARGS = {ValueChangeEvent.class};

    private UIComponentTagUtils() {}    //util class, no instantiation allowed


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
                //TODO: Warning if component has no such property (with reflection)
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
            if (isValueReference(value))
            {
                ValueBinding vb = context.getApplication().createValueBinding(value);
                component.setValueBinding(JSFAttr.VALUE_ATTR, vb);
            }
            else if (component instanceof ValueHolder)
            {
                ((ValueHolder)component).setValue(value);
            }
            else if (component instanceof UICommand)
            {
                ((UICommand)component).setValue(value);
            }
            else if (component instanceof UIParameter)
            {
                ((UIParameter)component).setValue(value);
            }
            else if (component instanceof UISelectBoolean)
            {
                ((UISelectBoolean)component).setValue(Boolean.valueOf(value));
            }
            else
            {
                log.error("Component " + component.getClass().getName() + " is no ValueHolder, cannot set value.");
            }
        }
    }


    public static void setConverterProperty(FacesContext context,
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
                    component.setValueBinding(JSFAttr.CONVERTER_ATTR, vb);
                }
                else
                {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    Converter converter = facesContext.getApplication().createConverter(value);
                    ((ValueHolder)component).setConverter(converter);
                }
            }
            else
            {
                log.error("Component " + component.getClass().getName() + " is no ValueHolder, cannot set value.");
            }
        }
    }


    public static void setValidatorProperty(FacesContext context,
                                            UIComponent component,
                                            String validator)
    {
        if (validator != null)
        {
            if (!(component instanceof EditableValueHolder))
            {
                throw new IllegalArgumentException("Component " + component.getClientId(context) + " is no EditableValueHolder");
            }
            if (isValueReference(validator))
            {
                MethodBinding mb = context.getApplication().createMethodBinding(validator,
                                                                                VALIDATOR_ARGS);
                ((EditableValueHolder)component).setValidator(mb);
            }
            else
            {
                log.error("Invalid expression " + validator);
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

    public static void setActionProperty(FacesContext context,
                                         UIComponent component,
                                         String action)
    {
        if (action != null)
        {
            if (!(component instanceof UICommand))
            {
                throw new IllegalArgumentException("Component " + component.getClientId(context) + " is no UICommand");
            }
            MethodBinding mb;
            if (isValueReference(action))
            {
                mb = context.getApplication().createMethodBinding(action, null);
            }
            else
            {
                mb = new SimpleActionMethodBinding(action);
            }
            ((UICommand)component).setAction(mb);
        }
    }

    public static void setActionListenerProperty(FacesContext context,
                                                 UIComponent component,
                                                 String actionListener)
    {
        if (actionListener != null)
        {
            if (!(component instanceof ActionSource))
            {
                throw new IllegalArgumentException("Component " + component.getClientId(context) + " is no ActionSource");
            }
            if (isValueReference(actionListener))
            {
                MethodBinding mb = context.getApplication().createMethodBinding(actionListener,
                                                                                ACTION_LISTENER_ARGS);
                ((ActionSource)component).setActionListener(mb);
            }
            else
            {
                log.error("Invalid expression " + actionListener);
            }
        }
    }

    public static void setValueChangedListenerProperty(FacesContext context,
                                                       UIComponent component,
                                                       String valueChangedListener)
    {
        if (valueChangedListener != null)
        {
            if (!(component instanceof EditableValueHolder))
            {
                throw new IllegalArgumentException("Component " + component.getClientId(context) + " is no EditableValueHolder");
            }
            if (isValueReference(valueChangedListener))
            {
                MethodBinding mb = context.getApplication().createMethodBinding(valueChangedListener,
                                                                                VALUE_LISTENER_ARGS);
                ((EditableValueHolder)component).setValueChangeListener(mb);
            }
            else
            {
                log.error("Invalid expression " + valueChangedListener);
            }
        }
    }

}

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
package net.sourceforge.myfaces.event;

import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UpdateActionListener
        implements ActionListener, ValueHolder, StateHolder
{
    //private static final Log log = LogFactory.getLog(UpdateActionListener.class);

    private ValueBinding _propertyBinding;
    private Object _value;
    private ValueBinding _valueBinding;
    private Converter _converter;

    public void setPropertyBinding(ValueBinding propertyBinding)
    {
        _propertyBinding = propertyBinding;
    }

    public ValueBinding getPropertyBinding()
    {
        return _propertyBinding;
    }

    public void setValue(Object value)
    {
        _value = value;
    }

    public Object getValue()
    {
        if (_value != null) return _value;
        ValueBinding vb = getValueBinding();
        if (vb != null)
        {
            FacesContext context = FacesContext.getCurrentInstance();
            return vb.getValue(context);
        }
        return null;
    }

    public Object getLocalValue()
    {
        return _value;
    }

    public ValueBinding getValueBinding()
    {
        return _valueBinding;
    }

    public void setValueBinding(ValueBinding valueBinding)
    {
        _valueBinding = valueBinding;
    }

    public Converter getConverter()
    {
        return _converter;
    }

    public void setConverter(Converter converter)
    {
        _converter = converter;
    }

    public void processAction(ActionEvent actionEvent) throws AbortProcessingException
    {
        FacesContext context = FacesContext.getCurrentInstance();
        ValueBinding updateBinding = getPropertyBinding();
        Object v = getValue();
        if (v != null &&
            v instanceof String)
        {
            Class type = updateBinding.getType(context);
            if (!type.equals(String.class))
            {
                Converter converter = getConverter();
                if (converter == null)
                {
                    try
                    {
                        converter = context.getApplication().createConverter(type);
                    }
                    catch (Exception e)
                    {
                        throw new FacesException("No Converter registered with UpdateActionListener and no appropriate standard converter found. Needed to convert String to " + type.getName(), e);
                    }
                }
                v = converter.getAsObject(context, context.getViewRoot(), (String)v);
            }
        }
        updateBinding.setValue(context, v);
    }



    // StateHolder methods

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = UIComponentBase.saveAttachedState(context, _propertyBinding);
        values[1] = _value;
        values[2] = UIComponentBase.saveAttachedState(context, _valueBinding);
        values[3] = UIComponentBase.saveAttachedState(context, _converter);
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        _propertyBinding = (ValueBinding)UIComponentBase.restoreAttachedState(context, values[0]);
        _value = values[1];
        _valueBinding = (ValueBinding)UIComponentBase.restoreAttachedState(context, values[2]);;
        _converter = (Converter)UIComponentBase.restoreAttachedState(context, values[3]);;
    }

    public boolean isTransient()
    {
        return false;
    }

    public void setTransient(boolean newTransientValue)
    {
        if (newTransientValue == true) throw new IllegalArgumentException();
    }

}

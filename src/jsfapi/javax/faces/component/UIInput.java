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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;
import java.util.ArrayList;
import java.util.List;

/**
 * see Javadoc of JSF Specification
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.9  2004/04/05 15:25:46  manolito
 * no message
 *
 */
public class UIInput
        extends UIOutput
        implements EditableValueHolder
{
    public static final String CONVERSION_MESSAGE_ID = "javax.faces.component.UIInput.CONVERSION";
    public static final String REQUIRED_MESSAGE_ID = "javax.faces.component.UIInput.REQUIRED";

    private static final Validator[] EMPTY_VALIDATOR_ARRAY = new Validator[0];

    private Object _submittedValue = null;
    private boolean _localValueSet = false;
    private boolean _valid = true;
    private MethodBinding _validator = null;
    private MethodBinding _valueChangeListener = null;
    private List _validatorList = null;

    public Object getSubmittedValue()
    {
        return _submittedValue;
    }

    public void setSubmittedValue(Object submittedValue)
    {
        _submittedValue = submittedValue;
    }

    public void setValue(Object value)
    {
        setLocalValueSet(true);
        super.setValue(value);
    }

    public boolean isLocalValueSet()
    {
        return _localValueSet;
    }

    public void setLocalValueSet(boolean localValueSet)
    {
        _localValueSet = localValueSet;
    }

    public boolean isValid()
    {
        return _valid;
    }

    public void setValid(boolean valid)
    {
        _valid = valid;
    }

    public MethodBinding getValidator()
    {
        return _validator;
    }

    public void setValidator(MethodBinding validator)
    {
        _validator = validator;
    }

    public MethodBinding getValueChangeListener()
    {
        return _valueChangeListener;
    }

    public void setValueChangeListener(MethodBinding valueChangeListener)
    {
        _valueChangeListener = valueChangeListener;
    }

    public void processDecodes(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;
        super.processDecodes(context);
        if (isImmediate())
        {
            try
            {
                validate(context);
            }
            catch (RuntimeException e)
            {
                context.renderResponse();
                throw e;
            }
            if (!isValid())
            {
                context.renderResponse();
            }
        }
    }

    public void processValidators(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;
        super.processValidators(context);
        if (!isImmediate())
        {
            try
            {
                validate(context);
            }
            catch (RuntimeException e)
            {
                context.renderResponse();
                throw e;
            }
            if (!isValid())
            {
                context.renderResponse();
            }
        }
    }

    public void processUpdates(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;
        super.processUpdates(context);
        try
        {
            updateModel(context);
        }
        catch (RuntimeException e)
        {
            context.renderResponse();
            throw e;
        }
        if (!isValid())
        {
            context.renderResponse();
        }
    }

    public void decode(FacesContext context)
    {
        super.decode(context);
    }

    public void broadcast(FacesEvent event)
            throws AbortProcessingException
    {
        if (!(event instanceof ValueChangeEvent))
        {
            throw new IllegalArgumentException("FacesEvent of class " + event.getClass().getName() + " not supported by UIInput");
        }

        super.broadcast(event);

        MethodBinding valueChangeListenerBinding = getValueChangeListener();
        if (valueChangeListenerBinding != null)
        {
            valueChangeListenerBinding.invoke(getFacesContext(),
                                              new Object[]{event});
        }
    }

    public void updateModel(FacesContext context)
    {
        if (!isValid()) return;
        if (!isLocalValueSet()) return;
        ValueBinding vb = getValueBinding("value");
        if (vb == null) return;
        try
        {
            vb.setValue(context, getLocalValue());
            setValue(null);
            setLocalValueSet(false);
        }
        catch (RuntimeException e)
        {
            _MessageUtils.addErrorMessage(context, this,CONVERSION_MESSAGE_ID);
            setValid(false);
        }
    }

    public void validate(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        Object submittedValue = getSubmittedValue();
        if (submittedValue == null) return;

        Object convertedValue = getConvertedValue(context, submittedValue);
        if (!isValid()) return;

        boolean empty = convertedValue == null ||
                        (convertedValue instanceof String
                         && ((String)convertedValue).length() == 0);
        if (isRequired() && empty)
        {
            _MessageUtils.addErrorMessage(context, this, REQUIRED_MESSAGE_ID);
            setValid(false);
            return;
        }

        if (!empty)
        {
            _ComponentUtils.callValidators(context, this, convertedValue);
        }
        if (!isValid()) return;

        Object previousValue = getValue();
        setValue(convertedValue);
        setSubmittedValue(null);
        if (compareValues(previousValue, convertedValue))
        {
            queueEvent(new ValueChangeEvent(this, previousValue, convertedValue));
        }
    }

    private Object getConvertedValue(FacesContext context, Object submittedValue)
    {
        Renderer renderer = getRenderer(context);
        if (renderer != null)
        {
            return renderer.getConvertedValue(context, this, submittedValue);
        }
        else if (submittedValue instanceof String)
        {
            Converter converter = _ComponentUtils.findConverter(context, this);
            if (converter != null)
            {
                try
                {
                    return converter.getAsObject(context, this, (String)submittedValue);
                }
                catch (ConverterException e)
                {
                    FacesMessage facesMessage = e.getFacesMessage();
                    if (facesMessage != null)
                    {
                        context.addMessage(getClientId(context), facesMessage);
                    }
                    else
                    {
                        _MessageUtils.addErrorMessage(context, this, CONVERSION_MESSAGE_ID);
                    }
                    setValid(false);
                }
            }
        }
        return submittedValue;
    }



    protected boolean compareValues(Object previous,
                                    Object value)
    {
        return !((previous == null && value == null) ||
                 (previous != null && value != null && previous.equals(value)));
    }

    public void addValidator(Validator validator)
    {
        if (validator == null) throw new NullPointerException("validator");
        if (_validatorList == null)
        {
            _validatorList = new ArrayList();
        }
        _validatorList.add(validator);
    }

    public Validator[] getValidators()
    {
        return _validatorList != null ?
               (Validator[])_validatorList.toArray(new Validator[_validatorList.size()]) :
               EMPTY_VALIDATOR_ARRAY;
    }

    public void removeValidator(Validator validator)
    {
        if (validator == null) throw new NullPointerException("validator");
        if (_validatorList != null)
        {
            _validatorList.remove(validator);
        }
    }

    public void addValueChangeListener(ValueChangeListener listener)
    {
        addFacesListener(listener);
    }

    public ValueChangeListener[] getValueChangeListeners()
    {
        return (ValueChangeListener[])getFacesListeners(ValueChangeListener.class);
    }

    public void removeValueChangeListener(ValueChangeListener listener)
    {
        removeFacesListener(listener);
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[9];
        values[0] = super.saveState(context);
        values[1] = _immediate;
        values[2] = Boolean.valueOf(_localValueSet);
        values[3] = _required;
        values[4] = _submittedValue;
        values[5] = Boolean.valueOf(_valid);
        values[6] = saveAttachedState(context, _validator);
        values[7] = saveAttachedState(context, _valueChangeListener);
        values[8] = saveAttachedState(context, _validatorList);
        return ((Object)(values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _immediate = (Boolean)values[1];
        _localValueSet = ((Boolean)values[2]).booleanValue();
        _required = (Boolean)values[3];
        _submittedValue = (Object)values[4];
        _valid = ((Boolean)values[5]).booleanValue();
        _validator = (MethodBinding)restoreAttachedState(context, values[6]);
        _valueChangeListener = (MethodBinding)restoreAttachedState(context, values[7]);
        _validatorList = (List)restoreAttachedState(context, values[8]);
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.Input";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Text";
    private static final boolean DEFAULT_IMMEDIATE = false;
    private static final boolean DEFAULT_REQUIRED = false;

    private Boolean _immediate = null;
    private Boolean _required = null;

    public UIInput()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setImmediate(boolean immediate)
    {
        _immediate = Boolean.valueOf(immediate);
    }

    public boolean isImmediate()
    {
        if (_immediate != null) return _immediate.booleanValue();
        ValueBinding vb = getValueBinding("immediate");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_IMMEDIATE;
    }

    public void setRequired(boolean required)
    {
        _required = Boolean.valueOf(required);
    }

    public boolean isRequired()
    {
        if (_required != null) return _required.booleanValue();
        ValueBinding vb = getValueBinding("required");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_REQUIRED;
    }


    //------------------ GENERATED CODE END ---------------------------------------
}

/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.faces.component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.EvaluationException;
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
 * Revision 1.17  2005/04/12 12:41:48  manolito
 * PR: MYFACES-179
 * Submitted by: Mathias Broekelmann
 *
 * Revision 1.16  2005/03/04 00:28:45  mmarinschek
 * Changes in configuration due to missing Attribute/Property classes for the converter; not building in the functionality yet except for part of the converter properties
 *
 * Revision 1.15  2005/01/22 16:47:17  mmarinschek
 * fixing bug with validation not called if the submitted value is empty; an empty string is submitted instead if the component is enabled.
 *
 * Revision 1.14  2004/07/01 22:00:50  mwessendorf
 * ASF switch
 *
 * Revision 1.13  2004/06/07 13:40:38  mwessendorf
 * solved Feature Request #966892
 *
 * Revision 1.12  2004/05/18 10:39:35  manolito
 * (re)set to valid on decode, so that component automatically gets (re)validated
 *
 * Revision 1.11  2004/04/16 15:13:33  manolito
 * validator attribute support and MethodBinding invoke exception handling fixed
 *
 * Revision 1.10  2004/04/06 13:03:35  manolito
 * x-checked getConvertedValue method in api and impl
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
        //We (re)set to valid, so that component automatically gets (re)validated
        setValid(true);
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
            try
            {
                valueChangeListenerBinding.invoke(getFacesContext(),
                                                  new Object[]{event});
            }
            catch (EvaluationException e)
            {
                Throwable cause = e.getCause();
                if (cause != null && cause instanceof AbortProcessingException)
                {
                    throw (AbortProcessingException)cause;
                }
                else
                {
                    throw e;
                }
            }
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
        	//Object[] args = {getId()};
            context.getExternalContext().log(e.getMessage(), e);
            _MessageUtils.addErrorMessage(context, this,CONVERSION_MESSAGE_ID,new Object[]{getId()});
            setValid(false);
        }
    }

    protected void validateValue(FacesContext context,Object convertedValue)
    {
        boolean empty = convertedValue == null ||
                        (convertedValue instanceof String
                         && ((String)convertedValue).length() == 0);

        if (isRequired() && empty)
        {
            _MessageUtils.addErrorMessage(context, this, REQUIRED_MESSAGE_ID,new Object[]{getId()});
            setValid(false);
            return;
        }

        if (!empty)
        {
            _ComponentUtils.callValidators(context, this, convertedValue);
        }

    }

    public void validate(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        Object submittedValue = getSubmittedValue();
        if (submittedValue == null) return;

        if(submittedValue instanceof String && ((String) submittedValue).length()==0)
        {
            submittedValue = null;
        }

        Object convertedValue = getConvertedValue(context, submittedValue);

        if (!isValid()) return;

        validateValue(context, convertedValue);

        if (!isValid()) return;

        Object previousValue = getValue();
        setValue(convertedValue);
        setSubmittedValue(null);
        if (compareValues(previousValue, convertedValue))
        {
            queueEvent(new ValueChangeEvent(this, previousValue, convertedValue));
        }
    }

    protected Object getConvertedValue(FacesContext context, Object submittedValue)
    {
        try
        {
            Renderer renderer = getRenderer(context);
            if (renderer != null)
            {
                return renderer.getConvertedValue(context, this, submittedValue);
            }
            else if (submittedValue instanceof String)
            {
                Converter converter = _SharedRendererUtils.findUIOutputConverter(context, this);
                if (converter != null)
                {
                    return converter.getAsObject(context, this, (String)submittedValue);
                }
            }
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
                _MessageUtils.addErrorMessage(context, this, CONVERSION_MESSAGE_ID,new Object[]{getId()});
            }
            setValid(false);
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

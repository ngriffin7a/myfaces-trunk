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

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

/**
 * see Javadoc of JSF Specification
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIInput
        extends UIOutput
        implements EditableValueHolder
{
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.Input";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Text";
    private static final boolean DEFAULT_IMMEDIATE = false;
    private static final boolean DEFAULT_LOCALVALUESET = false;
    private static final boolean DEFAULT_REQUIRED = false;
    private static final boolean DEFAULT_VALID = true;

    private Boolean _immediate = null;
    private Boolean _localValueSet = null;
    private Boolean _required = null;
    private Object _submittedValue = null;
    private Boolean _valid = null;
    private MethodBinding _validator = null;
    private MethodBinding _valueChangeListener = null;

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

    public void setLocalValueSet(boolean localValueSet)
    {
        _localValueSet = Boolean.valueOf(localValueSet);
    }

    public boolean isLocalValueSet()
    {
        if (_localValueSet != null) return _localValueSet.booleanValue();
        ValueBinding vb = getValueBinding("localValueSet");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_LOCALVALUESET;
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

    public void setSubmittedValue(Object submittedValue)
    {
        _submittedValue = submittedValue;
    }

    public Object getSubmittedValue()
    {
        if (_submittedValue != null) return _submittedValue;
        ValueBinding vb = getValueBinding("submittedValue");
        return vb != null ? (Object)vb.getValue(getFacesContext()) : null;
    }

    public void setValid(boolean valid)
    {
        _valid = Boolean.valueOf(valid);
    }

    public boolean isValid()
    {
        if (_valid != null) return _valid.booleanValue();
        ValueBinding vb = getValueBinding("valid");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_VALID;
    }

    public void setValidator(MethodBinding validator)
    {
        _validator = validator;
    }

    public MethodBinding getValidator()
    {
        if (_validator != null) return _validator;
        ValueBinding vb = getValueBinding("validator");
        return vb != null ? (MethodBinding)vb.getValue(getFacesContext()) : null;
    }

    public void setValueChangeListener(MethodBinding valueChangeListener)
    {
        _valueChangeListener = valueChangeListener;
    }

    public MethodBinding getValueChangeListener()
    {
        if (_valueChangeListener != null) return _valueChangeListener;
        ValueBinding vb = getValueBinding("valueChangeListener");
        return vb != null ? (MethodBinding)vb.getValue(getFacesContext()) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = _immediate;
        values[2] = _localValueSet;
        values[3] = _required;
        values[4] = _submittedValue;
        values[5] = _valid;
        values[6] = _validator;
        values[7] = _valueChangeListener;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _immediate = (Boolean)values[1];
        _localValueSet = (Boolean)values[2];
        _required = (Boolean)values[3];
        _submittedValue = (Object)values[4];
        _valid = (Boolean)values[5];
        _validator = (MethodBinding)values[6];
        _valueChangeListener = (MethodBinding)values[7];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

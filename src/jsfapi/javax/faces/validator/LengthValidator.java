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
package javax.faces.validator;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class LengthValidator
        implements Validator, StateHolder
{
    // FIELDS
    public static final String 	MAXIMUM_MESSAGE_ID = "javax.faces.validator.LengthValidator.MAXIMUM";
    public static final String 	MINIMUM_MESSAGE_ID = "javax.faces.validator.LengthValidator.MINIMUM";
    public static final String 	VALIDATOR_ID 	   = "javax.faces.Length";

    private Integer _minimum = null;
    private Integer _maximum = null;
    private boolean _transient = false;

    // CONSTRUCTORS
    public LengthValidator()
    {
    }

    public LengthValidator(int maximum)
    {
        _maximum = new Integer(maximum);
    }

    public LengthValidator(int maximum,
                           int minimum)
    {
        _maximum = new Integer(maximum);
        _minimum = new Integer(minimum);
    }

    // VALIDATE
    public void validate(FacesContext facesContext,
                         UIComponent uiComponent,
                         Object value)
            throws ValidatorException
    {
        if (facesContext == null) throw new NullPointerException("facesContext");
        if (uiComponent == null) throw new NullPointerException("uiComponent");

        if (value == null)
        {
            return;
        }

        int length = value instanceof String ?
            ((String)value).length() : value.toString().length();

        if (_minimum != null)
        {
            if (length < _minimum.intValue())
            {
                Object[] args = {_minimum,uiComponent.getId()};
                throw new ValidatorException(_MessageUtils.getErrorMessage(facesContext, MINIMUM_MESSAGE_ID, args));
            }
        }
        
        if (_maximum != null)
        {
            if (length > _maximum.intValue())
            {
                Object[] args = {_maximum,uiComponent.getId()};
                throw new ValidatorException(_MessageUtils.getErrorMessage(facesContext, MAXIMUM_MESSAGE_ID, args));
            }
        }
    }

    // SETTER & GETTER
    public int getMaximum()
    {
        return _maximum != null ? _maximum.intValue() : Integer.MAX_VALUE;
    }

    public void setMaximum(int maximum)
    {
        _maximum = new Integer(maximum);
    }

    public int getMinimum()
    {
        return _minimum != null ? _minimum.intValue() : 0;
    }

    public void setMinimum(int minimum)
    {
        _minimum = new Integer(minimum);
    }

    public boolean isTransient()
    {
        return _transient;
    }

    public void setTransient(boolean transientValue)
    {
        _transient = transientValue;
    }

    // RESTORE & SAVE STATE
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = _maximum;
        values[1] = _minimum;
        return values;
    }

    public void restoreState(FacesContext context,
                             Object state)
    {
        Object values[] = (Object[])state;
        _maximum = (Integer)values[0];
        _minimum = (Integer)values[1];
    }

    // MISC
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof LengthValidator)) return false;

        final LengthValidator lengthValidator = (LengthValidator)o;

        if (_maximum != null ? !_maximum.equals(lengthValidator._maximum) : lengthValidator._maximum != null) return false;
        if (_minimum != null ? !_minimum.equals(lengthValidator._minimum) : lengthValidator._minimum != null) return false;

        return true;
    }
}

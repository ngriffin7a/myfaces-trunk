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
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DoubleRangeValidator
        implements Validator, StateHolder
{
    private Double _minimum = null;
    private Double _maximum = null;
    private boolean _transient = false;

    public DoubleRangeValidator()
    {
    }

    public DoubleRangeValidator(double maximum)
    {
        _maximum = new Double(maximum);
    }

    public DoubleRangeValidator(double maximum,
                                double minimum)
    {
        _maximum = new Double(maximum);
        _minimum = new Double(minimum);
    }

    public double getMaximum()
    {
        return _maximum != null ? _maximum.doubleValue() : Double.MAX_VALUE;
    }

    public void setMaximum(double maximum)
    {
        _maximum = new Double(maximum);
    }

    public double getMinimum()
    {
        return _minimum != null ? _minimum.doubleValue() : Double.MIN_VALUE;
    }

    public void setMinimum(double minimum)
    {
        _minimum = new Double(minimum);
    }

    public void validate(FacesContext context,
                         UIComponent component,
                         Object value)
            throws ValidatorException
    {
        //TODO
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof DoubleRangeValidator)) return false;

        final DoubleRangeValidator doubleRangeValidator = (DoubleRangeValidator)o;

        if (_maximum != null ? !_maximum.equals(doubleRangeValidator._maximum) : doubleRangeValidator._maximum != null) return false;
        if (_minimum != null ? !_minimum.equals(doubleRangeValidator._minimum) : doubleRangeValidator._minimum != null) return false;

        return true;
    }

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
        _maximum = (Double)values[0];
        _minimum = (Double)values[1];
    }

    public boolean isTransient()
    {
        return _transient;
    }

    public void setTransient(boolean transientValue)
    {
        _transient = transientValue;
    }
}

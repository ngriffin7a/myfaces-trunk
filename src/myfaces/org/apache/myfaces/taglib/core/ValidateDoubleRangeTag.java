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
package net.sourceforge.myfaces.taglib.core;

import javax.faces.validator.Validator;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ValidateDoubleRangeTag
    extends ValidatorTag
{
    private boolean _isMinSet = false;
    private boolean _isMaxSet = false;
    private double _minimum;
    private double _maximum;

    private static final String TYPE = "javax.faces.validator.DoubleRangeValidator";

    public ValidateDoubleRangeTag()
    {
        _minimum = 0;
        _maximum = 0;
        super.setType(TYPE);
    }

    public double getMinimum()
    {
        return _minimum;
    }

    public void setMinimum(double minimum)
    {
        _minimum = minimum;
        _isMinSet = true;
    }

    public double getMaximum()
    {
        return _maximum;
    }

    public void setMaximum(double maximum)
    {
        _maximum = maximum;
        _isMaxSet = true;
    }

    protected Validator createValidator()
        throws JspException
    {
        DoubleRangeValidator result = (DoubleRangeValidator)super.createValidator();
        if(_isMinSet)
        {
            result.setMinimum(getMinimum());
        }
        if(_isMaxSet)
        {
            result.setMaximum(getMaximum());
        }
        return result;
    }
}

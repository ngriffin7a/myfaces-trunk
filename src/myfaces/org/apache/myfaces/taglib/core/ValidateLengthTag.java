/**
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
package net.sourceforge.myfaces.taglib.core;

import net.sourceforge.myfaces.convert.ConverterUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import javax.faces.webapp.UIComponentTag;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class ValidateLengthTag
    extends ValidatorTag
{
    private String _minimum = null;
    private String _maximum = null;

    private static final String VALIDATOR_ID = "Length";

    public void release()
    {
        _minimum = null;
        _maximum  = null;
    }

    public void setMinimum(String minimum)
    {
        _minimum = minimum;
    }

    public void setMaximum(String maximum)
    {
        _maximum = maximum;
    }

    protected Validator createValidator()
        throws JspException
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        setValidatorId(VALIDATOR_ID);
        LengthValidator validator = (LengthValidator)super.createValidator();
        if (_minimum != null)
        {
            if (UIComponentTag.isValueReference(_minimum))
            {
                ValueBinding vb = facesContext.getApplication().createValueBinding(_minimum);
                validator.setMinimum(ConverterUtils.convertToInt(vb.getValue(facesContext)));
            }
            else
            {
                validator.setMinimum(ConverterUtils.convertToInt(_minimum));
            }
        }
        if (_maximum != null)
        {
            if (UIComponentTag.isValueReference(_maximum))
            {
                ValueBinding vb = facesContext.getApplication().createValueBinding(_maximum);
                validator.setMaximum(ConverterUtils.convertToInt(vb.getValue(facesContext)));
            }
            else
            {
                validator.setMaximum(ConverterUtils.convertToInt(_maximum));
            }
        }
        return validator;
    }


}

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
package net.sourceforge.myfaces.custom.regexprvalidator;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;
import javax.faces.webapp.UIComponentTag;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/06/27 22:06:27  mwessendorf
 * Log
 *
 *
 */
public class ValidateRegExprTag extends ValidatorTag {
	
	//the pattern, needed by Commons-Validator.
	private String _pattern = null;
	
	public ValidateRegExprTag(){
		setValidatorId(RegExprValidator.VALIDATOR_ID);
	}

	public void setPattern(String string) {
		_pattern = string;
	}

	protected Validator createValidator() throws JspException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		RegExprValidator validator = (RegExprValidator)super.createValidator();
		if (_pattern != null)
		{
			if (UIComponentTag.isValueReference(_pattern))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_pattern);
				validator.setPattern(new String(vb.getValue(facesContext).toString()));
			}
			else
			{
				validator.setPattern(_pattern);
			}
		}
		return validator;
	}

}

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
package net.sourceforge.myfaces.custom.equalvalidator;

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
 * Revision 1.4  2004/07/11 16:20:21  mwessendorf
 * typo
 *
 * Revision 1.3  2004/07/01 21:53:10  mwessendorf
 * ASF switch
 *
 * Revision 1.2  2004/06/27 22:06:27  mwessendorf
 * Log
 *
 *
 */
public class ValidateEqualTag extends ValidatorTag {
	
	//the foreign component_id on which the validation is based.
	private String _for = null;
	
	public ValidateEqualTag(){
		setValidatorId(EqualValidator.VALIDATOR_ID);
	}

	public void setFor(String string) {
		_for = string;
	}

	protected Validator createValidator() throws JspException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		EqualValidator validator = (EqualValidator)super.createValidator();
		if (_for != null)
		{
			if (UIComponentTag.isValueReference(_for))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_for);
				validator.setFor(new String(vb.getValue(facesContext).toString()));
			}
			else
			{
				validator.setFor(_for);
			}
		}
		return validator;
	}


}

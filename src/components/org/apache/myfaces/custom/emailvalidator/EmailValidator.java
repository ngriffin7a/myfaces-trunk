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
package net.sourceforge.myfaces.custom.emailvalidator;

import net.sourceforge.myfaces.util.MessageUtils;

import org.apache.commons.validator.GenericValidator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/07/01 21:53:11  mwessendorf
 * ASF switch
 *
 * Revision 1.3  2004/06/16 23:02:19  o_rossmueller
 * merged confignew_branch
 *
 * Revision 1.2.2.1  2004/06/13 15:59:05  o_rossmueller
 * started integration of new config mechanism:
 * - factories
 * - components
 * - render kits
 * - managed beans + managed properties (no list/map initialization)
 *
 * Revision 1.2  2004/06/05 09:37:43  mwessendorf
 * new validator for regExpr.
 * and began with Friendly validator messages
 *
 * Revision 1.1  2004/05/27 14:09:01  manolito
 * creditcard and email validator refactored
 *
 */
public class EmailValidator implements Validator {
	
	/**
	 * <p>The standard converter id for this converter.</p>
	 */
	public static final String 	VALIDATOR_ID 	   = "net.sourceforge.myfaces.validator.Email";
	/**
	 * <p>The message identifier of the {@link FacesMessage} to be created if
	 * the maximum length check fails.</p>
	 */
	public static final String EMAIL_MESSAGE_ID = "net.sourceforge.myfaces.Email.INVALID";	
	
	public EmailValidator(){
	}

	/**
	 * methode that validates an email-address.
	 * it uses the commons-validator 
	 */
	public void validate(
		FacesContext facesContext,
		UIComponent uiComponent,
		Object value)
		throws ValidatorException {
	
	
			if (facesContext == null) throw new NullPointerException("facesContext");
			if (uiComponent == null) throw new NullPointerException("uiComponent");

			if (value == null)
			{
				return;
			}
			if (!GenericValidator.isEmail(value.toString())) {
				Object[] args = {uiComponent.getId()};
				throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,EMAIL_MESSAGE_ID, args));
				
			}

	}

}

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
package net.sourceforge.myfaces.validate;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sourceforge.myfaces.util.MessageUtils;

import org.apache.commons.validator.GenericValidator;

/**
 * @author mwessendorf
 */
public class EmailValidator implements Validator {
	
	/**
	 * <p>The standard converter id for this converter.</p>
	 */
	public static final String 	VALIDATOR_ID 	   = "myfaces.validator.Email";
	/**
	 * <p>The message identifier of the {@link FacesMessage} to be created if
	 * the maximum length check fails.</p>
	 */
	public static final String EMAIL_MESSAGE_ID = "myfaces.Email.INVALID";	
	
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

				throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,EMAIL_MESSAGE_ID, null));
				
			}

	}

}

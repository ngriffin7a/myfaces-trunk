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

import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.3  2004/09/15 07:58:58  mwessendorf
 * Custom Validators now work in OC4J - thanks to Daniel Kamakura for supporting this
 *
 * Revision 1.2  2004/07/01 21:53:11  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/05/27 14:09:01  manolito
 * creditcard and email validator refactored
 *
 */
public class ValidateEmailTag extends ValidatorTag
{
	
	public ValidateEmailTag()
    {
	}

	/* (non-Javadoc)
	 * @see javax.faces.webapp.ValidatorTag#createValidator()
	 */
	protected Validator createValidator() throws JspException
    {
		setValidatorId(EmailValidator.VALIDATOR_ID);
	    EmailValidator validator = (EmailValidator)super.createValidator();
		return validator;
	}
	
    public void release()
    {
        super.release();
    }

}

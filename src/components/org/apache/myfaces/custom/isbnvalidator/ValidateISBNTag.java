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
package org.apache.myfaces.custom.isbnvalidator;

import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

import org.apache.myfaces.custom.isbnvalidator.ISBNValidator;

/**
 * @author <a href="mailto:matzew@apache.org">Matthias We�endorf</a> (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/11/23 04:59:23  svieujot
 * Remove "unmappable character for encoding UTF8" warning.
 *
 * Revision 1.1  2004/11/20 20:17:05  matzew
 * added new validator for ISBN codes
 *
 */
public class ValidateISBNTag extends ValidatorTag{
    
    public ValidateISBNTag(){}
    
	protected Validator createValidator() throws JspException
    {
		setValidatorId(ISBNValidator.VALIDATOR_ID);
		ISBNValidator validator = (ISBNValidator)super.createValidator();
		return validator;
	}
	
    public void release()
    {
        super.release();
    }


}

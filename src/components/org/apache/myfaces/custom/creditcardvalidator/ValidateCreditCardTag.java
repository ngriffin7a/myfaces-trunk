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
package net.sourceforge.myfaces.custom.creditcardvalidator;

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
 * Revision 1.3  2004/09/15 07:58:59  mwessendorf
 * Custom Validators now work in OC4J - thanks to Daniel Kamakura for supporting this
 *
 * Revision 1.2  2004/07/01 21:53:08  mwessendorf
 * ASF switch
 *
 * Revision 1.1  2004/05/27 14:09:00  manolito
 * creditcard and email validator refactored
 *
 */
public class ValidateCreditCardTag extends ValidatorTag {
	//Cardtypes, that are supported by Commons-Validator.
	private String _amex = null;
	private String _discover = null;
	private String _mastercard = null;
	private String _visa = null;
	private String _none = null;
	
	public ValidateCreditCardTag()
    {
	}

	/**
	 * @param string
	 */
	public void setAmex(String string) {
		_amex = string;
	}

	/**
	 * @param string
	 */
	public void setDiscover(String string) {
		_discover = string;
	}

	/**
	 * @param string
	 */
	public void setMastercard(String string) {
		_mastercard = string;
	}

	/**
	 * @param string
	 */
	public void setNone(String string) {
		_none = string;
	}

	/**
	 * @param string
	 */
	public void setVisa(String string) {
		_visa = string;
	}

	/* (non-Javadoc)
	 * @see javax.faces.webapp.ValidatorTag#createValidator()
	 */
	protected Validator createValidator() throws JspException {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		setValidatorId(CreditCardValidator.VALIDATOR_ID);
		CreditCardValidator validator = (CreditCardValidator)super.createValidator();
		if (_none != null)
		{
			if (UIComponentTag.isValueReference(_none))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_none);
				validator.setNone(new Boolean(vb.getValue(facesContext).toString()).booleanValue());
			}
			else
			{
				validator.setNone(new Boolean(_none).booleanValue());
			}
		}
		if (_visa != null)
		{
			if (UIComponentTag.isValueReference(_visa))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_visa);
				validator.setVisa(new Boolean(vb.getValue(facesContext).toString()).booleanValue());
			}
			else
			{
				validator.setVisa(new Boolean(_visa).booleanValue());
			}
		}
		if (_mastercard != null)
		{
			if (UIComponentTag.isValueReference(_mastercard))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_mastercard);
				validator.setMastercard(new Boolean(vb.getValue(facesContext).toString()).booleanValue());
			}
			else
			{
				validator.setMastercard(new Boolean(_mastercard).booleanValue());
			}
		}
		if (_discover != null)
		{
			if (UIComponentTag.isValueReference(_discover))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_discover);
				validator.setDiscover(new Boolean(vb.getValue(facesContext).toString()).booleanValue());
			}
			else
			{
				validator.setDiscover(new Boolean(_discover).booleanValue());
			}
		}
		if (_amex != null)
		{
			if (UIComponentTag.isValueReference(_amex))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_amex);
				validator.setAmex(new Boolean(vb.getValue(facesContext).toString()).booleanValue());
			}
			else
			{
				validator.setAmex(new Boolean(_amex).booleanValue());
			}
		}
		return validator;
	}
	
	

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        super.release();
    	_amex = null;
    	_discover = null;
    	_mastercard = null;
    	_visa = null;
    	_none = null;
   }
}

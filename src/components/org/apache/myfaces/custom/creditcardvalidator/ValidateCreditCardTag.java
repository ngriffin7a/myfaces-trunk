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
		setValidatorId(CreditCardValidator.VALIDATOR_ID);
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

}

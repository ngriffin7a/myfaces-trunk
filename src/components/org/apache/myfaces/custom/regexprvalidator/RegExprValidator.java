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

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sourceforge.myfaces.util.MessageUtils;

import org.apache.commons.validator.GenericValidator;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/06/27 22:06:27  mwessendorf
 * Log
 *
 *
 */
public class RegExprValidator implements Validator, StateHolder {
	/**
	 * <p>The standard converter id for this converter.</p>
	 */
	public static final String 	VALIDATOR_ID 	   = "net.sourceforge.myfaces.validator.RegExpr";

	/**
	 * <p>The message identifier of the {@link FacesMessage} to be created if
	 * the creditcard check fails.</p>
	 */
	public static final String REGEXPR_MESSAGE_ID = "net.sourceforge.myfaces.Regexpr.INVALID";	

	public RegExprValidator(){
	}

	//the pattern on which the validation is based.
	private String _pattern= null;

	
	//JSF-Field for StateHolder-IF
	private boolean _transient = false;



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
		Object[] args = {uiComponent.getId()};
		if(!GenericValidator.matchRegexp(value.toString(),"^"+_pattern+"$")){
			throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,REGEXPR_MESSAGE_ID, args));
		
		}

	}



	// -------------------------------------------------------- StateholderIF

	public Object saveState(FacesContext context) {
		Object state = _pattern;
		return state;
	}

	public void restoreState(FacesContext context, Object state) {
		_pattern = (String) state;
	}

	public boolean isTransient() {
		return _transient;
	}

	public void setTransient(boolean newTransientValue) {
		_transient = newTransientValue;
	}
	// -------------------------------------------------------- GETTER & SETTER

	/**
	 * @return the pattern, on which a value should be validated
	 */
	public String getPattern() {
		return _pattern;
	}

	/**
	 * @param string the pattern, on which a value should be validated
	 */
	public void setPattern(String string) {
		_pattern = string;
	}

}

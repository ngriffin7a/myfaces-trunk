package net.sourceforge.myfaces.examples.validate;

/**
 * @author mwessendorf
 */
public class ValidateForm {

	private String email = null;
	private String creditCardNumber = null;
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String string) {
		email = string;
	}
	
	public String submit(){
		System.out.println("Action was called.");
		return ("valid");
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String string) {
		creditCardNumber = string;
	}

}

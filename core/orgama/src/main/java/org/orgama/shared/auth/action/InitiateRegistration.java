package org.orgama.shared.auth.action;

import org.orgama.shared.action.AbstractAction;

/**
 * Action that indicates the user wishes to register on the app with a given
 * auth source
 * @author kguthrie
 */
public class InitiateRegistration 
		extends AbstractAction<InitiateRegistrationResult>{

	private String authResourceName;
	private String emailAddress;
	
	/**
	 * For serialization
	 */
	public InitiateRegistration() {
		
	}

	/**
	 * @return the authSourceName
	 */
	public String getAuthResourceName() {
		return authResourceName;
	}

	/**
	 * @param authSourceName the authSourceName to set
	 */
	public void setAuthResourceName(String authSourceName) {
		this.authResourceName = authSourceName;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	
}

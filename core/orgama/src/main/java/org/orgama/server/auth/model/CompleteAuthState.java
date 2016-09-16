package org.orgama.server.auth.model;

import org.orgama.shared.auth.model.AuthState;
import org.orgama.shared.auth.model.ICompleteAuthState;

/**
 * Transfer and storage object that completely represents the user's state in 
 * the authentication process.  
 * @author kguthrie
 */
public class CompleteAuthState implements ICompleteAuthState {

	private AuthState authState;
	private String authServiceName;
	
	/**
	 * For serialization
	 */
	public CompleteAuthState() {
		
	}

	/**
	 * @return the authState
	 */
	@Override
	public AuthState getAuthState() {
		return authState;
	}

	/**
	 * @param authState the authState to set
	 */
	public void setAuthState(AuthState authState) {
		this.authState = authState;
	}

	/**
	 * @return the authServiceName
	 */
	@Override
	public String getAuthServiceName() {
		return authServiceName;
	}

	/**
	 * @param authServiceName the authServiceName to set
	 */
	public void setAuthServiceName(String authServiceName) {
		this.authServiceName = authServiceName;
	}
	
}

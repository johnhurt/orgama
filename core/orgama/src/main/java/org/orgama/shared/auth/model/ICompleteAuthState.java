package org.orgama.shared.auth.model;

/**
 * Transfer and storage object that completely represents the user's state in 
 * the authentication process.  
 * @author kguthrie
 */
public interface ICompleteAuthState {

	/**
	 * @return the authState
	 */
	public AuthState getAuthState();
	
	/**
	 * @return the authService's name
	 */
	public String getAuthServiceName();

}

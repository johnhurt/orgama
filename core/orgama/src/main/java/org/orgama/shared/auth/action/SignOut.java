package org.orgama.shared.auth.action;

import org.orgama.shared.action.AbstractAction;

/**
 * Action indicating that the user wants to sign out of either this application,
 * the external authentication service, or both.
 * @author kguthrie
 */
public class SignOut extends AbstractAction<SignOutResult> {

	private boolean signOutOfApp;
	private boolean signOutOfExternalService;
	private String authServiceName;
	
	public SignOut() {
	}

	/**
	 * @return the signOutOfApp
	 */
	public boolean isSignOutOfApp() {
		return signOutOfApp;
	}

	/**
	 * @param signOutOfApp the signOutOfApp to set
	 */
	public void setSignOutOfApp(boolean signOutOfApp) {
		this.signOutOfApp = signOutOfApp;
	}

	/**
	 * @return the signOutOfExternalService
	 */
	public boolean isSignOutOfExternalService() {
		return signOutOfExternalService;
	}

	/**
	 * @param signOutOfExternalService the signOutOfExternalService to set
	 */
	public void setSignOutOfExternalService(boolean signOutOfExternalService) {
		this.signOutOfExternalService = signOutOfExternalService;
	}

	/**
	 * @return the authServiceName
	 */
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

package org.orgama.shared.auth.model;

/**
 * enumeration of the potential states of the auth system.  
 * @author kguthrie
 */
public enum AuthState {
	nil, //Authenication not started
	serverError, //Error on the server preventing authentication
	authenticated, //User is fully authenticated
	externalAuthenticationFailed, //Exteral service says user not authenticated
	emailAddressTaken, //email address has already been registered
	externalAccountClaimed, //external account was already claimed
	externalUserIdMismatch, //external Id on authenticate != the one registered
}

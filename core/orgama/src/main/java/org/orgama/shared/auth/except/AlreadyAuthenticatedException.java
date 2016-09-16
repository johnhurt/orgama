package org.orgama.shared.auth.except;

/**
 * Exception indicating that a given operation cannot be performed because the
 * user is already authenticated.  This applies to creating a new auth 
 * initialization object.  
 * @author kguthrie
 */
public class AlreadyAuthenticatedException extends AuthException {

	public AlreadyAuthenticatedException() {
		super(AuthErrorCodes.alreadyAuthenticated);
	}
	
}

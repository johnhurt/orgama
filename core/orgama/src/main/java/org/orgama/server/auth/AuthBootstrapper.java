package org.orgama.server.auth;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.model.CompleteAuthState;
import org.orgama.server.auth.source.IAuthServiceProvider;
import org.orgama.server.auth.source.IAuthService;
import org.orgama.server.unique.except.UniqueFieldRestrictionException;
import org.orgama.shared.Logger;
import org.orgama.shared.auth.except.AuthException;
import org.orgama.shared.auth.model.AuthSession;
import org.orgama.shared.auth.model.AuthState;
import org.orgama.shared.auth.model.AuthUser;

/**
 * Class that bootstraps a user's interaction with the authentication.  This 
 * class's methods are meant to be accessible from either the jsp interop or the
 * web service layer.
 * @author kguthrie
 */
public class AuthBootstrapper {

	private final AuthSessionService authSessionService;
	private final AuthInitializationService authInitService;
	private final Provider<AuthSystemCleaner> authSystemCleanerProvider;
	private final IAuthServiceProvider serviceProvider;
	private final ICoreUserService userService;
	
	@Inject
	public AuthBootstrapper(AuthSessionService authSessionService,
			AuthInitializationService authInitService, 
			Provider<AuthSystemCleaner> authSystemCleanerProvider, 
			IAuthServiceProvider serviceProvider, 
			ICoreUserService userService) {
		
		this.authSessionService = authSessionService;
		this.authInitService = authInitService;
		this.authSystemCleanerProvider = authSystemCleanerProvider;
		this.serviceProvider = serviceProvider;
		this.userService = userService;
	}
	
	/**
	 * Bootstrap the authentication process on the current request.
	 * @return 
	 */
	public CompleteAuthState bootstrap() {
		CompleteAuthState result = new CompleteAuthState();
		
		try {
			AuthSession authSession = null;
			
			// Try to get the existing auth session.  While bootstrapping, 
			// errors in this step will lead to resetting of the auth process
			try {
				authSession = authSessionService.get();
			}
			catch(AuthException ax) {
				Logger.error("Authentication error while trying to get auth " +
						"session.  Reseting auth system in clean state", ax);
				authSystemCleanerProvider.get().clean();
			}
			
			if (authSession != null) {
				result.setAuthState(AuthState.authenticated);
				result.setAuthServiceName(authSession.getAuthServiceName());
				return result;
			}
			
			//Reaching this step means there was no existing auth session.  
			//In stead get the auth initialization
			AuthInitialization authInit = 
					authInitService.get();
			result.setAuthServiceName(authInit.getAuthServiceName());
			
			switch (authInit.getState()) {
				case nil: { //Implies user has not started authenticating yet
					result.setAuthState(AuthState.nil);
					break;
				}
				case registering: {
					result.setAuthState(handleRegistration(authInit));
					break;
				}
				case authenticating: {
					result.setAuthState(handleAuthentication(authInit));
				}
			}
		}
		catch(Exception ex) {
			Logger.error("Error bootstrapping authentication", ex);
			result.setAuthState(AuthState.serverError);
		}
		
		return result;
	}
	
	/**
	 * Handles bootstrapping for requests for clients that have left the site 
	 * for external registration and are returning.  The possible results are
	 * 1. that the external authentication succeeded, and upon verification, 
	 * everything will be fine.  
	 * 2. external registration failed, so upon verification the user will not
	 * be logged in
	 * 3. external registration succeeded, so verification shows that the user 
	 * is authenticated, but either the unique external id or the email address
	 * used by the user has already been taken
	 */
	private AuthState handleRegistration(
			AuthInitialization authInit) {
		
		IAuthService service = serviceProvider.getAuthServices().get(
				authInit.getAuthServiceName());
		
		authInitService.clear();
			
		if (service == null) {
			throw new AuthException("No authentication service was " + 
					"registered with the resource name: " + 
					authInit.getAuthServiceName());
		}
		
		String serviceSpecificSessionId = service.getServiceSpecificSessionId();
		
		//If the user comes back to the site without being authenticated (the
		//nerve), then the new auth state is "registrationFailed".  This is
		//returned as part of the compelte auth state and allows the app to 
		//decide how this information is presented and handled
		if (Strings.isNullOrEmpty(serviceSpecificSessionId)) {
			return AuthState.externalAuthenticationFailed;
		}
		
		String serviceSpecificUserId = 
				service.getServiceSpecificUserId(serviceSpecificSessionId);
		
		//At this point we are pretty certain the user is logged in because we 
		//have a service-specific sessionId.  However some services don't use
		//a service specific session Id, so we have to verify that the retrtied
		//service-specific user id is valid.
		if (Strings.isNullOrEmpty(serviceSpecificUserId)) {
			return AuthState.externalAuthenticationFailed;
		}
		
		authInit.setServiceSpecificSessionId(serviceSpecificSessionId);
		authInit.setServiceSpecificUserId(serviceSpecificUserId);
		
		AuthUser user;
		
		//Try to register the user.  Catch unique field exceptions to detect
		//email address already taken and external account already cleamed
		//scenarios
		try {
			user = userService.registerNewUser(authInit);
		}
		catch (UniqueFieldRestrictionException ufrx) {
			String message = ufrx.getMessage().toLowerCase();
			
			if (message.contains("email")) {
				return AuthState.emailAddressTaken;
			}
			else {
				return AuthState.externalAccountClaimed;
			}
		}
		
		//Create an auth session for the given user
		authSessionService.create(user, authInit);
		
		return AuthState.authenticated;
	}
	
	/**
	 * handle the completion of bootstrap requests for users returning to the
	 * site after going to their auth service's login url
	 * @param authInit
	 * @return 
	 */
	private AuthState handleAuthentication(AuthInitialization authInit) {
		
		IAuthService service = serviceProvider.getAuthServices().get(
				authInit.getAuthServiceName());
		
		authInitService.clear();
			
		if (service == null) {
			throw new AuthException("No authentication service was " + 
					"registered with the resource name: " + 
					authInit.getAuthServiceName());
		}
		
		String serviceSpecificSessionId = service.getServiceSpecificSessionId();
		
		//If the user comes back to the site without being authenticated (the
		//nerve), then the new auth state is "authenticationFailed".  This is
		//returned as part of the compelte auth state and allows the app to 
		//decide how this information is presented and handled
		if (Strings.isNullOrEmpty(serviceSpecificSessionId)) {
			return AuthState.externalAuthenticationFailed;
		}
		
		String serviceSpecificUserId = 
				service.getServiceSpecificUserId(serviceSpecificSessionId);
		
		//At this point we are pretty certain the user is logged in because we 
		//have a service-specific sessionId.  However some services don't use
		//a service specific session Id, so we have to verify that the retrtied
		//service-specific user id is valid.
		if (Strings.isNullOrEmpty(serviceSpecificUserId)) {
			return AuthState.externalAuthenticationFailed;
		}
		
		//Check that the user's service specific id matches the one returned by 
		//auth service.  If it doesn't return the state indicating it
		if (!serviceSpecificUserId.equals(
				authInit.getServiceSpecificUserId())) {
			return AuthState.externalUserIdMismatch;
		}
		
		authInit.setServiceSpecificSessionId(serviceSpecificSessionId);
		
		AuthUser user = userService.getUserFromEmailAddress(
				authInit.getEmailAddress());
		
		authSessionService.create(user, authInit);
		
		return AuthState.authenticated;
	}
}

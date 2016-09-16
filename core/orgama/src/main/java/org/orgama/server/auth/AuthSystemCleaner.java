package org.orgama.server.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javax.servlet.http.HttpSession;
import org.orgama.server.config.ServerSideConstants;
import org.orgama.shared.ICookieHandler;

/**
 * Class designed to take the auth system from any state to a clean, 
 * uninitialized state
 * @author kguthrie
 */
public class AuthSystemCleaner {

	private Provider<ICookieHandler> cookieHandlerProvider;
	private Provider<HttpSession> httpSessionProvider;
	private AuthSessionService authSessionService;
	private AuthInitializationService authInitializationProvider;
	private ServerSideConstants constants;
	
	@Inject
	public AuthSystemCleaner(
			Provider<ICookieHandler> cookieHandlerProvider, 
			Provider<HttpSession> httpSessionProvider, 
			AuthSessionService authSessionService,
			AuthInitializationService authInitializationProvider, 
			ServerSideConstants constants) {
		this.cookieHandlerProvider = cookieHandlerProvider;
		this.httpSessionProvider = httpSessionProvider;
		this.authSessionService = authSessionService;
		this.authInitializationProvider = authInitializationProvider;
		this.constants = constants;
	}
	
	/**
	 * Called to run the cleanup of the auth system
	 */
	public void clean() {
		HttpSession session = httpSessionProvider.get();
		session.removeAttribute(constants.getAuthInitializationKey());
		
		authInitializationProvider.reset();
		
		authSessionService.closeCurrent();
	}
	
}

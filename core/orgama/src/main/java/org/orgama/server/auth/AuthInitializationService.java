package org.orgama.server.auth;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.servlet.http.HttpSession;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.model.AuthInitializationState;
import org.orgama.server.config.ServerSideConstants;

/**
 * Provider for auth initializations.  This provider will try to retrieve the 
 * auth initialization from the http session, but if one is not found, then one
 * will be created and stored in the http session.  This provider is intended to 
 * be bound in session scope, so that once the auth initialization is retrieved
 * from the auth session, it can be retrieved from memory from then on.
 * @author kguthrie
 */
public class AuthInitializationService 
		implements Provider<AuthInitialization> {
	
	/**
	 * Annotation indicating that a certain authInitialization provider should
	 * only provide auth initializations in the new state
	 */
	@BindingAnnotation @Target({ PARAMETER, FIELD }) @Retention(RUNTIME)
	public static @interface RequireNew {}
	
	private final Provider<HttpSession> httpSessionProvider;
	private final ServerSideConstants constants;
	protected AuthInitialization authInitialization;
	
	/**
	 * construct a new auth initialization provider that does not always 
	 * provide an auth initialization in newly created state: nil
	 * @param httpSessionProvider
	 * @param constants 
	 */
	@Inject
	public AuthInitializationService(
			Provider<HttpSession> httpSessionProvider,
			ServerSideConstants constants) {
		this.httpSessionProvider = httpSessionProvider;
		this.constants = constants;
	}

	
	/**
	 * Get the local copy of the auth initialization if one exists or create one
	 * and return that instead
	 * @return 
	 */
	@Override
	public AuthInitialization get() {
		
		if (authInitialization != null) {
			return authInitialization;
		}
		
		authInitialization = getImpl();
		
		return authInitialization;
	}
	
	/**
	 * this method is called if there is no local copy 
	 * @return 
	 */
	private synchronized AuthInitialization getImpl() {
		
		AuthInitialization result = authInitialization;
		
		if (result != null) {
			return result;
		}
		
		HttpSession httpSession = httpSessionProvider.get();
		
		result = (AuthInitialization)httpSession.getAttribute(
				constants.getAuthInitializationKey());
		
		
		if (result != null) {
			return result;
		}
		
		result = new AuthInitialization();
		httpSession.setAttribute(
				constants.getAuthInitializationKey(), result);

		return result;
	}
	
	/**
	 * reset that auth initialization provider so that its local copy will 
	 * not be returned.
	 */
	synchronized void reset() {
		authInitialization = null;
	}
	
	/**
	 * Save the given auth Initialization in the session and as the local 
	 * instance
	 * @param authInitialization 
	 */
	public synchronized void save(AuthInitialization authInitialization) {
		this.authInitialization = authInitialization;
		HttpSession httpSession = httpSessionProvider.get();
		httpSession.setAttribute(constants.getAuthInitializationKey(), 
				authInitialization);
	}
	
	/**
	 * clear out the auth initialization stored in the session and in this 
	 * instance.
	 */
	public synchronized void clear() {
		this.authInitialization = null;
		HttpSession httpSession = httpSessionProvider.get();
		httpSession.removeAttribute(constants.getAuthInitializationKey());
	}
	
	/**
	 * always provides a new auth initialization entity
	 */
	public static class NewRequired extends AuthInitializationService {
		
		private AuthInitializationService naturalService;
		
		@Inject
		public NewRequired(
				AuthInitializationService naturalService,
				Provider<HttpSession> httpSessionProvider,
				ServerSideConstants constants) {
			super(httpSessionProvider, constants);
			this.naturalService = naturalService;
		}

		/**
		 * Get a the auth initialization from the natural service.  If the 
		 * initialization returned is not new, then clear it and return a new
		 * one
		 * @return 
		 */
		@Override
		public AuthInitialization get() {
			AuthInitialization result = naturalService.get();
			
			if (result.getState() != AuthInitializationState.nil) {
				
				synchronized(this) {
					result = naturalService.get();
					if (result.getState() != AuthInitializationState.nil) {
						naturalService.clear();
						result = naturalService.get();
					}

				}
			}
			
			return result;
		}
		
		
	}
}

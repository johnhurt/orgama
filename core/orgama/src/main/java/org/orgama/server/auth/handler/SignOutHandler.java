package org.orgama.server.auth.handler;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import javax.servlet.http.HttpServletRequest;
import org.orgama.server.auth.AuthInitializationService;
import org.orgama.server.auth.ICoreSessionService;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.source.IAuthServiceProvider;
import org.orgama.server.auth.source.IAuthService;
import org.orgama.server.handler.AbstractHandler;
import org.orgama.shared.Logger;
import org.orgama.shared.auth.action.SignOut;
import org.orgama.shared.auth.action.SignOutResult;
import org.orgama.shared.auth.model.AuthSession;

/**
 * handles the sign out action.  If the user only wishes to be logged out the
 * application, this action will do so and redirect the user to the base
 * domain.  If the user wishes to log out of the external authentication 
 * source, then the user is redirected to the service's log out url with 
 * indication that the user should be returned to the base domain when the 
 * external service has finished
 * @author kguthrie
 */
public class SignOutHandler extends AbstractHandler<SignOut, SignOutResult>{

	private final Provider<ICoreSessionService> sessionServicesProvider;
	private final IAuthServiceProvider serviceProvider;
	private final Provider<HttpServletRequest> requestProvider;
			
	
	@Inject
	public SignOutHandler(
			Provider<ICoreSessionService> sessionServicesProvider, 
			IAuthServiceProvider serviceProvider, 
			Provider<AuthInitializationService> authInitProvider, 
			Provider<HttpServletRequest> requestProvider) {
		this.sessionServicesProvider = sessionServicesProvider;
		this.serviceProvider = serviceProvider;
		this.requestProvider = requestProvider;
	}
	
	@Override
	public SignOutResult execImpl(SignOut a, ExecutionContext ec) {
		
		ICoreSessionService sessionService = sessionServicesProvider.get();
		AuthSession authSession = sessionService.get();
		String alternateRedirectUrl = null;
		SignOutResult result = new SignOutResult();
		
		if (a.isSignOutOfApp()) {
			if (authSession == null) {
				Logger.warn("Requested to log out of app when not logged in");
			}
			else {
				sessionService.close(authSession);
			}
		}
		
		if (a.isSignOutOfExternalService()) {
			String authServiceName = null;
			
			if (authSession != null) {
				authServiceName = authSession.getAuthServiceName();
			}
			else {
				authServiceName = a.getAuthServiceName();
			}
			
			if (authServiceName == null) {
				Logger.warn("No auth service could be found to log out of");
			}
			else {
				IAuthService service = 
						serviceProvider.getAuthServices().get(authServiceName);
				if (service != null) {
					alternateRedirectUrl = service.getSignOutUrl(
							authSession != null
							? authSession.getServiceSpecificSessionId()
							: null);
				}
			}
			
		}
		
		if (alternateRedirectUrl != null) {
			result.setRedirectUrl(alternateRedirectUrl);
		}
		else {
			result.setRedirectUrl(getDefaultUrl());
		}
		
		return result;
	}

	/**
	 * Get the default url to redirect the user to when there is no external
	 * account to log out of
	 * @return 
	 */
	private String getDefaultUrl() {
		HttpServletRequest request = requestProvider.get();
		String requestUrl = request.getRequestURL().toString();
		String requestUri = request.getRequestURI();
		String result = requestUrl.replace(requestUri, "");
		return result;
	}
	
	@Override
	public void undoImpl(SignOut a, SignOutResult r, ExecutionContext ec) {
		//Not available for logout action
	}

	@Override
	public Class<SignOut> getActionType() {
		return SignOut.class;
	}

}

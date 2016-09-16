package org.orgama.server.auth.handler;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import javax.servlet.http.HttpServletResponse;
import org.orgama.server.auth.AuthInitializationService;
import org.orgama.server.auth.AuthSessionService;
import org.orgama.server.auth.ICoreUserService;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.model.AuthInitializationState;
import org.orgama.server.auth.source.IAuthServiceProvider;
import org.orgama.server.auth.source.IAuthService;
import org.orgama.server.handler.AbstractHandler;
import org.orgama.shared.auth.action.InitiateRegistration;
import org.orgama.shared.auth.action.InitiateRegistrationResult;
import org.orgama.shared.auth.except.AlreadyAuthenticatedException;
import org.orgama.shared.auth.except.AuthException;
import org.orgama.shared.auth.except.EmailAddressTakenException;
import org.orgama.shared.auth.model.AuthSession;
import org.orgama.shared.auth.model.AuthUser;
import org.orgama.shared.except.OrgamaCoreException;

import static org.orgama.server.auth.AuthInitializationService.RequireNew;

/**
 * handles the registration action by updating the auth initialization and re
 * directing the user to their selected auth source's login url
 * @author kguthrie
 */
public class InitiateRegistrationHandler 
		extends AbstractHandler<InitiateRegistration, 
				InitiateRegistrationResult> {

    private final IAuthServiceProvider authSourceProvider;
	private final Provider<AuthInitializationService> initProviderProvider;
    private final Provider<AuthSessionService> sessionProviderProvider;
	private final ICoreUserService userServices;
	private final Provider<HttpServletResponse> responseProvider;
	
    @Inject
    public InitiateRegistrationHandler(
			IAuthServiceProvider authProviders, 
            @RequireNew Provider<AuthInitializationService> authInitProvProv, 
			Provider<AuthSessionService> sessionProviderProvider, 
			ICoreUserService userServices, 
			Provider<HttpServletResponse> responseProvider) {
        this.initProviderProvider = authInitProvProv;
		this.authSourceProvider = authProviders;
		this.sessionProviderProvider = sessionProviderProvider;
		this.userServices = userServices;
		this.responseProvider = responseProvider;
    }
	
	@Override
	public InitiateRegistrationResult execImpl(InitiateRegistration a, 
			ExecutionContext ec) {
		InitiateRegistrationResult result = new InitiateRegistrationResult();
		
		String emailAddress = a.getEmailAddress();
		String authResourceName = a.getAuthResourceName();
		
		//Check that the user is not already logged in
		AuthSession session = sessionProviderProvider.get().get();
		
		if (session != null) {
			throw new AlreadyAuthenticatedException();
		}
		
		//Doublecheck that no users already have the given email address
		AuthUser user = userServices.getUserFromEmailAddress(emailAddress);
		
		if (user != null) {
			throw new EmailAddressTakenException();
		}
		
		AuthInitializationService authInitService = 
				this.initProviderProvider.get();
		
		AuthInitialization authInit = authInitService.get();
		
		IAuthService authService = 
				authSourceProvider.getAuthServices().get(authResourceName);
		
		if (authService == null) {
			throw new AuthException("No auth service could be found matching: " 
					+ authResourceName);
		}
		
		HttpServletResponse response = responseProvider.get();
		
		if (response == null) {
			throw new OrgamaCoreException("Failed to get HttpResponse");
		}
		
		String signInUrl = authService.getSignInUrl();
		
		result.setRedirectUrl(signInUrl);
		
		authInit.setEmailAddress(emailAddress);
		authInit.setState(AuthInitializationState.registering);
		authInit.setAuthServiceName(authResourceName);
		
		authInitService.save(authInit);
		
		return result;
	}
	
	@Override
	public void undoImpl(InitiateRegistration a, InitiateRegistrationResult r, 
			ExecutionContext ec) {
		//Can't undo this
	}

	@Override
	public Class<InitiateRegistration> getActionType() {
		return InitiateRegistration.class;
	}

}

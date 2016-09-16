// <copyright file="ValidateEmailAddressHandler.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.server.auth.handler;

import static org.orgama.server.auth.AuthInitializationService.*;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import java.util.ArrayList;
import java.util.Map;
import org.orgama.server.auth.AuthInitializationService;
import org.orgama.server.auth.AuthSessionService;
import org.orgama.server.auth.ICoreUserService;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.model.AuthInitializationState;
import org.orgama.server.handler.AbstractHandler;
import org.orgama.shared.Logger;
import org.orgama.shared.auth.action.ValidateEmailAddress;
import org.orgama.shared.auth.action.ValidateEmailAddressResult;
import org.orgama.shared.auth.except.AuthException;
import org.orgama.shared.auth.except.InvalidEmailAddressFormatException;
import org.orgama.shared.auth.model.AuthUser;
import org.orgama.server.auth.source.IAuthServiceProvider;
import org.orgama.server.auth.source.IAuthService;
import org.orgama.shared.auth.AuthUtils;
import org.orgama.shared.auth.except.AlreadyAuthenticatedException;
import org.orgama.shared.auth.model.AuthSession;
import org.orgama.shared.auth.model.AuthSourceInfo;

/**
 * Handle validating email addresses
 *
 * @author kguthrie
 */
public class ValidateEmailAddressHandler extends AbstractHandler<ValidateEmailAddress, ValidateEmailAddressResult> {

	protected final IAuthServiceProvider authSourceProvider;
	protected final Provider<AuthInitializationService> initProviderProvider;
	protected final Provider<AuthSessionService> sessionProviderProvider;
	private final ICoreUserService userServices;

	@Inject
	public ValidateEmailAddressHandler(
			IAuthServiceProvider authProviders,
			@RequireNew Provider<AuthInitializationService> authInitProvProv,
			Provider<AuthSessionService> sessionProviderProvider,
			ICoreUserService userServices) {
		this.initProviderProvider = authInitProvProv;
		this.authSourceProvider = authProviders;
		this.sessionProviderProvider = sessionProviderProvider;
		this.userServices = userServices;
	}

	@Override
	public ValidateEmailAddressResult execImpl(ValidateEmailAddress a,
			ExecutionContext ec) {
		if ((a.getEmailAddress() == null)
				|| (a.getEmailAddress().length() <= 0)
				|| (!AuthUtils.validateEmailAddress(a.getEmailAddress()))) {
			throw new InvalidEmailAddressFormatException();
		}

		Logger.debug("Validating " + a.getEmailAddress());

		String emailAddress = a.getEmailAddress();

		AuthSession session = sessionProviderProvider.get().get();

		if (session != null) {
			throw new AlreadyAuthenticatedException();
		}

		AuthInitializationService authInitService = initProviderProvider.get();

		AuthInitialization authInit = authInitService.get();

		AuthUser user = userServices.getUserFromEmailAddress(emailAddress);

		ValidateEmailAddressResult result;

		Map<String, IAuthService> authSources =
				authSourceProvider.getAuthServices();

		//If the returned user is null, the given email address is not known, 
		//so the result should indicate this and return the list of auth 
		//providers 
		if (user == null) {

			//Save the email address the user validated against with in the auth 
			//initialization.  This must match any imediately subsequent 
			//requests for regisration
			authInit.setEmailAddress(a.getEmailAddress());
			authInitService.save(authInit);

			ArrayList<AuthSourceInfo> authSourceInfos =
					new ArrayList<AuthSourceInfo>();

			for (IAuthService authService : authSources.values()) {
				authSourceInfos.add(authService.getInfo());
			}

			result = new ValidateEmailAddressResult(a.getEmailAddress(),
					authSourceInfos);
		} else {

			//else the user needs to be redirected to the registered user's
			//auth service's login url.

			String serviceName = user.getAuthServiceName();
			emailAddress = user.getSanitizedEmailAddress();

			IAuthService authService = authSources.get(serviceName);

			if (authService == null) {
				throw new AuthException("The auth service that user: "
						+ emailAddress + " used to "
						+ "authenticate, " + serviceName + " "
						+ "was not found in the list of auth sources");
			}

			authInit.setAuthServiceName(serviceName);
			authInit.setEmailAddress(emailAddress);
			authInit.setState(AuthInitializationState.authenticating);
			authInit.setServiceSpecificUserId(user.getServiceSpecificUserId());

			authInitService.save(authInit);

			result = new ValidateEmailAddressResult(authService.getSignInUrl());
		}

		return result;
	}

	@Override
	public Class<ValidateEmailAddress> getActionType() {
		return ValidateEmailAddress.class;
	}

	/**
	 * nothing to undo
	 *
	 * @param a
	 * @param r
	 * @param ec
	 * @throws ActionException
	 */
	@Override
	public void undoImpl(ValidateEmailAddress a, ValidateEmailAddressResult r,
			ExecutionContext ec) {
		//nothing to do
	}
}

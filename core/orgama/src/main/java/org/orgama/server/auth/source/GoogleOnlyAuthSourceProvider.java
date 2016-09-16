// <copyright file="GoogleOnlyAuthSourceProvider.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.server.auth.source;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * an implementation of the auth source provider that only returns the info for
 * google's openId authentication method
 * @author kguthrie
 */
public class GoogleOnlyAuthSourceProvider implements IAuthServiceProvider {

	private final Map<String, IAuthService> services;
    
    @Inject
    public GoogleOnlyAuthSourceProvider(
			GoogleAccountsAuthService googleAuthService) {
        services = new HashMap<String, IAuthService>();
		services.put(googleAuthService.getInfo().getResourceName(), 
				googleAuthService);
    }

	@Override
	public Map<String, IAuthService> getAuthServices() {
		return services;
	}

}

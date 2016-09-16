package org.orgama.server.auth.source;

import org.orgama.shared.auth.model.AuthSourceInfo;

/**
 * interface that defines the methods that are needed to interact with an auth 
 * source
 * @author kguthrie
 */
public interface IAuthService {
	
	AuthSourceInfo getInfo();
	
	String getSignInUrl();
	String getSignOutUrl(String serviceSpecificSessionId);
	
	String getServiceSpecificSessionId();
	String getServiceSpecificUserId(String serviceSpecificSessionId);
}

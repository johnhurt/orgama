package org.orgama.server.config;

import org.orgama.server.auth.DevModeFacebookAuthService;

/**
 * This is extension provides some useful tools for local, development-mode 
 * testing.  It is only used when the Orgama application is running locally
 * @author kguthrie
 */
public class DevModeExtension extends BaseExtensionModule {

	@Override
	protected void addHandlerBindings() {
		//No handlers
	}

	@Override
	protected void addServerBindings() {
		bind(DevModeFacebookAuthService.class).asEagerSingleton();
	}

}

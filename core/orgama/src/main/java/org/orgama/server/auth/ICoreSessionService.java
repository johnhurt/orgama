/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

import com.google.inject.Provider;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.shared.auth.model.AuthSession;
import org.orgama.shared.auth.model.AuthUser;

/**
 * Interface for a class that handles the core services related to session
 * @author kguthrie
 */
public interface ICoreSessionService extends Provider<AuthSession> {
    
    public AuthSession create(AuthUser user, AuthInitialization authInit);
	public void close(AuthSession session);
	public void closeCurrent();
}

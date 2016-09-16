/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

import com.google.inject.Provider;
import org.orgama.shared.auth.model.AuthSession;

/**
 *
 * @author kguthrie
 */
public interface ISessionProvider extends Provider<AuthSession> {
    
}

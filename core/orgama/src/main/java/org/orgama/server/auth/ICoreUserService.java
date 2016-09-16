/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.shared.auth.model.AuthUser;

/**
 * interface for a class that handles all the core user service functions
 * @author kguthrie
 */
public interface ICoreUserService {
    
    public AuthUser getUserById(long userId);
    
    public AuthUser getUserFromEmailAddress(String emailAddress);
    
    public AuthUser registerNewUser(AuthInitialization authInit);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

import com.googlecode.objectify.Objectify;
import java.util.HashSet;
import java.util.List;
import org.orgama.shared.auth.model.AuthRight;
import org.orgama.shared.auth.model.AuthSession;
import org.orgama.shared.auth.model.AuthUser;

/**
 * Interface for a class that provides rights management for users and sessions
 * @author kguthrie
 */
public interface ICoreRightsServices {
    
    public void setSessionRightsForUser(AuthUser user, AuthSession session, 
            Objectify objectify);
    
    public boolean isUserAdmin(long userId, Objectify objectify);
    
    public AuthSession assertCurrentUserHasRights(AuthRight[] rights, 
            Objectify objectify);
    
    public List<Long> getDefaultGroupsForUser(AuthUser user, Objectify objectify);
}

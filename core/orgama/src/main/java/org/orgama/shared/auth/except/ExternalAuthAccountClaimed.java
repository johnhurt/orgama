/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.auth.except;

/**
 *
 * @author kguthrie
 */
public class ExternalAuthAccountClaimed extends AuthException {
    
    public ExternalAuthAccountClaimed() {
        super(AuthErrorCodes.externalAuthAccountClaimed);
    }
    
    public ExternalAuthAccountClaimed(Throwable cause) {
        super(AuthErrorCodes.externalAuthAccountClaimed, cause);
    }
    
}

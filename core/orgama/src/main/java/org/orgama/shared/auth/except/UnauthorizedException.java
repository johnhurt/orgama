/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.auth.except;

/**
 * Exception indicating that the user attempted to perform an action they were
 * not authorized to perform
 * @author kguthrie
 */
public class UnauthorizedException extends AuthException {
    
    public UnauthorizedException() {
        super(AuthErrorCodes.unauthorized);
    }
    
    
    public UnauthorizedException(Throwable ex) {
        super(AuthErrorCodes.unauthorized, ex);
    }
}

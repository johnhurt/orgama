/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.auth.except;

/**
 * Exception indicating that a request with required auth fields was given an
 * incomplete list of auth fields
 * @author kguthrie
 */
public class MissingAuthFieldException extends AuthException {

    public MissingAuthFieldException() {
        super(AuthErrorCodes.missingAuthField);
    }
    
    public MissingAuthFieldException(Throwable cause) {
        super(AuthErrorCodes.missingAuthField, cause);
    }
    
}

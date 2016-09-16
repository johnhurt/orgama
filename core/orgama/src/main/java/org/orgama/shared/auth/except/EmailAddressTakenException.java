// <copyright file="EmailAddressTakenException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.except;

/**
 * Indicates that the email address specified in registration is already taken
 * @author kguthrie
 */
public class EmailAddressTakenException extends AuthException {
    
    public EmailAddressTakenException() {
        super(AuthErrorCodes.emailAddressTaken);
    }
    
    public EmailAddressTakenException(Throwable cause) {
        super(AuthErrorCodes.emailAddressTaken, cause);
    }
    
}

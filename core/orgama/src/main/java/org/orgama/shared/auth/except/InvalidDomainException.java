// <copyright file="InvalidDomainException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.shared.auth.except;

/**
 * This exception indicates that the domain was invalid.  This can mean is was
 * null, empty, or in a format that cannot be made into a url
 * @author kguthrie
 */
public class InvalidDomainException extends AuthException {
    
    public InvalidDomainException() {
        super(AuthErrorCodes.invalidDomain);
    }
    
}

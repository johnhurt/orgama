// <copyright file="DomainMismatchException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.shared.auth.except;

/**
 * This exception indicates that the domain given did not match the domain it
 * was issued from or assigned to 
 * @author kguthrie
 */
public class DomainMismatchException extends AuthException {
    
    public DomainMismatchException() {
        super(AuthErrorCodes.domainMismatch);
    }
    
}

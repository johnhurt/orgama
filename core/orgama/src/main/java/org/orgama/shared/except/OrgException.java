// <copyright file="OrgException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://rookandpawn.com/

package org.orgama.shared.except;

/**
 * Mvp specific exception
 * @author Kevin Guthrie
 */
public class OrgException extends RuntimeException{

    protected CondensedThrowable realCause;
    
    /**
     * create an mvp exception with the given message and the given cause
     * @param message
     * @param cause 
     */
    public OrgException(String message, Throwable cause) {
        super(message);
        realCause = CondensedThrowable.from(cause);
    }
    
    /**
     * create an mvp from another exception as the cause
     * @param cause 
     */
    public OrgException(Throwable cause) {
        super("Mvp Exception thrown from an external cause");
        realCause = CondensedThrowable.from(cause);
    }
    
    /**
     * create an mvp exception with only a message
     * @param message 
     */
    public OrgException(String message) {
        super(message);
    }
    
    @Override
    public Throwable getCause() {
        return realCause;
    }
    
    public void clearCause() {
        realCause = null;
    }
}

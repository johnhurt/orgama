// <copyright file="ExceptionByErrorCode.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.except;

import org.orgama.shared.Logger;

/**
 * This exception can be thrown on either the client or server.  It contains a 
 * code for the actual error it represents, so when it is processed, the message
 * displayed for it can be looked up on a client side and displayed to the user
 * in the locale they are using.  The preferred format for exception codes is
 * a three letter module id followed by an integer.  
 * 
 * For example, all errors originating from the rook and pawn mvp extension 
 * library will have the module code "MVP", and those originating from the 
 * rook and pawn authentication library will start with the module code "AUT".
 * @author kguthrie
 */
public class ExceptionByErrorCode extends OrgException {

    
    protected static final String className = 
            ExceptionByErrorCode.class.getName();
    
    /**
     * searches through the class structure of the throwable, to determine if 
     * it is a instance of exception by error code;
     * @param t
     * @return 
     */
    public static boolean isInstance(Class c) {
        String cName;
        
        if (c == null) {
            return false;
        }
        
        Logger.debug("Checking " + c.getName() + " against " + 
                className);
        
        if (c.isInterface() || c.isPrimitive()) {
            return false;
        }
        
        cName = c.getName();
        
        if ((cName == null) || (cName.length() <= 0)) {
            return false;
        }
        
        cName = cName.toLowerCase();
        
        if (cName.equalsIgnoreCase(className)) {
            return true;
        }
        
        return isInstance(c.getSuperclass());
    }
    
    protected String errorCode;
    protected String extraMessage;
    protected boolean hasExtraMessage;
    
    /**
     * Create an instance of this exception with the given error code as a 
     * string.  
     * @param errorCode 
     */
    protected ExceptionByErrorCode(String errorCode) {
        super("Error: " + errorCode);
        this.errorCode = errorCode;
        extraMessage = super.getMessage();
        hasExtraMessage = false;
    } 
    
    /**
     * Create an exception from the constituent parts of the error code. 
     * @param moduleCode
     * @param errorNumber 
     */
    public ExceptionByErrorCode(String moduleCode, int errorNumber) {
        this(moduleCode + errorNumber);
    }
    
    /**
     * Create an instance of this exception with the given error code as a 
     * string.  This constructor also allows for an additional message string 
     * that will be provided to the client as given.  This is really only useful
     * for debugging
     * @param errorCode 
     */
    protected ExceptionByErrorCode(String errorCode, String message) {
        super("Error: " + errorCode);
        this.errorCode = errorCode;
        extraMessage = message;
        hasExtraMessage = true;
    } 
    
    /**
     * Create an exception from the constituent parts of the error code. 
     * This constructor also allows for an additional message string 
     * that will be provided to the client as given.  This is really only useful
     * for debugging
     * @param moduleCode
     * @param errorNumber 
     */
    public ExceptionByErrorCode(String moduleCode, int errorNumber, 
            String message) {
        this(moduleCode + errorNumber, message);
    }
    
    /**
     * Create an instance of this exception with the given error code as a 
     * string.  The causing exception can also be added
     * @param errorCode 
     */
    protected ExceptionByErrorCode(String errorCode, Throwable cause) {
        super("Error: " + errorCode, cause);
        this.errorCode = errorCode;
        extraMessage = super.getMessage();
        hasExtraMessage = false;
    } 
    
    /**
     * Create an exception from the constituent parts of the error code. 
     * The causing exception can also be added
     * @param moduleCode
     * @param errorNumber 
     */
    public ExceptionByErrorCode(String moduleCode, int errorNumber, 
            Throwable cause) {
        this(moduleCode + errorNumber, cause);
    }
    
    /**
     * Create an instance of this exception with the given error code as a 
     * string.  This constructor also allows for an additional message string 
     * that will be provided to the client as given.  This is really only useful
     * for debugging. The causing exception can also be added
     * @param errorCode 
     */
    protected ExceptionByErrorCode(String errorCode, String message, 
            Throwable cause) {
        super("Error: " + errorCode, cause);
        this.errorCode = errorCode;
        extraMessage = message;
        hasExtraMessage = true;
    } 
    
    /**
     * Create an exception from the constituent parts of the error code. 
     * This constructor also allows for an additional message string 
     * that will be provided to the client as given.  This is really only useful
     * for debugging.  The causing exception can also be added
     * @param moduleCode
     * @param errorNumber 
     */
    public ExceptionByErrorCode(String moduleCode, int errorNumber, 
            String message, Throwable cause) {
        this(moduleCode + errorNumber, message, cause);
    }
    
    /**
     * get the error code for this exception
     * @return 
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * gets the extra message
     * @return 
     */
    public String getExtraMessage() {
        return extraMessage;
    }
    
    /**
     * returns whether or not this exception has additional message info to be 
     * shown
     * @return 
     */
    public boolean hasExtraMessage() {
        return hasExtraMessage;
    }
}

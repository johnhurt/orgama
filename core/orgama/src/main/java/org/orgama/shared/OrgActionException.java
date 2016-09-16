// <copyright file="OrgActionException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared;

import com.gwtplatform.dispatch.shared.ActionException;
import org.orgama.shared.except.ExceptionByErrorCode;

/**
 * This action shouldn't be thrown anywhere.  It should only be used as a 
 * container to pass exceptions on the serverside to the client side safely
 * and without obsuring them
 * @author kguthrie
 */
public class OrgActionException extends ActionException {

    protected ExceptionByErrorCode realCause;
    
    /**
     * for serialization
     */
    OrgActionException() {
        
    }
    
    /**
     * converts the given exception into an rpAction exception.  This means
     * setting the cause of this exception to be ebe, and setting the 
     * cause of ebe to be null.  This hides internal exceptions and 
     * prevents serialization errors
     * @param ebe 
     */
    public OrgActionException(ExceptionByErrorCode ebe) {
        super(ebe.getMessage());
        
        realCause = ebe;
        
        ebe.clearCause();
    }
    
    @Override
    public Throwable getCause() {
        return realCause;
    }
    
}

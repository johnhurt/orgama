// <copyright file="BaseErrorMessageLookup.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client.except;

/**
 * base class for error message lookups.  error lookups extending this class
 * will register themselves in the central registry for error messages
 * @author kguthrie
 */
public abstract class BaseErrorMessageLookup implements ErrorMessageLookup {
    
    protected String moduleCode;
    
    /**
     * create the lookup with the module code.  This allows the leaking this
     * to be registered in the error module registry without causing problems
     * @param moduleCode 
     */
    public BaseErrorMessageLookup(String moduleCode) {
        this.moduleCode = moduleCode;
        
        ErrorMessageRegistry.registerLookup(this);
    }
    
    @Override
    public String getModuleCode() {
        return moduleCode;
    }
    
}

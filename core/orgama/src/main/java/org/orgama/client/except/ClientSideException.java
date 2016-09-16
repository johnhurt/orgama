// <copyright file="ClientSideException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client.except;

import org.orgama.shared.Logger;
import org.orgama.shared.enm.OrgErrorCodes;
import org.orgama.shared.except.ExceptionByErrorCode;
import org.orgama.shared.except.OrgException;

/**
 * Exception that can be caught on the client side from either the serverside 
 * or the clientside.  This is most useful when used with the exception by 
 * error code.  This allows the server or client to throw an exception with an
 * error code and have the localized message for that error code available to 
 * the client with this exception
 * @author kguthrie
 */
public class ClientSideException extends OrgException {

    String message;
    private String errorCode;
    
    /**
     * Create a client side exception by looking up the localized message for
     * an exception by error code
     * @param ebe 
     */
    public ClientSideException(ExceptionByErrorCode ebe) {
        super(ebe);
        
        if ((ebe == null) || (ebe.getErrorCode() == null) ||
                (ebe.getErrorCode().length() <= 0)) {
            message = ErrorMessageRegistry.getMessageForCode(
                    OrgErrorCodes.MODULE_CODE, OrgErrorCodes.UNKOWN_ERROR);
            errorCode = OrgErrorCodes.MODULE_CODE + OrgErrorCodes.UNKOWN_ERROR;
            return;
        }
        
        try {
            errorCode = ebe.getErrorCode();
            message = ErrorMessageRegistry.getMessageForCode(
                    ebe.getErrorCode());
        }
        catch(Exception ex) {
            Logger.warn("Error getting message for code: " + 
                    ebe.getErrorCode());
        }
        
        if ((message == null) || (message.length() <= 0)) {
            message = ErrorMessageRegistry.getMessageForCode(
                    OrgErrorCodes.MODULE_CODE, OrgErrorCodes.UNKOWN_ERROR);
        }
        
        if (ebe.hasExtraMessage()) {
            message += ": " + ebe.getExtraMessage();
        }
    }
    
    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
    
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return (errorCode == null)?(""):(errorCode);
    }

}

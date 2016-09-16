/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.except;

import org.orgama.shared.enm.OrgErrorCodes;

/**
 *
 * @author kguthrie
 */
public class OrgamaCoreException extends ExceptionByErrorCode {

    /**
     * create an Auth Exception with a message and cause
     * @param message
     * @param cause 
     */
    public OrgamaCoreException(int errorCode, Throwable cause) {
        super(OrgErrorCodes.MODULE_CODE, errorCode, cause);
    }

    /**
     * create an AuthException with just a message
     * @param message 
     */
    public OrgamaCoreException(int errorCode) {
        super(OrgErrorCodes.MODULE_CODE, errorCode);
    }
    
    /**
     * create a new unknown auth exception with this essage
     * @param message 
     */
    public OrgamaCoreException(String message) {
        super(OrgErrorCodes.MODULE_CODE, OrgErrorCodes.UNKOWN_ERROR, 
                message);
    }
    
    /**
     * create a new 
     * @param message
     * @param cause 
     */
    public OrgamaCoreException(String message, Throwable cause) {
        super(OrgErrorCodes.MODULE_CODE, OrgErrorCodes.UNKOWN_ERROR, 
                message, cause);
    }

}

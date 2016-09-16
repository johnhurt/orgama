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
public class DataAccessException extends OrgamaCoreException {
    
    public DataAccessException() {
        super(OrgErrorCodes.DATA_ACCESS_ERROR);
    }
    
    public DataAccessException(Throwable t) {
        super(OrgErrorCodes.DATA_ACCESS_ERROR, t);
    }
    
    
}

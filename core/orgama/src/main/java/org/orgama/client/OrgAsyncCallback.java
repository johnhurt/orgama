/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.orgama.client;

import org.orgama.shared.Logger;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.orgama.client.except.ClientSideException;
import org.orgama.shared.OrgActionException;
import org.orgama.shared.enm.OrgErrorCodes;
import org.orgama.shared.except.ExceptionByErrorCode;

/**
 * Simplified async callback that 
 * @author kguthrie
 */
public abstract class OrgAsyncCallback<T> implements AsyncCallback<T> {
    
    @Override
    public void onFailure(Throwable t) {
		
		ExceptionByErrorCode ebe;
		
		if (t instanceof OrgActionException) {
			 ebe = (ExceptionByErrorCode)t.getCause();
		}
		else {
			ebe = getEbeCause(t);
		}
        
        if (ebe == null) {
            ebe = new ExceptionByErrorCode(OrgErrorCodes.MODULE_CODE, 
                    OrgErrorCodes.UNKOWN_ERROR, t.getMessage(), t);
        }
            
        Logger.error("Error: " + t.getClass().getName() +
                " - " + t.getMessage(), t);
        
        onFailure(new ClientSideException(ebe));
    }
    
    /**
     * get the exception by error code that caused the 
     * @param ex
     * @return 
     */
    protected ExceptionByErrorCode getEbeCause(Throwable ex) {
        
        if (ex == null) {
            return null;
        }
        
        Logger.debug("checking instance of");
        
        try {
            if (ExceptionByErrorCode.isInstance(ex.getClass())) {
                Logger.debug("was instance");
                return (ExceptionByErrorCode)ex;
            }
        }
        catch(Exception e) {
            Logger.warn("Exception while getting the ebe cause of " + 
                    ex.getClass().getName());
        }
        
        Logger.debug("wasn't instance");
        
        Logger.debug("trying next on " + ex.getCause());
        return getEbeCause(ex.getCause());
    }
    
    public abstract void onFailure(ClientSideException ex);
}

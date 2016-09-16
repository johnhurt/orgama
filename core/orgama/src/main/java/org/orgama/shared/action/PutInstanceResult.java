/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.action;

import org.orgama.shared.IHasId;

/**
 * result of storing on object on the server
 * @author kguthrie
 */
public class PutInstanceResult extends AbstractResult {
    
    private IHasId<?> result;
    private IHasId<?> replaced;
    
    /** For serialization */
    PutInstanceResult() 
    { 
    }
    
    /** create a result based on the stored result from the server */
    public PutInstanceResult(PutInstanceResponse<? extends IHasId<?>> respnse) {
        this.result = respnse.putInstance;
        this.replaced = respnse.replaced;
    }
    
    public IHasId<?> getResult() {
        return result;
    }

    /**
     * @return the repaced
     */
    public IHasId<?> getRepaced() {
        return replaced;
    }

}

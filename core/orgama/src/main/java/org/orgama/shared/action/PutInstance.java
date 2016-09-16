/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.action;

import org.orgama.shared.IHasId;

/**
 * Action to put an instance of a class into persistent storage on the server
 * @author kguthrie
 */
public class PutInstance extends AbstractAction<PutInstanceResult> {
    
    private IHasId<?> toPut;
    
    /** For serialization */
    PutInstance() {}
    
    public PutInstance(IHasId<?> toPut) {
        this.toPut = toPut;
    }
    
    public IHasId<?> getInstance() {
        return toPut;
    }
}

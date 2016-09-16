/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.handler;

import com.gwtplatform.dispatch.server.ExecutionContext;
import org.orgama.server.SimpleDataServices;
import org.orgama.shared.action.PutInstance;
import org.orgama.shared.action.PutInstanceResult;

/**
 *
 * @author kguthrie
 */
public class PutInstanceHandler 
        extends AbstractHandler<PutInstance, PutInstanceResult> {

    @Override
    public PutInstanceResult execImpl(PutInstance a, ExecutionContext ec) {
        return new PutInstanceResult(SimpleDataServices.put(a.getInstance()));
    }

    @Override
    public void undoImpl(PutInstance a, PutInstanceResult r, 
            ExecutionContext ec) {
        if (r.getResult() == null) {
            return; // because nothing happened
        }
        
        //If the put instance replaced another instance, put the replaced 
        //instance back
        if (r.getRepaced() != null) {
            SimpleDataServices.put(r.getRepaced());
        }
        else {
            
        }
    }

    @Override
    public Class<PutInstance> getActionType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

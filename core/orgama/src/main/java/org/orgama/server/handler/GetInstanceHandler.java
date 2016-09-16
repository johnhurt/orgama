/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.handler;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import org.orgama.server.SimpleDataServices;
import org.orgama.shared.action.GetInstance;
import org.orgama.shared.action.GetInstanceResult;

    
/**
 * Handles getting single instances
 * @author kguthrie
 */
public class GetInstanceHandler 
        extends AbstractHandler<GetInstance, 
                GetInstanceResult> {

    @Inject
    public GetInstanceHandler(SimpleDataServices services) {
        
    }
    
    @Override
    public GetInstanceResult execImpl(GetInstance a, 
            ExecutionContext ec) {
        return new GetInstanceResult(SimpleDataServices.get(
                a.getClassType(), a.getKey()));
    }

    @Override
    public void undoImpl(GetInstance a, GetInstanceResult r, 
            ExecutionContext ec) {
        //Nothing to do here
    }

    @Override
    public Class<GetInstance> getActionType() {
        return GetInstance.class;
    }

}

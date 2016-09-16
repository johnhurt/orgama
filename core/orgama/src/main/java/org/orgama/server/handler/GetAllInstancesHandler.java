/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.handler;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import org.orgama.server.SimpleDataServices;
import org.orgama.shared.action.GetAllInstances;
import org.orgama.shared.action.GetAllInstancesResult;

    
/**
 * Handles getting all instances
 * @author kguthrie
 */
public class GetAllInstancesHandler 
        extends AbstractHandler<GetAllInstances, 
                GetAllInstancesResult> {

    @Inject
    public GetAllInstancesHandler(SimpleDataServices services) {
        
    }
    
    @Override
    public GetAllInstancesResult execImpl(GetAllInstances a, 
            ExecutionContext ec) {
        return new GetAllInstancesResult(SimpleDataServices.getAll(
                a.getClassType(), a.getPresentIds()));
    }

    @Override
    public void undoImpl(GetAllInstances a, GetAllInstancesResult r, 
            ExecutionContext ec) {
        //Nothing to do here
    }

    @Override
    public Class<GetAllInstances> getActionType() {
        return GetAllInstances.class;
    }

}

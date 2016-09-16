/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.action;

import org.orgama.shared.IHasId;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * respresents the result of getting all serverside instances of the given
 * class. 
 * @author kguthrie
 */
public class GetAllInstancesResult 
        extends AbstractResult implements Serializable {
    
    public ArrayList<IHasId<?>> results;

    /** For serialization */
    GetAllInstancesResult() {}
    
    public GetAllInstancesResult(ArrayList<IHasId<?>> results) {
        this.results = results;
    }
    
    /**
     * get the results of the get all query
     * @return 
     */
    public ArrayList<IHasId<?>> getResults() {
        return results;
    }
}

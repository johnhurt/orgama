/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.action;

import org.orgama.shared.IHasId;
import java.io.Serializable;

/**
 *
 * @author kguthrie
 */
public class GetInstanceResult extends AbstractResult 
        implements Serializable{
    
    //data to be returned
    protected IHasId<?> data;

    GetInstanceResult() {}
    
    /**
     * create a get data result from the given action
     * @param <A>
     * @param action
     */
    public GetInstanceResult(IHasId<?> data) {
        this.data = data;
    }

    /**
     * @param data the data to set
     */
    public void setData(IHasId<?> data) {
        this.data = data;
    }

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

}

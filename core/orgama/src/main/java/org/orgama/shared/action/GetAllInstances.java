/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.action;

import org.orgama.shared.IHasId;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Action to get all server side instances of the given class
 * @author kguthrie
 */
public class GetAllInstances
        extends AbstractAction<GetAllInstancesResult> implements Serializable {
    
    private String clazz;
    
    private ArrayList<?> presentIds;
    
    /** for serialization */
    GetAllInstances() {}
    
    public GetAllInstances(Class<IHasId<?>> clazz) {
        this.clazz = clazz.getName();
        presentIds = new ArrayList<Object>();
    }
    
    public GetAllInstances(Class<IHasId<?>> clazz, ArrayList<?> presentIds) {
        this.clazz = clazz.getName();
        
        if (presentIds == null) {
            this.presentIds = new ArrayList<Object>();
        }
        else {
            this.presentIds = presentIds;
        }
    }
    
    public String getClassType() {
        return clazz;
    }
    
    public ArrayList<?> getPresentIds() {
        return null;//presentIds;
    }
}

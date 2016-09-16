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
public class GetInstance extends 
        AbstractAction<GetInstanceResult> implements Serializable {

    private String clazz;
    private Object id;

    GetInstance() {}
    
    public GetInstance(Class<IHasId<?>> clazz, Object id) {
        this.clazz = clazz.getName();
        this.id = id;
    }

    public Object getKey() {
        return id;
    }

    public String getClassType() {
        return clazz;
    }
}

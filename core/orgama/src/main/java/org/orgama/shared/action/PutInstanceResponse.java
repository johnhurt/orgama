/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.action;

import org.orgama.shared.IHasId;

/**
 * Objet to be returned from a call to put instance in a simple data store.  
 * This class contains an instance of the object that put and a copy of the 
 * object that replaced this one.  Either of these fields can be null
 * 
 * @author kguthrie
 */
public class PutInstanceResponse<T extends IHasId<?>> {
    
    public T putInstance;
    public T replaced;
    
}

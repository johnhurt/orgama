// <copyright file="GetTypedInstance.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.shared.action;

import org.orgama.shared.IHasId;
import java.io.Serializable;

/**
 * Base action for retrieving data from the server using a string as a key or
 * query
 * @author kguthrie
 */
public class GetTypedInstance<K, T extends IHasId<K>>
        extends AbstractAction<GetTypedInstanceResult<K, T>> implements Serializable {

    private Class<T> clazz;
    private K id;
    
    /** For Serialization */
    GetTypedInstance() {}

    /**
     * Createa a get data action to get a singe object of the given type with 
     * the given id
     * @param clazz
     * @param id 
     */
    public GetTypedInstance(Class<T> clazz, K id)
    {
        this.clazz = clazz;
        this.id = id;
    }
    
    public K getKey() {
        return id;
    }

    public Class<T> getClassType() {
        return clazz;
    }
    
}

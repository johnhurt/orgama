/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.auth.model;

import org.orgama.shared.IHasId;
import java.io.Serializable;
import javax.persistence.Id;

/**
 * Represents one unit of authorization.  This is the link between 
 * @author kguthrie
 */
public class AuthRight implements Serializable, IHasId<Long> {
    
    @Id
    private Long authRightId;
    private String name;

    AuthRight() {}

    /**
     * Create a new auth right with the given name
     * @param name 
     */
    public AuthRight(String name) {
        this.name = name;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public Long getId() {
        return authRightId;
    }

	public String getIdFieldName() {
        return "authRightId";
    }
    
}

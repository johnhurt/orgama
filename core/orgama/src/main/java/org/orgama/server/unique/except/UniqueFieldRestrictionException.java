package org.orgama.server.unique.except;

import org.orgama.shared.except.OrgException;

/**
 *
 * @author kguthrie
 */
public class UniqueFieldRestrictionException extends OrgException {
	
	public UniqueFieldRestrictionException(String fieldDescripter) {
		super("Value for field: " + fieldDescripter + " is not unique.");
	}
	
}

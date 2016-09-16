package org.orgama.server.unique.except;

import java.lang.reflect.Field;
import org.orgama.shared.except.OrgException;

/**
 *
 * @author kguthrie
 */
public class UniqueFieldsMustBeIndexedException extends OrgException {

	public UniqueFieldsMustBeIndexedException(Class<?> clazz, Field field) {
		super("Unique field: " + clazz.getSimpleName() + "." + field.getName() +
				" is not indexed by having the @Index annotation");
	}
	
}

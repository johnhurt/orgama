package org.orgama.shared.unique;

/**
 * Interface for a class that handles unique fields for an objectified entity
 * @author kguthrie
 */
public interface IUniqueFieldHandler {
	
	public <T extends HasIdAndUniqueFields<?>> void handleUniqueFields( 
			T instance);
	
}

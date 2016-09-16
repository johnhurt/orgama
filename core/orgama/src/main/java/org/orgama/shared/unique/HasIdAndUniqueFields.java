package org.orgama.shared.unique;

import com.googlecode.objectify.annotation.OnSave;
import org.orgama.shared.IHasId;

/**
 * base class for an entity that has unique fields.
 * @author kguthrie
 */
public abstract class HasIdAndUniqueFields<T> implements IHasId<T> {

	private static final transient Object syncLock = new Object();
	private static transient IUniqueFieldHandler uniqueFieldHandler = null;

	/**
	 * @param uniqueFieldHandler the uniqueFieldHandler to set
	 */
	public static void setUniqueFieldHandler(
			IUniqueFieldHandler uniqueFieldHandler) {
		if (HasIdAndUniqueFields.uniqueFieldHandler == null) {
			synchronized(syncLock) {
				if (HasIdAndUniqueFields.uniqueFieldHandler == null) {
					HasIdAndUniqueFields.uniqueFieldHandler = 
							uniqueFieldHandler;
				}
			}
		}
		
	}
	
	/**
	 * use the unique field handler to ensure the uniqueness of all the 
	 * unique fields in this entity.  This is done just before saving the object
	 */
	@OnSave
	public synchronized void handleUniqueFields() {
		uniqueFieldHandler.handleUniqueFields(this);
	}
}

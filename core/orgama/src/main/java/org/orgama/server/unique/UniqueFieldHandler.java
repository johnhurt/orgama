package org.orgama.server.unique;

import com.googlecode.objectify.Work;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.orgama.server.Ofy;
import org.orgama.server.unique.except.UniqueFieldRestrictionException;
import org.orgama.shared.unique.HasIdAndUniqueFields;
import org.orgama.shared.unique.IUniqueFieldHandler;

/**
 * base class for an entity that has fields that need to be kept unique
 * @author kguthrie
 */
public class UniqueFieldHandler implements IUniqueFieldHandler {

	private final Map<Class<?>, List<FieldValueLock>> uniqueFields;
	
	public UniqueFieldHandler() {
		uniqueFields = new HashMap<Class<?>, List<FieldValueLock>>();
	}
	
	/**
	 * Add a new field that needs to be unique
	 * @param clazz
	 * @param field 
	 */
	public void addUniqueField(Class<?> clazz, Field field) {
		
		List<FieldValueLock> fields = uniqueFields.get(clazz);
		
		if (fields == null) {
			fields = new LinkedList<FieldValueLock>();
			uniqueFields.put(clazz, fields);
		}
		
		fields.add(FieldValueLock.getForField(field));
	}

	/**
	 * returns true if the given class is already in the local map, and false
	 * otherwise
	 * @param clazz
	 * @return 
	 */
	public boolean isRegistered(Class<?> clazz) {
		return uniqueFields.containsKey(clazz);
	}
	
	/**
	 * Iterate over all the unique fields of a given object (based on its class)
	 * @param clazz
	 * @param instance 
	 */
	@Override
	public <T extends HasIdAndUniqueFields<?>> void handleUniqueFields(
			final T instance) {
		final List<FieldValueLock> locks = 
				uniqueFields.get(instance.getClass());
		
		if (locks == null || locks.isEmpty()) {
			return;
		}
		
		UniqueFieldVerificationResult result = Ofy.transactNew(
				new Work<UniqueFieldVerificationResult>() {

			@Override
			public UniqueFieldVerificationResult run() {
				UniqueFieldVerificationResult result = 
						UniqueFieldVerificationResult.success();
				
				for (FieldValueLock lock : locks) {
					if (!lock.lock(instance)) {
						result = UniqueFieldVerificationResult.failed(lock);
						break;
					}
				}

				//If this operation was not successful, release any locks that
				//might have been locked
				if (!result.isSuccess()) {
					for (FieldValueLock lock : locks) {
						lock.release();
					}
				}
				
				return result;
			}
		});
		
		if (!result.isSuccess()) {
			throw new UniqueFieldRestrictionException(
					result.getErrorFieldDescription());
		}
	}
	
}

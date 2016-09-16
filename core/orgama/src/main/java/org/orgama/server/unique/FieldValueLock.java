package org.orgama.server.unique;

import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.lang.reflect.Field;
import org.orgama.server.Ofy;
import org.orgama.shared.Logger;
import org.orgama.shared.unique.HasIdAndUniqueFields;

/**
 * 
 * @author kguthrie
 */
@Entity
public class FieldValueLock {

	private static final transient String splitStringPattern = "%s-%s";
	
	/**
	 * get a lock object that can be used as a template to lock a single 
	 * unique field
	 * @param field
	 * @return 
	 */
	public static FieldValueLock getForField(Field field) {
		return new FieldValueLock(field);
	}
	
	@Id
	private String fieldValue;
	
	private final transient String prefix;
	private final transient Class<?> clazz;
	private final transient Field field;
	
	private transient boolean isLocked;
	
	public FieldValueLock(Field field) {
		this.field = field;
		this.clazz = field.getDeclaringClass();
		this.prefix = String.format(splitStringPattern, 
				clazz.getCanonicalName(), 
				field.getName());
		isLocked = false;
	}
	
	/**
	 * for serialization
	 */
	FieldValueLock() {
		prefix = null;
		clazz = null;
		field = null;
		
	}

	/**
	 * @return the fieldValue
	 */
	private String getFieldValue() {
		return fieldValue;
	}

	/**
	 * @param fieldValue the fieldValue to set
	 */
	private void setFieldValue(Object fieldValue) {
		if (isLocked) {
			release();
		}
		
		this.fieldValue = String.format(splitStringPattern, prefix, fieldValue);
	}
	
	/**
	 * get the representation of the current field
	 * @return 
	 */
	@Override
	public String toString() {
		return prefix;
	}
	
	/**
	 * lock the field represented by this field lock and return the result of
	 * trying to lock it.
	 * @param instance
	 * @return 
	 */
	public <T extends HasIdAndUniqueFields<?>> boolean lock(T instance) {
		
		final FieldValueLock that = this;
		Object value = null;
		boolean result = false;
		
		try {
			value = field.get(instance);
		}
		catch(Exception ex) {
			return result;
		}
		
		if (value == null) {
			return true;
		}
		
		setFieldValue(value);
		
		if (!checkForExistingField(instance, value)) {
			return result;
		}
		
		try {
			result = Ofy.transactNew(new Work<Boolean>() {

				@Override
				public Boolean run() {
					FieldValueLock lock = 
							Ofy.load().type(FieldValueLock.class).id(
									getFieldValue()).get();

					if (lock != null) {
						return false;
					}
					
					if (Ofy.save().entity(that).now() == null) {
						return false;
					}
					
					return true;
				}
			});
			
		}
		catch(Exception ex) {
		}
		
		isLocked = true;
		return result;
	}
	
	/**
	 * Release the lock currently held by this field lock.  This method only
	 * needs to be called when this lock is successfully locked but a 
	 * subsequent lock on the same object fails
	 */
	public void release() {
		
		if (!isLocked) {
			return;
		}
		
		try {
			Ofy.transactNew(new VoidWork() {

				@Override
				public void vrun() {
					Ofy.delete().type(FieldValueLock.class).id(
							fieldValue).now();
				}
			});
		}
		catch(Exception ex) {
			Logger.warn("Error unlocking field: " + prefix);
		}
		isLocked = false;
	}
	
	/**
	 * determine if there is already an instance of a the type with a field 
	 * value that prevents the current object's field from being unique
	 * @param <T>
	 * @param instance
	 * @param value
	 * @return 
	 */
	private <T extends HasIdAndUniqueFields<?>> boolean checkForExistingField(
			T instance, Object value) {
		boolean result = true;
		
		try {
			for (Object i : 
					Ofy.transactionless().load().type(clazz).filter(
							field.getName(), value).iterable()) {
				T curr = (T)i;
				if (!curr.getId().equals(instance.getId())) {
					result = false;
					break;
				}
			}
		}
		catch(Exception ex) {
			Logger.error("Error checking for existing overriding unique field",
					ex);
		}
		
		return result;
	}
}

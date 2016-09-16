package org.orgama.server;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.cmd.Deleter;
import com.googlecode.objectify.cmd.Loader;
import com.googlecode.objectify.cmd.Saver;
import java.lang.reflect.Field;
import org.orgama.server.annotation.Unique;
import org.orgama.server.unique.FieldValueLock;
import org.orgama.server.unique.UniqueFieldHandler;
import org.orgama.server.unique.except.UniqueFieldsMustBeIndexedException;
import org.orgama.shared.unique.HasIdAndUniqueFields;


/**
 * Static wrapper for objectify
 * @author kguthrie
 */
public class Ofy {

	private static UniqueFieldHandler uniqueFieldHandler = null;

	public static Saver save() {
		return ofy().save();
	}

	public static Loader load() {
		return ofy().load();
	}

	public static Deleter delete() {
		return ofy().delete();
	}

	public static <R> R transact(Work<R> work) {
		return ofy().transact(work);
	}

	public static <R> R transactOnce(Work<R> work) {
		return ofy().transactNew(0, work);
	}

	public static <R> R transactNew(Work<R> work) {
		return ofy().transactNew(work);
	}

	public static <R> R transactNew(int tryCount, Work<R> work) {
		return ofy().transactNew(tryCount, work);
	}

	public static Objectify transactionless() {
		return ofy().transactionless();
	}

	/**
	 * This method registers the given class with objectify, but orgama hijacks
	 * it to add features like unique fields
	 * @param clazz
	 */
	public synchronized static void register(Class<?> clazz) {

		if (HasIdAndUniqueFields.class.isAssignableFrom(clazz)) {
			handleClassWithUniqueFields(clazz);
		}

		ObjectifyService.register(clazz);
	}

	/**
	 * handle the registration necessary to make unique fields work
	 */
	private static void handleClassWithUniqueFields(
			Class<?> clazz) {

		if (uniqueFieldHandler == null) {
			uniqueFieldHandler = new UniqueFieldHandler();
			HasIdAndUniqueFields.setUniqueFieldHandler(uniqueFieldHandler);
			Ofy.register(FieldValueLock.class);
		}

		if (uniqueFieldHandler.isRegistered(clazz)) {
			return;
		}

		Class<?> curr = clazz;

		do {
			handleUniqueFieldsForClass(clazz, curr);
			curr = curr.getSuperclass();
		} while(curr != Object.class);
	}

	private static void handleUniqueFieldsForClass(Class<?> targetClass,
			Class<?> currClass) {

		Field[] fields = currClass.getDeclaredFields();

		for (Field field : fields) {
			if (field.isAnnotationPresent(Unique.class)) {
				if (!field.isAnnotationPresent(Index.class)) {
					throw new UniqueFieldsMustBeIndexedException(
							targetClass, field);
				}

				field.setAccessible(true);
				uniqueFieldHandler.addUniqueField(targetClass, field);
			}
		}
	}

}

package org.orgama.structure;

import com.google.inject.Inject;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 *
 * @author kguthrie
 */
public abstract class ClassWrapperRef {
	
	private String genClassName;
	private String genClassPackage;
	private File destinationFile;
	private Class<?> wrappedClass;
	private final Type[] constructorArgs;
	
	public ClassWrapperRef(Class<?> wrappedClass) {
		
		this.wrappedClass = wrappedClass;
		genClassPackage = CodeAndFileGenerator.getPackage(
				wrappedClass, ".");
		genClassName = wrappedClass.getSimpleName() + 
				CodeAndFileGenerator.filePostfix;
		destinationFile = new File(String.format("%s/%s/%s.java",
				App.classPathRootFile.getAbsolutePath(),
				genClassPackage.replace(".", "/"), getGenClassName()));
		constructorArgs = findConstructorArgs(wrappedClass);
	}

	/**
	 * get a list of the arguments that should be used in the constructor
	 * for the class wrapping the parent class
	 * @param parentClass
	 * @return 
	 */
	private Type[] findConstructorArgs(Class<?> parentClass) {
		Constructor<?> best = null;
		Constructor<?>[] constructors = parentClass.getDeclaredConstructors();
		
		
		for (Constructor<?> c : constructors) {
			if (c.isAnnotationPresent(Inject.class)) {
				best = c;
				break;
			}
			
			if (best == null) {
				best = c;
			}
			else if (best.getParameterTypes().length > 
					best.getParameterTypes().length) {
				best = c;
			}
		}
		
		if (best != null) {
			return best.getGenericParameterTypes();
		}
		
		return null;
	}
	
	
	
	/**
	 * @return the destinationFile
	 */
	public File getDestinationFile() {
		return destinationFile;
	}

	/**
	 * @return the wrappedClass
	 */
	public Class<?> getWrappedClass() {
		return wrappedClass;
	}
	
	/**
	 * Get the canonical name of the wrapped class
	 * @return 
	 */
	public String getOriginalCanonicalName() {
		return wrappedClass.getCanonicalName();
	}
	
	/**
	 * get the canonical name of the generated class
	 * @return 
	 */
	public String getCanonicalName() {
		return String.format("%s.%s", getGenClassPackage(), getGenClassName());
	}
	
	/**
	 * Get the simple name of the wrapped class
	 * @return 
	 */
	public String getOriginalSimpleName() {
		return wrappedClass.getSimpleName();
	}

	/**
	 * @return the genClassName
	 */
	public String getGenClassName() {
		return genClassName;
	}

	/**
	 * @return the genClassPackage
	 */
	public String getGenClassPackage() {
		return genClassPackage;
	}
	
	/**
	 * generate the code defining 
	 * @return 
	 */
	public abstract String generateDefinition();

	/**
	 * @return the constructorArgs
	 */
	public Type[] getConstructorArgs() {
		return constructorArgs;
	}
}

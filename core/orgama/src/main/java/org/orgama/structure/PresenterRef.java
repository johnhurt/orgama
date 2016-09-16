package org.orgama.structure;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.proxy.Proxy;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.orgama.client.annotation.DisableCodeSplit;

/**
 * reflection of a Presenter class.  This wraps the information about a 
 * presenter as well as some of the code generation logic
 * @author kguthrie
 */
public class PresenterRef extends ClassWrapperRef {

	private static final String importPrefix =
			"package ";
	
	
	private static final String presenterBodyPrefix = 
			"public class ";
	
	private static final String presenterBodyPostfix = 
			"}\n" +
			"}";
	
	private Class<?> viewInterface;
	
	private String nameToken;
	private boolean disableCodeSplitting;
	
	private ProxyRef proxy;
	
	public PresenterRef(Class<?> presenterClass) {
		super(presenterClass);
		Class<?>[] internalClasses = presenterClass.getDeclaredClasses();

		for (Class<?> internalClass : internalClasses) {
			if (View.class.isAssignableFrom(internalClass)) {
				viewInterface = internalClass;
				break;
			}
		}
		
		if (getWrappedClass().isAnnotationPresent(NameToken.class)) {
			nameToken = getWrappedClass().getAnnotation(
					NameToken.class).value();
		}
		else {
			nameToken = "";
		}
		
		this.disableCodeSplitting = 
				getWrappedClass().isAnnotationPresent(
				DisableCodeSplit.class);
	}

	/**
	 * @return the viewInterface
	 */
	public Class<?> getViewInterface() {
		return viewInterface;
	}

	/**
	 * @return the nameToken
	 */
	public String getNameToken() {
		return nameToken;
	}

	/**
	 * @return the disableCodeSplitting
	 */
	public boolean isDisableCodeSplitting() {
		return disableCodeSplitting;
	}

	/**
	 * returns a string with all the required constructor arguments and a call
	 * to super with the same arguments
	 * @return 
	 */
	private String generateConstructor() {
		
		StringBuilder content = new StringBuilder();
		List<String> classNames = new LinkedList<String>();
		List<String> instNames = new LinkedList<String>();
		
		for (Type argType : getConstructorArgs()) {
			
			ParameterizedType pType = (argType instanceof ParameterizedType)
					? ((ParameterizedType)argType)
					: null;
			
			Class<?> argClass;
			
			if (pType != null) {
				argClass = (Class<?>)pType.getRawType();
			}
			else {
				argClass = (Class<?>)argType;
			}
			
			String className = argClass.getSimpleName();
			
			if (Proxy.class.isAssignableFrom(argClass)) {
				className = proxy.getGenClassName();
			}
			
			if (EventBinder.class.isAssignableFrom(argClass)) {
				className = String.format("%s.Binder", getOriginalSimpleName());
			}
			
			String instName = (Character.toLowerCase(className.charAt(0)) + 
					className.substring(1)).replaceAll("[\\.]", "");
			
			if (pType != null) {
				Type[] generics = pType.getActualTypeArguments();
				
				if (generics != null && generics.length > 0) {
					className += "<";
					for (int i = 0; i < generics.length; i++) {
						if (i > 0) {
							className += ",";
						}
						className += ((Class<?>)generics[i]).getCanonicalName();
					}
					className += ">";
				}
			}
			
			classNames.add(className);
			instNames.add(instName);
		}
		
		content.append("@Inject\n");
		content.append("public ");
		content.append(getGenClassName());
		content.append("(");
		
		int count = 0;
		Iterator<String> cnIt = classNames.iterator();
		Iterator<String> inIt = instNames.iterator();
		
		while (cnIt.hasNext()) {
			String className = cnIt.next();
			String instName = inIt.next();
			
			if (count++ > 0) {
				content.append(", ");
			}
			content.append(className);
			content.append(" ");
			content.append(instName);
		}
			
		content.append(") {\n");
		
		content.append("super(");
		
		count = 0;
		inIt = instNames.iterator();
		
		while (inIt.hasNext()) {
			String instName = inIt.next();
			
			if (count++ > 0) {
				content.append(", ");
			}
			
			content.append(instName);
		}
		
		content.append(");");
		
		return content.toString();
	}
	
	@Override
	public String generateDefinition() {
		
		StringBuilder content = new StringBuilder();
		
		content.append(generatePresenterImportSection());
		
		content.append("\n");
		
		content.append(presenterBodyPrefix);
		
		content.append(String.format("%s extends %s {\n\n", 
				getGenClassName(),
				getOriginalSimpleName()));
		
		content.append(proxy.generateDefinition());
		
		content.append("\n\n");
		
		content.append(generateConstructor());
		
		content.append("\n\n");
		
		content.append(presenterBodyPostfix);
		
		return content.toString();
	}
	
	/**
	 * generate the import section for the given presenter
	 * @param pvc
	 * @return 
	 */
	private String generatePresenterImportSection() {
		StringBuilder content = new StringBuilder();
		
		content.append(importPrefix);
		content.append(getGenClassPackage());
		content.append(";");
		
		content.append("\n\n");
		
		List<String> importList = new LinkedList<String>();
		
		importList.add(Inject.class.getCanonicalName());
		importList.add(proxy.getOriginalCanonicalName());
		importList.add(proxy.getProxyAnnotationBase().getCanonicalName());
		
		if (proxy.getUri() != null) {
			importList.add(NameToken.class.getCanonicalName());
		}
		
		for (Type constArgType : getConstructorArgs()) {
			
			ParameterizedType pType = 
					(constArgType instanceof ParameterizedType)
					? ((ParameterizedType)constArgType)
					: null;
			
			Class<?> constArg;
			
			if (pType != null) {
				constArg = (Class<?>)pType.getRawType();
			}
			else {
				constArg = (Class<?>)constArgType;
			}
			
			//Don't import poxy.  That's taken care of
			if (Proxy.class.isAssignableFrom(constArg)) {
				continue;
			}
			
			//Don't import poxy.  That's taken care of
			if (EventBinder.class.isAssignableFrom(constArg)) {
				continue;
			}
			
			//Don't import view.  That's taken care of
			if (viewInterface.isAssignableFrom(constArg)) {
				continue;
			}
			
			//Don't include classes in the same package as the presenter
			if (CodeAndFileGenerator.getPackage(constArg, ".").equals(
					getGenClassPackage())) {
				continue;
			}
			
			importList.add(constArg.getCanonicalName());
		}
		
		for (String imprt : importList) {
			content.append("import ");
			content.append(imprt);
			content.append(";\n");
		}
		
		return content.toString();
	}
	

	/**
	 * @return the proxy
	 */
	public ProxyRef getProxy() {
		return proxy;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(ProxyRef proxy) {
		this.proxy = proxy;
	}
}

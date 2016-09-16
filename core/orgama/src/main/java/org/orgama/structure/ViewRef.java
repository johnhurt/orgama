package org.orgama.structure;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.proxy.Proxy;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author kguthrie
 */
public class ViewRef extends ClassWrapperRef {

	private static final String importPrefix = "package ";
	
	private final PresenterRef presenter;
	
	public ViewRef(Class<?> viewClass, PresenterRef presenter) {
		super(viewClass);
		this.presenter = presenter;
	}
	
	@Override
	public String generateDefinition() {
		
		StringBuilder content = new StringBuilder();
		
		content.append(generateImportSection());
		
		content.append("\n");
		
		content.append("public class ");
		content.append(getGenClassName());
		content.append(" extends ");
		content.append(getOriginalSimpleName());
		content.append(" {");
		content.append("\n\n");
		content.append(String.format("@UiTemplate(\"%s.ui.xml\")\n", 
				getOriginalSimpleName()));
		content.append(String.format(
				"public interface %sUiBinder extends UiBinder<Widget, %s> {}",
				getOriginalSimpleName(), getOriginalSimpleName()));
		content.append("\n\n");
		
		content.append(generateConstructor());
		content.append("\n\n");
		content.append("bind(binder);");
		content.append("\n\n");
		content.append("}");
		content.append("\n\n");
		content.append("}");
		return content.toString();
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
		
		for (Type argClassType : getConstructorArgs()) {
			Class<?> argClass = (Class<?>)argClassType;
			String className = argClass.getSimpleName();
			
			String instName = Character.toLowerCase(className.charAt(0)) + 
					className.substring(1);
			
			classNames.add(className);
			instNames.add(instName);
		}
		
		classNames.add(getOriginalSimpleName() + "UiBinder");
		instNames.add("binder");
		
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
			
			if (instName.equals("binder")) {
				continue;
			}
			
			if (count++ > 0) {
				content.append(", ");
			}
			
			content.append(instName);
		}
		
		content.append(");");
		
		return content.toString();
	}
	
	/**
	 * generate the import section for the given presenter
	 * @param pvc
	 * @return 
	 */
	private String generateImportSection() {
		StringBuilder content = new StringBuilder();
		
		content.append(importPrefix);
		content.append(getGenClassPackage());
		content.append(";");
		
		content.append("\n\n");
		
		List<String> importList = new LinkedList<String>();
		
		importList.add(UiBinder.class.getCanonicalName());
		importList.add(UiTemplate.class.getCanonicalName());
		importList.add(Widget.class.getCanonicalName());
		importList.add(Inject.class.getCanonicalName());
				
		for (Type constArgType : getConstructorArgs()) {
			
			Class<?> constArg = (Class<?>)constArgType;
			
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
	
}

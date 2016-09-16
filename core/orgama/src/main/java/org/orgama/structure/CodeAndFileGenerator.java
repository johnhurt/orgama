package org.orgama.structure;

import java.io.File;
import java.util.List;

/**
 * static class that generates code and file contents for Presenter/View 
 * structures
 * @author kguthrie
 */
public class CodeAndFileGenerator {

	public static final String filePostfix = "_";
	
	private static final String ginjectorImportPrefix = 
			"package org.orgama.structure.client;\n" +
			"\n" +
			"import com.google.gwt.inject.client.AsyncProvider;\n"+
			"import com.google.inject.Provider;";
	
	private static final String ginjectorBodyPrefix = 
			"public interface OrgamaStructureInjector {\n";
	
	private static final String ginjectorBodyPostfix = "}";
	
	private static final String moduleImportPrefix = 
			"package org.orgama.structure.client;\n" +
			"\n" +
			"import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;";
	
	private static final String moduleBodyPrefix = 
			"public class OrgamaStructureModule " + 
					"extends AbstractPresenterModule {\n" +
			"\n" +
			"@Override\n" +
			"protected void configure() {\n";
	
	private static final String moduleBodyPostfix = 
			"}\n" +
			"}";
	
	private static String rootClasspath;
	
	/**
	 * set rootclasspath
	 * @param rootClasspath 
	 */
	public static void setRootClasspath(File rootClasspath) {
		CodeAndFileGenerator.rootClasspath = rootClasspath.getAbsolutePath();
	}
	
	/**
	 * Generate the import statement for the structure ginjector
	 * @param pvCombos
	 * @return 
	 */
	private static String generateGinjectorImportSection(
			List<PresenterViewCombo> pvCombos) {
		StringBuilder result = new StringBuilder();
		
		result.append(ginjectorImportPrefix);
		
		result.append("\n");
		
		for (PresenterViewCombo pvc : pvCombos) {
			result.append(String.format("import %s;\n", 
					pvc.getPresenter().getCanonicalName()));
		}
		
		return result.toString();
	}
	
	/**
	 * generate the full contents of the ginjector file.
	 * @param pvCombos
	 * @return 
	 */
	public static String generateGinjector(List<PresenterViewCombo> pvCombos) {
		
		StringBuilder result = new StringBuilder();
		
		result.append(generateGinjectorImportSection(pvCombos));
		
		result.append("\n");
		
		result.append(ginjectorBodyPrefix);
		
		result.append("\n");
		
		for (PresenterViewCombo pvc : pvCombos) {
			PresenterRef presenter = pvc.getPresenter();
			
			if (pvc.isDisableCodeSplitting()) {
				result.append(String.format(
						"public Provider<%s> get%s();\n", 
						presenter.getGenClassName(), 
						presenter.getOriginalSimpleName()));
			}
			else {
				result.append(String.format(
						"public AsyncProvider<%s> get%s();\n", 
						presenter.getGenClassName(), 
						presenter.getOriginalSimpleName()));
			}
		}
		
		result.append("\n");
		
		result.append(ginjectorBodyPostfix);
		
		return result.toString();
	}
	
	/**
	 * Generate the import statement for the structure ginjector
	 * @param pvCombos
	 * @return 
	 */
	private static String generateModuleImportSection(
			List<PresenterViewCombo> pvCombos) {
		StringBuilder result = new StringBuilder();
		
		result.append(moduleImportPrefix);
		
		result.append("\n");
		
		for (PresenterViewCombo pvc : pvCombos) {
			result.append(String.format("import %s;\n", 
					pvc.getPresenter().getOriginalCanonicalName()));
			result.append(String.format("import %s;\n", 
					pvc.getPresenter().getCanonicalName()));
			result.append(String.format("import %s;\n", 
					pvc.getView().getCanonicalName()));
			result.append(String.format("import %s;\n", 
					pvc.getView().getOriginalCanonicalName()));
		}
		
		return result.toString();
	}
	
	/**
	 * generate the code for the gin module defining the bindings for the 
	 * presenters
	 * @param pvcs
	 * @return 
	 */
	public static String generateModule(List<PresenterViewCombo> pvcs) {
		
		StringBuilder result = new StringBuilder();
		
		result.append(generateModuleImportSection(pvcs));
		
		result.append("\n");
		
		result.append(moduleBodyPrefix);
		
		result.append("\n");
		
		for (PresenterViewCombo pvc : pvcs) {
			PresenterRef presenter = pvc.getPresenter();
			ViewRef view = pvc.getView();
			ProxyRef proxy = pvc.getPresenter().getProxy();
			
			Class<?> viewInterface = presenter.getViewInterface();
			
			result.append(String.format("\n" +
					"bindPresenter(%s.class,  \n", 
					presenter.getGenClassName()));
			result.append(String.format(
					"%s.%s.class, \n", 
					presenter.getOriginalSimpleName(), 
					viewInterface.getSimpleName()));
			result.append(String.format("%s.class,\n" +
					"%s.%s.class);\n\n", 
					view.getGenClassName(), 
					presenter.getGenClassName(),
					proxy.getGenClassName()));
			
			result.append(String.format("bind(%s.class)\n"
					+ ".to(%s.class);\n\n", 
					view.getOriginalSimpleName(),
					view.getGenClassName()));
			
			result.append(String.format("bind(%s.class)\n"
					+ ".to(%s.class);\n", 
					presenter.getOriginalSimpleName(),
					presenter.getGenClassName()));
		}
		
		result.append("\n\n");
		
		result.append(moduleBodyPostfix);
		
		return result.toString();
	}
	
	/**
	 * generate the presenter files as result files in the target list
	 * @param pvcs
	 * @param presenters 
	 */
	public static void generatePresentersAndViews(List<PresenterViewCombo> pvcs,
			 List<ResultFile> targetList) {
		
		for (PresenterViewCombo pvc : pvcs) {
			targetList.add(generatePresenter(pvc.getPresenter()));
			targetList.add(generateView(pvc.getView()));
		} 
		
	}
	
	/**
	 * generate the result file for the given presenter
	 * @param presenter
	 * @return 
	 */
	private static ResultFile generatePresenter(PresenterRef presenter) {
		ResultFile result = new ResultFile(presenter.getDestinationFile());
		result.setContent(presenter.generateDefinition());
		return result;
	}
	
	/**
	 * Generate the result file for the given view
	 */
	private static ResultFile generateView(ViewRef view) {
		ResultFile result = new ResultFile(view.getDestinationFile());
		result.setContent(view.generateDefinition());
		return result;
	}
	
	/**
	 * get the package from the given class
	 * @return 
	 */
	public static String getPackage(Class<?> clazz, String separator) {
		String name = clazz.getCanonicalName();
		String[] nameParts = name.split("\\.");
		StringBuilder result = new StringBuilder();
		
		for (int i = 0; i < nameParts.length - 1; i++) {
			if (i > 0) {
				result.append(separator);
			}
			result.append(nameParts[i]);
		}
		
		return result.toString();
	}
	
}

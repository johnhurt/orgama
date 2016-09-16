package org.orgama.structure;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This application will generate a structure ginjector and structure module to
 * define the relationships between the presenters, views, and proxies 
 * automatically
 */
public class App 
{
	
    public static void main( String[] args ) throws Exception
    {
		if (args.length < 0) {
			System.err.print("No classpath provided");
			System.exit(1);
		}
		try {
			App app = new App(args[0]);
			app.run();
			app.finish();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
    }
	
	public static File classPathRootFile;
	
	private ResultFile ginjector;
	private ResultFile module;
	private List<ResultFile> presentersAndViews;
	
	public App(String classPathRoot) {
		classPathRootFile = new File(classPathRoot);
		CodeAndFileGenerator.setRootClasspath(classPathRootFile);
	}
	
	public void run() throws Exception {
		if (!classPathRootFile.exists()) {
			throw new Exception("Given classpath root does not exist");
		}
		
		ginjector = new ResultFile("OrgamaStructureInjector.java", 
				new File(classPathRootFile.getAbsolutePath() + "/" + 
						"org/orgama/structure/client/"));
		module = new ResultFile("OrgamaStructureModule.java", 
				getGinjector().getParentFolder());
		
		presentersAndViews = new LinkedList<ResultFile>();
		
		List<File> presenterFiles = new ArrayList<File>();
		List<File> viewFiles = new ArrayList<File>();
		
		filterClasspathFiles(classPathRootFile, 
				presenterFiles, viewFiles);
		
		List<Class<?>> presenters = getPresenterClasses(presenterFiles);
		List<Class<?>> views = getViewClasses(viewFiles);
		List<PresenterViewCombo> pvCombos = matchPresentersAndViews(
				presenters, views);
		
		generateProxies(pvCombos);
		generateResults(pvCombos);
	}
	
	public void finish() {
		getGinjector().write();
		getModule().write();
		
		for (ResultFile file : getPresentersAndViews()) {
			file.write();
		}
	}
	
	/**
	 * Filter the files in the given classpath into presenter, view, and 
	 * proxy files
	 * @param presenterFiles
	 * @param viewFiles 
	 */
	private void filterClasspathFiles(File startingPoint,
			List<File> presenterFiles, 
			List<File> viewFiles) {
		
		List<File> subDirs = new LinkedList<File>();
		
		for (File file : startingPoint.listFiles()) {
			if (file.isDirectory()) {
				subDirs.add(file);
				continue;
			}
			
			switch(determineFileType(file)) {
				case presenter: {
					presenterFiles.add(file);
					break;
				}
				case view: {
					viewFiles.add(file);
					break;
				}
			}
		}
		
		for (File dir : subDirs) {
			filterClasspathFiles(dir, presenterFiles, viewFiles);
		}
	}
	
	/**
	 * determines if the given file is of one of the types used in orgama 
	 * structure
	 * @param file
	 * @return 
	 */
	private StructureFileType determineFileType(File file) {
		if (file.getName().contains("Presenter")) {
			return StructureFileType.presenter;
		}
		if (file.getName().contains("View")) {
			return StructureFileType.view;
		}
		return StructureFileType.nil;
	}
	
	/**
	 * tries to infer the class name from the file's path
	 * @param file
	 * @return 
	 */
	private String getClassNameFromFile(File file) {
		StringBuilder result = new StringBuilder();
		
		File curr = file;
		int pathCount = 0;
		
		do {
			if (pathCount++ > 0) {
				result.insert(0, ".");
			}
			result.insert(0, curr.getName().split("[\\.\\$]",2)[0]);
			curr = curr.getParentFile();
		} while(!curr.equals(classPathRootFile));
		
		return result.toString();
	}
	
	/**
	 * gets classes for the files given
	 * @param files
	 * @return 
	 */
	private List<Class<?>> getClassesForFiles(List<File> files) {
		List<Class<?>> result = new LinkedList<Class<?>>();
		
		for (File file : files) {
			String className = getClassNameFromFile(file);
			
			try {
				Class<?> clazz = Class.forName(className);
				result.add(clazz);
			}
			catch(Exception ex) {
				
			}
		}
		
		return result;
	}
	
	/**
	 * gets the actual presenter Classes from the list of possible presenter
	 * files
	 * @param presenterFiles
	 * @return 
	 */
	private List<Class<?>> getPresenterClasses(List<File> presenterFiles) {
		
		List<Class<?>> possibleClasses = getClassesForFiles(presenterFiles);
		List<Class<?>> result = new LinkedList<Class<?>>();
		
		for(Class<?> clazz : possibleClasses) {
			if (Presenter.class.isAssignableFrom(clazz)) {
				result.add(clazz);
			}
		}
		
		return result;
	}
	
	/**
	 * gets the actual presenter Classes from the list of possible presenter
	 * files
	 * @param presenterFiles
	 * @return 
	 */
	private List<Class<?>> getViewClasses(List<File> viewFiles) {
		
		List<Class<?>> possibleClasses = getClassesForFiles(viewFiles);
		List<Class<?>> result = new LinkedList<Class<?>>();
		
		for(Class<?> clazz : possibleClasses) {
			if (View.class.isAssignableFrom(clazz)) {
				result.add(clazz);
			}
		}
		
		return result;
	}
	
	private List<PresenterViewCombo> matchPresentersAndViews(
			List<Class<?>> presenters, List<Class<?>> views) {
		List<PresenterViewCombo> result = new LinkedList<PresenterViewCombo>();
		
		Set<String> processedClassses = new HashSet<String>();
		
		for (Class<?> presenter : presenters) {
			
			if (processedClassses.contains(
					presenter.getCanonicalName())) {
				continue;
			}
			
			processedClassses.add(presenter.getCanonicalName());
			
			PresenterRef presenterRef = new PresenterRef(presenter);
			
			for (Class<?> view : views) {
				if (presenterRef.getViewInterface().isAssignableFrom(view)) {
					ViewRef viewRef = new ViewRef(view, presenterRef);
					
					result.add(new PresenterViewCombo(presenterRef, viewRef));
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * create result file content for the presenters, injector, and module
	 * @param pvcs 
	 */
	private void generateResults(List<PresenterViewCombo> pvcs) {
		ginjector.setContent(CodeAndFileGenerator.generateGinjector(pvcs));
		module.setContent(CodeAndFileGenerator.generateModule(pvcs));
		CodeAndFileGenerator.generatePresentersAndViews(pvcs, presentersAndViews);
	}

	/**
	 * determine the proxy types and annotations based on the configurations
	 * of all the presenters and views
	 */
	private void generateProxies(List<PresenterViewCombo> pvcs) {
		
		for (PresenterViewCombo pvc : pvcs) {
			pvc.setUri(pvc.getNameToken());
			
		}
		
	}
	
	/**
	 * @return the ginjector
	 */
	public ResultFile getGinjector() {
		return ginjector;
	}

	/**
	 * @return the module
	 */
	public ResultFile getModule() {
		return module;
	}

	/**
	 * @return the presenters
	 */
	public List<ResultFile> getPresentersAndViews() {
		return presentersAndViews;
	}
	
	
}

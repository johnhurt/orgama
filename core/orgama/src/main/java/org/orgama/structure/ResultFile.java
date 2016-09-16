package org.orgama.structure;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Storage class for results file
 * @author kguthrie
 */
public class ResultFile {
	private String content;
	private String name;
	private File parentFolder;

	public ResultFile(String name, File parentFolder) {
		this.name = name;
		this.parentFolder = parentFolder;
	}
	
	public ResultFile(File file) {
		this.name = file.getName();
		this.parentFolder = file.getParentFile();
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parentFolder
	 */
	public File getParentFolder() {
		return parentFolder;
	}

	/**
	 * @param parentFolder the parentFolder to set
	 */
	public void setParentFolder(File parentFolder) {
		this.parentFolder = parentFolder;
	}
	
	private void verifyDirectory(File directory) {
		if (directory.exists()) {
			return;
		}
		verifyDirectory(directory.getParentFile());
		directory.mkdir();
	}
	
	/**
	 * write the given string to a file
	 * @param contents
	 * @param file 
	 */
	public void write() {
		
		try {
			verifyDirectory(parentFolder);
			FileWriter fw = new FileWriter(parentFolder.getAbsolutePath() +
					"/" + name);
			PrintWriter pw = new PrintWriter(fw);
			pw.append(content);
			pw.flush();
			pw.close();
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		
	}
}

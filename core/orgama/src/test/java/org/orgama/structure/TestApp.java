/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.structure;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kguthrie
 */
public class TestApp {
	
	public TestApp() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
    public void testApp() throws Exception
    {
		App app = new App("src/test/java");
		app.run();
		
		System.out.println(app.getGinjector().getContent());
		System.out.println("\n");
		System.out.println(app.getModule().getContent());
		
		for (ResultFile file : app.getPresentersAndViews()) {
			System.out.println("\n");
			System.out.println(file.getContent());
		}
    }

}

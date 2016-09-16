/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared;

import org.orgama.server.scope.TrueSingletonScope;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.orgama.server.annotation.TrueSingleton;

/**
 *
 * @author kguthrie
 */
public class TestTrueSingletonScope {
	
	public TestTrueSingletonScope() {
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
	
	/**
	 * Test a simple single injection with a true-singleton scoped object.
	 */
	@Test
	public void testTrueSingletonSingleInjection() {
		
		int injectedId1 = -1;
		int injectedId2 = -1;
		int injectedId3 = -1;
		
		try {
			Injector injecector = Guice.createInjector(
					new ModuleWithTrueSingleton());

			TstInjectedClass injected = injecector.getInstance(
					TstInjectedClass.class);
			injectedId1 = Math.abs(injected.id);
		}
		catch(Exception ex) {
			fail("Exception while injecting");
		}
		
		assertTrue(injectedId1 >= 0);
		
		try {
			Injector injecector = Guice.createInjector(
					new ModuleWithTrueSingleton());

			TstInjectedClass injected = injecector.getInstance(
					TstInjectedClass.class);
			injectedId2 = Math.abs(injected.id);
		}
		catch(Exception ex) {
			fail("Exception while injecting");
		}
		
		assertTrue(injectedId2 >= 0);
		assertEquals(injectedId1, injectedId2);
		
		try {
			Injector injector = Guice.createInjector(
					new ModuleWithTrueSingleton());
			Provider<TstInjectedClass> provider = 
					injector.getProvider(TstInjectedClass.class);
			injectedId3 = Math.abs(provider.get().id);
		}
		catch(Exception ex) {
			fail("Exception while injecting provider");
		}
		
		assertTrue(injectedId3 >= 0);
		assertEquals(injectedId1, injectedId3);
	}
	
	/**
	 * Class that can be injected
	 */
	public static class TstInjectedClass {
		
		private static final Random rand = 
				new Random(System.currentTimeMillis());
		
		int id;
		
		@Inject
		public TstInjectedClass() {
			this.id = rand.nextInt();
		}
		
	} 
	
	/**
	 * private module class that injects a TstInjectedClass as a TrueSingleton
	 */
	private static class ModuleWithTrueSingleton extends AbstractModule {

		@Override
		protected void configure() {
			//bind the true singleton scope to an instance created right here
			TrueSingletonScope trueSingletonScope = new TrueSingletonScope();
			bindScope(TrueSingleton.class, trueSingletonScope);
			bind(TrueSingletonScope.class)
					.annotatedWith(Names.named("trueSingletonScope"))
					.toInstance(trueSingletonScope);

			bind(TstInjectedClass.class).in(TrueSingleton.class);
		}
		
	}
}

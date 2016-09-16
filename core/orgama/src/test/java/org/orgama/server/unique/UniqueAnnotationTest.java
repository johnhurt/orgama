/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.unique;

import com.google.apphosting.api.ApiProxy;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import java.util.List;
import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.orgama.server.Ofy;
import org.orgama.server.config.OrgamaTstModule;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class UniqueAnnotationTest {
	
	public static class Module extends OrgamaTstModule {}
	
	public UniqueAnnotationTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
		Ofy.register(TstObject.class);
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
	 * test getting a unique lock on a test object
	 */
	@Test
	public void testUnobstructedCreation() {
		
		TstObject obj = new TstObject();
		obj.setUniqueField("hello1");
		
		Ofy.save().entity(obj).now();
		
		assertNotNull(obj.getId());
	}
	
	/**
	 * test to make sure one cannot insert an object that has reuses a value
	 * in a unique field
	 */
	@Test
	public void testObstructedCreation() {
		
		TstObject obj = new TstObject();
		obj.setUniqueField("hello2");
		
		Ofy.save().entity(obj).now();
				
		assertNotNull(obj.getId());
		
		obj = new TstObject();
		obj.setUniqueField("hello2"); //deliberate reuse
		
		try {
			Ofy.save().entity(obj).now();
		}
		catch(Exception ex) {
			
		}
		
		assertNull(obj.getId());
	}
	
	/**
	 * start two threads to insert two tst objects with conflicting unique.  The
	 * actual tstObject class used here is dynamic based on the index passed in.
	 * This avoids the huge amount of optimistic concurrency errors that come 
	 * from some of the operations on the datastore
	 * fields
	 */
	private boolean runDoubleFastCreation(int num) throws Exception {
		String name = TstObject.class.getCanonicalName()
					+ "$TstObject" + num;
		Class clazz = Class.forName(name);
		final TstObject o1 = (TstObject)clazz.newInstance();
		final TstObject o2 = (TstObject)clazz.newInstance();
		Ofy.register(clazz);
		
		String testString = "test" + num;
		
		o1.setUniqueField(testString);
		o2.setUniqueField(testString);
		
		final ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
		
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				ApiProxy.setEnvironmentForCurrentThread(env);
				Ofy.save().entity(o1).now();	
			}
		});
		
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				ApiProxy.setEnvironmentForCurrentThread(env);
				Ofy.save().entity(o2).now();
			}
		});
		
		List<TstObject> results = 
				Ofy.load().type(clazz).filter("uniqueField", testString).list();
		
		if (!results.isEmpty()) {
			return false;
		}
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		}
		catch(Exception ex) {
		}
		
		results = 
				Ofy.load().type(clazz).filter("uniqueField", testString).list();
		
		if (results == null) {
			return false;
		}
		
		//This is fine for now
		if (results.isEmpty()) {
			return false;
		}
		
		if (results.size() > 1) {
			return false;
		}
		
		if (results.get(0) == null) {
			return false;
		}
		
		if (!results.get(0).getUniqueField().equals(testString)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Test inserting two object with the same values very quickly multiple 
	 * times
	 */
	@Test
	public void testFastCreation() {
		//try 100 times
		int testCount = 100;
		Thread[] testThreads = new Thread[testCount];
		final boolean[] results = new boolean[testCount];
		
		final ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
		
		for (int i = 0; i < testCount; i++) {
			final int fi = i;
			Thread curr = new Thread(new Runnable() {

				@Override
				public void run() {
					ApiProxy.setEnvironmentForCurrentThread(env);
					try {
						results[fi] = runDoubleFastCreation(fi);
					}
					catch(Exception ex) {
						results[fi] = false;
					}
				}
			});
			testThreads[i] = curr;
			curr.start();
		}
		
		for (Thread t : testThreads) {
			try {
				t.join();
			}
			catch(Exception ex) {
				//
			}
		}
		
		int failCount = 0;
		
		for (boolean result : results) {
			if (!result) {
				failCount++;
			}
		}
		assertEquals(failCount + " of the uniqueness tests failed!", 0, 
				failCount);
	}
	
	/**
	 * test updating an object with a unique field without intentionally 
	 * causing contention
	 */
	@Test
	public void testUpdateWithoutContention() {
		TstObject o1 = new TstObject();
		
		o1.setUniqueField("unique");
		o1.setAnotherField(666);
		
		Ofy.save().entity(o1).now();
		
		assertNotNull(o1.getId());
		
		o1.setAnotherField(555);
		
		Key<TstObject> key = Ofy.save().entity(o1).now();
		
		ObjectifyService.ofy().clear();
		
		TstObject o1Copy = Ofy.load().key(key).get();
		
		assertEquals(o1.getAnotherField(), o1Copy.getAnotherField());
		
		o1.setUniqueField("still-unique");
		
		key = Ofy.save().entity(o1).now();
		
		o1Copy = Ofy.load().key(key).get();
		
		assertEquals(o1.getUniqueField(), o1Copy.getUniqueField());
		
	}
	
	/**
	 * Test updating fields
	 */
	@Test
	public void testUpdateWithContention() {
		TstObject o1 = new TstObject();
		TstObject o2 = new TstObject();
		
		o1.setUniqueField("unique1");
		o1.setAnotherField(666);
		
		o2.setUniqueField("unique2");
		o2.setAnotherField(555);
		
		Ofy.save().entity(o1).now();
		Key<TstObject> key = Ofy.save().entity(o2).now();
		
		assertNotNull(o1.getId());
		assertNotNull(o2.getId());
		
		String o2Uf = o2.getUniqueField();
		
		ObjectifyService.ofy().clear();
		
		TstObject o2Copy = Ofy.load().key(key).get();
		
		assertEquals(o2Uf, o2Copy.getUniqueField());
		
		o2.setUniqueField(o1.getUniqueField());
		
		try {
			 Ofy.save().entity(o2).now();
			 fail("The previous save should have thrown an exception");
		}
		catch(Exception ex) {
			//this is expected
		}
		
		o2Copy = Ofy.load().type(TstObject.class).id(
				o2.getId()).get();
		
		assertFalse(o2Copy.getUniqueField().equals(o1.getUniqueField()));
		
		
	}
	
	/**
	 * test that manipulating multiple unique fields 
	 */
	@Test
	public void testMultipleUniqueFieldManipulation() {
		TstObject o1 = new TstObject();
		TstObject o2 = new TstObject();
		
		o1.setUniqueField("first-unique-field");
		o1.setAnotherUniqueField(56565);
		o1.setYetAnotherUniqueField(56565);
		
		o2.setUniqueField("second-unique-field");
		o2.setAnotherField(66666);
		o2.setYetAnotherUniqueField(56565);
		
		Ofy.save().entity(o1).now();
		
		try {
			Ofy.save().entity(o2).now();
			fail("Previous save statement should have failed");
		}
		catch(Exception ex) {
			//this is expected
		}
	}
	
	/**
	 * Test to make sure a class that is registered with a unique field is 
	 * confirmed to have an @Index annotation on that field.
	 */
	@Test
	public void testUniqueFieldMustBeIndexed() {
		
		try {
			Ofy.register(TstFailingObject.class);
			fail("Registering class with unique but not indexed field should"
					+ "throw an exception");
		}
		catch(Exception ex) {
			//expected
		}
		
	}
}

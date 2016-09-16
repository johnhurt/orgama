/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatException;
import java.util.ArrayList;
import org.jukito.JukitoRunner;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.orgama.server.config.OrgamaTstModule;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class BetterTokenFormatterTest {
    
	
	//Boiler plate to make injections work using OrgamaTestEnvModule
    public static class Module extends OrgamaTstModule {}
	
	
	@Inject BetterTokenFormatter formatter;
	
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void testToPlaceToken() {
        PlaceRequest pr = new PlaceRequest("test").with("name1", "value1")
                .with("name2", "value2");
        
        String testString = formatter.toPlaceToken(pr);
        
        assertTrue(testString.equals("test?name1=value1&name2=value2"));
        
        pr = new PlaceRequest("test");
        
        testString = formatter.toPlaceToken(pr);
        
        assertTrue(testString.equals("test"));

        pr = new PlaceRequest("test").with("", "wrong").with("right1", "")
                .with("right2", "");
        
        testString = formatter.toPlaceToken(pr);
        
        assertTrue(testString.equals("test?right1&right2") || 
                testString.equals("test?right2&right1"));
    }
    
    @Test
    public void testToHistoryToken() {
        ArrayList<PlaceRequest> list = new ArrayList<PlaceRequest>();
        
        String testString = formatter.toHistoryToken(list);
        
        assertTrue(testString.equals(""));
        
        list.add(new PlaceRequest("test").with("name", "value"));
        
        testString = formatter.toHistoryToken(list);
        
        assertTrue(testString.equals("test?name=value"));
    }
    
    @Test
    public void testToPlaceRequest() {
        
        String preString = "test";
        
        PlaceRequest req = formatter.toPlaceRequest(preString);
        
        String postString = formatter.toPlaceToken(req);
        
        assertTrue(preString.equals(postString));
        
        
        preString = "test?name=value";
        
        req = formatter.toPlaceRequest(preString);
        
        postString = formatter.toPlaceToken(req);
        
        assertTrue(preString.equals(postString));
        
        
        preString = "test?name1=value1&name2=value2";
        
        req = formatter.toPlaceRequest(preString);
        
        postString = formatter.toPlaceToken(req);
        
        assertTrue(preString.equals(postString));
        
        try {
        
            preString = "test?=value&right&wrong=";

            req = formatter.toPlaceRequest(preString);
            
            fail("empty string as key should throw exception");
        }
        catch (TokenFormatException tfe)
        {
            //this is a good thing
        }
        
        preString = "test?name1=&name2";
        
        req = formatter.toPlaceRequest(preString);
        
        postString = formatter.toPlaceToken(req);
        
        assertTrue(postString.equals("test?name1&name2"));
        
        
    }
	
	@Test
	public void testPlaceRequestHierarchy() {
		
        String preString = "test?name1=value1&name2=value2";
        
        PlaceRequest req = formatter.toPlaceRequestHierarchy(preString).get(0);
        
        String postString = formatter.toPlaceToken(req);
        
        assertTrue(preString.equals(postString));
        
	}
}

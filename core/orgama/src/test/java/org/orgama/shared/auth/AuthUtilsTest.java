// <copyright file="AuthUtilsTest.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://rookandpawn.com/
package org.orgama.shared.auth;

import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.orgama.shared.auth.except.InvalidEmailAddressFormatException;
import org.orgama.shared.auth.except.InvalidUsernameFormatException;

/**
 * Tests for methods related to UserAuthInfo
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class AuthUtilsTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test the restrictions made on potential usernames
     */
    @Test
    public void testUsernameRestrictions() {
        //test a username that is too short
        assertFalse("Username cannot be less than 6 characters", 
                AuthUtils.validateUsernameString("test"));
        
        //test trimming takes place correctly
        assertFalse("Username cannot be less than 6 characters, and spaces " + 
                "don't count", 
                AuthUtils.validateUsernameString("    test      "));
        
        //test username that is too long
        assertFalse("Usernames cannot be more than 32 characters", 
                AuthUtils.validateUsernameString("A101010101010101010101010" +
                "1010101010101010101010101010101010101010101010101010"));
        
        //test for special characters that are not allowed
        assertFalse("Usernames cannot have special characters in it", 
                AuthUtils.validateUsernameString("Seems-Harmlesss"));
        
        //try one that should work just to make sure
        assertTrue("This one shuld have worked", 
                AuthUtils.validateUsernameString("Big4n_ploff"));
    }
    
    /**
     * Test the restrictions on the username
     */
    @Test
    public void testEmailAddressRestrictions() {
        //test that email addresses cannot be null or empty
        assertFalse("Email Addresses cannot be null", 
                AuthUtils.validateEmailAddress(null));
        assertFalse("Email Addresses cannot be empty", 
                AuthUtils.validateEmailAddress(""));
        
        //test that email addresses cannot be over 128 characters long
        assertFalse("Email addresses cannot exceed 128 characters", 
                AuthUtils.validateEmailAddress(
                        "12347890123478901234789012347890" +
                        "12347890123478901234789012347890" +
                        "12347890123478901234789012347890" +
                        "12347890123478901234789012347890" +
                        "@gmail.com"));
        
        //test that strings that aren't email addresses don't pass tests
        assertFalse("Email addresses must be valid", 
                AuthUtils.validateEmailAddress("kevin@"));
        
        //test some that work just to make sure
        assertTrue("This one should work", 
                AuthUtils.validateEmailAddress("pol45_plp@gmail.com"));
        assertTrue("This one should work", 
                AuthUtils.validateEmailAddress("pol45_plp+spam@gmail.co.edu"));
        assertTrue("This one should work", 
                AuthUtils.validateEmailAddress("kevin.guthrie@gmail.com"));
    }
    
    
    @Test 
    public void testUsernameSanitizer() {
        
        String result;
        
        try {
            AuthUtils.sanitizeStringForUsername(null);
            fail("sanitizing null username is an error");
        }
        catch(InvalidUsernameFormatException ex) {
            //this is right
        }
        
        try {
            AuthUtils.sanitizeStringForUsername("mfc8");
            fail("sanitizing too short username is an error");
        }
        catch(InvalidUsernameFormatException ex) {
            //this is right
        }
        
        try {
            AuthUtils.sanitizeStringForUsername("12347890123478901234789012347890"
                    + "12347890123478901234789012347890");
            fail("sanitizing too long username is an error");
        }
        catch(InvalidUsernameFormatException ex) {
            //this is right
        }
        
        try {
            AuthUtils.sanitizeStringForUsername("mf.c$asdf");
            fail("sanitizing invalid username is an error");
        }
        catch(InvalidUsernameFormatException ex) {
            //this is right
        }
        
        result = AuthUtils.sanitizeStringForUsername(" TeSTgbm123VVcm\t");
        assertEquals(result, "testgbm123vvcm");
    }
    
    @Test
    public void testEmailAddressSanitizer() {
        String result;
        
        
        try {
            AuthUtils.sanitizeEmailAddress(null);
            fail("sanitizing null email is an error");
        }
        catch(InvalidEmailAddressFormatException ex) {
            //this is right
        }
        
        try {
            AuthUtils.sanitizeEmailAddress(
                      "12347890123478901234789012347890"
                    + "12347890123478901234789012347890" 
                    + "12347890123478901234789012347890"
                    + "12347890123478901234789012347890"
                    + "12347890123478901234789012347890"
                    + "12347890123478901234789012347890"
                    + "12347890123478901234789012347890"
                    + "12347890123478901234789012347890");
            fail("sanitizing too long email address is an error");
        }
        catch(InvalidEmailAddressFormatException ex) {
            //this is right
        }
        
        
        try {
            AuthUtils.sanitizeEmailAddress("12347890123478901234789012347890"
                    + "12347890123478901234789012347890");
            fail("sanitizing all digits email is an error");
        }
        catch(InvalidEmailAddressFormatException ex) {
            //this is right
        }
        
        
        try {
            AuthUtils.sanitizeEmailAddress("12347890123@#$@%");
            fail("sanitizing crazy string email is an error");
        }
        catch(InvalidEmailAddressFormatException ex) {
            //this is right
        }
        
        result = AuthUtils.sanitizeEmailAddress("\t\nSt-_3d+12.TH@ER.cm.CoM  ");
        assertEquals(result, "st-_3d+12.th@er.cm.com");
    }
}

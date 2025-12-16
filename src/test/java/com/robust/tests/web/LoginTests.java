package com.robust.tests.web;

import org.testng.annotations.Test;

import com.robust.pages.web.HomePage;
import com.robust.core.base.BaseTest;

public class LoginTests extends BaseTest{
	
	 @Test(description = "Verify Home Page loads successfully")
	    public void verifyHomePageLoaded() {
	        HomePage home = new HomePage(getDriver()); 
	        
	   
	        home.enterContactExpertsButton();
	        
	    }
	 @Test(description = "Verify Login Page loads successfully")
	    public void verifyLoginPageLoaded() {
	        HomePage home = new HomePage(getDriver()); 
	        
	   
	        home.isHomePageLoaded();
	        
	    }

}

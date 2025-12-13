package com.robust.tests.web;


import org.testng.annotations.Test;

import com.robust.core.base.BaseTest;
import com.robust.pages.web.HomePage;


public class HomeTests extends BaseTest {
	

	 @Test(description = "Verify Home Page loads successfully")
	    public void verifyHomePageLoaded() {
	        HomePage home = new HomePage(getDriver()); 
	        
	   
	        home.enterContactExpertsButton();
	        
	    }
	 @Test(description = "Verify Login Page loads successfully")
	    public void verifyLoginPageLoaded() {
	        HomePage home = new HomePage(getDriver()); 
	        
	   
	        home.enterContactExpertsButton();
	        
	    }
}

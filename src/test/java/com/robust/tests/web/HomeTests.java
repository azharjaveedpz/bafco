package com.robust.tests.web;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.robust.annotations.TestInfo;
import com.robust.core.base.BaseTest;
import com.robust.enums.TestPriority;
import com.robust.enums.TestSeverity;
import com.robust.pages.web.HomePage;


public class HomeTests extends BaseTest {
	

	@Test(description = "Verify Home Page loads successfully")
	@TestInfo(priority = TestPriority.P1, severity = TestSeverity.HIGH)
	public void verifyHomePageLoaded() {
	    HomePage home = new HomePage(getDriver());
	    home.enterContactExpertsButton();
	    Assert.fail("Force failure to test retry");
	}

	@Test(description = "Verify Login Page loads successfully")
	@TestInfo(priority = TestPriority.P0, severity = TestSeverity.CRITICAL)
	public void verifyLoginPageLoaded() {
	    HomePage home = new HomePage(getDriver());
	    Assert.fail("Force failure to test retry");
	}


}

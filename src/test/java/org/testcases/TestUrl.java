package org.testcases;
import org.generic.BaseClass;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.utilities.CommonMethods;
import org.utilities.ExcelBank;

public class TestUrl extends BaseClass{
	
	@Test
	public static void testWelcomePage() throws Exception {
		SoftAssert sa = new SoftAssert();
		sa.assertEquals(driver.getTitle(), object.getProperty("welcomePageTitle"));
		sa.assertAll();
	}
}

package org.testcases;
import org.generic.BaseClass;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.utilities.CommonMethods;
import org.utilities.ExcelBank;

public class TestWelcomePage extends BaseClass{
	
	@Test
	public static void testWelcomePage() throws Exception {
		SoftAssert sa = new SoftAssert();
		sa.assertEquals(driver.getTitle(), object.getProperty("welcomePageTitle"));
		WebDriverWait wait=new WebDriverWait(driver, 20);
		CommonMethods cm = new CommonMethods();
		log.info("Clicking on customer login button");
		cm.clickEle(object.getProperty("customerLoginBtn"));
//		Assert.fail();
		log.info("Entering the username in username/email textbox");
		wait.until(ExpectedConditions.elementToBeClickable(getElement(object.getProperty("userNameTB"))));
		cm.enterText(object.getProperty("userNameTB"), ExcelBank.getCellValue(excel.getProperty("sheetName"), "Username", 1));
		log.info("Entering the password in password textbox");
		cm.enterText(object.getProperty("passwordTB"), ExcelBank.getCellValue(excel.getProperty("sheetName"), "Password", 1));
		log.info("Clicking on signin button");
		cm.clickEle(object.getProperty("signInBtn"));
		wait.until(ExpectedConditions.stalenessOf(getElement(object.getProperty("clientDropDwn"))));
		cm.clickEle(object.getProperty("continueBtn"));
		sa.assertEquals(driver.getTitle(), object.getProperty("welcomePageTitle"));
		sa.assertAll();
	}
}

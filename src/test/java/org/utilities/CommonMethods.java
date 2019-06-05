package org.utilities;
import org.generic.BaseClass;
import org.openqa.selenium.WebElement;

public class CommonMethods extends BaseClass{
	private  WebElement ele;
	public  void clickEle(String locator) {
		ele=getElement(locator);
		ele.click();
	}
	public void enterText(String locator, String data) {
		ele=getElement(locator);
		ele.sendKeys(data);
	}
}

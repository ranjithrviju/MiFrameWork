package org.generic;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.utilities.TestUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class BaseClass implements IConstants {
	public static WebDriver driver;
	public static Properties config;
	public static Properties object;
	public static Properties excel;
	public static Logger log=Logger.getLogger("MiFrameWork Logs");
	public static ExtentReports extent=TestUtils.getExtentReport();
	public static ExtentTest test;
	@BeforeTest
	public static void initialize() {
		try {
			config=new Properties();
			config.load(new FileInputStream(configPath));
			log.info("config.properties file loaded successfully");
		} catch (Exception e) {
			log.error("Failed to load config.properties"+e.getMessage());
		}
		try {
			object=new Properties();
			object.load(new FileInputStream(objectPath));
			log.info("object.properties file loaded successfully");
		} catch (Exception e) {
			log.error("Failed to load object.properties"+e.getMessage());
		}
		try {
			excel=new Properties();
			excel.load(new FileInputStream(excelPath));
			log.info("excel.properties file loaded successfully");
		} catch (Exception e) {
			log.error("Failed to load excel.properties"+e.getMessage());
		}
	}
	@BeforeClass
	public static void openApplication() {
		if(config.getProperty("browser").equals("chrome")) {
			System.setProperty(chromeKey, chromePath);
			log.info("Launching Chrome Browser");
			driver=new ChromeDriver();
		}
		driver.manage().window().maximize();
		driver.get(config.getProperty("url"));
		log.info("Navigated to the UAT link");
		driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);
	}
	@BeforeMethod
	public static void checkRunMode(Method method){
		String testname = method.getName();
		String sheetName=testname.substring(testname.indexOf("_")+1)+"_TestCase";
		if(!TestUtils.isTestCaseRunnable(sheetName, testname)) {
			closebrowser();
			throw new SkipException("Skipped the test case "+testname+" as the RunMode is No");
		}
	}
//	@AfterMethod
//	public void updateResult(ITestResult result) {
//		String name = result.getName();
//		String sheetName=name.substring(name.indexOf("_")+1)+"_TestCase";
//		TestUtils.setTestResultExcel(sheetName, name, String.valueOf(result.getStatus()));
//	}
	
	public static synchronized WebElement getElement(String locator) {
		String locators=object.getProperty(locator);
		String[] objects=locators.split("-", 2);
		String locType=objects[0];
		String locValue=objects[1];
		WebElement ele=null;
		By by=null;
		if(locType.equals("IDE")) {
			try {
				by=By.id(locValue);
			} catch (NoSuchElementException e) {
				log.error("Element cannot be found using "+locType+" locator.  "+e.getLocalizedMessage());
			}
		}
		else if(locType.equals("NAME")) {
			try {
				by=By.name(locValue);
			} catch (NoSuchElementException e) {
				log.error("Element cannot be found using "+locType+" locator.  "+e.getLocalizedMessage());
			}
		}
		else if(locType.equals("XPH")) {
			try {
				by=By.xpath(locValue);
			} catch (NoSuchElementException e) {
				log.error("Element cannot be found using "+locType+" locator.  "+e.getLocalizedMessage());
			}
		}
		if(driver.findElements(by).size()>0) {
			ele=driver.findElement(by);
		}
		return ele;
	}
	public static synchronized List<WebElement> getListOfElement(String locator) {
		String locators=object.getProperty(locator);
		String[] objects=locators.split("-", 2);
		String locType=objects[0];
		String locValue=objects[1];
		List<WebElement> ele=null;
		By by=null;
		if(locType.equals("IDE")) {
			try {
				by=By.id(locValue);
			} catch (NoSuchElementException e) {
				log.error("Element cannot be found using "+locType+" locator.  "+e.getLocalizedMessage());
			}
		}
		else if(locType.equals("NAME")) {
			try {
				by=By.name(locValue);
			} catch (NoSuchElementException e) {
				log.error("Element cannot be found using "+locType+" locator.  "+e.getLocalizedMessage());
			}
		}
		else if(locType.equals("XPH")) {
			try {
				by=By.xpath(locValue);
			} catch (NoSuchElementException e) {
				log.error("Element cannot be found using "+locType+" locator.  "+e.getLocalizedMessage());
			}
		}
		if(driver.findElements(by).size()>0) {
			ele=driver.findElements(by);
		}
		return ele;
	}
	@AfterClass
	public static void closebrowser() {
		if(driver!=null) {
			log.info("Closing the browser");
			driver.quit();
		}
	}
}

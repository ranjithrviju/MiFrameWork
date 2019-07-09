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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.utilities.TestUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class BaseClass implements IConstants {
	public static WebDriver driver;
	public static Properties config;
	public static Properties object;
	public static Properties excel;
	private static Logger log=Logger.getLogger("Base Class");
	public static ExtentReports extent=TestUtils.getExtentReport();
	public static ExtentTest test;
	public static  WebDriverWait driverWait;
//INITIALIZING THE PROPERTIES FILES
	@BeforeSuite
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
	
	//LAUNCHING THE BROWSER
	@BeforeClass
	public static void openApplication() {
		if(config.getProperty("browser").equals("chrome")) {
			System.setProperty(chromeKey, chromePath);
			log.info("Launching Chrome Browser");
			driver=new ChromeDriver();
			driverWait=new WebDriverWait(driver, exwait);
		}
		else if(config.getProperty("browser").equals("firefox")) {
			System.setProperty(fireFoxKey, fireFoxPAth);
			log.info("Launching FireFox Browser");
			driver=new FirefoxDriver();
			driverWait=new WebDriverWait(driver, exwait);
		}
		driver.manage().window().maximize();
		driver.get(config.getProperty("url"));
		log.info("Navigated to the UAT link");
		driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);
	}
	
	//CHECKING THE RUNMODE OF TESTCASE
	@BeforeMethod
	public static void checkRunMode(Method method){
		String testname = method.getName();
		String sheetName=testname.substring(testname.indexOf("_")+1)+"_TestCase";
		log.info("Checking the Run Mode of TestCase "+testname);
		if(!TestUtils.isTestCaseRunnable(sheetName, testname)) {
			log.info("Skipping the testcase as RunMode is No and Closing the browser");
			closebrowser();
			throw new SkipException("Skipped the test case "+testname.toUpperCase()+" as the RunMode is No");
		}
	}

	//TO GET THE ELEMENT 
	public static synchronized WebElement getElement(String locator) {
		String locators=object.getProperty(locator);
		String[] objects=locators.split("-", 2);
		String locType=objects[0];
		String locValue=objects[1];
		WebElement ele=null;
		By by=null;
		if(locType.equals("IDE")) {
			by=By.id(locValue);
		}
		else if(locType.equals("NAME")) {
			by=By.name(locValue);
		}
		else if(locType.equals("XPH")) {
			by=By.xpath(locValue);
		}
		if(driver.findElements(by).size()>0) {
			try {
				log.info("Finding Element  "+locator);
				ele=driver.findElement(by);
			} catch (NoSuchElementException e) {
				log.error(locator+" cannot be found using the locator : "+locType+" due to : "+e.getMessage());
			}
		}
		return ele;
	}
	
	//TO GET THE LIST OF ELEMENTS
	public static synchronized List<WebElement> getListOfElement(String locator) {
		String locators=object.getProperty(locator);
		String[] objects=locators.split("-", 2);
		String locType=objects[0];
		String locValue=objects[1];
		List<WebElement> ele=null;
		By by=null;
		if(locType.equals("IDE")) {
			by=By.id(locValue);
		}
		else if(locType.equals("NAME")) {
			by=By.name(locValue);
		}
		else if(locType.equals("XPH")) {
			by=By.xpath(locValue);
		}
		if(driver.findElements(by).size()>0) {
			try {
				log.info("Finding Elements  "+locator);
				ele=driver.findElements(by);
			} catch (NoSuchElementException e) {
				log.error(locator+"Element cannot be found using the locator : "+locType);
			}
		}
		return ele;
	}
	
	//CLOSING THE BROWSER
	@AfterClass
	public static void closebrowser() {
		if(driver!=null) {
			log.info("Closing the browser");
			driver.quit();
		}
	}
}
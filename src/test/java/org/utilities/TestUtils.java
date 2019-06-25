package org.utilities;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;
import org.generic.BaseClass;
import org.generic.IConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.io.Files;

//EXTENT REPORT
public class TestUtils extends BaseClass implements IConstants{
	private static ExtentReports extent;
	private static ExtentHtmlReporter rep;
	public static String screenshotPath;
	
	public static ExtentReports getExtentReport() {
		extent=new ExtentReports();
		rep=new ExtentHtmlReporter(extentPath);
		rep.config().setTheme(Theme.DARK);
		rep.config().setReportName("Final Report");
		rep.config().setDocumentTitle("Report For MiFrameWork");
		extent.attachReporter(rep);
		extent.setSystemInfo("Name", "MiFrameWork");
		extent.setSystemInfo("Platform", "Chrome Browser Version 74 ");
		extent.setSystemInfo("Author", "Ranjith");
		return extent;
	}

	@DataProvider(name="getData")
	@BeforeTest
	public static Object[][] getData(Method m) {
		String[] sheetName = m.getName().split("_", 2);
		String name=sheetName[1];
		String sheet = name+"_TestData";
		System.out.println("Sheet in DataProvider"+sheet);
		int rows = ExcelBank.getRowCount(sheet);
		System.out.println("DataProvider rows="+rows);
		int cols = ExcelBank.getColumnCount(sheet);
		System.out.println("DataProvider Columns="+cols);
		Object[][] data=new Object[rows-1][cols];
		System.out.println(sheet);
		for (int i = 1; i <= rows-1; i++) {
			for (int j = 0; j < cols; j++) {
				data[i-1][j]=ExcelBank.getCellValue(sheet, i, j);
			}
		}
		return data;
	}
	
	//TO CAPTURE SCREENSHOT
	public static void captureScreenshot(WebDriver driver, String testCaseName) {
		String date_time = new Date().toString().replaceAll(":", "_").replaceAll(" ", "_");
		screenshotPath=System.getProperty("user.dir")+"\\src\\test\\resources\\screenshots\\"+testCaseName.toUpperCase()+date_time+".png";
		try {
			TakesScreenshot ss = (TakesScreenshot) driver;
			File srcFile = ss.getScreenshotAs(OutputType.FILE);     
			log.info("Copying the scrrenshot to the project folder");
			Files.copy(srcFile, new File(screenshotPath));
		} catch (Exception e) {
			log.info("Failed to save the screenshot"+e.getMessage());
		}
	}
	//CHECK TESTCASE IS RUNNABLE OR NOT
	public static boolean isTestCaseRunnable(String sheetName, String testCaseName) {
		boolean isRunnable=false;
		String sheet=sheetName;
		for (int i = 1; i < ExcelBank.getRowCount(sheet)-1; i++) {
			if(ExcelBank.getCellValue(sheet, "TestCaseID", i).equalsIgnoreCase(testCaseName)) {
				if(ExcelBank.getCellValue(sheet, "RunMode", i).equalsIgnoreCase("YES"))
					isRunnable=true;
				else if(ExcelBank.getCellValue(sheet, "RunMode", i).equalsIgnoreCase("NO"))
					isRunnable=false;
			}
		}
		return isRunnable;
	}
	
public static void reportStatus( String testCaseName,String sheetName,String status ) {
		for (int i = 0; i < ExcelBank.getRowCount(sheetName); i++) {
			if(ExcelBank.getCellValue(sheetName, "TestCaseID",i).equalsIgnoreCase(testCaseName)) {
				ExcelBank.setCellValue(sheetName, "Status", i, status);
			}
		}
	}
}
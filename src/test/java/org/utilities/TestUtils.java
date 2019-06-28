package org.utilities;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.generic.BaseClass;
import org.generic.IConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.model.Test;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.io.Files;

//EXTENT REPORT
public class TestUtils extends BaseClass implements IConstants{
	private static ExtentReports extent;
	private static ExtentHtmlReporter rep;
	public static String screenshotPath;
	private int retryCount = 0;
	private static final int maxRetryCount = 2;
	public static ExtentReports getExtentReport() {
		extent=new ExtentReports();
		rep=new ExtentHtmlReporter(extentPath);
		rep.config().setTheme(Theme.DARK);
		rep.config().setReportName("SmartProp Report");
		rep.config().setDocumentTitle("Report For SmartProp");
		extent.attachReporter(rep);
		extent.setSystemInfo("Name", "SmartProp");
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
		int rows = ExcelBank.getRowCount(sheet);
		int cols = ExcelBank.getColumnCount(sheet);
		Object[][] data=new Object[rows-1][cols];
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
		for (int i = 1; i < ExcelBank.getRowCount(sheet); i++) {
			if(ExcelBank.getCellValue(sheet, "TestCaseID", i).equalsIgnoreCase(testCaseName)) {
				if(ExcelBank.getCellValue(sheet, "RunMode", i).equals("YES")) {
					isRunnable=true;
				}

				else if(ExcelBank.getCellValue(sheet, "RunMode", i).equals("NO"))
					isRunnable=false;
			}
		}
		return isRunnable;
	}

	public static String fromExcel(String sheetName, int colNo,int rowNo) {
		String cellValue=null;
		try {
			FileInputStream fis = new FileInputStream(excel.getProperty("path"));
			Workbook excel = WorkbookFactory.create(fis);
			Sheet sheet = excel.getSheet(sheetName);
			Cell cell = sheet.getRow(rowNo).getCell(colNo);
			if(cell.getCellType()==CellType.STRING) {
				cellValue = cell.getStringCellValue();
			}
			else if(cell.getCellType()==CellType.NUMERIC) {
				cellValue=String.valueOf((int)cell.getNumericCellValue());
			}
		} catch (Exception e) {
			log.error("failed to get the data from excel file"+e.getMessage());
		}
		return cellValue;
	}

	public static void setTestResultExcel(String sheetName,String testCaseName,String data) {
		try {
			for (int i = 0; i < ExcelBank.getRowCount(sheetName); i++) {
				if(ExcelBank.getCellValue(sheetName, "TestCaseID", i).equalsIgnoreCase(testCaseName)) {
					ExcelBank.setCellValue(sheetName, "Status", i, data);
				}
			}
		}catch (Exception e) {
			log.error("failed to save the data in excel file : "+e.getMessage());
		}
	}
@AfterTest
	public void reRunTest(ITestResult result) {
		try {
			if(result.getStatus()==ITestResult.FAILURE) {
					IRetryAnalyzer retry = new IRetryAnalyzer() {
						public boolean retry(ITestResult result) {
							if(retryCount<maxRetryCount) {
								retryCount++;
								return true;
							}
							return false;
						}
					};
				
			}
		} catch (Exception e) {
			log.info("Failed to execute failed testcases : "+e.getMessage());
		}
	}
}

package org.utilities;
import java.io.File;
import java.util.Date;
import org.generic.BaseClass;
import org.generic.IConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.io.Files;

public class ReportUtils extends BaseClass implements IConstants{
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

	public static void captureScreenshot(WebDriver driver, String testCaseName) {
		String date_time = new Date().toString().replaceAll(":", "_").replaceAll(" ", "_");
		System.out.println(date_time);
		screenshotPath=System.getProperty("user.dir")+"\\src\\test\\resources\\screenshots\\"+testCaseName.toUpperCase()+date_time+".jpg";
		try {
			TakesScreenshot ss = (TakesScreenshot) driver;
			File srcFile = ss.getScreenshotAs(OutputType.FILE);
			log.info("Copying the scrrenshot to the project folder");
			Files.copy(srcFile, new File(screenshotPath));
		} catch (Exception e) {
			log.info("Failed to save the screenshot"+e.getMessage());
		}
	}
}

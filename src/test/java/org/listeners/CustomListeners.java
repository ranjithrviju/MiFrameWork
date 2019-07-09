package org.listeners;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.generic.BaseClass;
import org.testng.IClassListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.utilities.TestUtils;
import com.aventstack.extentreports.Status;

public class CustomListeners extends BaseClass implements ITestListener, IClassListener {
	private static Logger log=Logger.getLogger("Custom Listeners");
	public void onTestStart(ITestResult result) {
	}
	public void onTestSuccess(ITestResult result) {
		String name = result.getName();
		String sheetName=name.substring(name.indexOf("_")+1)+"_TestCase";
		log.info("Updating the Test Case Status in Excel");
		TestUtils.setTestResultExcel(sheetName, name,"PASS");
		test.log(Status.PASS, name.toUpperCase()+" is PASSED");
		extent.flush();
	}

	public void onTestFailure(ITestResult result) {
		try {
			String name = result.getName();
			String sheetName=name.substring(name.indexOf("_")+1)+"_TestCase";
			log.info("Updating the Test Case Status in Excel");
			TestUtils.setTestResultExcel(sheetName, name, "FAIL");
			TestUtils.captureScreenshot(driver, result.getMethod().getMethodName());
			test.log(Status.FAIL, name.toUpperCase()+" is FAILED due to :- "+result.getThrowable().toString());
			test.addScreenCaptureFromPath(TestUtils.screenshotPath);
			extent.flush();
		} catch (IOException e) {
			log.info("Failed to update report"+e.getMessage());
		}
	}

	public void onTestSkipped(ITestResult result) {
		String name = result.getName();
		String sheetName=name.substring(name.indexOf("_")+1)+"_TestCase";
		log.info("Updating the Test Case Status in Excel");
		TestUtils.setTestResultExcel(sheetName, name,"SKIP");
		test.log(Status.SKIP, name.toUpperCase()+" is SKIPPED due to RunMode is NO");
		extent.flush();
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onStart(ITestContext context) {
	}

	public void onFinish(ITestContext context) {

	}

	public void onBeforeClass(ITestClass testClass) {
		String testName = testClass.getName().replaceAll("org.testcases.", "");
		test=extent.createTest(testName.toUpperCase());
	}

	public void onAfterClass(ITestClass testClass) {
		
	}
}
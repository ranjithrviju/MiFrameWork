package org.listeners;
import java.io.IOException;
import java.lang.reflect.Method;

import org.generic.BaseClass;
import org.testng.IClassListener;
import org.testng.IRetryAnalyzer;
import org.testng.ITest;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.utilities.TestUtils;

import com.aventstack.extentreports.Status;

public class CustomListeners extends BaseClass implements ITestListener, IClassListener {

	public void onTestStart(ITestResult result) {
	}

	public void onTestSuccess(ITestResult result) {
		String name = result.getName();
		String sheetName=name.substring(name.indexOf("_")+1)+"_TestCase";
		TestUtils.setTestResultExcel(sheetName, name,"PASS");
		test.log(Status.PASS, name.toUpperCase()+" is PASSED");
		extent.flush();
	}

	public void onTestFailure(ITestResult result) {
		try {
			String name = result.getName();
			String sheetName=name.substring(name.indexOf("_")+1)+"_TestCase";
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
		test=extent.createTest(testClass.getName().replaceAll("org.testcases.", "").toUpperCase());
	}

	public void onAfterClass(ITestClass testClass) {
	}
}

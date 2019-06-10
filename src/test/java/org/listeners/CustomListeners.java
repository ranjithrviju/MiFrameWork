package org.listeners;
import java.io.IOException;
import org.generic.BaseClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.utilities.ReportUtils;
import com.aventstack.extentreports.Status;

public class CustomListeners extends BaseClass implements ITestListener {

	public void onTestStart(ITestResult result) {
		test=extent.createTest(result.getName().toUpperCase());
	}

	public void onTestSuccess(ITestResult result) {
		test.log(Status.PASS, result.getName().toUpperCase()+" is PASSED");
		extent.flush();
	}

	public void onTestFailure(ITestResult result) {
		try {
			ReportUtils.captureScreenshot(driver, result.getMethod().getMethodName());
//			test=extent.createTest(result.getName());
			test.log(Status.FAIL, result.getName().toUpperCase()+" is FAILED");
			test.addScreenCaptureFromPath(ReportUtils.screenshotPath);
			extent.flush();
		} catch (IOException e) {
			log.info("Failed to update report"+e.getMessage());
		}
	}

	public void onTestSkipped(ITestResult result) {

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onStart(ITestContext context) {
	}

	public void onFinish(ITestContext context) {

	}

}

package org.generic;

public interface IConstants {
	String configPath=System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\config.properties";
	String objectPath=System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\object.properties";
	String excelPath=System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\excel.properties";
	String chromeKey="webdriver.chrome.driver";
	String chromePath=System.getProperty("user.dir")+"\\src\\test\\resources\\driverExe\\chromedriver.exe";
	String extentPath=System.getProperty("user.dir")+"\\src\\test\\resources\\reports\\ExtentReport.html";
	int wait=10;
}

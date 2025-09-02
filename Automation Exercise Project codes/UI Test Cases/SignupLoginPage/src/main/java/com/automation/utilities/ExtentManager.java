package com.automation.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
	private static ExtentReports extent;
	static String projectpath=System.getProperty("user.dir");
	public static ExtentReports getinstance()
	{
		ExtentReports extent=new ExtentReports();
		ExtentSparkReporter spark=new ExtentSparkReporter(projectpath+"\\aug29threport.html");
		extent.attachReporter(spark);
		return extent;
		
	}

}

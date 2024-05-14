package com.comcast.crm.basetest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;

import com.comcast.crm.generic.databaseutility.DatabaseUtility;
import com.comcast.crm.generic.fileutility.ExcelUtility;
import com.comcast.crm.generic.fileutility.FileUtility;
import com.comcast.crm.generic.webdriverutility.JavaUtility;
import com.comcast.crm.generic.webdriverutility.UtilityClassObject;
import com.comcast.crm.generic.webdriverutility.WebDriverUtility;
import com.comcast.crm.objectrepositoryutility.HomePage;
import com.comcast.crm.objectrepositoryutility.LoginPage;

public class BaseClass {

	public static WebDriver driver = null;
	public static WebDriver sdriver = null;  //static variable used in IListener
	public static DatabaseUtility dbLib = new DatabaseUtility();
	public static FileUtility fLib = new FileUtility();
	public static JavaUtility jLib = new JavaUtility();
	public static ExcelUtility eLib = new ExcelUtility();
	public static WebDriverUtility wLib = new WebDriverUtility();
	public ExtentReports report;
	public ExtentReports test;

	@BeforeSuite(groups = "Smoke Testcase")
	public void configBS() throws SQLException {
		System.out.println("=======connect to db, report config=======");
		dbLib.getDbconnection();
			/*//spark report config
					ExtentSparkReporter spark= new ExtentSparkReporter("./AdvanceReport/report.html");
					spark.config().setDocumentTitle("CRM Test Suite Result");
					spark.config().setReportName("CRM Report");
					spark.config().setTheme(Theme.DARK);
					
					//add environment informstion and create test
					 report= new ExtentReports();
					report.attachReporter(spark);
					report.setSystemInfo("OS", "Windows 11");
					report.setSystemInfo("browser","chrome");*/
		}
	

//	@Parameters("browser")
	@BeforeClass(groups = "Smoke Testcase")
	public void configBC(/*String browser*/) throws IOException {
		System.out.println("==Launch the browser==");
		String browser1=fLib.getDataFromPropertiesFile("browser");
		if (browser1.equals("chrome")) {
			driver = new ChromeDriver();
		} else if (browser1.equals("firefox")) {
			driver = new FirefoxDriver();
		} else if (browser1.equals("edge")) {
			driver = new EdgeDriver();
		} else {
			driver = new ChromeDriver();
		}
		// wLib.waitForPageToLoad(driver);
		sdriver= driver;
		UtilityClassObject.setDriver(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@BeforeMethod(groups = "Smoke Testcase")
	public void configBM() throws IOException {
		System.out.println("=login=");
		LoginPage lp = new LoginPage(driver);
		String url = fLib.getDataFromPropertiesFile("url");
		String usn = fLib.getDataFromPropertiesFile("usn");
		String pwd = fLib.getDataFromPropertiesFile("pwd");
		lp.loginToApp(url, usn, pwd);
	}

	@AfterMethod(groups = "Smoke Testcase")
	public void configAM() {
		System.out.println("=logout=");
		HomePage hp = new HomePage(driver);
		hp.signOut();
	}

	@AfterClass(groups = "Smoke Testcase")
	public void configAC() {
		System.out.println("==Close the browser==");
		driver.quit();
	}

	@AfterSuite(groups = "Smoke Testcase")
	public void configAS() throws SQLException {
		System.out.println("=====close db, report backup===");
		dbLib.closeDbconnection();
		//report.flush();
	}
}

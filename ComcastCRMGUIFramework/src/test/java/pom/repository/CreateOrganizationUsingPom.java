package pom.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;

import com.comcast.crm.generic.fileutility.ExcelUtility;
import com.comcast.crm.generic.webdriverutility.JavaUtility;
import com.comcast.crm.generic.webdriverutility.WebDriverUtility;
import com.comcast.crm.objectrepositoryutility.CreateNewOrganizationPage;
import com.comcast.crm.objectrepositoryutility.HomePage;
import com.comcast.crm.objectrepositoryutility.LoginPage;
import com.comcast.crm.objectrepositoryutility.OrganizationInfoPage;
import com.comcast.crm.objectrepositoryutility.OrganizationPage;

public class CreateOrganizationUsingPom {
	public static void main(String[] args) throws IOException, InterruptedException {
		//read common data from property file
		FileInputStream fis= new FileInputStream("./configAppData/selenium_ddt.properties");
		Properties pobj= new Properties();
		pobj.load(fis);
		
		String browser= pobj.getProperty("browser");
		String url=pobj.getProperty("url");
		String usn= pobj.getProperty("usn");
		String pwd= pobj.getProperty("pwd");
		
		//generate random number
		JavaUtility jLib = new JavaUtility();
		WebDriverUtility wLib= new WebDriverUtility();
		ExcelUtility eLib= new ExcelUtility();
		
		//read testscript data from excel file
	      String orgName= eLib.getDataFromExcel("org", 1, 2)+ jLib.getRandomNumber();
	      
		WebDriver driver= null;
		if(browser.equals("chrome"))
		{
			driver= new ChromeDriver();
		}
		else if(browser.equals("firefox"))
		{
			driver= new FirefoxDriver();
		}
		else if(browser.equals("edge"))
		{
			driver= new EdgeDriver();
		}
		else
		{
			driver= new ChromeDriver();
		}
		
		//step1: login to app
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		
		//step 1 login
		LoginPage lp = new LoginPage(driver);
		lp.loginToApp(usn,pwd,url);
		
		//step 2 navigate to organization link 
		HomePage hp= new HomePage(driver);
		hp.getOrgLink().click();
		
		
		// step 3 click on"create organization"
		OrganizationPage op= new OrganizationPage(driver);
		op.getCreateOrg().click();
		
		//step 4 enter all the details and create new org
		CreateNewOrganizationPage cnp= new CreateNewOrganizationPage(driver);
		cnp.createOrg(orgName);
	
		//verify header msg Expected result
		OrganizationInfoPage oip= new OrganizationInfoPage(driver);
		String actOrgName=oip.getHeaderMsg().getText();
		
		if(actOrgName.contains(orgName)) {
			System.out.println(orgName+ "name is verified==PASS");
		}
		else {
			System.out.println(orgName+ "name is not verified==FAIL");
		}
		
		//step 5 logout
		hp.signOut();
		
		
		driver.quit();
		
		
		
		
}
}
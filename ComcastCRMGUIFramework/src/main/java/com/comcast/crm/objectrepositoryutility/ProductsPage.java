package com.comcast.crm.objectrepositoryutility;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductsPage {

	@FindBy(xpath = "//img[@title='Create Product...']")
	private WebElement newProductBtn;
	
	@FindBy(name = "search")
	private WebElement ele2;

	public WebElement getNewProductBtn() {
		return newProductBtn;
	}

	//getters 
	
	public WebElement getEle2() {
		return ele2;
	}
	
	
	
	
	
}

package automation;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utilities.ReadExcelData;

public class Main {

	static WebDriver driver;
	static double totalExpectedPrice;
	static String fileName = "TestData.xlsx";
	static String[] searchStrings;
	static Properties prop;

	/******Validation of prices*******/
	public static void flipkartAutomation(String searchString,
			int searchStringNumber) {
		
		System.out.println("*****Details for " + searchString +"*****");
		Flipkart.searchProducts(searchString);

		List<WebElement> products = Flipkart.getAllProducts(searchString);
		for (int i = 0; i < 2; i++) {

			Flipkart.selectProduct(products.get(i));

			Flipkart.addToCart();

			Double price = Flipkart.getPrice();
			System.out.println("Price of product " + (i + 1) + " is " + price);
			totalExpectedPrice += price;

			Flipkart.goBack();
		}

		Double totalActualPrice = Flipkart.getTotalPrice();
		System.out.println("The Actual Total price for " + searchString
				+ " is " + totalActualPrice);
		System.out.println("The Expected Total price for " + searchString
				+ " is " + totalExpectedPrice);

		if (totalActualPrice == totalExpectedPrice) {
			System.out.println("The prices are Correct");
		} else {
			System.out.println("The prices are NOT Correct");
		}
	}

	/****************** Main Method ***********************/
	public static void main(String[] args) {

		dataSetter();
		String browser = Flipkart.chooseBrowser();
		Flipkart.driverSetup(browser);
		Flipkart.loadProperties();
		Flipkart.openHomePage();
		
		
		for (int i = 0; i < searchStrings.length; i++) {
			flipkartAutomation(searchStrings[i], i);
		}

		Flipkart.closeAllTheBrowser();
	}

	/******* Setting the variables from Excel Data ********/
	public static void dataSetter() {
		ReadExcelData red = new ReadExcelData(System.getProperty("user.dir")
				+ "\\src/test/java\\testData\\" + fileName);

		int rowCount = red.getRowCount();
		searchStrings = new String[rowCount];
		for (int i = 3; i < 3 + rowCount; i++) {
			searchStrings[i - 3] = red.getCellData(i, 1);
		}

	}
}

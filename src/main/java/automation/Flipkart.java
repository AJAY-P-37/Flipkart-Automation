package automation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Flipkart {

	static WebDriver driver;
	static double price;
	static Iterator<String> windowHandles;
	static String mainTab;
	static String newTab;
	static Properties prop;
	static String websiteURL, xButton, searchButton, searchBar, products,
			addToCartButton, priceText1, priceText2, cartButton, totalPrice;

	/******* Choose the browser *******/
	public static String chooseBrowser() {

		System.out.println("Choose the Browser: Chrome or FireFox");

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		String browser = scan.next();

		return browser;
	}

	/********** Setting up the Driver *******************/
	public static void driverSetup(String browser) {

		if (browser.equalsIgnoreCase("chrome")) {

			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir")
							+ "\\resources\\Drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {

			System.setProperty("webdriver.gecko.driver",
					System.getProperty("user.dir")
							+ "\\resources\\Drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		}

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();

	}

	/******** Loading the Properties file ************/
	public static void loadProperties() {
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\resources\\propertiesFile\\elementLocators.properties");
			prop.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		websiteURL = prop.getProperty("websiteURL");
		xButton = prop.getProperty("xButton_Xpath");
		searchBar = prop.getProperty("searchBar_Xpath");
		searchButton = prop.getProperty("searchButton_Xpath");
		products = prop.getProperty("productsLink_Xpath");
		addToCartButton = prop.getProperty("addToCart_Xpath");
		priceText1 = prop.getProperty("priceText_Xpath");
		priceText2 = prop.getProperty("priceText_CSS");
		cartButton = prop.getProperty("cart_ClassName");
		totalPrice = prop.getProperty("totalPriceText1_Xpath");

	}

	/******* Open the Home Page of FLIPKART ******/
	public static void openHomePage() {

		driver.get(websiteURL);

		driver.findElement(By.xpath(xButton)).click();

		String expTitle = "Online Shopping Site for Mobiles, Electronics, Furniture, Grocery, Lifestyle, Books & More. Best Offers!";

		String actTitle = driver.getTitle();

		if (expTitle.equalsIgnoreCase(actTitle)) {

			System.out.println("Home Page is Loaded");
		} else {

			System.out.println("Home Page is NOT Loaded");
		}

	}

	

	/************* Search for the results **********/
	public static void searchProducts(String searchString) {

		driver.findElement(By.xpath(searchBar)).sendKeys(searchString);

		driver.findElement(By.xpath(searchButton)).click();
	}

	public static void selectSearchString(String searchString) {
		if (searchString.equalsIgnoreCase("home appliances")) {
			products = prop.getProperty("homeAppliances_ClassName");
		} else if (searchString.equalsIgnoreCase("mobile")) {
			products = prop.getProperty("mobiles_ClassName");
		} else if (searchString.equalsIgnoreCase("laptop")) {
			products = prop.getProperty("laptop_ClassName");
		}

	}

	/*************** Get all the products *************/
	public static List<WebElement> getAllProducts(String searchString) {

		selectSearchString(searchString);
		List<WebElement> temp = driver.findElements(By.className(products));
		if (temp.size() != 0) {
			return temp;
		}
		temp = driver.findElements(By.className(prop
				.getProperty("laptop_ClassName")));
		return temp;

	}

	/************** Switch to New Tab *************/
	public static void goToNewTab() {
		windowHandles = driver.getWindowHandles().iterator();
		mainTab = windowHandles.next();
		newTab = windowHandles.next();

		driver.switchTo().window(newTab);
	}

	/************ Select the Product ************/
	public static void selectProduct(WebElement element) {

		element.click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		goToNewTab();
	}

	/*************** Add To Cart ***********/
	public static void addToCart() {

		driver.findElement(By.xpath(addToCartButton)).click();
	}

	/****** Display the price **********/
	public static double getPrice() {
		String[] priceString;
		try {
			priceString = driver.findElement(By.xpath(priceText1)).getText().trim()
					.substring(1).split(",");
		} catch (NoSuchElementException e) {
			priceString = driver.findElement(By.cssSelector(priceText2))
					.getText().trim().substring(1).split(",");
		}
		String temp = "";
		for (String s : priceString) {
			temp += s;
		}
		price = Double.valueOf(temp);

		return price;
	}

	/*********** Total Price *************/
	public static double getTotalPrice() {

		driver.findElement(By.className(cartButton)).click();
		String[] totalPriceString;
		try{
		totalPriceString = driver.findElement(By.xpath(totalPrice))
				.getText().trim().substring(1).split(",");
		}catch(NoSuchElementException e){
			totalPriceString = driver.findElement(By.xpath(prop.getProperty("totalPriceText2_Xpath")))
					.getText().trim().substring(1).split(",");
		}
		String temp = "";
		for (String s : totalPriceString) {
			temp += s;
		}
		price = Double.valueOf(temp);

		return price;

	}

	/******* Go Back to Main Tab **********/
	public static void goBack() {

		driver.close();
		driver.switchTo().window(mainTab);
	}

	/********** Close the Browser ***********/
	public static void closeAllTheBrowser() {

		driver.quit();
	}
}

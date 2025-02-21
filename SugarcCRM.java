package Project.Pro;
 
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import junit.framework.Assert;
//import static org.testng.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import java.time.Duration;
 
public class SugarcCRM {
    WebDriver driver;
    WebDriverWait wait;
 
    static int i=0;
	public void display() throws IOException {
		 i=i+1;
		String image="./screenshots/pass"+i+".png";
		 File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	      FileUtils.copyFile(screenshot, new File(image));
 
	}
	
    // Logger setup using java.util.logging
    private static final Logger logger = Logger.getLogger(SugarcCRM.class.getName());
 
    // Initialize driver and wait once in @BeforeClass
    @Parameters({"browser"})
    @BeforeClass
    public void setup(@Optional("chrome")String browser) throws InterruptedException, IOException {
        // Set up logger to log to both console and file
        try {
            FileHandler fileHandler = new FileHandler("./logs/automation.log", true);  // true for append mode
            fileHandler.setFormatter(new SimpleFormatter());  // Setting a simple formatter for logs
            logger.addHandler(fileHandler);
            // Also log to console
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());  // Simple formatter for console output
            logger.addHandler(consoleHandler);
            // Set the log level
            logger.setLevel(Level.INFO);
            logger.info("Logger setup complete.");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
 
        logger.info("Setting up the WebDriver and navigating to the URL.");
 
        // Initialize driver based on browser parameter
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        }
        driver.get("https://www.sugarcrm.com/au/solutions/");
        driver.manage().window().maximize();
        display();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));       
    }
 
    private void enterTextById(String id, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        logger.info("Entering text into the field with ID: " + id);
        element.sendKeys(text);
    }
 
    private void clickById(String id) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        logger.info("Clicking on the element with ID: " + id);
        element.click();
    }
 
    private void selectDropdownById(String id, String value) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        new Select(dropdown).selectByValue(value);
        logger.info("Selecting value '" + value + "' from the dropdown with ID: " + id);
    }
 
    @Test(priority = 0, description = "Accept cookies and verifying Title")
    public void acceptCookies() throws IOException {
        logger.info("Starting test to accept cookies.");
        Reporter.log("Starting test to accept cookies.", true);
        
        clickById("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll");
        
        logger.info("Cookies accepted.");
        Reporter.log("Cookies accepted.", true);
        
        display();
        
        // Expected title of the page
        String expectedTitle = "CRM Software Solutions for HD-CX | Marketing, Sales & Service Solutions | SugarCRM";
        String actualTitle = driver.getTitle();
        
        // Print both the actual and expected title for debugging
        System.out.println("Expected Title: '" + expectedTitle + "'");
        System.out.println("Actual Title: '" + actualTitle + "'");
        Reporter.log("Expected Title: '" + expectedTitle + "'", true);
        Reporter.log("Actual Title: '" + actualTitle + "'", true);
        
        Screenshotmethod.takeScreenshot(driver, "Cookiesaccepted");
        String path1="C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\Cookiesaccepted.png";
        Reporter.log("Click here to see Screenshot'<a href='" + path1 + "'>view screenshot</a>'");

        // Assert that the actual title matches the expected title
        try {
            Assert.assertEquals(actualTitle, expectedTitle);
            System.out.println("Page title is correct.");
            Reporter.log("Page title is correct.", true);
        } catch (AssertionError titleException) {
            System.out.println("Assertion failed: ");
            Reporter.log("Assertion failed: " + titleException.getMessage(), true);
            throw titleException; // Rethrow the exception to fail the test
        }
    }
    
    @Test(priority = 1, description = "Logo founded and take screenshot " )
    public void logoscreenshot() throws IOException {
        try {
            logger.info("Starting test to take screenshot of the logo.");
            Reporter.log("Starting test to take screenshot of the logo.", true);

            // Find the logo element
            WebElement logo = driver.findElement(By.xpath("//img[contains(@alt, 'SugarCRM')]"));
            
            // Assert that the logo is displayed
            Assert.assertTrue(logo.isDisplayed());
            Reporter.log("Logo is displayed successfully.", true);
            
            // Take the screenshot of the logo
            File logoScreenshot = logo.getScreenshotAs(OutputType.FILE);
            
            // Save the screenshot to the specified location
            String screenshotPath = "./screenshots/logo_screenshot.png";
            FileUtils.copyFile(logoScreenshot, new File(screenshotPath));
            
            logger.info("Logo screenshot successfully taken.");
            Reporter.log("Logo screenshot successfully taken. Saved at: " + screenshotPath, true);
            
        } catch (NoSuchElementException e) {
            logger.severe("Error: Logo element not found. " + e.getMessage());
            Reporter.log("Error: Logo element not found. " + e.getMessage(), true);
            Assert.fail("Logo element not found: " + e.getMessage());
            
        } catch (IOException e) {
            logger.severe("Error: Unable to save the screenshot. " + e.getMessage());
            Reporter.log("Error: Unable to save the screenshot. " + e.getMessage(), true);
            Assert.fail("Failed to save the screenshot: " + e.getMessage());
            
        } catch (AssertionError e) {
            logger.severe("Assertion failed: " + e.getMessage());
            Reporter.log("Assertion failed: " + e.getMessage(), true);
            throw e;  // Rethrow the assertion error to fail the test
            
        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
            Reporter.log("Unexpected error: " + e.getMessage(), true);
            Assert.fail("Test failed due to unexpected error: " + e.getMessage());
        }
    }    
    

@Test(priority = 2, dependsOnMethods = {"acceptCookies"}, description = "Navigated to header solution and checked watch demo button is working")
public void headercrmsolution() {
    try {
        Reporter.log("Starting test: Navigating to CRM Solution header.", true);
        
        // Locate and click on the CRM Solution header
        WebElement headerfirst = driver.findElement(By.xpath("//*[@id=\"menu-item-20406\"]/a"));
        headerfirst.click();
        Reporter.log("Clicked on CRM Solution header.", true);
        
        // Scroll down the page by 3200 pixels
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,3200);");
        Reporter.log("Scrolled down the page by 3200 pixels.", true);
        
        // Click on the "Watch Demo" button using JavaScript
        WebElement watchDemoButton = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div[2]/a"));
        JavascriptExecutor watchdemo = (JavascriptExecutor) driver;
 
        
        Screenshotmethod.takeScreenshot(driver, "watchdemobutton");
        String path1="C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\watchdemobutton.png";
        Reporter.log("Click here to see Screenshot'<a href='" + path1 + "'>view screenshot</a>'");

        
        watchdemo.executeScript("arguments[0].click();", watchDemoButton);
        Reporter.log("Clicked on the 'Watch Demo' button.", true);
        
        // Navigate back to the previous page
        driver.navigate().back();
        Reporter.log("Navigated back to the previous page.", true);
        
    } catch (NoSuchElementException e) {
        Reporter.log("Error: Element not found - " + e.getMessage(), true);
        System.out.println("Element not found: " + e.getMessage());
        Assert.fail("Test failed due to element not found.");
        
    } catch (TimeoutException e) {
        Reporter.log("Error: Timeout occurred while waiting for element - " + e.getMessage(), true);
        System.out.println("Timeout occurred while waiting for element: " + e.getMessage());
        Assert.fail("Test failed due to timeout.");
        
    } catch (Exception e) {
        Reporter.log("Error: Unexpected error occurred - " + e.getMessage(), true);
        System.out.println("An unexpected error occurred: " + e.getMessage());
        Assert.fail("Test failed due to unexpected error.");
    }
}
 

@Test(priority = 3, dependsOnMethods = {"acceptCookies"}, description = "Navigate to Pricing section and checking USD,AUD")
public void headerpricing() {
    try {
        Reporter.log("Starting test: Verifying pricing header and currency selection.", true);
        
        // Find and click the second header menu item
        WebElement headersecond = driver.findElement(By.xpath("//*[@id=\"menu-item-25016\"]/a"));
        headersecond.click();
        Reporter.log("Clicked on Pricing header.", true);

        // Scroll to the bottom of the page using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,300);");
        Reporter.log("Scrolled down by 300 pixels.", true);

        // Click on the USD toggle
        JavascriptExecutor USD = (JavascriptExecutor) driver;
        USD.executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"toggles-0\"]/div[1]/label")));
        Reporter.log("Switched currency to USD.", true);
        
        Screenshotmethod.takeScreenshot(driver, "USDPricingcheck");
        String path1="C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\USDPricingcheck.png";
        Reporter.log("Click here to see Screenshot'<a href='" + path1 + "'>view screenshot</a>'");


        // Verify USD text
        WebElement element = driver.findElement(By.xpath("//div[@id='sales-product']//div[contains(@class,'container')]//div[1]//div[1]//div[1]//div[1]//div[1]//span[1]//small[1]"));
        String actualText = element.getText();
        String expectedText = "(USD)";

        try {
            Assert.assertEquals(actualText, expectedText);
            Reporter.log("Verified that currency displayed is USD.", true);
        } catch (AssertionError titleException) {
            Reporter.log("Assertion failed: Expected USD but found " + actualText, true);
            throw titleException; // Rethrow to fail the test
        }

        display();

        // Click on the AUD toggle
        JavascriptExecutor AUD = (JavascriptExecutor) driver;
        AUD.executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"toggles-0\"]/div[2]/label")));
        Reporter.log("Switched currency to AUD.", true);

        Screenshotmethod.takeScreenshot(driver, "AUDPricingcheck");
        String path2="C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\AUDPricingcheck.png";
        Reporter.log("Click here to see Screenshot'<a href='" + path2 + "'>view screenshot</a>'");

        // Verify AUD text
        WebElement elementAUD = driver.findElement(By.xpath("//*[@id=\"sales-product\"]/div[1]/div[3]/div[1]/div[1]/div/div/div[1]/span[2]/small"));
        String actualTextAUD = elementAUD.getText();
        String expectedTextAUD = "(AUD)";

        try {
            Assert.assertEquals(actualTextAUD, expectedTextAUD);
            Reporter.log("Verified that currency displayed is AUD.", true);
        } catch (AssertionError titleException) {
            Reporter.log("Assertion failed: Expected AUD but found " + actualTextAUD, true);
            throw titleException; // Rethrow to fail the test
        }

        display();

        Thread.sleep(3000);
        Reporter.log("Test completed successfully.", true);

    } catch (NoSuchElementException e) {
        Reporter.log("Error: Element not found - " + e.getMessage(), true);
        Assert.fail("Test failed due to element not found.");
        
    } catch (TimeoutException e) {
        Reporter.log("Error: Timeout occurred while waiting for element - " + e.getMessage(), true);
        Assert.fail("Test failed due to timeout.");
        
    } catch (InterruptedException e) {
        Reporter.log("Error: Thread was interrupted - " + e.getMessage(), true);
        Assert.fail("Test failed due to interruption.");
        
    } catch (Exception e) {
        Reporter.log("Error: Unexpected error occurred - " + e.getMessage(), true);
        Assert.fail("Test failed due to unexpected error.");
    }
}
 
@Test(priority = 4, dependsOnMethods = {"acceptCookies"}, description = "Navigate to Industries and verifying contact as button is working" )
public void headerIndustries() {
    try {
        Reporter.log("Starting test: Navigating to Industries header.", true);
        
        // Find and click the third header menu item
        WebElement headerthird = driver.findElement(By.xpath("//*[@id=\"menu-item-24670\"]/a"));
        headerthird.click();
        Reporter.log("Clicked on Industries header.", true);

        // Click on Financial Services
        WebElement financialservice = driver.findElement(By.xpath("//span[text()=\"Financial Services\"]"));
        financialservice.click();
        Reporter.log("Clicked on Financial Services option.", true);

        // Scroll to the bottom of the page using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Reporter.log("Scrolled to the bottom of the page.", true);

        Screenshotmethod.takeScreenshot(driver, "Lookingcontactus");
        String path2="C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\Lookingcontactus.png";
        Reporter.log("Click here to see Screenshot'<a href='" + path2 + "'>view screenshot</a>'");

        // Click on LinkedIn footer link using JavaScript
        JavascriptExecutor linkedin = (JavascriptExecutor) driver;
        WebElement linkedinLink = driver.findElement(By.xpath("/html/body/div[2]/footer/div/div[1]/div[1]/div[1]/ul/li[4]/a"));
        linkedin.executeScript("arguments[0].click();", linkedinLink);
        Reporter.log("Clicked on LinkedIn footer link.", true);

        Screenshotmethod.takeScreenshot(driver, "NavigatetoLinkedin");
        String path1="C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\NavigatetoLinkedin.png";
        Reporter.log("Click here to see Screenshot'<a href='" + path1 + "'>view screenshot</a>'");

        display();

        // Navigate back
        driver.navigate().back();
        Reporter.log("Navigated back to the previous page.", true);

        Thread.sleep(3000);
        Reporter.log("Test completed successfully.", true);

    } catch (NoSuchElementException e) {
        Reporter.log("Error: Element not found - " + e.getMessage(), true);
        Assert.fail("Test failed due to element not found.");
        
    } catch (TimeoutException e) {
        Reporter.log("Error: Timeout occurred while waiting for element - " + e.getMessage(), true);
        Assert.fail("Test failed due to timeout.");
        
    } catch (InterruptedException e) {
        Reporter.log("Error: Thread was interrupted - " + e.getMessage(), true);
        Assert.fail("Test failed due to interruption.");
        
    } catch (Exception e) {
        Reporter.log("Error: Unexpected error occurred - " + e.getMessage(), true);
        Assert.fail("Test failed due to unexpected error.");
    }
}

@Test(priority = 5, dependsOnMethods = {"acceptCookies"})
public void headerwhysugar() {
    try {
        Reporter.log("Starting test: Navigating to 'Why Sugar' header.", true);
        
        // Find and click the fourth header menu item
        WebElement headerfourth = driver.findElement(By.xpath("//*[@id=\"menu-item-374\"]/a"));
        headerfourth.click();
        Reporter.log("Clicked on 'Why Sugar' header.", true);

        // Scroll to the bottom of the page using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Reporter.log("Scrolled to the bottom of the page.", true);

        Thread.sleep(3000);

        // Click on the language change dropdown
        WebElement changelanguage = driver.findElement(By.xpath("//*[@id=\"dropdownMenuButton\"]"));
        changelanguage.click();
        Reporter.log("Clicked on language dropdown.", true);

        Screenshotmethod.takeScreenshot(driver, "Languagebuttonclicked");
        String path2="C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\Languagebuttonclicked.png";
        Reporter.log("Click here to see Screenshot'<a href='" + path2 + "'>view screenshot</a>'");

        // Select the French language option
        WebElement franch = driver.findElement(By.xpath("//*[@id=\"menu-language-menu-1\"]/li[3]/ul/li[3]/a"));
        franch.click();
        Reporter.log("Selected French language option.", true);

        // Wait for a specific element on the French version of the page to ensure it has loaded
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement checkelement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/div[1]/div[2]/div/div/h1")));
        Assert.assertTrue(checkelement.isDisplayed());
        
        Screenshotmethod.takeScreenshot(driver, "Languagepage");
        String path1="C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\Languagepage.png";
        Reporter.log("Click here to see Screenshot'<a href='" + path1+ "'>view screenshot</a>'");

        
        Reporter.log("Verified that the French version of the page has loaded successfully.", true);

        display();

        Thread.sleep(3000);

        // Navigate back
        driver.navigate().back();
        Reporter.log("Navigated back to the previous page.", true);

        // Wait for a specific element on the new page to ensure it has loaded
        WebElement someElementOnTheNewPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/div[2]/div/div[6]/div/div[3]/div/div/a")));
        Assert.assertTrue(someElementOnTheNewPage.isDisplayed());
        Reporter.log("Verified that the original page has loaded successfully after navigating back.", true);

        Thread.sleep(3000);
        Reporter.log("Test completed successfully.", true);

    } catch (NoSuchElementException e) {
        Reporter.log("Error: Element not found - " + e.getMessage(), true);
        Assert.fail("Test failed due to element not found.");
        
    } catch (TimeoutException e) {
        Reporter.log("Error: Timeout occurred while waiting for element - " + e.getMessage(), true);
        Assert.fail("Test failed due to timeout.");
        
    } catch (InterruptedException e) {
        Reporter.log("Error: Thread was interrupted - " + e.getMessage(), true);
        Assert.fail("Test failed due to interruption.");
        
    } catch (Exception e) {
        Reporter.log("Error: Unexpected error occurred - " + e.getMessage(), true);
        Assert.fail("Test failed due to unexpected error.");
    }
}
 
@Test(priority = 6, dependsOnMethods = {"acceptCookies"} , description = "")
public void headerPartner() throws InterruptedException, IOException {
    try {
        Reporter.log("Starting test: Navigating to 'Partner' header.", true);

        // Find and click the 'Partner' header menu item
        WebElement headerfive = driver.findElement(By.xpath("//*[@id=\"menu-item-21322\"]/a"));
        headerfive.click();
        Reporter.log("Clicked on 'Partner' header.", true);

        // Scroll to the bottom of the page using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Reporter.log("Scrolled to the bottom of the page.", true);

        Thread.sleep(3000);

        display();
        Reporter.log("Displayed the required section.", true);

        // Wait for a specific element on the new page to ensure it has loaded correctly
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement someElementOnTheNewPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/footer/div/div[2]/div")));
        
        // Assert that the element is visible, confirming the page is loaded
        Assert.assertTrue(someElementOnTheNewPage.isDisplayed());
        Reporter.log("Verified that the page has loaded correctly.", true);

        Thread.sleep(3000);
        Reporter.log("Test completed successfully.", true);

    } catch (NoSuchElementException e) {
        Reporter.log("Error: Element not found - " + e.getMessage(), true);
        Assert.fail("Test failed due to element not found.");
        
    } catch (TimeoutException e) {
        Reporter.log("Error: Timeout occurred while waiting for element - " + e.getMessage(), true);
        Assert.fail("Test failed due to timeout.");
        
    } catch (InterruptedException e) {
        Reporter.log("Error: Thread was interrupted - " + e.getMessage(), true);
        Assert.fail("Test failed due to interruption.");
        
    } catch (Exception e) {
        Reporter.log("Error: Unexpected error occurred - " + e.getMessage(), true);
        Assert.fail("Test failed due to unexpected error.");
    }
}
 
    
@Test(priority = 7, dependsOnMethods = {"acceptCookies"})
public void Getdemo() throws InterruptedException {
    try {
        Reporter.log("Starting test: Clicking on 'Get a Demo' button.", true);

        // Find and click the 'Get a Demo' button
        WebElement demo = driver.findElement(By.xpath("//span[normalize-space()='Get a Demo']"));
        demo.click();
        Reporter.log("Clicked on 'Get a Demo' button.", true);

        // Wait for the demo form to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement demoForm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/div/div/div[1]/div[2]/div/div/div[1]/div/h2")));

        Assert.assertTrue(demoForm.isDisplayed());
        Reporter.log("Verified that the demo form is displayed.", true);

        Thread.sleep(3000);
        Reporter.log("Test completed successfully.", true);

    } catch (NoSuchElementException e) {
        Reporter.log("Error: Element not found - " + e.getMessage(), true);
        Assert.fail("Test failed due to element not found.");
        
    } catch (TimeoutException e) {
        Reporter.log("Error: Timeout occurred while waiting for element - " + e.getMessage(), true);
        Assert.fail("Test failed due to timeout.");
        
    } catch (InterruptedException e) {
        Reporter.log("Error: Thread was interrupted - " + e.getMessage(), true);
        Assert.fail("Test failed due to interruption.");
        
    } catch (Exception e) {
        Reporter.log("Error: Unexpected error occurred - " + e.getMessage(), true);
        Assert.fail("Test failed due to unexpected error.");
    }
}
 
    

@Test(priority = 8, dependsOnMethods = {"Getdemo"} ,  description = "Enter user details")
public void enterUserDetails() {
    try {
        Reporter.log("Starting test: Entering user details.", true);

        enterTextById("input_1_1", "mrdineshbabu27@gmail.com");
        Reporter.log("Entered Email: mrdineshbabu27@gmail.com", true);

        enterTextById("input_1_3_3", "Dinesh");
        Reporter.log("Entered First Name: Dinesh", true);

        enterTextById("input_1_3_6", "Babu");
        Reporter.log("Entered Last Name: Babu", true);

        enterTextById("input_1_4", "9384593432");
        Reporter.log("Entered Phone Number: 9384593432", true);

        Reporter.log("User details entered successfully.", true);

        // Create a SoftAssert object
        SoftAssert softAssert = new SoftAssert();

        // Get the values entered in the fields
        String emailEntered = driver.findElement(By.id("input_1_1")).getAttribute("value");
        String firstNameEntered = driver.findElement(By.id("input_1_3_3")).getAttribute("value");
        String lastNameEntered = driver.findElement(By.id("input_1_3_6")).getAttribute("value");
        String phoneEntered = driver.findElement(By.id("input_1_4")).getAttribute("value");

        
        Screenshotmethod.takeScreenshot(driver, "Entereduserdetails");
        String path1="C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\Entereduserdetails.png";
        Reporter.log("Click here to see Screenshot'<a href='" + path1+ "'>view screenshot</a>'");

        // Use SoftAssert to check the entered values
        softAssert.assertEquals(emailEntered, "mrdineshbabu27@gmail.com", "Email is not entered correctly.");
        softAssert.assertEquals(firstNameEntered, "Dinesh", "First name is not entered correctly.");
        softAssert.assertEquals(lastNameEntered, "Babu", "Last name is not entered correctly.");
        softAssert.assertEquals(phoneEntered, "9384593432", "Phone number is not entered correctly.");

        Reporter.log("Validated all user input fields successfully.", true);

        // Assert all the soft assertions at once
        softAssert.assertAll();
        Reporter.log("Test completed successfully.", true);

    } catch (Exception e) {
        Reporter.log("Test failed due to unexpected error: " + e.getMessage(), true);
        Assert.fail("Unexpected error occurred: " + e.getMessage());
    }
}

 


@Test(priority = 9, dependsOnMethods = {"enterUserDetails"}, description = "Enter Job details")
public void enterJobDetails() {
    SoftAssert softAssert = new SoftAssert();

    try {
        Reporter.log("Starting test: Entering job details.", true);

        enterTextById("input_1_5", "Software Engineer");
        Reporter.log("Entered Job Title: Software Engineer", true);

        enterTextById("input_1_6", "Aspire System");
        Reporter.log("Entered Company Name: Aspire System", true);

        Screenshotmethod.takeScreenshot(driver, "Jobdetails");
        String path1 = "C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\Jobdetails.png";
        Reporter.log("Click here to see Screenshot '<a href='" + path1 + "'>view screenshot</a>'", true);

        Reporter.log("Job details entered successfully.", true);

        // Validate entered values using SoftAssert
        String jobTitleEntered = driver.findElement(By.id("input_1_5")).getAttribute("value");
        String companyNameEntered = driver.findElement(By.id("input_1_6")).getAttribute("value");

        softAssert.assertEquals(jobTitleEntered, "Software Engineer", "Job Title is not entered correctly.");
        softAssert.assertEquals(companyNameEntered, "Aspire System", "Company Name is not entered correctly.");

        Reporter.log("Validated job details successfully.", true);

    } catch (Exception e) {
        Reporter.log("Test failed due to unexpected error: " + e.getMessage(), true);
        Assert.fail("Unexpected error occurred: " + e.getMessage());
    }

    // Ensure all soft assertions are verified
    softAssert.assertAll();
}


@Test(priority = 10, dependsOnMethods = {"enterJobDetails"}, description = "Selecting Company Size And Industry")
public void selectCompanySizeAndIndustry() {
    SoftAssert softAssert = new SoftAssert();

    try {
        Reporter.log("Starting test: Selecting company size and industry.", true);

        // Select Company Size
        selectDropdownById("input_1_8", "level3");
        Reporter.log("Selected Company Size: 51-200 employees", true);

        // Select Industry
        selectDropdownById("input_1_75", "Consulting_Business_Services");
        Reporter.log("Selected Industry: Consulting & Business Services", true);

        // Capture Screenshot
        Screenshotmethod.takeScreenshot(driver, "companysize");
        String path1 = "C:\\Users\\dineshbabu.ravi\\eclipse-workspace\\Pro\\screenots\\companysize.png";
        Reporter.log("Click here to see Screenshot '<a href='" + path1 + "'>view screenshot</a>'", true);

        Reporter.log("Company size and industry selected successfully.", true);

        // Validate selected values using SoftAssert
        String companySizeSelected = driver.findElement(By.id("input_1_8")).getAttribute("value");
        String industrySelected = driver.findElement(By.id("input_1_75")).getAttribute("value");

        softAssert.assertEquals(companySizeSelected, "level3", "Company Size is not selected correctly.");
        softAssert.assertEquals(industrySelected, "Consulting_Business_Services", "Industry is not selected correctly.");

        Reporter.log("Validated company size and industry selection successfully.", true);

    } catch (Exception e) {
        Reporter.log("Test failed due to unexpected error: " + e.getMessage(), true);
        Assert.fail("Unexpected error occurred: " + e.getMessage());
    }

    // Ensure all soft assertions are verified at the end
    softAssert.assertAll();
}

    @Test(priority = 11, dependsOnMethods = {"selectCompanySizeAndIndustry"})
    public void enterAdditionalInfo() throws IOException {
        logger.info("Starting test to enter additional information.");
        enterTextById("input_1_10", "By Random search");
        logger.info("Additional information entered successfully.");
        
        display();
        
    }
 
    @Test(priority = 12, dependsOnMethods = {"enterAdditionalInfo"})
    public void submit() throws InterruptedException {
        WebElement submitbutton = driver.findElement(By.xpath("//*[@id=\"menu-item-25016\"]/a"));
        submitbutton.click();
        
        Thread.sleep(3000);
    }
 
    @Test(priority = 13, dependsOnMethods = {"submit"})
    public void takeScreenshot() throws IOException {
        logger.info("Starting screenshot capture.");
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("./screenshots/screenshot4.png"));
        logger.info("Screenshot saved as screenshot4.png.");
        
    }
    
    @AfterClass
    public void teardown() throws IOException {
        logger.info("Starting teardown.");
        takeScreenshot();
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed.");
        }
    }
}
 

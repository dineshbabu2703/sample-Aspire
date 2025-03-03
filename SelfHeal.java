package Project.Pro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
 
public class SelfHeal {
 
	public WebElement findElementwithRetry(WebDriver driver, String primaryElem, String[] alternateElem) {
		WebElement applicationTitle = null;
		try {
			applicationTitle = driver.findElement(By.xpath(primaryElem));
		} catch (Exception exception) {
			Reporter.log(primaryElem + "    Primary locators failed trying self healed with alternative locators",true);
 
			for (String altlocator : alternateElem) {
				try {
					applicationTitle = driver.findElement(By.xpath(altlocator));
					Reporter.log(altlocator + "    Alternate locator is chosen and worked please update locators",true);
					break; // Exit loop if found
				} catch (Exception exe) {
					// Continue to the next locator if one fails
					Reporter.log(altlocator + "    Alternate locators is chosen and failed please update the locators",true);
				}
			}
		}
		return applicationTitle;
	}
}

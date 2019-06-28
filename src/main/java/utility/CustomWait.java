package utility;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pojo.Keywords;

public class CustomWait {
	WebDriver driver;
	DriverSupport support;
	MyLogger logger;
	final public static int Default_Wait = 45;

	public CustomWait(WebDriver driver) {
		this.driver = driver;
		logger = new MyLogger(Thread.currentThread().getStackTrace()[2].getClassName());
		support = new DriverSupport(driver);
	}

	public void waitForPageLoad() {

		try {
			WebDriverWait wait = new WebDriverWait(driver, Default_Wait);
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver wdriver) {
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
				}
			});
		} catch (Exception e) {
			staticWait(3);
		}
	}

	public void staticWait(int waitTime) {
		try {
			Thread.sleep(waitTime * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean explicitWait(int timeToWait, Keywords howToIdentify, String elementId) {
		By locator = support.customLocator(howToIdentify, elementId);
		return explicitWait(timeToWait, locator);
	}

	public boolean explicitWait(int timeToWait, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// explicit wait for an element in a frame
	public void explicitWaitFrameElement(int timeToWait, Keywords howToIdentify, String elementId, List<String> nestedFrames) {
		if(support.switchToFrameWithWait(nestedFrames, timeToWait)) {
			explicitWait(timeToWait, howToIdentify, elementId);
		}
	}

	// explicit wait for an element in frame
	public void explicitWaitWindowElement(int timeToWait, Keywords howToIdentify, String elementId, String window) {
		if(support.switchToWindow(window, timeToWait)) {
			explicitWait(timeToWait, howToIdentify, elementId);
		}
	}

	public boolean staleWait(int timeToWait, WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeToWait);
			wait.until(ExpectedConditions.stalenessOf(element));
			return true;
		} catch (Exception e) {
			logger.error("Element is not stolen");
			return false;
		}
	}

	public boolean enableWait(int timeToWait, Keywords howToIdentify, String elementId) {
		By locator = support.customLocator(howToIdentify, elementId);
		return enableWait(timeToWait, locator);
	}

	public boolean enableWait(int timeToWait, By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeToWait);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			return true;
		} catch (Exception e) {
			logger.error("Disabled Element: " + locator);
			return false;
		}
	}

	public boolean waitForElementWithoutException(int timeToWait, Keywords howToIdentify, String elementId) {
		try {
			explicitWait(timeToWait, howToIdentify, elementId);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean waitForStalenessOfElement(int timeToWait, Keywords howToIdentify, String elementId) {
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		By locator = support.customLocator(howToIdentify, elementId);
		WebElement element = driver.findElement(locator);

		try {
			wait.until(ExpectedConditions.stalenessOf(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean waitForInvisibilityOfElement(int timeToWait, Keywords howToIdentify, String elementId) {
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		By locator = support.customLocator(howToIdentify, elementId);

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void waitForElementWithinFrame(int timeToWait, String frameNames, Keywords howToIdentify, String elementId)
			throws Exception {
		support.switchToFrame(frameNames, ",");
		explicitWait(timeToWait, howToIdentify, elementId);
	}

}
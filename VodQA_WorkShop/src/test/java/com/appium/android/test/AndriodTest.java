package com.appium.android.test;

import java.io.File;
import java.net.MalformedURLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

/*
 * Code Snippet to run Appium Server programatically
 * Scroll/Swipe has been implemented
 * If running the test from windows please change the appium.js path @ line 39
 */

public class AndriodTest {

	private static final String Image_Scrollable = "org.wordpress.android:id/image_featured";
	public static final String PASSWORD = "org.wordpress.android:id/nux_password";
	public static final String USERNAME = "org.wordpress.android:id/nux_username";
	AppiumDriver<MobileElement> driver;
	AppiumDriverLocalService appiumDriverLocalService;
	@Before
	public void setUp() throws MalformedURLException {
		 AppiumServiceBuilder builder = new AppiumServiceBuilder().withAppiumJS(new File("/usr/local/lib/node_modules/appium/bin/appium.js"))
				 .withArgument(GeneralServerFlag.APP,
				                System.getProperty("user.dir") + "/build/wordpress.apk")
				               .withArgument(GeneralServerFlag.LOG_LEVEL, "info").usingAnyFreePort(); /*and so on*/;
				         appiumDriverLocalService = builder.build();
				         appiumDriverLocalService.start();
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "9111833b");
		caps.setCapability("platformVersion", "5.0.2");
		caps.setCapability("app", System.getProperty("user.dir") + "/build/wordpress.apk");
		caps.setCapability("package", "org.wordpress.android");
		caps.setCapability("appActivity", "org.wordpress.android.ui.WPLaunchActivity");
		driver = new AndroidDriver<MobileElement>(appiumDriverLocalService.getUrl(), caps);
	}

	@After
	public void tearDown() {
		driver.quit();
		appiumDriverLocalService.stop();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testLogin_Swipe_Scroll() throws InterruptedException {

		waitForElementClickable(By.id(USERNAME), 10);
		driver(By.id(USERNAME)).sendKeys("vodqa@gmail.com");
		driver(By.id(PASSWORD)).sendKeys("Hello12345678");
		driver(By.name("Sign in")).click();
		waitForElementClickable(By.id("org.wordpress.android:id/switch_site"), 30);

		// Swipe Method_1
		Dimension size = driver.manage().window().getSize();
		int startx = (int) (size.width * 0.9);
		int endx = (int) (size.width * 0.20);
		int starty = size.height / 2;
		driver.swipe(startx, starty, endx, starty, 1000);
        
		// Scroll UP
		waitForElementClickable(By.id(Image_Scrollable), 10);
		scrollDirection(Image_Scrollable, SwipeElementDirection.UP);

		driver(By.id("org.wordpress.android:id/image_featured")).click();
		waitForElementClickable(By.id("org.wordpress.android:id/menu_browse"), 10);

		driver(By.id("org.wordpress.android:id/menu_browse")).click();
	}

	public WebElement driver(By by) {
		return driver.findElement(by);

	}

	public void waitForElementClickable(By by, int waitTime) {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.elementToBeClickable(by));

	}

	public void scrollDirection(String Id, SwipeElementDirection arg) {
		MobileElement e = (MobileElement) driver.findElementById(Id);
		e.swipe(arg, 1000);
	}

}

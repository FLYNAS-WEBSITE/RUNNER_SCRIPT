package com.wegoflyadeal.browsers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowsersFactory {

	private static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();

	public static WebDriver getBrowser(String browserName) {
		WebDriver driver = null;

		switch (browserName) {
		case "Chrome":
			driver = drivers.get("Chrome");
			if (driver == null) {
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.setPageLoadStrategy(PageLoadStrategy.NONE); 
				options.addArguments("start-maximized"); 
				options.setExperimentalOption("useAutomationExtension", false);
				options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
				options.addArguments("--no-sandbox"); 
				options.addArguments("--disable-infobars"); 
				options.addArguments("--disable-dev-shm-usage"); 
				options.addArguments("--disable-browser-side-navigation"); 
				options.addArguments("--disable-gpu"); 
			    options.addArguments("--incognito", "--disable-blink-features=AutomationControlled");
				driver = new ChromeDriver(options);
				drivers.put("Chrome", driver);
			}
			break;
		}
		return driver;
	}

	public static void quitDriver(String browserName) {
		try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
		for (String key : drivers.keySet()) {
			if(browserName.equalsIgnoreCase(key))
				drivers.get(key).quit();
			break;
		}
	}


}

package cn.unionstech.Utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class ChromeDriverUtil {

    public static WebDriver prepareChromeWebDriver() {

//        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver webDriver = new ChromeDriver(chromeOptions);
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return webDriver;

    }
}

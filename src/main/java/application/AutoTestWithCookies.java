package application;

import Utils.BypassLoginWithCookies;
import org.openqa.selenium.*;


import static Utils.ChromeDriverUtil.prepareChromeWebDriver;

public class AutoTestWithCookies {

    public static void main(String[] args) throws InterruptedException {

        WebDriver webDriver = prepareChromeWebDriver();
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        login.BypassLoginWithCookies(webDriver);

        webDriver.quit();
    }


}

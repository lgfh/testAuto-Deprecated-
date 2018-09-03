package Utils;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class BypassLoginWithCookies {
    public void BypassLoginWithCookies(WebDriver webDriver) throws InterruptedException {
        webDriver.get("https://www.xrcloud.net/ruicloud/login");
        webDriver.manage().deleteAllCookies();


        Cookie cookie = new Cookie("XJSESSIONID", "c00a5ac5-8320-441c-b678-1e1c30b8310b");
        webDriver.manage().addCookie(cookie);
        webDriver.get("https://www.xrcloud.net/ruicloud/host");
        webDriver.manage().window().maximize();
        Thread.sleep(2000);
    }
}

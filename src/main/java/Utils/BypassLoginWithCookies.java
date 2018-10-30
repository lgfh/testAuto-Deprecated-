package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class BypassLoginWithCookies {
    public void bypassLoginWithCookies(WebDriver webDriver) throws InterruptedException {
//        webDriver = prepareChromeWebDriver();
        webDriver.get("https://zschj.xrcloud.net/ruicloud/login");
        webDriver.manage().window().maximize();

        webDriver.manage().deleteAllCookies();
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[2]/div[2]/form/div[1]/input")).sendKeys("18686693412");
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[2]/div[2]/form/div[2]/input")).sendKeys("wangxinyu123");
        Thread.sleep(6000);

        String[] s1 = webDriver.manage().getCookies().toString().split(";");
        s1 = s1[0].split("=");
        String result = s1[1];

        Cookie cookie = new Cookie("XJSESSIONID", result);
        webDriver.manage().addCookie(cookie);
        webDriver.get("https://zschj.xrcloud.net/ruicloud/host");
        Thread.sleep(2000);

    }
}

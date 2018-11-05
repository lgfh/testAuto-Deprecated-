package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class BypassLoginWithCookies {
    static String BASE_URL = "https://www.xrcloud.net/ruicloud/";
//    static String BASE_URL = "https://zschj.xrcloud.net/ruicloud/";

    public void bypassLoginWithCookies(WebDriver webDriver) throws InterruptedException {
        webDriver.get(BASE_URL + "login");
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
        Thread.sleep(1000);

    }

    public String getCurrentURL() {
        return BASE_URL;
    }
}

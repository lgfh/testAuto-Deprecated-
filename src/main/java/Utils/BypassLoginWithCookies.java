package Utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;


public class BypassLoginWithCookies {
    //    static String BASE_URL = "https://www.xrcloud.net/ruicloud/";
    static String BASE_URL = "https://zschj.xrcloud.net/ruicloud/";

    private final static Logger logger = Logger.getLogger(BypassLoginWithCookies.class);

    public void bypassLoginWithCookies(WebDriver webDriver) throws InterruptedException {
        webDriver.get(BASE_URL + "login");
        webDriver.manage().window().maximize();
        webDriver.manage().deleteAllCookies();

        //获取cookie.txt中的过期时间戳，和当前时间比较
        String resultSet[] = ReadCookieFromFile.getTimeFromTxt("cookie.txt");
        if (resultSet[0] != null && resultSet[1] != null) {
            long expired = Long.parseLong(resultSet[0]);
            long now = System.currentTimeMillis();
            String result = resultSet[1];
            if (expired > now) {
                Cookie cookie = new Cookie("XJSESSIONID", result);
                webDriver.manage().addCookie(cookie);
                logger.info("Cookie可用，跳过登录");
                Thread.sleep(1000);
                return;
            } else {
                loginAndWriteCookieToTxt(webDriver, now);
            }

        } else {
            loginAndWriteCookieToTxt(webDriver, System.currentTimeMillis() + 1800000);
        }

    }

    public void loginAndWriteCookieToTxt(WebDriver webDriver, long now) throws InterruptedException {
        String result;
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[2]/div[2]/form/div[1]/input")).sendKeys("18686693412");
        webDriver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[2]/div[2]/form/div[2]/input")).sendKeys("wangxinyu123");
        Thread.sleep(6000);

        String[] s1 = webDriver.manage().getCookies().toString().split(";");
        s1 = s1[0].split("=");
        result = s1[1];
        Cookie cookie = new Cookie("XJSESSIONID", result);
        webDriver.manage().addCookie(cookie);
        WriteCookieToFile.writeCookieToFile(result, (now + 1800000));
        logger.info("Cookie需要更新，已经更新");
    }

    public String getCurrentURL() {
        return BASE_URL;
    }
}

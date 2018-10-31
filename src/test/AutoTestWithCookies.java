import Utils.BypassLoginWithCookies;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import static Utils.ChromeDriverUtil.prepareChromeWebDriver;

public class AutoTestWithCookies {

    @Test
    public AutoTestWithCookies() throws InterruptedException {

        WebDriver webDriver = prepareChromeWebDriver();
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        login.bypassLoginWithCookies(webDriver);

        webDriver.quit();
    }


}

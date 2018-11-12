import cn.unionstech.Utils.BypassLoginWithCookies;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.text.ParseException;

import static cn.unionstech.Utils.ChromeDriverUtil.prepareChromeWebDriver;

public class AutoTestWithCookies {

    @Test
    public AutoTestWithCookies() throws InterruptedException, ParseException {

        WebDriver webDriver = prepareChromeWebDriver();
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        login.bypassLoginWithCookies(webDriver);
        webDriver.get(login.getCurrentURL() + "host");

        webDriver.quit();
    }


}

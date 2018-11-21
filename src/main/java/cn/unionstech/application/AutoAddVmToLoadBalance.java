package cn.unionstech.application;

import cn.unionstech.Utils.BypassLoginWithCookies;
import cn.unionstech.Utils.ChromeDriverUtil;
import cn.unionstech.Utils.JsonUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author WXY
 * @version 创建时间：2018/11/7
 */
@Service
public class AutoAddVmToLoadBalance {
    private final static Logger logger = Logger.getLogger(AutoAddVmToLoadBalance.class);

    @Autowired
    ChromeDriverUtil chromeDriverUtil;


    public String autoAddVmToLoadBalance(String zone) {
        WebDriver webDriver = chromeDriverUtil.prepareChromeWebDriver();
        BypassLoginWithCookies login = new BypassLoginWithCookies();
        Properties properties = new Properties();
        Actions action = new Actions(webDriver);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/xpath.properties"));
            properties.load(bufferedReader);
            login.bypassLoginWithCookies(webDriver);

            //负载均衡创建页面
            webDriver.get(login.getCurrentURL() + "balance");

            if (!(login.getCurrentURL().contains("zschj"))) {
                //区域选择
                action.moveToElement(webDriver.findElement(By.xpath(properties.getProperty("DB页面下拉选区")))).perform();
                Thread.sleep(1000);
                webDriver.findElement(By.xpath(properties.getProperty("DB页面下拉选区" + zone))).click();
            }

            if (!(webDriver.findElement(By.xpath(properties.getProperty("负载均衡器勾选框")))).isSelected()) {
                (webDriver.findElement(By.xpath(properties.getProperty("负载均衡器勾选框")))).click();
            }
            //点绑定虚拟机
            webDriver.findElement(By.xpath(properties.getProperty("负载均衡绑定虚拟机按钮"))).click();
            //选择要加入的主机
            webDriver.findElement(By.xpath(properties.getProperty("向LB添加主机"))).click();
            List<WebElement> VmList = webDriver.findElements(By.xpath(properties.getProperty("向LB添加主机列表")));
            if (VmList.size() >= 1) {
                for (WebElement e : VmList) {
                    logger.info("将主机" + e.getText() + "添加到负载均衡中");
                    e.click();
                }
            } else {
                logger.info("需要添加的主机数量不足，需要创建可以加入负载的主机");
            }

            webDriver.findElement(By.xpath(properties.getProperty("向LB添加主机-确认绑定"))).click();
            Thread.sleep(5000);
            return JsonUtil.getJSONString(0, "向负载添加主机成功");
        } catch (IOException e) {
            logger.error(e.getMessage());
            return JsonUtil.getJSONString(1, "xpath文件读取失败");
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            return JsonUtil.getJSONString(1, "向负载添加主机失败");
        } finally {
            webDriver.quit();
        }
    }
}

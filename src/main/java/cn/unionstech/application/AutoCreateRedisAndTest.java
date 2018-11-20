package cn.unionstech.application;

import cn.unionstech.Utils.BypassLoginWithCookies;
import cn.unionstech.Utils.ChromeDriverUtil;
import cn.unionstech.Utils.JsonUtil;
import cn.unionstech.Utils.RedisUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
@Service
public class AutoCreateRedisAndTest {
    private final static Logger logger = Logger.getLogger(AutoCreateRedisAndTest.class);
    @Autowired
    ChromeDriverUtil chromeDriverUtil;

//    public static void main(String[] args) {
//
//        WebDriver webDriver = chromeDriverUtil.prepareChromeWebDriver();
//        BypassLoginWithCookies login = new BypassLoginWithCookies();
//        try {
//            autoCreateRedisAndTest(webDriver, login);
//
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        } finally {
//            webDriver.quit();
//        }
//
//    }

    public String autoCreateRedisAndTest(String zone) {

        WebDriver webDriver = null;

        try {
            webDriver = chromeDriverUtil.prepareChromeWebDriver();
            BypassLoginWithCookies login = new BypassLoginWithCookies();
            login.bypassLoginWithCookies(webDriver);
            Properties properties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/xpath.properties"));
            properties.load(bufferedReader);
            webDriver.get(login.getCurrentURL() + "buy/bdata");

            if (!(login.getCurrentURL().contains("zschj"))) {
                //区域选择
                webDriver.findElement(By.xpath(properties.getProperty("DB购买页面" + zone))).click();
            }
            //实时计费
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[2]/div[1]/div[2]")).click();
            //选择redis镜像，此处为redis随机镜像
            Actions action = new Actions(webDriver);
            action.moveToElement(webDriver.findElement(By.xpath(properties.getProperty("DB购买页面Redis镜像")))).perform();
            Thread.sleep(500);
            List<WebElement> RedisDBTemplateList = webDriver.findElements(By.xpath(properties.getProperty("DB购买页面Redis镜像下拉列表")));
            Random random = new Random();
            int num = random.nextInt(RedisDBTemplateList.size());
            RedisDBTemplateList.get(num).click();
            logger.info("using DB template：" + RedisDBTemplateList.get(num).getText());
//        webDriver.findElement(By.xpath(String.format("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[5]/div[2]/ul/li[%d]", num))).click();
//        webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[1]/div[2]/ul/li/span")).click();
//        logger.info("using DB template：" + webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[1]/div[2]/ul/li/span")).getText());

            //点 2核
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[2]")).click();
            //点 4G
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[3]/div/div[2]/div[2]")).click();

            webDriver.findElement(By.xpath(properties.getProperty("DB购买页面选择VPC"))).click();
            Thread.sleep(500);
            List<WebElement> VPCList = webDriver.findElements(By.xpath(properties.getProperty("DB购买页面选择VPC下拉列表")));
            for (WebElement e : VPCList) {
                if (e.getText().contains("new"))
                    e.click();
            }

            //默认勾选ip，去掉磁盘
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[3]/div[1]/div[1]/div/img")).click();

            //数据库名称
            String DBName = "auto-db-redis" + num;
            String passwd = "Yrxt@123";
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[4]/div[1]/div/div[2]/input")).sendKeys(DBName);

            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[4]/div[3]/div/div[2]/input")).sendKeys(passwd);

            //点 立即购买
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[5]/div/button[2]/span")).click();
            //点 支付
            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div[2]/button/span")).click();
            //确认账户余额的勾选
            if (!(webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div[2]/div[1]/div/label[1]/span[1]/input")).isSelected())) {
                webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div[2]/div[1]/div/label[1]/span[1]/input")).click();
            }

            //点确认支付
            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div[2]/div[3]/button/span")).click();
            Thread.sleep(1000);

            webDriver.get(login.getCurrentURL() + "cloudDatabase");
            if (!(login.getCurrentURL().contains("zschj"))) {
                //区域选择
                action.moveToElement(webDriver.findElement(By.xpath(properties.getProperty("DB页面下拉选区")))).perform();
                Thread.sleep(1000);
                webDriver.findElement(By.xpath(properties.getProperty("DB页面下拉选区" + zone))).click();
            }
            Thread.sleep(60000);
            webDriver.navigate().refresh();
            String ip = null;
            try {
                for (int i = 0; i < 4; i++) {
                    ip = webDriver.findElement(By.xpath(properties.getProperty("DB页面第一条IP地址"))).getText();
                    if (ip != null)
                        break;
                    Thread.sleep(5000);
                }
                ip = webDriver.findElement(By.xpath(properties.getProperty("DB页面IP地址"))).getText();
                logger.info("The public ip is :" + ip);
            } catch (Exception e) {
                logger.info("failed to get DB public IP");
            }
//        String ip = webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div/div[2]/table/tbody/tr/td[5]/div/div/span[1]")).getText();
//        if (ip == null) {
//            logger.info("create db unfinished");
//        } else {
//        logger.info("The public ip is :" + ip);
//        }
            Thread.sleep(50000);
            String RedisConnectionResult = RedisUtil.getRedisConnectionResult(ip, passwd);
            if (RedisConnectionResult != null) {
                if (RedisConnectionResult.equals("PONG")) {
                    logger.info("Redis connect success");
                    return JsonUtil.getJSONString(0, "Redis connect success");
                } else {
                    logger.info("Redis connect failed");
                    return JsonUtil.getJSONString(1, "Redis connect failed");
                }
            } else {
                logger.info("cant get redis connnection result");
                return JsonUtil.getJSONString(1, "cant get redis connnection result");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtil.getJSONString(1, "Exception");
        } finally {
            webDriver.quit();
        }
    }
}

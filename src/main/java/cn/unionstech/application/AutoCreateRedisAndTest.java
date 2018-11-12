package cn.unionstech.application;

import cn.unionstech.Utils.BypassLoginWithCookies;
import cn.unionstech.Utils.ChromeDriverUtil;
import cn.unionstech.Utils.RedisUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class AutoCreateRedisAndTest {
    private final static Logger logger = Logger.getLogger(AutoCreateRedisAndTest.class);

    public static void main(String[] args) {

        WebDriver webDriver = ChromeDriverUtil.prepareChromeWebDriver();
        BypassLoginWithCookies login = new BypassLoginWithCookies();
        try {
            autoCreateRedisAndTest(webDriver, login);

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            webDriver.quit();
        }

    }

    public static void autoCreateRedisAndTest(WebDriver webDriver, BypassLoginWithCookies login) throws InterruptedException, IOException {

        login.bypassLoginWithCookies(webDriver);
        Properties properties = new Properties();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/xpath.properties"));
        properties.load(bufferedReader);
        webDriver.get(login.getCurrentURL() + "buy/bdata");

        if (!(login.getCurrentURL().contains("zschj"))) {
            //区域选择
            webDriver.findElement(By.xpath(properties.getProperty("DB购买页面华东一区"))).click();
        }
        //实时计费
        webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[2]/div[1]/div[2]")).click();
        //选择redis镜像，此处为mysql随机镜像
        Actions action = new Actions(webDriver);
        action.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[1]/div[1]/div"))).perform();
        Thread.sleep(1000);
//            List<WebElement> MysqlDBTemplateList = webDriver.findElements(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[1]/div[2]/ul/li"));
//            Random random = new Random();
//            int num = random.nextInt(MysqlDBTemplateList.size()) + 1;
//            webDriver.findElement(By.xpath(String.format("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[5]/div[2]/ul/li[%d]",num))).click();
        webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[1]/div[2]/ul/li/span")).click();
        logger.info("using DB template：" + webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[1]/div[2]/ul/li/span")).getText());
        //点 2核
        webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[2]")).click();
        //点 4G
        webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[3]/div/div[2]/div[2]")).click();
        //默认勾选ip，去掉磁盘
        webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[3]/div[1]/div[1]/div/img")).click();

        //数据库名称
        String DBName = "auto-db-redis";
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
            webDriver.findElement(By.xpath(properties.getProperty("DB页面下拉选区华东一区"))).click();
        }
        Thread.sleep(40000);
        webDriver.navigate().refresh();
        String ip = webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div/div[2]/table/tbody/tr/td[5]/div/div/span[1]")).getText();
//        if (ip == null) {
//            logger.info("create db unfinished");
//        } else {
            logger.info("The public ip is :" + ip);
//        }
        Thread.sleep(50000);
        String RedisConnectionResult = RedisUtil.getRedisConnectionResult(ip, passwd);
        if (RedisConnectionResult != null) {
            if (RedisConnectionResult.equals("PONG")) {
                logger.info("Redis connect success");
            } else {
                logger.info("Redis connect failed");
            }
        } else {
            logger.info("cant get redis connnection result");
        }
    }
}

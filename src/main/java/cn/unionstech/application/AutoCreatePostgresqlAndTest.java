package cn.unionstech.application;

import cn.unionstech.Utils.BypassLoginWithCookies;
import cn.unionstech.Utils.ChromeDriverUtil;
import cn.unionstech.Utils.MysqlUtil;
import cn.unionstech.Utils.PostgresqlUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class AutoCreatePostgresqlAndTest {
    private final static Logger logger = Logger.getLogger(AutoCreatePostgresqlAndTest.class);

    static Connection conn = null;

    public static void main(String[] args) {
        autoCreatePostgresqlTest();
    }

    public static void autoCreatePostgresqlTest() {

        //准备chrome的驱动
        WebDriver webDriver = ChromeDriverUtil.prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();
        try {
            Properties properties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/xpath.properties"));
            properties.load(bufferedReader);
            //利用cookies跳过登陆，进入数据库购买的界面
            login.bypassLoginWithCookies(webDriver);

            webDriver.get(login.getCurrentURL() + "buy/bdata");

            if (!(login.getCurrentURL().contains("zschj"))) {
                //区域选择
                webDriver.findElement(By.xpath(properties.getProperty("DB购买页面华东一区"))).click();
            }

            //实时计费
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[2]/div[1]/div[2]")).click();

            //选择postgresql镜像，此处为随机镜像
            Actions action = new Actions(webDriver);
            action.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[3]/div[1]/div"))).perform();
            Thread.sleep(1000);
            List<WebElement> MysqlDBTemplateList = webDriver.findElements(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[3]/div[2]/ul/li"));
            Random random = new Random();
//            int num = random.nextInt(MysqlDBTemplateList.size()) + 1;
            int num = 3;
            webDriver.findElement(By.xpath(String.format("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[3]/div[2]/ul/li[%d]", num))).click();
            logger.info("using DB template：" + webDriver.findElement(By.xpath(String.format("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[3]/div[2]/ul/li[%d]", num))).getText());

            //点 2核
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[2]")).click();
            //点 4G
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[3]/div/div[2]/div[2]")).click();
            //默认勾选ip，去掉磁盘
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[3]/div[1]/div[1]/div/img")).click();

            //数据库名称
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[4]/div[1]/div/div[2]/input")).sendKeys("auto-db-" + num);

            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[4]/div[3]/div/div[2]/input")).sendKeys("Yrxt@123");

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
            Thread.sleep(80000);
            webDriver.navigate().refresh();
            String ip = webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div/div[2]/table/tbody/tr/td[5]/div/div/span[1]")).getText();
//            if (ip == null) {
//                logger.info("create db unfinished");
//            } else {
            logger.info("The public ip is :" + ip);
//            }
            String passwd = "Yrxt@123";
            Thread.sleep(60000);
            conn = PostgresqlUtil.getPostgresqlConnectionAndTest(ip, passwd);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            MysqlUtil.closeConnection(conn);
            webDriver.quit();
        }
    }
}

package application;

import Utils.BypassLoginWithCookies;
import Utils.MongoUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import static Utils.ChromeDriverUtil.prepareChromeWebDriver;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class AutoCreateMongoDbAndTest {
    private final static Logger logger = Logger.getLogger(AutoCreateMongoDbAndTest.class);

    public static void main(String[] args) {
        //准备chrome的驱动
        WebDriver webDriver = prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        try {
            //利用cookies跳过登陆，进入数据库购买的界面
            login.bypassLoginWithCookies(webDriver);

            webDriver.get(login.getCurrentURL() + "buy/bdata");

            //实时计费
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[2]/div[1]/div[2]")).click();

            //选择mysql镜像，此处为mysql随机镜像
            Actions action = new Actions(webDriver);
            action.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[4]/div[1]/div"))).perform();
            Thread.sleep(1000);
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[4]/div[2]/ul/li")).click();
            logger.info("using DB template：" + webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[4]/div[2]/ul/li")).getText());

            //点 2核
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[2]")).click();
            //点 4G
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[3]/div/div[2]/div[2]")).click();
            //默认勾选ip，去掉磁盘
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[3]/div[1]/div[1]/div/img")).click();

            //数据库名称
            String DBName = "auto-db-mongo";
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
            Thread.sleep(50000);
            webDriver.navigate().refresh();
            String ip = webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div/div[2]/table/tbody/tr/td[5]/div/div/span[1]")).getText();
            if (ip == null) {
                logger.info("create db unfinished");
            } else {
                logger.info("The public ip is :" + ip);
            }
            Thread.sleep(40000);
            String MongoConnectionResult = MongoUtil.getMongoConnectionResult(ip);
            if (MongoConnectionResult == null) {
                logger.info("cant connect mongodb");
            } else if (MongoConnectionResult.equals(ip + ":27017")) {
                logger.info("connect mongodb success");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            webDriver.quit();
        }
    }
}

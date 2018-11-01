package application;

import Utils.BypassLoginWithCookies;
import Utils.ChromeDriverUtil;
import Utils.RedisUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

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

    public static void autoCreateRedisAndTest(WebDriver webDriver, BypassLoginWithCookies login) throws InterruptedException {
        login.bypassLoginWithCookies(webDriver);
        webDriver.get(login.getCurrentURL() + "buy/bdata");
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
        Thread.sleep(40000);
        webDriver.navigate().refresh();
        String ip = webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div/div[2]/table/tbody/tr/td[5]/div/div/span[1]")).getText();
        if (ip == null) {
            logger.info("create db unfinished");
        } else {
            logger.info("The public ip is :" + ip);
        }
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

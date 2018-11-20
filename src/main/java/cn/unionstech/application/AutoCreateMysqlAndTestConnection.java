package cn.unionstech.application;

import cn.unionstech.Utils.BypassLoginWithCookies;
import cn.unionstech.Utils.ChromeDriverUtil;
import cn.unionstech.Utils.JsonUtil;
import cn.unionstech.Utils.MysqlUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * @author WXY
 * @version 创建时间：2018/10/31
 */

@Service
public class AutoCreateMysqlAndTestConnection {
    private final static Logger logger = Logger.getLogger(AutoCreateMysqlAndTestConnection.class);

    @Autowired
    ChromeDriverUtil chromeDriverUtil;

    static Connection conn = null;

    public static void main(String[] args) {
//        autoCreateMysqlAndTestConnection("华东一区");
    }

    public String autoCreateMysqlAndTestConnection(String zone) {
        //准备chrome的驱动
        WebDriver webDriver = chromeDriverUtil.prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();
        Properties properties = new Properties();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/xpath.properties"));
            properties.load(bufferedReader);
            //利用cookies跳过登陆，进入数据库购买的界面
            login.bypassLoginWithCookies(webDriver);

            webDriver.get(login.getCurrentURL() + "buy/bdata");

            if (!(login.getCurrentURL().contains("zschj"))) {
                //区域选择
                webDriver.findElement(By.xpath(properties.getProperty("DB购买页面" + zone))).click();
            }

            //实时计费
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[2]/div[1]/div[2]")).click();

            //选择mysql镜像，此处为mysql随机镜像
            Actions action = new Actions(webDriver);
            action.moveToElement(webDriver.findElement(By.xpath(properties.getProperty("DB购买页面Mysql镜像")))).perform();
            Thread.sleep(1000);
            List<WebElement> MysqlDBTemplateList = webDriver.findElements(By.xpath(properties.getProperty("DB购买页面Mysql镜像下拉列表")));
            Random random = new Random();
            int num = random.nextInt(MysqlDBTemplateList.size()) + 1;
//            int num = 3;
            MysqlDBTemplateList.get(num).click();
            logger.info("using DB template：" + MysqlDBTemplateList.get(num).getText());
//            webDriver.findElement(By.xpath(String.format("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[5]/div[2]/ul/li[%d]", num))).click();
//            logger.info("using DB template：" + webDriver.findElement(By.xpath(String.format("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[1]/div/div[2]/div/div[5]/div[2]/ul/li[%d]", num))).getText());

            //点 2核
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[2]")).click();
            //点 4G
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[3]/div/div[2]/div[2]")).click();

            //筛选VPC
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
                webDriver.findElement(By.xpath(properties.getProperty("DB页面下拉选区" + zone))).click();
            }
            Thread.sleep(100000);
            webDriver.navigate().refresh();
            String ip = null;
            try {
                for (int i = 0; i < 4; i++) {
                    ip = webDriver.findElement(By.xpath(properties.getProperty("DB页面第一条IP地址"))).getText();
                    if (ip != null)
                        break;
                    Thread.sleep(5000);
                }
                logger.info("The public ip is :" + ip);
            } catch (Exception e) {
                logger.info("failed to get DB public IP");
            }
            String passwd = "Yrxt@123";
            Thread.sleep(100000);
            conn = MysqlUtil.getConnectionAndTest(ip, passwd);
            if (conn != null) {
                return JsonUtil.getJSONString(0, "Mysql数据库连接测试成功,zone是：" + zone + ",ip是：" + ip);
            } else {
                return JsonUtil.getJSONString(1, "Mysql数据库连接测试失败,zone是：" + zone);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtil.getJSONString(1, "Mysql数据库创建异常,zone是：" + zone);
        } finally {
            MysqlUtil.closeConnection(conn);
            webDriver.quit();
        }
    }
}
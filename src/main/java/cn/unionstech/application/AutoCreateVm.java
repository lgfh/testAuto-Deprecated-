package cn.unionstech.application;


/*
账号的companyid=150899716136
 */

import cn.unionstech.Utils.BypassLoginWithCookies;
import cn.unionstech.Utils.ChromeDriverUtil;
import cn.unionstech.Utils.JsonUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class AutoCreateVm {
    private final static Logger logger = Logger.getLogger(AutoCreateVm.class);

    public static void main(String[] args) {
        autoCreateVM();
    }

    public static String autoCreateVM() {

        //准备chrome的驱动
        WebDriver webDriver = ChromeDriverUtil.prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        try {
            Properties properties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/xpath.properties"));
            properties.load(bufferedReader);
            //利用cookies跳过登陆，进入host的界面
            login.bypassLoginWithCookies(webDriver);
            Actions action = new Actions(webDriver);

            webDriver.get(login.getCurrentURL() + "buy");
            //创建选择自定义配置
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[1]/div[2]")).click();
            if (!(login.getCurrentURL().contains("zschj"))) {
                //区域选择
                webDriver.findElement(By.xpath(properties.getProperty("VM购买页面-北方一区"))).click();
            }
            //实时计费
            webDriver.findElement(By.xpath(properties.getProperty("VM购买页面-实时计费"))).click();

            //选择镜像，此处为选择指定操作系统的某一个镜像
            action.moveToElement(webDriver.findElement(By.xpath(properties.getProperty("Linux镜像")))).perform();
            Thread.sleep(1000);

            List<WebElement> VmTemplateList = webDriver.findElements(By.xpath(properties.getProperty("Linux镜像下拉列表")));
            Random random = new Random();
            int num = random.nextInt(VmTemplateList.size()) + 1;
            logger.info("使用模板： " +
                    webDriver.findElement(By.xpath(String.format
                            (properties.getProperty("Linux镜像下拉列表") + "[%d]", num))).getText());
            webDriver.findElement(By.xpath(String.format(properties.getProperty("Linux镜像下拉列表") + "[%d]", num))).click();

            //点 2核
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[4]/div[1]/div[4]/div/div[2]/div[2]")).click();
            //点 4G
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[4]/div[1]/div[5]/div/div[2]/div[2]")).click();
            //创建公网负载均衡子网的主机

            webDriver.findElement(By.xpath(properties.getProperty("创建VM选择子网"))).click();
            Thread.sleep(1000);
            List<WebElement> tierList = webDriver.findElements(By.xpath(properties.getProperty("创建VM选择子网下拉列表")));
            for (WebElement e : tierList) {
                if (e.getText().contains("lb")) {
                    logger.info("使用子网" + e.getText() + "创建VM");
                    e.click();
                }
            }

            //默认勾选ip，去掉磁盘
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[4]/div[3]/div[1]/div[1]/div/img")).click();
            //点 自定义配置
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[5]/div[1]/div/div[3]")).click();

            //主机名称
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[5]/div[2]/div[1]/div/div[2]/input")).sendKeys("auto-vm" + num);

            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[5]/div[2]/div[3]/div/div[2]/input")).sendKeys("Yrxt@123");

            //点 立即购买
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[6]/div/button[2]/span")).click();
            //点 支付
            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div[2]/button/span")).click();

            //确认账户余额的勾选
            if (!(webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div[2]/div[1]/div/label[1]/span[1]/input")).isSelected())) {
                webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div[2]/div[1]/div/label[1]/span[1]/input")).click();
            }

            //点确认支付
            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div[2]/div[3]/button")).click();
            logger.info("创建VM成功");
            Thread.sleep(1000);
            return JsonUtil.getJSONString(0, "创建VM成功");
        } catch (IOException e) {
            logger.error("读取xpath文件异常");
            return JsonUtil.getJSONString(0, "读取xpath文件异常,创建VM失败");
        } catch (Exception e) {
            logger.error("创建VM失败" + e.getMessage());
            return JsonUtil.getJSONString(0, "创建VM失败");
        } finally {
            webDriver.quit();
        }
    }
}

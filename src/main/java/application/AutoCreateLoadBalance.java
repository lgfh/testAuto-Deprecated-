package application;

import Utils.BypassLoginWithCookies;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static Utils.ChromeDriverUtil.prepareChromeWebDriver;

/**
 * @author WXY
 * @version 创建时间：2018/11/7
 */
public class AutoCreateLoadBalance {
    private final static Logger logger = Logger.getLogger(AutoCreateLoadBalance.class);

    public static void main(String[] args) {
        autoCreateLoadBalance();
    }

    public static void autoCreateLoadBalance() {
        WebDriver webDriver = prepareChromeWebDriver();
        BypassLoginWithCookies login = new BypassLoginWithCookies();
        Properties properties = new Properties();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/xpath.properties"));
            properties.load(bufferedReader);
            login.bypassLoginWithCookies(webDriver);

            //负载均衡创建页面
            webDriver.get(login.getCurrentURL() + "balance");
            //点创建负载均衡
            webDriver.findElement(By.xpath(properties.getProperty("负载均衡页面创建负载均衡按钮"))).click();
            //填写负载均衡相关信息
            webDriver.findElement(By.xpath(properties.getProperty("创建负载均衡弹出框名称"))).sendKeys("auto-LB");
            //公网负载部分
            if (webDriver.findElement(By.xpath(properties.getProperty("创建负载均衡类型-公网"))).isSelected()) {
//                webDriver.findElement(By.xpath(properties.getProperty("创建负载均衡类型-公网"))).click();

                //选择VPC
                webDriver.findElement(By.xpath(properties.getProperty("创建负载均衡选择所属VPC"))).click();
                Thread.sleep(1000);

                List<WebElement> VPCList = webDriver.findElements(By.xpath(properties.getProperty("负载均衡所属VPC下拉列表")));
                if (VPCList.size() != 0) {
                    if (VPCList.size() == 1) {
                        logger.info("选择VPC：" + webDriver.findElement(By.xpath(String.format(properties.getProperty("负载均衡所属VPC下拉列表只有一个VPC")))).getText());
                        webDriver.findElement(By.xpath(properties.getProperty("负载均衡所属VPC下拉列表只有一个VPC"))).click();
                    } else {
                        Random random = new Random();
                        int num = random.nextInt(VPCList.size()) + 1;
                        logger.info("选择VPC：" + webDriver.findElement(By.xpath(String.format(properties.getProperty("负载均衡所属VPC下拉列表有多个VPC") + "[%d]", num))).getText());
                        webDriver.findElement(By.xpath(String.format(properties.getProperty("负载均衡所属VPC下拉列表有多个VPC") + "[%d]", num))).click();
                    }
                } else {
                    logger.warn("没有VPC,需要先创建VPC");
                    webDriver.quit();
                }
                //选择公网ip
                webDriver.findElement(By.xpath(properties.getProperty("负载均衡选择公网ip"))).click();
                Thread.sleep(1000);

                List<WebElement> PublicIPList = webDriver.findElements(By.xpath(properties.getProperty("负载均衡选择公网ip下拉列表")));
                if (PublicIPList.size() != 0) {
                    Random random = new Random();
                    int num = random.nextInt(PublicIPList.size()) + 1;
                    logger.info("选择公网ip：" + webDriver.findElement(By.xpath(String.format(properties.getProperty("负载均衡选择公网ip下拉列表") + "[%d]", num))).getText());
                    webDriver.findElement(By.xpath(String.format(properties.getProperty("负载均衡选择公网ip下拉列表") + "[%d]", num))).click();
                    Thread.sleep(1000);
                } else {
                    logger.warn("没有公网ip,需要先创建");
                    webDriver.quit();
                }

                //选择公网负载子网
                webDriver.findElement(By.xpath(properties.getProperty("负载均衡选择子网"))).click();
                Thread.sleep(1000);
                List<WebElement> PublicTierList = webDriver.findElements(By.xpath(properties.getProperty("负载均衡选择子网下拉列表")));
                if (PublicTierList.size() != 0) {
                    logger.info("选择子网： " + webDriver.findElement(By.xpath(properties.getProperty("负载均衡选择子网下拉列表"))).getText());
                    webDriver.findElement(By.xpath(properties.getProperty("负载均衡选择子网下拉列表"))).click();
                } else {
                    logger.warn("没有公网负载子网,需要先创建");
                    webDriver.quit();
                }

                //点下一步
                webDriver.findElement(By.xpath(properties.getProperty("创建负载均衡下一步"))).click();
                //填写内网负载端口
                webDriver.findElement(By.xpath(properties.getProperty("负载均衡的内网端口input"))).sendKeys("22");
                //填写公网负载端口
                webDriver.findElement(By.xpath(properties.getProperty("负载均衡的外网端口input"))).sendKeys("22");
                //选择公网负载算法
                webDriver.findElement(By.xpath(properties.getProperty("负载均衡算法选择"))).click();
                Thread.sleep(1000);
                webDriver.findElement(By.xpath(properties.getProperty("负载均衡算法-轮询算法"))).click();

                //点完成
                webDriver.findElement(By.xpath(properties.getProperty("负载均衡创建完成按钮"))).click();
                Thread.sleep(2000);
            } else {

                //内网负载均衡
            }


        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error("读取文件异常");
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            webDriver.quit();
        }
    }
}

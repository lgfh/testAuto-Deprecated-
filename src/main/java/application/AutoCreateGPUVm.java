package application;


/*
账号的companyid=150899716136
 */

import Utils.BypassLoginWithCookies;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Random;

import static Utils.ChromeDriverUtil.prepareChromeWebDriver;

public class AutoCreateGPUVm {

    private final static Logger logger = Logger.getLogger(AutoCreateGPUVm.class);

    public static void main(String[] args) {

        //准备chrome的驱动
        WebDriver webDriver = prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        try {
            //利用cookies跳过登陆，进入host的界面
            login.bypassLoginWithCookies(webDriver);

            webDriver.get(login.getCurrentURL() + "GpuList");
            //创建
            webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/button/span")).click();

            if (!(login.getCurrentURL().contains("zschj"))) {
                //区域选择
                webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[1]/div/div[5]")).click();
            }

            //实时计费
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[2]/div/div[3]")).click();

            //选择镜像，返回所有镜像放入一个list中
            Actions action = new Actions(webDriver);
            action.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[3]/div[2]/div[1]/div"))).perform();
            Thread.sleep(1000);

            List<WebElement> GpuVmTemplateList = webDriver.findElements(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[3]/div[2]/div[2]/ul/li"));
            Random random = new Random();
            int num = random.nextInt(GpuVmTemplateList.size()) + 1;
//            String firstTemplateElement =GpuVmTemplateList.get(0).getText();
            webDriver.findElement(By.xpath(String.format("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[3]/div[2]/div[2]/ul/li[%d]", num))).click();

            //点 自定义设置
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[4]/div[1]/div/div[3]")).click();
            //填写主机名称
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[4]/div[2]/div[1]/div/div[2]/input")).sendKeys("auto-gpu-vm-" + num);
            //填写密码
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[4]/div[2]/div[3]/div/div[2]/input")).sendKeys("Yrxt@123");
            //点立即购买
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[5]/div/button[2]/span")).click();
            //点 支付
            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div[2]/button/span")).click();
            //勾选账户余额
            if (!(webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div[2]/div[1]/div/label[1]/span[1]/input")).isSelected())) {
                webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div[2]/div[1]/div/label[1]/span[1]/input")).click();
            }

            //点 确认支付
            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div[2]/div[3]/button/span")).click();

            Thread.sleep(5000);
            if (webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div/div/h1")).getText() != null)
                logger.info(webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div/div/h1")).getText());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }

    }
}

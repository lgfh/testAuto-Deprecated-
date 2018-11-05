package application;


/*
账号的companyid=150899716136
 */

import Utils.BypassLoginWithCookies;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Random;

import static Utils.ChromeDriverUtil.prepareChromeWebDriver;

public class AutoCreateVm {

    public static void main(String[] args) {

        //准备chrome的驱动
        WebDriver webDriver = prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        try {
            //利用cookies跳过登陆，进入host的界面
            login.bypassLoginWithCookies(webDriver);

            webDriver.get(login.getCurrentURL() + "buy");
            //创建选择自定义配置
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[1]/div[2]")).click();
            //实时计费
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[2]/div/div[2]")).click();

            //选择镜像，此处为选择windows的某一个镜像
            Actions action = new Actions(webDriver);
            action.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[4]/div[1]/div[2]/div/div[2]/div[4]/div[1]/div[1]/div"))).perform();
            Thread.sleep(1000);

            List<WebElement> VmTemplateList = webDriver.findElements(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[4]/div[1]/div[2]/div/div[2]/div[4]/div[1]/div[2]/ul/li"));
            Random random = new Random();
            int num = random.nextInt(VmTemplateList.size()) + 1;
            webDriver.findElement(By.xpath(String.format("//*[@id=\"Pecs\"]/div[2]/div[4]/div[1]/div[2]/div/div[2]/div[4]/div[1]/div[2]/ul/li[%d]", num))).click();

            //点 2核
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[4]/div[1]/div[4]/div/div[2]/div[2]")).click();
            //点 4G
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[4]/div[1]/div[5]/div/div[2]/div[2]")).click();
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

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }

    }
}

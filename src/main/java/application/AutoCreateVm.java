package application;


/*
账号的companyid=150899716136
 */

import Utils.BypassLoginWithCookies;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import static Utils.ChromeDriverUtil.prepareChromeWebDriver;

public class AutoCreateVm {

    public static void main(String[] args) {

        //准备chrome的驱动
        WebDriver webDriver = prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        try {
            //利用cookies跳过登陆，进入host的界面
            login.BypassLoginWithCookies(webDriver);

            //在host页面点击创建主机
            webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div[2]/div[1]/div/div[2]/div/div/div")).click();
            //华东一区
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[1]/div/div[6]")).click();
            //实时计费主机
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[2]/div[1]/div[2]")).click();

            //选择镜像，此处为选择centos的第一个镜像
            Actions action = new Actions(webDriver);
            action.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[3]/div/div[1]/div/div[2]/div[3]/div[2]/div[1]/div"))).perform();
            Thread.sleep(2000);
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[3]/div/div[1]/div/div[2]/div[3]/div[2]/div[2]/ul/li[1]/span")).click();


            //点 立即购买
            webDriver.findElement(By.xpath("//*[@id=\"Pecs\"]/div[2]/div[6]/div/button[2]/span")).click();
            //点 支付
            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[4]/div/div/div[2]/button/span")).click();
            //点 确认支付
            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[4]/div/div[2]/div[3]/button")).click();


            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }

    }
}

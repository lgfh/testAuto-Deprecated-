package application;


/*
账号的companyid=150899716136
 */

import Utils.BypassLoginWithCookies;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import static Utils.ChromeDriverUtil.prepareChromeWebDriver;

public class AutoDestroyVm {

    public static void main(String[] args) {

        //准备chrome的驱动
        WebDriver webDriver = prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        try {
            //利用cookies跳过登陆，进入host的界面
            login.bypassLoginWithCookies(webDriver);

            webDriver.get(login.getCurrentURL() + "host");
            //选中第一台主机，在host页面点击释放公网ip
            Actions action = new Actions(webDriver);
            if (!webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div[2]/div[1]/div/div[1]/div/div/div/div[1]/span[4]")).getText().equals("公网地址:")) {
                webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div[2]/div[1]/div/div[1]/div/div/div/div[1]")).click();
                // 更多操作下拉
                action.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/div/div[1]/button/span"))).perform();
                Thread.sleep(1000);
                //点 释放IP
                webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/div/div[2]/ul/div[2]/div[1]/li")).click();
                webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/div/div[2]/ul/div[2]/div[2]/div/div[2]/div[2]/button[2]/span")).click();
                Thread.sleep(1000);
            }

            //关闭电源
            webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div[2]/div[1]/div/div[1]/div/div/div/div[1]/div/i")).click();
            Thread.sleep(35000);

            //切换关机选项卡
            webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div[1]/div/div/div/div/div[5]")).click();

            //删除主机
            webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div[2]/div[4]/div/div/div/div/div/div[1]")).click();
            action.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/div/div[1]/button/span"))).perform();
            Thread.sleep(1000);
            webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/div/div[2]/ul/li[7]")).click();
            webDriver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/button[2]/span")).click();

//            webDriver.findElement(By.linkText("确定")).click();
//            webDriver.switchTo().alert().accept();

            //清理回收站
            webDriver.get(login.getCurrentURL() + "recycle");
            if (!(webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div[2]/div[2]/div[1]/div/div/div/div[2]/table/tbody/tr/td[1]/div/label/span/input")).isSelected())) {
                webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div[2]/div[2]/div[1]/div/div/div/div[2]/table/tbody/tr/td[1]/div/label/span/input")).click();
            }

            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div[1]/button[1]/span")).click();

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }

    }
}

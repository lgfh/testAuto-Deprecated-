package cn.unionstech.application;

import cn.unionstech.Utils.BypassLoginWithCookies;
import cn.unionstech.Utils.ChromeDriverUtil;
import cn.unionstech.Utils.JsonUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
@Service
public class AutoDestroyDB {

    private final static Logger logger = Logger.getLogger(AutoDestroyDB.class);

    @Autowired
    ChromeDriverUtil chromeDriverUtil;

    public String autoDestroyDB(String zone) {
        //准备chrome的驱动
        WebDriver webDriver = chromeDriverUtil.prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        try {
            Properties properties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/xpath.properties"));
            properties.load(bufferedReader);
            //利用cookies跳过登陆，进入host的界面
            login.bypassLoginWithCookies(webDriver);
            Actions action = new Actions(webDriver);
            webDriver.get(login.getCurrentURL() + "cloudDatabase");
            if (!(login.getCurrentURL().contains("zschj"))) {
                //区域选择
                action.moveToElement(webDriver.findElement(By.xpath(properties.getProperty("DB页面下拉选区")))).perform();
                Thread.sleep(1000);
                webDriver.findElement(By.xpath(properties.getProperty("DB页面下拉选区" + zone))).click();
            }
            //数据库页面释放公网ip
            webDriver.navigate().refresh();
//            if(!(webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div/div[2]/table/tbody/tr/td[5]/div/div/span")).getText().contains("绑定公网IP"))) {
            webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div/div[2]/table/tbody/tr/td[5]/div/div/span[2]")).click();
            webDriver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/button[2]/span")).click();
            // 等待解绑
            Thread.sleep(15000);
//            }
            //点删除
            webDriver.navigate().refresh();
            webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/div/div/div[2]/table/tbody/tr/td[8]/div/div/span")).click();
            webDriver.findElement(By.xpath("/html/body/div[10]/div[2]/div/div[3]/button[2]")).click();


            //清理回收站
            webDriver.get(login.getCurrentURL() + "recycle");
            if (!(webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div[2]/div[2]/div[1]/div/div/div/div[1]/table/thead/tr/th[1]/div/label/span/input")).isSelected())) {
                webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div[2]/div[2]/div[1]/div/div/div/div[1]/table/thead/tr/th[1]/div/label/span/input")).click();
            }
            webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div[1]/button[1]/span")).click();
//            logger.info("DB has been destroyed");
            Thread.sleep(1000);
            logger.info("auto destroy DB success");
            return JsonUtil.getJSONString(0, "删除数据库成功,zone是：" + zone);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonUtil.getJSONString(1, "删除数据库失败,zone是: " + zone);
        } finally {
            webDriver.quit();
        }
    }


}

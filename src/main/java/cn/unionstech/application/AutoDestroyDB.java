package cn.unionstech.application;

import cn.unionstech.Utils.BypassLoginWithCookies;
import cn.unionstech.Utils.ChromeDriverUtil;
import cn.unionstech.Utils.JsonUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author WXY
 * @version 创建时间：2018/11/1
 */
public class AutoDestroyDB {

    private final static Logger logger = Logger.getLogger(AutoDestroyDB.class);

    public static void main(String[] args) {
        autoDestroyDB();
    }

    public static String autoDestroyDB() {
        //准备chrome的驱动
        WebDriver webDriver = ChromeDriverUtil.prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        try {
            //利用cookies跳过登陆，进入host的界面
            login.bypassLoginWithCookies(webDriver);
            webDriver.get(login.getCurrentURL() + "cloudDatabase");
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
            return JsonUtil.getJSONString(0, "删除数据库成功");
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            return JsonUtil.getJSONString(1, "删除数据库失败");
        } finally {
            webDriver.quit();
        }
    }


}

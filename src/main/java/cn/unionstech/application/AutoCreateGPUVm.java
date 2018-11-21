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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.Random;

@Service
public class AutoCreateGPUVm {

    private final static Logger logger = Logger.getLogger(AutoCreateGPUVm.class);

    @Autowired
    ChromeDriverUtil chromeDriverUtil;


    public String autoCreateGpuVM() {
        //准备chrome的驱动
        WebDriver webDriver = chromeDriverUtil.prepareChromeWebDriver();
        //实例化工具类
        BypassLoginWithCookies login = new BypassLoginWithCookies();

        try {
            Properties properties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/xpath.properties"));
            properties.load(bufferedReader);
            //利用cookies跳过登陆，进入创建GPU的界面
            login.bypassLoginWithCookies(webDriver);

            webDriver.get(login.getCurrentURL() + "GpuList");
            //创建
            webDriver.findElement(By.xpath(properties.getProperty("GPU页面创建按钮"))).click();
            Thread.sleep(5000);
            //*[@id="content"]/div[3]/button/span

            Actions action = new Actions(webDriver);
            if (!(login.getCurrentURL().contains("zschj"))) {
                //区域选择
                webDriver.findElement(By.xpath(properties.getProperty("GPU购买页面-北方二区"))).click();
            }

            //实时计费
            webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[2]/div/div[3]")).click();

            //选择镜像，返回所有镜像放入一个list中
            action.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[3]/div[2]/div[1]/div"))).perform();
            Thread.sleep(1000);

            List<WebElement> GpuVmTemplateList = webDriver.findElements(By.xpath("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[3]/div[2]/div[2]/ul/li"));
            Random random = new Random();
            int num = random.nextInt(GpuVmTemplateList.size()) + 1;
//            String firstTemplateElement =GpuVmTemplateList.get(0).getText();
            logger.info("使用模板： " +
                    webDriver.findElement(By.xpath(String.format
                            ("//*[@id=\"Pdata\"]/div/div[3]/div[1]/div[2]/div/div[2]/div[3]/div[2]/div[2]/ul/li[%d]", num))).getText());
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
            if (webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div/div/h1")).getText() != null) {
                if (webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div/div/h1")).getText().equals("支付失败")) {
                    return JsonUtil.getJSONString(1, "订单支付失败，创建GPU主机失败");
                }
                logger.info(webDriver.findElement(By.xpath("//*[@id=\"back\"]/div[5]/div/div/div/div/div/div/h1")).getText());
                logger.info("创建GPU主机成功");
                return JsonUtil.getJSONString(0, "创建GPU主机成功");
            }
        } catch (FileNotFoundException e) {
            logger.error("xpath文件读取失败");
            return JsonUtil.getJSONString(1, "xpath文件读取失败，创建GPU主机失败");
        } catch (Exception e) {
            logger.error("创建GPU主机失败" + e.getMessage());
            return JsonUtil.getJSONString(1, "创建GPU主机失败");
        } finally {
            webDriver.quit();
        }
        return JsonUtil.getJSONString(1, "创建GPU主机失败");
    }
}

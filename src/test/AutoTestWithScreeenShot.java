import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AutoTestWithScreeenShot {

    @Test
    public void AutoTestWithScreeenShot() throws IOException, InterruptedException {
        //    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        WebDriver webDriver = new ChromeDriver();


        webDriver.manage().window().maximize();
        webDriver.manage().deleteAllCookies();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        webDriver.get("https://www.xrcloud.net/ruicloud/login");

        WebElement ele = webDriver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[2]/div[2]/form/div[3]/img"));
//         webDriver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[2]/div[2]/form/div[3]/img")).click();
        File screenShot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        BufferedImage bufferedImage = ImageIO.read(screenShot);
        Point point = ele.getLocation();
        int eleWidth = ele.getSize().getWidth();
        int eleHeight = ele.getSize().getHeight();

        Dimension size = ele.getSize();

//        BufferedImage eleScreenshot = bufferedImage.getSubimage(point.getX(),point.getY(),180,130);
        BufferedImage eleScreenshot = bufferedImage.getSubimage(point.getX(),point.getY(),size.getWidth()+eleWidth,size.getHeight()+eleHeight);
        ImageIO.write(eleScreenshot,"png",screenShot);
        File screenShotLocation = new File("src/main/resources/testCode.png");
        FileUtils.copyFile(screenShot,screenShotLocation);

        Thread.sleep(5000);

        webDriver.quit();
    }

}

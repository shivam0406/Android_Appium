package testpackage.pageobjects;

import io.appium.java_client.android.AndroidDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testpackage.utils.CommandLine;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sachin on 3/31/2016.
 */
public class advancePlayBackSampleApp {

    public void waitForAppHomeScreen(AndroidDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath("//android.widget.TextView[@text='Play With InitialTime']")));

    }

    public void assertCurrentActivityAgainst(AndroidDriver driver, String activityName) {

        Assert.assertEquals(driver.currentActivity(), activityName);
    }

    public void clickBasedOnText(AndroidDriver driver, String clickText) {

        WebElement clickTextField = driver.findElement(By.xpath("//android.widget.TextView[@text='" + clickText + "']"));
        clickTextField.click();

    }

    public void waitForPresence(AndroidDriver driver, String typeOf, String waitString) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        if (typeOf == "className") {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className(waitString)));

        }

    }

    public void clickOnVastAd(AndroidDriver driver) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("in vast ad");
        driver.findElementById("com.ooyala.sample.AdvancedPlaybackSampleApp:id/doubleLeftButton").click();
        System.out.println("clicked");
    }

    public void playVideo(AndroidDriver driver) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElementByXPath("//android.widget.ImageButton[@index='0']").click();

    }

    public void pauseVideo(AndroidDriver driver) throws InterruptedException{
        Thread.sleep(2000);
        System.out.println("Pausing the Video");
        // Tap coordinates to pause
        String dimensions = driver.manage().window().getSize().toString();
        //System.out.println(" Dimensions are "+dimensions);
        String[] dimensionsarray=dimensions.split(",");
        int length = dimensionsarray[1].length();
        String ydimensions=dimensionsarray[1].substring(0,length-1);
        String ydimensionstrimmed=ydimensions.trim();
        int ydimensionsInt= Integer.parseInt(ydimensionstrimmed);
        driver.tap(1, 35 , (ydimensionsInt-25), 2);
    }

    public void getBackFromRecentApp (AndroidDriver driver) throws InterruptedException, IOException {

        String command = "adb shell input keyevent KEYCODE_APP_SWITCH";
        String[] final_command = CommandLine.command(command);
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(final_command);
        Thread.sleep(3000);
        System.out.println("showing recent app screen");
        driver.findElement(By.xpath("//android.view.View[@index= '0']")).click();  // here clicking on system ui to get back the sample app
        System.out.println("back to SDK");
    }

    public void powerKeyClick (AndroidDriver driver) throws InterruptedException,IOException {

        driver.sendKeyEvent(26);            // key 26 is used to lock the screen
        System.out.println("key sent");
        System.out.println("screen lock");
        Thread.sleep(5000);
        //driver.sendKeyEvent(82);            // key 82 is used to unlock the screen
        String command = "adb shell am start -n io.appium.unlock/.Unlock";
        String[] final_command = CommandLine.command(command);
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(final_command);
        Thread.sleep(3000);
        System.out.println("showing screen unlock");
        driver.navigate().back();
        System.out.println("Back to Sample App screen ");
        Thread.sleep(2000);
    }

    public void clickOnOoyalaAd(AndroidDriver driver) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("in Ooyala ad");
        driver.findElementById("com.ooyala.sample.AdvancedPlaybackSampleApp:id/doubleRightButton").click();
        System.out.println("clicked");
    }

    public void clickOnP1(AndroidDriver driver) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("in Video1 ad");
        driver.findElementById("com.ooyala.sample.AdvancedPlaybackSampleApp:id/doubleLeftButton").click();
        System.out.println("clicked");

    }

    public void clickOnP2(AndroidDriver driver) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("in Video2 ad");
        driver.findElementById("com.ooyala.sample.AdvancedPlaybackSampleApp:id/doubleRightButton").click();
        System.out.println("clicked");

    }

    public void clickFullScreen (AndroidDriver driver) throws InterruptedException{
        Thread.sleep(1000);
        System.out.println("Clicking on full screen button");
        driver.findElementByXPath("//android.widget.ImageButton[@index='2']").click();
    }

    public void clickNormalScreen (AndroidDriver driver) throws  InterruptedException{
        Thread.sleep(1000);
        System.out.println("Clicking on normal screen button");
        WebElement frameLayout = driver.findElement(By.id("content"));
        List<WebElement> layout = frameLayout.findElements(By.className("android.widget.LinearLayout"));
        WebElement normalScreen = layout.get(0).findElement(By.className("android.widget.ImageButton"));
        normalScreen.click();
        System.out.println("Clicked");
    }

    public void backSeekInFullScreen(AndroidDriver driver) throws  InterruptedException{
        System.out.println("Clicking on Back seek button");
        WebElement layout = driver.findElement(By.xpath("//android.widget.LinearLayout[@index=1]"));
        List<WebElement> seek = layout.findElements(By.className("android.widget.ImageButton"));
        seek.get(0).click();
        System.out.println("Back seek button clicked");
    }

    public void playVideoFullScreen(AndroidDriver driver) throws  InterruptedException{
        System.out.println("Clicking on play button in full screen");
        WebElement layout = driver.findElement(By.xpath("//android.widget.LinearLayout[@index=1]"));
        List<WebElement> play = layout.findElements(By.className("android.widget.ImageButton"));
        play.get(1).click();
        System.out.println("Play button clicked");
    }

    public void pauseVideoFullScreen(AndroidDriver driver) throws InterruptedException{
        Thread.sleep(2000);
        System.out.println("Pausing the Video in Full Screen");
        //Pausing Video
        String dimensions = driver.manage().window().getSize().toString();
        String[] dimensionsarray=dimensions.split(",");
        int length = dimensionsarray[1].length();
        String ydimensions=dimensionsarray[1].substring(0,length-1);
        String ydimensionstrimmed=ydimensions.trim();
        int ydimensionsInt= Integer.parseInt(ydimensionstrimmed);
        driver.tap(1, 530 , (ydimensionsInt-120), 2);
    }

    public void pauseSmallPlayer (AndroidDriver driver) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Pausing the Video");
        //Pausing Video
        String dimensions = driver.manage().window().getSize().toString();
        String[] dimensionsarray=dimensions.split(",");
        int length = dimensionsarray[1].length();
        String ydimensions=dimensionsarray[1].substring(0,length-1);
        String ydimensionstrimmed=ydimensions.trim();
        int ydimensionsInt= Integer.parseInt(ydimensionstrimmed);
        driver.tap(1, 45 , (ydimensionsInt-370), 2);
    }

    public void customControlPlayButton (AndroidDriver driver) throws InterruptedException{
        Thread.sleep(2000);
        System.out.println("Playing Paused Video");
        //Play Video
        String dimensions = driver.manage().window().getSize().toString();
        String[] dimensionsarray=dimensions.split(",");
        int length = dimensionsarray[1].length();
        String ydimensions=dimensionsarray[1].substring(0, length - 1);
        String ydimensionstrimmed=ydimensions.trim();
        int ydimensionsInt= Integer.parseInt(ydimensionstrimmed);
        System.out.println("Ydimention is:"+ydimensionstrimmed);
        driver.tap(1, 535, (ydimensionsInt-60), 2);
    }
    public void customControlPauseButton(AndroidDriver driver) throws InterruptedException{
        Thread.sleep(2000);
        System.out.println("Clicking on Pause Video");
        //Pausing Video
        String dimensions = driver.manage().window().getSize().toString();
        String[] dimensionsarray=dimensions.split(",");
        int length = dimensionsarray[1].length();
        String ydimensions=dimensionsarray[1].substring(0,length-1);
        String ydimensionstrimmed=ydimensions.trim();
        int ydimensionsInt= Integer.parseInt(ydimensionstrimmed);
        System.out.println("Ydimention is:"+ydimensionstrimmed);
        driver.tap(1, 535 , (ydimensionsInt-60), 2);
    }

    public void overlay(AndroidDriver driver) throws InterruptedException{
        Thread.sleep(1000);
        System.out.println("Check Overlay Present or not");
        WebElement overlay1 = driver.findElement(By.xpath("//android.widget.TextView[@text='This is an overlay']"));
        Assert.assertEquals(true, overlay1.isDisplayed());
        System.out.println("Overlay  diplayed");
    }

}



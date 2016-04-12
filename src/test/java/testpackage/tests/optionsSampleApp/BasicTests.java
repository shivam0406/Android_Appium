package testpackage.tests.optionsSampleApp;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import testpackage.pageobjects.optionsSampleApp;
import testpackage.utils.*;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Sachin on 4/11/2016.
 */
public class BasicTests {
    private static AndroidDriver driver;

    @BeforeClass
    public void beforeTest() throws Exception {
        System.out.println("BeforeTest \n");

        System.out.println(System.getProperty("user.dir"));
        // Get Property Values
        LoadPropertyValues prop = new LoadPropertyValues();
        Properties p = prop.loadProperty("optionsSampleApp.properties");

        //System.out.println("Device id from properties file " + p.getProperty("deviceName"));
        //System.out.println("PortraitMode from properties file " + p.getProperty("PortraitMode"));
        //System.out.println("Path where APK is stored"+ p.getProperty("appDir"));
        //System.out.println("APK name is "+ p.getProperty("app"));
        //System.out.println("Platform under Test is "+ p.getProperty("platformName"));
        //System.out.println("Mobile OS Version is "+ p.getProperty("OSVERSION"));
        //System.out.println("Package Name of the App is "+ p.getProperty("appPackage"));
        //System.out.println("Activity Name of the App is "+ p.getProperty("appActivity"));

        SetUpAndroidDriver setUpdriver = new SetUpAndroidDriver();
        driver = setUpdriver.setUpandReturnAndroidDriver(p.getProperty("udid"), p.getProperty("appDir"), p.getProperty("appValue"), p.getProperty("platformName"), p.getProperty("platformVersion"), p.getProperty("appPackage"), p.getProperty("appActivity"));
        Thread.sleep(2000);
    }

    @BeforeMethod
    //public void beforeTest() throws Exception{
    public void beforeMethod() throws Exception {
        System.out.println("beforeMethod \n");
        //removeEventsLogFile.removeEventsFileLog(); create events file
        PushLogFileToDevice logpush = new PushLogFileToDevice();
        logpush.pushLogFile();
        if (driver.currentActivity() != "com.ooyala.sample.lists.OptionsListActivity") {
            driver.startActivity("com.ooyala.sample.OptionsSampleApp", "com.ooyala.sample.lists.OptionsListActivity");
        }

        // Get Property Values
        LoadPropertyValues prop1 = new LoadPropertyValues();
        Properties p1 = prop1.loadProperty();

        System.out.println(" Screen Mode " + p1.getProperty("ScreenMode"));

        //if(p1.getProperty("ScreenMode") != "P"){
        //    System.out.println("Inside landscape Mode ");
        //    driver.rotate(ScreenOrientation.LANDSCAPE);
        //}

        //driver.rotate(ScreenOrientation.LANDSCAPE);
        //driver.rotate(ScreenOrientation.LANDSCAPE);

    }

    @AfterClass
    public void afterTest() throws InterruptedException, IOException {
        System.out.println("AfterTest \n");
        driver.closeApp();
        driver.quit();

    }

    @AfterMethod
    //public void afterTest() throws InterruptedException, IOException {
    public void afterMethod() throws InterruptedException, IOException {
        // Waiting for all the events from sdk to come in .
        System.out.println("AfterMethod \n");
        //ScreenshotDevice.screenshot(driver);
        RemoveEventsLogFile.removeEventsFileLog();
        Thread.sleep(10000);

    }

    @org.testng.annotations.Test
    public void CuePointsAndAdsControlOptions() throws Exception {

        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("OptionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");
            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "CuePoints and AdsControl Options");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.CuePointsOptionsFreewheelPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Click on Video create button
            po.clickButtons(driver, 0);

            // Wait for the video to be generated
            po.waitForPresenceOfText(driver, "00:00");

            // Click on video play icon after video has been generated .
            po.clickImagebuttons(driver, 0);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);


            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            //Wait for Ad to start and verify the adStarted event .
            ev.verifyEvent("adStarted", " Ad Started to Play ", 49000);

            //Wait for Ad to complete and verify the adCompleted event .
            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);

            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 30000);


        } catch (Exception e) {
            System.out.println(" Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }

    }

    @org.testng.annotations.Test
    public void preload_PromoImage_options() throws Exception {

        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("OptionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");
            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "Preload and PromoImage Options");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreloadOptionsPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Click on Video create button
            po.clickButtons(driver, 0);

            // Wait for the video to be generated
            po.waitForPresenceOfText(driver, "00:00");

            // Click on video play icon after video has been generated .
            po.clickImagebuttons(driver, 0);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);


            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            //Wait for Ad to start and verify the adStarted event .
            ev.verifyEvent("adStarted", " Ad Started to Play ", 49000);

            //Wait for Ad to complete and verify the adCompleted event .
            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);

            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 30000);


        } catch (Exception e) {
            System.out.println(" Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }

    }

    @org.testng.annotations.Test
    public void preload_promo_IntialTime() throws Exception {

        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("OptionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");
            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "Preload and Promo Options with Initial Time");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreloadWithInitTimePlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Click on Video create button
            po.clickButtons(driver, 0);

            // Wait for the video to be generated
         //   po.waitForPresenceOfText(driver, "00:00");
            Thread.sleep(7000);

            // Click on video play icon after video has been generated .
            po.clickImagebuttons(driver, 0);

            //Play Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("playStarted", " Video Started to Play ", 30000);

            ev.verifyEvent("seekCompleted - state: PLAYING", "video starting from predefined intial time",60000);

            ev.verifyEvent("playCompleted - state: LOADING", "video play completed",90000);

        } catch (Exception e) {
            System.out.println(" Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }


    }

    @org.testng.annotations.Test
    public void server_side_TvRating() throws Exception {

        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("OptionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");
            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "Server-Side TV Ratings");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.ServerConfiguredTVRatingsPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");


            //Play Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("playStarted", " Video Started to Play ", 30000);

            ev.verifyEvent("playCompleted - state: LOADING", "video play completed",90000);

        } catch (Exception e) {
            System.out.println(" Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }


    }

    @org.testng.annotations.Test
    public void tv_rating_config() throws Exception {

        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("OptionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");
            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "TV Ratings Configuration");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.TVRatingsPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Click on Video create button
            po.clickButtons(driver, 0);

            // Wait for the video to be generated
            po.waitForPresenceOfText(driver, "00:00");

            // Click on video play icon after video has been generated .
            po.clickImagebuttons(driver, 0);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

           //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 90000);


        } catch (Exception e) {
            System.out.println(" Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }

    }

    @org.testng.annotations.Test
    public void present_video_view_sharing_options() throws Exception {

        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("OptionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");
            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "Prevent Video View Sharing Option");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreventVideoViewSharingPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");


            //Play Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("playStarted", " Video Started to Play ", 30000);

            ev.verifyEvent("playCompleted - state: LOADING", "video play completed",90000);

        } catch (Exception e) {
            System.out.println(" Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }


    }

    @org.testng.annotations.Test
    public void CuePointsAndAdsControlOptions_On() throws Exception{

        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("optionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "CuePoints and AdsControl Options");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.CuePointsOptionsFreewheelPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Click on Video create button
            po.clickButtons(driver,0);

            // Wait for the video to be generated
            po.waitForPresenceOfText(driver,"00:00");

            // Click on video play icon after video has been generated .
            po.clickImagebuttons(driver,0);

            System.out.println("FWCuePointsAndAdsControlOptions_On");
            //Play Started Verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);
            WebDriverWait wait = new WebDriverWait(driver,30);
            wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//android.widget.TextView[@text='Learn More']"))));
            System.out.println("learn more displayed");
            Thread.sleep(1000);
            // Click on the web area so that player screen shows up
            WebElement viewarea = driver.findElementByClassName("android.view.View");

            po.clickOnViewarea(driver);

            Thread.sleep(800);

            //pausing ad
            po.adPause(driver);

            //verifing event for pause
            ev.verifyEvent("stateChanged - state: PAUSED", "Ad paused", 3000);

            Thread.sleep(5000);

            po.clickOnViewarea(driver);

            po.adPlay(driver);

            Thread.sleep(4000);

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);


            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            // Thread.sleep(1000);

            //Wait for Ad to start and verify the adStarted event .
            ev.verifyEvent("adStarted", " Ad Started to Play ", 49000);

            wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//android.widget.TextView[@text='Learn More']"))));
            System.out.println("learn more displayed");
            Thread.sleep(1000);
            po.clickOnViewarea(driver);
            // viewarea.click();

            Thread.sleep(1000);

            //pausing ad
            po.adPause(driver);

            //verifing event for pause
            ev.verifyEvent("stateChanged - state: PAUSED", "Ad paused", 3000);

            Thread.sleep(5000);

            // Click on the web area so that player screen shows up
            po.clickOnViewarea(driver);
            //viewarea.click();

            po.adPlay(driver);

            Thread.sleep(4000);

            //Wait for Ad to complete and verify the adCompleted event .
            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);


            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//android.widget.TextView[@text='Learn More']"))));
            System.out.println("learn more displayed");
            Thread.sleep(1000);
            po.clickOnViewarea(driver);

            Thread.sleep(800);

            //pausing ad
            po.adPause(driver);

            //verifing event for pause
            ev.verifyEvent("stateChanged - state: PAUSED", "Ad paused", 3000);

            Thread.sleep(5000);


            // Click on the web area so that player screen shows up

            po.clickOnViewarea(driver);

            po.adPlay(driver);

            Thread.sleep(4000);

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 30000);

        }
        catch(Exception e)
        {
            System.out.println(" Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }
    }

    @org.testng.annotations.Test
    public void CuePointsAndAdsControlOptions_cuePointOff() throws Exception{
        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("optionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "CuePoints and AdsControl Options");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.CuePointsOptionsFreewheelPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            System.out.println("FWCuePointsAndAdsControlOptions_cuePointOff_leanmore");
            //turning off cue point
            po.cuepointOff(driver);

            //Click on Video create button
            po.clickButtons(driver,0);

            // Wait for the video to be generated
            po.waitForPresenceOfText(driver,"00:00");

            // Click on video play icon after video has been generated .
            po.clickImagebuttons(driver,0);



            //Play Started Verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            // clicking on learn more button

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);



            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            // Thread.sleep(1000);

            //Wait for Ad to start and verify the adStarted event .
            ev.verifyEvent("adStarted", " Ad Started to Play ", 49000);


            //Wait for Ad to complete and verify the adCompleted event .
            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);


            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(4000);

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 30000);

        }
        catch(Exception e)
        {
            System.out.println(" Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }
    }

    @org.testng.annotations.Test
    public void CuePointsAndAdsControlOptions_adControlsOff() throws Exception{
        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("optionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "CuePoints and AdsControl Options");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.CuePointsOptionsFreewheelPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            System.out.println("FWCuePointsAndAdsControlOptions_adControlsOff_leanmore");
            //turning off cue point
            po.adControlOff(driver);

            //Click on Video create button
            po.clickButtons(driver,0);

            // Wait for the video to be generated
            po.waitForPresenceOfText(driver,"00:00");

            // Click on video play icon after video has been generated .
            po.clickImagebuttons(driver,0);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            //clicking on view area for click threw
            po.clickOnViewarea(driver);
            ev.verifyEvent("stateChanged - state: SUSPENDED","click on screen and click through",3000);

            Thread.sleep(5000);
            // getting back to SDK
            driver.navigate().back();

            // ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);




            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            // Thread.sleep(1000);

            //Wait for Ad to start and verify the adStarted event .
            ev.verifyEvent("adStarted", " Ad Started to Play ", 49000);

            po.clickOnViewarea(driver);
            ev.verifyEvent("stateChanged - state: SUSPENDED","click on screen and click through",30000);

            Thread.sleep(5000);
            // getting back to SDK
            driver.navigate().back();

            // verifing event ad play completed
            ev.verifyEvent("adCompleted - state: SUSPENDED", "Ad play completed", 40000);


            Thread.sleep(5000);

            ev.verifyEvent("adStarted", " Ad Started to Play ",60000);

            po.clickOnViewarea(driver);
            ev.verifyEvent("stateChanged - state: SUSPENDED","click on screen and click through",30000);

            Thread.sleep(5000);
            // getting back to SDK
            driver.navigate().back();


            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 70000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 90000);

        }
        catch(Exception e)
        {
            System.out.println(" Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }
    }

    @org.testng.annotations.Test
    public void timeout_Options() throws Exception {

        try {
            // Creating an Object of optionsSampleApp class
            optionsSampleApp po = new optionsSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.OptionsListActivity");
            // Wrire to console activity name of home screen app
            System.out.println("OptionsSample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");
            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "Timeout Options");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.TimeoutOptionsPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Click on Video create button
            po.clickButtons(driver, 0);

            // Wait for the video to be generated
            po.waitForPresenceOfText(driver, "00:00");

            // Click on video play icon after video has been generated .
            po.clickImagebuttons(driver, 0);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);


            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            //Wait for Ad to start and verify the adStarted event .
            ev.verifyEvent("adStarted", " Ad Started to Play ", 49000);

            //Wait for Ad to complete and verify the adCompleted event .
            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);

            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            ev.verifyEvent("adCompleted", " Ad Completed to Play ", 35000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 30000);


        } catch (Exception e) {
            System.out.println(" Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver);
        }

    }


}

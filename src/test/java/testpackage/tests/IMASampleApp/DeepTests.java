package testpackage.tests.IMASampleApp;

import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import testpackage.pageobjects.IMASampleApp;
import testpackage.utils.*;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Sachin on 4/26/2016.
 */
public class DeepTests extends EventLogTest{

    final static Logger logger = Logger.getLogger(DeepTests.class);

    @BeforeClass
    public void beforeTest() throws Exception {

        // closing all recent app from background.
        CloserecentApps.closeApps();

        logger.info("BeforeTest \n");

        logger.debug(System.getProperty("user.dir"));
        // Get Property Values
        LoadPropertyValues prop = new LoadPropertyValues();
        Properties p=prop.loadProperty("IMASampleapp.properties");

        logger.debug("Device id from properties file " + p.getProperty("deviceName"));
        logger.debug("PortraitMode from properties file " + p.getProperty("PortraitMode"));
        logger.debug("Path where APK is stored"+ p.getProperty("appDir"));
        logger.debug("APK name is "+ p.getProperty("appValue"));
        logger.debug("Platform under Test is "+ p.getProperty("platformName"));
        logger.debug("Mobile OS Version is "+ p.getProperty("OSVERSION"));
        logger.debug("Package Name of the App is "+ p.getProperty("appPackage"));
        logger.debug("Activity Name of the App is "+ p.getProperty("appActivity"));

        SetUpAndroidDriver setUpdriver = new SetUpAndroidDriver();
        driver = setUpdriver.setUpandReturnAndroidDriver(p.getProperty("udid"), p.getProperty("appDir"), p.getProperty("appValue"), p.getProperty("platformName"), p.getProperty("platformVersion"), p.getProperty("appPackage"), p.getProperty("appActivity"));
        Thread.sleep(2000);
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        logger.info("beforeMethod \n");
        driver.manage().logs().get("logcat");
        PushLogFileToDevice logpush=new PushLogFileToDevice();
        logpush.pushLogFile();
        if(driver.currentActivity()!= "com.ooyala.sample.lists.IMAListActivity") {
            driver.startActivity("com.ooyala.sample.IMASampleApp","com.ooyala.sample.lists.IMAListActivity");
        }

        // Get Property Values
        LoadPropertyValues prop1 = new LoadPropertyValues();
        Properties p1=prop1.loadProperty();

        logger.debug(" Screen Mode "+ p1.getProperty("ScreenMode"));

        //if(p1.getProperty("ScreenMode") != "P"){
        //    logger.info("Inside landscape Mode ");
        //    driver.rotate(ScreenOrientation.LANDSCAPE);
        //}

        //driver.rotate(ScreenOrientation.LANDSCAPE);
        //driver.rotate(ScreenOrientation.LANDSCAPE);

    }

    @AfterClass
    public void afterTest() throws InterruptedException, IOException {
        logger.info("AfterTest \n");
        driver.closeApp();
        driver.quit();
        LoadPropertyValues prop1 = new LoadPropertyValues();
        Properties p1 = prop1.loadProperty();
        String prop = p1.getProperty("appPackage");
        Appuninstall.uninstall(prop);

    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {
        // Waiting for all the events from sdk to come in .
        logger.info("AfterMethod \n");
        //ScreenshotDevice.screenshot(driver);
        RemoveEventsLogFile.removeEventsFileLog();
        Thread.sleep(10000);

    }
     @org.testng.annotations.Test
    public void IMAAdRulePreroll() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Ad-Rules Preroll");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(5000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 0);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);

            Thread.sleep(5000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(3000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMAAdRulePreroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAAdRulePreroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @org.testng.annotations.Test
    public void IMAAdRuleMidroll() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Ad-Rules Midroll");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(5000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 0);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);



            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            Thread.sleep(5000);
            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(3000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMAAdRuleMidroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAAdRuleMidroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @org.testng.annotations.Test
    public void IMAAdRulePostroll() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Ad-Rules Postroll");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(5000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 0);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);

            Thread.sleep(5000);
            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(3000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMAAdRulePostroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAAdRulePostroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @org.testng.annotations.Test
    public void IMAPoddedPreroll() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Podded Preroll");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(5000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 0);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);

            Thread.sleep(5000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(3000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMAPoddedPreroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAPoddedPreroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @org.testng.annotations.Test
    public void IMAPoddedMidroll() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Podded Midroll");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(5000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 0);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);



            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);


            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);


            Thread.sleep(5000);
            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(3000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMAPoddedMidroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAPoddedMidroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @org.testng.annotations.Test
    public void IMAPoddedPostroll() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Podded Postroll");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(5000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 0);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);

            Thread.sleep(5000);
            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(3000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMAPoddedPostroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAPoddedPostroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @org.testng.annotations.Test
    public void IMAPoddedPreMidPostroll() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Podded Pre-Mid-Post");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();

            // 1st IMA AD
            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

           ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            // 2nd IMA Ad
            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);



            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            // 3rd IMA AD
            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);



            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);


            // 1st Midroll IMA
            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            // 2nd Midroll IMa
            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            //3rd IMA Midroll
            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            ev.verifyEvent("stateChanged - state: PLAYING", " Ad has been completed and video start playing ", 60000);


            Thread.sleep(7000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 2);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);


            Thread.sleep(1000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(4000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);

            Thread.sleep(5000);
            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(3000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            // 1st Postroll IMA
            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            // 2nd Postroll IMa
            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            //3rd IMA Postroll
            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMAPoddedPreMidPostroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAPoddedPreMidPostroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @org.testng.annotations.Test
    public void IMASkippable() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Skippable");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(5000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 0);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);

            Thread.sleep(2000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMASkippable throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMASkippable");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @org.testng.annotations.Test
    public void IMAApplication_Configured() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Application-Configured");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.CustomConfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(3000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 0);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);


            ev.verifyEvent("adStarted", " Ad Started to Play ", 60000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);


            Thread.sleep(5000);
            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(3000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMAApplication_Configured throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAApplication_Configured");
            Assert.assertTrue(false, "This will fail!");
        }
    }

   @org.testng.annotations.Test
    public void IMA_PreMidPost_skippable() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Pre, Mid and Post Skippable");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();

            // 1st IMA AD
            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            // 1st Midroll IMA
            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            ev.verifyEvent("stateChanged - state: PLAYING", " Ad has been completed and video start playing ", 60000);


            Thread.sleep(7000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 2);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);


            Thread.sleep(1000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(4000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);

            Thread.sleep(5000);
            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(3000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            // 1st Postroll IMA
            ev.verifyEvent("adStarted", " Ad Started to Play ", 50000);

            Thread.sleep(5000);

            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMA_PreMidPost_skippable throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMA_PreMidPost_skippable");
            Assert.assertTrue(false, "This will fail!");
        }
    }


 @org.testng.annotations.Test
    public void IMASkippable_skip() throws Exception{

        try {
            // Creating an Object of IMA class
            IMASampleApp po = new IMASampleApp();
            // wait till home screen of IMASampleApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "IMA Skippable");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            //Play Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            po.skip_Button(driver);


            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(5000);

            // Tap coordinates to pause
            String dimensions = driver.manage().window().getSize().toString();
            //logger.info(" Dimensions are "+dimensions);
            String[] dimensionsarray = dimensions.split(",");
            int length = dimensionsarray[1].length();
            String ydimensions = dimensionsarray[1].substring(0, length - 1);
            String ydimensionstrimmed = ydimensions.trim();
            int ydimensionsInt = Integer.parseInt(ydimensionstrimmed);
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            driver.tap(1, 35, (ydimensionsInt - 25), 0);
            Thread.sleep(2000);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 30000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.getXYSeekBarAndSeek(driver,20,120);
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 30000);
            Thread.sleep(3000);

            po.videoPlay(driver);

            // video start playing after pause
            ev.verifyEvent("stateChanged - state: PLAYING", " Video Started to Play ", 30000);

            Thread.sleep(2000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 70000);
            Thread.sleep(2000);
        }
        catch(Exception e)
        {
            logger.error("IMASkippable_skip throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMASkippable_skip");
            Assert.assertTrue(false, "This will fail!");
        }
    }




}

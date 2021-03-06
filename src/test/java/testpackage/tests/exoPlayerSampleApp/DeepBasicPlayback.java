package testpackage.tests.exoPlayerSampleApp;

import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testpackage.pageobjects.exoPlayerSampleApp;
import testpackage.utils.*;

import java.io.IOException;
import java.util.Properties;


public class DeepBasicPlayback extends EventLogTest {
    final static Logger logger = Logger.getLogger(DeepBasicPlayback.class);


    @BeforeClass
    public void beforeTest() throws Exception {

        // closing all recent app from background.
        CloserecentApps.closeApps();
        logger.debug("BeforeTest \n");

        logger.debug(System.getProperty("user.dir"));
        // Get Property Values
        LoadPropertyValues prop = new LoadPropertyValues();
        Properties p = prop.loadProperty("exoPlayerSampleApp.properties");

        logger.debug("Device id from properties file " + p.getProperty("deviceName"));
        logger.debug("PortraitMode from properties file " + p.getProperty("PortraitMode"));
        logger.debug("Path where APK is stored" + p.getProperty("appDir"));
        logger.debug("APK name is " + p.getProperty("app"));
        logger.debug("Platform under Test is " + p.getProperty("platformName"));
        logger.debug("Mobile OS Version is " + p.getProperty("OSVERSION"));
        logger.debug("Package Name of the App is " + p.getProperty("appPackage"));
        logger.debug("Activity Name of the App is " + p.getProperty("appActivity"));

        SetUpAndroidDriver setUpdriver = new SetUpAndroidDriver();
        driver = setUpdriver.setUpandReturnAndroidDriver(p.getProperty("udid"), p.getProperty("appDir"), p.getProperty("appValue"), p.getProperty("platformName"), p.getProperty("platformVersion"), p.getProperty("appPackage"), p.getProperty("appActivity"));
        Thread.sleep(2000);
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        logger.info("beforeMethod \n");
        driver.manage().logs().get("logcat");
        PushLogFileToDevice logpush = new PushLogFileToDevice();
        logpush.pushLogFile();
        if (driver.currentActivity() != "com.ooyala.sample.complete.MainExoPlayerActivity") {
            driver.startActivity("com.ooyala.sample.ExoPlayerSampleApp", "com.ooyala.sample.complete.MainExoPlayerActivity");
        }

        // Get Property Values
        LoadPropertyValues prop1 = new LoadPropertyValues();
        Properties p1 = prop1.loadProperty();

        logger.debug(" Screen Mode " + p1.getProperty("ScreenMode"));

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
    public void AspectRatio() throws Exception {
        try {

            // Creating an Object of FreeWheelSampleApp class
            exoPlayerSampleApp po = new exoPlayerSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.complete.MainExoPlayerActivity");
            // Wrire to console activity name of home screen app
            logger.debug("ExoPlayerApp Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            po.clickBasedOnText(driver, "Basic Playback");
            Thread.sleep(2000);

            logger.debug(" Print current activity name" + driver.currentActivity());
            if (driver.currentActivity().toString().equals(".Settings$AppDrawOverlaySettingsActivity")) {
                //Navigate back to Skin playback activity
                driver.navigate().back();
                Thread.sleep(2000);

            }

            po.waitForPresenceOfText(driver, "4:3 Aspect Ratio");

            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.BasicPlaybackListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("Ooyala Skin - Basic PlayBack List Activity Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnText(driver, "4:3 Aspect Ratio");
            Thread.sleep(2000);

            logger.info("<<<<<Clicked on 4:3 Aspect Ratio Video>>>>>>");

            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.OoyalaSkinPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            po.waitForPresenceOfText(driver, "h");

            //Get coordinates and click on play button.
            po.getPlay(driver);
            Thread.sleep(1000);

            //Clicking on Play button in Ooyala Skin
            //po.clickBasedOnText(driver,"h");

            //Ad Started Verification
            EventVerification ev = new EventVerification();

            //Play Started
            ev.verifyEvent("playStarted", " Video Started to Play ", 30000);
            //Timeout for the duration of the video
            Thread.sleep(52000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.pausingVideo(driver);
            Thread.sleep(1000);

            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", "Video has been paused", 300000);
            Thread.sleep(1000);

            po.discoverUpNext(driver);
            Thread.sleep(1000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.getPlay(driver);
            Thread.sleep(1000);
            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 80000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            po.replayVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video replay start ", 70000);
            Thread.sleep(3000);

            po.screentap(driver);

            Thread.sleep(1000);

            // pausing video
            po.pauseVideo(driver);
            // verifing video get paused
            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", " Video paused ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(5000);

            //clicking on view area
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            Thread.sleep(1500);

            // clicking on more button
            po.moreButton(driver);

            Thread.sleep(2000);

            // clicking on Share button
            po.shareAsset(driver);

            logger.info("clicked on share button");

            Thread.sleep(2000);

            // sharing asset on gmail.
            po.shareOnGmail(driver);
            Thread.sleep(1000);

            ev.verifyEvent("stateChanged - state: READY", " Mail sent, Back to SDK ", 70000);
            Thread.sleep(2000);

            logger.info("clicking on discovery");
            po.clickOnDiscovery(driver);

            Thread.sleep(2000);

            po.clickOnCloseButton(driver);

            Thread.sleep(2000);

            logger.info("clicking on CC");
            po.clickOnCC(driver);

            Thread.sleep(2000);

            // closing more option
            po.clickOnCloseButton(driver);
            Thread.sleep(2000);
            po.clickOnCloseButton(driver);

            Thread.sleep(5000);

            // tapping on screen for get the scrubber bar and play/pause button
            po.screentap(driver);
            Thread.sleep(2000);


            // playing the video
            po.playVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video start ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);

            Thread.sleep(1000);

            po.powerKeyClick(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);
            Thread.sleep(45000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 100000);
            Thread.sleep(1000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            logger.info("<<<<<<<<<<<<<<Completed 4:3 Ascept ratio Asset playback>>>>>>>>>>>>>");

        } catch (Exception e) {
            logger.error("AspectRatio throws Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver, "AspectRatio");
            Assert.assertTrue(false, "This will fail!");
        }

    }

    @org.testng.annotations.Test
    public void MP4() throws Exception {
        try {

            // Creating an Object of FreeWheelSampleApp class
            exoPlayerSampleApp po = new exoPlayerSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.complete.MainExoPlayerActivity");
            // Wrire to console activity name of home screen app
            logger.debug("ExoPlayerApp Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);


            po.clickBasedOnText(driver, "Basic Playback");
            Thread.sleep(2000);

            logger.debug(" Print current activity name" + driver.currentActivity());
            if (driver.currentActivity().toString().equals(".Settings$AppDrawOverlaySettingsActivity")) {
                //Navigate back to Skin playback activity
                driver.navigate().back();
                Thread.sleep(2000);

            }

            po.waitForPresenceOfText(driver, "MP4 Video");

            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.BasicPlaybackListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("Ooyala Skin - Basic PlayBack List Activity Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnTextScrollTo(driver, "MP4 Video");
            Thread.sleep(2000);

            logger.info("<<<<<Clicked on MP4 Video>>>>>>");

            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.OoyalaSkinPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            po.waitForPresenceOfText(driver, "h");

            //Get coordinates and click on play button.
            po.getPlay(driver);
            Thread.sleep(1000);

            //Clicking on Play button in Ooyala Skin
            //po.clickBasedOnText(driver,"h");

            //Ad Started Verification
            EventVerification ev = new EventVerification();

            //Play Started
            ev.verifyEvent("playStarted", " Video Started to Play ", 30000);
            //Timeout for the duration of the video
            Thread.sleep(32000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.pausingVideo(driver);
            Thread.sleep(1000);

            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", "Video has been paused", 300000);
            Thread.sleep(1000);

            po.discoverUpNext(driver);
            Thread.sleep(1000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.getPlay(driver);
            Thread.sleep(2000);
            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 80000);
            Thread.sleep(1000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            po.replayVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video replay start ", 70000);
            Thread.sleep(3000);

            po.screentap(driver);

            Thread.sleep(1000);

            // pausing video
            po.pauseVideo(driver);
            // verifing video get paused
            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", " Video paused ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(5000);

            //clicking on view area
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            Thread.sleep(1500);

            // clicking on more button
            po.moreButton(driver);

            Thread.sleep(2000);

            // clicking on Share button
            po.shareAsset(driver);

            logger.info("clicked on share button");

            Thread.sleep(2000);

            // sharing asset on gmail.
            po.shareOnGmail(driver);
            Thread.sleep(1000);

            ev.verifyEvent("stateChanged - state: READY", " Mail sent, Back to SDK ", 70000);
            Thread.sleep(2000);

            logger.info("clicking on discovery");
            po.clickOnDiscovery(driver);

            Thread.sleep(2000);

            po.clickOnCloseButton(driver);

            Thread.sleep(2000);

            logger.info("clicking on CC");
            po.clickOnCC(driver);

            Thread.sleep(2000);

            // closing more option
            po.clickOnCloseButton(driver);
            Thread.sleep(2000);
            po.clickOnCloseButton(driver);

            Thread.sleep(5000);

            // tapping on screen for get the scrubber bar and play/pause button
            po.screentap(driver);
            Thread.sleep(1000);


            // playing the video
            po.playVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video start ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);

            Thread.sleep(1000);

            po.powerKeyClick(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);
            Thread.sleep(30000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 100000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            logger.info("<<<<<<<<<<<<<<Completed MP4 Video Asset playback>>>>>>>>>>>>>");


        } catch (Exception e) {
            logger.error("MP4 Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver, "MP4");
            Assert.assertTrue(false, "This will fail!");
        }

    }

    @org.testng.annotations.Test
    public void HLS() throws Exception {
        try {

            // Creating an Object of FreeWheelSampleApp class
            exoPlayerSampleApp po = new exoPlayerSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.complete.MainExoPlayerActivity");
            // Wrire to console activity name of home screen app
            logger.debug("ExoPlayerApp Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);


            po.clickBasedOnText(driver, "Basic Playback");
            Thread.sleep(2000);

            logger.debug(" Print current activity name" + driver.currentActivity());
            if (driver.currentActivity().toString().equals(".Settings$AppDrawOverlaySettingsActivity")) {
                //Navigate back to Skin playback activity
                driver.navigate().back();
                Thread.sleep(2000);

            }
            po.waitForPresenceOfText(driver, "HLS Video");

            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.BasicPlaybackListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("Ooyala Skin - Basic PlayBack List Activity Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnTextScrollTo(driver, "HLS Video");
            Thread.sleep(2000);

            logger.info("<<<<<Clicked on HLS Video>>>>>>");

            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.OoyalaSkinPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            po.waitForPresenceOfText(driver, "h");

            //Get coordinates and click on play button.
            po.getPlay(driver);
            Thread.sleep(1000);

            //Clicking on Play button in Ooyala Skin
            //po.clickBasedOnText(driver,"h");

            //Ad Started Verification
            EventVerification ev = new EventVerification();

            //Play Started
            ev.verifyEvent("playStarted", " Video Started to Play ", 30000);
            //Timeout for the duration of the video
            Thread.sleep(32000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.pausingVideo(driver);
            Thread.sleep(1000);

            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", "Video has been paused", 300000);
            Thread.sleep(1000);

            po.discoverUpNext(driver);
            Thread.sleep(1000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.getPlay(driver);
            Thread.sleep(2000);
            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 80000);
            Thread.sleep(1000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            po.replayVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video replay start ", 70000);
            Thread.sleep(3000);

            po.screentap(driver);

            Thread.sleep(1000);

            // pausing video
            po.pauseVideo(driver);
            // verifing video get paused
            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", " Video paused ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(5000);

            //clicking on view area
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            Thread.sleep(1500);

            // clicking on more button
            po.moreButton(driver);

            Thread.sleep(2000);

            // clicking on Share button
            po.shareAsset(driver);

            logger.info("clicked on share button");

            Thread.sleep(2000);

            // sharing asset on gmail.
            po.shareOnGmail(driver);
            Thread.sleep(1000);

            ev.verifyEvent("stateChanged - state: READY", " Mail sent, Back to SDK ", 70000);
            Thread.sleep(2000);

            logger.info("clicking on discovery");
            po.clickOnDiscovery(driver);

            Thread.sleep(2000);

            po.clickOnCloseButton(driver);

            Thread.sleep(2000);

            logger.info("clicking on CC");
            po.clickOnCC(driver);

            Thread.sleep(2000);

            // closing more option
            po.clickOnCloseButton(driver);
            Thread.sleep(2000);
            po.clickOnCloseButton(driver);

            Thread.sleep(5000);

            // tapping on screen for get the scrubber bar and play/pause button
            po.screentap(driver);
            Thread.sleep(1000);


            // playing the video
            po.playVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video start ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);

            Thread.sleep(2000);

            po.powerKeyClick(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);
            Thread.sleep(30000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 100000);
            Thread.sleep(1000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            logger.info("<<<<<<<<<<<<<<Completed HLS Video Asset playback>>>>>>>>>>>>>");
        } catch (Exception e) {
            logger.error("HLS throws Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver, "HLS");
            Assert.assertTrue(false, "This will fail!");
        }

    }

    @org.testng.annotations.Test
    public void encrypted_HLS() throws Exception {
        try {

            // Creating an Object of FreeWheelSampleApp class
            exoPlayerSampleApp po = new exoPlayerSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.complete.MainExoPlayerActivity");
            // Wrire to console activity name of home screen app
            logger.debug("ExoPlayerApp Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);

            po.clickBasedOnText(driver, "Basic Playback");
            Thread.sleep(2000);

            logger.debug(" Print current activity name" + driver.currentActivity());
            if (driver.currentActivity().toString().equals(".Settings$AppDrawOverlaySettingsActivity")) {
                //Navigate back to Skin playback activity
                driver.navigate().back();
                Thread.sleep(2000);

            }

            po.waitForPresenceOfText(driver, "4:3 Aspect Ratio");
            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.BasicPlaybackListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("Ooyala Skin - Basic PlayBack List Activity Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnTextScrollTo(driver, "Ooyala Encrypted HLS");
            Thread.sleep(2000);


            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.OoyalaSkinPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            po.waitForPresenceOfText(driver, "h");


            logger.info("<<<<<Clicked on Ooyala Encrypted HLS Video>>>>>>");

            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.OoyalaSkinPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            po.waitForPresenceOfText(driver, "h");

            //Get coordinates and click on play button.
            po.getPlay(driver);
            Thread.sleep(1000);

            //Clicking on Play button in Ooyala Skin
            //po.clickBasedOnText(driver,"h");

            //Ad Started Verification
            EventVerification ev = new EventVerification();

            //Play Started
            ev.verifyEvent("playStarted", " Video Started to Play ", 30000);
            //Timeout for the duration of the video
            Thread.sleep(22000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.pausingVideo(driver);
            Thread.sleep(1000);

            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", "Video has been paused", 300000);
            Thread.sleep(1000);

            po.discoverUpNext(driver);
            Thread.sleep(1000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.getPlay(driver);
            Thread.sleep(2000);
            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 80000);
            Thread.sleep(1000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            po.replayVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video replay start ", 70000);
            Thread.sleep(3000);

            po.screentap(driver);

            Thread.sleep(1000);

            // pausing video
            po.pauseVideo(driver);
            // verifing video get paused
            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", " Video paused ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(5000);

            //clicking on view area
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            Thread.sleep(1500);

            // clicking on more button
            po.moreButton(driver);

            Thread.sleep(2000);

            // clicking on Share button
            po.shareAsset(driver);

            logger.info("clicked on share button");

            Thread.sleep(2000);

            // sharing asset on gmail.
            po.shareOnGmail(driver);
            Thread.sleep(1000);

            ev.verifyEvent("stateChanged - state: READY", " Mail sent, Back to SDK ", 70000);
            Thread.sleep(2000);

            logger.info("clicking on discovery");
            po.clickOnDiscovery(driver);

            Thread.sleep(2000);

            po.clickOnCloseButton(driver);

            Thread.sleep(2000);

            logger.info("clicking on CC");
            po.clickOnCC(driver);

            Thread.sleep(2000);

            // closing more option
            po.clickOnCloseButton(driver);
            Thread.sleep(2000);
            po.clickOnCloseButton(driver);

            Thread.sleep(5000);

            // tapping on screen for get the scrubber bar and play/pause button
            po.screentap(driver);
            Thread.sleep(1000);


            // playing the video
            po.playVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video start ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);

            Thread.sleep(1000);

            po.powerKeyClick(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);
            Thread.sleep(15000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 100000);
            Thread.sleep(1000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            logger.info("<<<<<<<<<<<<<<Completed Ooyala Encrypted HLS Video Asset playback>>>>>>>>>>>>>");

        } catch (Exception e) {
            logger.error("encrypted_HLS Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver, "encrypted_HLS");
            Assert.assertTrue(false, "This will fail!");
        }

    }

    @org.testng.annotations.Test
    public void VOD_CC() throws Exception {
        try {

            // Creating an Object of FreeWheelSampleApp class
            exoPlayerSampleApp po = new exoPlayerSampleApp();
            // wait till home screen of basicPlayBackApp is opened
            po.waitForAppHomeScreen(driver);


            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.complete.MainExoPlayerActivity");
            // Wrire to console activity name of home screen app
            logger.debug("ExoPlayerApp Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            //Pause the running of test for a brief time .
            Thread.sleep(3000);


            po.clickBasedOnText(driver, "Basic Playback");
            Thread.sleep(2000);

            logger.debug(" Print current activity name" + driver.currentActivity());
            if (driver.currentActivity().toString().equals(".Settings$AppDrawOverlaySettingsActivity")) {
                //Navigate back to Skin playback activity
                driver.navigate().back();
                Thread.sleep(2000);

            }

            po.waitForPresenceOfText(driver, "4:3 Aspect Ratio");
            // Assert if current activity is indeed equal to the activity name of app home screen
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.BasicPlaybackListActivity");
            // Wrire to console activity name of home screen app
            logger.debug("Ooyala Skin - Basic PlayBack List Activity Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Select one of the video HLS,MP4 etc .
            po.clickBasedOnTextScrollTo(driver, "VOD with CCs");
            Thread.sleep(2000);

            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.OoyalaSkinPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            po.waitForPresenceOfText(driver, "h");


            logger.info("<<<<<Clicked on VOD with CCs Video>>>>>>");

            //verify if player was loaded
            po.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            po.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.OoyalaSkinPlayerActivity");
            // Print to console output current player activity
            logger.debug("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            po.waitForPresenceOfText(driver, "h");

            //Get coordinates and click on play button.
            po.getPlay(driver);
            Thread.sleep(1000);

            //Clicking on Play button in Ooyala Skin
            //po.clickBasedOnText(driver,"h");

            //Ad Started Verification
            EventVerification ev = new EventVerification();

            //Play Started
            ev.verifyEvent("playStarted", " Video Started to Play ", 30000);
            //Timeout for the duration of the video
            Thread.sleep(32000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.pausingVideo(driver);
            Thread.sleep(1000);

            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", "Video has been paused", 300000);
            Thread.sleep(1000);

            po.discoverUpNext(driver);
            Thread.sleep(1000);

            po.screentapping(driver);
            Thread.sleep(1000);

            po.getPlay(driver);
            Thread.sleep(2000);
            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 80000);
            Thread.sleep(1000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            po.replayVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video replay start ", 70000);
            Thread.sleep(3000);

            po.screentap(driver);

            Thread.sleep(1000);

            // pausing video
            po.pauseVideo(driver);
            // verifing video get paused
            ev.verifyEvent("Notification Received: stateChanged - state: PAUSED", " Video paused ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(2000);

            po.powerKeyClick(driver);

            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: READY", "Now player is ready", 50000);

            Thread.sleep(5000);

            //clicking on view area
            WebElement viewarea = driver.findElementByClassName("android.view.View");
            viewarea.click();
            Thread.sleep(1500);

            // clicking on more button
            po.moreButton(driver);

            Thread.sleep(2000);

            // clicking on Share button
            po.shareAsset(driver);

            logger.info("clicked on share button");

            Thread.sleep(2000);

            // sharing asset on gmail.
            po.shareOnGmail(driver);
            Thread.sleep(1000);

            ev.verifyEvent("stateChanged - state: READY", " Mail sent, Back to SDK ", 70000);
            Thread.sleep(2000);

            logger.info("clicking on discovery");
            po.clickOnDiscovery(driver);

            Thread.sleep(2000);

            po.clickOnCloseButton(driver);

            Thread.sleep(2000);

            logger.info("clicking on CC");
            po.clickOnCC(driver);

            Thread.sleep(2000);

            // closing more option
            po.clickOnCloseButton(driver);
            Thread.sleep(2000);
            po.clickOnCloseButton(driver);

            Thread.sleep(5000);

            // tapping on screen for get the scrubber bar and play/pause button
            po.screentap(driver);
            Thread.sleep(1000);


            // playing the video
            po.playVideo(driver);
            ev.verifyEvent("stateChanged - state: PLAYING", " Video start ", 70000);

            Thread.sleep(3000);

            po.getBackFromRecentApp(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);

            Thread.sleep(1000);

            po.powerKeyClick(driver);
            // verifing event that player has been get ready
            ev.verifyEvent("stateChanged - state: PLAYING", "Now player is ready", 60000);
            Thread.sleep(30000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed Play ", 100000);
            Thread.sleep(1000);

            po.discoveryTray(driver);
            Thread.sleep(3000);

            logger.info("<<<<<<<<<<<<<<Completed VOD with CCs Asset playback>>>>>>>>>>>>>");

        } catch (Exception e) {
            logger.error("VOD_CC throws Exception " + e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver, "VOD_CC");
            Assert.assertTrue(false, "This will fail!");
        }
    }
}

package testpackage.tests.IMASampleApp;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import testpackage.pageobjects.IMASampleApp;
import testpackage.utils.*;
import java.util.Properties;
import java.io.IOException;



public class BasicTests extends EventLogTest{

    IMASampleApp imaSampleApp = new IMASampleApp();
    //creating the object of loadpropertyvalues class
    LoadPropertyValues prop = new LoadPropertyValues();
    //creating the object of properties class
    Properties p;


    @BeforeClass
    public void beforeTest() throws Exception {
        // closing all recent app from background.
        CloserecentApps.closeApps();

        System.out.println("BeforeTest \n");

        System.out.println(System.getProperty("user.dir"));
        // Get Property Values
        
        p=prop.loadProperty("IMASampleapp.properties");

        System.out.println("Device id from properties file " + p.getProperty("deviceName"));
        System.out.println("PortraitMode from properties file " + p.getProperty("PortraitMode"));
        System.out.println("Path where APK is stored"+ p.getProperty("appDir"));
        System.out.println("APK name is "+ p.getProperty("appValue"));
        System.out.println("Platform under Test is "+ p.getProperty("platformName"));
        System.out.println("Mobile OS Version is "+ p.getProperty("OSVERSION"));
        System.out.println("Package Name of the App is "+ p.getProperty("appPackage"));
        System.out.println("Activity Name of the App is "+ p.getProperty("appActivity"));

        SetUpAndroidDriver setUpdriver = new SetUpAndroidDriver();
        driver = setUpdriver.setUpandReturnAndroidDriver(p.getProperty("udid"), p.getProperty("appDir"), p.getProperty("appValue"), p.getProperty("platformName"), p.getProperty("platformVersion"), p.getProperty("appPackage"), p.getProperty("appActivity"));
        Thread.sleep(2000);
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        System.out.println("beforeMethod \n");
        driver.manage().logs().get("logcat");
        PushLogFileToDevice logpush=new PushLogFileToDevice();
        logpush.pushLogFile();
        if(driver.currentActivity()!= "com.ooyala.sample.lists.IMAListActivity") {
            driver.startActivity("com.ooyala.sample.IMASampleApp","com.ooyala.sample.lists.IMAListActivity");
        }

        // Get Property Values
        p=prop.loadProperty();
        System.out.println(" Screen Mode "+ p.getProperty("ScreenMode"));
    }

    @AfterClass
    public void afterTest() throws InterruptedException, IOException {
        System.out.println("AfterTest \n");
        driver.closeApp();
        driver.quit();

    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws Exception {
        // Waiting for all the events from sdk to come in .
        System.out.println("AfterMethod \n");
        //ScreenshotDevice.screenshot(driver);
        RemoveEventsLogFile.removeEventsFileLog();
        Thread.sleep(10000);

    }

    @Test
    public void imaAdRulePreroll() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Select one of the video HLS,MP4 etc .
            imaSampleApp.clickBasedOnText(driver, "IMA Ad-Rules Preroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");
            //Clicking on play button
            System.out.println("Now will play in normal screen");
            imaSampleApp.playInNormalScreen(driver);

            //ad Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("adStarted", " Ad Started to Play ", 10000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);
            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Verifying pause event
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 35000);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            // SeekCompleted event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 40000);

            // If loading spinner is displaying then handling it.
            imaSampleApp.loadingSpinner(driver);

            // Resuing the playback
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 90000);
        }
        catch(Exception e)
        {
            System.out.println("IMAAdRulePreroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAAdRulePreroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void imaAdRuleMidroll() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting IMA Ad rules pre roll asset
            imaSampleApp.clickBasedOnText(driver, "IMA Ad-Rules Midroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //playStarted event verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            // Sleep for 3 second
            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            // pause event verification
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 40000);

            // Reading the pause time
            imaSampleApp.readTime(driver);

            // seeking the video
            imaSampleApp.seekVideo(driver);
            //Seek event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 50000);

            //Handling the loading spinner if displayes
            imaSampleApp.loadingSpinner(driver);

            //Reading the time after seek the video
            imaSampleApp.readTime(driver);

            //Starting playback again, after pause and seek
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",60000);

            //Midroll ad playback started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 75000);

            //Ad playback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 80000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 90000);
        }
        catch(Exception e)
        {
            System.out.println("IMAAdRuleMidroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAAdRuleMidroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void imaAdRulePostroll() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA Postroll asset
            imaSampleApp.clickBasedOnText(driver, "IMA Ad-Rules Postroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 10000);

            // Sleep for 3 second before clicking on pause button
            Thread.sleep(3000);

            //Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Pause event verification
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 25000);

            //Reading the time at the time of pause of video
            imaSampleApp.readTime(driver);

            //Seeking the video
            imaSampleApp.seekVideo(driver);
            //Seek Completed event verification after seek the video
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 55000);

            //Handling the loading spinner if it displays after the seek
            imaSampleApp.loadingSpinner(driver);

            //Reading the time after pause and seek the video
            imaSampleApp.readTime(driver);

            //Resuming the playback
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            // Postroll ad playback started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 90000);

            //Postroll ad playback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 100000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 150000);
        }
        catch(Exception e)
        {
            System.out.println("IMAAdRulePostroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAAdRulePostroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void imaPoddedPreroll() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA Podded Preroll asset
            imaSampleApp.clickBasedOnText(driver, "IMA Podded Preroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            // Preroll adplayback start event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 10000);

            // Preroll adplayback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 20000);

            // Preroll adplayback start event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ",20000);

            // Preroll adplayback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);
            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Verifying pause event
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 35000);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            // SeekCompleted event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 40000);

            // If loading spinner is displaying then handling it.
            imaSampleApp.loadingSpinner(driver);

            // Resuming the playback
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 90000);
        }
        catch(Exception e)
        {
            System.out.println("IMAPoddedPreroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAPoddedPreroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void IMAPoddedMidroll() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting IMA Podded Midroll asset
            imaSampleApp.clickBasedOnText(driver, "IMA Podded Midroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            //Sleep for 3 second before click on pause button
            Thread.sleep(3000);

            //clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            // verifying the pause event after click on pause button
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 40000);
            // Readin the time after pasue the video
            imaSampleApp.readTime(driver);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            // Verifying the seek completed event after seek the video
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 50000);

            //Handling the loading spinner after seek the video, If displayed
            imaSampleApp.loadingSpinner(driver);

            // Reading the time after pause and seek the video
            imaSampleApp.readTime(driver);

            // Resuming the playback after seek the video
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",60000);

            // Midroll adplayback completed event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 65000);
            // Midroll adplayback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 75000);

            // Midroll adplayback start event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 75000);
            // Midroll adplayback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 85000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 90000);
        }
        catch(Exception e)
        {
            System.out.println("IMAPoddedMidroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAPoddedMidroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void imaPoddedPostroll() throws Exception{

        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA Podded Postroll asset
            imaSampleApp.clickBasedOnText(driver, "IMA Podded Postroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            // 3 Second sleep before clicking on pause button
            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            // Video playback pause event verification
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 35000);

            //Reading the time after pause the video
            imaSampleApp.readTime(driver);
            // Seeking the video
            imaSampleApp.seekVideo(driver);
            // After seek the video verifying the seek completed event
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 55000);

            // Handling the loading spinner after seek the video, If Displayed
            imaSampleApp.loadingSpinner(driver);
            // Reading the time after seek the video
            imaSampleApp.readTime(driver);

            // Resuming the playback after pause and seek functionality
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            // Postroll adplayback start event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 90000);
            // Postroll adplayback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 100000);

            // Postroll adplayback start event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 90000);
            // Postroll adplayback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 100000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 150000);
        }
        catch(Exception e)
        {
            System.out.println("IMAPoddedPostroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAPoddedPostroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void imaPoddedPreMidPost() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA Podded PreMidPost roll asset
            imaSampleApp.clickBasedOnText(driver, "IMA Podded Pre-Mid-Post");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            // verify preroll adStarted event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 10000);

            // verify preroll adCompleted event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 20000);

            // verify preroll adStarted event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 20000);

            // verify preroll adCompleted event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            // verify preroll adStarted event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 30000);

            // verify preroll adCompleted event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 40000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 40000);
            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Verifying pause event
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 45000);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            // SeekCompleted event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 50000);

            // If loading spinner is displaying then handling it.
            imaSampleApp.loadingSpinner(driver);

            // Resuing the playback
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            // verify Midroll adStarted event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 60000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 70000);

            // verify Midroll adStarted event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 70000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 80000);

            // verify Midroll adStarted event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 80000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 90000);

            ev.verifyEvent("adStarted", " Ad Started to Play ", 100000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 100000);

            ev.verifyEvent("adStarted", " Ad Started to Play ", 120000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 130000);

            ev.verifyEvent("adStarted", " Ad Started to Play ", 140000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 150000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 160000);
        }
        catch(Exception e)
        {
            System.out.println("IMAPoddedPreMidPost throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAPoddedPreMidPost");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void imaSkippable() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA Skippable asset
            imaSampleApp.clickBasedOnText(driver, "IMA Skippable");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Preroll ad Playback started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 10000);

            //Preroll adplayback event completed verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 50000);

            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Verifying pause event
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 35000);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            // SeekCompleted event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 40000);

            // If loading spinner is displaying then handling it.
            imaSampleApp.loadingSpinner(driver);

            // Resuing the playback
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);
            // Postroll adPlayback started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 70000);

            //Postroll adplayback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 90000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 100000);
        }
        catch(Exception e)
        {
            System.out.println("IMASkippable throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMASkippable");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void imaPreMidPostSkippable() throws Exception{
        try {
            // wait till home screen of imaSampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA PreMidPost skipable asset
            imaSampleApp.clickBasedOnText(driver, "IMA Pre, Mid and Post Skippable");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Verifying Preroll AdPlayback Started
            ev.verifyEvent("adStarted", " Ad Started to Play ", 10000);

            // Preroll Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            // midroll adplayback started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 40000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 50000);

            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Verifying pause event
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 55000);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            // SeekCompleted event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 60000);

            // If loading spinner is displaying then handling it.
            imaSampleApp.loadingSpinner(driver);

            // Resuing the playback
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",70000);

            //Postroll ad playback event started verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 80000);

            // Postroll Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 90000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 100000);
        }
        catch(Exception e)
        {
            System.out.println("IMAPreMidPostSkippable throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAPreMidPostSkippable");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void imaApplicationConfigured() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA Application configured asset
            imaSampleApp.clickBasedOnText(driver, "IMA Application-Configured");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.CustomConfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on the playbutton
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            // 3 Second sleep before click on pause button
            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Verifying pause event
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 60000);

            // Reading the time after pause the video
            imaSampleApp.readTime(driver);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            //Verifying the seek completed event after seek the video.
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 70000);

            // Handling the loading spinner after seek the video, If displayed
            imaSampleApp.loadingSpinner(driver);

            //Reading the time after pause and seek the video
            imaSampleApp.readTime(driver);

            // Resuming the video
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            //Verifying the Ad playback event started
            ev.verifyEvent("adStarted", " Ad Started to Play ", 55000);

            // adCompleted event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 65000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 90000);

        }
        catch(Exception e)
        {
            System.out.println("IMAApplicationConfigured throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMAApplicationConfigured");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void imaNo_AdRules() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the No Ads Asset
            imaSampleApp.clickBasedOnTextScrollTo(driver, "No Ads");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");
            //Clicking on play button
            System.out.println("Now will play in normal screen");
            imaSampleApp.playInNormalScreen(driver);

            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);
            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Verifying pause event
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 35000);

            // Seeking the video
            imaSampleApp.seekVideoForLong(driver);
            // SeekCompleted event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 40000);

            // If loading spinner is displaying then handling it.
            imaSampleApp.loadingSpinner(driver);

            // Resuing the playback
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 90000);
        }
        catch(Exception e)
        {
            System.out.println("imaNon_AdRules throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"imaNon_AdRules");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void ima_NonAdRulePreroll() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA Non AdRules Preroll
            imaSampleApp.clickBasedOnTextScrollTo(driver, "IMA Non Ad-Rules Preroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");
            //Clicking on play button
            System.out.println("Now will play in normal screen");
            imaSampleApp.playInNormalScreen(driver);

            //ad Started Verification
            EventVerification ev = new EventVerification();
            ev.verifyEvent("adStarted", " Ad Started to Play ", 10000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);
            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Verifying pause event
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 35000);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            // SeekCompleted event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 40000);

            // If loading spinner is displaying then handling it.
            imaSampleApp.loadingSpinner(driver);

            // Resuing the playback
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 90000);
        }
        catch(Exception e)
        {
            System.out.println("ima_NonAdRulePreroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"ima_NonAdRulePreroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void ima_NonAdRuleMidroll() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA Non AdRules Midroll asset
            imaSampleApp.clickBasedOnTextScrollTo(driver, "IMA Non Ad-Rules Midroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(3000);

            imaSampleApp.pauseInNormalScreen(driver);
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 60000);

            imaSampleApp.readTime(driver);

            imaSampleApp.seekVideo(driver);
            // Verifying the seek completed event
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 70000);

            // handling the loading spinner after seek the video if it displayed
            imaSampleApp.loadingSpinner(driver);


            // Reading the time after pause and seek the video
            imaSampleApp.readTime(driver);

            // Resuming the playback after pause and seek the video
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);


            // Midroll adPlayback Started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 45000);

            // midroll adplayback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 50000);


            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 90000);
        }
        catch(Exception e)
        {
            System.out.println("ima_NonAdRuleMidroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"ima_NonAdRuleMidroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void ima_NonAdRulePostroll() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");


            // Selecting the IMA non Ad Rules Postroll asset
            imaSampleApp.clickBasedOnTextScrollTo(driver, "IMA Non Ad-Rules Postroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");
            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(3000);

            //Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            // Verifying the pause event after pause the video
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 35000);

            //Reading the time after pause the video
            imaSampleApp.readTime(driver);

            //Seeking the video after pasue
            imaSampleApp.seekVideo(driver);
            // Verifying the seek event
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 55000);

            // Handling the loading spinner after seek the video
            imaSampleApp.loadingSpinner(driver);

            // Reading the time after pause and seek the video
            imaSampleApp.readTime(driver);

            // Resuming the playback after seek and pause
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            // Postroll Adplayback started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 90000);

            //Postroll adPlayback completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 100000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 150000);
        }
        catch(Exception e)
        {
            System.out.println("ima_NonAdRulePostroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"ima_NonAdRulePostroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void ima_NonAdRuleQuadMidroll() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Selecting the IMA Non AdRules Quad midroll asset.
            imaSampleApp.clickBasedOnTextScrollTo(driver, "IMA Non Ad-Rules Quad Midroll");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            ev.verifyEvent("playStarted", " Video Started Play ", 30000);

            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Pause event verificaion
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 40000);

            //Reading the time when clicking on pause button
            imaSampleApp.readTime(driver);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            //SeekCompleted event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 50000);

            //handling the loading spinner if displays, After seek the video
            imaSampleApp.loadingSpinner(driver);

            //Reading the time after the seek the video
            imaSampleApp.readTime(driver);

            //Starting the playback after pause and seek the video
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            // 1st adPlayback started out of 4 quad midroll
            ev.verifyEvent("adStarted", " Ad Started to Play ", 45000);

            // 1st adPlayback completed out of 4 quad midroll
            ev.verifyEvent("adCompleted", " Ad Completed ", 50000);

            // 2st adPlayback started out of 4 quad midroll
            ev.verifyEvent("adStarted", " Ad Started to Play ", 55000);

            // 2st adPlayback completed out of 4 quad midroll
            ev.verifyEvent("adCompleted", " Ad Completed ", 60000);

            // 3st adPlayback started out of 4 quad midroll
            ev.verifyEvent("adStarted", " Ad Started to Play ", 65000);

            // 3st adPlayback completed out of 4 quad midroll
            ev.verifyEvent("adCompleted", " Ad Completed ", 70000);

            // 4st adPlayback started out of 4 quad midroll
            ev.verifyEvent("adStarted", " Ad Started to Play ", 75000);

            // 4st adPlayback completed out of 4 quad midroll
            ev.verifyEvent("adCompleted", " Ad Completed ", 80000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 120000);
        }
        catch(Exception e)
        {
            System.out.println("IMA Non Ad-Rules Quad Midroll throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"IMA Non Ad-Rules Quad Midroll");
            Assert.assertTrue(false, "This will fail!");
        }
    }

    @Test
    public void ima_NonPreMidMidPost() throws Exception{
        try {
            // wait till home screen of IMASampleApp is opened
            imaSampleApp.waitForAppHomeScreen(driver);

            // Assert if current activity is indeed equal to the activity name of app home screen
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.lists.IMAListActivity");
            // Write to console activity name of home screen app
            System.out.println("IMASample App Launched successfully. Activity :- " + driver.currentActivity() + "\n");

            // Select one of the video HLS,MP4 etc .
            imaSampleApp.clickBasedOnTextScrollTo(driver, "IMA Non Ad-Rules Pre-Mid-Mid-Post");

            //verify if player was loaded
            imaSampleApp.waitForPresence(driver, "className", "android.view.View");
            // Assert if current activity is indeed equal to the activity name of the video player
            imaSampleApp.assertCurrentActivityAgainst(driver, "com.ooyala.sample.players.PreconfiguredIMAPlayerActivity");
            // Print to console output current player activity
            System.out.println("Player Video was loaded successfully . Activity  :- " + driver.currentActivity() + "\n");

            imaSampleApp.waitForPresence(driver,"className","android.widget.ImageButton");

            //Clicking on play button
            imaSampleApp.playInNormalScreen(driver);

            //Play Started Verification
            EventVerification ev = new EventVerification();

            // adPlayback started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 10000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 30000);


            //Wait for video to start and verify the playStarted event .
            ev.verifyEvent("playStarted", " Video Started Play ", 40000);
            Thread.sleep(3000);

            // Clicking on pause button
            imaSampleApp.pauseInNormalScreen(driver);
            //Verifying pause event
            ev.verifyEvent("stateChanged - state: PAUSED", " Playing Video Was Paused ", 35000);

            // Seeking the video
            imaSampleApp.seekVideo(driver);
            // SeekCompleted event verification
            ev.verifyEvent("seekCompleted", " Playing Video was Seeked " , 40000);

            // If loading spinner is displaying then handling it.
            imaSampleApp.loadingSpinner(driver);

            // Resuing the playback
            imaSampleApp.resumeInNormalScreen(driver);

            //Video resumed event verification
            ev.verifyEvent("stateChanged - state: PLAYING", "Video resumed",50000);

            // Midroll ad started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 60000);

            // Midroll Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 65000);

            // 2Midroll Ad Started event verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 70000);

            // 2MidrollAd completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 75000);

            //Postroll adPlayback event started verification
            ev.verifyEvent("adStarted", " Ad Started to Play ", 80000);

            // Ad completed event verification
            ev.verifyEvent("adCompleted", " Ad Completed ", 90000);

            //Wait for video to finish and verify the playCompleted event .
            ev.verifyEvent("playCompleted", " Video Completed  ", 100000);
        }
        catch(Exception e)
        {
            System.out.println("ima_NonPreMidMidPost throws Exception "+e);
            e.printStackTrace();
            ScreenshotDevice.screenshot(driver,"ima_NonPreMidMidPost");
            Assert.assertTrue(false, "This will fail!");
        }
    }


}

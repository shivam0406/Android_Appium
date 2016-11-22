package testpackage.pageobjects;

import io.appium.java_client.android.AndroidDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class NpawSampleApp {

    //WebElement playButton;
    int[] playCoordinates= new int[2];

    public void waitForAppHomeScreen(AndroidDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath("//android.widget.TextView[@text='4:3 Aspect Ratio']")));
    }

    public void waitForTextView(AndroidDriver driver, String text) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath("//android.widget.TextView[@text='" + text + "']")));

    }

    public void assertCurrentActivityAgainst(AndroidDriver driver, String activityName) {
        Assert.assertEquals(driver.currentActivity(), activityName);
    }

    public void clickBasedOnText(AndroidDriver driver, String clickText) {
        WebElement clickTextField = driver.findElement(By.xpath("//android.widget.TextView[@text='" + clickText + "']"));
        clickTextField.click();

    }

    // Find the asset with scroll the screen and click on that
    public void clickBasedOnTextScrollTo(AndroidDriver driver, String clickText){
        driver.scrollTo(clickText).click();
    }

    // Wait for element to be presence on the device screen
    public void waitForPresence(AndroidDriver driver, String typeOf, String waitString) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        if (typeOf == "className") {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className(waitString)));
        }
    }

    // Start the playback of the video in normal screen.
    public void playInNormalScreen(AndroidDriver driver) {
        int[] play = new int[2];
        List<WebElement> imageButtons = driver.findElements(By.xpath("//android.widget.ImageButton"));
        play[0]=imageButtons.get(0).getLocation().getX();
        play[1]=imageButtons.get(0).getLocation().getY();

        playCoordinates[0]=play[0]+imageButtons.get(0).getSize().getWidth()/2 ;
        playCoordinates[1]=play[1]+imageButtons.get(0).getSize().getHeight()/2 ;
        System.out.println("X playCoordinates"+playCoordinates[0]);
        System.out.println("Y playCoordinates"+playCoordinates[1]);
        driver.tap(1, playCoordinates[0] , playCoordinates[1], 2);
    }

    // Pause the video in normal screen mode.
    public void pauseInNormalScreen(AndroidDriver driver){
        System.out.println("X pauseCoordinates"+playCoordinates[0]);
        System.out.println("Y pauseCoordinates"+playCoordinates[1]);
        driver.tap(1, playCoordinates[0] , playCoordinates[1], 2);
    }

    // For seek the video.
    public void seekVideo(AndroidDriver driver){
        WebElement seekBarField = driver.findElement(By.xpath("//android.widget.SeekBar"));
        int seekBarFieldWidth = seekBarField.getLocation().getX();
        int seekBarFieldHeigth = seekBarField.getLocation().getY();
        System.out.println(" Dimensions bounds value is :-"+seekBarFieldHeigth);
        System.out.println(" Dimensions bounds value is :-"+seekBarFieldWidth);
        System.out.println(" Dimensions bounds value is :-"+seekBarField.getSize().getHeight());
        System.out.println(" Dimensions bounds value is :-"+seekBarField.getSize().getWidth());
        System.out.println(" Seeking -------------------------  ");
        driver.swipe(seekBarFieldWidth + 20, seekBarFieldHeigth, seekBarFieldWidth + 100, seekBarFieldHeigth, 3);
    }

    // Play the video after the pause mode.
    public void resumeInNormalScreen(AndroidDriver driver){
        System.out.println("X resumeCoordinates"+playCoordinates[0]);
        System.out.println("Y resumeCoordinates"+playCoordinates[1]);
        driver.tap(1, playCoordinates[0] , playCoordinates[1], 2);
    }

    // Click on CC button
    public void clickImagebuttons(AndroidDriver driver, int index) {
        List<WebElement> imageButtons = driver.findElements(By.xpath("//android.widget.ImageButton"));
        imageButtons.get(index).click();
    }

    // For click on radio button
    public void clickRadiobuttons(AndroidDriver driver, int index) {

        List<WebElement> radioButtons = driver.findElements(By.xpath("//android.widget.RadioButton"));
        radioButtons.get(index).click();
    }

    // For check the radio button is sellected or not.
    public boolean radioButtonChecked(AndroidDriver driver, int index) {

        List<WebElement> radioButtons = driver.findElements(By.xpath("//android.widget.RadioButton"));
        return radioButtons.get(index).isEnabled();
    }
}

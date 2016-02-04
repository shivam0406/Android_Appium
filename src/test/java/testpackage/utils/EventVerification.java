package testpackage.utils;

import org.junit.Assert;

/**
 * Created by bsondur on 11/30/15.
 */

//TODO:  add timeout variable to function signature
public class EventVerification {
    
    private int count ;

    public EventVerification(){
        count=0;
    }

    public void verifyEvent(String eventType,String consoleMessage,int timeout){
         
        int returncount;         

        // Paused  Verification
        boolean status=false;
        long startTime = System.currentTimeMillis(); //fetch starting time
        while(!status && (System.currentTimeMillis()-startTime)<timeout) {

            //status = ParseEventsFile.parseeventfile("stateChanged - state: PAUSED");
            ParseEventsFile pe=new ParseEventsFile();
            returncount = pe.parseeventfile(eventType,count);

            if (returncount== -1){
                status=false;
            }
            else{
                status=true;
                count=returncount;
            }            

            if (status == true) {
                System.out.println(consoleMessage);
                System.out.println("\n");
            }
        }
        if(!status){
            Assert.assertTrue(status);
        }
    }
}

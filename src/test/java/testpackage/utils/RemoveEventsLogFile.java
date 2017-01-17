package testpackage.utils;

import com.sun.jmx.snmp.Timestamp;
import org.apache.log4j.Logger;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.io.File;
import java.time.Instant;

/**
 * Created by bsondur on 11/30/15.
 */
// TOD0 : Check if file exists, only then , remove/delete the file , Check the return value

public class RemoveEventsLogFile {
    final static Logger logger = Logger.getLogger(RemoveEventsLogFile.class);

    public static void removeEventsFileLog()
    {
        try{
            String[] final_command = CommandLine.command("adb shell rm /sdcard/log.file");
            Runtime run=Runtime.getRuntime();
            Process pr = run.exec(final_command);

        }
        catch(Exception e)
        {
            logger.error("Exception is : "+e);
            e.printStackTrace();

        }
    }

}

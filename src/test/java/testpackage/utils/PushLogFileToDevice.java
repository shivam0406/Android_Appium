package testpackage.utils;

import java.io.IOException;

/**
 * Created by bsondur on 1/11/16.
 */
public class PushLogFileToDevice {



    public static void pushLogFile() throws IOException {
            String command = "adb push log.file /sdcard/";
            String[] final_command = CommandLine.command(command);
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(final_command);

    }


}



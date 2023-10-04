package dmitr.tutor.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogCore {

    private static boolean allowLogs = true;

    public static void log(String message, LogType logType) {
        if (!allowLogs)
            return;
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.printf("[%s] [%s] %s \n", timeStamp, logType.getCaption(), message);
    }

    public static void setAllowLogs(boolean value) {
        allowLogs = value;
    }

}

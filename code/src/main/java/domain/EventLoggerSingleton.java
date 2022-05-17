package domain;

import java.util.logging.Level;

public class EventLoggerSingleton {
    private static EventLoggerSingleton single_instance = null;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EventLoggerSingleton.class.getName());

    private EventLoggerSingleton(){}

    public static EventLoggerSingleton getInstance(){
        if(single_instance == null){
            single_instance = new EventLoggerSingleton();
        }
        return single_instance;
    }

    /**
     * write to the logger events
     * @param l - level flag
     * @param s - event message
     */
    public void logMsg(Level l, String s){
        logger.log(l,s);
    }
}

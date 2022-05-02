package domain;

import java.util.logging.Level;

public class ErrorLoggerSingleton {
    private static ErrorLoggerSingleton single_instance = null;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ErrorLoggerSingleton.class.getName());

    private ErrorLoggerSingleton(){}

    public static ErrorLoggerSingleton getInstance(){
        if(single_instance == null){
            single_instance = new ErrorLoggerSingleton();
        }
        return single_instance;
    }
    public void logMsg(Level l, String s) { synchronized (this) { logger.log(l, s); } }
}

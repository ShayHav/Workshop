package domain;

import java.util.logging.Level;

public class Logger_singleton {
    private static Logger_singleton single_instance = null;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger_singleton.class.getName());

    private Logger_singleton(){}

    public static Logger_singleton getInstance(){
        if(single_instance == null){
            single_instance = new Logger_singleton();
        }
        return single_instance;
    }
    public void logMsg(Level l, String s){
        logger.log(l,s);
    }
}

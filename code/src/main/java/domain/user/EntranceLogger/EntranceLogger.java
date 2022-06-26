package domain.user.EntranceLogger;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.market.MarketSystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class EntranceLogger {

    private List<Entrance> entrances;

    private static class EntranceHolder {
        private static final EntranceLogger logger = new EntranceLogger();
    }

    public static EntranceLogger getInstance() {
        return EntranceHolder.logger;
    }


    private EntranceLogger(){
        entrances = new ArrayList<>();
    }

    public void logEntrance(Entrance entrance){
        if(entrance != null)
            entrances.add(entrance);
        else{
           ErrorLoggerSingleton.getInstance().logMsg(Level.WARNING,"Tried to log entrance to market with null value. action failed");
        }
    }

    public List<Entrance> getEntrances(LocalDate from, LocalDate to){
        List<Entrance> result = entrances.stream().filter(e -> e.getDateOfEntrance().isEqual(from)||
                e.getDateOfEntrance().isAfter(from) ||
                e.getDateOfEntrance().isBefore(to) ||
                e.getDateOfEntrance().isEqual(to)).collect(Collectors.toList());
        return result;
    }

}

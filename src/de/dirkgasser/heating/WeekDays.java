package de.dirkgasser.heating;
/**
 * Classm for week day selection
 * @author Dirk Gasser
 * @version 1.0
 */
public class WeekDays {
    String[] weekDay;
    
    public WeekDays() {
        weekDay = new String[10]; 
        weekDay[1] = "Montag";
        weekDay[2] = "Dienstag";
        weekDay[3] = "Mittwoch";
        weekDay[4] = "Donnerstag";
        weekDay[5] = "Freitag";
        weekDay[6] = "Samstag";
        weekDay[7] = "Sonntag";
        weekDay[8] = "Wochentag";
        weekDay[9] = "Wochenende";
    }
    public String getNameOfDay (int dayNo) {
        return weekDay[dayNo];
    }
}

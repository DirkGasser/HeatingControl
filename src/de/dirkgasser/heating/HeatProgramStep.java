package de.dirkgasser.heating;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Program step for HeatingProgram <br>
 * Contains Temperature which are valid for a time periode <br>
 * @author Dirk Gasser
 * @version 1.0
 */
public class HeatProgramStep {
    private LocalTime startTime;
    private LocalTime endTime;
    double hereTemp;
    double absentTemp;
    private transient DateTimeFormatter dtf; 
    private transient NumberFormat numberFormat;
/**
 * creator for HeatProgramStep
 * @param startTime valid from time of temperatures
 * @param endTime valid to time of temperatures
 * @param hereTemp temperatur if movement is detected
 * @param absentTemp temperatur if no movements are detected
 */
    public HeatProgramStep(LocalTime startTime, LocalTime endTime, double hereTemp, double absentTemp) {
        this.absentTemp = absentTemp;
        this.endTime = endTime;
        this.hereTemp = hereTemp;
        this.startTime = startTime;
        dtf = DateTimeFormatter.ofPattern("HH:mm");
        numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
       ((DecimalFormat) numberFormat).applyPattern("###.#"); 
    }
/**
 * get valid from time of temperatures
 * @return valid from time of temperatures
 */
    public LocalTime getStartTime() {
        return startTime;
    }
/**
 * get valid from time as string
 * @return valid from time as string
 */
    public String getStartTimeAsString(){
        dtf = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(dtf);
    }
/**
 * get valid to time of temperatures
 * @param startTime valid from time of temperatures
 */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
/**
 * get valid to time of temperatures
 * @return valid to time of temperatures
 */
    public LocalTime getEndTime() {
        return endTime;
    }
/**
 * get valid to time as string
 * @return valid to time as string
 */
    public String getEndTimeAsString() {
        dtf = DateTimeFormatter.ofPattern("HH:mm");
        return endTime.format(dtf);
    }

/**
 * set valid to time of temperatures
 * @param endTime valid to time of temperatures
 */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
/**
 * get temp if movement is detected
 * @return hereTemp
 */
    public double getHereTemp() {
        return hereTemp;
    }

/**
 * get temp as string if movement is detected
 * @return temps as string
 */    
    public String gethHereTempAsString() {
        numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        return numberFormat.format(hereTemp) + " °C";
    }

    public void setHereTemp(double hereTemp) {
        this.hereTemp = hereTemp;
    }
    
    public void increaseHereTemp() {
        hereTemp = hereTemp + 0.5;
    }
    
     public void decreaseHereTemp() {
        hereTemp = hereTemp - 0.5;
    }

    public double getAbsentTemp() {
        return absentTemp;
    }
    
    public String getAbsenTempAsString() {
         numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
         return numberFormat.format(absentTemp) + " °C";
    }

    public void setAbsentTemp(double absentTemp) {
        this.absentTemp = absentTemp;
    }
    
    public void increaseAbsentTemp() {
        absentTemp = absentTemp + 0.5;
    }
     public void decreaseAbsentTemp() {
        absentTemp = absentTemp - 0.5;
    }
    
}

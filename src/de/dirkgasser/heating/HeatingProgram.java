package de.dirkgasser.heating;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to hold heating program 
 * @author Dirk Gasser
 * @version 1.0
 */
public class HeatingProgram {
    int noMovetoAbsent;
    int moveToBeThereMin;
    double defaultHereTemp;
    double defaultAbsentTemp;
    double alarmhumidity;
    HeatDay[] heatDay = new HeatDay[10];
    WeekDays weekDays;
    int currentDay;
    private static Object me;
    Integer queryPos;
    
/**
 * Create new HeatingProgram with default values
 */    
 private HeatingProgram () {
     noMovetoAbsent = 20;
     moveToBeThereMin = 20;
     defaultHereTemp = 21;
     defaultAbsentTemp = 18;
     alarmhumidity = 60;
     for(int i = 1; i < 10;i++) {
         heatDay[i] = new HeatDay();
     }
     weekDays = new WeekDays();
     currentDay = 1;
 }

/**
 * get daily heating steps of current date
 * @return HeatDay
 */
 public HeatDay getCurrentHeatDay() {
     return heatDay[currentDay];
 }
 /**
  * get daily heating steps of next date
  * @return HeatDay
  */
  public HeatDay getnextHeatDay() {
     if (currentDay < 9) {
         currentDay ++;
     } else {
         currentDay = 1;
     }
     return heatDay[currentDay];
 }
  /**
  * get daily heating steps of previous date
  * @return HeatDay
  */
  public HeatDay getprevHeatDay() {
     if (currentDay > 1) {
         currentDay --;
     } else {
         currentDay = 9;
     }
     return heatDay[currentDay];
 } 
/**
 * get name of Day
 * @return name of the day as String
 */ 
 public String getNameOfDay() {
     return weekDays.getNameOfDay(currentDay);
 }

    public double getDefaultHereTemp() {
        return defaultHereTemp;
    }

    public double getDefaultAbsentTemp() {
        return defaultAbsentTemp;
    }

    public HeatDay[] getHeatDay() {
        return heatDay;
    }

    public int getNoMovetoAbsent() {
        return noMovetoAbsent;
    }

    public int getMoveToBeThereMin() {
        return moveToBeThereMin;
    }

    public double getAlarmhumidity() {
        return alarmhumidity;
    }
    
    public void setNoMovetoAbsent(int noMovetoAbsent) {
        this.noMovetoAbsent = noMovetoAbsent;
    }

    public void setMoveToBeThereMin(int moveToBeThereMin) {
        this.moveToBeThereMin = moveToBeThereMin;
    }

    public void setDefaultHereTemp(double defaultHereTemp) {
        this.defaultHereTemp = defaultHereTemp;
    }

    public void setDefaultAbsentTemp(double defaultAbsentTemp) {
        this.defaultAbsentTemp = defaultAbsentTemp;
    }

    public void setAlarmhumidity(double alarmhumidity) {
        this.alarmhumidity = alarmhumidity;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }
 /**
  * read Heating Program from file 
  * @param programName
  * @return heating program
  */
 
 
 public static HeatingProgram getHeatingProgramFromFile (String programName) {
     if (me == null) {   
     
         try {
                File recFile = new File(System.getProperty("user.home") + File.separator + programName + ".tpg");
                if (recFile.exists() && recFile.isFile()) { 
                    Reader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + File.separator + programName + ".tpg"));
                    Gson gson = new GsonBuilder().create();
                    HeatingProgram program = gson.fromJson(reader, HeatingProgram.class);
                    me = program;
                    return program;
                } else { 
                    me = new HeatingProgram();
                    return (HeatingProgram) me;
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return new HeatingProgram ();
     } else {
         return (HeatingProgram) me;
     }        
    }
 
    public void writeHeatProgram (String programName) {
       Gson gson = new GsonBuilder().setPrettyPrinting().create();
       Writer writer;
        try {
            writer = new FileWriter(System.getProperty("user.home") + File.separator + programName + ".tpg");
            gson.toJson(this, writer);  
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(HeatingProgram.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
/**
 * get the Temperatur which is just now wanted if someone is present
 * @return here temperature
 */
    public double getMoveTempNow() {
        synchronized(this) {
            HeatProgramStep heatProgramstep = getHeatProgramStepNow();
            if (heatProgramstep != null) {
                return heatProgramstep.getHereTemp();
            } else {
                return defaultHereTemp;
            }  
        }
    }
 
 /**
  * get the Temperatur which is just now wanted if no one is present
  * @return absent temperatur
  */
    public double getAbsentTempNow() {
        synchronized(this) {
            HeatProgramStep heatProgramstep = getHeatProgramStepNow();
            if (heatProgramstep != null) {
                return heatProgramstep.getAbsentTemp();
            } else {
                return defaultAbsentTemp;
            }  
        }
    }
    
    public HeatProgramStep getHeatProgramStepNow() {
       int weekDay;
       queryPos = 0;
       weekDay = LocalDate.now().getDayOfWeek().getValue();
       HeatProgramStep compareStep;
       compareStep = heatDay[weekDay].getStep(0);
       while (compareStep != null &&
              compareStep.getEndTime().isBefore(LocalTime.now())) {
              queryPos ++;
              compareStep = heatDay[weekDay].getStep(queryPos);
        }
       if (compareStep != null &&
           compareStep.getStartTime().isBefore(LocalTime.now())) {
           return compareStep;
       }
       if (weekDay < 6) {
           weekDay = 8; // all working days
       } else {
           weekDay = 9; // weekend
       }
       queryPos = 0;
       compareStep = heatDay[weekDay].getStep(0);
       while (compareStep != null &&
              compareStep.getEndTime().isBefore(LocalTime.now())) {
              queryPos ++;
              compareStep = heatDay[weekDay].getStep(queryPos);
        }
       if (compareStep != null &&
           compareStep.getStartTime().isBefore(LocalTime.now())) {
           return compareStep;
       }
       return null;
    }
}

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
import java.util.ArrayList;
import java.util.List;
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
    
/**
 * Create new HeatingProgram with default values
 */    
 public HeatingProgram () {
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
 
 
 
 public static HeatingProgram getHeatingProgramFromFile (String programName) {
        try {
            File recFile = new File(System.getProperty("user.home") + File.separator + programName + ".tpg");
            if (recFile.exists() && recFile.isFile()) { 
                Reader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + File.separator + programName + ".tpg"));
                Gson gson = new GsonBuilder().create();
                HeatingProgram program = gson.fromJson(reader, HeatingProgram.class);
                return program;
            } else { 
                return new HeatingProgram ();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new HeatingProgram ();
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
}

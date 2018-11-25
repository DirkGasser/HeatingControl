package de.dirkgasser.heating;

import static de.dirkgasser.heating.HeatingControl.gpio;
import static de.dirkgasser.heating.HeatingControl.mainscreen;
import static de.dirkgasser.heating.HeatingControl.moveSensor;
import static de.dirkgasser.heating.HeatingControl.heatingProgram;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Threat to control Heating <br>
 *  Set time and temperatur on screen <br>
 *  switch on and off heating<br>
 *  here also instances of DHT11, Releais and Buzzer are created 
 * @author Dirk Gasser
 */
public class HeatingControlThread implements Runnable {
    LocalTime last;
    double actTemp;
    double actHumidity;
    double wantedTemp;
    Boolean started;
    Boolean humAlarm;
    DHT11 dht11;
    Relais heatPanel;
    Buzzer buzzer;
    static Boolean manual;
    static double manualTemp;
    static LocalTime alarmOff;
    
    public HeatingControlThread() {
        actTemp = 0;
        actHumidity = 0;
        started = false;

        try {
             dht11 = new DHT11(7);
        } catch (InstantiationException ex) {
            Logger.getLogger(HeatingControlThread.class.getName()).log(Level.SEVERE, null, ex);

        }
        
        heatPanel = new Relais(12, gpio);
        buzzer = new Buzzer(28,gpio);
        manual = false;
        manualTemp = 0;
        alarmOff = LocalTime.of(0, 0, 0);
        humAlarm = false;
    }
    @Override
    public void run() {
        if (!started) {
            started = true;
           invokeLoop(); 
        } else {  
          mainscreen.setTime(LocalTime.now());
          mainscreen.setTemp(actTemp);
          mainscreen.setHumidity(actHumidity);
          mainscreen.setTempToBe(wantedTemp);
          if (heatPanel.isOn()) {
              mainscreen.setHeaterSignal(true);
          } else {
              mainscreen.setHeaterSignal(false);
          }
          if (humAlarm) {
              mainscreen.setHumiditySignal(true);
          } else {
              mainscreen.setHumiditySignal(false);
          }
        }
    }
    protected void invokeLoop() {
              
        while (mainscreen != null) {
            if (dht11.getTemp() > 0) {
                actTemp = dht11.getTemp();
            }

            if (dht11.getHumidity()> 0) {
                actHumidity  = dht11.getHumidity();
            } 
            if (moveSensor.getMovement()) {
                if (!manual) {
                    wantedTemp = heatingProgram.getMoveTempNow();
                } else {
                    wantedTemp = manualTemp;
                }   
            } else {
                wantedTemp = heatingProgram.getAbsentTempNow();
                manual = false;
            }
            if (wantedTemp < actTemp) {
                heatPanel.setOff();
            }
            if (wantedTemp > actTemp + 0.1) {
                heatPanel.setOn();
            }
            if (actHumidity > heatingProgram.getAlarmhumidity()) {
                humAlarm = true;
                if (alarmOff.isBefore(LocalTime.now().minusMinutes(15))) {
                    buzzer.beep();
                }
            } else {
                humAlarm = false;
            }

            java.awt.EventQueue.invokeLater(this);
            try {
                Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HeatingControlThread.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("ich bin ex");
                }
        }
        heatPanel.setOff();
        buzzer.beep();
        System.out.println("...tschüss");
    }
    public static void setManualTemp(double temp) {
        manualTemp = temp;
        manual = true;
        moveSensor.setManualMove();
    }
    public static void setAlarmOff() {
        alarmOff = LocalTime.now();
    }
}

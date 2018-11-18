package de.dirkgasser.heating;

import static de.dirkgasser.heating.HeatingControl.gpio;
import static de.dirkgasser.heating.HeatingControl.mainscreen;
import static de.dirkgasser.heating.HeatingControl.moveSensor;
import static de.dirkgasser.heating.HeatingControl.heatingProgram;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Threat to control Heating 
 *  Set time and temperatur on screen
 *  switch on and off heating
 * @author Dirk Gasser
 */
public class HeatingControlThread implements Runnable {
    LocalTime last;
    double actTemp;
    double actHumidity;
    double wantedTemp;
    Boolean started;
    DHT11 dht11;
    Relais heatPanel;
    
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
        }
    }
    protected void invokeLoop() {
              
        while (mainscreen.isActive()) {
            if (dht11.getTemp() > 0) {
                actTemp = dht11.getTemp();
            }

            if (dht11.getHumidity()> 0) {
                actHumidity  = dht11.getHumidity();
            } 
            if (moveSensor.getMovement()) {
                wantedTemp = heatingProgram.getMoveTempNow();
            } else {
                wantedTemp = heatingProgram.getAbsentTempNow();
            }
            if (wantedTemp < actTemp) {
                heatPanel.setOff();
                System.out.println("off");
            }
            if (wantedTemp > actTemp + 0.1) {
                heatPanel.setOn();
            }

            java.awt.EventQueue.invokeLater(this);
            try {
                Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HeatingControlThread.class.getName()).log(Level.SEVERE, null, ex);
                }
        } 
//         java.awt.EventQueue.invokeLater(this); //to stop heater
    }
}

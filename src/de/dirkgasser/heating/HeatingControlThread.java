package de.dirkgasser.heating;

import static de.dirkgasser.heating.HeatingControl.mainscreen;
import java.time.Instant;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.dirkgasser.heizung.DHT11c;

/**
 * Threat to control Heating 
 *  Set time and temperatur on screen
 *  switch on and off heating
 * @author Dirk Gassr
 */
public class HeatingControlThread implements Runnable {
    LocalTime last;
    double actTemp;
    double actHumidity;
    Boolean invoked;
    DHT11 dht11;
    
    public HeatingControlThread() {
        actTemp = 0;
        actHumidity = 0;
        invoked = false;

        try {
             dht11 = new DHT11(7);
        } catch (InstantiationException ex) {
            Logger.getLogger(HeatingControlThread.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
    @Override
    public void run() {
        if (!invoked) {
           invokeLoop(); 
        } else {  
          mainscreen.setTime(LocalTime.now());
          mainscreen.setTemp(actTemp);
          mainscreen.setHumidity(actHumidity);
          invoked = false;
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
            if (!invoked) {
                invoked = true;
                java.awt.EventQueue.invokeLater(this);
            }
            try {
                Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HeatingControlThread.class.getName()).log(Level.SEVERE, null, ex);
                }
        } 
//         java.awt.EventQueue.invokeLater(this); //to stop heater
    }
}

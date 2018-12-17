package de.dirkgasser.heating;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Set 5 time 300ms signal on GPIO pin for Buzzer beep <br>
 * in a new threat so that workflow isn't blocked
 * @author Dirk Gasser
 */
public class Buzzer {
    private GpioController gpio;
    private GpioPinDigitalOutput pinoutd;
    private int pin;
/**
 * Create Buzzer instance with PIN as WPI from GPIO readall command
 * @param pin 
 */    
    public Buzzer (int pin) {
        this.pin = pin;
        gpio = GpioFactory.getInstance();   
        pinoutd = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin));
        pinoutd.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF); 
    }
    public Buzzer (int pin, GpioController gpio) {
        this.pin = pin;
        this.gpio = gpio;  
        pinoutd = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin));
        pinoutd.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF); 
    }
    
    public void beep() {
        System.out.println("beep");
        Thread t = new Thread() {
            public void run() {
                for (int i = 0; i < 5; i++) {
                    try {
                        pinoutd.high();                               
                        Thread.sleep(300);   
                        pinoutd.low();
                        Thread.sleep(300);   
                     } catch (InterruptedException ex) {
                         Logger.getLogger(Buzzer.class.getName()).log(Level.SEVERE, null, ex);
                     }
                }
            };
        };
        t.start();
    }

}

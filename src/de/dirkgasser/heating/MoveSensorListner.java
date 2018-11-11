package de.dirkgasser.heating;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.time.LocalTime;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;

/**
 * Move Sensor listner check if move is detected and store time 
 * @author Dirk Gassr
 */
public class MoveSensorListner implements GpioPinListenerDigital {
    private LocalTime lastMove;
    private LocalTime lastDetection;
    private GpioController gpio;
    private GpioPinInput pinin;
    private int pin;
    
    public MoveSensorListner (GpioController gpio, int pin) {
        this.pin = pin;
        Gpio.pinMode(pin, Gpio.INPUT);  
        lastMove = LocalTime.of(0, 0);
        lastDetection = LocalTime.of(0, 0);
        this.gpio = gpio;  
        pinin = gpio.provisionDigitalInputPin(RaspiPin.getPinByAddress(pin), PinPullResistance.PULL_DOWN);
        gpio.addListener(this, pinin); 
    }
    

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) {
        System.out.println("Changed");
        if (gpdsce.getState() == PinState.HIGH) {
            System.out.println("high");
        }
    }
    
}

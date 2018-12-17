package de.dirkgasser.heating;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Class to switch on and off a GPIO pin of Raspberry Pi
 * @author Dirk Gassr
 */
public class Relais {
    private GpioController gpio;
    private GpioPinDigitalOutput pinoutd;
    private int pin;
    
    public Relais (int pin) {
        this.pin = pin;
        gpio = GpioFactory.getInstance();   
        pinoutd = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin));
        pinoutd.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF); 
        pinoutd.low();
    }
    public Relais (int pin, GpioController gpio) {
        this.pin = pin;
        this.gpio = gpio;  
        pinoutd = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin));
        pinoutd.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        pinoutd.low();
    }
    
    public void setOn() {
        pinoutd.high();  
    }
    public void setOff() {
        pinoutd.low();
    }
    public boolean isOn() {
        return pinoutd.getState().isHigh();
    }
}

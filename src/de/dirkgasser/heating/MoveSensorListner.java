package de.dirkgasser.heating;

import com.pi4j.io.gpio.GpioController;

import com.pi4j.io.gpio.GpioPinInput;

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
    private int detectionCount;
    private GpioController gpio;
    private GpioPinInput pinin;
    private int pin;
    private HeatingProgram heatingProgram;
    
    public MoveSensorListner (GpioController gpio, int pin) {
        this.pin = pin;
        Gpio.pinMode(pin, Gpio.INPUT);  
        lastMove = LocalTime.of(0, 0);
        lastDetection = LocalTime.of(0, 0);
        detectionCount = 0;
        this.gpio = gpio;  
//heating Program has only one instance, second call also get first one        
        heatingProgram = HeatingProgram.getHeatingProgramFromFile("Heating");
        pinin = gpio.provisionDigitalInputPin(RaspiPin.getPinByAddress(pin), PinPullResistance.PULL_DOWN);
        gpio.addListener(this, pinin); 
    }
    

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) {
        if (gpdsce.getState() == PinState.HIGH) {
            System.out.println("high");
            if (lastDetection.isAfter(LocalTime.now().minusSeconds(20))) {
               detectionCount++; 
               if (detectionCount > heatingProgram.getMoveToBeThereMin()) {
                  lastMove = LocalTime.now();
               }
            } else {
                detectionCount = 1;
            }
            lastDetection = LocalTime.now();
        }
    }
    
    public boolean getMovement() {
        if (lastMove.isAfter(LocalTime.now().minusMinutes(heatingProgram.getNoMovetoAbsent()))) {
            return true;
        } else {
            return false;
        }
    }
    
    public void setManualMove() {
        lastMove = LocalTime.now();
        lastDetection = LocalTime.now();
        detectionCount = heatingProgram.getMoveToBeThereMin() + 1; 
    }
    
}

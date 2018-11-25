package de.dirkgasser.heating;


import com.pi4j.wiringpi.Gpio;
import java.time.LocalTime;

/**
 * Read Temperatur and Humadity from DHT11 / 22
 * @author Dirk Gasser
 */
public class DHT11 {
    private static final int    MAXTIMINGS  = 100;
    private final int[]         dht11_dat   = { 0, 0, 0, 0, 0 };
    private int pin;
    private double temp;
    private double humidity;
    private LocalTime lastRead;
    private int counters[] = new int[100];
    private int sumCounters;
    private int avgCounters;

    public DHT11(int pin) throws InstantiationException {
        if (Gpio.wiringPiSetup() == -1) {
            throw new InstantiationException("GPIO not setup");
        }
        this.pin = pin;
        temp = 0;
        humidity = 0;
        lastRead = LocalTime.of(0, 0);
    }

    private void getValues() {
        int laststate = Gpio.HIGH;
        int newstate = Gpio.HIGH;
        int j;
        int tries = 0;
        float c = 0;
        int maxtry = 10;
        while (tries < maxtry && c == 0)  {
            j = 0;
            dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;
            for (int cnt = 0; cnt < 100;cnt++) {
                counters[cnt] = 0;
            }
// Set 1-Wire Pin of DHT11 18 millisconds to low to start output
            Gpio.pinMode(pin, Gpio.OUTPUT);
            Gpio.digitalWrite(pin, Gpio.HIGH);
            Gpio.delay(1000);
            Gpio.digitalWrite(pin, Gpio.LOW);
            Gpio.delay(18);
            Gpio.digitalWrite(pin, Gpio.HIGH);
// Set GPIO Pin to input to get data
            Gpio.pinMode(pin, Gpio.INPUT);     
//if no more signals are sended by DHT11 is identified by 1000 count cycles
//Attention: "int i" has to be defined here, otherwise Java is too slow!!!!
            for (int i = 0; i < MAXTIMINGS; i++) {
                int counter = 0;
//            while (i < MAXTIMINGS & counter < 1000) {
                while (newstate == laststate) {
                    counter++;
                    Gpio.delayMicroseconds(1);
                    newstate = Gpio.digitalRead(pin);
                    if (counter == 1000) {
                 // fertig
                    break;
                    }
                }   
                laststate = newstate;

                if (counter == 1000) {
                 // fertig
                    break;
                }
//Take only ever second signal
                if (i >= 1 && i % 2 == 0) {
                    counters[j] = counter;
                    j++;
                }
            }
// Try to get data out of counters
// 1. Counters 1 to 40 with counter = 12 as border
// 2. Counters 1 to 40 with border from avarage downto 10
// 3. Counters 0 to 39 with counter = 12 as border
// 4. Counters 0 to 39 with border from avarage downto 10
            dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;
            sumCounters = 0;
            if (j >= 41) { 
        // check we read 40 bits (8bit x 5 ) + verify checksum in the last
        // byte
                for (int y = 0; y < 40; y++) {
                    dht11_dat[y / 8] <<= 1;
                    if (counters[y + 1] > 12) {
                        dht11_dat[y / 8] |= 1;
                    }
                    sumCounters = sumCounters + counters[y + 1];
                } 
                avgCounters = sumCounters / 40;
                while (!checkParity() && avgCounters > 9) {
                    dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;
                    for (int y = 0; y < 40; y++) {
                        dht11_dat[y / 8] <<= 1;
                        if (counters[y + 1] > avgCounters) {
                          dht11_dat[y / 8] |= 1;
                        }
                    } 
                    avgCounters--;
                }
            } else { 
                if (j >= 39) {
                    for (int y = 0; y < 40; y++) {
                        dht11_dat[y / 8] <<= 1;
                        if (counters[y] > 12) {
                            dht11_dat[y / 8] |= 1;
                        }
                        sumCounters = sumCounters + counters[y];
                    }
                } 
                avgCounters = sumCounters / 40;
                while (!checkParity() && avgCounters > 9) {
                    dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;
                    for (int y = 0; y < 40; y++) {
                        dht11_dat[y / 8] <<= 1;
                        if (counters[y + 1] > avgCounters) {
                          dht11_dat[y / 8] |= 1;
                        }
                    } 
                    avgCounters--;
                }
            }
            if (j >= 39 && checkParity()) {
                float h = (float) ((dht11_dat[0] << 8) + dht11_dat[1]) / 10f;
                if (h > 100) {
                    h = (float) dht11_dat[0]; // for DHT11
                }
                c = (float) (((dht11_dat[2] & 0x7F) << 8) + dht11_dat[3]) / 10f;
                if (c > 125) {
                    c = (float) dht11_dat[2]; // for DHT11
                }
                if ((dht11_dat[2] & 0x80) != 0) {
                 c = -c;
                }
                temp = c;
                humidity = h;
                lastRead = LocalTime.now(); 
            } else {
// Data not good 
            }
            tries ++;
        }
    }
    private boolean checkParity() {
        return dht11_dat[4] == (dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3] & 0xFF);
    }

/**
 * If last DHT read is older than 2 seconds, read sensor
 * return temperatur 
 * @return temperatur in °C as double
 */
    public double getTemp() {
        if (LocalTime.now().isAfter(lastRead.plusSeconds(2))) {
            getValues();
        }
        if (temp > 0 && temp < 50) {
            return temp;
        } else {
            return 0;
        }
    }
/**
 * If last DHT read is older than 2 seconds, read sensor
 * return humidity 
 * @return humidity as double
 */
    public double getHumidity() {
        if (LocalTime.now().isAfter(lastRead.plusSeconds(2))) {
            getValues();
        }
        if (humidity > 0 && humidity < 100) {
            return humidity;
        } else {
            return 0;
        }
    }
}

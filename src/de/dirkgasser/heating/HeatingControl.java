package de.dirkgasser.heating;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Class to start application HeatingControl
 * @author Dirk Gasser
 * @version 1.0
 */
public class HeatingControl {
    public static double currentTemp;
    public static double currenthumidity;
    static public JFHeatingControl mainscreen;
    public static GpioController gpio;
    public static MoveSensorListner moveSensor;
    public static HeatingProgram heatingProgram;
    
public static void main(String args[]) throws IOException {
        heatingProgram = HeatingProgram.getHeatingProgramFromFile("Heating"); 
        gpio = GpioFactory.getInstance();
        mainscreen = new JFHeatingControl();

        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFHeatingControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFHeatingControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFHeatingControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFHeatingControl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        gpio = GpioFactory.getInstance();     
       Thread thTemp = new Thread(new HeatingControlThread());
       
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainscreen.setVisible(true);
            }
        });
       moveSensor = new MoveSensorListner(gpio, 4);

       try {
                Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HeatingControlThread.class.getName()).log(Level.SEVERE, null, ex);
                }
       thTemp.start();
       

    }
}

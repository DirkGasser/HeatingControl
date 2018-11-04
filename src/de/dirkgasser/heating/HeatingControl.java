package de.dirkgasser.heating;

import java.io.IOException;

/**
 * Main Class to start application HeatingControl
 * @author Dirk Gasser
 * @version 1.0
 */
public class HeatingControl {
    public static double currentTemp;
    public static double currenthumidity;
    
public static void main(String args[]) throws IOException {
        JFHeatingControl mainscreen = new JFHeatingControl();
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
        
        
//        FullScreen.fullScreen(recipeframe, false);
/*       
       Thread thTemp = new Thread(new TemperaturWatcher(temperaturSensor));
       thTemp.start();
*/
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainscreen.setVisible(true);
            }
        });
    }
}

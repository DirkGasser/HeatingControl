package de.dirkgasser.heating;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFrame;

/**
 * class offer a static function to set a JFrame to a full screen 
 * @author Dirk Gasser
 * @version 1.0
 */
public class FullScreen {
    /**
     * Set a JFrame to fullscreen if screensize is framesize
     * @param frame - JFrame to set fullscreen
     * @param doPack - flag is Frame must be packed
     * @return flag if fullscreen was done
     */
    static public boolean fullScreen(final JFrame frame, boolean doPack) {

    GraphicsDevice device = frame.getGraphicsConfiguration().getDevice();
    boolean result = device.isFullScreenSupported();

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    if (width == frame.getPreferredSize().getWidth()) {
        if (result) {
            frame.dispose();
            frame.setUndecorated(true);
            frame.setResizable(true);

            frame.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent arg0) {
                    frame.setAlwaysOnTop(true);
                }

                @Override
                public void focusLost(FocusEvent arg0) {
                    frame.setAlwaysOnTop(false);
                }
            });

            if (doPack)
                frame.pack();

            device.setFullScreenWindow(frame);
        }
        else {
            frame.setPreferredSize(frame.getGraphicsConfiguration().getBounds().getSize());

            if (doPack)
                frame.pack();

            frame.setResizable(true);

            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            boolean successful = frame.getExtendedState() == Frame.MAXIMIZED_BOTH;

            frame.setVisible(true);

            if (!successful)
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
    }
    return result;
}
}

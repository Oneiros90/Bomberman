package support;

import com.sun.awt.AWTUtilities;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import javax.swing.UIManager;

/**
 * La classe <code>SuperFrame</code> migliora ed estende le funzioni della classe <code>JFrame</code>
 * tramite alcune funzioni avanzate.
 * @author Lorenzo Valente (aka Oneiros)
 */
public class SuperFrame extends javax.swing.JFrame {

    private GraphicsDevice device;
    private boolean fullscreen;
    private Dimension backupDim;
    private AppearingThread appearingThread;
    private TrayIcon trayIcon;

    /**
     * Crea un oggetto di tipo <code>SuperFrame</code> con la configurazione di default
     */
    public SuperFrame() {
        this(480, 320);
    }

    /**
     * Crea un oggetto di tipo <code>SuperFrame</code> di dimensioni <code>d</code>
     * @param d Le dimensioni del frame
     */
    public SuperFrame(Dimension d) {
        this(d.width, d.height);
    }

    /**
     * Crea un oggetto di tipo <code>SuperFrame</code> di dimensioni <code>width</code>, <code>height</code>
     * @param width La larghezza del frame
     * @param height L'altezza del frame
     */
    public SuperFrame(int width, int height) {
        this.setLookAndFeel(DEFAULT_LOOK_AND_FEEL);
        this.getContentPane().setBackground(Color.white);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.fullscreen = false;
        this.setSizeWithoutInsets(width, height);
        this.backupDim = new Dimension(width, height);
        this.centerFrame();
        if (SystemTray.isSupported()) {
            this.trayIcon = new TrayIcon(createImage(16, 16));
            this.trayIcon.setImageAutoSize(true);
        }
        this.setLayout(null);
    }

    /**
     * Centra il frame all'interno dello schermo
     */
    public final void centerFrame() {
        setLocationRelativeTo(null);
    }

    /**
     * Imposta la grandezza del frame senza considerare i bordi
     * @param width La nuova larghezza del frame
     * @param height La nuova altezza del frame
     */
    public final void setSizeWithoutInsets(int width, int height) {
        this.setSize(width + this.getInsets().left + this.getInsets().right,
                height + this.getInsets().top + this.getInsets().bottom);
    }

    /**
     * Imposta la grandezza del frame senza considerare i bordi
     * @param d Le nuove dimensioni del frame
     */
    public final void setSizeWithoutInsets(Dimension d) {
        this.setSizeWithoutInsets(d.width, d.height);
    }

    public final Dimension getSizeWithoutInsets() {
        return new Dimension(this.getWidth() - this.getInsets().left - this.getInsets().right,
                this.getHeight() - this.getInsets().top - this.getInsets().bottom);
    }

    private class AppearingThread extends Thread {

        protected boolean appearing;
        protected int speed;
        protected Window window;

        @Override
        public void run() {
            try {
                float f = (isVisible()) ? 0.99f : 0.01f;
                if (!isVisible()) {
                    setOpacity(0);
                    setVisible(true);
                }
                while (f >= 0 && f <= 1) {
                    setOpacity(f);
                    Thread.sleep(speed);
                    f += (appearing) ? 0.01 : -0.01;
                }
                if (f <= 0) {
                    setVisible(false);
                }
                setOpacity(1);
            } catch (InterruptedException ex) {
            }
        }

        private void setOpacity(float f){
            AWTUtilities.setWindowOpacity(window, f);
        }
    };

    /**
     * Fa apparire il frame lentamente con effetto traslucido
     * @param b <code>true</code>: fa apparire il frame
     *          <code>false</code>: fa scomparire il frame
     * @param speed La velocità dell'effetto
     * @since 1.6
     */
    public void setVisibleSmoothly(boolean b, int speed) {
        this.setVisibleSmoothly(b, speed, 0);
    }

    /**
     * Fa apparire il frame lentamente con effetto traslucido
     * @param b <code>true</code>: fa apparire il frame
     *          <code>false</code>: fa scomparire il frame
     * @param speed La velocità dell'effetto
     * @param window L'indice della finestra
     * @since 1.6
     */
    public void setVisibleSmoothly(boolean b, int speed, int window) {
        if (speed < 1 || speed > 100) {
            throw new IllegalArgumentException("The speed argument must be between 1 and 100");
        }
        if (!AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT)){
            this.setVisible(b);
            throw new RuntimeException("Translucency is not supported on the current platform");
        }
        if (this.appearingThread == null || (!this.appearingThread.isAlive() && b != this.isVisible())) {
            this.appearingThread = new AppearingThread();
            this.appearingThread.appearing = b;
            this.appearingThread.speed = 100 - speed;
            this.appearingThread.window = getWindows()[window];
            this.appearingThread.start();
        }
    }

    /** Look And Feel di default del sistema */
    public static final short DEFAULT_LOOK_AND_FEEL = 0;
    /** Look And Feel "Metal" */
    public static final short METAL_LOOK_AND_FEEL = 1;
    /** Look And Feel "Nimbus" */
    public static final short NIMBUS_LOOK_AND_FEEL = 2;
    /** Look And Feel "Motif" */
    public static final short MOTIF_LOOK_AND_FEEL = 3;
    /** Look And Feel "Windows" */
    public static final short WINDOWS_LOOK_AND_FEEL = 4;
    /** Look And Feel "Windows Classico" */
    public static final short WINDOWS_CLASSIC_LOOK_AND_FEEL = 5;

    /**
     * Imposta il Look And Feel del SuperFrame
     * @param lookAndFeel Il Look And Feel da impostare
     */
    public final void setLookAndFeel(int lookAndFeel) {
        try {
            switch (lookAndFeel) {
                case DEFAULT_LOOK_AND_FEEL:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case METAL_LOOK_AND_FEEL:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.metal.MetalLookAndFeel");
                    break;
                case NIMBUS_LOOK_AND_FEEL:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case MOTIF_LOOK_AND_FEEL:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
                case WINDOWS_LOOK_AND_FEEL:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
                case WINDOWS_CLASSIC_LOOK_AND_FEEL:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    break;
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Imposta il frame in modalità a tutto schermo o in modalità finestra
     * @param b <code>true</code>: attiva la modalità a tutto schermo
     *          <code>false</code>: attiva la modalità finestra
     */
    public final void setFullscreen(boolean b) {
        if (this.fullscreen != b) {
            this.fullscreen = b;
            this.setVisible(false);
            this.dispose();
            this.setUndecorated(b);
            if (b) {
                this.backupDim = this.getSize();
                this.device.setFullScreenWindow(this);
            } else {
                this.device.setFullScreenWindow(null);
                this.getContentPane().setSize(this.getPreferredSize());
                this.setSizeWithoutInsets(this.backupDim);
                this.centerFrame();
            }
            this.setVisible(true);
        }
    }

    /**
     * Riduce la finestra nella System Tray (area di notifica) o la riporta in primo piano
     * @param b <code>true</code>: minimizza la finestra nell'area di notifica
     *          <code>false</code>: riporta la finestra in primo piano
     */
    public final void toTrayIcon(boolean b) {
        if (this.trayIcon == null) {
            throw new RuntimeException("TrayIcon is not supported on the current platform");
        }
        this.setVisible(!b);
        if (b) {
            try {
                SystemTray.getSystemTray().add(this.trayIcon);
            } catch (AWTException ex) {
                throw new RuntimeException(ex.toString());
            }
        } else {
            this.setState(javax.swing.JFrame.NORMAL);
            SystemTray.getSystemTray().remove(this.trayIcon);
        }
    }

    /**
     * Restituisce la <code>TrayIcon</code> del frame
     * @return la <code>TrayIcon</code> del frame
     */
    public final TrayIcon getTrayIcon() {
        if (this.trayIcon == null) {
            throw new RuntimeException("TrayIcon is not supported on the current platform");
        }
        return this.trayIcon;
    }
}

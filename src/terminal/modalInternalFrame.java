//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
//нужно чтобы CustomDailod2 возвращал нормальные значения
package terminal;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

class modalInternalFrame extends javax.swing.JInternalFrame {

    boolean modal = false;// показывает modal или no.

    @Override
    public void show() {
        super.show();
        if (this.modal) {
            startModal();
        }
    }

    @Override
    public void setVisible(boolean value) {
        super.setVisible(value);
        if (modal) {
            if (value) {
                startModal();
            } else {
                stopModal();
            }
        }
    }

    private synchronized void startModal() {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                EventQueue theQueue = getToolkit().getSystemEventQueue();
                while (isVisible()) {
                    AWTEvent event = theQueue.getNextEvent();
                    Object source = event.getSource();
                    boolean dispatch = true;

                    if (event instanceof MouseEvent) {
                        MouseEvent e = (MouseEvent) event;
                        MouseEvent m = SwingUtilities.convertMouseEvent(
                                (Component) e.getSource(), e, this);
                        if (!this.contains(m.getPoint())
                                && e.getID() != MouseEvent.MOUSE_DRAGGED) {
                            dispatch = false;
                        }
                    }

                    if (dispatch) {
                        if (event instanceof ActiveEvent) {
                            ((ActiveEvent) event).dispatch();
                        } else if (source instanceof Component) {
                            ((Component) source).dispatchEvent(event);
                        } else if (source instanceof MenuComponent) {
                            ((MenuComponent) source).dispatchEvent(event);
                        }
                    }
                }
            } else {
                while (isVisible()) {
                    wait();
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

    private synchronized void stopModal() {
        notifyAll();
    }

// --Commented out by Inspection START (16.01.18 15:00):
//    public boolean isModal() {
//        return this.modal;
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)

// --Commented out by Inspection START (16.01.18 15:00):
//    public void setModal(boolean modal) {
//        this.modal = modal;
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)
}

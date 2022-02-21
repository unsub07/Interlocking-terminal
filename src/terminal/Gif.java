//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

class Gif extends Thread {

//    private VariableChangeListener variableChangeListener;
    private boolean blink = false;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void setVariable(boolean newValue) {
        boolean oldValue = blink;
        blink = newValue;
        support.firePropertyChange("variable", oldValue, newValue);
    }

//    public interface VariableChangeListener {
//        public void onVariableChanged(Object... variableThatHasChanged);
//    }
//
//    public void setVariableChangeListener(VariableChangeListener variableChangeListener) {
//        this.variableChangeListener = variableChangeListener;
//    }

    @Override
    public void run() {
//        boolean previousValue = false;
//        boolean variableValue = false;
//        if (variableValue != previousValue && this.variableChangeListener != null) {
//            this.variableChangeListener.onVariableChanged(variableValue);
//        }

        while (true) {
            try {
                setVariable(!blink);
                Gif.sleep(600);
            } catch (InterruptedException ex) {
                Err.err(ex);
            }
        }

    }

}

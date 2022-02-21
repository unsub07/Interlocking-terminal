//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import javax.swing.BorderFactory;

class Timer {

    private static final javax.swing.border.Border BORDER = javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(99, 97, 0)), BorderFactory.createLineBorder(new java.awt.Color(171, 167, 0)));
    private final javax.swing.JProgressBar cmdTimer = new javax.swing.JProgressBar();
    private boolean activated;

    Timer(int GX, int GY, int SHIFT_X, int SHIFT_Y) {
        cmdTimer.setBounds(Cell.getRealX(GX, SHIFT_X), Cell.getRealY(GY, SHIFT_Y), 40, 20);//zoom
        cmdTimer.setFont(Terminal.SANS12);//zoom
        cmdTimer.setBorder(BORDER);
        cmdTimer.setStringPainted(true);
        cmdTimer.setVisible(false);
        terminal.Commander.cmdLayers.add(cmdTimer);
        terminal.Commander.cmdLayers.setLayer(cmdTimer, 12);
    }

    void setState(int currValue, boolean cancelingMode) {
        cmdTimer.setBorder((cancelingMode) ? (BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(100, 0, 0)), BorderFactory.createLineBorder(java.awt.Color.RED)))
                : (BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 100, 0)), BorderFactory.createLineBorder(java.awt.Color.GREEN))));
        setState(currValue);
    }

    void setState(int currValue) {
        int i = 0;
        if (currValue < 1) {
            activated = false;
            cmdTimer.setVisible(false);
        } else if (!activated) {
            activated = true;
            do {
                i += 5;
            } while (i < currValue);
            cmdTimer.setMaximum(i);
            cmdTimer.setValue(currValue);
            cmdTimer.setString(String.valueOf(currValue));
            cmdTimer.setVisible(true);
        } else {
            cmdTimer.setValue(currValue);
            cmdTimer.setString(String.valueOf(currValue));
        }
    }
}

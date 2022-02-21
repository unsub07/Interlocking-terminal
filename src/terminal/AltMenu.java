//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
package terminal;

class AltMenu extends javax.swing.JPanel {

    public static final javax.swing.JToggleButton toggleAlarmer = new javax.swing.JToggleButton(Terminal.mainPictureHash.get("alarm"));
    public static final javax.swing.JToggleButton toggleCPUMonitor = new javax.swing.JToggleButton(Terminal.mainPictureHash.get("cpu"));
    public static final javax.swing.JToggleButton toggleNavigator = new javax.swing.JToggleButton(Terminal.mainPictureHash.get("navigator"));
    public static final javax.swing.JToggleButton toggleEventer = new javax.swing.JToggleButton(Terminal.mainPictureHash.get("event"));
    private static final javax.swing.JButton btnAbout = new javax.swing.JButton(Terminal.mainPictureHash.get("info"));
    private static final javax.swing.JButton btnExit = new javax.swing.JButton(Terminal.mainPictureHash.get("exit"));
//    private static final long serialVersionUID = 1L;

    private static void Close() {
        java.awt.PointerInfo a = java.awt.MouseInfo.getPointerInfo();
        java.awt.Point b = a.getLocation();
        int X = (int) b.getX() + 50;
        int Y = (int) b.getY() + 50;
        Commander.Close(X, Y);
    }

    AltMenu() {
        super();
        // -------------------------------Translate-----------------------------
        String exit = "Выход";
        String events = "События";
        String diag = "Диагностика";
        String about = "О Программе";
        String navigator = "Навигатор";
        String alarm = "Аварийные уведомления";
        // ---------------------------------------------------------------------

        setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEtchedBorder(),
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(
                        102, 102, 255), 2)));
        setBackground(new java.awt.Color(214, 217, 223));
        setForeground(new java.awt.Color(0, 0, 0));
        setSize(370, 60); // 370 //default size

        toggleNavigator.setPreferredSize(new java.awt.Dimension(40, 40));
        toggleNavigator.setToolTipText(navigator);
        toggleNavigator.setFocusable(false);
        add(toggleNavigator);
        toggleNavigator.addActionListener((java.awt.event.ActionEvent evt) -> toggleNavigatorActionPerformed());

        toggleAlarmer.setPreferredSize(new java.awt.Dimension(40, 40));
        toggleAlarmer.setSelected(Terminal.ALARMER_VISIBLE);// берем из
        // базы
        toggleAlarmer.setToolTipText(alarm);
        add(toggleAlarmer);
        toggleAlarmer.addActionListener((java.awt.event.ActionEvent evt) -> toggleAlarmActionPerformed());

        toggleEventer.setPreferredSize(new java.awt.Dimension(40, 40));
        toggleEventer.setSelected(Terminal.EVENTER_VISIBLE);
        toggleEventer.setToolTipText(events);
        add(toggleEventer);
        toggleEventer.addActionListener((java.awt.event.ActionEvent evt) -> toggleEventActionPerformed());

        toggleCPUMonitor.setPreferredSize(new java.awt.Dimension(40, 40));
        toggleCPUMonitor.setSelected(Terminal.CPU_MONITOR_VISIBLE);
        toggleCPUMonitor.setToolTipText(diag + " МПЦ");
        add(toggleCPUMonitor);
        toggleCPUMonitor.addActionListener((java.awt.event.ActionEvent evt) -> toggleCPUMonitorActionPerformed());

        btnAbout.setPreferredSize(new java.awt.Dimension(40, 40));
        btnAbout.setToolTipText(about + "...");

        add(btnAbout);
        btnAbout.addActionListener((java.awt.event.ActionEvent evt) -> btnAboutActionPerformed());

        btnExit.setPreferredSize(new java.awt.Dimension(40, 40));
        btnExit.setToolTipText(exit);
        add(btnExit);
        btnExit.addActionListener((java.awt.event.ActionEvent evt) -> btnExitActionPerformed());

        setLocation(370, 60);
    }

    private void toggleNavigatorActionPerformed() {
        if (Terminal.SIM) {
            if (toggleNavigator.isSelected()) {
                Commander.sim.setVisible(true);
            } else {
                Commander.sim.setVisible(false);
            }
        }
    }

    private void toggleCPUMonitorActionPerformed() {
        if (toggleCPUMonitor.isSelected()) {
            Commander.cpuMon.setVisible(true);
        } else {
            Commander.cpuMon.setVisible(false);
        }
    }

    private void toggleAlarmActionPerformed() {
        if (toggleAlarmer.isSelected()) {
            Commander.alarm.setVisible(true);
        } else {
            Commander.alarm.setVisible(false);
        }
    }

    private void toggleEventActionPerformed() {
        if (toggleEventer.isSelected()) {
            Commander.event.setVisible(true);
        } else {
            Commander.event.setVisible(false);
        }
    }

    private void btnAboutActionPerformed() {
        if (!Commander.about.isVisible()) {
            Commander.about.info();
            Commander.about.setVisible(true);
        }
    }

    private void btnExitActionPerformed() {
        Close();
    }

}

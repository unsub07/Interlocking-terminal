//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
package terminal;

final class Commander extends javax.swing.JFrame {

    static final Exit HALT = new Exit();
    static About about;
    static CustomDialog2 customDialog2;
    static Alarms alarm;
    static Events event;
    static Vgn vgn;
    static Player player;
    static Sim sim;
    static Emul emul;
    static CpuMon cpuMon;
    // --Commented out by Inspection (16.01.18 15:00):static UnitLst unitLst;
    private static Clock clock;
//    private static final long serialVersionUID = 1L;
//    private final int cmdLayersWidth = Terminal.COMMANDER_WIDTH;
//    private final int cmdLayersHeight = Terminal.COMMANDER_HEIGHT;

    Commander() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
//        setTitle("Схема путевого развития");
        cmdLayers.setOpaque(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing();
            }
        });
        setIconImage(Terminal.mainPictureHash.get("att_16x16").getImage());// установить иконку
        cmdLayers.setPreferredSize(new java.awt.Dimension(Terminal.COMMANDER_WIDTH, Terminal.COMMANDER_HEIGHT));
        adjustCommander();

        customDialog2 = new CustomDialog2();
        cmdLayers.setLayer(cmdLayers.add(customDialog2), javax.swing.JLayeredPane.MODAL_LAYER);// 30
        customDialog2.setVisible(false);
        customDialog2.modal = true;

        Splash.jProgressBar1.setValue(25);
        Splash.lblText.setText("Загрузка навигатора...");

        alarm = new Alarms();//из за гостя
        event = new Events();//из за гостя
        vgn = new Vgn();
        if (Terminal.play){
            player = new Player();
        }
        if (Terminal.noGuest) {// GUEST

            about = new About();
            cmdLayers.setLayer(cmdLayers.add(about), 30);
            about.setVisible(false);
            adjustAbout();

            Splash.jProgressBar1.setValue(20);
            Splash.lblText.setText("Часы ...");

            clock = new Clock();
            clock.setVisible(Terminal.CLOCK_VISIBLE);
            adjustClock();
            Commander.cmdLayers.setLayer(cmdLayers.add(clock), 12);// 12 LAYER (NOTICE_AREA_LAYER)
            Splash.jProgressBar1.setValue(35);
            Splash.lblText.setText("Часы ...");

//            alarm = new Alarms();//из за гостя
            adjustAlarm();
            cmdLayers.setLayer(cmdLayers.add(alarm), 30);
            alarm.setVisible(Terminal.ALARMER_VISIBLE);
            Splash.jProgressBar1.setValue(40);
            Splash.lblText.setText("Инициализация аварийных уведомлений ...");

//            event = new Events(); //из за гостя
            adjustEvent();
            cmdLayers.setLayer(cmdLayers.add(event), 30);
            event.setVisible(Terminal.EVENTER_VISIBLE);
            Splash.jProgressBar1.setValue(45);
            Splash.lblText.setText("Загрузка сообщений...");
            
            adjustVgn();
            cmdLayers.setLayer(cmdLayers.add(vgn), 30);
            vgn.setVisible(false);
            
            if (Terminal.play){
                adjustPlayer();
                cmdLayers.setLayer(cmdLayers.add(player), 30);
                player.setVisible(true);
            }

            if (Terminal.SIM) {
                if (Util.isClass()) {
                    sim = new Sim();
                    adjustSim();
                    cmdLayers.setLayer(cmdLayers.add(sim), 30);
                    sim.setVisible(false);
                    Splash.jProgressBar1.setValue(50);
                    Splash.lblText.setText("Симулятор...");
                    //---
                    emul = new Emul();
                    adjustEmul();
                    cmdLayers.setLayer(cmdLayers.add(emul), 30);
                    emul.setVisible(false);
                } else {
                    Log.log("Class Sim not found");
                }
            }

        }// end GUEST

        set_role();

        set_title();

        Clock.lblUser.setText("<html><font color=white size=3>" + Terminal.role
                + " / " + "<font color=#33ff00 size=3>" + Terminal.FIO
                + "</html>");
    }
    
    private static void set_role() {
        if (Terminal.DSP && Terminal.SHN) {
            Terminal.role = "Администратор";
        } 
//        else {
            if (Terminal.DSP) {
                Terminal.role = "Диспетчер";
            }
            if (Terminal.SHN) {
                Terminal.role = "Механик";
            }
            if (Terminal.SIM) {
                Terminal.role = "Симулятор";
            }
            if (Terminal.SEC) {
                Terminal.role = "Безопасность";
            }    
            if (Terminal.GST) {
                Terminal.role = "Гость";
            //}
                
        }
    }

    void set_title() {
        setTitle("Схема путевого развития - " + Terminal.role + " "
                + Terminal.FIO + " - " + Net.host);
    }

    private static void adjustClock() {
        clock.setBounds(Terminal.CLOCK_X, Terminal.CLOCK_Y, Terminal.CLOCK_WIDTH, Terminal.CLOCK_HEIGHT);
    }

    private static void adjustSim() {
        sim.setBounds(Terminal.SIM_X, Terminal.SIM_Y, Terminal.SIM_WIDTH, Terminal.SIM_HEIGHT);
//        sim.setBounds(100, 200, 600, 400);
    }
    
    private static void adjustEmul() {
//        sim.setBounds(Terminal.SIM_X, Terminal.SIM_Y, Terminal.SIM_WIDTH, Terminal.SIM_HEIGHT);
        emul.setBounds(10, 20, 600, 800);
    }
    
    private static void adjustAlarm() {
        alarm.setBounds(Terminal.ALARMER_X, Terminal.ALARMER_Y, Terminal.ALARMER_WIDTH, Terminal.ALARMER_HEIGHT);
    }
    
    private static void adjustEvent() {
        event.setBounds(Terminal.EVENTER_X, Terminal.EVENTER_Y, Terminal.EVENTER_WIDTH, Terminal.EVENTER_HEIGHT);
    }    
    
    private static void adjustVgn() {
        vgn.setBounds(Terminal.VGN_X, Terminal.VGN_Y, Terminal.VGN_WIDTH, Terminal.VGN_HEIGHT);
        //vgn.setBounds(0, 600, 1_000, 300);
    }    
    
    private static void adjustPlayer() {
        player.setBounds(Terminal.PLAYER_X, Terminal.PLAYER_Y, Terminal.PLAYER_WIDTH, Terminal.PLAYER_HEIGHT);
    }    

    static void Close(int X, int Y) {
//        new Sound(99).start();//99-close sound
        switch (Commander.customDialog2.showOptionDialog(X, Y,
                "Завершение работы.",
                "<html><font color=red size=5 style=bold><br> Выйти из программы? </font></html>")) {
            case 0:
                break;
            case 1:
                HALT.exit();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdScroller = new javax.swing.JScrollPane();

        cmdScroller.setPreferredSize(new java.awt.Dimension(1858, 840));

        cmdLayers.setBackground(new java.awt.Color(221, 237, 252));
        cmdLayers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cmdLayersMousePressed(evt);
            }
        });
        cmdScroller.setViewportView(cmdLayers);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cmdScroller, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 839, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cmdScroller, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void adjustCommander() {
        int xx = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int x = terminal.Terminal.COMMANDER_X;
        if (xx < 1_281) {
            x = 0;
        }
        setBounds(x, Terminal.COMMANDER_Y, Terminal.COMMANDER_WIDTH, Terminal.COMMANDER_HEIGHT);
        if (Terminal.full_screen) {
            setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);// Victor развернуть на весь экран
        }
    }

    private void adjustAbout() {
        about.setLocation(this.getWidth() / 2
                - (about.getPreferredSize().width / 2) ,// x
                this.getHeight() / 2 - (about.getPreferredSize().height / 2) + 100);
    }

    private void cmdLayersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdLayersMousePressed
        Note(evt);
    }//GEN-LAST:event_cmdLayersMousePressed

    private void formWindowClosing() {
        HALT.exit();
    }
//    private void showhelp() {
//        // <editor-fold defaultstate="collapsed" desc="showhelp()">
//        String FS = Terminal.FS;
//        String path;// = "/home/attrans/dist/scripts/dsp_doc";
//        if ("Linux".equals(System.getProperty("os.name"))) {
//            path = System.getenv("ATTRANS_HOME") + FS + "scripts" + FS
//                    + "doc_html.sh monitor.html";
//        } else {
//            path = System.getenv("ATTRANS_HOME") + FS + "scripts" + FS
//                    + "doc_html.bat monitor.html";
//        }
//        try {
//            Runtime.getRuntime().exec(path); // ЕСЛИ СУЩЕСТВУЕТ и ЗАПУСКАЕТСЯ то
//            // выполнить.
//        } catch (java.io.IOException e) {
//            Err.err(e);
//        }
//        // </editor-fold>
//    }
    private void Note(java.awt.event.MouseEvent evt) {
        // <editor-fold defaultstate="collapsed" desc="Note">
        if (evt.isControlDown()) {
            if (Terminal.DSP) {
                makeSingleNote(evt.getX(), evt.getY());
            }
        }
        // </editor-fold>
    }

    private void makeSingleNote(int x, int y) {
        int gX = x / 72, gY = y / 72, sX = x - gX * 72, sY = y - gY * 72;
        Note.create_note_gobj(gX, gY, sX, sY);
    }

    static void set_alarm_title(String TITLE) {
        alarm.setTitle(TITLE);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static final javax.swing.JLayeredPane cmdLayers = new javax.swing.JLayeredPane();
    public javax.swing.JScrollPane cmdScroller;
    // End of variables declaration//GEN-END:variables
}

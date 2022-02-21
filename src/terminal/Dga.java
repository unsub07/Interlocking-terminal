//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
//сделать JLabel - время до останоки 
package terminal;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import ru.attrans.proc.objs.DgaState;
import static terminal.Commander.cmdLayers;

class Dga extends Cell {//нахуя Cell?

//    private static final javax.swing.ImageIcon fuel = Terminal.mainPictureHash.get("fuel");
    // кнопки
    private static final JButton btn_auto = new javax.swing.JButton("Авто");
    private static final JButton btn_hndl = new javax.swing.JButton("Ручной");
    private static final JButton btn_stop = new javax.swing.JButton("Стоп");//бордер убрать и раскрасить
    private static final JButton btn_start = new javax.swing.JButton("Старт");//раскрасить

    private static final javax.swing.JLabel lblMode = new javax.swing.JLabel("A"); //M
    private static final javax.swing.JLabel lblRNx = new javax.swing.JLabel("~Ф");
    private static final javax.swing.JLabel lblFuelControl = new javax.swing.JLabel(Terminal.mainPictureHash.get("fuel"));//       IMG
    private static final javax.swing.JLabel lblTime = new javax.swing.JLabel(""); //123456789
    private final javax.swing.JLabel lblName = new javax.swing.JLabel();
    
    private final JLayeredPane cmdCellPane = new JLayeredPane();
    private static final java.awt.Color cmdNameBackgroundColor = new java.awt.Color(140, 255, 100, 80);
    private static final javax.swing.border.Border cmdNameBorder2 = javax.swing.BorderFactory.createLineBorder(new Color(150, 180, 210));
    
    private boolean dga_auto;//Режим работы - "автоматический"
    private boolean dga_hndl;//Режим работы - "ручной"
    private boolean dga_stop;//Нажата кнопка "СТОП"
    private boolean dga_fuel;//Контроль топлива
    private boolean start_hnd;//Запуск ДГА в ручном режиме
    private boolean stop_hnd;//Остановка ДГА в ручном режиме
    private boolean start_aut;//Запуск ДГА в автоматическом режиме
    private boolean stop_aut;//Остановка ДГА в автоматическом режиме
    private boolean rnx;// Наличие напряжения на фидере x (ДГА)
    private boolean knx;// Питание от фидера x (ДГА)
    private boolean run;//была ли отдана любая команда на запуск ДГА    
    
    Dga(int ID_OBJ, String S_NAME, int X, int Y, int SHIFT_X, int SHIFT_Y, int VIDEO_STATUS) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 80);//80 DGA
        setPanes();
        SetDefaultState();
        SetCmd(true);
    }

    private void setPanes() {
        int lblW = 40;
        int lblH = 24;
        int cmdH = 76;
        int cmdW = 200;

        cmdCellPane.setSize(cmdW, cmdH);
        cmdCellPane.setLocation(cmdX, cmdY);
        cmdCellPane.setBackground(cmdNameBackgroundColor);
        cmdCellPane.setBorder(cmdNameBorder2);
        cmdCellPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 2));
        setCmdLayers();
        cmdCellPane.setVisible(true);
        prep_lbl(lblName,s_name,cmdW,lblName.getFontMetrics(Terminal.SANS14).getHeight());
        lblName.setOpaque(false);
        lblName.setText(s_name);//оставить
        lblName.setBorder(null);
        prep_btn(btn_hndl,"Включение режима \"Ручной\"");
        prep_btn(btn_start,"Старт");
        prep_btn(btn_stop,"Стоп");
        prep_btn(btn_auto,"Включение режима \"Авто\"");

        btn_auto.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_autoMousePressed();
            }
        });

        btn_hndl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_hndlMousePressed();
            }
        });

        btn_stop.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_stopMousePressed();
            }
        });

        btn_start.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_startMousePressed();
            }
        });
// --------------------------------------
//        lblName.setFont(Terminal.SANS14);
//        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
//        lblName.setVerticalAlignment(javax.swing.JLabel.TOP);
//        lblName.setPreferredSize(new Dimension(cmdW, lblName.getFontMetrics(Terminal.SANS14).getHeight()));
//        lblName.setToolTipText(s_name);
//        lblName.setBorder(null);
//        cmdCellPane.add(lblName);
//        cmdCellPane.setLayer(lblName, 2);        
//        lblName.setVisible(true);
// --------------------     
        prep_lbl(lblMode,"Режим работы",lblW,lblH);
//        lblMode.setOpaque(true);
//        lblMode.setFont(Terminal.SANS14);
//        lblMode.setHorizontalAlignment(javax.swing.JLabel.CENTER);
//        lblMode.setVerticalAlignment(javax.swing.JLabel.BOTTOM);
//        lblMode.setPreferredSize(new Dimension(lblW, lblH));
//        lblMode.setToolTipText("Режим работы");
//        lblMode.setBorder(BorderFactory.createEtchedBorder());
//        cmdCellPane.add(lblMode);
//        cmdCellPane.setLayer(lblMode, 2);
//        lblMode.setVisible(true);
// --------------------     
        prep_lbl(lblRNx,"Наличие напряжения на фидере (ДГА)",lblW,lblH);
//        lblRNx.setOpaque(true);
//        lblRNx.setFont(Terminal.SANS14);
//        lblRNx.setHorizontalAlignment(javax.swing.JLabel.CENTER);
//        lblRNx.setVerticalAlignment(javax.swing.JLabel.BOTTOM);
//        lblRNx.setPreferredSize(new Dimension(lblW, lblH));
//        lblRNx.setToolTipText("Наличие напряжения на фидере (ДГА)");
//        lblRNx.setBorder(BorderFactory.createEtchedBorder());
//        cmdCellPane.add(lblRNx);
//        cmdCellPane.setLayer(lblRNx, 2);
//        lblRNx.setVisible(true);
// --------------------                        
        prep_lbl(lblFuelControl,"Контроль топлива",lblW,lblH);
//        lblFuelControl.setOpaque(true);
////        lblFuelControl.setFont(cmdAreaLblFont);
//        lblFuelControl.setHorizontalAlignment(javax.swing.JLabel.CENTER);
//        lblFuelControl.setVerticalAlignment(javax.swing.JLabel.BOTTOM);
//        lblFuelControl.setPreferredSize(new Dimension(lblW, lblH));
//        lblFuelControl.setToolTipText("Контроль топлива");
//        lblFuelControl.setBorder(BorderFactory.createEtchedBorder());
//        cmdCellPane.add(lblFuelControl);
//        cmdCellPane.setLayer(lblFuelControl, 2);
//        lblFuelControl.setVisible(true);
// -----------------------------------------
        prep_lbl(lblTime,"оставшееся время работы",40,24);
//        lblTime.setOpaque(true);
//        lblTime.setFont(Terminal.SANS14);
//        lblTime.setHorizontalAlignment(javax.swing.JLabel.CENTER);
//        lblTime.setVerticalAlignment(javax.swing.JLabel.BOTTOM);
//        lblTime.setPreferredSize(new Dimension(lblW, lblH));
//        lblTime.setToolTipText("оставшееся время работы");
//        lblTime.setBorder(BorderFactory.createEtchedBorder());
//        cmdCellPane.add(lblTime);
//        cmdCellPane.setLayer(lblTime, 2);
//        lblTime.setVisible(true);
    }
    
    private void prep_btn(JButton btn, String s){
        btn.setToolTipText(s);
        btn.setFont(Terminal.SANS10);
        btn.setSize(60, 26);
        btn.setFocusable(false);
        btn.setVerticalAlignment(javax.swing.JLabel.CENTER);
        cmdCellPane.add(btn);
        cmdCellPane.setLayer(btn, 2);
    }
    
    private void prep_lbl(javax.swing.JLabel lbl, String s, int x, int y){
        lbl.setOpaque(true);
        lbl.setFont(Terminal.SANS14);
        lbl.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lbl.setVerticalAlignment(javax.swing.JLabel.BOTTOM);
        lbl.setPreferredSize(new Dimension(x, y));
        lbl.setToolTipText(s);
        lbl.setBorder(BorderFactory.createEtchedBorder());
        cmdCellPane.add(lbl);
        cmdCellPane.setLayer(lbl, 2);
        lbl.setVisible(true);
    }

    private void setCmdLayers() {
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, CELLPANE_LAYER_10);//10
    }

    private void SetDefaultState() {
        btn_auto.setBackground(java.awt.Color.LIGHT_GRAY);//auto
        btn_hndl.setBackground(java.awt.Color.LIGHT_GRAY);//auto
        btn_stop.setBackground(java.awt.Color.LIGHT_GRAY);//auto
        btn_start.setBackground(java.awt.Color.LIGHT_GRAY);//auto
        lblMode.setBackground(java.awt.Color.GREEN.darker());//auto

//        lblRNx.setBackground(java.awt.Color.RED);
//        lblRNx.setForeground(java.awt.Color.WHITE);
        lblRNx.setBackground(java.awt.Color.GRAY);
        lblRNx.setForeground(java.awt.Color.BLACK);
        lblFuelControl.setBackground(java.awt.Color.RED);//auto

        lblTime.setBackground(java.awt.Color.GREEN.darker());//auto
    }
    
    void SetCmd(boolean STATUS) {
        // запретим или разрешим все кнопки при изменении переменной когда связь пропадает появляется.
        boolean status = (STATUS & Terminal.DSP & Area.Manager_PC);
        btn_auto.setEnabled(status);
        btn_hndl.setEnabled(status);
        btn_stop.setEnabled(status);
        btn_start.setEnabled(status);
        lblMode.setEnabled(status);
        lblRNx.setEnabled(status);
        lblFuelControl.setEnabled(status);
        lblTime.setEnabled(status);
    }

    void setState(long DTIME, int DGA_TIME, boolean DGA_AUTO, boolean DGA_HNDL, boolean DGA_STOP, boolean DGA_FUEL, boolean START_HND, boolean STOP_HND, boolean START_AUT, boolean STOP_AUT, boolean RNx, boolean KNx, boolean DGA_ON, boolean RUN //тригер в сименсе 1 если отдана любая команда на запуск
    ) {
        
        setDtime(DTIME);
        if (DGA_TIME > 0 && DGA_TIME < 9) {
            lblTime.setBackground(Color.WHITE);
            lblTime.setText(String.valueOf(DGA_TIME));
        } else {
            lblTime.setBackground(Color.DARK_GRAY);
            lblTime.setToolTipText("");
            lblTime.setText("");
        }
// -------------------------------------------------
        if (dga_auto != DGA_AUTO) {
            dga_auto = DGA_AUTO;
            if (DGA_AUTO) {
                lblMode.setText("A");
                lblMode.setToolTipText("Режим работы - \"автоматический\"");
                lblMode.setBackground(java.awt.Color.GREEN.darker());
                event(355);//1 355	Режим работы - "автоматический"	MESS_DGA_AUTO
                btn_start.setEnabled(false);
                btn_stop.setEnabled(false);
            }
//            else {
//                lblMode.setBackground(java.awt.Color.DARK_GRAY);
//            }
        }
// -------------------------------------------------
        if (dga_hndl != DGA_HNDL) {
            dga_hndl = DGA_HNDL;
            if (DGA_HNDL) {
                lblMode.setText("M");
                lblMode.setToolTipText("Режим работы - \"ручной\"");
                lblMode.setBackground(java.awt.Color.YELLOW.darker());
                event(357);// 1 357	Режим работы - "ручной"	MESS_DGA_HNDL
                btn_start.setEnabled(true);
                btn_stop.setEnabled(true);
            }
//            else {
//                lblMode.setBackground(java.awt.Color.DARK_GRAY);
//            }
        }
// -------------------------------------------------
//сделать раскраску и такуюже для старта
//        if (dga_stop != DGA_STOP) {
//            dga_stop = DGA_STOP;
//            if (DGA_STOP) {// бит для раскраски кнопки (ни сообщений ни алармов)
//                btn_stop.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 1));//red
//            } else {
//                btn_stop.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2));//normal
//            }
//        }
// -------------------------------------------------
        //if run
//        if (dga_fuel != DGA_FUEL) {
//        if (RUN) {
            dga_fuel = DGA_FUEL;
            if (DGA_FUEL) {// ALARM_Уровень топлива ДГА ниже нормы
                lblFuelControl.setBackground(java.awt.Color.GREEN.darker());
                alarm_off(363);
            } else {
                event(356);
                alarm_on();//363	Уровень топлива ДГА ниже нормы	ALARM_DGA_FUEL
                lblFuelControl.setBackground(java.awt.Color.RED);
            }
//        }
// -------------------------------------------------
        if (start_hnd != START_HND) {
            start_hnd = START_HND;
            if (START_HND) {
                event(360);// 1 /360	Запуск ДГА в ручном режиме	START_HND
            }
        }
// -------------------------------------------------
        if (stop_hnd != STOP_HND) {
            stop_hnd = STOP_HND;
            if (STOP_HND) {
                event(362);// 1 362	Остановка ДГА в ручном режиме	STOP_HND
            }
        }
// -------------------------------------------------
        if (start_aut != START_AUT) {
            start_aut = START_AUT;
            if (START_AUT) {
                event(359);// 1 /359	Запуск ДГА в автоматическом режиме	START_AUT
            }
        }
// -------------------------------------------------
        if (stop_aut != STOP_AUT) {
            stop_aut = STOP_AUT;
            if (STOP_AUT) {
                event(361);// 1 361	Остановка ДГА в автоматическом режиме	STOP_AUT
            }
        }
// -------------------------------------------------
        //тут ещё надо знать работет DGA или нет, если не работает то рисовать серым  
        if (RUN) {//rnx != RNx) {
            rnx = RNx;
            if (RNx) {
                lblRNx.setBackground(java.awt.Color.GREEN.darker());
                lblRNx.setForeground(java.awt.Color.BLACK);
                alarm_off(364);
            } else {
                lblRNx.setBackground(java.awt.Color.RED);
                lblRNx.setForeground(java.awt.Color.WHITE);
                event(364);//364	Отсутствует напряжение на фидере ДГА	ALARM_DGA_RNx  !!!!!!!! Это не аларм а эвент
            }
        } else {//!RUN
            lblRNx.setBackground(java.awt.Color.GRAY);
            lblRNx.setForeground(java.awt.Color.BLACK);
        }
// -------------------------------------------------
//        if (knx != KNx) {
            knx = KNx;
            if (KNx) {
                lblRNx.setBorder(BorderFactory.createLineBorder(new Color(0, 140, 0), 3));
                event(358);// 1 // MESS_ - Нагрузка на ДГА
            } else {
                lblRNx.setBorder(BorderFactory.createEtchedBorder());
            }
//        }
    }

    private void event(
            int id_msg) {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
    }

    private void alarm_on() {
        if (getDtime() > 0) {
            Alarms.alarm_on(getDtime(), 363, id_obj);
        }
    }

    private void alarm_off(int id) {
        if (getDtime() > 0) {
            Alarms.alarm_off(getDtime(), id, id_obj);
        }
    }

    private void btn_autoMousePressed() {
        Net.sendMaskedCmd_DSP(id_obj, "DGA_AUTO1");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "DGA_AUTO0");
    }

    private void btn_hndlMousePressed() {
        Net.sendMaskedCmd_DSP(id_obj, "DGA_HNDL1");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "DGA_HNDL0");
    }

    private void btn_stopMousePressed() {
        Net.sendMaskedCmd_DSP(id_obj, "DGA_STOP1");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "DGA_STOP0");
    }

    private void btn_startMousePressed() {
        Net.sendMaskedCmd_DSP(id_obj, "DGA_START1");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "DGA_START0");
    }

    private void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }

    @Override
    public void setState(Object oid) {
        DgaState d = (DgaState) oid;
        this.setState(
                d.timestamp,
                d.DGA_TIME,
                d.DGA_AUTO,
                d.DGA_HNDL,
                d.DGA_STOP,
                d.DGA_FUEL,
                d.START_HND,
                d.STOP_HND,
                d.START_AUT,
                d.STOP_AUT,
                d.RNx,
                d.KMx,
                d.DGA_ON,//- это типа DGA_START
                d.RUN //есть команда на запуск
        );
    }

}

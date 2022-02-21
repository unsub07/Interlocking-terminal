//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.UsState;
import static terminal.Commander.cmdLayers;

class Blokpost extends Cell {

    private static final int LAYER = 7;
    private static final Border cmdCellPaneBorder = BorderFactory.createLineBorder(new Color(150, 180, 210));
    private final javax.swing.JPanel pnlUps = new javax.swing.JPanel();

    private static final Color cmdIdleForeground = Color.DARK_GRAY;
    private static final Color cmdAlarmBackground = new Color(255, 204, 204);
    private static final Color cmdIdleBackground_on = new Color(220, 236, 250);
    private static final Color cmdIdleBackground_off = Color.LIGHT_GRAY;
    private static final Border cmdAlarmBorderLow = BorderFactory.createLineBorder(new Color(255, 0, 0), 2);
//    private static final Border cmdAlarmBorderHigh = BorderFactory.createLineBorder(new Color(170, 0, 0), 2);
//    private static final Border cmdAlarmBorderHigh = BorderFactory.createLineBorder(Color.YELLOW);
    private static final Border cmdIdleBorder = BorderFactory.createLineBorder(Color.DARK_GRAY);

    //--------------------------------------------------------------------------
    private final javax.swing.JLayeredPane cmdCellPane = new javax.swing.JLayeredPane();

    private final javax.swing.JLabel lbl05 = new javax.swing.JLabel();//1
    private final javax.swing.JLabel lbl06 = new javax.swing.JLabel("2");//1
    private final javax.swing.JLabel lbl07 = new javax.swing.JLabel("3");//1
    private final javax.swing.JLabel lbl10 = new javax.swing.JLabel("4");//1
    private final javax.swing.JLabel lbl11 = new javax.swing.JLabel("5");//1
    private final javax.swing.JLabel lbl12 = new javax.swing.JLabel("6");//1
    private final javax.swing.JLabel lbl13 = new javax.swing.JLabel("7");//1
    private final javax.swing.JLabel lbl14 = new javax.swing.JLabel("8");//1
    private final javax.swing.JLabel lbl15 = new javax.swing.JLabel("9");//1
    private final javax.swing.JLabel lbl16 = new javax.swing.JLabel("0");//1
    private final javax.swing.JLabel lbl17 = new javax.swing.JLabel("10");//1

    // -------------------------------Translate---------------------------------
    private final String menu_title;

    private final Img lbl = new Img();
    private final javax.swing.JLabel lblName = new javax.swing.JLabel();
    private boolean blink1;
    private boolean norma = true;
    private boolean hz = true;
    private boolean _02;
    private boolean _03;
    private boolean _04;
    private boolean _05;
    private boolean _06;
    private boolean _07;
    private boolean _10;
    private boolean _11;
    private boolean _12;
    private boolean _13;
    private boolean _14;
    private boolean _15;
    private boolean _16;
    private boolean _17;
    private boolean panelVisible = false;

    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();// меню
    private final javax.swing.JMenuItem off_Item = new javax.swing.JMenuItem("<html><b><font color=orange>Отключить</font></b></html>");//Отключить
    private final javax.swing.JMenuItem on_Item = new javax.swing.JMenuItem("<html><b><font color=green>Включить</font></b></html>");//Включить

    private final ActionListener US1 = (ActionEvent e) -> {
        blink1 = !blink1;
//        if (blink1) {
//            lblName.setBackground(Color.DARK_GRAY);
//            lblName.setForeground(Color.YELLOW);
//        } else {
//            lblName.setBackground(Color.YELLOW);
//            lblName.setForeground(Color.DARK_GRAY);
//        }
        if (blink1) {
                lblName.setBorder(cmdAlarmBorderLow);
            } else {
                lblName.setBorder(cmdIdleBorder);
            }
    };

    Blokpost(
            int ID_OBJ,
            String S_NAME,
            int GX,
            int GY,
            int VIDEO_STATUS,
            int SHIFT_X,
            int SHIFT_Y,
            int TYPE) {
        super(ID_OBJ, S_NAME, GX, GY, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 31);//31 - US Установка Сигнальная
        menu_title = S_NAME;
        setPanes(VIDEO_STATUS);
    }

    private void setPanes(int VIDEO_STATUS) {

        cmdX += Terminal.TunePngX + 24;// -6
        cmdY += Terminal.TunePngY - 8;// -6

        lblName.setText(s_name);
        int cmdW = lblName.getFontMetrics(lblName.getFont()).stringWidth(s_name);
        int cmdH = lblName.getFontMetrics(lblName.getFont()).getHeight();
        lblName.setFont(Terminal.SANS09);//zoom
        lblName.setForeground(cmdIdleForeground);
        lblName.setBounds(cmdX + 22, cmdY, cmdW + 8, cmdH + 4);//двигаем от картинки лейбу
        lblName.setOpaque(true);
        lblName.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        lblName.setBackground(cmdIdleBackground_off);
        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVerticalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVisible(true);
        terminal.Commander.cmdLayers.add(lblName);
        terminal.Commander.cmdLayers.setLayer(lblName, LAYER);
// ---------------------------
        if ((Terminal.DSP || Terminal.SHN) && (VIDEO_STATUS == 0)) {// DSP и SHN
            Popup.setBorder(BorderFactory.createTitledBorder(menu_title));
            Popup.setBorderPainted(true);
            Popup.add(off_Item);
            off_Item.addActionListener((java.awt.event.ActionEvent evt) -> off_ActionPerformed());
            Popup.addSeparator();
            Popup.add(on_Item);
            on_Item.addActionListener((java.awt.event.ActionEvent evt) -> on_ActionPerformed());
            lblName.setComponentPopupMenu(Popup);
            Popup.setBorderPainted(true);
            Popup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    preparePopup();
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
        }//END DSP

        Util.prep_lbl(lbl, LAYER, "fault", cmdX, cmdY);
        lbl.setVisible(false);//default state

        lbl.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblPressed();
            }
        });
        
//------------------------------
        int pnlUpsW = 110;//76
        int pnlUpsH = 22;
        pnlUps.setPreferredSize(new java.awt.Dimension(pnlUpsW, pnlUpsH));
        pnlUps.setBorder(BorderFactory.createEtchedBorder());
        pnlUps.setBackground(Color.DARK_GRAY);
        pnlUps.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 1));

        lbl05.setPreferredSize(new Dimension(8,8));
        lbl05.setBorder(cmdIdleBorder);
        lbl05.setOpaque(true);
        
        lbl06.setPreferredSize(new Dimension(8,8));
        lbl06.setBorder(cmdIdleBorder);
        lbl06.setOpaque(true);
        
        lbl07.setPreferredSize(new Dimension(8,8));
        lbl07.setBorder(cmdIdleBorder);
        lbl07.setOpaque(true);
        
        lbl10.setPreferredSize(new Dimension(8,8));
        lbl10.setBorder(cmdIdleBorder);
        lbl10.setOpaque(true);
        
        lbl11.setPreferredSize(new Dimension(8,8));
        lbl11.setBorder(cmdIdleBorder);
        lbl11.setOpaque(true);
        
        lbl12.setPreferredSize(new Dimension(8,8));
        lbl12.setBorder(cmdIdleBorder);
        lbl12.setOpaque(true);
        
        lbl13.setPreferredSize(new Dimension(8,8));
        lbl13.setBorder(cmdIdleBorder);
        lbl13.setOpaque(true);

//        lbl14.setPreferredSize(new Dimension(8,8));
//        lbl14.setBorder(cmdIdleBorder);
//        lbl14.setOpaque(true);
//
//        lbl15.setPreferredSize(new Dimension(8,8));
//        lbl15.setBorder(cmdIdleBorder);
//        lbl15.setOpaque(true);
//
//        lbl16.setPreferredSize(new Dimension(8,8));
//        lbl16.setBorder(cmdIdleBorder);
//        lbl16.setOpaque(true);
//
//        lbl17.setPreferredSize(new Dimension(8,8));
//        lbl17.setBorder(cmdIdleBorder);
//        lbl17.setOpaque(true);
        
        pnlUps.add(lbl05);
        pnlUps.add(lbl06);
        pnlUps.add(lbl07);
        pnlUps.add(lbl10);
        pnlUps.add(lbl11);
        pnlUps.add(lbl12);
        pnlUps.add(lbl13);
//        pnlUps.add(lbl14);
//        pnlUps.add(lbl15);
//        pnlUps.add(lbl16);
//        pnlUps.add(lbl17);

        pnlUps.setOpaque(true);
        cmdCellPane.add(pnlUps);
        cmdCellPane.setLayer(pnlUps, 2);
//        cmdCellPane.setSize(cmdW, 28);//cmdH);
        cmdCellPane.setSize(112, 26);//cmdH);
        cmdCellPane.setLocation(cmdX, cmdY - 28);
        cmdCellPane.setBorder(cmdCellPaneBorder);
        cmdCellPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 1));

        setCmdLayers();
        cmdCellPane.setVisible(false);//false

    }

    private void setCmdLayers() {
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, CELLPANE_LAYER_10);//10
    }

    private void preparePopup() {
        off_Item.setEnabled(!norma);// && Status.work);
        on_Item.setEnabled(norma);// && Status.work);
    }

    private void off_ActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold> "
                + OPERATION
                + "!!</font><br>Подтвердите <b>Отключение</b> Сигнальной установки <b>" + s_name + "</b></html>")) {//translate
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "US_OFF1");
                pause();
                Net.sendMaskedCmd_DSP(id_obj, "US_OFF0");
                break;
        }
    }

    private void on_ActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(
                cmdX,
                cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold> "
                + OPERATION
                + "!!</font><br>Подтвердите <b>Включение</b><br> для <b>" + s_name + "</b></html>")) {//translate
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP_SHN(id_obj, "US_ON1");
                pause();
                Net.sendMaskedCmd_DSP_SHN(id_obj, "US_ON0");
                break;
        }
    }

    void SetCmd(boolean STATUS) {// запретим или разрешим все кнопки при изменении переменной когда связь пропадает появляется.
        boolean status = (STATUS & Terminal.DSP && Area.Manager_PC);
    }

    private void lblPressed() {// обработка кнопки режима управления
        panelVisible = !panelVisible;
        cmdCellPane.setVisible(panelVisible);//false
    }
    
    void setState(
            long DTIME,
            boolean NORMA,  //Устройство в норме
            boolean HZ,     //Неисправность
            boolean _02,    //ЧН 1 - установлено четное направление
            boolean _03,    //НЧ 1 - установлено нечетное направление
            boolean _04,    //ЧКО+НКО 1 - перегорание красного огня на светофорах
            boolean _05,    //КА срабатывание автоматов
            boolean _06,    //GND1 (СВ) "земля на светофорах"
            boolean _07,    //GND2 (СО) "земля на счете осей"
            boolean _10,    //отказ электосети - сигнал от ИБП, 1- нет питания
            boolean _11,    //акк. разряжен - сигнал от ИБП, 1- акб разряжен
            boolean _12,    //общая тревога - сигнал от ИБП, 1- тревога
            boolean _13,    //отказ ИБП - сигнал от ИБП, 1- отказ
            boolean _14,    //ДГА1 что-то от ДГА
            boolean _15,    //ДГА2 ещё что-то от ДГА
            boolean _16,    //У1 - чем-то управляем
            boolean _17     //У2 - ещё чем-то управляем
    ) {
        setDtime(DTIME);
//-------------------------------
        if (norma != NORMA) {//OFF 1 - блокпост выключен
            norma = NORMA;
            if (NORMA) {
                lblName.setBackground(cmdIdleBackground_off);
            } else {
                lblName.setBackground(cmdIdleBackground_on);
            }
        }
//-------------------------------
        if (hz != HZ) {//ALM 1 - есть неисправность на БП
            hz = HZ;
            if (HZ) {
                event(374);//374	<html><font color=red>Неисправность</html>	MESS_US_HZ	INFO_MESS
                alarm_on(378);//378	Неисправность	ALARM_US_HZ	ALARM
                Terminal.TIMER600.addActionListener(US1);
                lbl.setVisible(true);
            } else {
                event(375);//375	Устройство в норме	MESS_US_NORMA	INFO_MESS
                alarm_off(378);
                Terminal.TIMER600.removeActionListener(US1);
                lbl.setVisible(false);
                cmdCellPane.setVisible(false);//false
                panelVisible = false;
                lblName.setBorder(cmdIdleBorder);
                lblName.repaint();
            }
        }
//-------------------------------
        if (this._02 != _02) {//ЧН 1 - установлено четное направление
            this._02 = _02;
            if (_02) {
                Log.log("+++!!! 02");
            } else {
                Log.log("+++!!! -02");
            }
        }
//-------------------------------
        if (this._03 != _03) {//НЧ 1 - установлено нечетное направление
            this._03 = _03;
            if (_03) {
                Log.log("+++!!! 03");
            } else {
                Log.log("+++!!! -03");
            }
        }
//-------------------------------
        if (this._04 != _04) {//ЧКО+НКО 1 - перегорание красного огня на светофорах
            this._04 = _04;
            if (_04) {
                Log.log("+++!!! 04");
            } else {
                Log.log("+++!!! -04");
            }
        }
//-------------------------------
        if (this._05 != _05) {//КА срабатывание автоматов
            this._05 = _05;
            if (_05) {
                lbl05.setBackground(Color.RED);
//                lbl05.setText("<html><font color=red>\u258c</font><html>");
                lbl05.setToolTipText("КА срабатывание автоматов");
                                Log.log("+++!!! 05");
            } else {
                lbl05.setBackground(Color.GREEN);
//                lbl05.setText("<html><font color=green>\u258c</font><html>");
                lbl05.setToolTipText("КА автоматоы норма");
                                Log.log("+++!!! -05");
            }
        }
//-------------------------------
        if (this._06 != _06) {//GND1 (СВ) "земля на светофорах"
            this._06 = _06;
            if (_06) {
                lbl06.setBackground(Color.RED);
                lbl06.setToolTipText("земля на светофорах");
                                Log.log("+++!!! 06");
            } else {
                lbl06.setBackground(Color.GREEN);
                lbl06.setToolTipText("земля на светофорах норма");
                                Log.log("+++!!! -06");
            }
        }
//-------------------------------
        if (this._07 != _07) {//GND2 (СО) "земля на счете осей"
            this._07 = _07;
            if (_07) {
                lbl07.setBackground(Color.RED);
                lbl07.setToolTipText("земля на счете осей");
                                Log.log("+++!!! 07");
            } else {
                lbl07.setBackground(Color.GREEN);
                lbl07.setToolTipText("земля на счете осей норма");
                                Log.log("+++!!! -07");
            }
        }
//-------------------------------
        if (this._10 != _10) {//отказ электосети - сигнал от ИБП, 1- нет питания
            this._10 = _10;
            if (_10) {
                lbl10.setBackground(Color.RED);
                lbl10.setToolTipText("ИБП - нет питания");
                                Log.log("+++!!! 10");
            } else {
                lbl10.setBackground(Color.GREEN);
                lbl10.setToolTipText("ИБП - есть электричество");
                                Log.log("+++!!! -10");
            }
        }
//-------------------------------
        if (this._11 != _11) {//акк. разряжен - сигнал от ИБП, 1- акб разряжен
            this._11 = _11;
            if (_11) {
                lbl11.setBackground(Color.RED);
                lbl11.setToolTipText("акб. разряжен");
                                Log.log("+++!!! 11");
            } else {
                lbl11.setBackground(Color.GREEN);
                lbl11.setToolTipText("акк. заряжен");
                                Log.log("+++!!! -11");
            }
        }
//-------------------------------
        if (this._12 != _12) {//общая тревога - сигнал от ИБП, 1- тревога
            this._12 = _12;
            if (_12) {
                lbl12.setBackground(Color.RED);
                lbl12.setToolTipText("ИБП - тревога");
                                Log.log("+++!!! 12");
            } else {
                lbl12.setBackground(Color.GREEN);
                lbl12.setToolTipText("ИБП - нет тревоги");
                                Log.log("+++!!! -12");
            }
        }
    
//-------------------------------
        if (this._13 != _13) {//отказ ИБП - сигнал от ИБП, 1- отказ
            this._13 = _13;
            if (_13) {
                lbl13.setBackground(Color.RED);
                lbl13.setToolTipText("ИБП - отказ");
                                Log.log("+++!!! 12");
            } else {
                lbl13.setBackground(Color.GREEN);
                lbl13.setToolTipText("ИБП - нет отказа");
                                Log.log("+++!!! -13");
            }
        }
////-------------------------------
//        if (this._14 != _14) {//ДГА1 что-то от ДГА
//            this._14 = _14;
//            if (_14) {
//                lbl14.setBackground(Color.RED);
//                lbl14.setToolTipText("ДГА1 - 1");
//            } else {
//                lbl14.setBackground(Color.GREEN);
//                lbl14.setToolTipText("ДГА1 - 0");
//            }
//        }
////-------------------------------
//        if (this._15 != _15) {//ДГА2 ещё что-то от ДГА
//            this._15 = _15;
//            if (_15) {
//                lbl15.setBackground(Color.RED);
//                lbl15.setToolTipText("ДГА2 - 1");
//            } else {
//                lbl15.setBackground(Color.GREEN);
//                lbl15.setToolTipText("ДГА2 - 0");
//            }
//        }
////-------------------------------
//        if (this._16 != _16) {//У1 - чем-то управляем
//            this._16 = _16;
//            if (_16) {
//                lbl16.setBackground(Color.RED);
//                lbl16.setToolTipText("У1 - 1");
//            } else {
//                lbl16.setBackground(Color.GREEN);
//                lbl16.setToolTipText("У1 - 0");
//            }
//        }
////-------------------------------
//        if (this._17 != _17) {//У2 - ещё чем-то управляем
//            this._17 = _17;
//            if (_17) {
//                lbl17.setBackground(Color.RED);
//                lbl17.setToolTipText("У2 - 1");
//            } else {
//                lbl17.setBackground(Color.GREEN);
//                lbl17.setToolTipText("У2 - 0");
//            }
//        }
    }
    //==============================

    private void event(int id_msg) {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
    }

    private void alarm_on(int id) {
        if (getDtime() > 0) {
            Alarms.alarm_on(getDtime(), id, id_obj);
        }
    }

    private void alarm_off(int id) {
        if (getDtime() > 0) {
            Alarms.alarm_off(getDtime(), id, id_obj);
        }
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
        UsState u = (UsState) oid;
        this.setState(
                u.timestamp,
                u.NORMA,//OFF 1 - блокпост выключен
                u.HZ, //ALM 1 - есть неисправность на БП
                u._02,//ЧН 1 - установлено четное направление
                u._03,//НЧ 1 - установлено нечетное направление
                u._04,//ЧКО+НКО 1 - перегорание красного огня на светофорах
                u._05,//КА срабатывание автоматов
                u._06,//GND1 (СВ) "земля на светофорах"
                u._07,//GND2 (СО) "земля на счете осей"
                u._10,//отказ электосети - сигнал от ИБП, 1- нет питания
                u._11,//акк. разряжен - сигнал от ИБП, 1- акб разряжен
                u._12,//общая тревога - сигнал от ИБП, 1- тревога
                u._13,//отказ ИБП - сигнал от ИБП, 1- отказ
                u._14,//ДГА1 что-то от ДГА
                u._15,//ДГА2 ещё что-то от ДГА
                u._16,//У1 - чем-то управляем
                u._17 //У2 - ещё чем-то управляем
        );
    }
}

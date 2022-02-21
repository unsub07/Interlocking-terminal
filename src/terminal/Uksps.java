//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.UkspsState;

class Uksps extends Cell {
//    PropertyChangeListener listener1 = null;
//    PropertyChangeListener listener2 = null;
    // ------------------------------------------------------------------------
    private static final int LAYER = 7;
    private static final Color cmdIdleForeground = Color.DARK_GRAY;
    private static final Color cmdAlarmBackground = new Color(255, 204, 204);
    private static final Border cmdAlarmBorderLow = BorderFactory.createLineBorder(new Color(255, 0, 0), 2);
    private static final Border cmdAlarmBorderHigh = BorderFactory.createLineBorder(new Color(170, 0, 0), 2);
    private static final Border cmdIdleBorder = BorderFactory.createLineBorder(Color.DARK_GRAY);
    private static final Color cmdIdleBackground = new Color(220, 236, 250);

    // -------------------------------Translate---------------------------------
    private final String menu_title;

    private final Img lbl = new Img();
    private final javax.swing.JLabel lblName = new javax.swing.JLabel();
//комманды сначала 1 потом 0  - 2шт.
    private boolean ks;
    private boolean kzhz;
    private boolean kz;
    private boolean hz;
    private boolean norma;
    private boolean blink1;
    private boolean blink2;

    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();// меню
    private final javax.swing.JMenuItem off_ks_Item = new javax.swing.JMenuItem("<html><b><font color=orange>Отключить</font></b> контроль габарита</html>");//Отключить контроль габарита
    private final javax.swing.JMenuItem off_kz_Item = new javax.swing.JMenuItem("<html><b><font color=green>Сброс</font></b> неисправности (КЗ)</html>");//Сброс неисправности

    private final ActionListener UKSPS1 = (ActionEvent e) -> {
        blink1 = !blink1;
        if (blink1) {
            lblName.setBackground(Color.DARK_GRAY);
            lblName.setForeground(Color.YELLOW);
        } else {
            lblName.setBackground(Color.YELLOW);
            lblName.setForeground(Color.DARK_GRAY);
        }
    };
//    private final javax.swing.Timer blinkTimer1 = new javax.swing.Timer(600, blinkPerformer1);

    private final ActionListener UKSPS2 = (ActionEvent e) -> {
        blink2 = !blink2;
        if (blink2) {
            lblName.setBorder(cmdAlarmBorderLow);
        } else {
            lblName.setBorder(cmdAlarmBorderHigh);
        }
    };
//    private final javax.swing.Timer blinkTimer2 = new javax.swing.Timer(600, blinkPerformer2);

    Uksps(int ID_OBJ, int GX, int GY, int ORIENTATION, int VIDEO_STATUS, int SHIFT_X, int SHIFT_Y, String S_NAME) {
        super(ID_OBJ, S_NAME, GX, GY, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 84);//84 - UKSPS
//        s_name = S_NAME;
        menu_title = S_NAME;
        setPanes(ORIENTATION, VIDEO_STATUS);
    }

    private void setPanes(int ORIENTATION, int VIDEO_STATUS) {

        cmdX += Terminal.TunePngX;// -6
        cmdY += Terminal.TunePngY;// -6

        lblName.setText(s_name);
        int cmdW = lblName.getFontMetrics(lblName.getFont()).stringWidth(s_name);
        int cmdH = lblName.getFontMetrics(lblName.getFont()).getHeight();
        lblName.setFont(Terminal.SANS09);//zoom
        lblName.setForeground(cmdIdleForeground);
        lblName.setBounds(cmdX, cmdY, cmdW + 8, cmdH + 4);//fuck victor надо cmdX + cmdW + 8, cmdY + cmdH + 4
        lblName.setOpaque(true);
        lblName.setBorder(cmdIdleBorder);
        lblName.setBackground(cmdIdleBackground);
        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVerticalAlignment(javax.swing.JLabel.CENTER);
//        cmdCellPane.setLayer(cmdCellPane.add(lblCmdRnName), 1);
        lblName.setVisible(true);
        terminal.Commander.cmdLayers.add(lblName);
        terminal.Commander.cmdLayers.setLayer(lblName, LAYER);
// ---------------------------
        if ((Terminal.DSP || Terminal.SHN) && (VIDEO_STATUS == 0)) {// DSP и SHN
            Popup.setBorder(BorderFactory.createTitledBorder(menu_title));
            Popup.setBorderPainted(true);
            Popup.add(off_ks_Item);
            off_ks_Item.addActionListener((java.awt.event.ActionEvent evt) -> off_ks_ActionPerformed());
            Popup.addSeparator();
            Popup.add(off_kz_Item);
            off_kz_Item.addActionListener((java.awt.event.ActionEvent evt) -> off_kz_ActionPerformed());
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
        int orient;
        if (ORIENTATION>12){
            orient = ORIENTATION - 12;
        } else {
            orient = ORIENTATION;
        }
        Util.prep_lbl(lbl, LAYER, "uksps__" + String.valueOf(orient), cmdX, cmdY);
        lbl.setVisible(true);//default state
    }
    
    private void preparePopup() {
        off_ks_Item.setEnabled(ks && Status.work);
        off_kz_Item.setEnabled(kzhz && Status.work);
    }

    private void off_ks_ActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold> "
                + OPERATION
                + "!!</font><br>Подтвердите Отключение контроля габарита для <b>" + s_name + "</b></html>")) {//translate
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "UKSPS_OFF_KS1");
                pause();
                Net.sendMaskedCmd_DSP(id_obj, "UKSPS_OFF_KS0");
                break;
        }
    }

    private void off_kz_ActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(
                cmdX,
                cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold> "
                + OPERATION
                + "!!</font><br>Подтвердите Сброс неисправности (КЗ)<br> для <b>" + s_name + "</b></html>")) {//translate
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP_SHN(id_obj, "UKSPS_OFF_KZ1");
                pause();
                Net.sendMaskedCmd_DSP_SHN(id_obj, "UKSPS_OFF_KZ0");
                break;
        }
    }

    void setState(
            long DTIME,
            boolean KS0,//контроль схода одинаково с KS1 (Нарушение габарита)
            boolean KZHZ,//неисправность
    //        boolean KS1,//контроль схода одинаково с KS0 (Нарушение габарита)
            boolean KZ,//Неисправность - короткое замыкание
            boolean HZ,//Неисправность
            boolean NORMA//Устройство в норме
    ) {
        setDtime(DTIME);
// -----------------------------
        if (ks != KS0) {
            ks = KS0;
            if (KS0) { //тут рисуем
                event(372);//372	<html><font color=red>Нарушение габарита</html>	MESS_UKSPS_KS	INFO_MESS
                alarm_on(376);//376	Нарушение габарита	ALARM_UKSPS_KS	ALARM
//                if (!blinkTimer1.isRunning()) {
//                    blinkTimer1.start();
//                }
                Terminal.TIMER600.addActionListener(UKSPS1);
            } else {
                alarm_off(376);
//                if (blinkTimer1.isRunning()) {
//                    blinkTimer1.stop();
//                }
                Terminal.TIMER600.removeActionListener(UKSPS1);
                if (kzhz) {
                    lblName.setBackground(cmdAlarmBackground);
                } else {
                    lblName.setForeground(cmdIdleForeground);
                    lblName.setBackground(cmdIdleBackground);
                    lblName.setBorder(cmdIdleBorder);
                    lblName.repaint();
                }
            }
        }
//-------------------------------
        if (kzhz != KZHZ) {
            kzhz = KZHZ;
            if (KZHZ) {
                //тут рисуем
                lblName.setBackground(cmdAlarmBackground);
//                if (!blinkTimer2.isRunning()) {
//                    blinkTimer2.start();
//                }
Terminal.TIMER600.addActionListener(UKSPS2);
            } else {
//                if (blinkTimer2.isRunning()) {
//                    blinkTimer2.stop();
//                }
Terminal.TIMER600.removeActionListener(UKSPS2);
                lblName.setForeground(cmdIdleForeground);
                lblName.setBackground(cmdIdleBackground);
                lblName.setBorder(cmdIdleBorder);
                lblName.repaint();
            }
        }
//-------------------------------
        if (kz != KZ) {
            kz = KZ;
            if (KZ) {
                event(373);//373	<html><font color=red>Неисправность - короткое замыкание</html>	MESS_UKSPS_KZ	INFO_MESS
                alarm_on(377);//377	Неисправность - короткое замыкание	ALARM_UKSPS_KZ	ALARM
            } else {
                alarm_off(377);
            }
        }
//-------------------------------
        if (hz != HZ) {
            hz = HZ;
            if (HZ) {
                event(374);//374	<html><font color=red>Неисправность</html>	MESS_UKSPS_HZ	INFO_MESS
                alarm_on(378);//378	Неисправность	ALARM_UKSPS_HZ	ALARM
            } else {
                alarm_off(378);
            }
        }
//-------------------------------
        if (norma != NORMA) {
            norma = NORMA;
            if (NORMA) {
                event(375);//375	Устройство в норме	MESS_UKSPS_NORMA	INFO_MESS
            }
        }
    }

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
        UkspsState u = (UkspsState) oid;
        this.setState(
                u.timestamp,
                u.KS0,//контроль схода одинаково с KS1 (Нарушение габарита)
                u.KZHZ,//неисправность
//                u.KS1,//контроль схода одинаково с KS0 (Нарушение габарита)
                u.KZ,//Неисправность - короткое замыкание
                u.HZ,//Неисправность
                u.NORMA//Устройство в норме
        );
    }
 
}

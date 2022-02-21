//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
//зоны оповещения
package terminal;

import java.awt.Color;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.ProtectedZoneState;

class Protected extends Cell {
    // -------------------------------------------------------------------
    // Notice layers

    // --Commented out by Inspection (16.01.18 15:00):private static final int NOTICE_LAYER = 2;
    // static final int NOTICE_LR = 1;
    private static final int NOTICE_NAME_LR = 2;
    // private static final javax.swing.ImageIcon soundLoudIcon =
    // Terminal.mainPictureHash.get("sound_loud_btn");
    private static final javax.swing.ImageIcon nameIcon = Terminal.mainPictureHash.get("protected16x16");
    // static final int NOTICE_SOUND_LR = 3;
    private static final Border noticeNameBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    private static final java.awt.Cursor protected_cursor = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(Terminal.mainPictureHash.get("protected_cursor").getImage(), new java.awt.Point(0, 0), "protected_cursor");
    // -------------------------------Translate--------------------------
    private final String menu_title;
    private final boolean withName;

    private final String s_name;
//    private final javax.swing.JLabel lblCmdOff = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdOn = new javax.swing.JLabel();
private final Img lblCmdOff = new Img();
private final Img lblCmdOn = new Img();
    
    // private int activeNumbers; // перечень включенных зон (несколько из 16
    // управляемых лог. объектом)
    // private int blockedNumbers; // перечень блокированных зон (несколько из
    // 16 управляемых лог. объектом)
    // private final javax.swing.JLabel soundMsg = new javax.swing.JLabel();
    private final javax.swing.JLabel lblName = new javax.swing.JLabel();
    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();// меню
    private final JMenuItem Notification_On = new javax.swing.JMenuItem("<html>Оповещение выкл., <b><font color=orange>Включить</font></b></html>");
    private final JMenuItem Notification_Off = new javax.swing.JMenuItem("<html>Оповещение вкл., <b><font color=green>Выключить</font></b></html>");
    private final JMenuItem All_Notification_On = new javax.swing.JMenuItem("<html>Все зоны. Оповещение выкл., <b><font color=orange>Включить</font></b></html>");
    private final JMenuItem All_Notification_Off = new javax.swing.JMenuItem("<html>Все зоны. Оповещение вкл., <b><font color=green>Выключить</font></b></html>");
    // --Commented out by Inspection (16.01.18 15:00):private MouseAdapter adapter;
    private boolean block = false;
    private boolean ZoneOn = false;// default valeu
    private boolean AllZoneOn = false;// default value
    private boolean knm = true;// зона на охране
    private boolean cncl_knm = true;// зона снимается с охраны
    // --Commented out by Inspection (16.01.18 17:04):private boolean om;// включено оповещение (маршрут)
    private boolean special_notice = true;// немаршрутное передвижение
    private Timer Protected_Timer;
    private final int video_status;

    Protected(
    //        int ID_GOBJ,
            int ID_OBJ,
    //        int ID_AREA,
            String S_NAME,
    //        int IPAR,
            int GX,
            int GY,
            int SHIFT_X,
            int SHIFT_Y,
            int VIDEO_STATUS,
            int ORIENTATION,
    //        int LAYER,
            boolean WITHNAME
    ) {
        super(ID_OBJ, S_NAME, GX, GY, SHIFT_X, SHIFT_Y, 0, 23);// 23=PROTECTED
// -------------------------------Translate---------------------------
        menu_title = "<html><b>" + Util.objType(ID_OBJ) + " <font color=blue>" + S_NAME + "</font></b></html>";
//        String p_notif_on = Terminal.Translate("p_notif_on", "<html>Оповещение выкл., <b><font color=orange>Включить</font></b></html>");
//        String p_notif_off = Terminal.Translate("p_notif_off", "<html>Оповещение вкл., <b><font color=green>Выключить</font></b></html>");
//        String p_all_notifi_on = Terminal.Translate("p_all_notifi_on", "<html>Все зоны. Оповещение выкл., <b><font color=orange>Включить</font></b></html>");
//        String p_all_notif_off = Terminal.Translate("p_all_notif_off", "<html>Все зоны. Оповещение вкл., <b><font color=green>Выключить</font></b></html>");
        Notification_On.setText(p_notif_on);
        Notification_Off.setText(p_notif_off);
        All_Notification_On.setText(p_all_notifi_on);
        All_Notification_Off.setText(p_all_notif_off);
// -------------------------------------------------------------------
        s_name = S_NAME;
        withName = WITHNAME;
        video_status = VIDEO_STATUS;
        setPanes(ORIENTATION, VIDEO_STATUS);
    }

    private String cmdPicturePreffix(int ORIENTATION) {
        return "protected__" + String.valueOf(ORIENTATION);
    }

    private void setPanes(int ORIENTATION, int VIDEO_STATUS) {
        if (VIDEO_STATUS==0){
        int sx;
        int sy;
        cmdX += Terminal.TunePngX;// -6
        cmdY += Terminal.TunePngY;// -6
        switch (ORIENTATION) {
            case 3:// -\ наклонная
//                lblName.setBounds(6, 34, 40, 16);// 32,32,32,16
                sx = 6;
                sy = 34;
                break;
            case 6: // - | справа
//                lblName.setBounds(32, 32, 40, 16);
                sx = 32;
                sy = 32;
                break;
            case 9:// -/ наклонная
//                lblName.setBounds(32, 32, 40, 16);//
                sx = 32;
                sy = 32;
                break;
            case 12:// - _ снизу
//                lblName.setBounds(26, 62, 40, 16);
                sx = 26;
                sy = 62;
                break;
            case 15:// - \ наклонная как 3
//                lblName.setBounds(38, 30, 40, 16);//
                sx = 38;
                sy = 30;
                break;
            case 18:// - | слева
//                lblName.setBounds(6, 34, 40, 16);
                sx = 6;
                sy = 34;
                break;
            case 21:// - / наклонная как 9
//                lblName.setBounds(38, 36, 40, 16);//
                sx = 38;
                sy = 36;
                break;
            case 24:// _ сверху
//                lblName.setBounds(26, 6, 40, 16);
                sx = 26;
                sy = 6;
                break;
            default:
//                lblName.setBounds(0, 0, 40, 16);
                sx = 0;
                sy = 0;
                break;
        }

        if (withName) {
            lblName.setText(String.valueOf(s_name));
            lblName.setSize(40, 16);
            lblName.setToolTipText("Охранная зона " + String.valueOf(s_name));//translate
            lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            lblName.setVerticalAlignment(javax.swing.JLabel.CENTER);
            lblName.setBorder(noticeNameBorder);
            lblName.setBackground(Color.LIGHT_GRAY);
            lblName.setOpaque(true);
            lblName.setFont(Terminal.SANS12);//zoom
            lblName.setForeground(Color.BLUE);
            
            lblName.setIcon(nameIcon);
            lblName.setLocation(cmdX + sx, cmdY + sy);
            terminal.Commander.cmdLayers.add(lblName);
            terminal.Commander.cmdLayers.setLayer(lblName, NOTICE_NAME_LR);
//        lblName.setVisible(withName);
            // ---------------------------------Menu--------------------------------
            if (Terminal.DSP) {// Если ДСП && VIDEO_STATUS
                lblName.setCursor(protected_cursor);
                Popup.setBorder(BorderFactory.createTitledBorder(menu_title + s_name));
                Popup.add(Notification_On);
                Notification_On.addActionListener((java.awt.event.ActionEvent evt) -> Notification_On_ActionPerformed());
                Popup.add(Notification_Off);
                Notification_Off.addActionListener((java.awt.event.ActionEvent evt) -> Notification_Off_ActionPerformed());
                Popup.addSeparator();
                Popup.add(All_Notification_On);
                All_Notification_On.addActionListener((java.awt.event.ActionEvent evt) -> All_Notification_On_ActionPerformed());
                Popup.add(All_Notification_Off);
                All_Notification_Off.addActionListener((java.awt.event.ActionEvent evt) -> All_Notification_Off_ActionPerformed());
                Popup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        // if(StatusCheck.isOkStatus()){}
                        preparePopup();
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                });

                lblName.setComponentPopupMenu(Popup);
            }// END DSP        

            // ------------------------------Timer----------------------------------
            Protected_Timer = new Timer(GX, GY, shift_x + lblName.getX(), shift_y + lblName.getY() + lblName.getHeight());
        }//end with name

        Util.prep_lbl(lblCmdOff, 1, cmdPicturePreffix(ORIENTATION) + "__off", cmdX, cmdY);
        Util.prep_lbl(lblCmdOn, 1, cmdPicturePreffix(ORIENTATION) + "__on", cmdX, cmdY);

        // -----------------Set Default State--------------------------------
        setState(
                0,
                0,
                false,
                false,
//                true,//OM
                false,
                false
        );
        }
    }

    void setState(
            long DTIME,
            int PROTECTED_TIME,
            boolean KNM,
            boolean CNCL_KNM,
    //        boolean OM,
            boolean SPECIAL_NOTICE,
            boolean BLOCK
    ) {
        setDtime(DTIME);
        if (withName) {
            /*
         * 0 PROTECTED_ZONE_KNM_OFF Выкл. оповещение по зоне
         * MESS_PROTECTED_ZONE_OFF 275 1 PROTECTED_ZONE_KNM_ON Включено
         * оповещение по зоне MESS_PROTECTED_ZONE_ON 276
             */
            if (knm != KNM) {
                knm = KNM;
                ZoneOn = KNM;
                if (KNM) {// true 1 PROTECTED_ZONE_KNM_ON Включено оповещение по зоне MESS_PROTECTED_ZONE_ON 276
                    if (SPECIAL_NOTICE) {
                        lblName.setBackground(Color.WHITE);// включить желто белую мигалку.
                        lblName.setForeground(Color.MAGENTA);
                    } else {
                        lblName.setBackground(new Color(153, 255, 255));
                        lblName.setForeground(Color.BLUE);
                    }
//                lblCmdOff.setVisible(false);
//                lblCmdOn.setVisible(true);
                    event(276);
                } else {// false 0 PROTECTED_ZONE_KNM_OFF Выкл. оповещение по зоне // MESS_PROTECTED_ZONE_OFF 275
                    if (SPECIAL_NOTICE) {
                        lblName.setBackground(Color.WHITE);// включить желто белую мигалку.
                        lblName.setForeground(Color.MAGENTA);
                    } else {
                        lblName.setBackground(Color.LIGHT_GRAY);
                        lblName.setForeground(Color.BLACK);

                    }
//                lblCmdOff.setVisible(true);
//                lblCmdOn.setVisible(false);
                    event(275);
                }
            }
            /*
         * 0 PROTECTED_ZONE_CNCL_KNM_OFF 1 PROTECTED_ZONE_CNCL_KNM_ON Идет
         * снятие зоны с охраны
             */
//        if (withName) {// boolean
            if (cncl_knm != CNCL_KNM) {
                cncl_knm = CNCL_KNM;
                if (CNCL_KNM) {// true 1 PROTECTED_ZONE_CNCL_KNM_ON Идет снятие зоны с охраны
                    // Protected_Timer.setState((PROTECTED_TIME >> 8) +
                    // (PROTECTED_TIME & 0x000000ff << 8));//Siemens
                    Protected_Timer.setState(PROTECTED_TIME);
                } else {// false 0 PROTECTED_ZONE_CNCL_KNM_OFF
                    Protected_Timer.setState(0);
                }
            }
//        }
            /*
         * 1 PROTECTED_ZONE_OM_ON включено оповещение (маршрут) 0
         * PROTECTED_ZONE_OM_OFF

            if (om != OM) {
                om = OM;
                if (OM) {// 1 PROTECTED_ZONE_OM_ON включено оповещение (маршрут)

                } else {// 0 PROTECTED_ZONE_OM_OFF

                }
            }
            */
            if (block != BLOCK) {
                block = BLOCK;
            }
            /*
         * 1 PROTECTED_ZONE_SPECIAL_NOTICE_ON немаршрутное передвижение 0
         * PROTECTED_ZONE_SPECIAL_NOTICE_OFF немаршрутное передвижение
             */
            if (special_notice != SPECIAL_NOTICE) {
                special_notice = SPECIAL_NOTICE;
                AllZoneOn = SPECIAL_NOTICE;
                if (SPECIAL_NOTICE) {// 1 PROTECTED_ZONE_SPECIAL_NOTICE_ON немаршрутное передвижение
                    lblName.setBackground(Color.WHITE);// включить желто белую мигалку.
                    lblName.setForeground(Color.MAGENTA);
                } else// 0 PROTECTED_ZONE_SPECIAL_NOTICE_OFF немаршрутное передвижение
                {
                    if (KNM) {
                        lblName.setBackground(new Color(153, 255, 255));
                        lblName.setForeground(Color.BLUE);
                    } else {
                        lblName.setBackground(Color.LIGHT_GRAY);
                        lblName.setForeground(Color.BLACK);
                    }
                }
            }
        }//end if with name
//        if (knm != KNM) {
        if (KNM) {
            lblCmdOff.setVisible(false);
            lblCmdOn.setVisible(true);
        } else {
            lblCmdOff.setVisible(true);
            lblCmdOn.setVisible(false);
        }
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="preparePopup">
    private void preparePopup() {
        Notification_On.setEnabled(!ZoneOn & !block);
        Notification_Off.setEnabled(ZoneOn & !block);
        All_Notification_On.setEnabled(!AllZoneOn);
        All_Notification_Off.setEnabled(AllZoneOn);
    }
    // </editor-fold>

    private void Notification_On_ActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "PROTECTED_ZONE_ON");
    }

    private void Notification_Off_ActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "PROTECTED_ZONE_OFF");
    }

    private void All_Notification_On_ActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "PROTECTED_ZONE_ALL_ON"); // 4 вкл.
    }

    private void All_Notification_Off_ActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "PROTECTED_ZONE_ALL_OFF");// 8 выкл.
    }

// --Commented out by Inspection START (16.01.18 15:00):
//    public void disablePopupMenu() {
//        Popup.removeMouseListener(adapter);
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)

// --Commented out by Inspection START (16.01.18 15:00):
//    public void enablePopupeMenu() {
//        Popup.addMouseListener(adapter);
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)

    private void event(int id_msg) {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
    }

// --Commented out by Inspection START (16.01.18 15:00):
//    private void alarm_on(int id) {
//        if (getDtime() > 0) {
//            Alarms.alarm_on(getDtime(), id, id_obj);
//        }
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)

// --Commented out by Inspection START (16.01.18 15:00):
//    private void alarm_off(int id) {
//        if (getDtime() > 0) {
//            Alarms.alarm_off(getDtime(), id, id_obj);
//        }
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)

    @Override
    public void setState(Object oid) { 
        if (video_status == 0){
        ProtectedZoneState p = (ProtectedZoneState) oid;
        Terminal.ProtectedCell_Hash.values().stream().filter((Protected pr) -> pr.id_obj == p.objId).forEach((pr) -> pr.setState(
                p.timestamp,
                p.PROTECTED_TIME,
                p.KNM,
                p.CNCL_KNM,
//                p.OM,
                p.SPECIAL_NOTICE,
                p.BLOCK
        ));
//
//        this.setState(
//                p.timestamp,
//                p.PROTECTED_TIME,
//                p.KNM,
//                p.CNCL_KNM,
//                p.OM,
//                p.SPECIAL_NOTICE,
//                p.BLOCK
//        );
        }
    }
}

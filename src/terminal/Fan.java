//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.FanState;
import static terminal.Commander.cmdLayers;

class Fan extends Cell {

    private static final java.awt.Color BKG_COLOR = new java.awt.Color(140, 255, 100, 80);
    private static final Border cmdAreaNameBorder2 = BorderFactory.createLineBorder(new Color(150, 180, 210));
    // иконки
    private static final ImageIcon ico0 = Terminal.mainPictureHash.get("FAN_OFF");
    private static final ImageIcon ico1 = Terminal.mainPictureHash.get("FAN_LITE");
    private final javax.swing.JLayeredPane cmdCellPane = new javax.swing.JLayeredPane();
    private final JLabel lblCmdFanDescr = new JLabel();
    private final JLabel lblICON = new JLabel();
    private final JMenuItem Fan_Lite_On = new javax.swing.JMenuItem(
            "<html>Облегченная обдувка., <b><font color=orange>Включить</font></b></html>");
    private final JMenuItem Fan_Norm_On = new javax.swing.JMenuItem(
            "<html>Нормальная обдувка., <b><font color=orange>Включить</font></b></html>");
    private final JMenuItem Fan_Hard_On = new javax.swing.JMenuItem(
            "<html>Усиленная обдувка., <b><font color=orange>Включить</font></b></html>");
    private final JMenuItem Fan_Off = new javax.swing.JMenuItem(
            "<html>Отключение обдувки., <b><font color=green>Выключить</font></b></html>");
    private boolean fan_lite = true;
    private boolean fan_norm = true;
    private boolean fan_hard = true;

    Fan(
            int ID_OBJ, //0=ID_OBJ
    //        int ID_AREA, //1=ID_AREA
            String S_NAME, //2=S_NAME
    //        String MPC_NAME, //3=MPC_NAME
            int X, //4=X
            int Y, //5=Y
            int SHIFT_X, //6=SHIFT_X
            int SHIFT_Y, //7=SHIFT_Y
            int VIDEO_STATUS //8=VIDEO_STATUS
    //        int IND, //9=IND
    //        int CMD //10=CMD
    ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, 0, 1);//24 - FAN
        // -------------------------------Translate--------------------------------------
        String menu_title = "<html><b><font color=blue>" + S_NAME + "</font></b></html>";

        Fan_Lite_On.setText(f_menu_item_light);
        Fan_Norm_On.setText(f_menu_item_norm);
        Fan_Hard_On.setText(f_menu_item_hard);
        Fan_Off.setText(f_menu_item_off);
        // <editor-fold defaultstate="collapsed" desc="setPanes">
        int cmdH = 70;
        int cmdW = 200;

        cmdCellPane.setSize(cmdW, cmdH);
        cmdCellPane.setLocation(cmdX, cmdY);
        cmdCellPane.setBackground(BKG_COLOR);
        cmdCellPane.setBorder(cmdAreaNameBorder2);
        cmdCellPane.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
        setCmdLayers();
        if (VIDEO_STATUS == 0){
            cmdCellPane.setVisible(true);
        } else {
            cmdCellPane.setVisible(false);
        }
        JLabel lblCmdFanName = new JLabel();
        lblCmdFanName.setText(S_NAME);
        lblCmdFanName.setPreferredSize(new Dimension(cmdW, lblCmdFanName.getFontMetrics(Terminal.SANS12).getHeight()));
        lblCmdFanName.setHorizontalAlignment(JLabel.CENTER);
        lblCmdFanName.setVerticalAlignment(JLabel.TOP);
        lblCmdFanName.setFont(Terminal.SANS12);

        String descr = "";
        lblCmdFanDescr.setText(descr);
        lblCmdFanDescr.setHorizontalAlignment(JLabel.CENTER);
        lblCmdFanDescr.setVerticalAlignment(JLabel.BOTTOM);

        cmdCellPane.add(lblCmdFanName);
        cmdCellPane.setLayer(lblCmdFanName, 2);

        cmdCellPane.add(lblCmdFanDescr);
        cmdCellPane.setLayer(lblCmdFanDescr, 2);

        lblICON.setFocusable(false);
        lblICON.setBorder(null);

        // ---------------------------------Menu-----------------------------------------
        if (Terminal.DSP || Terminal.SHN) {// Если ДСП && VIDEO_STATUS
            
            if (vStatus == 0){
            JPopupMenu popup = new JPopupMenu();
            popup.setBorder(BorderFactory.createTitledBorder(menu_title));

            popup.add(Fan_Lite_On);
            Fan_Lite_On.addActionListener((ActionEvent evt) -> Fan_Lite_On_ActionPerformed());
            
            popup.add(Fan_Norm_On);
            Fan_Norm_On.addActionListener((java.awt.event.ActionEvent evt) -> Fan_Norm_On_ActionPerformed());
            
            popup.add(Fan_Hard_On);
            Fan_Hard_On.addActionListener((java.awt.event.ActionEvent evt) -> Fan_Hard_On_ActionPerformed());
            
            popup.addSeparator();
            popup.add(Fan_Off);
            
            Fan_Off.addActionListener((java.awt.event.ActionEvent evt) -> Fan_Off_ActionPerformed());
            popup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
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

            lblICON.setComponentPopupMenu(popup);
            }//end video_status
        }// END DSP
        // ------------------------------end menu----------------------------------------

        // добавим кнопочки
        cmdCellPane.add(lblICON);
        cmdCellPane.setLayer(lblICON, 2);
        // -------------------------Set Default State-------------------------------------
        lblCmdFanDescr.setText("Обдувка Выкл.");
        lblICON.setIcon(ico0);
        lblICON.setToolTipText("Обдувка Выкл.");
        // </editor-fold>
    }

    private void setCmdLayers() {
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, CELLPANE_LAYER_10);//10
    }

    private void Fan_Lite_On_ActionPerformed() {// обработка кнопки FAN_LITE
        Net.sendMaskedCmd_DSP_SHN(id_obj, "FAN_LITE_ON");
//        Net.sendMaskedCmd_DSP(id_obj, "FAN_LITE_ON");
        Log.log(id_obj + " FAN_LITE_ON");
    }

    private void Fan_Norm_On_ActionPerformed() {// обработка кнопки FAN_NORM
        Net.sendMaskedCmd_DSP_SHN(id_obj, "FAN_NORM_ON");
//        Net.sendMaskedCmd_DSP(id_obj, "FAN_NORM_ON");
        Log.log(id_obj + " FAN_NORM_ON");
    }

    private void Fan_Hard_On_ActionPerformed() {// обработка кнопки FAN_HARD
        Net.sendMaskedCmd_DSP_SHN(id_obj, "FAN_HARD_ON");
//        Net.sendMaskedCmd_DSP(id_obj, "FAN_HARD_ON");
        Log.log(id_obj + " FAN_HARD_ON");
    }

    private void Fan_Off_ActionPerformed() {// обработка кнопки FAN_OFF
        Net.sendMaskedCmd_DSP_SHN(id_obj, "FAN_OFF");
//        Net.sendMaskedCmd_DSP(id_obj, "FAN_OFF");
        Log.log(id_obj + " FAN_OFF");
    }

    
    private void preparePopup() {
        Fan_Lite_On.setEnabled(!fan_lite && Status.work);
        Fan_Norm_On.setEnabled(!fan_norm && Status.work);
        Fan_Hard_On.setEnabled(!fan_hard && Status.work);
        Fan_Off.setEnabled((fan_lite || fan_norm || fan_hard) && Status.work);
    }

    void setState(
            long DTIME,
            boolean FAN_LITE,
            boolean FAN_NORM,
            boolean FAN_HARD
//            ,
//            boolean FAN_OFF
    ) {
        if (vStatus == 0){
boolean        FAN_OFF = !FAN_LITE & !FAN_NORM & !FAN_HARD;
        
//        System.out.println("FAN - " + FAN_LITE + " " + FAN_NORM + " " + FAN_HARD + " " + FAN_OFF);
 
        setDtime(DTIME);
    //    if (fan_lite != FAN_LITE) {
            fan_lite = FAN_LITE;
//            boolean fan_off = false;
            if (FAN_LITE) {// MESS_FAN_LITE_ON - 239
                lblCmdFanDescr.setText("Облегч. обдувка");
                lblICON.setIcon(ico1);
                lblICON.setToolTipText("Облегч. обдувка");
                event(239);
            }
    //    }
    //    if (fan_norm != FAN_NORM) {
            fan_norm = FAN_NORM;
            if (FAN_NORM) {// MESS_FAN_NORM_ON - 240
                lblCmdFanDescr.setText("Норм. обдувка");
                lblICON.setIcon(ico1);
                lblICON.setToolTipText("Норм. обдувка");
                event(240);
            }
    //    }
    //    if (fan_hard != FAN_HARD) {
            fan_hard = FAN_HARD;
            if (FAN_HARD) {// MESS_FAN_HARD_ON -238
                lblCmdFanDescr.setText("Усил. обдувка");
                lblICON.setIcon(ico1);
                lblICON.setToolTipText("Усил. обдувка");
                event(238);
            }
    //    }
        
    //    if (fan_off != FAN_OFF) {
            if (FAN_OFF) {// FAN_OFF - 241
                lblCmdFanDescr.setText("Обдувка Выкл.");
                lblICON.setIcon(ico0);
                lblICON.setToolTipText("Обдувка Выкл.");
                event(241);
            }
    //    }
        }//end vStatus
    }

    private void event(int id_msg) {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
    }

    void SetCmd(boolean b) {
        
    }
    
    @Override
    public void setState(Object oid) {
        FanState f = (FanState) oid;
        this.setState(
                f.timestamp,
                f.FAN_LITE,
                f.FAN_NORM,
                f.FAN_HARD
//                ,
//                f.FAN_OFF
        );
    }
}

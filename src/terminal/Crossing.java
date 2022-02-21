//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
//нужно переделать CrossingNameCell - с менюшкой название , и просто тупые картинки CrossingCell, которые управляются из первой.
/*
 1-FUTURE-строительство, 2-INVISIBLE-невидимый, 3-DEPRECATE-н.е.н.х.
 */
package terminal;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.CrossingState;

class Crossing extends Cell {

    private static final int CROSSING_FREE_LR = 9;//+8
    private static final int CROSSING_LEFT_NOTICE_LR = 10;//+8
    private static final int CROSSING_RIGHT_NOTICE_LR = 11;//+8
    private static final int CROSSING_DOUBLE_NOTICE_LR = 12;//+8
    private static final int CROSSING_BOOM_LR = 13;//+8
    private static final int CROSSING_BARRIER_LR = 14;//+8
    private static final int CROSSING_FAULT_LR = 15;//+8
    private static final int CROSSING_ALARM_LR = 16;//+8
//    private static final int CROSSING_LAYER = 8;

    private static final java.awt.Cursor CR_CURSOR = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(Terminal.mainPictureHash.get("cr_cursor").getImage(), new java.awt.Point(0, 0), "cr_cursor");
    private final boolean label_show;
    // -------------------------------Translate---------------------------------
    private final String menu_title;

    // -------------------------------------------------------------------------
//    private final int vStatus;

    private final int inPut;// input direction
    private final int outPut;// output direction
//    private final String s_name;// имя объекта

    private final javax.swing.JLabel lblName = new javax.swing.JLabel();

//    private final javax.swing.JLabel lblCmdFree = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdDoubleNotice = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdLeftNotice = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdRightNotice = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdBarrier = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdFault = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdCrash = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdBoom = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdWarning = new javax.swing.JLabel();
    
    private final Img lblCmdFree = new Img();
    private final Img lblCmdFuture = new Img();
    private final Img lblCmdDoubleNotice = new Img();
    private final Img lblCmdLeftNotice = new Img();
    private final Img lblCmdRightNotice = new Img();
    private final Img lblCmdBarrier = new Img();
    private final Img lblCmdFault = new Img();
    private final Img lblCmdCrash = new Img();
    private final Img lblCmdBoom = new Img();
    private final Img lblCmdWarning = new Img();

    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();
    private final javax.swing.JMenuItem openItem = new javax.swing.JMenuItem();
    private final javax.swing.JMenuItem closeItem = new javax.swing.JMenuItem();
    private final javax.swing.JMenuItem zg_on_Item = new javax.swing.JMenuItem();
    private final javax.swing.JMenuItem zg_off_Item = new javax.swing.JMenuItem();    
    private final javax.swing.JMenuItem block_on = new javax.swing.JMenuItem();// разрешить подачу извещения
    private final javax.swing.JMenuItem block_off = new javax.swing.JMenuItem();// запретить подачу извещения
    // private boolean noticed = false;// Default value
    private boolean notice_blok = true;// запрещено или разрешено подача  извещения.
    private boolean w_bloking_notice = false;// тип переезда = 5 (это как 1 только с запретом подачи  извещения на переезд)
    private boolean closed = true;// Default state "CLOSEDSTATE"
    private boolean boom = false; // Default state "BOOMSTATE"
    private int notice;// Default state "NOTICESTATE"
    private int fault = 1; // Default state "FAULTSTATE"
    private boolean manual = true; // Default state "MANUAL" //1 NO_MANUAL Нет ручного извещения
    private boolean pereg = true;// def Неисправность схемы горения ламп
    private boolean nozp = true;// def Неисправность схемы реле ЗП(ИП)
    private boolean netmg = true;// def Неисправность схемы мигания
    private final boolean zg_cmd; //ZG_CMD есть команда включить заграждение с арма
    
    private boolean pa;//несиправность схемы питания
    private boolean knb;//Батарея разряжена
    private boolean kso;//Неисправность счета осей  
        
    private final boolean azot = Terminal.crossing;//-----------------------------ДЛЯ АЗОТА (нет в контроллере MANUAL)--
    private final int ruled;

    // --Commented out by Inspection (16.01.18 15:00):int byteNumber;
    // --Commented out by Inspection (16.01.18 15:00):int bitNumber;
    
    private final int id_gobj;

    Crossing(
            int ID_GOBJ, //0=ID_OBJ
    //        int ID_AREA, //1=ID_AREA
            String S_NAME, //2=S_NAME
    //        String MPC_NAME, //3=MPC_NAME
            int X, //4=X
            int Y, //5=Y
            int SHIFT_X, //6=SHIFT_X
            int SHIFT_Y, //7=SHIFT_Y
            int VIDEO_STATUS, //8=VIDEO_STATUS
            int ORIENTATION, //9=ORIENTATION
            int SIDE, //10=SIDE
            boolean HONESTODD,
            //            boolean CONTROLLED,
            int RULED, //crossing_type ??
    //        int LAYER,
    //        int IND,
    //        int CMD,
            boolean ZG_CMD,
            int ID_OBJ
    ) {
        super(ID_GOBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 10);// 10=CROSSING
        id_gobj = ID_OBJ;
        // -------------------------------Translate-----------------------------

        menu_title = "<html><b><font color=blue>" + S_NAME + "</font></b></html>";

        openItem.setText(c_open_menu_item);
        closeItem.setText(c_close_menu_item);
        
        zg_on_Item.setText("<html>ЗАГРАЖДЕНИЕ <font color=green>Включить</font></html>"); //ответственная
        zg_off_Item.setText("<html>ЗАГРАЖДЕНИЕ <font color=red>Выключить</font></html>");

        inPut = Math.min(ORIENTATION, SIDE);
        outPut = Math.max(ORIENTATION, SIDE);
        zg_cmd = ZG_CMD;
//        s_name = S_NAME;
//        vStatus = VIDEO_STATUS;
        label_show = needToShowSName();
        ruled = RULED;
        switch (RULED) {// integer (CROSSING_TYPE)
            case 0:
                openItem.setText("извещение подано, СНЯТЬ");
                closeItem.setText("извещение снято, ПОДАТЬ");
                break;
            case 1:
                openItem.setText("переезд закрыт, ОТКРЫТЬ");
                closeItem.setText("переезд открыт, ЗАКРЫТЬ");
                break;
            case 5:
                openItem.setText("переезд закрыт, ОТКРЫТЬ");
                closeItem.setText("переезд открыт, ЗАКРЫТЬ");
                w_bloking_notice = true;
                block_on.setText(c_disable_notif_menu_item);
                block_off.setText(c_enable_notif_menu_item);
                break;
            default:
                break;
        }

        // openItem.setText(((RULED == 1 ) ? "переезд закрыт, ОТКРЫТЬ" :
        // "извещение подано, СНЯТЬ"));
        // closeItem.setText(((RULED == 1) ? "переезд открыт, ЗАКРЫТЬ" :
        // "извещение снято, ПОДАТЬ"));
        setPanes(HONESTODD, RULED, VIDEO_STATUS);
    }

    private boolean needToShowSName() {// а нужно ли выводить имя?
        return ((inPut == 1) && (outPut == 1))
                || ((inPut == 2) && (outPut == 2))
                || ((inPut == 24) && (outPut == 24) || (inPut == 6)
                && (outPut == 6));
    }

    private String cmdPicturePreffix() {
        return "crossing__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__";
    }

// RULED (0 - не управляемый , 1 - управляемый, 3 - контролируемый, 4 - не котролируемый, 5 - управляемый с отключателем извещения    
    private void setPanes(boolean HONESTODD, int RULED, int VIDEO_STATUS) {
        if (VIDEO_STATUS == 0){
        cmdX += Terminal.TunePngX;//-6
        int shiftH = 0;
        cmdY += Terminal.TunePngY - shiftH;
    
        if (w_bloking_notice) {// если тип переезда = 5 - блокируемая подача извещения
            lblCmdWarning.setIcon(Terminal.mainPictureHash.get("warn1"));
            lblCmdWarning.setToolTipText("Авотматическая подача Извещения ОТКЛЮЧЕНА!");
            // lblCmdWarning.setFont(lblCmdSNameFont);
            // lblCmdWarning.setForeground(Color.BLACK);
            lblCmdWarning.setBorder(BorderFactory.createLineBorder(Color.RED));
//            lblCmdWarning.setHorizontalAlignment(javax.swing.JLabel.LEFT);
            // lblCmdWarning.setBackground(new Color(255, 255, 255));
            // lblCmdWarning.setOpaque(true);
            lblCmdWarning.setVisible(true);
        }
        lblName.setText(s_name);//
//-------------------------------MENU----------------------------------
        if (Terminal.DSP && VIDEO_STATUS == 0) {// Если ДСП
            Popup.setBorder(BorderFactory.createTitledBorder(menu_title));
            Popup.setBorderPainted(true);
            Popup.add(openItem);
            openItem.addActionListener((java.awt.event.ActionEvent evt) -> openItemActionPerformed());
            Popup.add(closeItem);
            closeItem.addActionListener((java.awt.event.ActionEvent evt) -> closeItemActionPerformed());
//-------------------если есть в таблице признак команда на заграждение ZG_CMD=1
            if (zg_cmd) {
                Popup.addSeparator();
                Popup.add(zg_on_Item);
                zg_on_Item.addActionListener((java.awt.event.ActionEvent evt) -> zg_on_ItemActionPerformed());
                Popup.add(zg_off_Item);
                zg_off_Item.addActionListener((java.awt.event.ActionEvent evt) -> zg_off_ItemActionPerformed());
            }

//---------------------------------здесь надо добавить 2 пункта меню---
            if (w_bloking_notice) {// если тип переезда = 5 - блокируемая подача извещения
                Popup.addSeparator();
                Popup.add(block_on);
                block_on.addActionListener((java.awt.event.ActionEvent evt) -> block_onActionPerformed());
                Popup.add(block_off);
                block_off.addActionListener((java.awt.event.ActionEvent evt) -> block_offActionPerformed());
            }
//            if (Util.GetUserGroups("SIM")) {
//                    Popup.addSeparator();
//                    rnSimBusyItem.addActionListener(this::rnSimBusyItemActionPerformed);
//                    Popup.add(rnSimBusyItem);
//                    rnSimUnBusyItem.addActionListener(this::rnSimUnBusyItemActionPerformed);
//                    Popup.add(rnSimUnBusyItem);
//            }
            // -----------------------------------------------------------------
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

            Popup.setVisible(false);
            // RULED (0 - не управляемый , 1 - управляемый, 3 - контролируемый, 4 - не котролируемый, 5 - управляемый с отключателем извещения
            if (RULED != 4) {
                // Для ЧЭМК раскоментачить по заключению договора выставить
                // номер процессора индикации 1 и адрес упровления 262
                if (Terminal.DSP && vStatus == 0) {
                    lblName.setComponentPopupMenu(Popup);
                    lblName.setCursor(CR_CURSOR);
                }
            }
        }// end DSP
        // --------------------------END MENU----------------------------------

        lblName.setFont(Terminal.SANS11);//zoom
        switch (Terminal.zoom) {
            case 72:
                lblName.setFont(Terminal.SANS11);//zoom
                break;
            case 56:
                lblName.setFont(Terminal.SANS10);//zoom
                break;
            case 36:
                lblName.setFont(Terminal.SANS08);//zoom
                break;
        }
        
        
        
        if (needToShowSName()) {
            int nameW = lblName.getFontMetrics(lblName.getFont()).stringWidth(lblName.getText()) + 4;
            int nameH = lblName.getFontMetrics(lblName.getFont()).getHeight() + 4;

            // int yW =
            // lblCmdWarning.getFontMetrics(lblCmdWarning.getFont()).stringWidth(lblCmdWarning.getText())
            // + 4;
            // int xH =
            // lblCmdWarning.getFontMetrics(lblCmdWarning.getFont()).getHeight()
            // + 4;
            if ((inPut == 2) && (outPut == 2) || (inPut == 6) && (outPut == 6)) {
                lblName.setBounds(cmdX + 96, cmdY + 48, nameW, nameH);
                lblCmdWarning.setBounds(cmdX + 80, cmdY + 48, 16, 16);
            } else {
                int xx = 0;
                if (w_bloking_notice) {
                    xx = 16;
                }
                lblName.setBounds(cmdX + xx, cmdY - nameH, nameW, nameH);
                lblCmdWarning.setBounds(cmdX, cmdY - nameH, 16, 16);
            }

            lblName.setForeground(Color.BLUE);
            lblName.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            lblName.setHorizontalAlignment(javax.swing.JLabel.LEFT);

        }

// RULED (0 - не управляемый , 1 - управляемый, 3 - контролируемый, 4 - не котролируемый, 5 - управляемый с отключателем извещения        
        if (needToShowSName() && (RULED != 4)) {
//            lblName.setLocation(cmdX + shiftW + 80, cmdY + shiftH);//?????????????????????????????????
            terminal.Commander.cmdLayers.add(lblName);
            terminal.Commander.cmdLayers.setLayer(lblName, CROSSING_FREE_LR);
            if (w_bloking_notice) {// если тип переезда = 5 - блокируемая подача извещения
//                lblCmdWarning.setLocation(cmdX + shiftW + 80, cmdY + shiftH);//??????????????????????????
                terminal.Commander.cmdLayers.add(lblCmdWarning);
                terminal.Commander.cmdLayers.setLayer(lblCmdWarning, CROSSING_FREE_LR);
            }
        }

        String ln = (HONESTODD) ? "left_notice" : "right_notice";
        String rn = (HONESTODD) ? "right_notice" : "left_notice";

        Util.prep_lbl(lblCmdFree, CROSSING_FREE_LR, cmdPicturePreffix() + "free", cmdX, cmdY);
        Util.prep_lbl(lblCmdDoubleNotice, CROSSING_DOUBLE_NOTICE_LR, cmdPicturePreffix() + "double_notice", cmdX, cmdY);
        Util.prep_lbl(lblCmdRightNotice, CROSSING_RIGHT_NOTICE_LR, cmdPicturePreffix() + rn, cmdX, cmdY);
        Util.prep_lbl(lblCmdLeftNotice, CROSSING_LEFT_NOTICE_LR, cmdPicturePreffix() + ln, cmdX, cmdY);
        Util.prep_lbl(lblCmdBarrier, CROSSING_BARRIER_LR, cmdPicturePreffix() + "barrier", cmdX, cmdY);
        Util.prep_lbl(lblCmdFault, CROSSING_FAULT_LR, cmdPicturePreffix() + "fault", cmdX, cmdY);//gif
        Util.prep_lbl(lblCmdCrash, CROSSING_ALARM_LR, cmdPicturePreffix() + "crash", cmdX, cmdY);//gif
        Util.prep_lbl(lblCmdBoom, CROSSING_BOOM_LR, cmdPicturePreffix() + "boom", cmdX, cmdY);

        // -------------------Set Default State---------------------------------
        lblCmdBoom.setVisible(false);// BOOM - не используется в сименсе всегда приколочен в 1
        lblCmdFree.setVisible(true);
        setState(0, false, true, 3, 0, true, false, false, false, false, false,false,false);// -----------------------------------------Убрать нахуй! отсюда 
        } else {
        Util.prep_lbl(lblCmdFuture, CROSSING_FREE_LR, cmdPicturePreffix() + "future", cmdX, cmdY);
        lblCmdFuture.setVisible(true);
        }
        openItem.setFont(Terminal.SANS14);
        closeItem.setFont(Terminal.SANS14);
        zg_on_Item.setFont(Terminal.SANS14);
        zg_off_Item.setFont(Terminal.SANS14);
        block_on.setFont(Terminal.SANS14);
        block_off.setFont(Terminal.SANS14);
    }

    private void preparePopup() {
        openItem.setEnabled(!manual && Status.work);
        closeItem.setEnabled(manual && Status.work);
        if (zg_cmd){
            zg_on_Item.setEnabled(!boom && Status.work);
            zg_off_Item.setEnabled(boom && Status.work);
        }
        if (w_bloking_notice) {// если тип переезда = 5 - блокируемая подача
            // извещения
            block_on.setEnabled(!notice_blok && Status.work);
            block_off.setEnabled(notice_blok && Status.work);
            // block_on.setEnabled(true);
            // block_off.setEnabled(true);
        }
    }

    private void openItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_gobj, "CROSSING_OPEN_1");
        pause();
        Net.sendMaskedCmd_DSP(id_gobj, "CROSSING_OPEN_2");
        if (azot) {
//            Terminal.CrossingCell_Hash.values().stream().filter((CrossingCell cc) -> (cc.id_obj == id_obj)).forEach((cc) -> {
            set_manual(true);//1 NO_MANUAL Нет ручного извещения
//            });
        }
    }

    private void closeItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_gobj, "CROSSING_CLOSE_1");
        pause();
        Net.sendMaskedCmd_DSP(id_gobj, "CROSSING_CLOSE_2");
        if (azot) {
//            Terminal.CrossingCell_Hash.values().stream().filter((CrossingCell cc) -> (cc.id_obj == id_obj)).forEach((cc) -> {
            set_manual(false);//0 MANUAL ручноге извещение
//            });
        }
    }

    private void block_onActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION, "<html><font color=red size=5 style=bold> "
                + OPERATION
                + "!!</font><br>Подтвердите операцию\n запрета автоматичеккой подачи\n извещения <font color=red size=5 style=bold>"
                + s_name + "</font></html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "CROSSING_BLOCK_ON_2");
                pause();
                Net.sendMaskedCmd_DSP(id_obj, "CROSSING_BLOCK_ON_1");
        }
    }

    private void block_offActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "CROSSING_BLOCK_OFF_2");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "CROSSING_BLOCK_OFF_1");
    }
    
    private void zg_on_ItemActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION, "<html><font color=red size=5 style=bold> "
                + OPERATION
                + "!!</font><br>Подтвердите операцию\n включения ЗАГРАЖДЕНИЯ <font color=red size=5 style=bold>"
                + s_name + "</font></html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "CROSSING_ZG_ON_2");
                pause();
                Net.sendMaskedCmd_DSP(id_obj, "CROSSING_ZG_ON_1");
        }
    }

    private void zg_off_ItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "CROSSING_ZG_OFF_2");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "CROSSING_ZG_OFF_1");
    }

    // !!! тут нужно добавить индикацию забокирована подача извещения или нет.
    void setState(
            long DTIME,
            boolean CLOSED,//false
            boolean BOOM,//true
            int NOTICE,//3
            int FAULT,//0
            boolean MANUAL,//true
            boolean PEREG,//false
            boolean NOZP,//false
            boolean NETMG,//false
            boolean BLOKED,//false
            boolean PA,//false
            boolean KNB,//false
            boolean KSO//false
    ) {// setState("OPENED","BOOM_OFF","NO_NOTICE","NO_FAULT","NO_MANUAL");
        setDtime(DTIME);
        if (vStatus == 0) {
            if (ruled != 4){

            /*
             * 1 CLOSED Закрыт MESS_CROSSING_CLOSED 208 0 OPENED Открыт
             * MESS_CROSSING_OPENED 216
             */
            if (closed != CLOSED) {
                closed = CLOSED;
                if (CLOSED) {// 1 CLOSED Закрыт MESS_CROSSING_CLOSED 208
                    lblCmdBarrier.setVisible(true);
                    event(208);
                } else {// 0 OPENED Открыт MESS_CROSSING_OPENED 216
                    lblCmdBarrier.setVisible(false);
                    event(216);
                }
            }
            /*
             * 1 BOOM_OFF Выключена заградительная сигнализация
             * MESS_CROSSING_BOOM_OFF 205 0 BOOM_ON Включена заградительная
             * сигнализация MESS_CROSSING_BOOM_ON 206
             */
            // ------------------------------БЛЯ тут опять всё наоборот!--------
            if (boom == BOOM) {
                boom = !BOOM;
                if (BOOM) {// 1 BOOM_OFF Выключена заградительная сигнализация
                    // MESS_CROSSING_BOOM_OFF 205
                    lblCmdBoom.setVisible(false);// false
                    event(205);
                    alarm_off(123);
                } else {// 0 BOOM_ON Включена заградительная сигнализация
                    // MESS_CROSSING_BOOM_ON 206
                    lblCmdBoom.setVisible(boom);// true
                    event(206);
                    alarm_on(123);
                }
            }
            /*
             * 0 DOUBLE_NOTICE Двойное извещение 1 ODD_NOTICE Извещение с
             * "четной" стороны 2 EVEN_NOTICE Извещение с "нечетной" стороны 3
             * NO_NOTICE Нет извещения
             */
            if (notice != NOTICE) {
                notice = NOTICE;
                switch (NOTICE) {// int
                    case 3:// "NO_NOTICE"://3 NO_NOTICE Нет извещения
                        lblCmdLeftNotice.setVisible(false);
                        lblCmdRightNotice.setVisible(false);
                        lblCmdDoubleNotice.setVisible(false);
//                        if (lbl){
//                            lblCmdSName.setOpaque(false);
//                            lblCmdSName.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//                            lblCmdSName.setForeground(Color.BLUE);
//                        }
//                        noticed = false;//ДЛЯ АЗОТ
                        break;
                    case 1:// "ODD_NOTICE"://1 ODD_NOTICE Извещение с "четной"
                        // стороны
                        lblCmdLeftNotice.setVisible(true);
                        lblCmdRightNotice.setVisible(false);
                        lblCmdDoubleNotice.setVisible(false);
//                        noticed = false;//ДЛЯ АЗОТ
//                        if (lbl){
//                            lblCmdSName.setOpaque(false);
//                            lblCmdSName.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//                            lblCmdSName.setForeground(Color.BLUE);
//                        }
                        break;
                    case 2:// "EVEN_NOTICE"://2 EVEN_NOTICE Извещение с "нечетной"
                        // стороны
                        lblCmdLeftNotice.setVisible(false);
                        lblCmdRightNotice.setVisible(true);
                        lblCmdDoubleNotice.setVisible(false);
//                        if (lbl){
//                            lblCmdSName.setOpaque(false);
//                            lblCmdSName.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//                            lblCmdSName.setForeground(Color.BLUE);
//                        }
//                        noticed = false;//ДЛЯ АЗОТ
                        break;
                    case 0:// "DOUBLE_NOTICE"://0 DOUBLE_NOTICE Двойное извещение
                        lblCmdLeftNotice.setVisible(false);
                        lblCmdRightNotice.setVisible(false);
                        lblCmdDoubleNotice.setVisible(true);

//                        if (azot){
//                            noticed = true; //ДЛЯ АЗОТ
//                        } 
                        break;
                    default:
                        // System.err.println("CrossingCell: - xz2 " + notice);
                        break;
                }
            }

            set_manual(MANUAL);

            if (label_show) {
                if (w_bloking_notice) {// если тип переезда = 5 - блокируемая подача извещения
                    if (notice_blok != BLOKED) {
                        notice_blok = BLOKED;
                        if (BLOKED) {// 1 true 339 Запретить подачу извещения MESS_BLOCK_NOTICE_ON
                            if (manual) {
                                lblName.setOpaque(true);
                                lblName.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                                lblName.setForeground(Color.BLUE);
                                lblName.setBackground(Color.YELLOW);
                            }
                            lblCmdWarning.setVisible(true);
                            event(339);
                        } else {// 0 false 338 Разрешить подачу извещения MESS_BLOCK_NOTICE_OFF
                            if (manual) {
                                lblName.setOpaque(false);
                                lblName.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                                lblName.setForeground(Color.BLUE);
                            }
                            lblCmdWarning.setVisible(false);
                            event(338);
                        }
                    }
                }
            }
// -----------------------------------------------------------------------------
            /*
             * 2 CRASH Неисправность MESS_CROSSING_CRASH 209 1 FAULT Авария
             * MESS_CROSSING_FAULT 210 0 NO_FAULT В норме MESS_CROSSING_NO_FAULT
             * 212
             */
            if (fault != FAULT) {
                fault = FAULT;
                switch (FAULT) {// int
                    case 0:// "NO_FAULT"://0 NO_FAULT В норме MESS_CROSSING_NO_FAULT
                        // 212
                        lblCmdCrash.setVisible(false);
                        lblCmdFault.setVisible(false);

                        event(212);
                        alarm_off(124);
                        alarm_off(125);
                        break;
                    case 1:// "FAULT"://1 FAULT Авария MESS_CROSSING_FAULT 210
                        lblCmdCrash.setVisible(false);
                        lblCmdFault.setVisible(true);

                        event(210);
                        alarm_on(125);
                        alarm_off(124);
                        break;
                    case 2:// "CRASH"://2 CRASH Неисправность MESS_CROSSING_CRASH
                        // 209
                        lblCmdCrash.setVisible(true);
                        lblCmdFault.setVisible(false);

                        event(209);
                        alarm_on(124);
                        alarm_off(125);
                        break;
                    default:
                        Log.log("CrossingCell: - xz3 " + FAULT);
//!!!   НУЖНО СДЕЛАТЬ 3 - НЕИСПРАВНОСТЬ СХЕМЫ ПЕРЕЕЗДА
                        break;
                }
            }
            // ------------------------------------------------------------------------------
            if (pereg != PEREG) {
                pereg = PEREG;
                if (PEREG) {// 1
                    event(217);
                    alarm_on(128);
                } else {// 0
                    alarm_off(128);
                }
            }
            // ------------------------------------------------------------------------------
            if (nozp != NOZP) {
                nozp = NOZP;
                if (NOZP) {// 1
                    event(214);
                    alarm_on(127);
                } else {// 0
                    alarm_off(127);
                }
            }
            // -----------------------------------------------------------------
            if (netmg != NETMG) {
                netmg = NETMG;
                if (NETMG) {// 1
                    event(211);
                    alarm_on(126);
                } else {// 0
                    alarm_off(126);
                }
            }
            // -----------------------------------------------------------------
            if (pa != PA) {
                pa = PA;
                if (PA) {// 1
                    event(404);
                    alarm_on(407);
                } else {// 0
                    alarm_off(407);
                }
            }
            // -----------------------------------------------------------------
            if (knb != KNB) {
                knb = KNB;
                if (KNB) {// 1
                    event(405);
                    alarm_on(408);
                } else {// 0
                    alarm_off(408);
                }
            }
            // -----------------------------------------------------------------
            if (kso != KSO) {
                kso = KSO;
                if (KSO) {// 1
                    event(406);
                    alarm_on(409);
                } else {// 0
                    alarm_off(409);
                }
            }
            }//end if ruled;
        }//end if vstatus
    }

    private void set_manual(boolean MANUAL) {
        /*
     * 0 MANUAL Ручное извещение MESS_CROSSIN_MANUAL 219 1 NO_MANUAL Нет
     * ручного извещения MESS_CROSSIN_NO_MANUAL 220
         */

// ----------------------------БЛЯ тут опять всё наоборот!-----------
        if (label_show) {
            if (manual != MANUAL) {
                manual = MANUAL;
                if (manual) {// 1 NO_MANUAL Нет ручного извещения MESS_CROSSIN_NO_MANUAL 220 noticed = false;
                    if (w_bloking_notice && notice_blok) {
                        lblName.setOpaque(true);
                        lblName.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                        lblName.setForeground(Color.BLUE);
                        lblName.setBackground(Color.YELLOW);
//                        lblCmdWarning.setVisible(true);
                    } else {
                        lblName.setOpaque(false);
                        lblName.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        lblName.setForeground(Color.BLUE);
                        event(220);
                    }
                } else {// 0 MANUAL Ручное извещение MESS_CROSSIN_MANUAL 219  noticed = true;

                    lblName.setOpaque(true);
                    lblName.setBorder(BorderFactory.createLineBorder(Color.CYAN));
                    lblName.setForeground(Color.WHITE);
                    lblName.setBackground(new Color(0, 128, 128));
                    event(219);
                }
            }
        }
// ----------------------------------------------------------------            
    }

    private void event(int id_msg) {
        if (getDtime() > 0) {
            if (label_show) {
                Events.InsertMessage(getDtime(), id_obj, id_msg);
            }
        }
    }

    private void alarm_on(int id) {
        if (getDtime() > 0) {
            if (label_show) {
                Alarms.alarm_on(getDtime(), id, id_obj);
            }
        }
    }

    private void alarm_off(int id) {
        if (getDtime() > 0) {
            if (label_show) {
                Alarms.alarm_off(getDtime(), id, id_obj);
            }
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
        CrossingState c = (CrossingState) oid;
        Terminal.Crossings_Hash.values().stream().filter((cc) -> (cc.id_gobj == id_gobj)).forEach((cc) -> {
            if (azot) {
                cc.setState(c.timestamp, c.CLOSED, c.BOOM, c.NOTICE, c.FAULT, cc.manual, c.PEREG, c.NOZP, c.NETMG, c.BLOKED, c.PA, c.KNB, c.KSO);

            } else {
                cc.setState(
                        c.timestamp,
                        c.CLOSED,
                        c.BOOM,
                        c.NOTICE,
                        c.FAULT,
                        c.MANUAL,
                        c.PEREG,
                        c.NOZP,
                        c.NETMG,
                        c.BLOKED,
                        c.PA,
                        c.KNB,
                        c.KSO
                );
            }
        });
    }
}

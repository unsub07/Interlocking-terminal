//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.LightsState_New;
import static terminal.Commander.cmdLayers;

class Lights_New extends Cell {

    // Lights layers
    private static final int LIGHTS_SOUL_LR = 1;
    private static final int LIGHTS_STAND_LR = 2;
    private static final int LIGHTS_LOWER_LIGHT_LR = 3;
    private static final int LIGHTS_UPPER_LIGHT_LR = 4;
    // static final int LIGHTS_LOWER_FAULT_LR = 5;
    // static final int LIGHTS_UPPER_FAULT_LR = 6;
    private static final int LIGHTS_BLOCK_LR = 7;
    private static final int LIGHTS_CONTROL_BUTTON_LR = 8;
    // static final int LIGHTS_TIMER_LR = 9;
    private static final int LIGHTS_LAYER = 9;

    private static final javax.swing.ImageIcon redLight = Terminal.mainPictureHash.get("light_red");
    private static final javax.swing.ImageIcon faultRedLight = Terminal.mainPictureHash.get("light_red_fault");

    //
    private static final javax.swing.ImageIcon blueLight = Terminal.mainPictureHash.get("light_blue");
    private static final javax.swing.ImageIcon faultBlueLight = Terminal.mainPictureHash.get("light_blue_fault");

    //
    private static final javax.swing.ImageIcon yellowLight = Terminal.mainPictureHash.get("light_yellow");
    private static final javax.swing.ImageIcon faultYellowLight = Terminal.mainPictureHash.get("light_yellow_fault");
    private static final javax.swing.ImageIcon yellowBlinkLight = Terminal.mainPictureHash.get("light_yellow_blink");

    //
    private static final javax.swing.ImageIcon greenLight = Terminal.mainPictureHash.get("light_green");
    private static final javax.swing.ImageIcon faultGreenLight = Terminal.mainPictureHash.get("light_green_fault");

    //
    private static final javax.swing.ImageIcon whiteLight = Terminal.mainPictureHash.get("light_white");
    private static final javax.swing.ImageIcon faultWhiteLight = Terminal.mainPictureHash.get("light_white_fault");
    private static final javax.swing.ImageIcon whiteBlinkLight = Terminal.mainPictureHash.get("light_white_blink");
    //
    private static final javax.swing.ImageIcon blackLight = Terminal.mainPictureHash.get("light_black");
    private static final javax.swing.ImageIcon blockLight = Terminal.mainPictureHash.get("light_block");

    private static final javax.swing.ImageIcon futureLight = Terminal.mainPictureHash.get("light_future");
    //
    private static final javax.swing.ImageIcon deprecateLight = Terminal.mainPictureHash.get("light_deprecate");
    private static final javax.swing.ImageIcon lt_f = Terminal.mainPictureHash.get("lt_f");

    private static final Color idleBtnBackground = new Color(225, 225, 225);
    private static final Color pressBtnBackground = Color.green.brighter();
    private static final Color wrongBtnBackground = Color.YELLOW;
    private static final Color routeBtnBackground = Color.GREEN;
    private static final Border idleBtnBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

    private static final Border wrongBtnBorder = BorderFactory.createLineBorder(Color.CYAN, 2);
    private static final Border cancelBtnBorder = BorderFactory.createLineBorder(Color.RED, 2);
    private static final Border pressedBorder = BorderFactory.createLineBorder(Color.ORANGE, 2);
    private static final java.awt.Cursor lightsCursor = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(Terminal.mainPictureHash.get("lights_cursor").getImage(), new java.awt.Point(0, 0), "lights_cursor");
    private final javax.swing.JLayeredPane cmdCellPane = new javax.swing.JLayeredPane();
    // -------------------------------Translate--------------------------------------
    private final String menu_title;

//    private final int vStatus;

    // это TRAIN и предвходной у
    // которого приходящий из сименса
    // сигнал WHITE_BLINK -
    // расшифровывается как YELLOW_BLINK
    private final boolean redProhibit;// true красный, false синий
    private final boolean withInviting;
    private final int control;
    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();
    // блокировка
    private final javax.swing.JMenuItem ltBlockItem = new javax.swing.JMenuItem("<html>блокировка снята, <b><font color=orange>установить</font></b></html>");
    private final javax.swing.JMenuItem ltUnBlockItem = new javax.swing.JMenuItem("<html>блокировка установлена, <b><font color=green>снять</font></b></html>");
    // пригласительный
    private final javax.swing.JMenuItem ltInviteOnItem = new javax.swing.JMenuItem("<html><b><font color=blue>включение</font></b> пригласительного</html>");
    private final javax.swing.JMenuItem ltInviteOffItem = new javax.swing.JMenuItem("<html><b><font color=red>выключение</font></b> пригласительного</html>");
    private final javax.swing.JMenuItem ltInviteRepeateItem = new javax.swing.JMenuItem("<html><b><font color=orange>повтор</font></b> пригласительного</html>");
    // восстановление
    private final javax.swing.JMenuItem ltRecoverItem = new javax.swing.JMenuItem("<html><b><font color=green>восстановление</font></b> (повторное открытие)</html>");
    private final javax.swing.JLabel lblCmdLowerLight = new javax.swing.JLabel();
    private final javax.swing.JLabel lblCmdUpperLight = new javax.swing.JLabel();
    private final javax.swing.JLabel lblCmdLightsName = new javax.swing.JLabel();
    private final javax.swing.JLabel lblCmdLowerBlock = new javax.swing.JLabel();
    private final javax.swing.JLabel lblCmdUpperBlock = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdCrash = new javax.swing.JLabel("<html><b><font color=red>H</font></b></html>");
        private final javax.swing.JLabel lbl_lt_f = new javax.swing.JLabel();

    private final Timer cmdLightsTimer;
    private final boolean trainLights;// is train lights?
    private boolean blocked;// default value
    private boolean routed;// default value
    private boolean canceling;// default value - отмена
    private boolean block_error;// defaul value
    private boolean kmg;//контроь мигания
    private boolean _50;////Перегорание основной нити 1 желтого огня входного Н для Луги

//    private int light = 0;// default value "STOP" or default value "STOP_FAULT"
    // = 256 ?? тут храниться текуший цвет светофора
    private int route = 0;// default value
    private int btnstate = 0;// default value "RELEASE_BTN состояние нажания
    private Border storedBorder;// вроде не должна быть статик

    private int light;
//    boolean l_20; //красный (синий)	запрещ. поездной — красный, маневровый — белый	1-Закрыт
//    boolean l_21; //белый при маневр.	разреш., маневровый (белый)	1-Открыт: Белый
//    boolean l_22; //зеленый	разреш., поездной (зел.)	1-Открыт: Зеленый
//    boolean l_23; //желтый	разреш., поездной (желт.)	1-Открыт: Желтый
//    boolean l_24; //два желтых	разреш., поездной (2 желт.)	1-Открыт: Два желтых
//    boolean l_25; //2 желтых, верхний мигающий	разреш., поездной (2 желт., верхний — миг.)	1-Открыт: 2 желтых, верхний мигающий
//    boolean l_26; //белый мигающий	поездной приглас. (белый миг.)	1-Открыт: Белый мигающий
//    boolean l_27; //два белых		1-Открыт: Два белых
//    boolean l_30; //перегорание лампы красного (синего) огня
//    boolean l_31; //перегорание лампы белого огня
//    boolean l_32; //перегорание лампы зеленого огня
//    boolean l_33; //перегорание резервной нити 1 желтого огня
//    boolean l_34; //перегорание резервной нити 2 желтого огня
//    boolean l_35; //перегорание лампы второго белого огня
//    boolean l_36; //перегорание основной нити 1 желтого огня
//    boolean l_37; //перегорание основной нити 2 желтого огня

//    LightsCell_New(
//    
//  //          int ID_GOBJ,
//            int ID_OBJ,
//            String S_NAME,            
//            int X,
//            int Y,            
//            int VIDEO_STATUS,
//            int ORIENTATION,
////            boolean TRAIN_LIGHT,
//            boolean MOUNT,
//            boolean RED_PROHIBIT,
//            int CONTROL,
//            boolean INVITING,
//            int SHIFT_X,
//            int SHIFT_Y
////            boolean WARN
//    ) {
    Lights_New(
            int ID_OBJ, //0=ID_OBJ
//            int ID_AREA, //1=ID_AREA
            String S_NAME, //2=S_NAME
//            String MPC_NAME, //3=MPC_NAME
            int X, //4=X
            int Y, //5=Y
            int SHIFT_X, //6=SHIFT_X
            int SHIFT_Y, //7=SHIFT_Y
            int VIDEO_STATUS, //8=VIDEO_STATUS
            int ORIENTATION, //9=ORIENTATION
            boolean MOUNT, //10=MOUNT (0-ON_GROUND 1-ON_STAND)
            boolean RED_PROHIBIT, // STOP_LIGHT (0-BLUE, 1-RED)
            int TYPE, // (0-SHUNTING, 1-TRAIN, 2-WARNING)
            //            boolean TRAIN_LIGHT,
            //            boolean WARN            
            int CONTROL, //13=CONTROL (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
            boolean INVITING //14=INVITING
//            int IND, //15=IND
//            int CMD //16=CMD
    ) {    
//        super(GX, GY, ID_GOBJ, ID_OBJ, SHIFT_X, SHIFT_Y);// 11=LIGHTS
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 86);// 11=LIGHTS
        // -------------------------------Translate-----------------------------

        menu_title = "<html><b>" + Util.objType(ID_OBJ) + " <font color=blue>"
                + S_NAME + "</font></b></html>";
//        String l_block_off_menu_item = Terminal.Translate("l_block_off_menu_item",
//                "<html>блокировка снята, <b><font color=orange>установить</font></b></html>");
//        String l_block_on_menu_item = Terminal.Translate("l_block_on_menu_item",
//                "<html>блокировка установлена, <b><font color=green>снять</font></b></html>");
//
//        String l_on_menu_item = Terminal.Translate("l_on_menu_item",
//                "<html><b><font color=blue>включение</font></b> пригласительного</html>");
//        String l_off_menu_item = Terminal.Translate("l_off_menu_item",
//                "<html><b><font color=red>выключение</font></b> пригласительного</html>");
//
//        String l_repeat_menu_item = Terminal.Translate("l_repeat_menu_item",
//                "<html><b><font color=orange>повтор</font></b> пригласительного</html>");
//        String l_recover_menu_item = Terminal.Translate(
//                "l_recover_menu_item",
//                "<html><b><font color=green>восстановление</font></b> (повторное открытие)</html>");

        // Popup.setName(l_menu.replace("?", S_NAME));
        ltBlockItem.setText(l_block_off_menu_item);
        ltUnBlockItem.setText(l_block_on_menu_item);
        ltInviteOnItem.setText(l_on_menu_item);
        ltInviteRepeateItem.setText(l_repeat_menu_item);
        ltInviteOffItem.setText(l_off_menu_item);
        ltRecoverItem.setText(l_recover_menu_item);
        // ------------------------------------------------------------------------------
        


//        vStatus = VIDEO_STATUS;

//        trainLights = TRAIN_LIGHT;
        redProhibit = RED_PROHIBIT;
        control = CONTROL;
        withInviting = INVITING;
        // ------------------------------------------------------------------------------
        int cmdShift;
        
//        vStatus = VIDEO_STATUS;
//        if (TYPE == 1) {trainLights = true;} else {trainLights = false;}
        trainLights = TYPE == 1;
//        trainLights = TRAIN_LIGHT;
//        if (TYPE == 2) {warningLights = true;}// в таблице тип светофора WARNING - это TRAIN и предвходной у которого приходящий из сименса  сигнал WHITE_BLINK - расшифровывается как YELLOW_BLINK
//        boolean warningLights = TYPE == 2;
//        warningLights = WARN;
        lblCmdLightsName.setText(S_NAME);
        lblCmdLightsName.setOpaque(true);
        lblCmdLightsName.setBackground(idleBtnBackground);
        lblCmdLightsName.setBorder(idleBtnBorder);
        lblCmdLightsName.setFont(Terminal.SANS12);
        lblCmdLightsName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblCmdLightsName.setVerticalAlignment(javax.swing.JLabel.CENTER);

        if (Terminal.DSP && vStatus == 0) {// DSP && VIDEO_STATUS
            lblCmdLightsName.setComponentPopupMenu(Popup);
            lblCmdLightsName.setCursor(lightsCursor);
            setLtPopup();
        }// end DSP

        int cmdW = lblCmdLightsName.getFontMetrics(lblCmdLightsName.getFont()).stringWidth(lblCmdLightsName.getText()) + 4;
        if (cmdW <= 38) {
            cmdW = 38;
        }
        lblCmdLightsName.setSize(cmdW, 16);
        cmdShift = ((cmdW - 38) > 0) ? (cmdW - 38) : 0;

        javax.swing.JLabel lblCmdSoul = new javax.swing.JLabel();
        javax.swing.JLabel lblCmdStand = new javax.swing.JLabel();
        // ---------------------------------------------------------------------
        lblCmdLowerLight.setSize(16, 16);
        lblCmdUpperLight.setSize(16, 16);
        lblCmdUpperLight.setIcon(blackLight);

        cmdCellPane.setLayer(cmdCellPane.add(lblCmdLightsName), LIGHTS_CONTROL_BUTTON_LR);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdSoul), LIGHTS_SOUL_LR);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdLowerLight), LIGHTS_LOWER_LIGHT_LR);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdUpperLight), LIGHTS_UPPER_LIGHT_LR);

        // ---------------------------------------------------------------------
        switch (ORIENTATION) {
            // <editor-fold defaultstate="collapsed" desc="case 1: ...">
            case 1:
                shift_x = 46;
                shift_y = -18;
                if (MOUNT) {
                    cmdCellPane.setSize(38 + cmdShift, 58);
                } else {
                    cmdCellPane.setSize(38 + cmdShift, 54);
                }

                lblCmdLightsName.setLocation(0, 0);

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);

                lblCmdSoul.setLocation(0, 18);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(0, 22);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(0, 26);// 26
                } else {
                    lblCmdLowerLight.setLocation(0, 22);
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(0, 42);// 42
                } else {
                    lblCmdUpperLight.setLocation(0, 38);
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 23: ...">
            case 23:
                shift_x = -12 - cmdShift;
                shift_y = -18;
                if (MOUNT) {
                    cmdCellPane.setSize(38 + cmdShift, 58);
                } else {
                    cmdCellPane.setSize(38 + cmdShift, 54);
                }

                lblCmdLightsName.setLocation(0, 0);

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);

                lblCmdSoul.setLocation(22 + cmdShift, 18);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(22 + cmdShift, 22);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(22 + cmdShift, 26);// 26
                } else {
                    lblCmdLowerLight.setLocation(22 + cmdShift, 22);
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(22 + cmdShift, 42);// 42
                } else {
                    lblCmdUpperLight.setLocation(22 + cmdShift, 38);
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 11: ...">
            case 11:
                if (MOUNT) {
                    cmdCellPane.setSize(38 + cmdShift, 58);
                } else {
                    cmdCellPane.setSize(38 + cmdShift, 54);
                }
                shift_x = 46;
                shift_y = 36;

                if (MOUNT) {
                    lblCmdLightsName.setLocation(0, 42);
                } else {
                    lblCmdLightsName.setLocation(0, 38);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);

                if (MOUNT) {
                    lblCmdSoul.setLocation(0, 36);
                } else {
                    lblCmdSoul.setLocation(0, 32);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(0, 32);
                }

                lblCmdLowerLight.setLocation(0, 16);// 26

                lblCmdUpperLight.setLocation(0, 0);// 42
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 13: ...">
            case 13:
                if (MOUNT) {
                    cmdCellPane.setSize(38 + cmdShift, 58);
                } else {
                    cmdCellPane.setSize(38 + cmdShift, 54);
                }
                shift_x = -12 - cmdShift;
                shift_y = 36;

                if (MOUNT) {
                    lblCmdLightsName.setLocation(0, 42);
                } else {
                    lblCmdLightsName.setLocation(0, 38);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);

                if (MOUNT) {
                    lblCmdSoul.setLocation(22 + cmdShift, 36);
                } else {
                    lblCmdSoul.setLocation(22 + cmdShift, 32);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(22 + cmdShift, 32);
                }

                lblCmdLowerLight.setLocation(22 + cmdShift, 16);// 26

                lblCmdUpperLight.setLocation(22 + cmdShift, 0);// 42
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 17: ...">
            case 17:
                if (MOUNT) {
                    cmdCellPane.setSize(80 + cmdShift, 16);
                } else {
                    cmdCellPane.setSize(76 + cmdShift, 16);
                }
                shift_x = -40 - cmdShift;
                shift_y = 46;

                lblCmdLightsName.setLocation(0, 0);

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                lblCmdSoul.setLocation(40 + cmdShift, 0);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(44 + cmdShift, 0);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(48 + cmdShift, 0);// 26
                } else {
                    lblCmdLowerLight.setLocation(44 + cmdShift, 0);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(64 + cmdShift, 0);// 42
                } else {
                    lblCmdUpperLight.setLocation(60 + cmdShift, 0);// 42
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 19: ...">
            case 19:
                if (MOUNT) {
                    cmdCellPane.setSize(80 + cmdShift, 16);
                } else {
                    cmdCellPane.setSize(76 + cmdShift, 16);
                }
                shift_x = -40 - cmdShift;
                shift_y = 10;

                lblCmdLightsName.setLocation(0, 0);

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                lblCmdSoul.setLocation(40 + cmdShift, 0);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(44 + cmdShift, 0);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(48 + cmdShift, 0);// 26
                } else {
                    lblCmdLowerLight.setLocation(44 + cmdShift, 0);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(64 + cmdShift, 0);// 42
                } else {
                    lblCmdUpperLight.setLocation(60 + cmdShift, 0);// 42
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 5: ...">
            case 5:
                if (MOUNT) {
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    shift_x = 32;
                    shift_y = 10;
                } else {
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    shift_x = 36;
                    shift_y = 10;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(42, 0);
                } else {
                    lblCmdLightsName.setLocation(38, 0);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(36, 0);
                } else {
                    lblCmdSoul.setLocation(32, 0);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(32, 0);
                }

                lblCmdLowerLight.setLocation(16, 0);// 26
                lblCmdUpperLight.setLocation(0, 0);// 42
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 7: ...">
            case 7:
                if (MOUNT) {
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    shift_x = 32;
                    shift_y = 46;
                } else {
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    shift_x = 36;
                    shift_y = 46;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(42, 0);
                } else {
                    lblCmdLightsName.setLocation(38, 0);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(36, 0);
                } else {
                    lblCmdSoul.setLocation(32, 0);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(32, 0);
                }

                lblCmdLowerLight.setLocation(16, 0);// 26
                lblCmdUpperLight.setLocation(0, 0);// 42
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 2: ...">
            case 2:
                if (MOUNT) {
                    cmdCellPane.setSize(52 + cmdShift, 54);
                    shift_x = 28 - cmdShift;
                    shift_y = -35;
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = 32 - cmdShift;
                    shift_y = -35;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(13, 0);
                } else {
                    lblCmdLightsName.setLocation(10, 0);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(22 + cmdShift, 16);
                } else {
                    lblCmdSoul.setLocation(18 + cmdShift, 15);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(23 + cmdShift, 23);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(12 + cmdShift, 26);// 10,25
                } else {
                    lblCmdLowerLight.setLocation(12 + cmdShift, 22);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(1 + cmdShift, 37);// 42
                } else {
                    lblCmdUpperLight.setLocation(1 + cmdShift, 33);// 42
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 4: ...">
            case 4:
                if (MOUNT) {
                    cmdCellPane.setSize(71 + cmdShift, 47);
                    shift_x = 54;
                    shift_y = -4;
                } else {
                    cmdCellPane.setSize(67 + cmdShift, 43);
                    shift_x = 58;
                    shift_y = -4;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(33, 0);
                } else {
                    lblCmdLightsName.setLocation(29, 0);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(22, 9);
                } else {
                    lblCmdSoul.setLocation(18, 9);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(23, 17);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(11, 20);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(11, 16);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(0, 31);// 42
                } else {
                    lblCmdUpperLight.setLocation(0, 27);// 42
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 14: ...">
            case 14:
                if (MOUNT) {
                    cmdCellPane.setSize(52 + cmdShift, 54);
                    shift_x = -9;
                    shift_y = 54;
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = -8;
                    shift_y = 58;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(0, 38);
                } else {
                    lblCmdLightsName.setLocation(0, 34);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(14, 22);
                } else {
                    lblCmdSoul.setLocation(14, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(21, 24);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(24, 12);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(21, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(35, 1);// 42
                } else {
                    lblCmdUpperLight.setLocation(32, 0);// 42
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 16: ...">
            case 16:
                if (MOUNT) {
                    cmdCellPane.setSize(74 + cmdShift, 48);
                    shift_x = -54 - cmdShift;
                    shift_y = 29;
                } else {
                    cmdCellPane.setSize(67 + cmdShift, 43);
                    shift_x = -53 - cmdShift;
                    shift_y = 33;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(0, 31);
                } else {
                    lblCmdLightsName.setLocation(0, 27);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(33 + cmdShift, 23);
                } else {
                    lblCmdSoul.setLocation(33 + cmdShift, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(41 + cmdShift, 25);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(44 + cmdShift, 12);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(40 + cmdShift, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(55 + cmdShift, 1);// 42
                } else {
                    lblCmdUpperLight.setLocation(51 + cmdShift, 0);// 42
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 8: ...">
            case 8:
                if (MOUNT) {
                    cmdCellPane.setSize(71 + cmdShift, 47);
                    shift_x = 55;
                    shift_y = 29;
                } else {
                    cmdCellPane.setSize(67 + cmdShift, 43);
                    shift_x = 59;
                    shift_y = 33;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(33, 31);
                } else {
                    lblCmdLightsName.setLocation(29, 27);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(22, 22);
                } else {
                    lblCmdSoul.setLocation(18, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_9"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(23, 23);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(11, 11);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(11, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(0, 0);// 42
                } else {
                    lblCmdUpperLight.setLocation(0, 0);// 42
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 10: ...">
            case 10:
                if (MOUNT) {
                    cmdCellPane.setSize(51 + cmdShift, 54);
                    shift_x = 29 - cmdShift;
                    shift_y = 55;
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = 33 - cmdShift;
                    shift_y = 59;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(13, 38);
                } else {
                    lblCmdLightsName.setLocation(10, 34);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(22 + cmdShift, 22);
                } else {
                    lblCmdSoul.setLocation(18 + cmdShift, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_9"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(23 + cmdShift, 23);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(11 + cmdShift, 11);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(11 + cmdShift, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(cmdShift, 0);// 42
                } else {
                    lblCmdUpperLight.setLocation(cmdShift, 0);// 42
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 20: ...">
            case 20:
                if (MOUNT) {
                    cmdCellPane.setSize(71 + cmdShift, 47);
                    shift_x = -54 - cmdShift;
                    shift_y = -4;
                } else {
                    cmdCellPane.setSize(70 + cmdShift, 43);
                    shift_x = -54 - cmdShift;
                    shift_y = -2;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(0, 0);
                } else {
                    lblCmdLightsName.setLocation(0, 0);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(33 + cmdShift, 10);
                } else {
                    lblCmdSoul.setLocation(34 + cmdShift, 8);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_9"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(41 + cmdShift, 17);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(44 + cmdShift, 20);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(41 + cmdShift, 15);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(55 + cmdShift, 31);// 42
                } else {
                    lblCmdUpperLight.setLocation(52 + cmdShift, 26);// 42
                }
                break;
		// </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 22: ...">
            case 22:
                if (MOUNT) {
                    cmdCellPane.setSize(51 + cmdShift, 54);
                    shift_x = -7;
                    shift_y = -35;
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = -7;
                    shift_y = -35;
                }

                if (MOUNT) {
                    lblCmdLightsName.setLocation(0, 0);
                } else {
                    lblCmdLightsName.setLocation(0, 0);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(13, 16);
                } else {
                    lblCmdSoul.setLocation(13, 16);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_9"
                            + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(21, 23);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(24, 26);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(20, 22);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(35, 36);// 42
                } else {
                    lblCmdUpperLight.setLocation(30, 32);// 42
                }
                break;
            // </editor-fold>
        }
        // ------------------------------------------------------------------------------
        if (VIDEO_STATUS == 1) {// 1 - "FUTURE"
            lblCmdLowerLight.setIcon(futureLight);
            lblCmdUpperLight.setIcon(futureLight);
        }

        if (VIDEO_STATUS == 3) {// 3 - "DEPRECATE"
            lblCmdLowerLight.setIcon(deprecateLight);
            lblCmdUpperLight.setIcon(deprecateLight);
        }
// victor - Если в базе данных координата написана с ошибкой то всё плохо...
        cmdLightsTimer = new Timer(GX, GY, shift_x + lblCmdLightsName.getX(), shift_y + lblCmdLightsName.getY() + ((ORIENTATION == 2) ? (-20) : lblCmdLightsName.getHeight()));

//        lbl_lt_f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 0, 0)), BorderFactory.createLineBorder(Color.RED)));        
        lbl_lt_f.setLocation(GX * Terminal.zoom + shift_x + lblCmdLightsName.getX() - 22, GY * Terminal.zoom + shift_y + lblCmdLightsName.getY() - 22);
        lbl_lt_f.setSize(22, 22);
        lbl_lt_f.setVisible(false);
        lbl_lt_f.setIcon(lt_f);
        lbl_lt_f.setFocusable(false);
        terminal.Commander.cmdLayers.add(lbl_lt_f);
        terminal.Commander.cmdLayers.setLayer(lbl_lt_f, LIGHTS_BLOCK_LR);
        
        lblCmdLowerBlock.setBounds(lblCmdLowerLight.getBounds());
        lblCmdLowerBlock.setIcon(blockLight);
        lblCmdLowerBlock.setVisible(false);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdLowerBlock), LIGHTS_BLOCK_LR);

        lblCmdUpperBlock.setBounds(lblCmdUpperLight.getBounds());
        lblCmdUpperBlock.setIcon(blockLight);
        lblCmdUpperBlock.setVisible(false);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdUpperBlock), LIGHTS_BLOCK_LR);

        lblCmdSoul.setOpaque(false);
        lblCmdLowerLight.setOpaque(false);
        lblCmdUpperLight.setOpaque(false);

        if (!trainLights) {
            lblCmdUpperLight.setEnabled(false);
            lblCmdUpperLight.setVisible(false);
            lblCmdUpperBlock.setEnabled(false);
            lblCmdUpperBlock.setVisible(false);
        }

        cmdX += shift_x;
        cmdY += shift_y;
        cmdCellPane.setLocation(cmdX, cmdY);
        setCmdLayers();
        cmdCellPane.setVisible(!(VIDEO_STATUS == 2));// INVISIBLE

        if (CONTROL == 2) {//        if (CONTROL_TYPE.equals("ONLY_BUTTON")) { (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
            lblCmdUpperLight.setVisible(false);
            lblCmdLowerLight.setVisible(false);

            lblCmdUpperBlock.setVisible(false);
            lblCmdLowerBlock.setVisible(false);

            lblCmdSoul.setVisible(false);
            lblCmdStand.setVisible(false);
        }
        // ----------------------------Set Default State------------------------
        if (redProhibit) {
            lblCmdLowerLight.setIcon(faultRedLight);
        } else {
            lblCmdLowerLight.setIcon(faultBlueLight);
        }
    }

    private void setCmdLayers() {
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, Lights_New.LIGHTS_LAYER);
    }

    private void setState(
            long DTIME,
            int CURTIME,//TIME	Таймер выдержки  (оставшееся время) (сек.)	 0-65535
            int LIGHT,
            boolean L_20, //красный (синий)	запрещ. поездной — красный, маневровый — белый	1-Закрыт
            boolean L_21, //белый при маневр.	разреш., маневровый (белый)	1-Открыт: Белый
            boolean L_22, //зеленый	разреш., поездной (зел.)	1-Открыт: Зеленый
            boolean L_23, //желтый	разреш., поездной (желт.)	1-Открыт: Желтый
            boolean L_24, //два желтых	разреш., поездной (2 желт.)	1-Открыт: Два желтых
            boolean L_25, //2 желтых, верхний мигающий	разреш., поездной (2 желт., верхний — миг.)	1-Открыт: 2 желтых, верхний мигающий
            boolean L_26, //белый мигающий	поездной приглас. (белый миг.)	1-Открыт: Белый мигающий
            boolean L_27, //два белых		1-Открыт: Два белых
            boolean L_30, //перегорание лампы красного (синего) огня
            boolean L_31, //перегорание лампы белого огня
            boolean L_32, //перегорание лампы зеленого огня
            boolean L_33, //перегорание резервной нити 1 желтого огня
            boolean L_34, //перегорание резервной нити 2 желтого огня
            boolean L_35, //перегорание лампы второго белого огня
            boolean L_36, //перегорание основной нити 1 желтого огня
            boolean L_37, //перегорание основной нити 2 желтого огня
            int BTNSTATE,//KN	Нажатие кнопки ERR_SV	"Неправильный светофор"
            int ROUTESTATE,//OP (MP)	"Начало маршрута" VKM	"Конец маршрута" OT	"Отмена маршрута"
            boolean BLOCKSTATE,//BL	Блокировка
            boolean BLOCK_ERROR,
            boolean TIMERSTATE,//VIS_TMR	Показывать таймер
            boolean KI, //
            boolean KMG//KMG	контроль мигания	1-неисправность схемы мигания (нету пока в индикации)
    ) {
        if (vStatus == 0) {
            setDtime(DTIME);
//            System.out.println("L20 "+L_20);
            if (light != LIGHT) {
                light = LIGHT;
                switch (LIGHT) {
                    case 0:// "-"://0
                        break;
                    case 1:// "STOP": //1 STOP Запрещающий огонь MESS_LIGHTS_STOP 259
                        event(259);
                        alarm_off(132);
                        break;
                    case 2:// "WHITE": //2 WHITE белый MESS_LIGHTS_WHITE 263
                        event(263);
                        alarm_off(133);
                        break;
                    case 4:// "GREEN": //4 GREEN зеленый MESS_LIGHTS_GREEN 251
                        event(251);
                        alarm_off(129);
                        break;
                    case 8:// "YELLOW": //8 YELLOW желтый MESS_LIGHTS_YELLOW 266
                        event(266);
                        alarm_off(134);
                        break;
                    case 16:// "DOUBLE_YELLOW": //16 DOUBLE_YELLOW два желтых MESS_LIGHTS_DOUBLE_YELLOW 247
                        event(247);
                        alarm_off(134);
                        alarm_off(131);
                        alarm_off(397);
                        alarm_off(391);                        
                        break;
                    case 32:// "DOUBLE_YELLOW_WITH_BLINK": //32 DOUBLE_YELLOW_WITH_BLINK два желтых мигающих MESS_LIGHTS_DOUBLE_YELLOW_BLINK 248
                        event(248);
                        alarm_off(134);
                        alarm_off(131);
                        break;
                    case 64:// "WHITE_BLINK"://если светофор предвходной то мигать не белым а желтым //64 WHITE_BLINK белый мигающий MESS_LIGHTS_WHITE_BLINK_ON 264
                        event(264);
                        alarm_off(133);
                        break;
                    case 65:// "STOP_WHITE_BLINK": //65 STOP_WHITE_BLINK Запрещающий и белый мигающий MESS_LIGHTS_WHITE_BLINK_ON 264
                        event(264);
                        alarm_off(133);
                        alarm_off(132);
                        break;
                    case 128:// "DOUBLE_WHITE"://128 DOUBLE_WHITE два белых MESS_LIGHTS_DOUBLE_WHITE 246
                        event(246);
                        // нет аларма?
                        break;
                    case 256:// "STOP_FAULT"://256 STOP_FAULT Перегорание запрещающего огня MESS_LIGHTS_STOP_FAULT 260
                        event(260);
                        alarm_on(132);
                        break;
//                    case 320:
//                        event();
//                        alarm_on();
                    case 513:// "WHITE_FAULT"://512 WHITE_FAULT Перегорание лампы белого огня MESS_LIGHTS_WHITE_FAULT 265
                        event(265);
                        alarm_on(133);
                        break;
                    case 1_024:// "GREEN_FAULT"://1024 GREEN_FAULT Перегорание лампы зеленого огня MESS_LIGHTS_GREEN_FAULT 252
                        event(252);
                        alarm_on(129);
                        break;
                    case 2_048:// "YELLOW_FAULT"://2048 YELLOW_FAULT Перегорание лампы желтого огня MESS_LIGHTS_YELLOW_FAULT 267
                        event(267);
                        alarm_on(134);
                        break;
                    case 4_096:// "SECOND_YELLOW_FAULT"://4096 SECOND_YELLOW_FAULT Перег лампа второго желтого ог MESS_LIGHTS_SECOND_YELLOW_FAULT 258
                        event(258);
                        alarm_on(131);
                        break;
                    case 6_144:// "DOUBLE_YELLOW_FAULT"://6144 DOUBLE_YELLOW_FAULT Перегорание двух желтых MESS_LIGHTS_DOUBLE_YELLOW_FAULT 249
                        event(249);
                        // alarm_on(134); or alarm_on(131);
                        break;
                    case 8_192:// "SECOND_WHITE_FAULT": //8192 SECOND_WHITE_FAULT
                        event(257);
                        alarm_on(130);
                        break;
                    default:
                        System.out.println("LIGHT: color light not found" + LIGHT);
                        break;
//                        System.out.println("");
                }
            }
//---------------------------------------------------------------------
// "STOP": //1 STOP Запрещающий огонь MESS_LIGHTS_STOP 259
            if (L_20) {
                lblCmdUpperLight.setIcon(blackLight);
                if (redProhibit) {
                    lblCmdLowerLight.setIcon(redLight);
                } else {
                    lblCmdLowerLight.setIcon(blueLight);
                }
            }

//---------------------------------------------------------------------
// "WHITE": //2 WHITE белый MESS_LIGHTS_WHITE 263
            if (L_21) {
                if (trainLights) {
                    lblCmdUpperLight.setIcon(whiteLight);
                    lblCmdLowerLight.setIcon(blackLight);
                } else {
                    lblCmdLowerLight.setIcon(whiteLight);
                }
            }
//---------------------------------------------------------------------
//                    case 22:// "GREEN": //4 GREEN зеленый MESS_LIGHTS_GREEN 251
            if (L_22) {
                lblCmdUpperLight.setIcon(greenLight);
                lblCmdLowerLight.setIcon(blackLight);
            }
//---------------------------------------------------------------------
//                    case 23:// "YELLOW": //8 YELLOW желтый MESS_LIGHTS_YELLOW 266
            if (L_23) {
                lblCmdUpperLight.setIcon(yellowLight);
                lblCmdLowerLight.setIcon(blackLight);
            }
//---------------------------------------------------------------------
//                    case 24:// "DOUBLE_YELLOW": //16 DOUBLE_YELLOW два желтых MESS_LIGHTS_DOUBLE_YELLOW 247
            if (L_24) {
                lblCmdUpperLight.setIcon(yellowLight);
                lblCmdLowerLight.setIcon(yellowLight);
            }
//---------------------------------------------------------------------
//                    case 25:// "DOUBLE_YELLOW_WITH_BLINK": 32 DOUBLE_YELLOW_WITH_BLINK два желтых мигающих MESS_LIGHTS_DOUBLE_YELLOW_BLINK 248
            if (L_25) {
                lblCmdUpperLight.setIcon(yellowBlinkLight);
                lblCmdLowerLight.setIcon(yellowLight);
            }
//---------------------------------------------------------------------
//                    case 26:// "WHITE_BLINK"://если светофор предвходной то мигать не белым а желтым 64 WHITE_BLINK белый мигающий MESS_LIGHTS_WHITE_BLINK_ON 264

            if (L_26) {
                lblCmdUpperLight.setIcon(whiteBlinkLight);
//                lblCmdLowerLight.setIcon(redLight);//(или погасшим!)
            }
//---------------------------------------------------------------------
//                    case 65:// "STOP_WHITE_BLINK": //65 STOP_WHITE_BLINK Запрещающий и белый мигающий MESS_LIGHTS_WHITE_BLINK_ON 264
//            if (L_20 && L_26) {
//                if (warningLights) {
//                    lblCmdUpperLight.setIcon(yellowBlinkLight);
//                    lblCmdLowerLight.setIcon(blackLight);
//                } else {
//                    lblCmdUpperLight.setIcon(whiteBlinkLight);
//                    lblCmdLowerLight.setIcon(redLight);
//                }
//            }

//---------------------------------------------------------------------
//                    case 27:// "DOUBLE_WHITE"://128 DOUBLE_WHITE два белых MESS_LIGHTS_DOUBLE_WHITE 246
            if (L_27) {
                lblCmdUpperLight.setIcon(whiteLight);
                lblCmdLowerLight.setIcon(whiteLight);
            }
            
            if (!(L_20 | L_21 | L_22 | L_23 | L_24 | L_25 | L_26 | L_27)){
                lblCmdUpperLight.setIcon(blackLight);
            }
//---------------------------------------------------------------------
            if (L_30) {//256
                if (redProhibit) {
//                    lblCmdUpperLight.setIcon(blackLight);
                    lblCmdLowerLight.setIcon(faultRedLight);
                }
            }
//---------------------------------------------------------------------
//                    case 31:// "WHITE_FAULT"://512 WHITE_FAULT Перегорание лампы белого огня MESS_LIGHTS_WHITE_FAULT 265
            if (L_31) {
                if (trainLights) {
                    lblCmdUpperLight.setIcon(faultWhiteLight);
//                    lblCmdLowerLight.setIcon(blackLight);
                } else {
                    lblCmdLowerLight.setIcon(faultWhiteLight);
                }
            }
//---------------------------------------------------------------------
//                    case 32:// "GREEN_FAULT"://1024 GREEN_FAULT Перегорание лампы зеленого огня MESS_LIGHTS_GREEN_FAULT 252
            if (L_32) {
                lblCmdUpperLight.setIcon(faultGreenLight);
                lblCmdLowerLight.setIcon(blackLight);
            }
//---------------------------------------------------------------------
//            if (L_33 && L_34) {
//                    case 6_144:// "DOUBLE_YELLOW_FAULT"://6144 DOUBLE_YELLOW_FAULT Перегорание двух желтых MESS_LIGHTS_DOUBLE_YELLOW_FAULT 249
//                        lblCmdUpperLight.setIcon(blackLight);
//                        lblCmdLowerLight.setIcon(blackLight);
//                            lblCmdUpperLight.setIcon(faultYellowLight);
//                            lblCmdLowerLight.setIcon(faultYellowLight);
////                        event(249);
//                        alarm_on(134);
//                        alarm_on(131);
//            }
//---------------------------------------------------------------------
//                    case 33:// "YELLOW_FAULT"://2048 YELLOW_FAULT Перегорание лампы желтого огня MESS_LIGHTS_YELLOW_FAULT 267
            if (L_33) {
                lblCmdUpperLight.setIcon(faultYellowLight);
//                        lblCmdLowerLight.setIcon(blackLight);
            }
//---------------------------------------------------------------------
//                    case 34:// "SECOND_YELLOW_FAULT"://4096 SECOND_YELLOW_FAULT //ОТКАЗ! Перегорание лампы второго желтого огня Перег лампа второго желтого ог MESS_LIGHTS_SECOND_YELLOW_FAULT 258
            if (L_34) {
//                        lblCmdUpperLight.setIcon(blackLight);
                lblCmdLowerLight.setIcon(faultYellowLight);
            }
//---------------------------------------------------------------------
//                    case 35:// "SECOND_WHITE_FAULT": //8192 SECOND_WHITE_FAULT //ОТКАЗ! Перегорание лампы второго белого огня перегор. верх. белого MESS_LIGHTS_SECOND_WHITE_FAULT 257
            if (L_35) {
                lblCmdLowerLight.setIcon(faultWhiteLight);
            }
//---------------------------------------------------------------------
//                    case 36://НЕИСПРАВНОСТЬ! Перегорание основной нити желтого огня
            if (L_36) {
                lblCmdLowerLight.setIcon(yellowLight);
                event(395);//395	<html><font color=red>НЕИСПРАВНОСТЬ! Перегорание основной нити желтого огня</html>	MESS_LIGHT_YOO
                alarm_on(391);//391	Перегорание основной нити желтого огня	ALARM_LIGHT_YOO
            }
//---------------------------------------------------------------------
//                    case 37://НЕИСПРАВНОСТЬ! Перегорание основной нити второго желтого огня
            if (L_37) {
                lblCmdUpperLight.setIcon(yellowLight);
                event(398);//НЕИСПРАВНОСТЬ! Перегорание основной нити второго желтого огня
                alarm_on(397);//Перегорание основной нити втого желтого огня
            }
//---------------------------------------------------------------------


            /*------------------------------------------------------------------------------
             0	-
             1	ROUTE_BEGIN	Начало маршрута	MESS_LIGHTS_ROUTE_BEGIN         254
             2	ROUTE_END	Конеч маршрута	MESS_LIGHTS_ROUTE_END           256
             4	ROUTE_CANCEL	Отмена маршрута	MESS_LIGHTS_ROUTE_CANCEL	255
             */
            if (route != ROUTESTATE) {
                route = ROUTESTATE;
                switch (ROUTESTATE) {
                    case 0:// "-": //0
                        routed = false;
                        canceling = false;
                        if (BTNSTATE != 1) {// 1 PRESS_BTN
                            lblCmdLightsName.setBorder(idleBtnBorder);
                            lblCmdLightsName.setBackground(idleBtnBackground);
                        }
                        break;
                    case 1:// "ROUTE_BEGIN": //1 ROUTE_BEGIN Начало маршрута
                        // MESS_LIGHTS_ROUTE_BEGIN 254
                        routed = true;
                        canceling = false;
                        event(254);
                        lblCmdLightsName.setBackground(routeBtnBackground); // green
                        break;
                    case 2:// "ROUTE_END": //2 ROUTE_END Конец маршрута
                        // MESS_LIGHTS_ROUTE_END 256
                        routed = true;
                        canceling = false;
                        lblCmdLightsName.setBackground(routeBtnBackground); // green
                        event(256);
                        break;
                    case 4:// "ROUTE_CANCEL": //4 ROUTE_CANCEL Отмена маршрута
                        // MESS_LIGHTS_ROUTE_CANCEL 255
                        lblCmdLightsName.setBorder(cancelBtnBorder); //
                        routed = false;
                        canceling = true;
                        event(255);
                        break;
                    default:
                        routed = false;
                        canceling = false;
                }
            }
            /*------------------------------------------------------------------------------
             0	RELEASE_BTN
             1	PRESS_BTN
             2	WRONG_BTN	Неправильный светофор	MESS_LIGHTS_ERR_BTN	250
             */
            if (btnstate != BTNSTATE) {
                btnstate = BTNSTATE;
                if (control != 1) {//        if (CONTROL_TYPE.equals("ONLY_LIGHTS")) { (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
                    // btnState = currBtnState;
                    switch (BTNSTATE) {// int
                        case 1:// "PRESS_BTN":// 1 PRESS_BTN
                            lblCmdLightsName.setBackground(pressBtnBackground); // green
                            break;
                        case 0:// "RELEASE_BTN": //0 RELEASE_BTN
                            if (!routed) {
                                lblCmdLightsName.setBackground(idleBtnBackground); // default
                            }
                            break;
                        case 2:// "WRONG_BTN": //2 WRONG_BTN Неправельный светофор
                            // MESS_LIGHTS_ERR_BTN 250
                            lblCmdLightsName.setBackground(wrongBtnBackground);
                            lblCmdLightsName.setBorder(wrongBtnBorder);
                            event(250);
                            break;
                        default:
                            lblCmdLightsName.setBackground(idleBtnBackground);
                            break;
                    }
                }
            }
            // ------------------------------------------------------------------------------
            /*
             * 1 BLOCK Блокирован MESS_LIGHTS_BLOCK 244 0 UNBLOCK Разблокирован
             * MESS_LIGHTS_UNBLOCK 262
             */
            if (control != 2) {//        if (CONTROL_TYPE.equals("ONLY_BUTTON")) { (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
                if (blocked != BLOCKSTATE) {
                    blocked = BLOCKSTATE;
                    if (BLOCKSTATE) {// true 1 BLOCK Блокирован
                        // MESS_LIGHTS_BLOCK 244
                        if (trainLights) {
                            lblCmdUpperBlock.setVisible(true);
                        } else {
                            lblCmdLowerBlock.setVisible(true);
                        }
                        event(244);
                    } else {// false case "UNBLOCK": //0 UNBLOCK Разблокирован
                        // MESS_LIGHTS_UNBLOCK 262
                        if (trainLights) {
                            lblCmdUpperBlock.setVisible(false);
                        } else {
                            lblCmdLowerBlock.setVisible(false);
                        }
                        event(262);
                    }
                }
            }
            /*
             * 0 BLOCK_NO_ERROR //?????????? 1 BLOCK_ERROR Ошибка блокировки
             * MESS_LIGHTS_BLOCK_ERR 245
             */
            if (block_error != BLOCK_ERROR) {
                block_error = BLOCK_ERROR;
                if (block_error) {// true 1 BLOCK_ERROR Ошибка блокировки
                    // MESS_LIGHTS_BLOCK_ERR 245
                    event(245);
                }
                // else {//false
                // 0 BLOCK_NO_ERROR
                // }
            }
            // ----------------------------------------------------------------
            if (kmg != KMG) {
                kmg = KMG;
                if (KMG) {// true 1 KMG Неисправность схемы мигания ALARM_LIGHTS_KMG
                    event(347);
                    alarm_on(347);
                } else {//false
                    alarm_off(347);//Неисправность схемы мигания
                }
            }
            /*
             * 0 TIMER_OFF 1 TIMER_ON
             */
            if (control != 1) {//        if (CONTROL_TYPE.equals("ONLY_LIGHTS")) { (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
                // if (timerOn != TIMERSTATE) {
                if (TIMERSTATE) {// true 1 TIMER_ON
                    if ((CURTIME) != 0) {
                        // cmdLightsTimer.setState(CURTIME >> 8 + (CURTIME & 0x0000_00ff << 8), canceling);//Old Siemens from DB
                        // cmdLightsTimer.setState(CURTIME, canceling);//HIMA
                        cmdLightsTimer.setState(CURTIME, canceling);
                    } else { // false 0 TIMER_OFF
                        cmdLightsTimer.setState(0, canceling);
                    }
                } else { // false 0 TIMER_OFF
                    cmdLightsTimer.setState(0, canceling);
                }
            }
            // ----------------------------------------------------------------
            if (this._50 != KI) {
                this._50 = KI;
                if (KI) {//
                    lbl_lt_f.setVisible(true);
                    event(388);//неисправность в шкафу входного светофора
                    alarm_on(388);//неисправность в шкафу входного светофора
                } else {//false
                    lbl_lt_f.setVisible(false);
                    alarm_off(388);//неисправность в шкафу входного светофора
                }
            }
            // ----------------------------------------------------------------
//            if (this._51 != _51) {
//                this._51 = _51;
//                if (_51) {//
////                    event(Перегорание основной нити 2 желтого огня)
////                    alarm_on(Перегорание основной нити 2 желтого огня);
//                } else {//false
////                    alarm_off(Перегорание основной нити 2 желтого огня);
//                }
//            }
        }// end if v_status
    }

    // =========================================================================
    private boolean ltBlockItemCanBeEnabled() {
        return (!blocked && (light == 1 || light == 256));// STOP or STOP_FAULT
    }

    private boolean ltInviteOnItemCanBeEnabled() {
        return light != 65;// STOP_WHITE_BLINK
    }

    private boolean ltInviteRepeateItemCanBeEnabled() {
        return light == 65;// STOP_WHITE_BLINK
    }

    private boolean ltInviteOffItemCanBeEnabled() {
        return light == 65;// STOP_WHITE_BLINK
    }

    private boolean ltRecoverItemCanBeEnabled() {
        return light == 1 || light == 256;// STOP or STOP_FAULT
    }

    // ------------------------------------------------------------------------------
    // подготовка меню и кнопочных команд
    private void setLtPopup() {
        if (control != 1) {//        if (CONTROL_TYPE.equals("ONLY_LIGHTS")) { (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
            Popup.setBorder(BorderFactory.createTitledBorder(menu_title));
            Popup.setBorderPainted(true);

            Popup.add(ltRecoverItem);

            ltRecoverItem.addActionListener((java.awt.event.ActionEvent e) -> ltRecoverItemMousePressed());

            Popup.addSeparator();
            Popup.add(ltBlockItem);
            ltBlockItem.addActionListener((java.awt.event.ActionEvent evt) -> ltBlockItemActionPerformed());
            Popup.add(ltUnBlockItem);
            ltUnBlockItem.addActionListener((java.awt.event.ActionEvent evt) -> ltUnBlockItemActionPerformed());

            if (withInviting) {
                Popup.addSeparator();
                Popup.add(ltInviteOnItem);
                ltInviteOnItem.addActionListener((java.awt.event.ActionEvent evt) -> ltInviteOnItemActionPerformed());
                Popup.add(ltInviteRepeateItem);
                ltInviteRepeateItem.addActionListener((java.awt.event.ActionEvent evt) -> ltInviteRepeateItemActionPerformed());
                Popup.add(ltInviteOffItem);
                ltInviteOffItem.addActionListener((java.awt.event.ActionEvent evt) -> ltInviteOffItemActionPerformed());
            }

            Popup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    preparePopup();
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    // throw new
                    // UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    // throw new
                    // UnsupportedOperationException("Not supported yet.");
                }
            });

            lblCmdLightsName
                    .addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                            lblCmdLightNameMousePressed(evt);
                        }

                        @Override
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            lblCmdLightNameMouseReleased(evt);
                        }
                    });
        }
    }

    private void lblCmdLightNameMousePressed(java.awt.event.MouseEvent evt) {
        if (Net.logged) {

            if (evt.getButton() == MouseEvent.BUTTON1) {
                storedBorder = lblCmdLightsName.getBorder();
                lblCmdLightsName.setBorder(pressedBorder);

////---------------------- нажатие кнопки маневрового маршрута -----------------
//                for (AreaNameCell ar : Terminal.Area_Hash.values()) {
//                    if (!ar.withTrainRouting && !ar.withShuntingRouting && ar.groupCancel){
//                        ar.btnShuntingRouteMousePressed();
//                    }
//                }
//                pause(50);
////-----------------------------------------------------------------------
                Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BTN_PRESSED");
            }
        }
    }

    private void lblCmdLightNameMouseReleased(java.awt.event.MouseEvent evt) {
        pause();
        if (Net.logged) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
                lblCmdLightsName.setBorder(storedBorder);
                Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BTN_RELEASED");
            }
        }
    }

    private void ltRecoverItemMousePressed() {
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_RECOVER_PHASE1");//8.8
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_RECOVER_PHASE2");//8.0
    }

    private void ltBlockItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BLOCK_ON");
    }

    private void ltUnBlockItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BLOCK_OFF");
    }

    private void ltInviteOnItemActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=4 style=bold> "
                + OPERATION
                + "!!</font><br> Подвердите включение пригласительного огня<br> на светофоре <font color=red size=4 style=bold>"
                + lblCmdLightsName.getText() + "</font></html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "FIRST_LIGHTS_INVITE_ON");
                pause();
                Net.sendMaskedCmd_DSP(id_obj, "REPEAT_LIGHTS_INVITE_ON_PHASE1");
                break;
        }
    }

    private void ltInviteRepeateItemActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=4 style=bold> "
                + OPERATION
                + "!!</font><br>Подвердите продление действия пригласительного огня<br> на светофоре <font color=red size=4 style=bold>"
                + lblCmdLightsName.getText() + "</font></html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "REPEAT_LIGHTS_INVITE_ON_PHASE2");
                pause();
                Net.sendMaskedCmd_DSP(id_obj, "REPEAT_LIGHTS_INVITE_ON_PHASE1");
                break;
        }
    }

    private void ltInviteOffItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_INVITE_OFF_PHASE1");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "REPEAT_LIGHTS_INVITE_OFF");
    }

    private void preparePopup() {// подготовка опций перед включением
        ltBlockItem.setEnabled(ltBlockItemCanBeEnabled() && Net.logged);
        ltUnBlockItem.setEnabled(blocked && Net.logged);
        if (withInviting) {
            ltInviteOnItem.setEnabled(ltInviteOnItemCanBeEnabled()
                    && Net.logged);
            ltInviteRepeateItem.setEnabled(ltInviteRepeateItemCanBeEnabled()
                    && Net.logged);
            ltInviteOffItem.setEnabled(ltInviteOffItemCanBeEnabled()
                    && Net.logged);
        }
        ltRecoverItem.setEnabled(ltRecoverItemCanBeEnabled() && Net.logged);
// -----------------------------------------------------------------------------
        // ltBlockItem.setEnabled(true);
        // ltUnBlockItem.setEnabled(true);
        // if (withInviting) {
        // ltInviteOnItem.setEnabled(true);
        // ltInviteRepeateItem.setEnabled(true);
        // ltInviteOffItem.setEnabled(true);
        // }
        // ltRecoverItem.setEnabled(true);
// -----------------------------------------------------------------------------
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
        LightsState_New l = (LightsState_New) oid;
        this.setState(
                l.timestamp,
                l.CURTIME, //TIME	Таймер выдержки  (оставшееся время) (сек.)	 0-65535
                l.LIGHT,
                l.L_20, //красный (синий)	запрещ. поездной — красный, маневровый — белый	1-Закрыт
                l.L_21, //белый при маневр.	разреш., маневровый (белый)	1-Открыт: Белый
                l.L_22, //зеленый	разреш., поездной (зел.)	1-Открыт: Зеленый
                l.L_23, //желтый	разреш., поездной (желт.)	1-Открыт: Желтый
                l.L_24, //два желтых	разреш., поездной (2 желт.)	1-Открыт: Два желтых
                l.L_25, //2 желтых, верхний мигающий	разреш., поездной (2 желт., верхний — миг.)	1-Открыт: 2 желтых, верхний мигающий
                l.L_26, //белый мигающий	поездной приглас. (белый миг.)	1-Открыт: Белый мигающий
                l.L_27, //два белых		1-Открыт: Два белых
                l.L_30, //перегорание лампы красного (синего) огня
                l.L_31, //перегорание лампы белого огня
                l.L_32, //перегорание лампы зеленого огня
                l.L_33, //перегорание резервной нити 1 желтого огня
                l.L_34, //перегорание резервной нити 2 желтого огня
                l.L_35, //перегорание лампы второго белого огня
                l.L_36, //перегорание основной нити 1 желтого огня
                l.L_37, //перегорание основной нити 2 желтого огня
                l.BTNSTATE, // Prohibit.... red, blue, or white - etc.......
                l.ROUTESTATE, //OP (MP)	"Начало маршрута" VKM	"Конец маршрута" OT	"Отмена маршрута"
                l.BLOCKSTATE, //BL	Блокировка
                l.BLOCK_ERROR,//"Ошибка блокировки"		"Блокировка невозможна" ??????
                l.TIMERSTATE, //VIS_TMR	Показывать таймер
                l.KI, //неисправность в шкафу входного светофора		1-неисправность в шкафу входного светофора
                l.KMG //KMG	контроль мигания	1-неисправность схемы мигания
        );
    }
}
